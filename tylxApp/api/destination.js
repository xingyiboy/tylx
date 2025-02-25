import request from '@/utils/request';

// 获取目的地分页列表
export function getDestinationPage(params) {
  return request({
    url: '/tylx/destination/page',
    method: 'get',
    params
  });
}
