import request from '@/config/axios'

// 论坛 VO
export interface ForumVO {
  id: number // 用户ID
  picture: string // 首页图片
  title: string // 首页标题
  content: string // 内容
  introduce: string // 简介
  userId: number // 用户编号
  destinationId: number // 目的地编号
  view: number // 阅读量
  likes: number // 点赞量
  comment: number // 评论量
  isHome: number // 是否精选
}

// 论坛 API
export const ForumApi = {
  // 查询论坛分页
  getForumPage: async (params: any) => {
    return await request.get({ url: `/tylx/forum/page`, params })
  },

  // 查询论坛详情
  getForum: async (id: number) => {
    return await request.get({ url: `/tylx/forum/get?id=` + id })
  },

  // 新增论坛
  createForum: async (data: ForumVO) => {
    return await request.post({ url: `/tylx/forum/create`, data })
  },

  // 修改论坛
  updateForum: async (data: ForumVO) => {
    return await request.put({ url: `/tylx/forum/update`, data })
  },

  // 删除论坛
  deleteForum: async (id: number) => {
    return await request.delete({ url: `/tylx/forum/delete?id=` + id })
  },

  // 导出论坛 Excel
  exportForum: async (params) => {
    return await request.download({ url: `/tylx/forum/export-excel`, params })
  },

// ==================== 子表（论坛评论） ====================

  // 获得论坛评论列表
  getForumCommentListByForumId: async (forumId) => {
    return await request.get({ url: `/tylx/forum/forum-comment/list-by-forum-id?forumId=` + forumId })
  },

// ==================== 子表（论坛点赞） ====================

  // 获得论坛点赞列表
  getForumLikesListByForumId: async (forumId) => {
    return await request.get({ url: `/tylx/forum/forum-likes/list-by-forum-id?forumId=` + forumId })
  },

// ==================== 子表（论坛打赏） ====================

  // 获得论坛打赏列表
  getForumRewardListByForumId: async (forumId) => {
    return await request.get({ url: `/tylx/forum/forum-reward/list-by-forum-id?forumId=` + forumId })
  }
}