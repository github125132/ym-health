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
import { useRouter } from 'vue-router'
import request from '../../api/request'
import { ElMessage, ElMessageBox } from 'element-plus'
const router = useRouter()
const list = ref([])
const load = async () => {
  const res = await request.get('/cart')
  list.value = res.data
}
const update = async (item) => { await request.put(`/cart/${item.id}`, null, { params: { quantity: item.quantity } }) }
const remove = async (item) => { await request.delete(`/cart/${item.id}`); ElMessage.success('已删除'); load() }
const checkout = async () => {
  ElMessageBox.prompt('收货地址', '填写收货信息', {
    inputPattern: /.{5,}/,
    inputErrorMessage: '请填写完整地址'
  }).then(async ({ value }) => {
    await request.post('/orders', { receiverAddr: value })
    ElMessage.success('下单成功')
    router.push('/orders')
  }).catch(() => {})
}
onMounted(load)
</script>
