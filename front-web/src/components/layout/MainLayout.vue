<template>
  <a-layout class="main-layout">
    <!-- 顶部导航栏 -->
    <GlobalHeader />
    
    <!-- 中部内容区域 -->
    <a-layout-content class="main-content">
      <div class="content-container">
        <transition name="fade" mode="out-in">
          <slot />
        </transition>
      </div>
    </a-layout-content>
    
    <!-- 底部 -->
    <GlobalFooter />
  </a-layout>
</template>

<script setup lang="ts">
import GlobalHeader from './GlobalHeader.vue';
import GlobalFooter from './GlobalFooter.vue';
</script>

<style scoped>
.main-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  flex: 1;
  background: #f0f2f5;
  padding: 24px;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  background: #fff;
  padding: 24px;
  border-radius: 8px;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.03);
  min-height: calc(100vh - 200px);
}

/* 路由过渡动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    padding: 16px;
  }

  .content-container {
    padding: 16px;
    border-radius: 4px;
  }
}

@media (max-width: 480px) {
  .main-content {
    padding: 12px;
  }

  .content-container {
    padding: 12px;
    min-height: calc(100vh - 180px);
  }
}
</style>

<style>
/* 全局样式重置 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial,
    'Noto Sans', sans-serif, 'Apple Color Emoji', 'Segoe UI Emoji', 'Segoe UI Symbol',
    'Noto Color Emoji';
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

#app {
  width: 100%;
  min-height: 100vh;
}
</style>
