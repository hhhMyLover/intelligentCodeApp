<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-card">
        <!-- Logo 和标题 -->
        <div class="register-header">
          <img src="@/assets/logo.jpg" alt="Logo" class="register-logo" />
          <h1 class="register-title">创建账户</h1>
          <p class="register-subtitle">加入智能代码应用，开启高效开发之旅</p>
        </div>

        <!-- 注册表单 -->
        <a-form
          :model="registerForm"
          :rules="rules"
          @finish="handleRegister"
          class="register-form"
          layout="vertical"
        >
          <a-form-item name="userAccount" label="账号">
            <a-input
              v-model:value="registerForm.userAccount"
              size="large"
              placeholder="请输入账号（4-16位字符）"
              :prefix="h(UserOutlined)"
            >
            </a-input>
          </a-form-item>

          <a-form-item name="userPassword" label="密码">
            <a-input-password
              v-model:value="registerForm.userPassword"
              size="large"
              placeholder="请输入密码（至少6位）"
              :prefix="h(LockOutlined)"
            >
            </a-input-password>
          </a-form-item>

          <a-form-item name="checkPassword" label="确认密码">
            <a-input-password
              v-model:value="registerForm.checkPassword"
              size="large"
              placeholder="请再次输入密码"
              :prefix="h(LockOutlined)"
            >
            </a-input-password>
          </a-form-item>

          <a-form-item name="agreement">
            <a-checkbox v-model:checked="agreement">
              我已阅读并同意
              <a @click.prevent="showTerms">《用户协议》</a>
              和
              <a @click.prevent="showPrivacy">《隐私政策》</a>
            </a-checkbox>
          </a-form-item>

          <a-form-item>
            <a-button
              type="primary"
              html-type="submit"
              size="large"
              :loading="loading"
              block
              class="register-button"
            >
              注册
            </a-button>
          </a-form-item>

          <div class="login-link">
            已有账号？
            <a @click="handleBackToLogin">立即登录</a>
          </div>
        </a-form>
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
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import type { Rule } from 'ant-design-vue/es/form';
import { userRegister } from '@/api/userController';

const router = useRouter();
const loading = ref(false);
const agreement = ref(false);

// 注册表单数据
const registerForm = reactive({
  userAccount: '',
  userPassword: '',
  checkPassword: '',
});

// 自定义验证：确认密码
const validatePassword = async (_rule: Rule, value: string) => {
  if (value === '') {
    return Promise.reject('请再次输入密码');
  } else if (value !== registerForm.userPassword) {
    return Promise.reject('两次输入的密码不一致');
  } else {
    return Promise.resolve();
  }
};

// 自定义验证：用户协议
const validateAgreement = async (_rule: Rule, _value: boolean) => {
  if (!agreement.value) {
    return Promise.reject('请阅读并同意用户协议和隐私政策');
  }
  return Promise.resolve();
};

// 表单验证规则
const rules: Record<string, Rule[]> = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 4, max: 16, message: '账号长度为4-16位', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '账号只能包含字母、数字和下划线', trigger: 'blur' },
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少6位', trigger: 'blur' },
  ],
  checkPassword: [
    { required: true, validator: validatePassword, trigger: 'blur' },
  ],
  agreement: [
    { validator: validateAgreement, trigger: 'change' },
  ],
};

// 处理注册
const handleRegister = async () => {
  if (!agreement.value) {
    message.warning('请先同意用户协议和隐私政策');
    return;
  }

  loading.value = true;
  try {
    const res = await userRegister(registerForm);
    if (res.data.code === 0 && res.data.data) {
      message.success('注册成功！即将跳转到登录页面');
      setTimeout(() => {
        router.push('/login');
      }, 1500);
    } else {
      message.error(res.data.message || '注册失败');
    }
  } finally {
    loading.value = false;
  }
};

// 返回登录页
const handleBackToLogin = () => {
  router.push('/login');
};

// 显示用户协议
const showTerms = () => {
  message.info('用户协议详情');
};

// 显示隐私政策
const showPrivacy = () => {
  message.info('隐私政策详情');
};
</script>

<style scoped>
.register-page {
  height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.register-container {
  position: relative;
  z-index: 10;
  width: 100%;
  max-width: 440px;
}

.register-card {
  background: #fff;
  border-radius: 16px;
  padding: 28px 40px;
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

.register-header {
  text-align: center;
  margin-bottom: 16px;
}

.register-logo {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  margin-bottom: 12px;
  object-fit: cover;
}

.register-title {
  font-size: 24px;
  font-weight: 700;
  color: #001529;
  margin-bottom: 6px;
}

.register-subtitle {
  font-size: 14px;
  color: #666;
  margin: 0;
}

.register-form {
  margin-top: 12px;
}

.register-form :deep(.ant-form-item) {
  margin-bottom: 14px;
}

.register-button {
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 8px;
  margin-top: 8px;
}

.login-link {
  text-align: center;
  color: #666;
  font-size: 14px;
}

.login-link a {
  color: #1890ff;
  font-weight: 600;
  margin-left: 4px;
}

.login-link a:hover {
  color: #40a9ff;
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
  .register-card {
    padding: 36px 28px;
  }

  .register-title {
    font-size: 24px;
  }

  .register-subtitle {
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .register-page {
    padding: 16px;
  }

  .register-card {
    padding: 28px 20px;
  }

  .register-title {
    font-size: 22px;
  }

  .register-logo {
    width: 56px;
    height: 56px;
  }
}
</style>
