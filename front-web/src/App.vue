<template>
  <a-config-provider :locale="zhCN">
    <router-view v-slot="{ Component, route }">
      <MainLayout v-if="!route.meta.hideLayout">
        <component :is="Component" />
      </MainLayout>
      <component v-else :is="Component" />
    </router-view>
  </a-config-provider>
</template>

<script setup lang="ts">
import MainLayout from './components/layout/MainLayout.vue'
import zhCN from 'ant-design-vue/es/locale/zh_CN'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'

dayjs.locale('zh-cn')
</script>

<style scoped>

</style>
