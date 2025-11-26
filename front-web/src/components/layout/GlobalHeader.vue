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
        <a-button type="primary" @click="handleLogin">登录</a-button>
      </div>
    </div>
  </a-layout-header>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import type { MenuProps } from 'ant-design-vue';
import {
  HomeOutlined,
  AppstoreOutlined,
  UserOutlined,
  SettingOutlined
} from '@ant-design/icons-vue';
import { h } from 'vue';

const router = useRouter();
const selectedKeys = ref<string[]>(['home']);

const menuItems: MenuProps['items'] = [
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
    key: 'settings',
    icon: () => h(SettingOutlined),
    label: '设置',
  },
];

const handleMenuClick: MenuProps['onClick'] = ({ key }) => {
  console.log('Menu clicked:', key);
  // 可以根据key进行路由跳转
  // router.push(`/${key}`);
};

const handleLogin = () => {
  console.log('Login clicked');
  // 跳转到登录页面
  // router.push('/login');
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
