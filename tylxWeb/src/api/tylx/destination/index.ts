import request from '@/config/axios'

// 目的地 VO
export interface DestinationVO {
  id: number // 用户ID
  name: string // 目的地名称
  picture: string // 首页图片
  detail: string // 目的地描述
}

// 目的地 API
export const DestinationApi = {
  // 查询目的地分页
  getDestinationPage: async (params: any) => {
    return await request.get({ url: `/tylx/destination/page`, params })
  },

  // 查询目的地详情
  getDestination: async (id: number) => {
    return await request.get({ url: `/tylx/destination/get?id=` + id })
  },

  // 新增目的地
  createDestination: async (data: DestinationVO) => {
    return await request.post({ url: `/tylx/destination/create`, data })
  },

  // 修改目的地
  updateDestination: async (data: DestinationVO) => {
    return await request.put({ url: `/tylx/destination/update`, data })
  },

  // 删除目的地
  deleteDestination: async (id: number) => {
    return await request.delete({ url: `/tylx/destination/delete?id=` + id })
  },

  // 导出目的地 Excel
  exportDestination: async (params) => {
    return await request.download({ url: `/tylx/destination/export-excel`, params })
  },

// ==================== 子表（特色美食） ====================

  // 获得特色美食列表
  getDestinationFoodListByDestinationId: async (destinationId) => {
    return await request.get({ url: `/tylx/destination/destination-food/list-by-destination-id?destinationId=` + destinationId })
  },

// ==================== 子表（热门景点） ====================

  // 获得热门景点列表
  getDestinationSceneryListByDestinationId: async (destinationId) => {
    return await request.get({ url: `/tylx/destination/destination-scenery/list-by-destination-id?destinationId=` + destinationId })
  }
}