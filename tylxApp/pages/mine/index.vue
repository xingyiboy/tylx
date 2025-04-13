<template>
  <view class="mine-container" :style="{ height: `${windowHeight}px` }">
    <!-- 顶部个人信息栏 -->
    <view class="header-section">
      <view class="header-content">
        <view class="user-profile">
          <view v-if="!avatar" class="cu-avatar round">
            <view class="iconfont icon-people"></view>
          </view>
          <image v-if="avatar" @click="handleToAvatar" :src="avatar" class="cu-avatar round" mode="aspectFill"></image>
          <view class="user-info">
            <view v-if="!name" @click="handleToLogin" class="login-tip">包小妹</view>
            <view v-if="name" @click="handleToInfo" class="username">{{ name }}</view>
            <view class="motto">每天进步一点点</view>
          </view>
        </view>
        <view class="stat-item">
          <view class="value">{{ money }}</view>
          <view class="label">账户(¥)</view>
        </view>
      </view>

      <!-- 快捷功能区 -->
      <view class="quick-actions">
        <view @click="goToMyTeam" class="action-item">
          <view class="icon-wrapper">
            <view class="iconfont icon-service"></view>
          </view>
          <text>我的组队</text>
        </view>
        <view class="action-item">
          <view @click="goToJoinTeam" class="icon-wrapper">
            <view class="iconfont icon-user"></view>
          </view>
          <text>报名队伍</text>
        </view>
        <view @click="goToMyForum" class="action-item">
          <view class="icon-wrapper">
            <view class="iconfont icon-community"></view>
          </view>
          <text>我的攻略</text>
        </view>
        <view @click="goToMoney" class="action-item">
          <view class="icon-wrapper">
            <view class="iconfont icon-code"></view>
          </view>
          <text>充值金额</text>
        </view>
      </view>
    </view>

    <view class="menu-section">
      <view class="menu-list">
        <view class="menu-item" @click="handleToEditInfo">
          <view class="menu-item-content">
            <view class="icon-wrapper">
              <view class="iconfont icon-user"></view>
            </view>
            <text>编辑资料</text>
          </view>
          <view class="iconfont icon-right arrow"></view>
        </view>

        <view class="menu-item" @click="handleToPwd">
          <view class="menu-item-content">
            <view class="icon-wrapper">
              <view class="iconfont icon-password"></view>
            </view>
            <text>修改密码</text>
          </view>
          <view class="iconfont icon-right arrow"></view>
        </view>
      </view>
    </view>

    <view class="logout-section">
      <view class="logout-button" @click="handleLogout">
        <text>退出登录</text>
      </view>
    </view>
  </view>
</template>

<script>
import storage from '@/utils/storage';
import { getUserProfile } from '@/api/system/user';

export default {
  data() {
    return {
      version: getApp().globalData.config.appInfo.version,
      userInfo: {},
      money: 0
    };
  },

  onShow() {
    // 每次显示页面时重新获取用户信息
    this.getUser();
  },

  computed: {
    name() {
      return this.$store.state.user.name;
    },
    nickname() {
      return this.$store.state.user.nickname;
    },
    avatar() {
      return this.$store.state.user.avatar;
    },
    windowHeight() {
      return uni.getSystemInfoSync().windowHeight - 50;
    }
  },

  methods: {
    async getUser() {
      try {
        const res = await getUserProfile();
        if (res.code === 0) {
          this.userInfo = res.data;
          this.money = res.data.money || 0;
          // 更新 store 中的用户信息
          this.$store.commit('user/setUserInfo', res.data);
        }
      } catch (error) {
        console.error('获取用户信息失败:', error);
        uni.showToast({
          title: '获取用户信息失败',
          icon: 'none'
        });
      }
    },
    handleToPwd() {
      this.$tab.navigateTo('/pages/mine/pwd/index');
    },
    handleLogout() {
      this.$modal.confirm('确定退出系统吗？').then(() => {
        this.$store.dispatch('LogOut').then(() => {
          this.$tab.reLaunch('/pages/index');
        });
      });
    },
    handleToInfo() {
      this.$tab.navigateTo('/pages/mine/info/index');
    },
    handleToEditInfo() {
      this.$tab.navigateTo('/pages/mine/info/edit');
    },
    handleToSetting() {
      this.$tab.navigateTo('/pages/history-record/index');
    },
    handleToLogin() {
      this.$tab.reLaunch('/pages/login');
    },
    handleToAvatar() {
      this.$tab.navigateTo('/pages/mine/avatar/index');
    },
    handleHelp() {
      this.$tab.navigateTo('/pages/mine/help/index');
    },
    handleAbout() {
      this.$tab.navigateTo('/pages/collect/index');
    },
    handleJiaoLiuQun() {
      this.$modal.showToast('微信搜索 naidaguo 后，添加好友后拉你进技术交流群');
    },
    handleBuilding() {
      this.$modal.showToast('模块建设中~');
    },
    // 跳转到我的组队
    goToMyTeam() {
      this.$tab.navigateTo('/pages/team/my')
    },
    // 跳转到报名队伍
    goToJoinTeam() {
      this.$tab.navigateTo('/pages/team/join')
    },
    // 跳转到我的攻略
    goToMyForum() {
      this.$tab.navigateTo('/pages/forum/my')
    },
    // 跳转到充值页面
    goToMoney() {
      this.$tab.navigateTo('/pages/mine/money')
    }
  }
};
</script>

