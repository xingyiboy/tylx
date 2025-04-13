<template>
  <view class="add-container">
    <uni-forms ref="form" :model="formData" :rules="rules" label-position="top" err-show-type="undertext">
      <!-- 标题 -->
      <uni-forms-item name="title" label="标题：">
        <uni-easyinput v-model="formData.title" placeholder="请输入标题" />
      </uni-forms-item>

      <!-- 分类 - 改为下拉搜索 -->
      <uni-forms-item name="destinationId" label="目的地：">
        <uni-combox v-model="selectedDestinationName" :candidates="categories" placeholder="请选择或搜索目的地"
          @input="handleDestinationInput"></uni-combox>
      </uni-forms-item>

      <!-- 简介 -->
      <uni-forms-item name="introduce" label="简介：">
        <uni-easyinput v-model="formData.introduce" type="textarea" placeholder="请输入简介" />
      </uni-forms-item>

      <!-- 内容 -->
      <uni-forms-item name="content" label="内容：">
        <rich-text-editor v-model="formData.content" placeholder="请输入内容描述" />
      </uni-forms-item>

      <!-- 首页图片 -->
      <uni-forms-item name="picture" label="首页图片">
        <view class="upload-box">
          <view class="upload-item" v-for="(pic, index) in pictureList" :key="index">
            <image :src="pic" mode="aspectFill"></image>
            <view class="delete-btn" @click="deletePicture(index)">
              <uni-icons type="trash" size="20" color="#fff"></uni-icons>
            </view>
          </view>
          <view v-if="pictureList.length < 9" class="upload-btn" @click="choosePicture">
            <uni-icons type="plusempty" size="30" color="#999"></uni-icons>
            <text>上传图片</text>
          </view>
        </view>
      </uni-forms-item>
    </uni-forms>

    <!-- 提交按钮 -->
    <view class="submit-btn">
      <button type="primary" @click="submitForm">发布攻略</button>
    </view>
  </view>
</template>

<script>
import { uploadFile, createForum } from '@/api/forum';
import { getDestinationPage } from '@/api/destination';
import RichTextEditor from '@/components/rich-text-editor/index.vue';

export default {
  data() {
    return {
      selectedDestinationName: '',
      formData: {
        title: '',
        destinationId: '',
        introduce: '',
        content: '',
        picture: ''
      },
      categories: [],
      categoriesMap: {},
      editorCtx: null,
      rules: {
        title: {
          rules: [
            {
              required: true,
              errorMessage: '请输入标题'
            }
          ]
        },
        destinationId: {
          rules: [
            {
              required: true,
              errorMessage: '请选择分类'
            }
          ]
        },
        introduce: {
          rules: [
            {
              required: true,
              errorMessage: '请输入简介'
            }
          ]
        },
        content: {
          rules: [
            {
              required: true,
              errorMessage: '请输入内容'
            }
          ]
        },
        picture: {
          rules: [
            {
              required: true,
              errorMessage: '请上传首页图片'
            }
          ]
        }
      },
      pictureList: []
    };
  },
  components: {
    RichTextEditor
  },
  onLoad() {
    this.loadCategories();
  },
  methods: {
    async loadCategories() {
      try {
        const res = await getDestinationPage({
          pageNo: 1,
          pageSize: 100
        });
        this.categoriesMap = {};
        res.data.list.forEach((item) => {
          this.categoriesMap[item.name] = item.id;
        });
        this.categories = res.data.list.map((item) => item.name);
      } catch (error) {
        uni.showToast({
          title: '获取目的地失败',
          icon: 'none'
        });
      }
    },
    handleDestinationInput(value) {
      this.formData.destinationId = this.categoriesMap[value] || '';
    },
    onEditorReady() {
      uni
        .createSelectorQuery()
        .select('#editor')
        .context((res) => {
          this.editorCtx = res.context;
        })
        .exec();
    },
    onEditorInput(e) {
      this.formData.content = e.detail.html;
    },
    async choosePicture() {
      uni.chooseImage({
        count: 9 - this.pictureList.length,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: async (res) => {
          try {
            uni.showLoading({ title: '上传中...' });

            for (let tempFilePath of res.tempFilePaths) {
              const uploadRes = await uploadFile(tempFilePath);
              if (uploadRes.code === 0) {
                this.pictureList.push(uploadRes.data);
              }
            }

            this.formData.picture = this.pictureList.join(',');

            uni.showToast({
              title: '上传成功',
              icon: 'success'
            });
          } catch (error) {
            console.error('上传失败：', error);
            uni.showToast({
              title: '上传失败',
              icon: 'none'
            });
          } finally {
            uni.hideLoading();
          }
        }
      });
    },
    deletePicture(index) {
      this.pictureList.splice(index, 1);
      this.formData.picture = this.pictureList.join(',');
    },
    submitForm() {
      // 直接检查必填字段
      if (!this.formData.title) {
        uni.showToast({ title: '请输入标题', icon: 'none' });
        return;
      }
      if (!this.formData.destinationId) {
        uni.showToast({ title: '请选择目的地', icon: 'none' });
        return;
      }
      if (!this.formData.introduce) {
        uni.showToast({ title: '请输入简介', icon: 'none' });
        return;
      }
      if (!this.formData.content) {
        uni.showToast({ title: '请输入内容', icon: 'none' });
        return;
      }
      if (!this.formData.picture) {
        uni.showToast({ title: '请上传首页图片', icon: 'none' });
        return;
      }

      try {
        uni.showLoading({ title: '发布中...' });
        createForum(this.formData).then(() => {
          uni.showToast({
            title: '发布成功',
            icon: 'success'
          });
          setTimeout(() => {
            uni.navigateBack();
          }, 1500);
        });
      } catch (error) {
        uni.showToast({
          title: '发布失败',
          icon: 'none'
        });
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
}

.upload-box {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20rpx;
}

.upload-item {
  width: 200rpx;
  height: 200rpx;
  position: relative;
  border-radius: 8rpx;
  overflow: hidden;

  image {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .delete-btn {
    position: absolute;
    top: 10rpx;
    right: 10rpx;
    width: 40rpx;
    height: 40rpx;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1;
  }
}

.upload-btn {
  width: 200rpx;
  height: 200rpx;
  background-color: #f8f8f8;
  border: 2rpx dashed #ddd;
  border-radius: 8rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  cursor: pointer;

  text {
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
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

:deep(.uni-forms-item__label) {
  white-space: nowrap;
}
</style>
