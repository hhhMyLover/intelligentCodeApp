<template>
  <router-view v-slot="{ Component, route }">
    <MainLayout v-if="!route.meta.hideLayout">
      <component :is="Component" />
    </MainLayout>
    <component v-else :is="Component" />
  </router-view>
</template>

<script setup lang="ts">
import MainLayout from './components/layout/MainLayout.vue';
</script>

<style scoped>

</style>
