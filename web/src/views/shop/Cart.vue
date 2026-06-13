<template>
  <div style="padding:15px">
    <el-card v-for="item in list" :key="item.id" style="margin-bottom:10px">
      <div style="display:flex;justify-content:space-between;align-items:center">
        <div><h4>商品ID: {{ item.productId }}</h4><span v-if="item.spec">规格: {{ item.spec }}</span></div>
        <el-input-number v-model="item.quantity" :min="1" size="small" @change="update(item)" />
        <el-button text type="danger" @click="remove(item)">删除</el-button>
      </div>
    </el-card>
    <el-button type="danger" style="width:100%;margin-top:15px" @click="checkout">去结算</el-button>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const list = ref([])
const load = async () => {
  const res = await request.get('/cart')
  list.value = res.data
}
const update = async (item) => { await request.put(`/cart/${item.id}`, null, { params: { quantity: item.quantity } }) }
const remove = async (item) => { await request.delete(`/cart/${item.id}`); ElMessage.success('已删除'); load() }
const checkout = () => {}
onMounted(load)
</script>
