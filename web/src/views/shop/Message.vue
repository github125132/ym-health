<template>
  <div style="padding:15px">
    <el-card>
      <h3>留言反馈</h3>
      <el-form>
        <el-input v-model="form.title" placeholder="标题" style="margin-bottom:10px" />
        <el-input v-model="form.content" type="textarea" :rows="4" placeholder="内容" style="margin-bottom:10px" />
        <el-button type="primary" @click="submit">提交</el-button>
      </el-form>
    </el-card>
  </div>
</template>
<script setup>
import { reactive } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const form = reactive({ title: '', content: '' })
const submit = async () => { await request.post('/messages', form); ElMessage.success('已提交'); form.title = ''; form.content = '' }
</script>
