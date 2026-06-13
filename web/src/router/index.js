import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import adminRoutes from './admin'

const routes = [
  ...adminRoutes,
  { path: '/', component: () => import('../views/home/Index.vue') },
  { path: '/login', component: () => import('../views/member/Login.vue') },
  { path: '/register', component: () => import('../views/member/Register.vue') },
  { path: '/member', component: () => import('../views/member/Center.vue'), meta: { requiresAuth: true } },
  { path: '/products', component: () => import('../views/shop/ProductList.vue') },
  { path: '/products/:id', component: () => import('../views/shop/ProductDetail.vue') },
  { path: '/cart', component: () => import('../views/shop/Cart.vue'), meta: { requiresAuth: true } },
  { path: '/orders', component: () => import('../views/order/OrderList.vue'), meta: { requiresAuth: true } },
  { path: '/orders/:no', component: () => import('../views/order/OrderDetail.vue'), meta: { requiresAuth: true } },
  { path: '/member/finance', component: () => import('../views/finance/PayLog.vue'), meta: { requiresAuth: true } },
  { path: '/member/withdraw', component: () => import('../views/finance/Withdraw.vue'), meta: { requiresAuth: true } },
  { path: '/member/team', component: () => import('../views/member/Team.vue'), meta: { requiresAuth: true } },
]

const router = createRouter({ history: createWebHistory(), routes })

router.beforeEach((to, from, next) => {
  if (to.meta.requiresAuth) {
    const auth = useAuthStore()
    if (!auth.isLoggedIn) return next('/login')
  }
  next()
})

export default router
