<template>
  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="用户ID" align="center" prop="id" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        :formatter="dateFormatter"
        width="180px"
      />
       <el-table-column label="景点名称" align="center" prop="name" />
      <el-table-column label="景点详情" align="center" prop="content" />
      <el-table-column label="首页图片" align="center" prop="picture" />
      <el-table-column label="简介" align="center" prop="detail" />
      <el-table-column label="是否精选" align="center" prop="isHome">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.IS_HOME" :value="scope.row.isHome" />
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>
</template>
<script setup lang="ts">
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import { DestinationApi } from '@/api/tylx/destination'

const { t } = useI18n() // 国际化
const message = useMessage() // 消息弹窗

const props = defineProps<{
  destinationId?: number // 目的地编号（主表的关联字段）
}>()
const loading = ref(false) // 列表的加载中
const list = ref([]) // 列表的数据

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    list.value = await DestinationApi.getDestinationSceneryListByDestinationId(props.destinationId)
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 初始化 **/
onMounted(() => {
  getList()
})
</script>