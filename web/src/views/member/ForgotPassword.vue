<template>
  <div class="forgot-page">
    <el-card class="forgot-card">
      <h2>忘记密码</h2>
      <el-form ref="formRef" :model="form" :rules="rules">
        <el-form-item prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleReset" :loading="loading" style="width:100%">
            重置密码
          </el-button>
        </el-form-item>
      </el-form>
      <div class="tips">密码将重置为 123456</div>
      <div><router-link to="/login">返回登录</router-link></div>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
import { ElMessage } from 'element-plus'

const router = useRouter()
const form = reactive({ phone: '' })
const loading = ref(false)
const rules = { phone: [{ required: true, message: '请输入手机号' }] }

const handleReset = async () => {
  loading.value = true
  try {
    await request.post('/auth/forgot-password', null, { params: { phone: form.phone } })
    ElMessage.success('密码已重置为 123456')
    router.push('/login')
  } catch { ElMessage.error('操作失败') }
  finally { loading.value = false }
}
</script>
<style scoped>
.forgot-page { display: flex; justify-content: center; padding-top: 80px; background: #f5f5f5; min-height: 100vh; }
.forgot-card { width: 360px; }
.tips { color: #999; font-size: 12px; margin-bottom: 10px; }
</style>
