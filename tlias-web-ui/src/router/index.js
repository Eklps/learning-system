import { createRouter, createWebHistory } from 'vue-router'
// 导入组件
import DeptView from '../views/DeptView.vue'
import EmpView from '../views/EmpView.vue'
import LoginView from '../views/LoginView.vue' // 1. 导入登录页

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 2. 配置登录路由
    {
      path: '/login',
      name: 'login',
      component: LoginView
    },
    {
      path: '/',
      redirect: '/dept'
    },
    {
      path: '/dept',
      name: 'dept',
      component: DeptView
    },
    {
      path: '/emp',
      name: 'emp',
      component: EmpView
    }
  ]
})

// 3. 配置全局前置守卫 (安检)
router.beforeEach((to, from, next) => {
  // 获取 token
  const token = localStorage.getItem('token');
  
  // 判断: 如果要去的地方不是 login，且没有 token，就强制跳转到 login
  if (to.path !== '/login' && !token) {
    next('/login');
  } else {
    next(); // 放行
  }
})

export default router