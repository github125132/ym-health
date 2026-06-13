<template>
  <el-card>
    <el-table :data="list" stripe>
      <el-table-column prop="userId" label="用户ID" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="userLevel" label="等级" width="80" />
      <el-table-column prop="addDate" label="注册时间" />
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" />
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]), page = ref(1), total = ref(0)
const load = async () => {
  const res = await request.get('/admin/members', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
onMounted(load)
</script>
