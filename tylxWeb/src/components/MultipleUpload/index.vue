<template>
  <div class="multiple-upload">
    <el-upload
      v-model:file-list="fileList"
      :action="UPLOAD_URL"
      list-type="picture-card"
      :multiple="true"
      :limit="limit"
      :headers="headers"
      :before-upload="beforeUpload"
      :on-success="handleSuccess"
      :on-remove="handleRemove"
      :on-exceed="handleExceed"
      :on-progress="handleProgress"
      :on-preview="handlePreview"
    >
      <el-icon>
        <Plus />
      </el-icon>
      <template #tip>
        <div class="el-upload__tip">支持 jpg/png 文件，单个文件不超过 10MB</div>
      </template>
    </el-upload>

    <!-- 图片预览组件 -->
    <el-image-viewer
      v-if="showViewer"
      :url-list="[previewUrl]"
      :initial-index="0"
      @close="showViewer = false"
    />
  </div>
  <!-- 调用示例
   import MultipleUpload from '@/components/MultipleUpload/index.vue'
   <MultipleUpload v-model="formData.picture" :limit="5" />
   
   -->
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getAccessToken } from '@/utils/auth'
import { updateFile } from '@/api/infra/file'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  },
  limit: {
    type: Number,
    default: 5
  }
})

const emit = defineEmits(['update:modelValue'])

const UPLOAD_URL = import.meta.env.VITE_BASE_URL + '/admin-api/infra/file/upload'
const headers = {
  Authorization: 'Bearer ' + getAccessToken()
}

const fileList = ref([])

// 监听 modelValue 变化，更新文件列表
watch(
  () => props.modelValue,
  (newVal) => {
    if (!newVal) {
      fileList.value = []
      return
    }
    const urls = newVal.split(',')
    fileList.value = urls.map((url) => ({
      url,
      name: url.split('/').pop()
    }))
  },
  { immediate: true }
)

// 上传前验证
const beforeUpload = (file: File) => {
  // 验证文件类型
  const isImage = ['image/jpeg', 'image/png'].includes(file.type)
  if (!isImage) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片！')
    return false
  }

  // 验证文件大小（10MB）
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    ElMessage.error('图片大小不能超过 10MB！')
    return false
  }

  return true
}

// 上传成功回调
const handleSuccess = (response: any, uploadFile: any) => {
  if (response.code === 0) {
    const newFileList = fileList.value.map((file) => {
      if (file.uid === uploadFile.uid) {
        return {
          ...file,
          url: response.data
        }
      }
      return file
    })
    fileList.value = newFileList
    updateModelValue()
  } else {
    ElMessage.error('上传失败：' + response.msg)
  }
}

// 删除图片回调
const handleRemove = () => {
  updateModelValue()
}

// 超出限制回调
const handleExceed = () => {
  ElMessage.warning(`最多只能上传 ${props.limit} 张图片！`)
}

// 更新 modelValue
const updateModelValue = () => {
  const urls = fileList.value.filter((file) => file.url).map((file) => file.url)
  emit('update:modelValue', urls.join(','))
}

// 图片预览相关
const showViewer = ref(false)
const previewUrl = ref('')

// 处理图片预览
const handlePreview = (file: any) => {
  previewUrl.value = file.url
  showViewer.value = true
}
</script>

<style lang="scss" scoped>
.multiple-upload {
  :deep(.el-upload--picture-card) {
    width: 100px;
    height: 100px;
    line-height: 100px;
  }

  :deep(.el-upload-list--picture-card) {
    .el-upload-list__item {
      width: 100px;
      height: 100px;
    }
  }

  .el-upload__tip {
    font-size: 12px;
    color: #909399;
    margin-top: 8px;
  }
}

// 可以添加一些预览相关的样式
:deep(.el-image-viewer__wrapper) {
  .el-image-viewer__close {
    color: #fff;
  }
}
</style>