<template>
  <div style="padding:15px">
    <el-button type="primary" style="margin-bottom:10px" @click="openForm">新增地址</el-button>
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
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="收货人" prop="receiver">
          <el-input v-model="form.receiver" placeholder="收货人" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="手机号" />
        </el-form-item>
        <el-form-item label="所在地区" prop="region">
          <el-cascader v-model="form.region" :options="regions" placeholder="选择省/市/区" style="width:100%" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="详细地址" />
        </el-form-item>
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
import { regions } from '../../utils/regions'
const list = ref([]), showForm = ref(false)
const formRef = ref(null)
const form = reactive({ receiver: '', phone: '', address: '', region: [] })
const rules = {
  receiver: [{ required: true, message: '请输入收货人' }],
  phone: [{ required: true, message: '请输入手机号' }, { pattern: /^1\d{10}$/, message: '手机号格式不正确' }],
  address: [{ required: true, message: '请输入详细地址' }]
}
const load = async () => { const res = await request.get('/address'); list.value = res.data || [] }
const openForm = () => { form.receiver = ''; form.phone = ''; form.address = ''; form.region = []; showForm.value = true }
const save = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  const r = form.region
  if (r && r.length >= 2) {
    form.province = r[0]
    form.city = r[1]
    if (r.length >= 3) form.district = r[2]
  }
  await request.post('/address', form)
  showForm.value = false
  ElMessage.success('已保存')
  load()
}
const setDefault = async (a) => { await request.put(`/address/${a.id}/default`); load() }
const del = async (a) => { await request.delete(`/address/${a.id}`); ElMessage.success('已删除'); load() }
onMounted(load)
</script>
