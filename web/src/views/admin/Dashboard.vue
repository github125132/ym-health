<template>
  <el-row :gutter="20">
    <el-col :span="6" v-for="s in statsList" :key="s.label">
      <el-card>
        <div class="stat-value">{{ s.value }}</div>
        <div>{{ s.label }}</div>
      </el-card>
    </el-col>
  </el-row>
</template>
<script setup>
import { ref, computed, onMounted } from 'vue'
import request from '../../api/request'
const stats = ref({})
const statsList = computed(() => [
  { label: '会员总数', value: stats.value.memberCount ?? '—' },
  { label: '商品总数', value: stats.value.productCount ?? '—' },
  { label: '订单总数', value: stats.value.orderCount ?? '—' },
  { label: '待审核提现', value: stats.value.pendingWithdraw ?? '—' },
])
onMounted(async () => {
  try {
    const res = await request.get('/admin/dashboard')
    stats.value = res.data || {}
  } catch {}
})
</script>
<style scoped>.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }</style>
