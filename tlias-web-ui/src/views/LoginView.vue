<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h2>tlias 智能学习辅助系统</h2>
        </div>
      </template>
      
      <el-form :model="loginForm" label-width="0px">
        <el-form-item>
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名" 
            :prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码" 
            :prefix-icon="Lock"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" style="width: 100%" @click="handleLogin" :loading="isLoading">
            立即登录
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
// 修正点2: 替换原生 axios 为我们封装的 request 工具
import request from '@/utils/request'; 
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';

const router = useRouter();
const isLoading = ref(false);

const loginForm = reactive({
  username: '',
  password: ''
});

const handleLogin = async () => {
  if(!loginForm.username || !loginForm.password) {
    return ElMessage.warning('请输入用户名和密码');
  }

  try {
    isLoading.value = true;
    
    // 修正点2: 使用 request 发送请求
    // 1. URL 变短了 (baseURL 自动拼接)
    // 2. 依然是 post 方法
    const res = await request.post('/login', loginForm);
    
    // request.js 的响应拦截器通常会返回 response，所以我们要取 res.data
    // 注意：具体取决于你的 request.js 怎么写的。
    // 如果你的 request.js 拦截器里写的是 return response.data，那这里就直接判断 if(res.code === 1)
    // 如果你的 request.js 拦截器里写的是 return response，那这里就是 res.data.code
    // 假设按标准封装返回的是 response：
    if (res.data.code === 1) {
      ElMessage.success('登录成功');
      
      const token = res.data.data;
      localStorage.setItem('token', token);
      
      router.push('/'); 
    } else {
      ElMessage.error(res.data.message || '登录失败');
    }
  } catch (error) {
    console.error(error);
    // request.js 可能已经弹过一次窗了，这里可以视情况保留
    // ElMessage.error('无法连接服务器'); 
  } finally {
    isLoading.value = false;
  }
};
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f0f2f5;
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.login-card {
  width: 400px;
}
.login-header {
  text-align: center;
}
</style>