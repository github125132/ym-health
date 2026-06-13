<template>
  <div style="padding:15px">
    <el-card><h3>会员中心</h3></el-card>
    <div style="margin-top:10px">
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/team')">我的团队</el-button>
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/finance')">财务明细</el-button>
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/withdraw')">申请提现</el-button>
    </div>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'
const router = useRouter()
</script>
