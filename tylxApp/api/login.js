import request from '@/utils/request';

// 登录方法
export function login(username, password, captchaVerification) {
  const data = {
    username,
    password,
    captchaVerification
  };
  return request({
    url: '/system/auth/login',
    headers: {
      isToken: false
    },
    method: 'POST',
    data: data
  });
}

// 注册方法
export function register(data) {
  return request({
    url: '/system/auth/register',
    headers: {
      isToken: false
    },
    method: 'POST',
    data: data
  });
}

// 使用租户名，获得租户编号
export function getTenantIdByName(name) {
  return request({
    url: '/system/tenant/get-id-by-name',
    method: 'GET',
    params: { name }
  });
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: '/system/auth/get-permission-info',
    method: 'GET'
  });
}

// 退出方法
export function logout() {
  return request({
    url: '/system/auth/logout',
    method: 'POST'
  });
}
