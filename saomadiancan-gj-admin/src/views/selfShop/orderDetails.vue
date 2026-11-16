<template>
	<div class="nf-page nf-orderdetails-page">
		<div class="nf-page-hero nf-orderdetails-hero">
			<a href="#" class="nf-orderdetails-back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-orderdetails-back__icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-badge">订单</span>
			<h2 class="nf-orderdetails-hero__title">订单详情</h2>
			<p class="nf-orderdetails-hero__desc">查看自营商品订单基本信息、商品明细、发货与收货及物流状态</p>
		</div>

		<div class="nf-orderdetails-layout">
			<div class="nf-orderdetails-main">
				<div class="nf-table-card nf-orderdetails-info-card">
					<div class="nf-filter-card__head nf-orderdetails-card__head">
						<i class="el-icon-document"></i>
						<span>订单信息</span>
					</div>
					<div class="nf-orderdetails-info-grid">
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item nf-orderdetails-info-item--wide">
								<span class="nf-orderdetails-info-label">订单号</span>
								<span class="nf-orderdetails-info-value">{{ tableData.orderNum }}</span>
							</div>
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">订单备注</span>
								<span class="nf-orderdetails-info-value">{{ tableData.descrition ? tableData.descrition : '暂无备注' }}</span>
							</div>
						</div>
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item nf-orderdetails-info-item--wide">
								<span class="nf-orderdetails-info-label">订单状态</span>
								<span class="nf-orderdetails-info-value nf-orderdetails-status-text">
									<span v-if="tableData.status === 1">待付款</span>
									<span v-if="tableData.status === 2">待发货</span>
									<span v-if="tableData.status === 3">已发货</span>
									<span v-if="tableData.status === 4">已收货</span>
									<span v-if="tableData.status === 5">已取消</span>
									<span v-if="tableData.status === 6">退款中</span>
									<span v-if="tableData.status === 7">已退款</span>
									<span v-if="tableData.status === 8">已驳回</span>
									<span v-if="tableData.status === 9">拼团中</span>
									<span v-if="tableData.status === 10">已评价</span>
									<span v-if="tableData.status === 11">待审核退货</span>
									<span v-if="tableData.status === 12">已同意退货</span>
									<span v-if="tableData.status === 13">拒绝退货</span>
									<span v-if="tableData.status === 14">申请换货</span>
									<span v-if="tableData.status === 15">已同意，买家待退货</span>
									<span v-if="tableData.status === 16">买家已发货</span>
									<span v-if="tableData.status === 17">拒绝换货</span>
									<span v-if="tableData.status === 18">退款，买家已发货</span>
								</span>
							</div>
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">支付方式</span>
								<span class="nf-orderdetails-info-value">
									<span v-if="tableData.payWay === 1">微信</span>
									<span v-if="tableData.payWay === 2">微信公众号</span>
									<span v-if="tableData.payWay === 3">微信小程序</span>
									<span v-if="tableData.payWay === 4">app支付宝</span>
									<span v-if="tableData.payWay === 5">H5支付宝</span>
									<span v-if="tableData.payWay === 6">零钱</span>
									<span v-if="tableData.payWay === 7">积分</span>
									<span v-if="tableData.payWay === null">未支付</span>
								</span>
							</div>
						</div>
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item nf-orderdetails-info-item--wide">
								<span class="nf-orderdetails-info-label">创建时间</span>
								<span class="nf-orderdetails-info-value">{{ tableData.createAt }}</span>
							</div>
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">付款时间</span>
								<span class="nf-orderdetails-info-value">{{ tableData.payTime }}</span>
							</div>
						</div>
					</div>
				</div>

				<div class="nf-table-card nf-orderdetails-goods-card">
					<div class="nf-filter-card__head nf-orderdetails-card__head">
						<i class="el-icon-goods"></i>
						<span>商品信息</span>
					</div>
					<div class="nf-orderdetails-goods-table">
						<div class="nf-orderdetails-goods-head">
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--img">商品图片</span>
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--title">商品标题</span>
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--price">商品价格</span>
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--pay">支付金额</span>
						</div>
						<div class="nf-orderdetails-goods-body">
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--img">
								<img :src="tableData.img" alt="" class="nf-orderdetails-goods-thumb">
							</span>
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--title">{{ tableData.title }}</span>
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--price">￥{{ tableData.price }} x {{ tableData.number }}</span>
							<span class="nf-orderdetails-goods-col nf-orderdetails-goods-col--pay nf-orderdetails-amount-text">￥{{ tableData.payMoney }}</span>
						</div>
					</div>
				</div>
			</div>

			<div class="nf-orderdetails-side">
				<div class="nf-table-card nf-orderdetails-side-card">
					<div class="nf-filter-card__head nf-orderdetails-card__head">
						<i class="el-icon-s-promotion"></i>
						<span>发货信息</span>
					</div>
					<div class="nf-orderdetails-info-grid nf-orderdetails-info-grid--compact">
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">发货人</span>
								<span class="nf-orderdetails-info-value">官方</span>
							</div>
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">订单数量</span>
								<span class="nf-orderdetails-info-value">{{ tableData.number }}件</span>
							</div>
						</div>
					</div>
				</div>

				<div class="nf-table-card nf-orderdetails-side-card">
					<div class="nf-filter-card__head nf-orderdetails-card__head">
						<i class="el-icon-location-outline"></i>
						<span>收货信息</span>
					</div>
					<div class="nf-orderdetails-info-grid nf-orderdetails-info-grid--compact">
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">收货人</span>
								<span class="nf-orderdetails-info-value">{{ tableData.consignee }}</span>
							</div>
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">手机号</span>
								<span class="nf-orderdetails-info-value">{{ tableData.mobile }}</span>
							</div>
						</div>
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item nf-orderdetails-info-item--full">
								<span class="nf-orderdetails-info-label">收货地址</span>
								<span class="nf-orderdetails-info-value">{{ tableData.provinces }}{{ tableData.detail }}</span>
							</div>
						</div>
					</div>
				</div>

				<div class="nf-table-card nf-orderdetails-side-card" v-if="tableData.isExpress == 1">
					<div class="nf-filter-card__head nf-orderdetails-card__head">
						<i class="el-icon-truck"></i>
						<span>物流信息</span>
					</div>
					<div class="nf-orderdetails-info-grid nf-orderdetails-info-grid--compact" v-if="tableData.status == 3 || tableData.status == 4">
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">物流公司</span>
								<span class="nf-orderdetails-info-value">{{ tableData.expressName }}</span>
							</div>
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">运单号</span>
								<span class="nf-orderdetails-info-value">{{ tableData.expressNumber }}</span>
							</div>
						</div>
					</div>
					<div class="nf-orderdetails-empty" v-if="tableData.status !== 3 && tableData.status !== 4">
						暂无信息
					</div>
				</div>

				<div class="nf-table-card nf-orderdetails-side-card" v-else>
					<div class="nf-filter-card__head nf-orderdetails-card__head">
						<i class="el-icon-tickets"></i>
						<span>虚拟商品信息</span>
					</div>
					<div class="nf-orderdetails-info-grid nf-orderdetails-info-grid--compact">
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">商品标题</span>
								<span class="nf-orderdetails-info-value">{{ virtualData.title }}</span>
							</div>
							<div class="nf-orderdetails-info-item">
								<span class="nf-orderdetails-info-label">兑换码</span>
								<span class="nf-orderdetails-info-value">{{ virtualData.content }}</span>
							</div>
						</div>
						<div class="nf-orderdetails-info-row">
							<div class="nf-orderdetails-info-item nf-orderdetails-info-item--full">
								<span class="nf-orderdetails-info-label">兑换地址</span>
								<span class="nf-orderdetails-info-value">{{ virtualData.linkUrl }}</span>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
	    data() {
	      return {
			  id:'',
			  expressName:'',
			  expressNumber:'',
			  orderName:'',
			  orderNumber:'',
			  virtualId:'',
			  dialogFormVisible:false,
			  tableData:{},
			  virtualData:{},
			}
		},

		methods: {
			// 返回上一级
			prev(){
				this.$router.back()
			},
			// 获取订单详情数据
			dataSelect () {
			  let id = this.$route.query.id
			  this.tableDataLoading = true
			  this.$http({
				url: this.$http.adornUrl('orders/find'),
				method: 'get',
				params: this.$http.adornParams({
					'id':id
				})
			  }).then(({data}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
					this.virtualId = returnData.virtualId
					console.log(this.virtualId,returnData)
					this.$http({
						url: this.$http.adornUrl('selfGoodsVirtual/find'),
						method: 'get',
						params: this.$http.adornParams({
							'id':this.virtualId
						})
					}).then(({data}) => {
						this.tableDataLoading = false
						let returnData = data.data;
						this.virtualData = returnData
					})
			  })
			}
		},
		activated() {
			this.dataSelect()
		}
	  };
