<template>
  <el-card>
    <el-table :data="list" stripe>
      <el-table-column prop="userId" label="用户" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="payType" label="类型" />
      <el-table-column prop="description" label="说明" />
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
  const res = await request.get('/admin/finance', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(load)
</script>
