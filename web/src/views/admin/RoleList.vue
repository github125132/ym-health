<template>
  <el-card>
    <el-button type="primary" style="margin-bottom:10px" @click="showForm=true; form={}">新增角色</el-button>
    <el-table :data="list" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="permissions" label="权限" />
      <el-table-column label="操作" width="150">
        <template #default="{ row }">
          <el-button size="small" @click="edit(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="del(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-model:current-page="page" :total="total" @current-change="load" />
    <el-dialog v-model="showForm" title="角色">
      <el-form>
        <el-input v-model="form.name" placeholder="名称" style="margin-bottom:10px" />
        <el-input v-model="form.permissions" placeholder="权限（逗号分隔）" style="margin-bottom:10px" />
      </el-form>
      <template #footer>
        <el-button @click="showForm=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const list = ref([]), page = ref(1), total = ref(0)
const showForm = ref(false), form = ref({})
const load = async () => {
  const res = await request.get('/admin/roles', { params: { page: page.value } })
  list.value = res.data.records; total.value = res.data.total
}
const save = async () => { await request.post('/admin/roles', form.value); ElMessage.success('已保存'); showForm.value = false; load() }
const del = async (row) => { await request.delete(`/admin/roles/${row.id}`); ElMessage.success('已删除'); load() }
const edit = (row) => { form.value = { ...row }; showForm.value = true }
onMounted(load)
</script>
