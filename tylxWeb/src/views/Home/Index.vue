<template>
  <div class="home-container">
    <el-card shadow="never" class="welcome-card">
      <el-skeleton :loading="loading" animated>
        <el-row :gutter="24" justify="space-between" class="stats-row">
          <!-- 左侧欢迎信息 -->
          <el-col :xl="8" :lg="8" :md="24" :sm="24" :xs="24" class="mb-md-4">
            <div class="welcome-section">
              <el-avatar :src="avatar" :size="80" class="welcome-avatar">
                <img src="@/assets/imgs/avatar.gif" alt="" />
              </el-avatar>
              <div class="welcome-text">
                <div class="welcome-title">
                  {{ t('workplace.welcome') }} {{ username }} {{ t('workplace.happyDay') }}
                </div>
                <div class="welcome-subtitle">
                  <i class="el-icon-sunny mr-2"></i>
                  {{ t('workplace.toady') }}，20℃ - 32℃！
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </el-skeleton>
    </el-card>
    <!-- 统计卡片区域 -->
    <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24">
      <el-row :gutter="16" class="stat-cards">
        <!-- 在线用户数 -->
        <el-col :span="12" class="mb-4">
          <el-card class="stat-card" shadow="hover">
            <template #header>
              <div class="stat-header">
                <i class="el-icon-user"></i>
                <span>在线用户</span>
              </div>
            </template>
            <div class="stat-content">
              <div class="stat-number text-primary">{{ onlineCount }}</div>
              <div class="stat-label">当前在线</div>
              <div class="stat-trend">
                <i class="el-icon-data-line"></i>
                实时数据
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 注册用户数 -->
        <el-col :span="12" class="mb-4">
          <el-card class="stat-card" shadow="hover">
            <template #header>
              <div class="stat-header">
                <i class="el-icon-user-solid"></i>
                <span>注册用户</span>
              </div>
            </template>
            <div class="stat-content">
              <div class="stat-number text-success">{{ totalUserCount }}</div>
              <div class="stat-label">总用户数</div>
              <div class="stat-trend">
                <i class="el-icon-refresh"></i>
                10秒更新
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-col>
  </div>
</template>
<script lang="ts" setup>
import { set } from 'lodash-es'
import { EChartsOption } from 'echarts'
import { formatTime } from '@/utils'

import { useUserStore } from '@/store/modules/user'
import { useWatermark } from '@/hooks/web/useWatermark'
import type { WorkplaceTotal, Project, Notice, Shortcut } from './types'
import { pieOptions, barOptions } from './echarts-data'
import { useWebSocket } from '@vueuse/core'
import { getRefreshToken } from '@/utils/auth'
import { getUserCount } from '@/api/system/user'

defineOptions({ name: 'Home' })

const { t } = useI18n()
const userStore = useUserStore()
const { setWatermark } = useWatermark()
const loading = ref(true)
const avatar = userStore.getUser.avatar
const username = userStore.getUser.nickname
const pieOptionsData = reactive<EChartsOption>(pieOptions) as EChartsOption
// 获取统计数
let totalSate = reactive<WorkplaceTotal>({
  project: 0,
  access: 0,
  todo: 0
})

const getCount = async () => {
  const data = {
    project: 40,
    access: 2340,
    todo: 10
  }
  totalSate = Object.assign(totalSate, data)
}

