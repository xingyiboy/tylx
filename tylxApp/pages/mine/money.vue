<template>
  <view class="money-container">
    <!-- 用户信息 -->
    <view class="user-info">
      <image class="avatar" :src="avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
      <view class="user-detail">
        <text class="username">{{ name }}</text>
        <text class="user-id">ID: {{ userId }}</text>
      </view>
    </view>

    <!-- 当前余额 -->
    <view class="balance-box">
      <text class="label">当前余额</text>
      <text class="amount">¥{{ money }}</text>
    </view>

    <!-- 支付方式选择 -->
    <view class="payment-methods">
      <text class="section-title">选择支付方式</text>
      <view class="method-list">
        <view class="method-item" :class="{ active: currentMethod === 'alipay' }" @tap="currentMethod = 'alipay'">
          <image src="http://43.139.211.116:9000/financinglink/3775337b8da3454050dd341b1bda9b0e12d4ca605d35eaaaed0f3c0817d03465.png" mode="aspectFit" class="method-icon"></image>
          <text>支付宝</text>
        </view>
        <view class="method-item" :class="{ active: currentMethod === 'wechat' }" @tap="currentMethod = 'wechat'">
          <image src="http://43.139.211.116:9000/financinglink/99709cebb8a57f5cf34938d5cb6ccd392212ffee39c9c7867e9c04222a9b2a70.png" mode="aspectFit" class="method-icon"></image>
          <text>微信支付</text>
        </view>
      </view>
    </view>

    <!-- 二维码展示 -->
    <view class="qrcode-box">
      <image
        :src="
          currentMethod === 'alipay'
            ? 'http://43.139.211.116:9000/financinglink/13ea12c0e69812775b4a464db776bf56e550e1ec80cd46066186bcc2d79a5001.png'
            : 'http://43.139.211.116:9000/financinglink/13ea12c0e69812775b4a464db776bf56e550e1ec80cd46066186bcc2d79a5001.png'
        "
        mode="aspectFit"
        class="qrcode"
      ></image>
    </view>

    <!-- 提示信息 -->
    <view class="tips">
      <text class="tips-title">充值说明</text>
      <text class="tips-content">请使用{{ currentMethod === 'alipay' ? '支付宝' : '微信' }}扫描上方二维码进行支付</text>
      <text class="tips-highlight">转账时请务必备注您的用户ID：{{ userId }}</text>
    </view>
  </view>
</template>

<script>
import { mapState } from 'vuex';

export default {
  data() {
    return {
      currentMethod: 'alipay' // 当前选择的支付方式
    };
  },

  computed: {
    name() {
      return this.$store.state.user.name;
    },
    money() {
      return this.$store.state.user.money;
    },
    avatar() {
      return this.$store.state.user.avatar;
    },
    userId() {
      return this.$store.state.user.id;
    }
  }
};
</script>

<style lang="scss" scoped>
.money-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding: 30rpx;
}

.user-info {
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 30rpx;
  border-radius: 16rpx;
  margin-bottom: 30rpx;

  .avatar {
    width: 120rpx;
    height: 120rpx;
    border-radius: 60rpx;
    margin-right: 30rpx;
  }

  .user-detail {
    display: flex;
    flex-direction: column;

    .username {
      font-size: 32rpx;
      font-weight: bold;
      color: #333;
      margin-bottom: 10rpx;
    }

    .user-id {
      font-size: 26rpx;
      color: #999;
    }
  }
}

.balance-box {
  background-color: #fff;
  padding: 40rpx 30rpx;
  border-radius: 16rpx;
  margin-bottom: 30rpx;

  .label {
    font-size: 28rpx;
    color: #666;
    margin-bottom: 20rpx;
    display: block;
  }

  .amount {
    font-size: 48rpx;
    font-weight: bold;
    color: #333;
  }
}

.payment-methods {
  background-color: #fff;
  padding: 30rpx;
  border-radius: 16rpx;
  margin-bottom: 30rpx;

  .section-title {
    font-size: 28rpx;
    color: #333;
    margin-bottom: 30rpx;
    display: block;
  }

  .method-list {
    display: flex;
    gap: 30rpx;

    .method-item {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20rpx;
      border-radius: 12rpx;
      border: 2rpx solid #eee;
      transition: all 0.3s;

      &.active {
        border-color: #1890ff;
        background-color: #e6f7ff;
      }

      .method-icon {
        width: 80rpx;
        height: 80rpx;
        margin-bottom: 16rpx;
      }

      text {
        font-size: 26rpx;
        color: #666;
      }
    }
  }
}

.qrcode-box {
  background-color: #fff;
  padding: 40rpx;
  border-radius: 16rpx;
  margin-bottom: 30rpx;
  display: flex;
  justify-content: center;

  .qrcode {
    width: 400rpx;
    height: 400rpx;
  }
}

.tips {
  background-color: #fff;
  padding: 30rpx;
  border-radius: 16rpx;

  .tips-title {
    font-size: 28rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
    display: block;
  }

  .tips-content {
    font-size: 26rpx;
    color: #666;
    margin-bottom: 16rpx;
    display: block;
  }

  .tips-highlight {
    font-size: 26rpx;
    color: #f56c6c;
    font-weight: bold;
    display: block;
  }
}
</style>
