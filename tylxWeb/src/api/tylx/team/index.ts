import request from '@/config/axios'

// 队伍 VO
export interface TeamVO {
  id: number // 用户ID
  destinationId: number // 目的地编号
  userId: number // 用户编号
  detail: string // 简介
  content: string // 内容
  status: number // 状态 0招募中 1招募结束
  area: string // 详细地址
  maxNumber: number // 最大组队数
  startTime: Date // 开始时间
  endTime: Date // 结束时间
}

// 队伍 API
export const TeamApi = {
  // 查询队伍分页
  getTeamPage: async (params: any) => {
    return await request.get({ url: `/tylx/team/page`, params })
  },

  // 查询队伍详情
  getTeam: async (id: number) => {
    return await request.get({ url: `/tylx/team/get?id=` + id })
  },

  // 新增队伍
  createTeam: async (data: TeamVO) => {
    return await request.post({ url: `/tylx/team/create`, data })
  },

  // 修改队伍
  updateTeam: async (data: TeamVO) => {
    return await request.put({ url: `/tylx/team/update`, data })
  },

  // 删除队伍
  deleteTeam: async (id: number) => {
    return await request.delete({ url: `/tylx/team/delete?id=` + id })
  },

  // 导出队伍 Excel
  exportTeam: async (params) => {
    return await request.download({ url: `/tylx/team/export-excel`, params })
  },

// ==================== 子表（队伍聊天） ====================

  // 获得队伍聊天列表
  getTeamChatListByTeamId: async (teamId) => {
    return await request.get({ url: `/tylx/team/team-chat/list-by-team-id?teamId=` + teamId })
  },

// ==================== 子表（队伍队员） ====================

  // 获得队伍队员列表
  getTeamMemberListByTeamId: async (teamId) => {
    return await request.get({ url: `/tylx/team/team-member/list-by-team-id?teamId=` + teamId })
  }
}