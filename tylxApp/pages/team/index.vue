<template>
  <view class="team-detail">
    <!-- 用户信息 -->
    <view class="user-info">
      <image class="avatar" :src="detail.avatar" mode="aspectFill"></image>
      <view class="user-detail">
        <text class="username">{{ detail.userName }}</text>
        <text class="user-tag">{{ detail.gender }} · {{ detail.age || '' }}岁</text>
      </view>
      <view class="team-status" :class="detail.status === 0 ? 'recruiting' : 'completed'">
        {{ detail.status === 0 ? '招募中' : '已结束' }}
      </view>
    </view>

    <!-- 行程信息 -->
    <view class="trip-info">
      <view class="destination">
        <uni-icons type="location" size="16" color="#666"></uni-icons>
        <text>{{ detail.area }}</text>
      </view>
      <view class="time">
        <uni-icons type="calendar" size="16" color="#666"></uni-icons>
        <text>{{ formatDate(detail.startTime) }} - {{ formatDate(detail.endTime) }}</text>
      </view>
    </view>

    <!-- 活动内容 -->
    <view class="content-section">
      <mp-html :content="detail.content" />
    </view>

    <!-- 组队进度 -->
    <view class="team-progress">
      <view class="progress-text">
        <text>已组{{ detail.currentNumber - 1 }}人</text>
        <text>最多{{ detail.maxNumber }}人</text>
      </view>
      <progress :percent="((detail.currentNumber - 1) / detail.maxNumber) * 100" stroke-width="4" activeColor="#87CEEB" backgroundColor="#eee" />
    </view>

    <!-- 已有队员 -->
    <view class="members-list">
      <view class="section-title">已有队员</view>
      <view class="member-item" v-for="member in detail.teamMemberList" :key="member.userId">
        <view class="member-info">
          <image class="avatar" :src="member.avatar || '/static/default-avatar.png'" mode="aspectFill"></image>
          <view class="member-detail">
            <view class="member-name">
              <text class="role-tag" :class="member.userId === detail.userId ? 'leader' : 'member'">
                {{ member.userId === detail.userId ? '队长' : '队员' }}
              </text>
              {{ member.userName }}
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-button" v-if="detail.status === 0">
      <button v-if="!detail.isTeamMember" class="join-btn" @tap="handleJoin">申请加入</button>
      <button v-if="detail.isTeamMember" class="join-btn" @tap="handleChat(detail.id)">进入聊天室</button>
    </view>
  </view>
</template>

<script>
import { getTeamDetail, joinTeam } from '@/api/team';

export default {
  data() {
    return {
      id: null,
      detail: {
        id: '',
        userName: '',
        avatar: '',
        gender: '',
        age: 0,
        area: '',
        startTime: '',
        endTime: '',
        content: '',
        status: 0,
        currentNumber: 0,
        maxNumber: 0,
        teamMemberList: []
      }
    };
  },

  onLoad(options) {
    this.id = options.id;
    this.loadTeamDetail();
  },

  methods: {
    async loadTeamDetail() {
      try {
        const res = await getTeamDetail(this.id);
        if (res.code === 0) {
          this.detail = res.data;
        } else {
          uni.showToast({
            title: '加载失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('加载队伍详情失败:', error);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      }
    },

    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    },
    handleChat(id) {
      uni.navigateTo({
        url: `/pages/team/chat?id=${id}`
      });
    },
    async handleJoin() {
      try {
        const res = await joinTeam(this.id);
        if (res.code === 0) {
          uni.showToast({
            title: res.data,
            icon: 'success'
          });
          // 重新加载详情
          this.loadTeamDetail();
        }
      } catch (error) {
        console.error('加入失败:', error);
        uni.showToast({
          title: '加入失败',
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style lang="scss">
.team-detail {
  padding: 30rpx;
  background-color: #f5f5f5;
  min-height: 100vh;
}

.user-info {
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 20rpx;
  border-radius: 12rpx;

  .avatar {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    margin-right: 20rpx;
  }

  .user-detail {
    flex: 1;

    .username {
      font-size: 30rpx;
      font-weight: bold;
      color: #333;
      display: block;
    }

    .user-tag {
      font-size: 24rpx;
      color: #999;
    }
  }

  .team-status {
    font-size: 24rpx;
    padding: 4rpx 16rpx;
    border-radius: 20rpx;

    &.recruiting {
      background-color: #e6f7ff;
      color: #1890ff;
    }

    &.completed {
      background-color: #f5f5f5;
      color: #999;
    }
  }
}

.trip-info {
  background-color: #fff;
  padding: 20rpx;
  border-radius: 12rpx;

  .destination,
  .time {
    display: flex;
    align-items: center;
    font-size: 28rpx;
    color: #666;
    margin-bottom: 10rpx;

    text {
      margin-left: 10rpx;
    }
  }
}

.content-section {
  background-color: #fff;
  padding: 20rpx;
  border-radius: 12rpx;

  .content-text {
    font-size: 28rpx;
    color: #333;
    line-height: 1.6;
  }
}

.team-progress {
  background-color: #fff;
  padding: 20rpx;
  border-radius: 12rpx;

  .progress-text {
    display: flex;
    justify-content: space-between;
    font-size: 24rpx;
    color: #666;
    margin-bottom: 10rpx;
  }
}

.members-list {
  background-color: #fff;
  padding: 20rpx;
  border-radius: 12rpx;
  margin-bottom: 120rpx;

  .section-title {
    font-size: 32rpx;
    font-weight: bold;
    color: #333;
    margin-bottom: 20rpx;
  }

  .member-item {
    margin-bottom: 20rpx;

    .member-info {
      display: flex;
      align-items: center;

      .avatar {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        margin-right: 20rpx;
      }

      .member-detail {
        .member-name {
          display: flex;
          align-items: center;
          font-size: 28rpx;
          color: #333;

          .role-tag {
            font-size: 24rpx;
            padding: 4rpx 16rpx;
            border-radius: 20rpx;
            margin-right: 12rpx;

            &.leader {
              background-color: #ffd591;
              color: #873800;
            }

            &.member {
              background-color: #91d5ff;
              color: #003a8c;
            }
          }
        }
      }
    }
  }
}

.bottom-button {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  background-color: #fff;
  box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);

  .join-btn {
    background-color: #50a8df;
    color: #fff;
    border-radius: 44rpx;
    font-size: 32rpx;
    height: 88rpx;
    line-height: 88rpx;

    &:disabled {
      background-color: #ccc;
    }
  }
}
</style>
