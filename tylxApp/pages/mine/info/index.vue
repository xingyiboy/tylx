<template>
  <view class="container">
    <uni-list>
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'person-filled' }" title="昵称" :rightText="user.nickname" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'phone-filled' }" title="手机号码" :rightText="user.mobile" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'email-filled' }" title="邮箱" :rightText="user.email" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'staff-filled' }" title="性别" :rightText="formatSex(user.sex)" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'auth-filled' }" title="出生日期" :rightText="formatDate(user.birthDate)" />
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'wallet-filled' }" title="金额" :rightText="String(user.money)" />
      <!-- <uni-list-item showExtraIcon="true" :extraIcon="{type: 'auth-filled'}" title="岗位" :rightText="(user.posts || []).map(post => post.name).join(',')" />
      <uni-list-item showExtraIcon="true" :extraIcon="{type: 'staff-filled'}" title="角色" :rightText="(user.roles || []).map(role => role.name).join(',')" /> -->
      <uni-list-item showExtraIcon="true" :extraIcon="{ type: 'calendar-filled' }" title="注册日期" :rightText="this.parseTime(user.createTime)" />
    </uni-list>
  </view>
</template>

<script>
import { getUserProfile } from '@/api/system/user';
import { parseTime } from '@/utils/ruoyi';

export default {
  data() {
    return {
      user: {}
    };
  },
  onLoad() {
    this.getUser();
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
    formatSex(sex) {
      return sex === 1 ? '男' : '女';
    },
    getUser() {
      getUserProfile().then((response) => {
        this.user = response.data;
      });
    },
    parseTime(time) {
      return parseTime(time);
    }
  }
};
</script>

<style lang="scss">
page {
  background-color: #ffffff;
}
</style>
