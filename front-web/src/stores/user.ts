import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  // 用户信息
  const userInfo = ref<API.LoginUserVO | null>(null)
  
  // 是否已登录
  const isLogin = ref(false)

  // 设置用户信息
  const setUserInfo = (user: API.LoginUserVO | null) => {
    userInfo.value = user
    isLogin.value = !!user
  }

  // 清除用户信息（登出）
  const clearUserInfo = () => {
    userInfo.value = null
    isLogin.value = false
  }

  return {
    userInfo,
    isLogin,
    setUserInfo,
    clearUserInfo,
  }
}, {
  persist: true, // 启用持久化
})
