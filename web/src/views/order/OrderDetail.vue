<template>
  <div v-if="order" style="padding:15px">
    <el-card>
      <div>订单号: {{ order.orderNo }}</div>
      <div>状态: {{ statusText(order.orderStatus) }}</div>
      <div>金额: ¥{{ order.totalAmount }}</div>
      <div v-if="order.expressCompany">快递: {{ order.expressCompany }} {{ order.expressNo }}</div>
    </el-card>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import request from '../../api/request'
const route = useRoute()
const order = ref(null)
const statusText = s => ({ 0:'待付款',1:'待发货',2:'待收货',3:'已完成',4:'已取消' }[s]||'未知')
onMounted(async () => { const res = await request.get(`/orders/${route.params.no}`); order.value = res.data })
</script>
