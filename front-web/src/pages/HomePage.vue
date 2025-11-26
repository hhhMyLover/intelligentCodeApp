<template>
  <div class="home-view">
    <div class="welcome-section">
      <h1>欢迎使用智能代码应用</h1>
      <p class="subtitle">高效、智能的代码开发解决方案</p>
    </div>

    <a-row :gutter="[16, 16]" class="features-section">
      <a-col :xs="24" :sm="12" :md="8">
        <a-card hoverable>
          <template #cover>
            <div class="card-icon">
              <CodeOutlined :style="{ fontSize: '48px', color: '#1890ff' }" />
            </div>
          </template>
          <a-card-meta title="智能编码" description="提供智能代码补全和建议，提升开发效率" />
        </a-card>
      </a-col>

      <a-col :xs="24" :sm="12" :md="8">
        <a-card hoverable>
          <template #cover>
            <div class="card-icon">
              <ThunderboltOutlined :style="{ fontSize: '48px', color: '#52c41a' }" />
            </div>
          </template>
          <a-card-meta title="快速开发" description="内置丰富的模板和组件，加速项目开发" />
        </a-card>
      </a-col>

      <a-col :xs="24" :sm="12" :md="8">
        <a-card hoverable>
          <template #cover>
            <div class="card-icon">
              <SafetyOutlined :style="{ fontSize: '48px', color: '#faad14' }" />
            </div>
          </template>
          <a-card-meta title="安全可靠" description="严格的代码审查和安全检测机制" />
        </a-card>
      </a-col>
    </a-row>

    <div class="action-section">
      <a-button type="primary" size="large">开始使用</a-button>
      <a-button size="large" style="margin-left: 16px">了解更多</a-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { CodeOutlined, ThunderboltOutlined, SafetyOutlined } from '@ant-design/icons-vue';
</script>

<style scoped>
.home-view {
  padding: 24px 0;
}

.welcome-section {
  text-align: center;
  margin-bottom: 48px;
}

.welcome-section h1 {
  font-size: 36px;
  font-weight: 700;
  color: #001529;
  margin-bottom: 16px;
}

.subtitle {
  font-size: 18px;
  color: #666;
  margin: 0;
}

.features-section {
  margin-bottom: 48px;
}

.card-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 120px;
  background: #f5f5f5;
}

.action-section {
  text-align: center;
  padding: 24px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome-section h1 {
    font-size: 28px;
  }

  .subtitle {
    font-size: 16px;
  }

  .features-section {
    margin-bottom: 32px;
  }
}

@media (max-width: 480px) {
  .welcome-section h1 {
    font-size: 24px;
  }

  .subtitle {
    font-size: 14px;
  }

  .action-section {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .action-section .ant-btn {
    width: 100%;
    margin-left: 0 !important;
  }
}
</style>
