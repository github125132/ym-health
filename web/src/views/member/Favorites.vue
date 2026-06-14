<template>
  <div style="padding:15px">
    <el-card v-for="f in list" :key="f.id" style="margin-bottom:10px">
      商品ID: {{ f.targetId }}
      <el-button text type="danger" size="small" @click="remove(f)">取消收藏</el-button>
    </el-card>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const list = ref([])
const load = async () => { const res = await request.get('/favorites'); list.value = res.data || [] }
const remove = async (f) => { await request.delete(`/favorites/${f.targetId}`); ElMessage.success('已取消'); load() }
onMounted(load)
</script>
