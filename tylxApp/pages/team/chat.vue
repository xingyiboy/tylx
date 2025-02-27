<template>
  <view class="chat-container">
    <!-- 聊天记录区域 -->
    <scroll-view class="chat-list" scroll-y="true" :scroll-top="scrollTop" :scroll-into-view="scrollToMessage"
      @scrolltoupper="loadMoreMessages">
      <view class="chat-item" v-for="(item, index) in chatList" :key="item.id" :id="'msg-' + item.id">
        <!-- 时间显示 -->
        <view class="time-stamp" v-if="showTimeStamp(item, index)">
          {{ formatTime(item.createTime) }}
        </view>

        <!-- 消息内容 -->
        <view class="message" :class="{ self: item.isMyChat }">
          <image class="avatar" :src="item.avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
          <view class="content">
            <view class="username">
              {{ item.userName }}
              <text class="role-tag" :class="item.isOwner ? 'leader' : 'member'">
                {{ item.isOwner ? '队长' : '队员' }}
              </text>
            </view>
            <view class="text">{{ item.content }}</view>
          </view>
        </view>
      </view>
    </scroll-view>

    <!-- 输入区域 -->
    <view class="input-area">
      <input class="message-input" v-model="messageContent" type="text" placeholder="请输入消息" @confirm="sendMessage" />
      <button class="send-btn" :disabled="!messageContent.trim()" @tap="sendMessage">发送</button>
    </view>
  </view>
</template>

<script>
import { getTeamChatList, createTeamChat } from '@/api/team';

export default {
  data() {
    return {
      teamId: null,
      chatList: [],
      messageContent: '',
      currentUserId: uni.getStorageSync('userId'), // 当前用户ID
      scrollTop: 0,
      scrollToMessage: '',
      lastTimestamp: 0, // 用于控制时间戳显示
      refreshInterval: null // 定时刷新计时器
    };
  },

  onLoad(options) {
    this.teamId = options.id;
    this.loadChatList();
    // 设置定时刷新
    this.refreshInterval = setInterval(() => {
      this.loadChatList();
    }, 3000);
  },

  onUnload() {
    // 清除定时器
    if (this.refreshInterval) {
      clearInterval(this.refreshInterval);
    }
  },

  methods: {
    async loadChatList() {
      try {
        const res = await getTeamChatList(this.teamId);
        if (res.code === 0) {
          this.chatList = res.data;
          this.$nextTick(() => {
            // 滚动到最新消息
            this.scrollToBottom();
          });
        }
      } catch (error) {
        console.error('加载聊天记录失败:', error);
      }
    },

    async sendMessage() {
      if (!this.messageContent.trim()) return;

      try {
        const message = {
          teamId: this.teamId,
          content: this.messageContent,
          userId: this.currentUserId
        };

        const res = await createTeamChat(message);
        if (res.code === 0) {
          this.messageContent = '';
          await this.loadChatList();
        }
      } catch (error) {
        console.error('发送消息失败:', error);
        uni.showToast({
          title: '发送失败',
          icon: 'none'
        });
      }
    },

    scrollToBottom() {
      // 获取最后一条消息的ID
      if (this.chatList.length > 0) {
        this.scrollToMessage = 'msg-' + this.chatList[this.chatList.length - 1].id;
      }
    },

    showTimeStamp(item, index) {
      if (index === 0) return true
      const prevTime = this.chatList[index - 1].createTime
      const currentTime = item.createTime
      // 如果与上一条消息相差超过5分钟，显示时间戳
      return (currentTime - prevTime) > 5 * 60 * 1000
    },

    formatTime(timestamp) {
      const date = new Date(timestamp)
      const now = new Date()
      const diff = now - date
      const days = Math.floor(diff / (24 * 60 * 60 * 1000))

      if (days === 0) {
        // 今天
        return this.formatTimeOnly(date)
      } else if (days === 1) {
        // 昨天
        return `昨天 ${this.formatTimeOnly(date)}`
      } else if (days <= 7) {
        // 一周内
        const weekdays = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六']
        return `${weekdays[date.getDay()]} ${this.formatTimeOnly(date)}`
      } else {
        // 更早
        return `${date.getMonth() + 1}月${date.getDate()}日 ${this.formatTimeOnly(date)}`
      }
    },

    formatTimeOnly(date) {
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      return `${hours}:${minutes}`
    }
  }
};
</script>

<style lang="scss">
.chat-container {
  display: flex;
  flex-direction: column;
  height: 100vh;
  background-color: #ededed;
}

.chat-list {
  flex: 1;
  padding: 20rpx 30rpx;
}

.chat-item {
  margin-bottom: 20rpx;

  .time-stamp {
    text-align: center;
    font-size: 24rpx;
    color: #b2b2b2;
    margin: 20rpx 0;

    // 微信风格时间戳
    background: transparent;
    padding: 8rpx 16rpx;
  }

  .message {
    display: flex;
    align-items: flex-start;
    margin-bottom: 28rpx;

    &.self {
      flex-direction: row-reverse;

      .content {
        margin: 0 20rpx 0 80rpx;

        .username {
          display: none; // 自己发送的消息不显示用户名
        }

        .text {
          background-color: #95ec69;
          color: #000;
          border: 1rpx solid rgba(0, 0, 0, 0.05);

          // 微信特有的气泡尖角
          &::before {
            content: '';
            position: absolute;
            right: -12rpx;
            top: 20rpx;
            width: 0;
            height: 0;
            border-style: solid;
            border-width: 16rpx 0 0 12rpx;
            border-color: transparent transparent transparent #95ec69;
          }
        }
      }
    }

    .avatar {
      width: 80rpx;
      height: 80rpx;
      border-radius: 8rpx; // 微信头像是方形圆角
      flex-shrink: 0;
    }

    .content {
      margin: 0 80rpx 0 20rpx;

      .username {
        font-size: 24rpx;
        color: #999;
        margin-bottom: 6rpx;
        padding-left: 4rpx;

        .role-tag {
          font-size: 20rpx;
          padding: 2rpx 8rpx;
          border-radius: 4rpx;
          margin-left: 8rpx;

          &.leader {
            background: #ffd591;
            color: #873800;
          }

          &.member {
            background: #91d5ff;
            color: #003a8c;
          }
        }
      }

      .text {
        position: relative;
        display: inline-block;
        background-color: #fff;
        padding: 16rpx 24rpx;
        border-radius: 8rpx;
        font-size: 32rpx;
        line-height: 44rpx;
        max-width: 100%;
        word-break: break-all;

        // 微信特有的气泡尖角
        &::before {
          content: '';
          position: absolute;
          left: -12rpx;
          top: 20rpx;
          width: 0;
          height: 0;
          border-style: solid;
          border-width: 0 12rpx 16rpx 0;
          border-color: transparent #fff transparent transparent;
        }
      }
    }
  }
}

.input-area {
  padding: 16rpx 20rpx;
  background-color: #f7f7f7;
  border-top: 1rpx solid #e6e6e6;
  display: flex;
  align-items: center;

  .message-input {
    flex: 1;
    height: 68rpx;
    background-color: #fff;
    border-radius: 8rpx;
    padding: 0 20rpx;
    font-size: 32rpx;
    margin-right: 16rpx;
    border: 1rpx solid #e6e6e6;
  }

  .send-btn {
    width: 120rpx;
    height: 68rpx;
    line-height: 68rpx;
    text-align: center;
    background-color: #07c160;
    color: #fff;
    border-radius: 8rpx;
    font-size: 28rpx;
    padding: 0;

    &:disabled {
      background-color: #95ec69;
      opacity: 0.5;
    }
  }
}
</style>
