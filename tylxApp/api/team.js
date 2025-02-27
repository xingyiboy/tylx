import request from '@/utils/request';
import config from '@/config';

// 获取队伍列表
export function getTeamPage(params) {
  return request({
    url: '/tylx/team/page',
    method: 'GET',
    params
  });
}

// 获取队伍详情
export function getTeamDetail(id) {
  return request({
    url: '/tylx/team/get',
    method: 'get',
    params: {
      id
    }
  });
}

// 申请加入队伍
export function joinTeam(id) {
  return request({
    url: `/tylx/team/join?id=${id}`,
    method: 'post'
  });
}

// 获取队伍聊天记录
export function getTeamChatList(teamId) {
  return request({
    url: '/tylx/team/team-chat/list-by-team-id',
    method: 'get',
    params: { teamId }
  });
}

// 发送聊天消息
export function createTeamChat(data) {
  return request({
    url: '/tylx/team/createChat',
    method: 'post',
    data
  });
}

// 创建组队
export function createTeam(data) {
  return request({
    url: '/tylx/team/create',
    method: 'post',
    data
  });
}

// 获取我创建的队伍列表
export function getMyTeamPage(params) {
  return request({
    url: '/tylx/team/Mypage',
    method: 'get',
    params
  });
}

// 获取我加入的队伍列表
export function getJoinTeamPage(params) {
  return request({
    url: '/tylx/team/Joinpage',
    method: 'get',
    params
  });
}
