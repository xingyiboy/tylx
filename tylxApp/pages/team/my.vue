<template>
  <view class="team-container">
    <!-- 目的地筛选区 -->
    <view class="filter-section">
      <!-- 一级目的地 -->
      <scroll-view class="filter-scroll" scroll-x="true" show-scrollbar="false">
        <view class="filter-item" v-for="(item, index) in topDestinations" :key="index" :class="{ active: currentTopDestination === item.id }" @tap="selectTopDestination(item.id)">
          {{ item.name }}
        </view>
      </scroll-view>

      <!-- 二级目的地 -->
      <scroll-view class="filter-scroll" scroll-x="true" show-scrollbar="false" v-if="subDestinations.length">
        <view class="filter-item" v-for="(item, index) in subDestinations" :key="index" :class="{ active: currentDestination === item.id }" @tap="selectDestination(item.id)">
          {{ item.name }}
        </view>
      </scroll-view>
    </view>

    <!-- 组队列表 -->
    <scroll-view class="team-list" scroll-y="true" @scrolltolower="loadMore" @refresherrefresh="refresh" refresher-enabled :refresher-triggered="isRefreshing">
      <view class="team-item" v-for="(item, index) in teamList" :key="index" @tap="goToTeamDetail(item.id)">
        <!-- 用户信息 -->
        <view class="user-info">
          <image class="avatar" :src="item.leaderAvatar" mode="aspectFill"></image>
          <view class="user-detail">
            <text class="username">{{ item.leaderName }}</text>
            <text class="user-tag">{{ item.gender }} · {{ item.age || '' }}岁</text>
          </view>
          <view class="team-status" :class="item.status">{{ item.statusText }}</view>
        </view>

        <!-- 行程信息 -->
        <view class="trip-info">
          <view class="destination">
            <uni-icons type="location" size="16" color="#666"></uni-icons>
            <text>{{ getDestinationsName(item.destinationId) + '·' + item.destination }}</text>
          </view>
          <view class="time">
            <uni-icons type="calendar" size="16" color="#666"></uni-icons>
            <text>{{ item.startDate }} - {{ item.endDate }}</text>
          </view>
        </view>

        <!-- 组队详情 -->
        <view class="team-detail">
          <text class="detail-text">{{ item.description }}</text>
        </view>

        <!-- 组队进度 -->
        <view class="team-progress">
          <view class="progress-text">
            <text>已组{{ item.currentMembers - 1 }}人</text>
            <text>最多{{ item.maxMembers }}人</text>
          </view>
          <progress :percent="((item.currentMembers - 1) / item.maxMembers) * 100" stroke-width="4" activeColor="#87CEEB" backgroundColor="#eee" />
        </view>
      </view>

      <!-- 加载更多 -->
      <view class="loading-more" v-if="hasMore">
        <text>加载中...</text>
      </view>
      <view class="no-more" v-else>
        <text>没有更多了</text>
      </view>
    </scroll-view>

    <!-- 发布按钮 -->
    <view class="publish-btn" @tap="goToPublish">
      <uni-icons type="plus" size="20" color="#fff"></uni-icons>
      <text>发布组队</text>
    </view>
  </view>
</template>

<script>
import { getDestinationList } from '@/api/destination';
import { getMyTeamPage } from '@/api/team';

