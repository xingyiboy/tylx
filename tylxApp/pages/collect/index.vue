<template>
  <view class="collect-container">
    <!-- 收藏列表(可滚动区域) -->
    <scroll-view scroll-y class="scroll-content" @scrolltolower="loadMore" refresher-enabled
      :refresher-triggered="isRefreshing" @refresherrefresh="refresh">
      <view class="collect-list">
        <!-- 收藏项 -->
        <view class="collect-item" v-for="(item, index) in collectList" :key="index" @click="goToDetail(item.id)">
          <!-- 左侧图片 -->
          <image :src="item.picture || '/static/images/default.png'" class="collect-image" mode="aspectFill"></image>

          <!-- 右侧内容 -->
          <view class="collect-content">
            <text class="collect-title">{{ item.title }}</text>
            <view class="stats-info">
              <view class="stats-group">
                <view class="stat-item">
                  <uni-icons type="eye" size="12" color="#999"></uni-icons>
                  <text>{{ item.views || 0 }}</text>
                </view>
                <view class="stat-item">
                  <uni-icons type="chat" size="12" color="#999"></uni-icons>
                  <text>{{ item.comment || 0 }}</text>
                </view>
                <view class="stat-item">
                  <uni-icons type="heart" size="12" color="#999"></uni-icons>
                  <text>{{ item.likes || 0 }}</text>
                </view>
              </view>
              <text class="create-time">{{ formatDate(item.createTime) }}</text>
            </view>
          </view>
        </view>

        <!-- 加载更多提示 -->
        <view class="load-more" v-if="collectList.length > 0">
          <text v-if="hasMore">上拉加载更多</text>
          <text v-else>没有更多数据了</text>
        </view>

        <!-- 无数据提示 -->
        <view v-if="collectList.length === 0" class="empty-tip">
          <image src="/static/images/empty.png" mode="aspectFit"></image>
          <text>暂无收藏内容</text>
        </view>
      </view>
    </scroll-view>
  </view>
</template>

<script>
import { getEducationCollectPage } from '@/api/education';

export default {
  data() {
    return {
      collectList: [],
      isRefreshing: false,
      queryParams: {
        pageNo: 1,
        pageSize: 10
      },
      total: 0,
      hasMore: true
    };
  },
  onLoad() {
    this.loadCollects();
  },
  methods: {
    async loadCollects(isLoadMore = false) {
      try {
        const res = await getEducationCollectPage(this.queryParams);
        this.total = res.data.total;
        this.hasMore = this.queryParams.pageNo * this.queryParams.pageSize < this.total;

        if (isLoadMore) {
          this.collectList = [...this.collectList, ...res.data.list];
        } else {
          this.collectList = res.data.list;
        }
      } catch (error) {
        uni.showToast({
          title: '获取收藏列表失败',
          icon: 'none'
        });
      }
    },

    goToDetail(id) {
      uni.navigateTo({
        url: `/pages/education/detail?id=${id}`
      });
    },

    async loadMore() {
      if (!this.hasMore) return;
      this.queryParams.pageNo++;
      await this.loadCollects(true);
    },

    async refresh() {
      this.isRefreshing = true;
      this.queryParams.pageNo = 1;
      await this.loadCollects();
      this.isRefreshing = false;
    },

    formatDate(timestamp) {
      if (!timestamp) return '';
      const date = new Date(timestamp);
      return `${date.getMonth() + 1}月${date.getDate()}日 ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`;
    }
  }
};
</script>

<style>
.collect-container {
  height: 100vh;
  display: flex;
  flex-direction: column;
  background-color: #f5f5f5;
}

.scroll-content {
  flex: 1;
  height: 0;
}

.collect-list {
  padding: 20rpx;
}

.collect-item {
  display: flex;
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.collect-image {
  width: 200rpx;
  height: 140rpx;
  border-radius: 8rpx;
  margin-right: 20rpx;
}

.collect-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.collect-title {
  font-size: 28rpx;
  color: #333;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
}

.stats-info {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10rpx;
}

.stats-group {
  display: flex;
  gap: 16rpx;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4rpx;
  font-size: 24rpx;
  color: #999;
}

.create-time {
  font-size: 24rpx;
  color: #999;
}

.load-more {
  text-align: center;
  padding: 30rpx;
  color: #999;
  font-size: 24rpx;
}

.empty-tip {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 100rpx 0;
}

.empty-tip image {
  width: 200rpx;
  height: 200rpx;
  margin-bottom: 20rpx;
}

.empty-tip text {
  font-size: 28rpx;
  color: #999;
}
</style>
