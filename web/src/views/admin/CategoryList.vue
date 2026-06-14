<template>
  <el-card>
    <el-button type="primary" style="margin-bottom:10px" @click="openForm()">新增分类</el-button>
    <el-table :data="list" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="sortOrder" label="排序" width="80" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="openForm(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" />

    <el-dialog v-model="dialogVisible" :title="editId ? '编辑分类' : '新增分类'">
      <el-form :model="form">
        <el-input v-model="form.name" placeholder="分类名称" />
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
const form = reactive({ name: '' })
const load = async () => {
  const res = await request.get('/admin/categories', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
const openForm = (row) => {
  if (row) { editId.value = row.id; form.name = row.name }
  else { editId.value = null; form.name = '' }
  dialogVisible.value = true
}
const save = async () => {
  if (editId.value) await request.post('/admin/categories', { ...form, id: editId.value })
  else await request.post('/admin/categories', form)
  dialogVisible.value = false; ElMessage.success('已保存'); load()
}
const del = async (row) => { await request.delete(`/admin/categories/${row.id}`); ElMessage.success('已删除'); load() }
onMounted(load)
</script>
