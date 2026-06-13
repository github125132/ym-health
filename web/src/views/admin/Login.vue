<template>
  <div style="display:flex;justify-content:center;padding-top:200px">
    <el-card style="width:360px">
      <h2>后台登录</h2>
      <el-form @submit.prevent="handleLogin">
        <el-form-item><el-input v-model="username" placeholder="用户名" /></el-form-item>
        <el-form-item><el-input v-model="password" type="password" placeholder="密码" /></el-form-item>
        <el-button type="primary" native-type="submit" style="width:100%">登录</el-button>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
const router = useRouter()
const username = ref(''), password = ref('')
const handleLogin = async () => {
  ElMessage.success('登录成功')
  router.push('/admin/dashboard')
}
</script>
