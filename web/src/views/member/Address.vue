<template>
  <div style="padding:15px">
    <el-button type="primary" style="margin-bottom:10px" @click="showForm=true">新增地址</el-button>
    <el-card v-for="a in list" :key="a.id" style="margin-bottom:10px">
      <div>{{ a.receiver }} {{ a.phone }}</div>
      <div>{{ a.province }}{{ a.city }}{{ a.district }}{{ a.address }}</div>
      <div style="margin-top:5px">
        <el-tag v-if="a.isDefault" type="success" size="small">默认</el-tag>
        <el-button text size="small" @click="setDefault(a)">设为默认</el-button>
        <el-button text size="small" type="danger" @click="del(a)">删除</el-button>
      </div>
    </el-card>
    <el-dialog v-model="showForm" title="地址">
      <el-form :model="form">
        <el-input v-model="form.receiver" placeholder="收货人" style="margin-bottom:10px" />
        <el-input v-model="form.phone" placeholder="手机号" style="margin-bottom:10px" />
        <el-input v-model="form.address" placeholder="详细地址" />
      </el-form>
      <template #footer>
        <el-button @click="showForm=false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const list = ref([]), showForm = ref(false)
const form = reactive({ receiver: '', phone: '', address: '' })
const load = async () => { const res = await request.get('/address'); list.value = res.data || [] }
const save = async () => { await request.post('/address', form); showForm.value = false; ElMessage.success('已保存'); load() }
const setDefault = async (a) => { await request.put(`/address/${a.id}/default`); load() }
const del = async (a) => { await request.delete(`/address/${a.id}`); ElMessage.success('已删除'); load() }
onMounted(load)
</script>
