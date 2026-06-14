<template>
  <div style="padding:15px">
    <el-table :data="list" stripe>
      <el-table-column prop="amount" label="返利金额" />
      <el-table-column prop="level" label="层级" width="80" />
      <el-table-column prop="orderNo" label="来源订单" />
      <el-table-column prop="createdAt" label="时间" />
    </el-table>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([])
onMounted(async () => { const res = await request.get('/finance/rebate'); list.value = res.data || [] })
</script>
