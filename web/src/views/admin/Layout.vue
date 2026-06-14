<template>
  <el-container style="height:100vh">
    <el-aside :width="sidebarOpen ? '220px' : '0'" style="overflow:hidden;transition:width .3s">
      <el-menu router :default-active="$route.path" style="height:100%">
        <el-menu-item index="/admin/dashboard">仪表盘</el-menu-item>
        <el-menu-item index="/admin/products">商品管理</el-menu-item>
        <el-menu-item index="/admin/categories">分类管理</el-menu-item>
        <el-menu-item index="/admin/ads">广告管理</el-menu-item>
        <el-menu-item index="/admin/orders">订单管理</el-menu-item>
        <el-menu-item index="/admin/members">会员管理</el-menu-item>
        <el-menu-item index="/admin/finance">财务管理</el-menu-item>
        <el-menu-item index="/admin/withdraw">提现管理</el-menu-item>
        <el-menu-item index="/admin/content">内容管理</el-menu-item>
        <el-menu-item index="/admin/admins">管理员</el-menu-item>
        <el-menu-item index="/admin/roles">角色管理</el-menu-item>
        <el-menu-item index="/admin/messages">留言管理</el-menu-item>
        <el-menu-item index="/admin/settings">系统设置</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="display:flex;align-items:center;background:#fff;border-bottom:1px solid #eee;height:48px;padding:0 16px">
        <el-button @click="sidebarOpen = !sidebarOpen" text style="font-size:20px;padding:0">☰</el-button>
        <span style="margin-left:12px;font-weight:600">管理后台</span>
      </el-header>
      <el-main><router-view /></el-main>
    </el-container>
  </el-container>
</template>
<script setup>
import { ref } from 'vue'
const sidebarOpen = ref(window.innerWidth > 768)
</script>
