import axios from 'axios'
import { message } from 'ant-design-vue';

const baseConfig = {
    baseURL: 'http://localhost:8081/api',
    timeout: 5000,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
}

const request = axios.create(baseConfig);

request.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => {
    return Promise.reject(error)
  },
)

request.interceptors.response.use(
  (response) => {
        const { code, message: msg } = response.data;
        // 未登录
        if (code === 40100) {
            const noTokenMes = '未登录';
            // 清除 token 或其他登录状态（如果有）
            // localStorage.removeItem('token');
            // message.error(noTokenMes);
            // 可以选择跳转到登录页
            window.location.href = '/login';
            return Promise.reject(new Error(noTokenMes));
        }
        // 业务错误（非 0 且非 200，视后端约定，通常 0 为成功）
        if (code !== 0 && code !== 200) {
            message.error(msg || '网络异常');
            return Promise.reject(new Error(msg || '网络异常'));
        }
        return response;
    },
    error => {
        message.error(error.message || '请求失败');
        return Promise.reject(error);
    }
);

export default request;
