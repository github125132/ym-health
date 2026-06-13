<template>
  <el-card>
    <el-table :data="list" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="author" label="作者" />
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
  const res = await request.get('/admin/content', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(load)
</script>
