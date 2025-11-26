import { createRouter, createWebHistory } from 'vue-router'
import DeptView from '../views/DeptView.vue'
import EmpView from '../views/EmpView.vue' // 1. 导入 EmpView

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/dept' // 默认重定向到部门管理
    },
    {
      path: '/dept',
      name: 'dept',
      component: DeptView
    },
    {
      // 2. 添加员工管理路由
      path: '/emp',
      name: 'emp',
      component: EmpView
    }
  ]
})

export default router