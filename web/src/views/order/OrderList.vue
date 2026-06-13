<template>
  <div style="padding:15px">
    <el-tabs v-model="tab" @tab-change="load">
      <el-tab-pane label="全部" name="all" /><el-tab-pane label="待付款" name="0" />
      <el-tab-pane label="待发货" name="1" /><el-tab-pane label="已完成" name="3" />
    </el-tabs>
    <el-card v-for="o in list" :key="o.id" style="margin-bottom:10px;cursor:pointer" @click="router.push(`/orders/${o.orderNo}`)">
      <div style="display:flex;justify-content:space-between">
        <span>订单号: {{ o.orderNo }}</span>
        <el-tag :type="statusTag(o.orderStatus)">{{ statusText(o.orderStatus) }}</el-tag>
      </div>
      <div>金额: ¥{{ o.totalAmount }}</div>
    </el-card>
  </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
const router = useRouter()
const list = ref([]), tab = ref('all')
const load = async () => {
  const res = await request.get('/orders', { params: { status: tab.value === 'all' ? undefined : tab.value } })
  list.value = res.data?.records || []
}
const statusText = s => ({ 0:'待付款',1:'待发货',2:'待收货',3:'已完成',4:'已取消' }[s]||'未知')
const statusTag = s => ({ 0:'danger',1:'warning',2:'primary',3:'success',4:'info' }[s]||'')
onMounted(load)
</script>
