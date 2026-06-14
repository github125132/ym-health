<template>
  <div style="padding:15px">
    <el-card v-if="pointsData" style="margin-bottom:15px">
      <el-row :gutter="20">
        <el-col :span="12"><div>可用余额: ¥{{ pointsData.balance || 0 }}</div></el-col>
        <el-col :span="12"><div>积分: {{ pointsData.points || 0 }}</div></el-col>
      </el-row>
    </el-card>
    <el-table :data="list" stripe>
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="description" label="说明" />
      <el-table-column prop="createdAt" label="时间" />
    </el-table>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([])
const pointsData = ref(null)
onMounted(async () => {
  const [logRes, ptRes] = await Promise.all([
    request.get('/finance/paylog'),
    request.get('/finance/points').catch(() => ({}))
  ])
  list.value = logRes.data?.records || []
  pointsData.value = ptRes.data
})
</script>
