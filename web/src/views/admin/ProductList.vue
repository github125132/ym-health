<template>
  <el-card>
    <el-button type="primary" style="margin-bottom:10px" @click="showForm=true">新增商品</el-button>
    <el-table :data="list" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="basePrice" label="价格" width="120" />
      <el-table-column prop="stock" label="库存" width="80" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="edit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row)">删除</el-button>
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
  const res = await request.get('/admin/products', { params: { page: page.value } })
  list.value = res.data.records
  total.value = res.data.total
}
const del = async (row) => { await request.delete(`/admin/products/${row.id}`); ElMessage.success('已删除'); load() }
const edit = (row) => {}
onMounted(load)
</script>
