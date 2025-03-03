<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="目的地名称" prop="name">
        <el-input v-model="formData.name" placeholder="请输入目的地名称" />
      </el-form-item>
      <el-form-item label="首页图片" prop="picture">
        <UploadImg v-model="formData.picture" />
      </el-form-item>
      <el-form-item label="目的地描述" prop="detail">
        <el-input v-model="formData.detail" placeholder="请输入目的地描述" />
      </el-form-item>
      <el-form-item label="父级编号" prop="parentId">
        <el-tree-select
          v-model="formData.parentId"
          :data="destination2Tree"
          :props="defaultProps"
          check-strictly
          default-expand-all
          placeholder="请选择父级编号"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { Destination2Api, Destination2VO } from '@/api/tylx/destination2'
import { defaultProps, handleTree } from '@/utils/tree'

/** 目的地树表 表单 */
defineOptions({ name: 'Destination2Form' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  name: undefined,
  picture: undefined,
  detail: undefined,
  parentId: undefined
})
const formRules = reactive({
})
const formRef = ref() // 表单 Ref
const destination2Tree = ref() // 树形结构

/** 打开弹窗 */
const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  // 修改时，设置数据
  if (id) {
    formLoading.value = true
    try {
      formData.value = await Destination2Api.getDestination2(id)
    } finally {
      formLoading.value = false
    }
  }
  await getDestination2Tree()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as Destination2VO
    if (formType.value === 'create') {
      await Destination2Api.createDestination2(data)
      message.success(t('common.createSuccess'))
    } else {
      await Destination2Api.updateDestination2(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = () => {
  formData.value = {
    id: undefined,
    name: undefined,
    picture: undefined,
    detail: undefined,
    parentId: undefined
  }
  formRef.value?.resetFields()
}

/** 获得目的地树表树 */
const getDestination2Tree = async () => {
  destination2Tree.value = []
  const data = await Destination2Api.getDestination2List()
  const root: Tree = { id: 0, name: '顶级目的地树表', children: [] }
  root.children = handleTree(data, 'id', 'parentId')
  destination2Tree.value.push(root)
}
</script>