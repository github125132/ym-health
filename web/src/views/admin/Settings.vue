<template>
  <el-card>
    <h3>系统设置</h3>
    <el-form label-width="150px">
      <el-form-item v-for="(val, key) in configs" :key="key" :label="key">
        <el-input v-model="configs[key]" />
      </el-form-item>
      <el-button type="primary" @click="save">保存</el-button>
    </el-form>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const configs = ref({})
onMounted(async () => {
  const res = await request.get('/admin/config')
  const obj = {}
  ;(res.data || []).forEach(c => { obj[c.configKey || c.key] = c.value })
  configs.value = obj
})
const save = async () => {
  for (const [key, value] of Object.entries(configs.value)) {
    await request.put(`/admin/config/${key}`, null, { params: { value } })
  }
  ElMessage.success('已保存')
}
</script>
