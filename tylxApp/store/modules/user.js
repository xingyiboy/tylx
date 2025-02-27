import config from '@/config';
import storage from '@/utils/storage';
import constant from '@/utils/constant';
import { login, logout, getInfo } from '@/api/login';
import { setToken, removeToken } from '@/utils/auth';

const baseUrl = config.baseUrl;

const user = {
  state: {
    id: storage.get(constant.id), // 用户编号
    name: storage.get(constant.name),
    avatar: storage.get(constant.avatar),
    roles: storage.get(constant.roles),
    permissions: storage.get(constant.permissions),
    money: storage.get(constant.money)
  },

  mutations: {
    SET_ID: (state, id) => {
      state.id = id;
      storage.set(constant.id, id);
    },
    SET_NAME: (state, name) => {
      state.name = name;
      storage.set(constant.name, name);
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
      storage.set(constant.avatar, avatar);
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
      storage.set(constant.roles, roles);
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
      storage.set(constant.permissions, permissions);
    },
    SET_MONEY: (state, money) => {
      state.money = money;
      storage.set(constant.money, money);
    }
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const username = userInfo.username.trim();
      const password = userInfo.password;
      const captchaVerification = userInfo.captchaVerification;
      return new Promise((resolve, reject) => {
        login(username, password, captchaVerification)
          .then((res) => {
            res = res.data;
            // 设置 token
            setToken(res);
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            res = res.data; // 读取 data 数据
            const user = res.user;
            const avatar = user == null || user.avatar === '' || user.avatar == null ? require('@/static/images/profile.jpg') : user.avatar;
            const nickname = user == null || user.nickname === '' || user.nickname == null ? '' : user.nickname;
            const money = user == null || user.money === '' || user.money == null ? 0 : user.money;
            const id = user == null || user.id === '' || user.id == null ? 0 : user.id;
            commit('SET_ID', id);
            if (res.roles && res.roles.length > 0) {
              commit('SET_ROLES', res.roles);
              commit('SET_PERMISSIONS', res.permissions);
            } else {
              commit('SET_ROLES', ['ROLE_DEFAULT']);
            }
            commit('SET_NAME', nickname);
            commit('SET_AVATAR', avatar);
            commit('SET_MONEY', money);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        logout(state.token)
          .then(() => {
            commit('SET_ROLES', []);
            commit('SET_PERMISSIONS', []);
            removeToken();
            storage.clean();
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    }
  }
};

export default user;
