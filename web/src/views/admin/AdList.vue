<template>
  <el-card>
    <el-button type="primary" style="margin-bottom:10px" @click="openForm()">新增广告</el-button>
    <el-table :data="list" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="position" label="位置" width="100" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="openForm(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" />

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑广告' : '新增广告'">
      <el-form :model="form" label-width="80px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="位置"><el-input v-model="form.position" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortOrder" :min="0" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const list = ref([]), page = ref(1), total = ref(0), dialogVisible = ref(false), editId = ref(null)
const form = reactive({ title: '', position: '', sortOrder: 0 })
const load = async () => {
  const res = await request.get('/admin/ads', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
const openForm = (row) => {
  if (row) { editId.value = row.id; form.title = row.title; form.position = row.position; form.sortOrder = row.sortOrder }
  else { editId.value = null; form.title = ''; form.position = ''; form.sortOrder = 0 }
  dialogVisible.value = true
}
const save = async () => {
  if (editId.value) await request.post('/admin/ads', { ...form, id: editId.value })
  else await request.post('/admin/ads', form)
  dialogVisible.value = false; ElMessage.success('已保存'); load()
}
const del = async (row) => { await request.delete(`/admin/ads/${row.id}`); ElMessage.success('已删除'); load() }
onMounted(load)
</script>
