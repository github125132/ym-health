<template>
  <el-card>
    <el-table :data="list" stripe>
      <el-table-column prop="userId" label="用户" />
      <el-table-column prop="amount" label="金额" />
      <el-table-column prop="status" label="状态" width="80" />
      <el-table-column prop="createdAt" label="申请时间" />
      <el-table-column label="操作" width="100">
        <template #default="{ row }">
          <el-button v-if="row.status===0" size="small" type="success" @click="approve(row)">通过</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" />
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const list = ref([]), page = ref(1), total = ref(0)
const load = async () => {
  const res = await request.get('/admin/withdraw', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
const approve = async (row) => {
  await request.post(`/admin/withdraw/${row.id}/approve`)
  ElMessage.success('已通过'); load()
}
onMounted(load)
</script>
