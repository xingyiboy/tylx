<template>
  <view class="wrapper">
    <view class="toolbar" @tap="format">
      <view class="toolbar-group">
        <text :class="['tool-btn', formats.bold ? 'ql-active' : '']" data-name="bold">加粗</text>
        <text :class="['tool-btn', formats.italic ? 'ql-active' : '']" data-name="italic">斜体</text>
        <text :class="['tool-btn', formats.underline ? 'ql-active' : '']" data-name="underline">下划线</text>
        <text :class="['tool-btn', formats.strike ? 'ql-active' : '']" data-name="strike">删除线</text>
      </view>

      <view class="toolbar-group">
        <text :class="['tool-btn', formats.align === 'left' ? 'ql-active' : '']" data-name="align"
          data-value="left">左对齐</text>
        <text :class="['tool-btn', formats.align === 'center' ? 'ql-active' : '']" data-name="align"
          data-value="center">居中</text>
        <text :class="['tool-btn', formats.align === 'right' ? 'ql-active' : '']" data-name="align"
          data-value="right">右对齐</text>
      </view>

      <view class="toolbar-group">
        <text class="tool-btn" @tap="insertImage">图片</text>
        <text class="tool-btn" @tap="removeFormat">清格式</text>
        <text class="tool-btn danger" @tap="clear">清空</text>
      </view>
    </view>

    <editor id="editor" class="ql-container" :placeholder="placeholder" @ready="onEditorReady" @input="onEditorInput"
      :read-only="false" show-img-size show-img-toolbar show-img-resize @statuschange="onStatusChange"></editor>
  </view>
</template>

<script>
import { uploadFile } from '@/api/forum';
export default {
  name: 'RichTextEditor',

  props: {
    value: {
      type: String,
      default: ''
    },
    placeholder: {
      type: String,
      default: '请输入内容'
    }
  },

  data() {
    return {
      editorCtx: null,
      formats: {}
    };
  },

  methods: {
    // 编辑器准备就绪
    onEditorReady() {
      uni
        .createSelectorQuery()
        .in(this)
        .select('#editor')
        .context((res) => {
          this.editorCtx = res.context;
          // 如果有初始内容，设置编辑器内容
          if (this.value) {
            this.editorCtx.setContents({
              html: this.value
            });
          }
        })
        .exec();
    },

    // 编辑器内容变化
    onEditorInput(e) {
      this.$emit('input', e.detail.html);
    },

    // 格式化操作
    format(e) {
      const { name, value } = e.target.dataset;
      if (!name) return;
      this.editorCtx && this.editorCtx.format(name, value);
    },

    // 状态变化
    onStatusChange(e) {
      this.formats = e.detail;
    },

    // 插入图片
    async insertImage() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: async (res) => {
          try {
            uni.showLoading({ title: '上传中...' });

            const uploadRes = await uploadFile(res.tempFilePaths[0]);

            if (uploadRes.code === 0) {
              // 上传成功后，将返回的url插入到富文本中
              this.editorCtx && this.editorCtx.insertImage({
                src: uploadRes.data,
                alt: '图片',
                success: () => {
                  uni.showToast({
                    title: '插入成功',
                    icon: 'success'
                  });
                }
              });
            } else {
              uni.showToast({
                title: uploadRes.msg || '上传失败',
                icon: 'none'
              });
            }
          } catch (error) {
            console.error('上传失败：', error);
            uni.showToast({
              title: '上传失败',
              icon: 'none'
            });
          } finally {
            uni.hideLoading();
          }
        },
        fail: (error) => {
          console.error('选择图片失败：', error);
          uni.showToast({
            title: '选择图片失败',
            icon: 'none'
          });
        }
      });
    },

    // 清除格式
    removeFormat() {
      this.editorCtx && this.editorCtx.removeFormat();
    },

    // 清空内容
    clear() {
      uni.showModal({
        title: '清空编辑器',
        content: '确定清空编辑器全部内容？',
        success: (res) => {
          if (res.confirm) {
            this.editorCtx &&
              this.editorCtx.clear({
                success: function (res) {
                  console.log('clear success');
                }
              });
          }
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.wrapper {
  padding: 20rpx;
  background: #fff;
  border-radius: 12rpx;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.toolbar {
  padding: 16rpx;
  border-bottom: 1px solid #eee;
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.toolbar-group {
  display: flex;
  flex-wrap: wrap;
  gap: 12rpx;
  padding: 8rpx 0;

  &:not(:last-child) {
    border-bottom: 1px dashed #eee;
  }
}

.tool-btn {
  padding: 8rpx 16rpx;
  font-size: 24rpx;
  border: 1px solid #e5e5e5;
  border-radius: 6rpx;
  color: #666;
  background: #f8f8f8;
  transition: all 0.2s ease;

  &:active {
    opacity: 0.8;
  }

  &.ql-active {
    color: #fff;
    background-color: #14569b;
    border-color: #14569b;
  }

  &.danger {
    color: #ff4d4f;
    border-color: #ffccc7;
    background-color: #fff2f0;

    &:active {
      background-color: #fff2f0;
    }
  }
}

.ql-container {
  min-height: 300rpx;
  padding: 20rpx;
  width: 100%;
  box-sizing: border-box;
}
</style>
