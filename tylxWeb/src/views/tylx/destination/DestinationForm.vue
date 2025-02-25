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
        <el-input v-model="formData.detail" type="textarea" placeholder="请输入目的地描述" />
      </el-form-item>
    </el-form>
    <!-- 子表的表单 -->
    <el-tabs v-model="subTabsName">
      <el-tab-pane label="特色美食" name="destinationFood">
        <DestinationFoodForm ref="destinationFoodFormRef" :destination-id="formData.id" />
      </el-tab-pane>
      <el-tab-pane label="热门景点" name="destinationScenery">
        <DestinationSceneryForm ref="destinationSceneryFormRef" :destination-id="formData.id" />
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { DestinationApi, DestinationVO } from '@/api/tylx/destination'
import DestinationFoodForm from './components/DestinationFoodForm.vue'
import DestinationSceneryForm from './components/DestinationSceneryForm.vue'

/** 目的地 表单 */
defineOptions({ name: 'DestinationForm' })

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
  detail: undefined
})
const formRules = reactive({
})
const formRef = ref() // 表单 Ref

/** 子表的表单 */
const subTabsName = ref('destinationFood')
const destinationFoodFormRef = ref()
const destinationSceneryFormRef = ref()

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
      formData.value = await DestinationApi.getDestination(id)
    } finally {
      formLoading.value = false
    }
  }
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 校验子表单
  try {
    await destinationFoodFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'destinationFood'
    return
  }
  try {
    await destinationSceneryFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'destinationScenery'
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as DestinationVO
    // 拼接子表的数据
    data.destinationFoods = destinationFoodFormRef.value.getData()
    data.destinationScenerys = destinationSceneryFormRef.value.getData()
    if (formType.value === 'create') {
      await DestinationApi.createDestination(data)
      message.success(t('common.createSuccess'))
    } else {
      await DestinationApi.updateDestination(data)
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
    detail: undefined
  }
  formRef.value?.resetFields()
}
</script>