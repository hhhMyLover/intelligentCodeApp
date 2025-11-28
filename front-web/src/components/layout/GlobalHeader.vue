<template>
  <a-layout-header class="global-header">
    <div class="header-container">
      <div class="left-section">
        <div class="logo">
          <img src="@/assets/logo.jpg" alt="Logo" />
          <span class="logo-text">智能代码应用</span>
        </div>
        <a-menu
          v-model:selectedKeys="selectedKeys"
          mode="horizontal"
          :items="menuItems"
          class="header-menu"
          @click="handleMenuClick"
        />
      </div>
      <div class="right-section">
        <!-- 未登录：显示登录按钮 -->
        <a-button v-if="!userStore.isLogin" type="primary" @click="handleLogin">登录</a-button>
        
        <!-- 已登录：显示用户头像或账号首字母 -->
        <a-dropdown v-else>
          <div class="user-avatar-container">
            <!-- 有头像：显示头像 -->
            <a-avatar v-if="userStore.userInfo?.userAvatar" :size="50" :src="userStore.userInfo.userAvatar" />
            <!-- 无头像：显示账号首字母 -->
            <a-avatar v-else :size="50" style="background-color: #1890ff">
              {{ userStore.userInfo?.userName?.charAt(0).toUpperCase() || 'U' }}
            </a-avatar>
          </div>
          <template #overlay>
            <a-menu>
              <a-menu-item key="profile">
                <UserOutlined />
                个人中心
              </a-menu-item>
              <a-menu-item key="settings">
                <SettingOutlined />
                设置
              </a-menu-item>
              <a-menu-divider />
              <a-menu-item key="logout" @click="handleLogout">
                <span style="color: #ff4d4f">退出登录</span>
              </a-menu-item>
            </a-menu>
          </template>
        </a-dropdown>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import type { MenuProps } from 'ant-design-vue';
import {
  HomeOutlined,
  AppstoreOutlined,
  UserOutlined,
  SettingOutlined,
  ExclamationCircleOutlined,
  TeamOutlined
} from '@ant-design/icons-vue';
import { h } from 'vue';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const route = useRoute();
const userStore = useUserStore();

// 根据当前路由设置选中的菜单项
const getSelectedKeyFromRoute = () => {
  const path = route.path;
  if (path === '/') {
    return ['home'];
  }
  // 移除开头的斜杠
  const key = path.substring(1);
  return [key];
};

const selectedKeys = ref<string[]>(getSelectedKeyFromRoute());

// 监听路由变化，更新选中的菜单项
watch(() => route.path, () => {
  selectedKeys.value = getSelectedKeyFromRoute();
});

// 定义所有菜单项（包括需要权限的）
const allMenuItems = [
  {
    key: 'home',
    icon: () => h(HomeOutlined),
    label: '首页',
  },
  {
    key: 'features',
    icon: () => h(AppstoreOutlined),
    label: '功能',
  },
  {
    key: 'about',
    icon: () => h(UserOutlined),
    label: '关于',
  },
  {
    key: 'admin/user',
    icon: () => h(TeamOutlined),
    label: '用户管理',
  },
  {
    key: 'settings',
    icon: () => h(SettingOutlined),
    label: '设置',
  },
];

// 根据用户权限过滤菜单项
const menuItems = computed<MenuProps['items']>(() => {
  const isAdmin = userStore.userInfo?.userRole === 'admin';
  
  return allMenuItems.filter(item => {
    // 如果菜单项的 key 以 'admin' 开头，只有管理员才能看到
    if (item.key.startsWith('admin')) {
      return isAdmin;
    }
    // 其他菜单项都可以看到
    return true;
  });
});

const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  console.log('Menu clicked:', key);
  // 路由跳转
  if (key === 'home') {
    router.push('/');
  } else {
    router.push(`/${key}`);
  }
};

const handleLogin = () => {
  router.push('/login');
};

const handleLogout = () => {
  Modal.confirm({
    title: '确认退出',
    icon: h(ExclamationCircleOutlined),
    content: '确定要退出登录吗？',
    okText: '确定',
    cancelText: '取消',
    onOk() {
      userStore.clearUserInfo();
      message.success('已退出登录');
      router.push('/');
    },
  });
};
</script>

<style scoped>
.global-header {
  background: #fff;
  padding: 0;
  position: sticky;
  top: 0;
  z-index: 1000;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid #f0f0f0;
}

.header-container {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.left-section {
  display: flex;
  align-items: center;
  gap: 24px;
  flex: 1;
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
  cursor: pointer;
}

.logo img {
  height: 40px;
  width: 40px;
  border-radius: 4px;
  object-fit: cover;
}

.logo-text {
  color: #000;
  font-size: 18px;
  font-weight: 600;
  white-space: nowrap;
}

.header-menu {
  background: transparent;
  border: none;
  flex: 1;
  min-width: 0;
  line-height: 64px;
}

.right-section {
  display: flex;
  align-items: center;
}

.user-avatar-container {
  cursor: pointer;
  display: flex;
  align-items: center;
  transition: opacity 0.3s;
}

.user-avatar-container:hover {
  opacity: 0.8;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .header-container {
    padding: 0 16px;
  }

  .left-section {
    gap: 16px;
  }

  .logo-text {
    display: none;
  }

  .logo img {
    height: 32px;
    width: 32px;
  }

  .header-menu :deep(.ant-menu-item .anticon) {
    margin-right: 0;
  }

  .header-menu :deep(.ant-menu-item span) {
    display: none;
  }
}

@media (max-width: 480px) {
  .header-container {
    padding: 0 12px;
  }

  .left-section {
    gap: 12px;
  }

  .right-section .ant-btn {
    padding: 4px 12px;
    font-size: 14px;
  }
}
</style>
