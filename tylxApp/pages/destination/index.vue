<template>
  <view class="destination-container">
    <!-- 搜索栏 -->
    <view class="search-bar">
      <input type="text" v-model="searchKeyword" placeholder="搜索美食或景点" @input="handleSearch" />
    </view>

    <!-- 筛选标签 -->
    <view class="filter-tabs">
      <view class="tab-item" :class="{ active: currentType === 'all' }" @click="switchType('all')">全部</view>
      <view class="tab-item" :class="{ active: currentType === 'scenery' }" @click="switchType('scenery')">景点</view>
      <view class="tab-item" :class="{ active: currentType === 'food' }" @click="switchType('food')">美食</view>
    </view>

    <!-- 列表展示 -->
    <view class="item-list">
      <view class="item-card" v-for="(item, index) in filteredList" :key="index" @click="goToDetail(item)">
        <image :src="item.picture" mode="aspectFill" class="item-image"></image>
        <view class="item-content">
          <view class="item-name">{{ item.name }}</view>
          <view class="item-desc">{{ item.detail }}</view>
          <view class="item-tag" :class="item.type === 'scenery' ? 'tag-scenery' : 'tag-food'">
            {{ item.type === 'scenery' ? '景点' : '美食' }}
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
import { getDestinationFoodList, getDestinationSceneryList } from '@/api/destination';

export default {
  data() {
    return {
      destinationId: null,
      searchKeyword: '',
      currentType: 'all', // all, scenery, food
      allItems: [],
      searchTimer: null
    };
  },

  computed: {
    filteredList() {
      let list = this.allItems;

      // 关键词筛选
      if (this.searchKeyword.trim()) {
        list = list.filter((item) => item.name.toLowerCase().includes(this.searchKeyword.trim().toLowerCase()));
      }

      // 类型筛选
      if (this.currentType !== 'all') {
        list = list.filter((item) => item.type === this.currentType);
      }

      return list;
    }
  },

  onLoad(options) {
    this.destinationId = options.id;
    this.loadData();
  },

  methods: {
    async loadData() {
      try {
        // 并行获取数据
        const [sceneryRes, foodRes] = await Promise.all([getDestinationSceneryList(this.destinationId), getDestinationFoodList(this.destinationId)]);

        let items = [];

        // 处理景点数据
        if (sceneryRes.code === 0) {
          const sceneryItems = sceneryRes.data.map((item) => ({
            ...item,
            type: 'scenery'
          }));
          items = items.concat(sceneryItems);
        }

        // 处理美食数据
        if (foodRes.code === 0) {
          const foodItems = foodRes.data.map((item) => ({
            ...item,
            type: 'food'
          }));
          items = items.concat(foodItems);
        }

        this.allItems = items;
      } catch (error) {
        console.error('加载数据失败:', error);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      }
    },

    handleSearch() {
      if (this.searchTimer) {
        clearTimeout(this.searchTimer);
      }

      this.searchTimer = setTimeout(() => {
        // 搜索逻辑已通过计算属性实现
      }, 300);
    },

    switchType(type) {
      this.currentType = type;
    },

    goToDetail(item) {
      const url = item.type === 'scenery' ? `/pages/destination/scenery?id=${item.id}` : `/pages/destination/food?id=${item.id}`;

      uni.navigateTo({ url });
    }
  }
};
</script>

<style lang="scss">
.destination-container {
  padding: 20rpx;
}

.search-bar {
  padding: 20rpx;
  background: #fff;
  border-radius: 12rpx;
  margin-bottom: 20rpx;

  input {
    height: 70rpx;
    background: #f5f5f5;
    border-radius: 35rpx;
    padding: 0 30rpx;
    font-size: 28rpx;
  }
}

.filter-tabs {
  display: flex;
  margin-bottom: 20rpx;
  background: #fff;
  padding: 20rpx;
  border-radius: 12rpx;

  .tab-item {
    flex: 1;
    text-align: center;
    padding: 15rpx 0;
    font-size: 28rpx;
    color: #666;

    &.active {
      color: #007aff;
      font-weight: bold;
    }
  }
}

.item-list {
  .item-card {
    background: #fff;
    border-radius: 12rpx;
    margin-bottom: 20rpx;
    overflow: hidden;
    position: relative;
    display: flex;

    .item-image {
      width: 240rpx;
      height: 180rpx;
      flex-shrink: 0;
    }

    .item-content {
      flex: 1;
      padding: 20rpx;
      position: relative;

      .item-name {
        font-size: 32rpx;
        font-weight: bold;
        margin-bottom: 10rpx;
        padding-right: 120rpx;
      }

      .item-desc {
        font-size: 26rpx;
        color: #666;
        line-height: 1.4;
        display: -webkit-box;
        -webkit-box-orient: vertical;
        -webkit-line-clamp: 2;
        overflow: hidden;
      }
    }

    .item-tag {
      position: absolute;
      top: 20rpx;
      right: 20rpx;
      padding: 6rpx 20rpx;
      border-radius: 20rpx;
      font-size: 24rpx;
      color: #fff;

      &.tag-scenery {
        background: rgba(0, 122, 255, 0.8);
      }

      &.tag-food {
        background: rgba(255, 59, 48, 0.8);
      }
    }
  }
}
</style>
