import request from '@/config/axios'

// 目的地树表 VO
export interface Destination2VO {
  id: number // 用户ID
  name: string // 目的地名称
  picture: string // 首页图片
  detail: string // 目的地描述
  parentId: number // 父级编号
}

// 目的地树表 API
export const Destination2Api = {
  // 查询目的地树表列表
  getDestination2List: async (params) => {
    return await request.get({ url: `/tylx/destination2/list`, params })
  },

  // 查询目的地树表详情
  getDestination2: async (id: number) => {
    return await request.get({ url: `/tylx/destination2/get?id=` + id })
  },

  // 新增目的地树表
  createDestination2: async (data: Destination2VO) => {
    return await request.post({ url: `/tylx/destination2/create`, data })
  },

  // 修改目的地树表
  updateDestination2: async (data: Destination2VO) => {
    return await request.put({ url: `/tylx/destination2/update`, data })
  },

  // 删除目的地树表
  deleteDestination2: async (id: number) => {
    return await request.delete({ url: `/tylx/destination2/delete?id=` + id })
  },

  // 导出目的地树表 Excel
  exportDestination2: async (params) => {
    return await request.download({ url: `/tylx/destination2/export-excel`, params })
  }
}