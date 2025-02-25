<template>
  <view class="team-container">
    <!-- 目的地筛选区 -->
    <view class="filter-section">
      <scroll-view class="filter-scroll" scroll-x="true" show-scrollbar="false">
        <view class="filter-item" v-for="(item, index) in destinations" :key="index" :class="{ active: currentDestination === item.id }" @tap="selectDestination(item.id)">
          {{ item.name }}
        </view>
      </scroll-view>
    </view>

    <!-- 组队列表 -->
    <scroll-view class="team-list" scroll-y="true">
      <view class="team-item" v-for="(item, index) in teamList" :key="index" @tap="goToTeamDetail(item.id)">
        <!-- 用户信息 -->
        <view class="user-info">
          <image class="avatar" :src="item.leaderAvatar" mode="aspectFill"></image>
          <view class="user-detail">
            <text class="username">{{ item.leaderName }}</text>
            <text class="user-tag">{{ item.gender }} · {{ item.age }}岁</text>
          </view>
          <view class="team-status" :class="item.status">{{ item.statusText }}</view>
        </view>

        <!-- 行程信息 -->
        <view class="trip-info">
          <view class="destination">
            <uni-icons type="location" size="16" color="#666"></uni-icons>
            <text>{{ item.destination }}</text>
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
            <text>已组{{ item.currentMembers }}人</text>
            <text>最多{{ item.maxMembers - item.currentMembers }}人</text>
          </view>
          <progress :percent="(item.currentMembers / item.maxMembers) * 100" stroke-width="4" activeColor="#87CEEB" backgroundColor="#eee" />
        </view>
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
export default {
  data() {
    return {
      currentDestination: 0,
      destinations: [
        { id: 0, name: '全部' },
        { id: 1, name: '日本' },
        { id: 2, name: '韩国' },
        { id: 3, name: '泰国' },
        { id: 4, name: '马来西亚' },
        { id: 5, name: '新加坡' },
        { id: 6, name: '越南' }
      ],
      teamList: [
        {
          id: 1,
          leaderAvatar: 'http://43.139.211.116:9000/financinglink/01563fbddc976d6db2f8e440c462ae6fddc5fa10ceef08132b6d7dc4665e3ca0.jpg',
          leaderName: '旅行者小王',
          gender: '男',
          age: 28,
          status: 'recruiting', // recruiting-招募中 full-已满 completed-已成团
          statusText: '招募中',
          destination: '东京-大阪-京都',
          startDate: '2024-04-01',
          endDate: '2024-04-07',
          description: '计划春季赏樱之旅，寻找3-4位志同道合的伙伴，一起游览日本关东关西经典景点',
          tags: ['赏樱', '美食', '购物', '拍照'],
          currentMembers: 2,
          maxMembers: 6
        },
        {
          id: 2,
          leaderAvatar: 'http://43.139.211.116:9000/financinglink/c3c32240d2a2b117fd3f0e767e46c91a7ee4198fc3e1ce63664996ff8ae214a9.jpg',
          leaderName: '小美',
          gender: '女',
          age: 25,
          status: 'recruiting',
          statusText: '招募中',
          destination: '首尔-釜山',
          startDate: '2024-05-01',
          endDate: '2024-05-06',
          description: '韩国美食探店之旅，想找几个吃货一起，美食美景两不误',
          tags: ['美食', '购物', 'K-POP'],
          currentMembers: 3,
          maxMembers: 4
        }
      ]
    };
  },
  methods: {
    selectDestination(id) {
      this.currentDestination = id;
      // TODO: 根据目的地筛选列表
    },
    goToTeamDetail(id) {
      uni.navigateTo({
        url: `/pages/team/detail?id=${id}`
      });
    },
    goToPublish() {
      uni.navigateTo({
        url: '/pages/team/publish'
      });
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
  padding: 20rpx 0;
  position: sticky;
  top: 0;
  z-index: 100;
}

.filter-scroll {
  white-space: nowrap;
  padding: 0 20rpx;
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
