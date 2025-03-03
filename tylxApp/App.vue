<script>
import config from './config';
import store from '@/store';
import { getAccessToken, getRefreshToken } from '@/utils/auth';

export default {
  globalData: {
    websocket: null,
    wsTimer: null,
    config: null
  },
  onLaunch: function () {
    this.initApp();
  },
  methods: {
    // 初始化应用
    initApp() {
      // 初始化应用配置
      this.initConfig();
      // 检查用户登录状态
      //#ifdef H5
      this.checkLogin();
      //#endif
      // 初始化WebSocket连接
      this.initWebSocket();
    },
    initConfig() {
      this.globalData.config = config;
    },
    checkLogin() {
      if (!getAccessToken()) {
        this.$tab.reLaunch('/pages/login');
      }
    },
    // 初始化WebSocket连接
    initWebSocket() {
      const wsUrl = config.baseUrl.replace('http', 'ws') + '/infra/ws?token=' + getRefreshToken();
      this.globalData.websocket = uni.connectSocket({
        url: wsUrl,
        success: () => {
          console.log('WebSocket连接成功');
        }
      });

      // 监听WebSocket连接打开
      this.globalData.websocket.onOpen(() => {
        console.log('WebSocket连接已打开');
        this.startHeartbeat();
      });

      // 监听WebSocket错误
      this.globalData.websocket.onError((res) => {
        console.error('WebSocket连接错误：', res);
      });

      // 监听WebSocket关闭
      this.globalData.websocket.onClose(() => {
        console.log('WebSocket已关闭');
        this.stopHeartbeat();
      });
    },

    // 开始心跳
    startHeartbeat() {
      this.globalData.wsTimer = setInterval(() => {
        this.globalData.websocket.send({
          data: 'ping',
          fail: (error) => {
            console.error('发送心跳失败：', error);
          }
        });
      }, 2000);
    },

    // 停止心跳
    stopHeartbeat() {
      if (this.globalData.wsTimer) {
        clearInterval(this.globalData.wsTimer);
        this.globalData.wsTimer = null;
      }
    }
  }
};
</script>

<style lang="scss">
@import '@/static/scss/index.scss';
</style>