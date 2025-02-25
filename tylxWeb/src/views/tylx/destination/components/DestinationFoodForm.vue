<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    v-loading="formLoading"
    label-width="0px"
    :inline-message="true"
  >
    <el-table :data="formData" class="-mt-10px">
      <el-table-column label="序号" type="index" width="100" />
       <el-table-column label="美食名称" min-width="150">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.name`" :rules="formRules.name" class="mb-0px!">
            <el-input v-model="row.name" placeholder="请输入美食名称" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="首页图片" min-width="200">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.picture`" :rules="formRules.picture" class="mb-0px!">
            <UploadImg v-model="row.picture" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="简介" min-width="200">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.detail`" :rules="formRules.detail" class="mb-0px!">
            <el-input v-model="row.detail" type="textarea" placeholder="请输入简介" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column label="内容" min-width="400">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.content`" :rules="formRules.content" class="mb-0px!">
            <Editor v-model="row.content" height="150px" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" label="操作" width="60">
        <template #default="{ $index }">
          <el-button @click="handleDelete($index)" link>—</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3">
    <el-button @click="handleAdd" round>+ 添加特色美食</el-button>
  </el-row>
</template>
<script setup lang="ts">
import { DestinationApi } from '@/api/tylx/destination'

const props = defineProps<{
  destinationId: undefined // 目的地编号（主表的关联字段）
}>()
const formLoading = ref(false) // 表单的加载中
const formData = ref([])
const formRules = reactive({
})
const formRef = ref() // 表单 Ref

/** 监听主表的关联字段的变化，加载对应的子表数据 */
watch(
  () => props.destinationId,
  async (val) => {
    // 1. 重置表单
    formData.value = []
    // 2. val 非空，则加载数据
    if (!val) {
      return;
    }
    try {
      formLoading.value = true
      formData.value = await DestinationApi.getDestinationFoodListByDestinationId(val)
    } finally {
      formLoading.value = false
    }
  },
  { immediate: true }
)

/** 新增按钮操作 */
const handleAdd = () => {
  const row = {
    id: undefined,
    destinationId: undefined,
    name: undefined,
    picture: undefined,
    detail: undefined,
    content: undefined
  }
  row.destinationId = props.destinationId
  formData.value.push(row)
}

/** 删除按钮操作 */
const handleDelete = (index) => {
  formData.value.splice(index, 1)
}

/** 表单校验 */
const validate = () => {
  return formRef.value.validate()
}

/** 表单值 */
const getData = () => {
  return formData.value
}

defineExpose({ validate, getData })
</script>