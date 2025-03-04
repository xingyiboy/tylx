<template>
  <view class="team-container">
    <!-- 搜索和筛选导航 -->
    <view class="search-filter-wrapper">
      <!-- 搜索框 -->
      <view class="search-bar">
        <view class="search-input">
          <uni-icons type="search" size="16" color="#999"></uni-icons>
          <input type="text" v-model="queryParams.detail" placeholder="搜索队伍简介" @input="handleSearch" />
          <view v-if="queryParams.detail" class="clear-btn" @click="clearSearch">
            <uni-icons type="clear" size="16" color="#999"></uni-icons>
          </view>
        </view>
      </view>

      <!-- 地区筛选下拉菜单 -->
      <view class="filter-dropdown" @click="showDropdown = !showDropdown">
        <text>{{ currentLocation || '全国' }}</text>
        <uni-icons :type="showDropdown ? 'top' : 'bottom'" size="14" color="#666"></uni-icons>
      </view>

      <!-- 下拉菜单内容 -->
      <view class="dropdown-content" v-if="showDropdown">
        <scroll-view scroll-y class="location-list">
          <view class="location-item" @click="selectLocation('')">
            <text :class="{ active: currentLocation === '' }">全国</text>
          </view>
          <view class="location-item" v-for="(item, index) in topDestinations" :key="index" @click="selectLocation(item)">
            <text :class="{ active: currentLocation === item.name }">{{ item.name }}</text>
          </view>
        </scroll-view>
      </view>
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
import { getTeamPage } from '@/api/team';

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
        pageSize: 10,
        destinationId: '',
        title: ''
      },
      showDropdown: false,
      currentLocation: ''
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

        const res = await getTeamPage(params);
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
    },
    handleSearch() {
      this.queryParams.pageNo = 1;
      this.loadTeamList();
    },
    clearSearch() {
      this.queryParams.title = '';
      this.queryParams.pageNo = 1;
      this.loadTeamList();
    },
    selectLocation(item) {
      this.showDropdown = false;
      this.currentLocation = item ? item.name : '';
      this.currentTopDestination = item ? item.id : '';
      this.selectTopDestination(item ? item.id : '');
    }
  }
};
</script>

<style lang="scss">
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

.search-filter-wrapper {
  position: sticky;
  top: 0;
  z-index: 99;
  background: #fff;
  padding: 20rpx;
  display: flex;
  align-items: center;
  gap: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
}

.search-bar {
  flex: 1;
}

.search-input {
  display: flex;
  align-items: center;
  background-color: #f5f7fa;
  padding: 12rpx 24rpx;
  border-radius: 32rpx;

  input {
    flex: 1;
    margin: 0 20rpx;
    font-size: 28rpx;
  }
}

.clear-btn {
  padding: 10rpx;
}

.filter-dropdown {
  display: flex;
  align-items: center;
  gap: 8rpx;
  padding: 12rpx 24rpx;
  background: #f5f7fa;
  border-radius: 32rpx;
  font-size: 28rpx;
  color: #333;
}

.dropdown-content {
  position: absolute;
  top: 100%;
  right: 20rpx;
  width: 240rpx;
  background: #fff;
  border-radius: 16rpx;
  box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.location-list {
  max-height: 800rpx;
}

.location-item {
  padding: 20rpx 24rpx;
  font-size: 28rpx;
  color: #333;

  text {
    &.active {
      color: #4080ff;
      font-weight: 500;
    }
  }

  &:active {
    background-color: #f5f7fa;
  }
}
</style>
