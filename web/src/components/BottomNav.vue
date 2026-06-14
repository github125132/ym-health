<template>
  <div class="bottom-nav">
    <router-link to="/" class="nav-item" :class="{ active: $route.path === '/' }">
      <span class="icon">🏠</span><span class="label">首页</span>
    </router-link>
    <router-link to="/products" class="nav-item" :class="{ active: $route.path.startsWith('/products') }">
      <span class="icon">🛍️</span><span class="label">商城</span>
    </router-link>
    <router-link to="/cart" class="nav-item" :class="{ active: $route.path === '/cart' }">
      <span class="icon">🛒</span><span class="label">购物车</span>
    </router-link>
    <router-link to="/member" class="nav-item" :class="{ active: $route.path.startsWith('/member') }">
      <span class="icon">👤</span><span class="label">我的</span>
    </router-link>
  </div>
</template>
<style scoped>
.bottom-nav { position: fixed; bottom: 0; left: 0; right: 0; display: flex; background: #fff; border-top: 1px solid #eee; z-index: 1000; }
.nav-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 6px 0; text-decoration: none; color: #999; font-size: 10px; }
.nav-item.active { color: #409eff; }
.nav-item .icon { font-size: 20px; margin-bottom: 2px; }
</style>
