<template>
  <view class="detail-container">
    <!-- 帖子内容 -->
    <view class="post-content">
      <view class="post-header">
        <view class="user-info">
          <image :src="detail.avatar || '/static/avatar/default.png'" class="avatar"></image>
          <view class="user-meta">
            <text class="username">{{ detail.userName || '用户' }}</text>
            <text class="time">{{ formatDate(detail.createTime) }}</text>
          </view>
        </view>
      </view>

      <view class="post-main">
        <view class="title">{{ detail.title }}</view>

        <mp-html :content="detail.content" />
        <view class="post-images" v-if="detail.picture">
          <image v-for="(pic, picIndex) in detail.picture.split(',')" :key="picIndex" :src="pic" mode="aspectFill"></image>
        </view>
        <view class="images" v-if="detail.images">
          <image v-for="(img, index) in detail.images.split(',')" :key="index" :src="img" mode="widthFix" @click="previewImage(img, detail.images.split(','))"></image>
        </view>
      </view>

      <view class="post-footer">
        <view class="action-bar">
          <view class="action-item">
            <uni-icons type="eye" size="20" color="#8a9ab0"></uni-icons>
            <text>{{ detail.view || 0 }}</text>
          </view>
          <view class="action-item" @click="handleLike">
            <uni-icons :type="isLiked ? 'heart-filled' : 'heart'" size="20" :color="isLiked ? '#ff4d4f' : '#8a9ab0'"></uni-icons>
            <text :class="{ liked: isLiked }">{{ detail.likes || 0 }}</text>
          </view>
          <view class="action-item">
            <uni-icons type="chat" size="20" color="#8a9ab0"></uni-icons>
            <text>{{ detail.comment || 0 }}</text>
          </view>
          <view class="action-item" @click="showRewardDialog">
            <uni-icons type="gift" size="20" color="#8a9ab0"></uni-icons>
            <text>打赏 {{ detail.totalMoney ? '¥' + detail.totalMoney : '¥0' }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 评论列表 -->
    <view class="comments-section">
      <view class="section-title">评论 ({{ comments.length }})</view>
      <view class="comment-list">
        <view class="comment-item" v-for="(item, index) in comments" :key="index">
          <image :src="item.avatar || '/static/avatar/default.png'" class="comment-avatar"></image>
          <view class="comment-content">
            <view class="comment-header">
              <text class="comment-user">{{ item.userName || '用户' }}</text>
              <text class="comment-time">{{ formatDate(item.createTime) }}</text>
            </view>
            <view class="comment-text">{{ item.content }}</view>
          </view>
        </view>
      </view>
    </view>

    <!-- 评论输入框 -->
    <view class="comment-input">
      <input type="text" v-model="commentText" placeholder="说点什么..." placeholder-style="color: #8a9ab0;" @confirm="submitComment" />
      <button @click="submitComment" :disabled="!commentText.trim()" :class="{ active: commentText.trim() }">发送</button>
    </view>

    <!-- 添加打赏弹窗 -->
    <uni-popup ref="rewardPopup" type="dialog">
      <uni-popup-dialog mode="input" title="打赏金额" placeholder="请输入打赏金额" :before-close="true" @confirm="handleReward" @close="closeRewardDialog"></uni-popup-dialog>
    </uni-popup>
  </view>
</template>

<script>
import { getForumDetail, getForumComments, createComment, createLike, isLike, createReward } from '@/api/forum';

export default {
  data() {
    return {
      id: null,
      detail: {},
      comments: [],
      commentText: '',
      isLiked: false,
      rewardAmount: ''
    };
  },
  onLoad(options) {
    this.id = options.id;
    this.loadDetail();
    this.loadComments();
    this.checkLikeStatus();
  },
  methods: {
    async loadDetail() {
      try {
        const res = await getForumDetail(this.id);
        this.detail = res.data;
      } catch (error) {
        uni.showToast({
          title: '获取详情失败',
          icon: 'none'
        });
      }
    },
    async loadComments() {
      try {
        const res = await getForumComments(this.id);
        this.comments = res.data;
      } catch (error) {
        uni.showToast({
          title: '获取评论失败',
          icon: 'none'
        });
      }
    },
    async checkLikeStatus() {
      try {
        const res = await isLike(this.id);
        this.isLiked = res.data;
      } catch (error) {
        console.error('获取点赞状态失败', error);
      }
    },
    async handleLike() {
      try {
        const userInfo = uni.getStorageSync('userInfo');
        await createLike({
          forumId: this.id,
          userId: userInfo.id
        });
        // 更新点赞状态和数量
        this.isLiked = !this.isLiked;
        this.detail.likes += this.isLiked ? 1 : -1;

        uni.showToast({
          title: this.isLiked ? '点赞成功' : '取消点赞成功',
          icon: 'success'
        });
      } catch (error) {
        uni.showToast({
          title: '操作失败',
          icon: 'none'
        });
      }
    },
    async submitComment() {
      if (!this.commentText.trim()) return;

      try {
        const userInfo = uni.getStorageSync('userInfo');
        await createComment({
          forumId: this.id,
          userId: userInfo.id,
          content: this.commentText.trim()
        });

        this.commentText = '';
        // 刷新评论列表和数量
        this.loadComments();
        this.loadDetail();

        uni.showToast({
          title: '评论成功',
          icon: 'success'
        });
      } catch (error) {
        uni.showToast({
          title: '评论失败',
          icon: 'none'
        });
      }
    },
    previewImage(current, urls) {
      uni.previewImage({
        current,
        urls
      });
    },
    formatDate(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');
      const hour = String(date.getHours()).padStart(2, '0');
      const minute = String(date.getMinutes()).padStart(2, '0');
      return `${year}-${month}-${day} ${hour}:${minute}`;
    },
    showRewardDialog() {
      this.$refs.rewardPopup.open();
    },
    closeRewardDialog() {
      this.$refs.rewardPopup.close();
    },
    async handleReward(amount) {
      if (!amount || isNaN(amount) || amount <= 0) {
        uni.showToast({
          title: '请输入有效金额',
          icon: 'none'
        });
        return;
      }

      try {
        const userInfo = uni.getStorageSync('userInfo');
        const res = await createReward({
          forumId: this.id,
          money: parseFloat(amount)
        });

        uni.showToast({
          title: res.data || '打赏成功',
          icon: 'success'
        });

        this.closeRewardDialog();
        // 刷新详情
        this.loadDetail();
      } catch (error) {
        uni.showToast({
          title: '打赏失败',
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style lang="scss">
.detail-container {
  min-height: 100vh;
  background-color: #f5f7fa;
  padding-bottom: 120rpx;
}

.post-content {
  background: #fff;
  margin-bottom: 20rpx;
  padding: 30rpx;
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30rpx;
}

.user-info {
  display: flex;
  align-items: center;
}

.avatar {
  width: 88rpx;
  height: 88rpx;
  border-radius: 50%;
  border: 2rpx solid #eee;
  margin-right: 20rpx;
}

.user-meta {
  display: flex;
  flex-direction: column;
  gap: 6rpx;
}

.username {
  font-size: 30rpx;
  color: #333;
  font-weight: 500;
}

.time {
  font-size: 24rpx;
  color: #8a9ab0;
}

.post-tag {
  font-size: 24rpx;
  color: #4080ff;
  background: rgba(64, 128, 255, 0.1);
  padding: 6rpx 20rpx;
  border-radius: 20rpx;
}

.post-main {
  margin-bottom: 30rpx;
}

.title {
  font-size: 36rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 24rpx;
  line-height: 1.4;
}

.content {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
  margin-bottom: 24rpx;
}

.images {
  display: flex;
  flex-direction: column;
  gap: 16rpx;

  image {
    width: 100%;
    border-radius: 12rpx;
    box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.05);
  }
}

.action-bar {
  display: flex;
  justify-content: flex-end;
  gap: 40rpx;
  padding-top: 20rpx;
  border-top: 2rpx solid #f5f7fa;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 8rpx;

  text {
    font-size: 26rpx;
    color: #8a9ab0;

    &.liked {
      color: #ff4d4f;
    }
  }

  &:last-child {
    margin-left: auto;
  }
}

.comments-section {
  background: #fff;
  padding: 30rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 30rpx;
}

.comment-item {
  display: flex;
  margin-bottom: 30rpx;

  &:last-child {
    margin-bottom: 0;
  }
}

.comment-avatar {
  width: 72rpx;
  height: 72rpx;
  border-radius: 50%;
  margin-right: 20rpx;
  border: 2rpx solid #eee;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 8rpx;
}

.comment-user {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.comment-time {
  font-size: 24rpx;
  color: #8a9ab0;
}

.comment-text {
  font-size: 28rpx;
  color: #666;
  line-height: 1.6;
}

.comment-input {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  padding: 20rpx;
  background: #fff;
  border-top: 2rpx solid #f0f2f5;
  box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.05);

  input {
    flex: 1;
    height: 72rpx;
    background: #f5f7fa;
    border-radius: 36rpx;
    padding: 0 30rpx;
    margin-right: 20rpx;
    font-size: 28rpx;
    color: #333;
  }

  button {
    width: 120rpx;
    height: 72rpx;
    line-height: 72rpx;
    text-align: center;
    background: #e0e7ff;
    color: #8a9ab0;
    border-radius: 36rpx;
    font-size: 28rpx;
    transition: all 0.3s ease;

    &.active {
      background: #4080ff;
      color: #fff;
    }

    &:active {
      transform: scale(0.95);
    }
  }
}

.post-images {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 10rpx;
  border-radius: 12rpx;
  overflow: hidden;

  image {
    width: 100%;
    height: 200rpx;
    object-fit: cover;
  }
}

/* 添加打赏金额样式 */
.action-item text {
  margin-left: 4rpx;
  font-size: 24rpx;
  color: #8a9ab0;
}

.action-item .money {
  color: #ff9500;
  font-weight: 500;
}
</style>