</script>

<style scoped>
.nf-orderdetails-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-orderdetails-hero {
  margin-bottom: 12px;
}

.nf-orderdetails-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--nf-text-secondary, #909399);
  text-decoration: none;
  transition: color 0.2s;
}

.nf-orderdetails-back:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-orderdetails-back__icon {
  width: 1.2em;
  height: 1.2em;
}

.nf-orderdetails-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-orderdetails-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-orderdetails-layout {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.nf-orderdetails-main {
  flex: 1;
  min-width: 0;
}

.nf-orderdetails-side {
  width: 36%;
  min-width: 280px;
  flex-shrink: 0;
}

.nf-orderdetails-info-card,
.nf-orderdetails-goods-card,
.nf-orderdetails-side-card {
  margin-bottom: 12px;
}

.nf-orderdetails-side-card:last-child {
  margin-bottom: 0;
}

.nf-orderdetails-card__head {
  margin-bottom: 12px;
}

.nf-orderdetails-info-grid {
  border: 1px solid var(--nf-border, #e8eaed);
  border-radius: 6px;
  overflow: hidden;
}

.nf-orderdetails-info-row {
  display: flex;
  border-bottom: 1px solid var(--nf-border, #e8eaed);
}

.nf-orderdetails-info-row:last-child {
  border-bottom: none;
}

.nf-orderdetails-info-item {
  flex: 1;
  display: flex;
  min-width: 0;
  border-right: 1px solid var(--nf-border, #e8eaed);
}

.nf-orderdetails-info-item:last-child {
  border-right: none;
}

.nf-orderdetails-info-item--wide {
  flex: 1.6;
}

.nf-orderdetails-info-item--full {
  flex: 1;
  border-right: none;
}

.nf-orderdetails-info-label {
  flex-shrink: 0;
  width: 88px;
  padding: 12px 16px;
  background-color: var(--nf-sidebar-bg-alt, #f7f8fa);
  border-right: 1px solid var(--nf-border, #e8eaed);
  font-size: 13px;
  font-weight: 500;
  color: var(--nf-text-primary, rgba(0, 0, 0, 0.85));
  line-height: 1.5;
}

.nf-orderdetails-info-value {
  flex: 1;
  min-width: 0;
  padding: 12px 16px;
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  line-height: 1.5;
  word-break: break-word;
}

.nf-orderdetails-info-grid--compact .nf-orderdetails-info-label {
  width: 76px;
}

.nf-orderdetails-goods-table {
  border: 1px solid var(--nf-border, #e8eaed);
  border-radius: 6px;
  overflow: hidden;
}

.nf-orderdetails-goods-head,
.nf-orderdetails-goods-body {
  display: flex;
  align-items: center;
}

.nf-orderdetails-goods-head {
  background-color: var(--nf-sidebar-bg-alt, #f7f8fa);
  border-bottom: 1px solid var(--nf-border, #e8eaed);
  font-size: 13px;
  font-weight: 500;
  color: var(--nf-text-primary, rgba(0, 0, 0, 0.85));
}

.nf-orderdetails-goods-body {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
}

.nf-orderdetails-goods-col {
  padding: 12px 16px;
  line-height: 1.5;
  word-break: break-word;
}

.nf-orderdetails-goods-col--img {
  width: 100px;
  flex-shrink: 0;
}

.nf-orderdetails-goods-col--title {
  flex: 1;
  min-width: 0;
}

.nf-orderdetails-goods-col--price {
  width: 140px;
  flex-shrink: 0;
}

.nf-orderdetails-goods-col--pay {
  width: 120px;
  flex-shrink: 0;
}

.nf-orderdetails-goods-thumb {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
  vertical-align: middle;
}

.nf-orderdetails-status-text {
  color: var(--nf-brand, #ffa8c2);
}

.nf-orderdetails-amount-text {
  color: var(--nf-brand, #ffa8c2);
  font-weight: 500;
}

.nf-orderdetails-empty {
  padding: 12px 16px;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  border: 1px solid var(--nf-border, #e8eaed);
  border-radius: 6px;
}

@media (max-width: 992px) {
  .nf-orderdetails-layout {
    flex-direction: column;
  }

  .nf-orderdetails-side {
    width: 100%;
    min-width: 0;
  }

  .nf-orderdetails-info-row {
    flex-direction: column;
  }

  .nf-orderdetails-info-item {
    border-right: none;
    border-bottom: 1px solid var(--nf-border, #e8eaed);
  }

  .nf-orderdetails-info-item:last-child {
    border-bottom: none;
  }
}
</style>
