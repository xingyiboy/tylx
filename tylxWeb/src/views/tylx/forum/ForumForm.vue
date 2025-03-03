<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      v-loading="formLoading"
    >
      <el-form-item label="首页图片" prop="picture">
        <MultipleUpload v-model="formData.picture" :limit="9" />
      </el-form-item>
      <el-form-item label="首页标题" prop="title">
        <el-input v-model="formData.title" placeholder="请输入首页标题" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <Editor v-model="formData.content" height="150px" />
      </el-form-item>
      <el-form-item label="简介" prop="introduce">
        <el-input v-model="formData.introduce" type="textarea" placeholder="请输入简介" />
      </el-form-item>
      <el-form-item label="用户编号" prop="userId">
        <el-input v-model="formData.userId" placeholder="请输入用户编号" />
      </el-form-item>
      <el-form-item label="目的地编号" prop="destinationId">
        <el-input v-model="formData.destinationId" placeholder="请输入目的地编号" />
      </el-form-item>
      <el-form-item label="阅读量" prop="view">
        <el-input v-model="formData.view" placeholder="请输入阅读量" />
      </el-form-item>
      <el-form-item label="点赞量" prop="likes">
        <el-input v-model="formData.likes" placeholder="请输入点赞量" />
      </el-form-item>
      <el-form-item label="评论量" prop="comment">
        <el-input v-model="formData.comment" placeholder="请输入评论量" />
      </el-form-item>
      <el-form-item label="是否精选" prop="isHome">
        <el-select v-model="formData.isHome" placeholder="请选择是否精选">
          <el-option
            v-for="dict in getIntDictOptions(DICT_TYPE.IS_HOME)"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
    </el-form>
    <!-- 子表的表单 -->
    <el-tabs v-model="subTabsName">
      <el-tab-pane label="论坛评论" name="forumComment">
        <ForumCommentForm ref="forumCommentFormRef" :forum-id="formData.id" />
      </el-tab-pane>
      <el-tab-pane label="论坛点赞" name="forumLikes">
        <ForumLikesForm ref="forumLikesFormRef" :forum-id="formData.id" />
      </el-tab-pane>
      <el-tab-pane label="论坛打赏" name="forumReward">
        <ForumRewardForm ref="forumRewardFormRef" :forum-id="formData.id" />
      </el-tab-pane>
    </el-tabs>
    <template #footer>
      <el-button @click="submitForm" type="primary" :disabled="formLoading">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { ForumApi, ForumVO } from '@/api/tylx/forum'
import ForumCommentForm from './components/ForumCommentForm.vue'
import ForumLikesForm from './components/ForumLikesForm.vue'
import ForumRewardForm from './components/ForumRewardForm.vue'
import MultipleUpload from '@/components/MultipleUpload/index.vue'

/** 论坛 表单 */
defineOptions({ name: 'ForumForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  picture: undefined,
  title: undefined,
  content: undefined,
  introduce: undefined,
  userId: undefined,
  destinationId: undefined,
  view: undefined,
  likes: undefined,
  comment: undefined,
  isHome: undefined
})
const formRules = reactive({})
const formRef = ref() // 表单 Ref

/** 子表的表单 */
const subTabsName = ref('forumComment')
const forumCommentFormRef = ref()
const forumLikesFormRef = ref()
const forumRewardFormRef = ref()

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
      formData.value = await ForumApi.getForum(id)
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
    await forumCommentFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'forumComment'
    return
  }
  try {
    await forumLikesFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'forumLikes'
    return
  }
  try {
    await forumRewardFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'forumReward'
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as ForumVO
    // 拼接子表的数据
    data.forumComments = forumCommentFormRef.value.getData()
    data.forumLikess = forumLikesFormRef.value.getData()
    data.forumRewards = forumRewardFormRef.value.getData()
    if (formType.value === 'create') {
      await ForumApi.createForum(data)
      message.success(t('common.createSuccess'))
    } else {
      await ForumApi.updateForum(data)
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
    picture: undefined,
    title: undefined,
    content: undefined,
    introduce: undefined,
    userId: undefined,
    destinationId: undefined,
    view: undefined,
    likes: undefined,
    comment: undefined,
    isHome: undefined
  }
  formRef.value?.resetFields()
}
</script>
