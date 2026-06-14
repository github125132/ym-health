<template>
  <div style="padding:15px">
    <h2 style="margin-bottom:15px">排行榜</h2>
    <el-table :data="list" stripe>
      <el-table-column type="index" label="排名" width="80" />
      <el-table-column prop="realName" label="姓名" />
      <el-table-column prop="phone" label="手机号" />
      <el-table-column prop="userLevel" label="等级" width="80" />
      <el-table-column label="加入时间" width="180">
        <template #default="scope">{{ formatDate(scope.row.addDate) }}</template>
      </el-table-column>
    </el-table>
    <el-pagination background layout="prev,pager,next" :total="total" v-model:current-page="page" @current-change="load" style="margin-top:15px" />
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
const list = ref([]), page = ref(1), total = ref(0)
const load = async () => {
  const res = await request.get('/ranking', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
const formatDate = d => d ? d.substring(0, 10) : ''
onMounted(load)
</script>
