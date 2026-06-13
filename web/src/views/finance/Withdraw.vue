<template>
  <div style="padding:15px">
    <el-card>
      <h3>申请提现</h3>
      <el-form>
        <el-form-item label="金额"><el-input-number v-model="amount" :min="1" /></el-form-item>
        <el-form-item label="银行"><el-input v-model="bankName" /></el-form-item>
        <el-form-item label="卡号"><el-input v-model="bankCard" /></el-form-item>
        <el-button type="primary" @click="submit">提交申请</el-button>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { ref } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const amount = ref(100), bankName = ref(''), bankCard = ref('')
const submit = async () => {
  await request.post('/finance/withdraw', null, { params: { amount: amount.value, bankName: bankName.value, bankCard: bankCard.value } })
  ElMessage.success('提现申请已提交')
}
</script>
