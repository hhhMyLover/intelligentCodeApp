<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-card">
        <!-- Logo 和标题 -->
        <div class="login-header">
          <img src="@/assets/logo.jpg" alt="Logo" class="login-logo" />
          <h1 class="login-title">智能代码应用</h1>
          <p class="login-subtitle">欢迎回来，请登录您的账户</p>
        </div>

        <!-- 登录表单 -->
        <a-form
          :model="loginForm"
          :rules="rules"
          @finish="handleLogin"
          class="login-form"
          layout="vertical"
        >
          <a-form-item name="userAccount" label="账号">
            <a-input
              v-model:value="loginForm.userAccount"
              size="large"
              placeholder="请输入账号"
              :prefix="h(UserOutlined)"
            >
            </a-input>
          </a-form-item>

          <a-form-item name="userPassword" label="密码">
            <a-input-password
              v-model:value="loginForm.userPassword"
              size="large"
              placeholder="请输入密码"
              :prefix="h(LockOutlined)"
            >
            </a-input-password>
          </a-form-item>

          <a-form-item>
            <div class="form-options">
              <a-checkbox v-model:checked="rememberMe">记住我</a-checkbox>
              <a class="forgot-password" @click="handleForgotPassword">忘记密码？</a>
            </div>
          </a-form-item>

          <a-form-item>
            <a-button
              type="primary"
              html-type="submit"
              size="large"
              :loading="loading"
              block
              class="login-button"
            >
              登录
            </a-button>
          </a-form-item>

          <div class="register-link">
            还没有账号？
            <a @click="handleRegister">立即注册</a>
          </div>
        </a-form>

        <!-- 分隔线 -->
        <a-divider>或</a-divider>

        <!-- 第三方登录 -->
        <div class="social-login">
          <a-button class="social-button" size="large" @click="handleSocialLogin('GitHub')">
            <GithubOutlined />
            GitHub 登录
          </a-button>
          <a-button class="social-button" size="large" @click="handleSocialLogin('微信')">
            <WechatOutlined />
            微信登录
          </a-button>
        </div>
      </div>
    </div>

    <!-- 装饰背景 -->
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, h } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import {
  UserOutlined,
  LockOutlined,
  GithubOutlined,
  WechatOutlined,
} from '@ant-design/icons-vue';
import type { Rule } from 'ant-design-vue/es/form';
import { userLogin } from '@/api/userController';
import { useUserStore } from '@/stores/user';

const router = useRouter();
const userStore = useUserStore();
const loading = ref(false);
const rememberMe = ref(false);

// 登录表单数据
const loginForm = reactive({
  userAccount: '',
  userPassword: '',
});

// 表单验证规则
const rules: Record<string, Rule[]> = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 4, message: '账号长度至少4位', trigger: 'blur' },
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' },
  ],
};

// 处理登录
const handleLogin = async () => {
  loading.value = true;
  try {
    const res = await userLogin(loginForm);
    console.log(res)
    if (res.data.code === 0 && res.data.data) {
      // 保存用户信息到 store
      userStore.setUserInfo(res.data.data);
      message.success('登录成功！');
      router.push('/');
    } else {
      message.error(res.data.message || '登录失败');
    }
  } finally {
    loading.value = false;
  }
};

// 处理忘记密码
const handleForgotPassword = () => {
  message.info('请联系管理员重置密码');
};

// 处理注册
const handleRegister = () => {
  router.push('/register');
};

// 处理第三方登录
const handleSocialLogin = (platform: string) => {
  message.info(`${platform} 登录功能开发中...`);
};
</script>

<style scoped>
.login-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.login-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 440px;
}

.login-card {
  background: #fff;
  border-radius: 16px;
  padding: 32px 40px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.5s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 20px;
}

.login-logo {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  margin-bottom: 12px;
  object-fit: cover;
}

.login-title {
  font-size: 24px;
  font-weight: 700;
  color: #001529;
  margin-bottom: 6px;
}

.login-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.login-form {
  margin-top: 16px;
}

.login-form :deep(.ant-form-item) {
  margin-bottom: 16px;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forgot-password {
  color: #1890ff;
  font-size: 14px;
}

.forgot-password:hover {
  color: #40a9ff;
}

.login-button {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  margin-top: 8px;
}

.register-link {
  text-align: center;
  color: #666;
  font-size: 14px;
}

.register-link a {
  color: #1890ff;
  font-weight: 600;
  margin-left: 4px;
}

.register-link a:hover {
  color: #40a9ff;
}

.social-login {
  display: flex;
  gap: 12px;
  margin-top: 24px;
}

.social-button {
  flex: 1;
  height: 44px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-weight: 500;
}

.social-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  transition: all 0.3s ease;
}

/* 背景装饰 */
.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  overflow: hidden;
  z-index: 1;
}

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 20s infinite ease-in-out;
}

.circle-1 {
  width: 300px;
  height: 300px;
  top: -100px;
  left: -100px;
  animation-delay: 0s;
}

.circle-2 {
  width: 200px;
  height: 200px;
  bottom: -50px;
  right: -50px;
  animation-delay: 5s;
}

.circle-3 {
  width: 150px;
  height: 150px;
  top: 50%;
  right: 10%;
  animation-delay: 10s;
}

@keyframes float {
  0%, 100% {
    transform: translateY(0) rotate(0deg);
  }
  50% {
    transform: translateY(-30px) rotate(180deg);
  }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-card {
    padding: 36px 28px;
  }

  .login-title {
    font-size: 24px;
  }

  .login-subtitle {
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .login-page {
    padding: 16px;
  }

  .login-card {
    padding: 28px 20px;
  }

  .login-title {
    font-size: 22px;
  }

  .login-logo {
    width: 56px;
    height: 56px;
  }

  .social-login {
    flex-direction: column;
  }

  .social-button {
    width: 100%;
  }
}
</style>
