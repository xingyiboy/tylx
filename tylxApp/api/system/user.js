import upload from '@/utils/upload';
import request from '@/utils/request';
import config from '@/config';

// 上传文件
export function uploadFile(file) {
  return new Promise((resolve, reject) => {
    uni.uploadFile({
      url: config.baseUrl + config.baseApi + '/infra/file/upload',
      filePath: file,
      name: 'file',
      header: {
        Authorization: 'Bearer ' + uni.getStorageSync('REFRESH_TOKEN')
      },
      success: (res) => {
        try {
          const result = JSON.parse(res.data);
          resolve(result);
        } catch (error) {
          reject(new Error('解析响应数据失败'));
        }
      },
      fail: (error) => {
        reject(error);
      }
    });
  });
}

// 用户密码重置
export function updateUserPwd(data) {
  return request({
    url: '/system/user/profile/update-password',
    method: 'PUT',
    data
  });
}

// 查询用户个人信息
export function getUserProfile() {
  return request({
    url: '/system/user/profile/get',
    method: 'GET'
  });
}

// 修改用户个人信息
export function updateUserProfile(data) {
  return request({
    url: '/system/user/profile/update',
    method: 'PUT',
    data: data
  });
}

// 用户头像上传
export function uploadAvatar(data) {
  return upload({
    url: '/admin-api/system/user/profile/update-avatar',
    method: 'PUT',
    name: data.name,
    filePath: data.filePath
  });
}
