<template>
  <view class="add-container">
    <uni-forms ref="form" :model="formData" :rules="rules" label-position="top" err-show-type="undertext">
      <!-- 目的地 -->
      <uni-forms-item name="destinationId" label="目的地：">
        <uni-combox v-model="selectedDestinationName" :candidates="destinations" placeholder="请选择或搜索目的地" @input="handleDestinationInput"></uni-combox>
      </uni-forms-item>

      <!-- 简介 -->
      <uni-forms-item name="detail" label="简介：">
        <uni-easyinput v-model="formData.detail" type="textarea" placeholder="请输入简介" />
      </uni-forms-item>

      <!-- 内容 -->
      <uni-forms-item name="content" label="内容：">
        <rich-text-editor v-model="formData.content" placeholder="请输入内容描述" />
      </uni-forms-item>

      <!-- 详细地址 -->
      <uni-forms-item name="area" label="详细地址：">
        <uni-easyinput v-model="formData.area" placeholder="请输入详细地址" />
      </uni-forms-item>

      <!-- 最大组队数 -->
      <uni-forms-item name="maxNumber" label="最大组队数：">
        <uni-number-box v-model="formData.maxNumber" :min="2" :max="20" />
      </uni-forms-item>

      <!-- 开始时间 -->
      <uni-forms-item name="startTime" label="开始时间：">
        <uni-datetime-picker v-model="formData.startTime" type="datetime" :clear-icon="false" />
      </uni-forms-item>

      <!-- 结束时间 -->
      <uni-forms-item name="endTime" label="结束时间：">
        <uni-datetime-picker v-model="formData.endTime" type="datetime" :clear-icon="false" />
      </uni-forms-item>
    </uni-forms>

    <!-- 提交按钮 -->
    <view class="submit-btn">
      <button type="primary" @click="submitForm">发布组队</button>
    </view>
  </view>
</template>

<script>
import { createTeam } from '@/api/team';
import { getDestinationPage } from '@/api/destination';
import RichTextEditor from '@/components/rich-text-editor/index.vue';

export default {
  components: {
    RichTextEditor
  },

  data() {
    return {
      selectedDestinationName: '',
      formData: {
        destinationId: '',
        detail: '',
        content: '',
        status: 0,
        area: '',
        maxNumber: 2,
        startTime: '',
        endTime: ''
      },
      destinations: [],
      destinationsMap: {},
      rules: {
        destinationId: {
          rules: [{ required: true, errorMessage: '请选择目的地' }]
        },
        detail: {
          rules: [{ required: true, errorMessage: '请输入简介' }]
        },
        content: {
          rules: [{ required: true, errorMessage: '请输入内容' }]
        },
        area: {
          rules: [{ required: true, errorMessage: '请输入详细地址' }]
        },
        maxNumber: {
          rules: [{ required: true, errorMessage: '请选择最大组队数' }]
        },
        startTime: {
          rules: [{ required: true, errorMessage: '请选择开始时间' }]
        },
        endTime: {
          rules: [{ required: true, errorMessage: '请选择结束时间' }]
        }
      }
    };
  },

  onLoad() {
    this.loadDestinations();
  },

  methods: {
    async loadDestinations() {
      try {
        const res = await getDestinationPage({
          pageNo: 1,
          pageSize: 100
        });
        this.destinationsMap = {};
        res.data.list.forEach((item) => {
          this.destinationsMap[item.name] = item.id;
        });
        this.destinations = res.data.list.map((item) => item.name);
      } catch (error) {
        uni.showToast({
          title: '获取目的地失败',
          icon: 'none'
        });
      }
    },

    handleDestinationInput(value) {
      this.formData.destinationId = this.destinationsMap[value] || '';
    },

    async submitForm() {
      try {
        await this.$refs.form.validate();

        uni.showLoading({ title: '发布中...' });
        this.formData.startTime = new Date(this.formData.startTime).getTime();
        this.formData.endTime = new Date(this.formData.endTime).getTime();

        const res = await createTeam(this.formData);

        if (res.code === 0) {
          uni.showToast({
            title: '发布成功',
            icon: 'success'
          });
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        } else {
          uni.showToast({
            title: res.msg || '发布失败',
            icon: 'none'
          });
        }
      } catch (error) {
        console.error('表单验证失败：', error);
        // 验证失败的提示已经由 uni-forms 自动处理
      } finally {
        uni.hideLoading();
      }
    }
  }
};
</script>

<style lang="scss" scoped>
.add-container {
  padding: 30rpx;
  background-color: #fff;
  min-height: 100vh;
  box-sizing: border-box;
}

:deep(.uni-forms-item) {
  margin-bottom: 30rpx;

  .uni-forms-item__label {
    font-size: 28rpx;
    font-weight: bold;
    margin-bottom: 10rpx;
  }

  .uni-easyinput__content,
  .uni-combox__selector {
    background-color: #f8f8f8;
    border-radius: 8rpx;
    border: none !important;
  }

  .uni-data-checkbox {
    .uni-data-checkbox-item {
      margin-right: 30rpx;
    }
  }

  .uni-datetime-picker {
    width: 100%;

    .uni-datetime-picker-timebox {
      border: none !important;
      background-color: #f8f8f8;
      border-radius: 8rpx;
    }
  }
}

.submit-btn {
  margin-top: 60rpx;
  padding: 0 30rpx;

  button {
    width: 100%;
    height: 88rpx;
    line-height: 88rpx;
    font-size: 32rpx;
    border-radius: 44rpx;
    background: linear-gradient(to right, #1890ff, #1890ff);
    border: none;

    &:active {
      opacity: 0.8;
    }
  }
}
</style>
