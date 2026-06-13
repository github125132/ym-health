<template>
  <el-row :gutter="20">
    <el-col :span="6" v-for="stat in stats" :key="stat.label">
      <el-card><div class="stat-value">{{ stat.value }}</div><div>{{ stat.label }}</div></el-card>
    </el-col>
  </el-row>
</template>
<script setup>
const stats = [
  { label: '会员总数', value: '—' },
  { label: '今日订单', value: '—' },
  { label: '本月销售额', value: '—' },
  { label: '待处理提现', value: '—' },
]
</script>
<style scoped>.stat-value { font-size: 28px; font-weight: bold; color: #409eff; }</style>
