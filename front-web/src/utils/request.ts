import axios from 'axios';
import { message } from 'ant-design-vue';

const baseConfig = {
    baseURL: 'http://localhost:8081',
    timeout: 5000,
    withCredentials: true,
    headers: {
        'Content-Type': 'application/json'
    }
}

const request = axios.create(baseConfig);

request.interceptors.request.use(
    config => {
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

request.interceptors.response.use(
    response => {
        const { code, mes } = response.data;
        if (code === 40100) {
            const noTokenMes = '未登录';
            // 未登录
            localStorage.removeItem('token');
            message.error(noTokenMes);
            return Promise.reject(new Error(noTokenMes));
        }
        if (code !== 200) {
            message.error(mes);
            return Promise.reject(new Error(mes));
        }
        return response;
    },
    error => {
        return Promise.reject(error);
    }
);

export default request;