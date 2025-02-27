import request from '@/utils/request';

// 获取目的地分页列表
export function getDestinationPage(params) {
  return request({
    url: '/tylx/destination/page',
    method: 'get',
    params
  });
}
// 获取景点分页列表
export function getDestinationPageScenery(params) {
  return request({
    url: '/tylx/destination/pageScenery',
    method: 'get',
    params
  });
}

// 获取目的地下的美食列表
export function getDestinationFoodList(destinationId) {
  return request({
    url: '/tylx/destination/destination-food/list-by-destination-id',
    method: 'get',
    params: { destinationId }
  });
}

// 获取目的地下的景点列表
export function getDestinationSceneryList(destinationId) {
  return request({
    url: '/tylx/destination/destination-scenery/list-by-destination-id',
    method: 'get',
    params: { destinationId }
  });
}

// ... existing code ...

// 获取景点详情
export function getDestinationSceneryDetail(id) {
  return request({
    url: '/tylx/destination/getScenery',
    method: 'get',
    params: { id }
  });
}

// 获取美食详情
export function getDestinationFoodDetail(id) {
  return request({
    url: '/tylx/destination/getFood',
    method: 'get',
    params: { id }
  });
}
