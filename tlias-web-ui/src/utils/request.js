import axios from 'axios';
import { ElMessage } from 'element-plus';
import router from '../router'; // 导入路由，用于强制退回登录页

// 创建一个 axios 实例 (就像创建一个专用的 HTTP 客户端)
const request = axios.create({
    baseURL: 'http://localhost:8080', // 统一设置基础路径
    timeout: 5000
});

// 1. 请求拦截器：在发送请求之前做些什么
request.interceptors.request.use(config => {
    // 从 localStorage 获取 token
    const token = localStorage.getItem('token');
    if (token) {
        // 如果有 token，把它加到请求头里
        config.headers['token'] = token;
    }
    return config;
}, error => {
    return Promise.reject(error);
});

// 2. 响应拦截器：在接收响应之后做些什么
request.interceptors.response.use(response => {
    // 统一处理后端返回的 Result 结构
    const res = response.data;
    
    // 如果后端返回 code=0 (失败)
    if (res.code === 0) {
        ElMessage.error(res.message || '操作失败');
        return Promise.reject(new Error(res.message));
    }
    
    return response; // 成功则返回完整响应
}, error => {
    // 处理 HTTP 错误状态码
    if (error.response && error.response.status === 401) {
        // 401 说明未登录或 Token 过期
        ElMessage.error('登录过期，请重新登录');
        localStorage.removeItem('token'); // 清除无效 token
        router.push('/login'); // 强制跳转登录页
    } else {
        ElMessage.error(error.message || '网络异常');
    }
    return Promise.reject(error);
});

export default request;