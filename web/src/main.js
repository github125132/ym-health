import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(ElementPlus)
app.use(router)

// 全局图片加载失败回退
document.addEventListener('error', (e) => {
  if (e.target.tagName === 'IMG') {
    e.target.src = 'data:image/svg+xml,' + encodeURIComponent(
      '<svg xmlns="http://www.w3.org/2000/svg" width="200" height="200" fill="#f5f5f5">' +
      '<rect width="200" height="200"/><text x="50%" y="50%" dominant-baseline="middle" ' +
      'text-anchor="middle" fill="#ccc" font-size="14">暂无图片</text></svg>')
  }
}, true)

app.mount('#app')