export default {
  data() {
    return {
      currentTopDestination: 0, // 当前选中的一级目的地
      currentDestination: 0, // 当前选中的二级目的地
      destinations: [], // 所有目的地数据
      topDestinations: [], // 一级目的地
      subDestinations: [], // 二级目的地
      teamList: [],
      isRefreshing: false,
      hasMore: true,
      newList: [],
      queryParams: {
        pageNo: 1,
        pageSize: 10
      }
    };
  },

  onLoad() {
    this.loadDestinations();
    this.loadTeamList();
  },

  methods: {
    // 加载目的地列表
    async loadDestinations() {
      try {
        const res = await getDestinationList({
          pageNo: 1,
          pageSize: 100
        });

        if (res.code === 0) {
          this.destinations = res.data;

          // 处理一级目的地
          this.topDestinations = [
            { id: 0, name: '全部' },
            ...this.destinations
              .filter((item) => !item.parentId) // 筛选 parentId 为 0 或空的记录
              .map((item) => ({
                id: item.id,
                name: item.name
              }))
          ];
        }
      } catch (error) {
        console.error('加载目的地列表失败:', error);
        uni.showToast({
          title: '加载目的地失败',
          icon: 'none'
        });
      }
    },

    // 选择一级目的地
    selectTopDestination(id) {
      this.currentTopDestination = id;
      if (!id) {
        // 点击"全部"时清空二级目的地
        this.subDestinations = [];
        this.currentDestination = 0;
      } else {
        // 加载二级目的地
        this.subDestinations = [
          ...this.destinations
            .filter((item) => item.parentId === id)
            .map((item) => ({
              id: item.id,
              name: item.name
            }))
        ];
      }
      // 重置查询参数
      this.queryParams.pageNo = 1;
      this.loadTeamList();
    },

    // 选择二级目的地
    selectDestination(id) {
      this.currentDestination = id;
      this.queryParams.pageNo = 1;
      this.loadTeamList();
    },

    goToTeamDetail(id) {
      uni.navigateTo({
        url: `/pages/team/index?id=${id}`
      });
    },
    goToPublish() {
      uni.navigateTo({
        url: '/pages/team/add'
      });
    },
    async refresh() {
      this.isRefreshing = true;
      this.queryParams.pageNo = 1;
      await this.loadTeamList();
      this.isRefreshing = false;
    },
    async loadMore() {
      if (!this.hasMore) return;
      this.queryParams.pageNo++;
      await this.loadTeamList();
    },
    async loadTeamList() {
      try {
        const params = { ...this.queryParams };
        // 根据选择的分类设置查询参数
        if (this.currentDestination !== 0) {
          params.destinationId = this.currentDestination;
        } else if (this.currentTopDestination !== 0) {
          params.destinationId = this.currentTopDestination;
        }

        const res = await getMyTeamPage(params);
        if (res.code === 0) {
          this.newList = res.data.list.map((item) => ({
            id: item.id,
            leaderAvatar: item.avatar || '/static/default-avatar.png',
            leaderName: item.userName || '未知用户',
            destination: item.area,
            startDate: this.formatDate(item.startTime),
            endDate: this.formatDate(item.endTime),
            description: item.detail,
            currentMembers: item.currentNumber || 1,
            maxMembers: item.maxNumber,
            status: item.status == 0 ? 'recruiting' : 'completed',
            statusText: item.status == 0 ? '招募中' : '已结束',
            gender: item.gender,
            age: item.age,
            destinationId: item.destinationId
          }));
          if (this.queryParams.pageNo == 1) {
            this.teamList = this.newList;
          } else {
            this.teamList = [...this.teamList, ...this.newList];
          }
          this.hasMore = this.newList.length === this.queryParams.pageSize;
        }
      } catch (error) {
        console.error('加载组队列表失败:', error);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      }
    },
    getDestinationsName(value) {
      const destinations = this.destinations.find((item) => item.id == value);
      return destinations ? destinations.name : '';
    },
    formatDate(dateStr) {
      if (!dateStr) return '';
      const date = new Date(dateStr);
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
    }
  }
};
</script>

<style>
.team-container {
  min-height: 100vh;
  background-color: #f5f5f5;
  padding-bottom: 120rpx;
}

.filter-section {
  background-color: #fff;
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 100;
  display: flex;
  flex-direction: column;
}

.filter-scroll {
  white-space: nowrap;
  padding: 20rpx;
}

.filter-scroll:first-child {
  border-bottom: 1rpx solid #f0f2f5;
}

.filter-item {
  display: inline-block;
  padding: 10rpx 30rpx;
  margin-right: 20rpx;
  border-radius: 30rpx;
  font-size: 28rpx;
  color: #666;
  background-color: #f5f5f5;
}

.filter-item.active {
  background-color: #87ceeb;
  color: #fff;
}

.team-list {
  padding: 20rpx;
}

.team-item {
  background-color: #fff;
  border-radius: 12rpx;
  padding: 20rpx;
  margin-bottom: 20rpx;
}

.user-info {
  display: flex;
  align-items: center;
  margin-bottom: 20rpx;
}

.avatar {
  width: 80rpx;
  height: 80rpx;
  border-radius: 50%;
  margin-right: 20rpx;
}

.user-detail {
  flex: 1;
}

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

.team-status {
  font-size: 24rpx;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
}

.team-status.recruiting {
  background-color: #e6f7ff;
  color: #1890ff;
}

.trip-info {
  margin-bottom: 20rpx;
}

.destination,
.time {
  display: flex;
  align-items: center;
  font-size: 28rpx;
  color: #666;
  margin-bottom: 10rpx;
}

.destination text,
.time text {
  margin-left: 10rpx;
}

.team-detail {
  margin-bottom: 20rpx;
}

.detail-text {
  font-size: 28rpx;
  color: #333;
  line-height: 1.5;
  margin-bottom: 16rpx;
}

.tags {
  display: flex;
  flex-wrap: wrap;
}

.tag {
  font-size: 24rpx;
  color: #666;
  background-color: #f5f5f5;
  padding: 4rpx 16rpx;
  border-radius: 20rpx;
  margin-right: 16rpx;
  margin-bottom: 10rpx;
}

.team-progress {
  border-top: 1rpx solid #eee;
  padding-top: 20rpx;
}

.progress-text {
  display: flex;
  justify-content: space-between;
  font-size: 24rpx;
  color: #666;
  margin-bottom: 10rpx;
}

.publish-btn {
  position: fixed;
  bottom: 40rpx;
  left: 50%;
  transform: translateX(-50%);
  background-color: #87ceeb;
  color: #fff;
  padding: 20rpx 40rpx;
  border-radius: 40rpx;
  display: flex;
  align-items: center;
  box-shadow: 0 4rpx 16rpx rgba(135, 206, 235, 0.4);
}

.publish-btn text {
  margin-left: 10rpx;
  font-size: 28rpx;
}
</style>
