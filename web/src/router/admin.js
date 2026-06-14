export default [
  { path: '/admin/login', component: () => import('../views/admin/Login.vue') },
  {
    path: '/admin',
    component: () => import('../views/admin/Layout.vue'),
    redirect: '/admin/dashboard',
    children: [
      { path: 'dashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'products', component: () => import('../views/admin/ProductList.vue') },
      { path: 'orders', component: () => import('../views/admin/OrderList.vue') },
      { path: 'members', component: () => import('../views/admin/MemberList.vue') },
      { path: 'finance', component: () => import('../views/admin/FinanceList.vue') },
      { path: 'withdraw', component: () => import('../views/admin/WithdrawList.vue') },
      { path: 'categories', component: () => import('../views/admin/CategoryList.vue') },
      { path: 'ads', component: () => import('../views/admin/AdList.vue') },
      { path: 'content', component: () => import('../views/admin/ContentList.vue') },
      { path: 'admins', component: () => import('../views/admin/AdminList.vue') },
      { path: 'roles', component: () => import('../views/admin/RoleList.vue') },
      { path: 'messages', component: () => import('../views/admin/MessageList.vue') },
      { path: 'settings', component: () => import('../views/admin/Settings.vue') },
    ]
  }
]
