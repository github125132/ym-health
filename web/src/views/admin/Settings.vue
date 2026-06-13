<template>
  <el-card>
    <h3>系统设置</h3>
    <el-form label-width="120px">
      <el-form-item label="提现门槛"><el-input-number v-model="form.minWithdraw" /></el-form-item>
      <el-form-item label="提现手续费(%)"><el-input-number v-model="form.withdrawFee" /></el-form-item>
      <el-form-item label="积分兑换比例"><el-input-number v-model="form.pointRate" /></el-form-item>
      <el-button type="primary" @click="save">保存</el-button>
    </el-form>
  </el-card>
</template>
<script setup>
import { reactive } from 'vue'
const form = reactive({ minWithdraw: 100, withdrawFee: 5, pointRate: 1 })
const save = () => {}
</script>