// 获取项目数
let projects = reactive<Project[]>([])
const getProject = async () => {
  const data = [
    {
      name: 'ruoyi-vue-pro',
      icon: 'akar-icons:github-fill',
      message: 'https://github.com/YunaiV/ruoyi-vue-pro',
      personal: 'Spring Boot 单体架构',
      time: new Date()
    },
    {
      name: 'yudao-ui-admin-vue3',
      icon: 'logos:vue',
      message: 'https://github.com/yudaocode/yudao-ui-admin-vue3',
      personal: 'Vue3 + element-plus',
      time: new Date()
    },
    {
      name: 'yudao-ui-admin-vben',
      icon: 'logos:vue',
      message: 'https://github.com/yudaocode/yudao-ui-admin-vben',
      personal: 'Vue3 + vben(antd)',
      time: new Date()
    },
    {
      name: 'yudao-cloud',
      icon: 'akar-icons:github',
      message: 'https://github.com/YunaiV/yudao-cloud',
      personal: 'Spring Cloud 微服务架构',
      time: new Date()
    },
    {
      name: 'yudao-ui-mall-uniapp',
      icon: 'logos:vue',
      message: 'https://github.com/yudaocode/yudao-ui-admin-uniapp',
      personal: 'Vue3 + uniapp',
      time: new Date()
    },
    {
      name: 'yudao-ui-admin-vue2',
      icon: 'logos:vue',
      message: 'https://github.com/yudaocode/yudao-ui-admin-vue2',
      personal: 'Vue2 + element-ui',
      time: new Date()
    }
  ]
  projects = Object.assign(projects, data)
}

// 获取通知公告
let notice = reactive<Notice[]>([])
const getNotice = async () => {
  const data = [
    {
      title: '系统支持 JDK 8/17/21，Vue 2/3',
      type: '通知',
      keys: ['通知', '8', '17', '21', '2', '3'],
      date: new Date()
    },
    {
      title: '后端提供 Spring Boot 2.7/3.2 + Cloud 双架构',
      type: '公告',
      keys: ['公告', 'Boot', 'Cloud'],
      date: new Date()
    },
    {
      title: '全部开源，个人与企业可 100% 直接使用，无需授权',
      type: '通知',
      keys: ['通知', '无需授权'],
      date: new Date()
    },
    {
      title: '国内使用最广泛的快速开发平台，超 300+ 人贡献',
      type: '公告',
      keys: ['公告', '最广泛'],
      date: new Date()
    }
  ]
  notice = Object.assign(notice, data)
}

// 获取快捷入口
let shortcut = reactive<Shortcut[]>([])

const getShortcut = async () => {
  const data = [
    {
      name: 'Github',
      icon: 'akar-icons:github-fill',
      url: 'github.io'
    },
    {
      name: 'Vue',
      icon: 'logos:vue',
      url: 'vuejs.org'
    },
    {
      name: 'Vite',
      icon: 'vscode-icons:file-type-vite',
      url: 'https://vitejs.dev/'
    },
    {
      name: 'Angular',
      icon: 'logos:angular-icon',
      url: 'github.io'
    },
    {
      name: 'React',
      icon: 'logos:react',
      url: 'github.io'
    },
    {
      name: 'Webpack',
      icon: 'logos:webpack',
      url: 'github.io'
    }
  ]
  shortcut = Object.assign(shortcut, data)
}

// 用户来源
const getUserAccessSource = async () => {
  const data = [
    { value: 335, name: 'analysis.directAccess' },
    { value: 310, name: 'analysis.mailMarketing' },
    { value: 234, name: 'analysis.allianceAdvertising' },
    { value: 135, name: 'analysis.videoAdvertising' },
    { value: 1548, name: 'analysis.searchEngines' }
  ]
  set(
    pieOptionsData,
    'legend.data',
    data.map((v) => t(v.name))
  )
  pieOptionsData!.series![0].data = data.map((v) => {
    return {
      name: t(v.name),
      value: v.value
    }
  })
}
const barOptionsData = reactive<EChartsOption>(barOptions) as EChartsOption

// 周活跃量
const getWeeklyUserActivity = async () => {
  const data = [
    { value: 13253, name: 'analysis.monday' },
    { value: 34235, name: 'analysis.tuesday' },
    { value: 26321, name: 'analysis.wednesday' },
    { value: 12340, name: 'analysis.thursday' },
    { value: 24643, name: 'analysis.friday' },
    { value: 1322, name: 'analysis.saturday' },
    { value: 1324, name: 'analysis.sunday' }
  ]
  set(
    barOptionsData,
    'xAxis.data',
    data.map((v) => t(v.name))
  )
  set(barOptionsData, 'series', [
    {
      name: t('analysis.activeQuantity'),
      data: data.map((v) => v.value),
      type: 'bar'
    }
  ])
}

