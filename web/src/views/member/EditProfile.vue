<template>
  <div style="padding:15px">
    <el-card><h3>修改密码</h3>
      <el-input v-model="form.oldPassword" type="password" placeholder="旧密码" style="margin-bottom:10px" />
      <el-input v-model="form.newPassword" type="password" placeholder="新密码" style="margin-bottom:10px" />
      <el-button type="primary" @click="changePwd">确认修改</el-button>
    </el-card>
  </div>
</template>
<script setup>
import { reactive } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const form = reactive({ oldPassword: '', newPassword: '' })
const changePwd = async () => {
  await request.put('/member/password', form)
  ElMessage.success('密码已修改')
}
</script>
