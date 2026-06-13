<template>
  <div class="login-page">
    <el-card class="login-card">
      <h2>登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" :loading="loading" style="width:100%">
            登录
          </el-button>
        </el-form-item>
      </el-form>
      <div class="links">
        <router-link to="/register">注册</router-link>
        <router-link to="/forgot">忘记密码</router-link>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '../../stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const auth = useAuthStore()
const form = reactive({ phone: '', password: '' })
const loading = ref(false)
const rules = {
  phone: [{ required: true, message: '请输入手机号' }],
  password: [{ required: true, message: '请输入密码' }]
}

const handleLogin = async () => {
  loading.value = true
  try {
    await auth.login(form.phone, form.password)
    ElMessage.success('登录成功')
    router.push('/')
  } catch { ElMessage.error('登录失败') }
  finally { loading.value = false }
}
</script>

<style scoped>
.login-page { display: flex; justify-content: center; padding-top: 80px; background: #f5f5f5; min-height: 100vh; }
.login-card { width: 360px; }
.links { display: flex; justify-content: space-between; margin-top: 10px; }
</style>
