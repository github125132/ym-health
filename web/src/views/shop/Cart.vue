<template>
  <div style="padding:15px">
    <el-card v-for="item in list" :key="item.id" style="margin-bottom:10px">
      <div style="display:flex;justify-content:space-between;align-items:center">
        <div style="display:flex;align-items:center">
          <img :src="item.productImage" style="width:60px;height:60px;object-fit:cover;margin-right:10px;border-radius:4px" @error="e => e.target.style.display='none'" />
          <div>
            <div>{{ item.productName }}</div>
            <div style="color:#999;font-size:12px">规格: {{ item.spec || '默认' }}</div>
          </div>
        </div>
        <el-input-number v-model="item.quantity" :min="1" size="small" @change="update(item)" />
        <el-button text type="danger" @click="remove(item)">删除</el-button>
      </div>
    </el-card>
    <el-button type="danger" style="width:100%;margin-top:15px" @click="checkout">去结算</el-button>

    <el-dialog v-model="checkoutVisible" title="填写收货信息" width="90%">
      <el-form :model="checkoutForm" ref="checkoutFormRef" :rules="checkoutRules" label-width="80px">
        <el-form-item label="所在地区" prop="region">
          <el-cascader v-model="checkoutForm.region" :options="regions" placeholder="省/市/区" style="width:100%" />
        </el-form-item>
        <el-form-item label="详细地址" prop="detail">
          <el-input v-model="checkoutForm.detail" placeholder="详细地址" />
        </el-form-item>
        <el-form-item label="收货人" prop="receiver">
          <el-input v-model="checkoutForm.receiver" placeholder="收货人" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="checkoutForm.phone" placeholder="手机号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="checkoutVisible = false">取消</el-button>
        <el-button type="primary" @click="submitOrder">提交订单</el-button>
      </template>
    </el-dialog>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import request from '../../api/request'
import { ElMessage } from 'element-plus'
import { regions } from '../../utils/regions'
const router = useRouter()
const list = ref([])
const checkoutVisible = ref(false)
const checkoutFormRef = ref(null)
const checkoutForm = reactive({ region: [], detail: '', receiver: '', phone: '' })
const checkoutRules = {
  region: [{ required: true, message: '请选择所在地区' }],
  detail: [{ required: true, message: '请输入详细地址' }],
  receiver: [{ required: true, message: '请输入收货人' }],
  phone: [{ required: true, message: '请输入手机号' }, { pattern: /^1\d{10}$/, message: '手机号格式不正确' }]
}
const load = async () => {
  const res = await request.get('/cart')
  const items = res.data || []
  for (const item of items) {
    try {
      const prod = await request.get(`/products/${item.productId}`)
      item.productName = prod.data?.title || '未知商品'
      item.productPrice = prod.data?.basePrice || 0
      item.productImage = prod.data?.purl || ''
    } catch {
      item.productName = `商品 #${item.productId}`
    }
  }
  list.value = items
}
const update = async (item) => { await request.put(`/cart/${item.id}`, null, { params: { quantity: item.quantity } }) }
const remove = async (item) => { await request.delete(`/cart/${item.id}`); ElMessage.success('已删除'); load() }
const checkout = () => { checkoutForm.region = []; checkoutForm.detail = ''; checkoutForm.receiver = ''; checkoutForm.phone = ''; checkoutVisible.value = true }
const submitOrder = async () => {
  if (!checkoutFormRef.value) return
  await checkoutFormRef.value.validate()
  const addr = (checkoutForm.region || []).join(' ') + ' ' + checkoutForm.detail
  await request.post('/orders', { receiverAddr: addr, receiverName: checkoutForm.receiver, receiverPhone: checkoutForm.phone })
  ElMessage.success('下单成功')
  checkoutVisible.value = false
  router.push('/orders')
}
onMounted(load)
</script>