const getAllApi = async () => {
  await Promise.all([
    getCount(),
    getProject(),
    getNotice(),
    getShortcut(),
    getUserAccessSource(),
    getWeeklyUserActivity()
  ])
  loading.value = false
}

getAllApi()

// WebSocket相关
const server = ref(
  (import.meta.env.VITE_BASE_URL + '/infra/ws').replace('http', 'ws') +
    '?token=' +
    getRefreshToken()
)
const { status, data, send } = useWebSocket(server.value, {
  autoReconnect: true,
  heartbeat: {
    message: 'ping',
    interval: 2000, // 每2秒发送一次心跳
    pongTimeout: 1000 // pong 超时时间
  }
})

// 在线用户数
const onlineCount = ref(0)

// 监听WebSocket消息
watchEffect(() => {
  if (!data.value) return

  try {
    const message = JSON.parse(data.value)
    if (message.type === 'pong') {
      onlineCount.value = message.onlineCount
      return
    }
    // ... 处理其他消息类型 ...
  } catch (error) {
    console.error('处理WebSocket消息出错：', error)
  }
})

// 组件卸载时清除WebSocket连接
onUnmounted(() => {
  if (status.value === 'OPEN') {
    send('close')
  }
})

// 总用户数
const totalUserCount = ref(0)

// 获取总用户数
const fetchTotalUserCount = async () => {
  try {
    const res = await getUserCount()
    totalUserCount.value = res
  } catch (error) {
    console.error('获取用户总数失败：', error)
  }
}

// 定时获取总用户数
let userCountTimer: any = null

onMounted(() => {
  // 立即获取一次
  fetchTotalUserCount()
  // 每10秒获取一次
  userCountTimer = setInterval(fetchTotalUserCount, 10000)
})

onUnmounted(() => {
  // 清除定时器
  if (userCountTimer) {
    clearInterval(userCountTimer)
    userCountTimer = null
  }
})
</script>

<style scoped>
.home-container {
  padding: 16px;
}

.welcome-card {
  margin-bottom: 24px;
}

.stats-row {
  margin: 0 -12px;
}

.welcome-section {
  display: flex;
  align-items: center;
  padding: 20px;
  border-radius: 8px;
}

.welcome-avatar {
  margin-right: 24px;
  border: 4px solid #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.welcome-text {
  flex: 1;
}

.welcome-title {
  font-size: 24px;
  font-weight: bold;
  color: var(--el-text-color-primary);
  margin-bottom: 8px;
}

.welcome-subtitle {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  display: flex;
  align-items: center;
}

.stat-cards {
  height: 100%;
}

.stat-card {
  height: 100%;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-header {
  display: flex;
  align-items: center;
  font-size: 16px;
  color: var(--el-text-color-primary);
}

.stat-header i {
  margin-right: 8px;
  font-size: 20px;
}

.stat-content {
  text-align: center;
  padding: 16px 0;
}

.stat-number {
  font-size: 36px;
  font-weight: bold;
  line-height: 1;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: var(--el-text-color-secondary);
  margin-bottom: 12px;
}

.stat-trend {
  font-size: 12px;
  color: var(--el-text-color-secondary);
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-trend i {
  margin-right: 4px;
}

.text-primary {
  color: var(--el-color-primary);
}

.text-success {
  color: var(--el-color-success);
}

/* 响应式调整 */
@media (max-width: 768px) {
  .mb-md-4 {
    margin-bottom: 16px;
  }

  .welcome-section {
    padding: 16px;
  }

  .welcome-title {
    font-size: 20px;
  }

  .stat-number {
    font-size: 28px;
  }
}
</style>
