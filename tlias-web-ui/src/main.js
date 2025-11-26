import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 1. 导入 Element Plus
import ElementPlus from 'element-plus' 
// 2. 导入 Element Plus 的样式文件
import 'element-plus/dist/index.css'

const app = createApp(App)

// 3. 注册 Element Plus 插件
app.use(ElementPlus)
app.use(router)

app.mount('#app')