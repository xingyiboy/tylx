<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view class="search-header">
      <view class="search-bar">
        <view class="search-input">
          <uni-icons type="search" size="16" color="#999"></uni-icons>
          <input type="text" placeholder="搜索目的地" />
        </view>
      </view>
      <!-- 轮播图 -->
      <swiper class="banner" circular :indicator-dots="true" :autoplay="true" :interval="3000" :duration="1000">
        <swiper-item>
          <image src="http://43.139.211.116:9000/financinglink/01563fbddc976d6db2f8e440c462ae6fddc5fa10ceef08132b6d7dc4665e3ca0.jpg" mode="aspectFill"></image>
        </swiper-item>
        <swiper-item>
          <image src="http://43.139.211.116:9000/financinglink/c3c32240d2a2b117fd3f0e767e46c91a7ee4198fc3e1ce63664996ff8ae214a9.jpg" mode="aspectFill"></image>
        </swiper-item>
      </swiper>
    </view>

    <!-- 功能导航 -->
    <!-- <view class="nav-section">
      <view class="nav-item" @click="navigateTo('/pages/guide/index')">
        <image src="/static/images/nav-guide.png"></image>
        <text>攻略</text>
      </view>
      <view class="nav-item" @click="navigateTo('/pages/hotel/index')">
        <image src="/static/images/nav-hotel.png"></image>
        <text>组队</text>
      </view>
      <view class="nav-item" @click="navigateTo('/pages/mall/index')">
        <image src="/static/images/nav-mall.png"></image>
        <text>商城</text>
      </view>
      <view class="nav-item" @click="navigateTo('/pages/travel-notes/index')">
        <image src="/static/images/nav-notes.png"></image>
        <text>游记</text>
      </view>
    </view> -->

    <!-- 精品推荐 -->
    <view class="recommend-section">
      <view class="section-title">精品推荐</view>
      <view class="recommend-list">
        <view class="recommend-item" v-for="(item, index) in recommendList" :key="index" @click="goToDetail(item)">
          <image :src="item.image" mode="aspectFill"></image>
          <view class="item-info">
            <text class="item-name">{{ item.name }}</text>
            <text class="item-sales">已售 {{ item.sales }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      recommendList: [
        {
          id: 1,
          name: '东京迪士尼海洋',
          image: 'http://43.139.211.116:9000/financinglink/01563fbddc976d6db2f8e440c462ae6fddc5fa10ceef08132b6d7dc4665e3ca0.jpg',
          sales: 4526
        },
        {
          id: 2,
          name: '浅草寺攻略',
          image: 'http://43.139.211.116:9000/financinglink/c3c32240d2a2b117fd3f0e767e46c91a7ee4198fc3e1ce63664996ff8ae214a9.jpg',
          sales: 4526
        },
        {
          id: 3,
          name: '汉堡仓库城攻略',
          image: 'http://43.139.211.116:9000/financinglink/c3c32240d2a2b117fd3f0e767e46c91a7ee4198fc3e1ce63664996ff8ae214a9.jpg',
          sales: 3856
        },
        {
          id: 4,
          name: '德国石林',
          image: 'http://43.139.211.116:9000/financinglink/01563fbddc976d6db2f8e440c462ae6fddc5fa10ceef08132b6d7dc4665e3ca0.jpg',
          sales: 2967
        }
      ]
    };
  },
  methods: {
    navigateTo(url) {
      uni.navigateTo({
        url
      });
    },
    goToDetail(item) {
      uni.navigateTo({
        url: `/pages/detail/index?id=${item.id}`
      });
    }
  }
};
</script>

<style>
.container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.search-header {
  background: linear-gradient(180deg, #87ceeb, #b0e0e6);
  height: 340rpx;
  margin-bottom: 100rpx;
}

.search-bar {
  padding: 20rpx;
}

.search-input {
  display: flex;
  align-items: center;
  background-color: #fff;
  padding: 10rpx 20rpx;
  border-radius: 30rpx;
}

.search-input input {
  flex: 1;
  margin-left: 10rpx;
  font-size: 28rpx;
}

.banner {
  margin-top: -200rpx;
  width: 90%;
  height: 300rpx;
  margin: 0 auto;
  border-radius: 20rpx;
  overflow: hidden;
  transform: translateY(20rpx);
}

.banner image {
  width: 100%;
  height: 100%;
  border-radius: 20rpx;
}

.nav-section {
  display: flex;
  justify-content: space-around;
  padding: 60rpx 0 30rpx;
  background-color: #fff;
  margin-top: -20rpx;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.nav-item image {
  width: 80rpx;
  height: 80rpx;
  margin-bottom: 10rpx;
}

.nav-item text {
  font-size: 24rpx;
  color: #333;
}

.recommend-section {
  padding: 20rpx;
}

.section-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 20rpx;
  text-align: center;
}

.recommend-list {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20rpx;
}

.recommend-item {
  overflow: hidden;
}

.recommend-item image {
  width: 100%;
  height: 200rpx;
  border-radius: 12rpx;
}

.item-info {
  padding: 10rpx 0;
  text-align: center;
}

.item-name {
  font-size: 28rpx;
  color: #333;
  display: block;
}

.item-sales {
  font-size: 24rpx;
  color: #999;
  margin-top: 6rpx;
  display: block;
}

.tab-bar {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  height: 100rpx;
  background-color: #fff;
  display: flex;
  justify-content: space-around;
  align-items: center;
  border-top: 1rpx solid #eee;
}

.tab-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.tab-item image {
  width: 40rpx;
  height: 40rpx;
  margin-bottom: 4rpx;
}

.tab-item text {
  font-size: 20rpx;
  color: #999;
}

.tab-item.active text {
  color: #87ceeb;
}
</style>
