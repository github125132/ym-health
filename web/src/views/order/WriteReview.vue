<template>
  <div style="padding:15px">
    <h2>发表评价</h2>
    <div v-if="items.length" v-for="item in items" :key="item.id" style="border:1px solid #eee;border-radius:8px;padding:12px;margin-bottom:12px">
      <div style="display:flex;gap:12px;align-items:center;margin-bottom:10px">
        <img :src="item.productImage" style="width:60px;height:60px;object-fit:cover;border-radius:6px" />
        <div>{{ item.productName }}</div>
      </div>
      <div style="margin-bottom:8px">
        <span style="margin-right:8px">评分：</span>
        <el-rate v-model="ratings[item.id]" :max="5" />
      </div>
      <el-input v-model="contents[item.id]" type="textarea" placeholder="写写对商品的感受吧" :rows="3" />
    </div>
    <el-button type="primary" @click="submit" style="margin-top:15px" :loading="submitting">提交评价</el-button>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
const route = useRoute(), router = useRouter()
const order = ref(null), items = ref([]), submitting = ref(false)
const ratings = reactive({}), contents = reactive({})
const loadOrder = async () => {
  const res = await request.get(`/orders/${route.params.orderNo}`)
  order.value = res.data
}
const loadItems = async () => {
  const res = await request.get('/orders/items', { params: { orderNo: route.params.orderNo } })
  items.value = res.data || []
  items.value.forEach(i => { ratings[i.id] = 5; contents[i.id] = '' })
}
const submit = async () => {
  if (!items.value.length) { ElMessage.warning('没有可评价的商品'); return }
  for (const item of items.value) {
    if (!contents[item.id] || !contents[item.id].trim()) {
      ElMessage.warning('请填写评价内容')
      return
    }
  }
  submitting.value = true
  try {
    for (const item of items.value) {
      await request.post('/reviews', null, {
        params: {
          productId: item.productId,
          orderNo: route.params.orderNo,
          rating: ratings[item.id] || 5,
          content: contents[item.id] || ''
        }
      })
    }
    ElMessage.success('评价成功')
    router.push('/orders')
  } finally {
    submitting.value = false
  }
}
onMounted(() => { loadOrder(); loadItems() })
</script>
