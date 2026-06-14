<template>
  <div style="padding:15px">
    <el-card>
      <h3>申请提现</h3>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="60px">
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="form.amount" :min="1" />
        </el-form-item>
        <el-form-item label="银行" prop="bankName">
          <el-input v-model="form.bankName" />
        </el-form-item>
        <el-form-item label="卡号" prop="bankCard">
          <el-input v-model="form.bankCard" />
        </el-form-item>
        <el-button type="primary" @click="submit">提交申请</el-button>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const formRef = ref(null)
const form = reactive({ amount: 100, bankName: '', bankCard: '' })
const rules = {
  amount: [{ required: true, message: '请输入金额' }, { type: 'number', min: 1, message: '金额至少1元' }],
  bankName: [{ required: true, message: '请输入银行名称' }],
  bankCard: [{ required: true, message: '请输入卡号' }]
}
const submit = async () => {
  if (!formRef.value) return
  await formRef.value.validate()
  await request.post('/finance/withdraw', null, { params: { amount: form.amount, bankName: form.bankName, bankCard: form.bankCard } })
  ElMessage.success('提现申请已提交')
}
</script>
