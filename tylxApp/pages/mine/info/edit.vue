<template>
  <view class="container">
    <view class="example">
      <uni-forms ref="form" :model="user" labelWidth="80px">
        <uni-forms-item label="用户昵称" name="nickname">
          <uni-easyinput v-model="user.nickname" placeholder="请输入昵称" />
        </uni-forms-item>
        <uni-forms-item label="手机号码" name="mobile">
          <uni-easyinput v-model="user.mobile" placeholder="请输入手机号码" />
        </uni-forms-item>
        <uni-forms-item label="邮箱" name="email">
          <uni-easyinput v-model="user.email" placeholder="请输入邮箱" />
        </uni-forms-item>
        <!-- TODO 芋艿：uni-data-checkbox 存在问题 -->
        <uni-forms-item label="性别" name="sex" required>
          <uni-data-checkbox v-model="user.sex" :localdata="sexs" />
        </uni-forms-item>
        <uni-forms-item label="出生年月" name="birthDate" required>
          <uni-datetime-picker v-model="user.birthDate" type="date" />
        </uni-forms-item>
      </uni-forms>
      <button type="primary" @click="submit">提交</button>
    </view>
  </view>
</template>

<script>
import { getUserProfile } from '@/api/system/user';
import { updateUserProfile } from '@/api/system/user';

export default {
  data() {
    return {
      user: {
        nickname: '',
        mobile: '',
        email: '',
        sex: '',
        birthDate: ''
      },
      sexs: [
        {
          text: '男',
          value: 1
        },
        {
          text: '女',
          value: 2
        }
      ],
      rules: {
        nickname: {
          rules: [
            {
              required: true,
              errorMessage: '用户昵称不能为空'
            }
          ]
        },
        birthDate: {
          rules: [
            {
              required: true,
              errorMessage: '出生年月不能为空'
            }
          ]
        },
        mobile: {
          rules: [
            {
              required: true,
              errorMessage: '手机号码不能为空'
            },
            {
              pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
              errorMessage: '请输入正确的手机号码'
            }
          ]
        },
        email: {
          rules: [
            {
              required: true,
              errorMessage: '邮箱地址不能为空'
            },
            {
              format: 'email',
              errorMessage: '请输入正确的邮箱地址'
            }
          ]
        }
      }
    };
  },
  onLoad() {
    this.getUser();
  },
  onReady() {
    this.$refs.form.setRules(this.rules);
  },
  methods: {
    formatDate(timestamp) {
      if (timestamp == null) {
        return '';
      }
      const date = new Date(timestamp);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0'); // 月份是从0开始的，所以加1
      const day = String(date.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`; // 返回年月日格式
    },
    getUser() {
      getUserProfile().then((response) => {
        this.user = response.data;
        this.user.birthDate = this.formatDate(this.user.birthDate);
      });
    },
    submit(ref) {
      this.$refs.form.validate().then((res) => {
        this.user.birthDate = new Date(this.user.birthDate).getTime();
        updateUserProfile(this.user).then((response) => {
          this.$modal.msgSuccess('修改成功');
          this.$store.commit('SET_NAME', this.user.nickname);
        });
      });
    }
  }
};
</script>

<style lang="scss">
page {
  background-color: #ffffff;
}

.example {
  padding: 15px;
  background-color: #fff;
}

.segmented-control {
  margin-bottom: 15px;
}

.button-group {
  margin-top: 15px;
  display: flex;
  justify-content: space-around;
}

.form-item {
  display: flex;
  align-items: center;
  flex: 1;
}

.button {
  display: flex;
  align-items: center;
  height: 35px;
  line-height: 35px;
  margin-left: 10px;
}
</style>
