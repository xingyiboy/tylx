<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
      <el-form-item label="创建时间" prop="createTime">
        <el-date-picker v-model="queryParams.createTime" value-format="YYYY-MM-DD HH:mm:ss" type="daterange"
          start-placeholder="开始日期" end-placeholder="结束日期"
          :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]" class="!w-220px" />
      </el-form-item>
      <el-form-item label="首页标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入首页标题" clearable @keyup.enter="handleQuery"
          class="!w-240px" />
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
      <el-form-item label="目的地" prop="destinationId">
        <el-select v-model="queryParams.destinationId" filterable placeholder="请选择目的地" clearable @change="handleQuery"
          class="!w-240px">
          <el-option v-for="item in destinationOptions" :key="item.id" :label="item.name" :value="item.id">
            <span>{{ item.name }}</span>
            <span class="text-gray-400 ml-2">(ID: {{ item.id }})</span>
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="阅读量" prop="view">
        <el-input v-model="queryParams.view" placeholder="请输入阅读量" clearable @keyup.enter="handleQuery"
          class="!w-240px" />
      </el-form-item>
      <el-form-item label="点赞量" prop="likes">
        <el-input v-model="queryParams.likes" placeholder="请输入点赞量" clearable @keyup.enter="handleQuery"
          class="!w-240px" />
      </el-form-item>
      <el-form-item label="评论量" prop="comment">
        <el-input v-model="queryParams.comment" placeholder="请输入评论量" clearable @keyup.enter="handleQuery"
          class="!w-240px" />
      </el-form-item>
      <el-form-item label="是否精选" prop="isHome">
        <el-select v-model="queryParams.isHome" placeholder="请选择是否精选" clearable class="!w-240px">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.IS_HOME)" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="ep:search" class="mr-5px" /> 搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="ep:refresh" class="mr-5px" /> 重置
        </el-button>
        <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['tylx:forum:create']">
          <Icon icon="ep:plus" class="mr-5px" /> 新增
        </el-button>
        <el-button type="success" plain @click="handleExport" :loading="exportLoading"
          v-hasPermi="['tylx:forum:export']">
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
          <el-tabs model-value="forumComment">
            <el-tab-pane label="论坛评论" name="forumComment">
              <ForumCommentList :forum-id="scope.row.id" />
            </el-tab-pane>
            <el-tab-pane label="论坛点赞" name="forumLikes">
              <ForumLikesList :forum-id="scope.row.id" />
            </el-tab-pane>
            <el-tab-pane label="论坛打赏" name="forumReward">
              <ForumRewardList :forum-id="scope.row.id" />
            </el-tab-pane>
          </el-tabs>
        </template>
      </el-table-column>
      <el-table-column label="用户ID" align="center" prop="id" />
      <el-table-column label="创建时间" align="center" prop="createTime" :formatter="dateFormatter" width="180px" />
      <el-table-column label="首页图片" align="center" prop="picture" />
      <el-table-column label="首页标题" align="center" prop="title" />
      <el-table-column label="内容" align="center" prop="content" />
      <el-table-column label="简介" align="center" prop="introduce" />
      <el-table-column label="用户名称" align="center" prop="userName" />
      <el-table-column label="目的地编号" align="center" prop="destinationId" />
      <el-table-column label="阅读量" align="center" prop="view" />
      <el-table-column label="点赞量" align="center" prop="likes" />
      <el-table-column label="评论量" align="center" prop="comment" />
      <el-table-column label="是否精选" align="center" prop="isHome">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.IS_HOME" :value="scope.row.isHome" />
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" min-width="120px">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)" v-hasPermi="['tylx:forum:update']">
            编辑
          </el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)" v-hasPermi="['tylx:forum:delete']">
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
  <ForumForm ref="formRef" @success="getList" />
</template>

<script setup lang="ts">
import { getIntDictOptions, DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import { ForumApi, ForumVO } from '@/api/tylx/forum'
import ForumForm from './ForumForm.vue'
import ForumCommentList from './components/ForumCommentList.vue'
import ForumLikesList from './components/ForumLikesList.vue'
import ForumRewardList from './components/ForumRewardList.vue'
import { DestinationApi } from '@/api/tylx/destination'
import * as UserApi from '@/api/system/user'

/** 论坛 列表 */
defineOptions({ name: 'Forum' })

const message = useMessage() // 消息弹窗
const { t } = useI18n() // 国际化

const loading = ref(true) // 列表的加载中
const list = ref<ForumVO[]>([]) // 列表的数据
const total = ref(0) // 列表的总页数
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  createTime: [],
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
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中

const destinationOptions = ref([])
const userOptions = ref([])

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ForumApi.getForumPage(queryParams)
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
    await ForumApi.deleteForum(id)
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
    const data = await ForumApi.exportForum(queryParams)
    download.excel(data, '论坛.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

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

/** 初始化 **/
onMounted(() => {
  getList()
  getDestinationList()
  getUserList() // 添加获取用户列表
})
</script>
