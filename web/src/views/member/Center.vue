<template>
  <div style="padding:15px">
    <el-card v-if="member">
      <h3>会员中心</h3>
      <div style="margin:10px 0">
        <div>用户ID: {{ member.userId }}</div>
        <div>手机: {{ member.phone }}</div>
        <div>姓名: {{ member.realName }}</div>
        <div>等级: {{ member.userLevel || 0 }}</div>
      </div>
    </el-card>
    <div style="margin-top:10px">
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/address')">收货地址</el-button>
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/favorites')">我的收藏</el-button>
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/edit-profile')">修改密码</el-button>
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/team')">我的团队</el-button>
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/finance')">财务明细</el-button>
      <el-button style="width:100%;margin-bottom:10px" @click="router.push('/member/withdraw')">申请提现</el-button>
    </div>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
const router = useRouter()
const member = ref(null)
onMounted(async () => {
  const res = await request.get('/member/profile')
  member.value = res.data
})
</script>
