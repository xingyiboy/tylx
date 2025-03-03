<template>
  <view class="normal-login-container">
    <view class="logo-content align-center justify-center flex">
      <image style="width: 100rpx; height: 100rpx" :src="globalConfig.appInfo.logo" mode="widthFix"></image>
      <text class="title">同游旅行{{ !isRegister ? '登录' : '注册' }}</text>
    </view>
    <view class="login-form-content">
      <!-- 登录表单 -->
      <view v-if="!isRegister">
        <view class="form-item">
          <text class="error-tip" v-if="loginErrors.username">{{ loginErrors.username }}</text>
          <view class="input-item flex align-center" :class="{ error: loginErrors.username }">
            <view class="iconfont icon-user icon"></view>
            <input v-model="loginForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" @input="clearError('username')" />
          </view>
        </view>

        <view class="form-item">
          <text class="error-tip" v-if="loginErrors.password">{{ loginErrors.password }}</text>
          <view class="input-item flex align-center" :class="{ error: loginErrors.password }">
            <view class="iconfont icon-password icon"></view>
            <input v-model="loginForm.password" type="password" class="input" placeholder="请输入密码" maxlength="20" @input="clearError('password')" />
          </view>
        </view>

        <Verify @success="pwdLogin" :mode="'pop'" :captchaType="'blockPuzzle'" :imgSize="{ width: '330px', height: '155px' }" ref="verify"></Verify>
        <view class="action-btn">
          <button @click="handleLogin" class="login-btn cu-btn block bg-blue lg round">登录</button>
        </view>
      </view>

      <!-- 注册表单 -->
      <view v-else>
        <!-- <view v-if="tenantEnable" class="input-item flex align-center">
          <view class="iconfont icon-house icon"></view>
          <input v-model="registerForm.tenantName" class="input" type="text" placeholder="请输入租户" maxlength="30" />
        </view> -->
        <view class="form-item">
          <text class="error-tip" v-if="registerErrors.username">{{ registerErrors.username }}</text>
          <view class="input-item flex align-center" :class="{ error: registerErrors.username }">
            <view class="iconfont icon-user icon"></view>
            <input v-model="registerForm.username" class="input" type="text" placeholder="请输入账号" maxlength="30" @input="clearRegisterError('username')" />
          </view>
        </view>

        <view class="form-item">
          <text class="error-tip" v-if="registerErrors.nickname">{{ registerErrors.nickname }}</text>
          <view class="input-item flex align-center" :class="{ error: registerErrors.nickname }">
            <view class="iconfont icon-user icon"></view>
            <input v-model="registerForm.nickname" class="input" type="text" placeholder="请输入昵称" maxlength="30" @input="clearRegisterError('nickname')" />
          </view>
        </view>

        <view class="form-item">
          <text class="error-tip" v-if="registerErrors.password">{{ registerErrors.password }}</text>
          <view class="input-item flex align-center" :class="{ error: registerErrors.password }">
            <view class="iconfont icon-password icon"></view>
            <input v-model="registerForm.password" type="password" class="input" placeholder="请输入密码" maxlength="20" @input="clearRegisterError('password')" />
          </view>
        </view>

        <view class="form-item">
          <text class="error-tip" v-if="registerErrors.confirmPassword">{{ registerErrors.confirmPassword }}</text>
          <view class="input-item flex align-center" :class="{ error: registerErrors.confirmPassword }">
            <view class="iconfont icon-password icon"></view>
            <input v-model="registerForm.confirmPassword" type="password" class="input" placeholder="请确认密码" maxlength="20" @input="clearRegisterError('confirmPassword')" />
          </view>
        </view>

        <Verify @success="handleRegister" :mode="'pop'" :captchaType="'blockPuzzle'" :imgSize="{ width: '330px', height: '155px' }" ref="verifyRegister"></Verify>
        <view class="action-btn">
          <button @click="submitRegister" class="login-btn cu-btn block bg-blue lg round">注册</button>
        </view>
      </view>

      <!-- 切换按钮 -->
      <view class="switch-btn" @click="switchMode">
        <text>{{ isRegister ? '已有账号，去登录' : '没有账号，去注册' }}</text>
      </view>
    </view>

    <view class="xieyi text-center">
      <text class="text-grey1">登录即代表同意</text>
      <text @click="handleUserAgrement" class="text-blue">《用户协议》</text>
      <text @click="handlePrivacy" class="text-blue">《隐私协议》</text>
    </view>
  </view>
</template>

<script>
import Verify from '@/components/verifition/Verify';
import { register, getTenantIdByName } from '@/api/login';

