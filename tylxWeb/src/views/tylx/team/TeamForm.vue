<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible">
    <el-form ref="formRef" :model="formData" :rules="formRules" label-width="100px" v-loading="formLoading">
      <el-form-item label="目的地" prop="destinationId">
        <el-select v-model="formData.destinationId" filterable placeholder="请选择目的地" clearable class="!w-240px">
          <el-option v-for="item in destinationOptions" :key="item.id" :label="item.name" :value="item.id">
            <span>{{ item.name }}</span>
            <span class="text-gray-400 ml-2">(ID: {{ item.id }})</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户" prop="userId">
        <el-select v-model="formData.userId" filterable placeholder="请选择用户" clearable class="!w-240px">
          <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id">
            <span>{{ item.nickname }}</span>
            <span class="text-gray-400 ml-2">(ID: {{ item.id }})</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="简介" prop="detail">
        <el-input v-model="formData.detail" type="textarea" placeholder="请输入简介" />
      </el-form-item>
      <el-form-item label="内容" prop="content">
        <Editor v-model="formData.content" height="150px" />
      </el-form-item>
      <el-form-item label="状态 0招募中 1招募结束" prop="status">
        <el-select v-model="formData.status" placeholder="请选择状态 0招募中 1招募结束">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.TYLX_TEAM_STATUS)" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="详细地址" prop="area">
        <el-input v-model="formData.area" placeholder="请输入详细地址" />
      </el-form-item>
      <el-form-item label="最大组队数" prop="maxNumber">
        <el-input v-model="formData.maxNumber" placeholder="请输入最大组队数" />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker v-model="formData.startTime" type="date" value-format="x" placeholder="选择开始时间" />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker v-model="formData.endTime" type="date" value-format="x" placeholder="选择结束时间" />
      </el-form-item>
    </el-form>
    <!-- 子表的表单 -->
    <el-tabs v-model="subTabsName">
      <el-tab-pane label="队伍聊天" name="teamChat">
        <TeamChatForm ref="teamChatFormRef" :team-id="formData.id" />
      </el-tab-pane>
      <el-tab-pane label="队伍队员" name="teamMember">
        <TeamMemberForm ref="teamMemberFormRef" :team-id="formData.id" />
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
import { TeamApi, TeamVO } from '@/api/tylx/team'
import TeamChatForm from './components/TeamChatForm.vue'
import TeamMemberForm from './components/TeamMemberForm.vue'
import { DestinationApi } from '@/api/tylx/destination'
import * as UserApi from '@/api/system/user'

/** 队伍 表单 */
defineOptions({ name: 'TeamForm' })

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const dialogVisible = ref(false) // 弹窗的是否展示
const dialogTitle = ref('') // 弹窗的标题
const formLoading = ref(false) // 表单的加载中：1）修改时的数据加载；2）提交的按钮禁用
const formType = ref('') // 表单的类型：create - 新增；update - 修改
const formData = ref({
  id: undefined,
  destinationId: undefined,
  userId: undefined,
  detail: undefined,
  content: undefined,
  status: undefined,
  area: undefined,
  maxNumber: undefined,
  startTime: undefined,
  endTime: undefined
})
const formRules = reactive({
})
const formRef = ref() // 表单 Ref

/** 子表的表单 */
const subTabsName = ref('teamChat')
const teamChatFormRef = ref()
const teamMemberFormRef = ref()

const destinationOptions = ref([])
const userOptions = ref([])

/** 获取目的地列表 */
const getDestinationList = async () => {
  try {
    const res = await DestinationApi.getDestinationPage({
      pageNo: 1,
      pageSize: 100 // 获取所有目的地
    })
    destinationOptions.value = res.list
  } catch (error) {
    console.error('获取目的地列表失败:', error)
  }
}

/** 获取用户列表 */
const getUserList = async () => {
  try {
    const res = await UserApi.getUserPage({
      pageNo: 1,
      pageSize: 100 // 获取所有用户
    })
    userOptions.value = res.list
  } catch (error) {
    console.error('获取用户列表失败:', error)
  }
}

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
      formData.value = await TeamApi.getTeam(id)
    } finally {
      formLoading.value = false
    }
  }
  // 在表单打开时获取目的地列表和用户列表
  getDestinationList()
  getUserList()
}
defineExpose({ open }) // 提供 open 方法，用于打开弹窗

/** 提交表单 */
const emit = defineEmits(['success']) // 定义 success 事件，用于操作成功后的回调
const submitForm = async () => {
  // 校验表单
  await formRef.value.validate()
  // 校验子表单
  try {
    await teamChatFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'teamChat'
    return
  }
  try {
    await teamMemberFormRef.value.validate()
  } catch (e) {
    subTabsName.value = 'teamMember'
    return
  }
  // 提交请求
  formLoading.value = true
  try {
    const data = formData.value as unknown as TeamVO
    // 拼接子表的数据
    data.teamChats = teamChatFormRef.value.getData()
    data.teamMembers = teamMemberFormRef.value.getData()
    if (formType.value === 'create') {
      await TeamApi.createTeam(data)
      message.success(t('common.createSuccess'))
    } else {
      await TeamApi.updateTeam(data)
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
    destinationId: undefined,
    userId: undefined,
    detail: undefined,
    content: undefined,
    status: undefined,
    area: undefined,
    maxNumber: undefined,
    startTime: undefined,
    endTime: undefined
  }
  formRef.value?.resetFields()
}
</script>