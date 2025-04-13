<template>
  <view class="container">
    <!-- 搜索栏 -->
    <view :class="!searchKeyword ? 'search-header' : ''">
      <view class="search-bar">
        <view class="search-input">
          <uni-icons type="search" size="16" color="#999"></uni-icons>
          <input type="text" v-model="searchKeyword" placeholder="搜索目的地" @input="handleSearch" />
          <view v-if="searchKeyword" class="clear-btn" @click="clearSearch">
            <uni-icons type="clear" size="16" color="#999"></uni-icons>
          </view>
        </view>
      </view>
    </view>

    <!-- 添加无搜索结果提示 -->
    <view v-if="searchKeyword && searchResults.length === 0" class="no-result">
      <text class="no-result-text">暂无相关搜索结果</text>
    </view>

    <view v-for="item in searchResults" :key="item.id">
      <view class="search-item" @click="handleSelectDestination(item)">
        <block v-if="item.picture">
          <image v-for="(pic, picIndex) in item.picture.split(',')" :key="picIndex" :src="pic" mode="aspectFill" class="item-image"></image>
        </block>
        <view class="item-content">
          <view class="item-name">{{ item.name }}</view>
          <view class="item-detail">{{ item.detail }}</view>
        </view>
      </view>
    </view>

    <!-- 主要内容区域 -->
    <view v-show="!searchKeyword">
      <!-- 轮播图 -->
      <swiper class="banner" circular :indicator-dots="true" :autoplay="true" :interval="3000" :duration="1000">
        <swiper-item>
          <image src="http://43.139.211.116:9000/financinglink/01563fbddc976d6db2f8e440c462ae6fddc5fa10ceef08132b6d7dc4665e3ca0.jpg" mode="aspectFill"></image>
        </swiper-item>
        <swiper-item>
          <image src="http://43.139.211.116:9000/financinglink/c3c32240d2a2b117fd3f0e767e46c91a7ee4198fc3e1ce63664996ff8ae214a9.jpg" mode="aspectFill"></image>
        </swiper-item>
      </swiper>

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
            <swiper class="swiper-box" circular :indicator-dots="false" :autoplay="true" :interval="3000" :duration="1000">
              <swiper-item v-for="(pic, picIndex) in item.picture.split(',')" :key="picIndex">
                <image :src="pic" mode="aspectFill" class="item-image"></image>
              </swiper-item>
            </swiper>
            <view class="item-info">
              <text class="item-name">{{ item.name }}</text>
              <text class="item-sales">{{ item.view }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getDestinationPage } from '@/api/destination';
import { getDestinationPageScenery } from '@/api/destination';
import { getForumPage } from '@/api/forum';

export default {
  data() {
    return {
      searchKeyword: '',
      searchResults: [],
      recommendList: []
    };
  },

  // 页面每次显示时
  onShow() {
    // 如果不是首次加载，不重新获取数据
    this.loadRecommendList();
  },

  methods: {
    async loadRecommendList() {
      try {
        // 获取景点推荐
        const sceneryRes = await getDestinationPageScenery({
          pageNo: 1,
          pageSize: 100
        });

        // 获取论坛推荐
        const forumRes = await getForumPage({
          pageNo: 1,
          pageSize: 100,
          isHome: 1
        });

        let recommendItems = [];

        // 处理景点数据
        if (sceneryRes.code === 0 && sceneryRes.data.list) {
          const sceneryItems = sceneryRes.data.list.map((item) => ({
            id: item.id,
            name: item.name,
            picture: item.picture || 'default-image-url', // 设置默认图片
            sales: item.viewCount || 0,
            type: 'scenery' // 标记类型为景点
          }));
          recommendItems = recommendItems.concat(sceneryItems);
        }

        // 处理论坛数据
        if (forumRes.code === 0 && forumRes.data.list) {
          const forumItems = forumRes.data.list.map((item) => ({
            id: item.id,
            name: item.title + '攻略',
            picture: item.picture || 'default-image-url', // 设置默认图片
            sales: item.viewCount || 0,
            type: 'forum' // 标记类型为论坛
          }));
          recommendItems = recommendItems.concat(forumItems);
        }

        // 随机打乱数组顺序
        this.recommendList = recommendItems;
      } catch (error) {
        console.error('加载推荐列表失败:', error);
        uni.showToast({
          title: '加载推荐失败',
          icon: 'none'
        });
      }
    },

    // 随机打乱数组的方法
    shuffleArray(array) {
      let currentIndex = array.length;
      let randomIndex;

      while (currentIndex != 0) {
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex--;

        [array[currentIndex], array[randomIndex]] = [array[randomIndex], array[currentIndex]];
      }

      return array;
    },

    async handleSearch() {
      if (!this.searchKeyword) {
        this.searchResults = [];
        return;
      }

      try {
        const res = await getDestinationPage({
          pageNo: 1,
          pageSize: 100,
          name: this.searchKeyword
        });

        if (res.code == 0) {
          this.searchResults = res.data.list;
        }
      } catch (error) {
        console.error('搜索失败:', error);
        uni.showToast({
          title: '搜索失败',
          icon: 'none'
        });
      }
    },

    handleSelectDestination(item) {
      // 处理目的地选择
      uni.navigateTo({
        url: `/pages/destination/index?id=${item.id}`
      });
    },
    navigateTo(url) {
      uni.navigateTo({
        url
      });
    },
    goToDetail(item) {
      // 根据不同类型跳转到不同页面
      const url = item.type === 'scenery' ? `/pages/destination/scenery?id=${item.id}` : `/pages/forum/detail?id=${item.id}`;

      uni.navigateTo({ url });
    },
    clearSearch() {
      this.searchKeyword = '';
      this.searchResults = [];
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
  position: relative;
  background: linear-gradient(180deg, #87ceeb, #b0e0e6);
  height: 340rpx;
  margin-bottom: -170rpx;
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
  margin-top: -70rpx;
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

.search-results {
  position: absolute;
  top: 100rpx;
  left: 20rpx;
  right: 20rpx;
  background: #fff;
  border-radius: 12rpx;
  box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
  max-height: 600rpx;
  overflow-y: auto;
}

.search-list {
  padding: 0 20rpx;
}

.search-item {
  display: flex;
  padding: 20rpx;
  background: #fff;
  margin-bottom: 20rpx;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}

.item-image {
  width: 200rpx;
  height: 200rpx;
  margin-right: 10rpx;
  margin-bottom: 10rpx;
  border-radius: 8rpx;
}

/* 最后一张图片不需要右边距 */
.item-image:last-child {
  margin-right: 0;
}

.item-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.item-name {
  font-size: 32rpx;
  font-weight: bold;
  color: #333;
  margin-bottom: 12rpx;
  line-height: 1.2;
}

.item-detail {
  font-size: 26rpx;
  color: #666;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  text-overflow: ellipsis;
  word-break: break-all;
}

.no-result {
  padding: 60rpx 0;
  text-align: center;
}

.no-result-image {
  width: 200rpx;
  height: 200rpx;
  margin-bottom: 20rpx;
}

.no-result-text {
  font-size: 28rpx;
  color: #999;
}

/* 添加清除按钮样式 */
.clear-btn {
  padding: 10rpx;
  margin-left: 10rpx;
}
</style>
