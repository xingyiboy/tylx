<template>
  <view><mp-html :content="detail.content" /></view>
</template>

<script>
import { getDestinationFoodDetail } from '@/api/destination';

export default {
  data() {
    return {
      id: null,
      detail: {}
    };
  },

  onLoad(options) {
    this.id = options.id;
    this.loadDetail();
  },

  methods: {
    async loadDetail() {
      try {
        const res = await getDestinationFoodDetail(this.id);
        if (res.code === 0) {
          this.detail = res.data;
        } else {
          uni.showToast({
            title: '加载失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('加载详情失败:', error);
        uni.showToast({
          title: '加载失败',
          icon: 'none'
        });
      }
    }
  }
};
</script>

<style lang="scss"></style>
