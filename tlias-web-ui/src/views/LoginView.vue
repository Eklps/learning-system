<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="login-header">
          <h2>  学习辅助系统</h2>
        </div>
      </template>
      
      <el-form :model="loginForm" label-width="0px">
        <el-form-item>
          <el-input 
            v-model="loginForm.username" 
            placeholder="请输入用户名" 
            prefix-icon="User"
          />
        </el-form-item>
        <el-form-item>
          <el-input 
            v-model="loginForm.password" 
            type="password" 
            placeholder="请输入密码" 
            prefix-icon="Lock"
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
import axios from 'axios';
import { useRouter } from 'vue-router'; // 导入路由控制
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue'; // 记得导入图标

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
    // 发送登录请求
    const res = await axios.post('http://localhost:8080/login', loginForm);
    
    if (res.data.code === 1) {
      ElMessage.success('登录成功');
      
      // 1. 把 Token 存入 localStorage (浏览器本地存储)
      const token = res.data.data;
      localStorage.setItem('token', token);
      
      // 2. 跳转到主页 (部门管理)
      router.push('/'); 
    } else {
      ElMessage.error(res.data.message || '登录失败');
    }
  } catch (error) {
    console.error(error);
    ElMessage.error('无法连接服务器');
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
  background-color: #f0f2f5; /* 灰色背景 */
  background-image: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
}
.login-card {
  width: 400px;
}
.login-header {
  text-align: center;
}
</style>