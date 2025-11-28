<template>
  <a-layout-footer class="global-footer">
    <div class="footer-container">
      <!-- <div class="footer-content">
        <div class="footer-section">
          <h4>关于我们</h4>
          <p>智能代码应用 - 提供高效的代码开发解决方案</p>
        </div>
        <div class="footer-section">
          <h4>快速链接</h4>
          <ul>
            <li><a href="#">文档</a></li>
            <li><a href="#">API</a></li>
            <li><a href="#">支持</a></li>
          </ul>
        </div>
        <div class="footer-section">
          <h4>联系方式</h4>
          <ul>
            <li>邮箱: contact@example.com</li>
            <li>电话: +86 123-4567-8900</li>
          </ul>
        </div>
      </div> -->
      <div class="footer-bottom">
        <p>&copy; {{ currentYear }} 智能代码应用. 保留所有权利.</p>
        <p class="footer-version">版本 1.0.0</p>
      </div>
    </div>
  </a-layout-footer>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const currentYear = computed(() => new Date().getFullYear());
</script>

<style scoped>
.global-footer {
  background: #f5f5f5;
  color: rgba(0, 0, 0, 0.65);
  padding: 10px 24px 24px;
  margin-top: auto;
}

.footer-container {
  max-width: 1200px;
  margin: 0 auto;
}

/*
.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 32px;
  margin-bottom: 32px;
}

.footer-section h4 {
  color: #fff;
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 16px;
}

.footer-section p {
  margin: 0;
  line-height: 1.6;
}

.footer-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.footer-section ul li {
  margin-bottom: 8px;
}

.footer-section a {
  color: rgba(255, 255, 255, 0.65);
  text-decoration: none;
  transition: color 0.3s;
}

.footer-section a:hover {
  color: #1890ff;
}
*/

.footer-bottom {
  padding-top: 16px;
  text-align: center;
}

.footer-bottom p {
  margin: 4px 0;
  color: rgba(0, 0, 0, 0.45);
  font-size: 14px;
}

.footer-version {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.35);
}

/* 响应式设计 */
@media (max-width: 768px) {
  .global-footer {
    padding: 32px 16px 16px;
  }

  /* .footer-content {
    grid-template-columns: 1fr;
    gap: 24px;
    margin-bottom: 24px;
  }

  .footer-section {
    text-align: center;
  } */
}

@media (max-width: 480px) {
  .global-footer {
    padding: 24px 12px 12px;
  }

  /* .footer-section h4 {
    font-size: 14px;
  } */

  .footer-bottom p {
    font-size: 12px;
  }
}
</style>