export default {
  name: 'Login',
  components: {
    Verify
  },
  data() {
    return {
      captchaEnabled: true, // 验证码开关 TODO 芋艿：需要抽到配置里
      globalConfig: getApp().globalData.config,
      loginForm: {
        username: '',
        password: '',
        captchaVerification: ''
      },
      isRegister: false,
      tenantEnable: true, // 是否启用租户功能
      registerForm: {
        tenantName: '',
        username: '',
        nickname: '',
        password: '',
        confirmPassword: '',
        captchaVerification: '',
        tenantId: 0
      },
      loginErrors: {
        username: '',
        password: ''
      },
      registerErrors: {
        username: '',
        nickname: '',
        password: '',
        confirmPassword: ''
      }
    };
  },
  methods: {
    // 隐私协议
    handlePrivacy() {
      let site = this.globalConfig.appInfo.agreements[0];
      this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`);
    },
    // 用户协议
    handleUserAgrement() {
      let site = this.globalConfig.appInfo.agreements[1];
      this.$tab.navigateTo(`/pages/common/webview/index?title=${site.title}&url=${site.url}`);
    },
    // 清除登录错误提示
    clearError(field) {
      this.loginErrors[field] = '';
    },

    // 清除注册错误提示
    clearRegisterError(field) {
      this.registerErrors[field] = '';
    },
    // 登录方法
    async handleLogin() {
      // 重置错误信息
      this.loginErrors = {
        username: '',
        password: ''
      };

      if (this.loginForm.username === '') {
        this.loginErrors.username = '请输入账号';
        return;
      }
      if (this.loginForm.password === '') {
        this.loginErrors.password = '请输入密码';
        return;
      }
      if (this.loginForm.password.length < 4 || this.loginForm.password.length > 16) {
        this.loginErrors.password = '密码长度需要在4-16位之间';
        return;
      }
      // 显示验证码
      if (this.captchaEnabled) {
        this.$refs.verify.show();
      } else {
        await this.pwdLogin({});
      }
    },
    // 密码登录
    async pwdLogin(captchaParams) {
      this.$modal.loading('登录中，请耐心等待...');
      // 执行登录
      this.loginForm.captchaVerification = captchaParams.captchaVerification;
      this.$store.dispatch('Login', this.loginForm).then(() => {
        this.$modal.closeLoading();
        this.loginSuccess();
      });
    },
    // 登录成功后，处理函数
    loginSuccess(result) {
      // 设置用户信息
      this.$store.dispatch('GetInfo').then((res) => {
        this.$tab.reLaunch('/pages/index');
      });
    },
    // 切换登录/注册模式
    switchMode() {
      this.isRegister = !this.isRegister;
    },

    // 提交注册
    async submitRegister() {
      // 重置错误信息
      this.registerErrors = {
        username: '',
        nickname: '',
        password: '',
        confirmPassword: ''
      };

      // 表单验证
      if (!this.registerForm.username) {
        this.registerErrors.username = '请输入账号';
        return;
      }
      if (!this.registerForm.nickname) {
        this.registerErrors.nickname = '请输入昵称';
        return;
      }
      if (!this.registerForm.password) {
        this.registerErrors.password = '请输入密码';
        return;
      }
      if (this.registerForm.password.length < 4 || this.registerForm.password.length > 16) {
        this.registerErrors.password = '密码长度需要在4-16位之间';
        return;
      }
      if (this.registerForm.password !== this.registerForm.confirmPassword) {
        this.registerErrors.confirmPassword = '两次输入的密码不一致';
        return;
      }

      // 显示验证码
      this.$refs.verifyRegister.show();
    },

    // 注册处理
    async handleRegister(captchaParams) {
      try {
        this.$modal.loading('注册中...');

        // 获取租户ID
        if (this.tenantEnable && this.registerForm.tenantName) {
          const tenantRes = await getTenantIdByName(this.registerForm.tenantName);
          this.registerForm.tenantId = tenantRes;
        }

        // 设置验证码信息
        this.registerForm.captchaVerification = captchaParams.captchaVerification;

        // 调用注册接口
        const res = await register(this.registerForm);

        if (res.data == null) {
          this.$modal.msgError(res.msg);
          this.$refs.verifyRegister.hide();
          return;
        }

        // 保存注册的账号密码
        const registeredUsername = this.registerForm.username;
        const registeredPassword = this.registerForm.password;

        // 切换到登录页并填充表单
        this.loginForm.username = registeredUsername;
        this.loginForm.password = registeredPassword;

        this.$store.dispatch('Login', this.loginForm).then(() => {
          this.$modal.closeLoading();
          this.loginSuccess();
        });
      } catch (error) {
        console.error('注册失败', error);
      } finally {
        // 先关闭验证码弹窗
        this.$modal.closeLoading();
      }
    }
  }
};
</script>

<style lang="scss">
page {
  background-color: #ffffff;
}

.normal-login-container {
  width: 100%;

  .logo-content {
    width: 100%;
    font-size: 21px;
    text-align: center;
    padding-top: 15%;

    image {
      border-radius: 4px;
    }

    .title {
      margin-left: 10px;
    }
  }

  .login-form-content {
    text-align: center;
    margin: 20px auto;
    margin-top: 15%;
    width: 80%;

    .form-item {
      margin-bottom: 30rpx;
    }

    .error-tip {
      color: #ff4d4f;
      font-size: 24rpx;
      margin-bottom: 8rpx;
      padding-left: 20rpx;
    }

    .input-item {
      border: 2rpx solid #eee;
      border-radius: 8rpx;
      height: 88rpx;
      padding: 0 20rpx;
      transition: all 0.3s;

      &.error {
        border-color: #ff4d4f;
        background-color: #fff2f0;
      }

      .icon {
        font-size: 40rpx;
        color: #999;
        margin-right: 20rpx;
      }

      .input {
        flex: 1;
        font-size: 28rpx;
      }
    }

    .login-btn {
      margin-top: 40px;
      height: 45px;
    }

    .xieyi {
      color: #333;
      margin-top: 20px;
    }
  }

  .easyinput {
    width: 100%;
  }
}

.login-code-img {
  height: 45px;
}

.switch-btn {
  margin-top: 20rpx;
  text-align: center;
  color: #409eff;
  font-size: 28rpx;
}
</style>
