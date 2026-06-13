<template>
  <div class="register-page">
    <el-card class="register-card">
      <h2>注册</h2>
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="密码" />
        </el-form-item>
        <el-form-item prop="realName">
          <el-input v-model="form.realName" placeholder="真实姓名" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleRegister" :loading="loading" style="width:100%">
            注册
          </el-button>
        </el-form-item>
      </el-form>
      <div><router-link to="/login">已有账号？去登录</router-link></div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { registerApi } from '../../api/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = reactive({ phone: '', password: '', realName: '' })
const loading = ref(false)
const rules = {
  phone: [{ required: true, message: '请输入手机号' }],
  password: [{ required: true, message: '请输入密码' }]
}

const handleRegister = async () => {
  loading.value = true
  try {
    await registerApi(form)
    ElMessage.success('注册成功')
    router.push('/login')
  } catch { ElMessage.error('注册失败') }
  finally { loading.value = false }
}
</script>

<style scoped>
.register-page { display: flex; justify-content: center; padding-top: 80px; background: #f5f5f5; min-height: 100vh; }
.register-card { width: 360px; }
</style>
