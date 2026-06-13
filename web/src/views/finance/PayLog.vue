<template>
  <div style="padding:15px">
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
onMounted(async () => { const res = await request.get('/finance/paylog'); list.value = res.data?.records || [] })
</script>