<style lang="scss">
.mine-container {
  background-color: #f5f7fa;

  .header-section {
    padding: 40rpx 30rpx;
    height: 260rpx;
    margin-bottom: 150rpx;
    background: linear-gradient(90deg, #2989ff 0%, #42a4ff 100%);

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .user-profile {
        display: flex;
        align-items: center;

        .cu-avatar {
          width: 120rpx;
          height: 120rpx;
          background: #fff;
        }

        .user-info {
          margin-left: 20rpx;

          .username,
          .login-tip {
            font-size: 36rpx;
            color: #ffffff;
            font-weight: 500;
          }

          .motto {
            font-size: 24rpx;
            color: rgba(255, 255, 255, 0.8);
            margin-top: 8rpx;
          }
        }
      }

      .sign-in-btn {
        padding: 12rpx 30rpx;
        background: #ff9500;
        color: #ffffff;
        border-radius: 30rpx;
        font-size: 28rpx;
      }
    }

    .user-stats {
      display: flex;
      justify-content: space-between;
      margin-top: 40rpx;
    }

    .quick-actions {
      display: flex;
      justify-content: space-between;
      margin-top: 40rpx;
      padding: 30rpx;
      background: #ffffff;
      border-radius: 20rpx;
      box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
      position: relative;
      z-index: 1;

      .action-item {
        text-align: center;

        .icon-wrapper {
          width: 80rpx;
          height: 80rpx;
          margin: 0 auto;
          background: #f0f5ff;
          border-radius: 50%;
          display: flex;
          align-items: center;
          justify-content: center;

          .iconfont {
            font-size: 40rpx;
            color: #0066ff;
          }
        }

        text {
          display: block;
          margin-top: 16rpx;
          font-size: 24rpx;
          color: #333;
        }
      }
    }
  }

  .menu-section {
    margin-top: 20rpx;
    background: #fff;

    .menu-list {
      .menu-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 30rpx;
        border-bottom: 2rpx solid #f5f5f5;

        .menu-item-content {
          display: flex;
          align-items: center;

          .icon-wrapper {
            width: 40rpx;
            height: 40rpx;
            margin-right: 20rpx;
            display: flex;
            align-items: center;
            justify-content: center;

            .iconfont {
              font-size: 40rpx;
              color: #3b7cff;
            }
          }

          text {
            font-size: 28rpx;
            color: #333;
          }
        }

        .arrow {
          color: #ccc;
          font-size: 28rpx;
        }
      }
    }
  }

  .logout-section {
    margin: 60rpx 30rpx;

    .logout-button {
      padding: 30rpx 0;
      text-align: center;
      background: #ffffff;
      border-radius: 12rpx;
      box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.05);

      text {
        font-size: 32rpx;
        color: #ff4d4f;
        font-weight: 500;
      }
    }
  }
}

.stat-item {
  text-align: center;

  .value {
    color: #ffffff;
    font-size: 36rpx;
    font-weight: bold;
  }

  .label {
    color: rgba(255, 255, 255, 0.8);
    font-size: 24rpx;
    margin-top: 8rpx;
  }
}
</style>
