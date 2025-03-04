<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
      <el-form-item label="目的地" prop="destinationId">
        <el-select v-model="queryParams.destinationId" filterable placeholder="请选择目的地" clearable @change="handleQuery"
          class="!w-240px">
          <el-option v-for="item in destinationOptions" :key="item.id" :label="item.name" :value="item.id">
            <span>{{ item.name }}</span>
            <span class="text-gray-400 ml-2">(ID: {{ item.id }})</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="用户" prop="userId">
        <el-select v-model="queryParams.userId" filterable placeholder="请选择用户" clearable @change="handleQuery"
          class="!w-240px">
          <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id">
            <span>{{ item.nickname }}</span>
            <span class="text-gray-400 ml-2">(ID: {{ item.id }})</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable class="!w-240px">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.TYLX_TEAM_STATUS)" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="详细地址" prop="area">
        <el-input v-model="queryParams.area" placeholder="请输入详细地址" clearable @keyup.enter="handleQuery"
          class="!w-240px" />
      </el-form-item>
      <el-form-item label="最大组队" prop="maxNumber">
        <el-input v-model="queryParams.maxNumber" placeholder="请输入最大组队数" clearable @keyup.enter="handleQuery"
          class="!w-240px" />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker v-model="queryParams.startTime" value-format="YYYY-MM-DD HH:mm:ss" type="daterange"
          start-placeholder="开始日期" end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]" class="!w-220px" />
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker v-model="queryParams.endTime" value-format="YYYY-MM-DD HH:mm:ss" type="daterange"
          start-placeholder="开始日期" end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]" class="!w-220px" />
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" /> 搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" /> 重置
        </el-button>
        <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['tylx:team:create']">
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button type="success" plain @click="handleExport" :loading="exportLoading"
          v-hasPermi="['tylx:team:export']">
          <Icon icon="ep:download" class="mr-5px" /> 导出
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <!-- 子表的列表 -->
      <el-table-column type="expand">
        <template #default="scope">
          <el-tabs model-value="teamChat">
            <el-tab-pane label="队伍聊天" name="teamChat">
              <TeamChatList :team-id="scope.row.id" />
            </el-tab-pane>
            <el-tab-pane label="队伍队员" name="teamMember">
              <TeamMemberList :team-id="scope.row.id" />
            </el-tab-pane>
          </el-tabs>
        </template>
      </el-table-column>
      <el-table-column label="用户ID" align="center" prop="id" />
      <el-table-column label="创建时间" align="center" prop="createTime" :formatter="dateFormatter" width="180px" />
      <el-table-column label="目的地编号" align="center" prop="destinationId" />
      <el-table-column label="用户名称" align="center" prop="userName" />
      <el-table-column label="简介" align="center" prop="detail" />
      <el-table-column label="内容" align="center" prop="content" />
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.TYLX_TEAM_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="详细地址" align="center" prop="area" />
      <el-table-column label="最大组队数" align="center" prop="maxNumber" />
      <el-table-column label="开始时间" align="center" prop="startTime" :formatter="dateFormatter" width="180px" />
      <el-table-column label="结束时间" align="center" prop="endTime" :formatter="dateFormatter" width="180px" />
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)" v-hasPermi="['tylx:team:update']">
            编辑
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['tylx:team:delete']">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <!-- 表单弹窗：添加/修改 -->
  <TeamForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { TeamApi, TeamVO } from '@/api/tylx/team'
import TeamForm from './TeamForm.vue'
import TeamChatList from './components/TeamChatList.vue'
import TeamMemberList from './components/TeamMemberList.vue'
import { DestinationApi } from '@/api/tylx/destination'
import * as UserApi from '@/api/system/user'

/** 队伍 列表 */
defineOptions({ name: 'Team' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<TeamVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  createTime: [],
  destinationId: undefined,
  userId: undefined,
  detail: undefined,
  content: undefined,
  status: undefined,
  area: undefined,
  maxNumber: undefined,
  startTime: [],
  endTime: []
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

const destinationOptions = ref([])
const userOptions = ref([])

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await TeamApi.getTeamPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await TeamApi.deleteTeam(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch { }
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await TeamApi.exportTeam(queryParams)
    download.excel(data, '队伍.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

// 获取目的地列表
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

// 获取用户列表
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

/** 初始化 **/
onMounted(() => {
  getList()
  getDestinationList()
  getUserList() // 添加获取用户列表
})
</script>
