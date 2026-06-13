<template>
  <el-card>
    <el-table :data="list" stripe>
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="userId" label="用户" />
      <el-table-column prop="totalAmount" label="金额" width="120" />
      <el-table-column prop="orderStatus" label="状态" width="80" />
      <el-table-column prop="createdAt" label="时间" />
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" />
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]), page = ref(1), total = ref(0)
const load = async () => {
  const res = await request.get('/admin/orders', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(load)
</script>
