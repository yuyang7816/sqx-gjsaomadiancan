<template>
	<view>
		<view class="bg padding-tb-xl">
			<view class="box padding">
				<view>
					<!-- <view class="lins"></view> -->
					<view  class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">订单总金额</view>
						<view style="width: 50%;">{{dataCentre.money?dataCentre.money:0}}<text class="text-sm">元</text></view>
						
					</view>
					
					<view   class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">订 单 数 量</view>
						<view style="width: 50%;">{{dataCentre.count?dataCentre.count:0}}<text class="text-sm">单</text></view>
						
					</view>
					<view   class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">外卖订单金额</view>
						<view style="width: 50%;">{{dataCentre.takeMoney1?dataCentre.takeMoney1:0}}<text class="text-sm">元</text></view>
						
					</view>
					
					<view   class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">外卖订单数</view>
						<view style="width: 50%;">{{dataCentre.takeCount2?dataCentre.takeCount2:0}}<text class="text-sm">单</text></view>
						
					</view>
					
					<view   class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">退款订单金额</view>
						<view style="width: 50%;">{{dataCentre.cancelOrderMoney?dataCentre.cancelOrderMoney:0}}<text class="text-sm">元</text></view>
						
					</view>
					
					<view   class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">退款订单数</view>
						<view style="width: 50%;">{{dataCentre.cancelOrderCount?dataCentre.cancelOrderCount:0}}<text class="text-sm">单</text></view>
						
					</view>
					
					<view   class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">到店单金额</view>
						<view style="width: 50%;">{{dataCentre.takeMoney?dataCentre.takeMoney:0}}<text class="text-sm">元</text></view>
						
					</view>
					
					<view   class="margin-top marginlr" style="display: flex;font-size:32upx;">
						<view style="width: 50%;">到店订单数</view>
						<view style="width: 50%;">{{dataCentre.takeCount?dataCentre.takeCount:0}}<text class="text-sm">单</text></view>
						
					</view>

				</view>
			</view>
		</view>
		<view class="page-box" v-if="orderList.length == 0" >
			<view class="centre">
				<image src="../static/images/empty.png" mode=""></image>
				<view class="tips">暂无内容</view>
			</view>
		</view>
		<view style="margin: 10upx;">
			<view class="boxs" v-for="(item,index) in orderList" :key='index' >
				<view class="flex justify-between align-center padding">
					<view class="textRed">已完成</view>
					<view style="color:#999999;">{{item.payTime}}</view>
				</view>
				<view class="line"></view>
				<view class="padding">
					<view style="color:#333333;" class="text-lg text-bold">{{item.orderGoodsList[0].goodsName}}</view>
					<view style="color:#333333;" class="padding-tb-sm"><text
							style="color:#999999;">规格：</text>{{item.orderGoodsList[0].skuMessage}}</view>
					<view style="color:#333333;"><text style="color:#999999;">取餐号：</text>{{item.orderCode}}</view>
				</view>
				<view class="line"></view>
				<view class="flex justify-between padding">
					<view style="color:#333333;" class="text-lg">
						<text style="color:#999999;" class="text-df">实收:</text>￥{{item.payMoney}}
					</view>
					<view class="look" @click="getDet(item)" >查看详情</view>
				</view>
			</view>
		</view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				dataCentre: {},
				page: 1,
				limit: 10,
				orderList: [],
				shopId: '',
				totalCount: 0
			}
		},
		onLoad(option) {
			this.shopId = option.shopId
			this.getDataCentre()
			this.getOrderList()
		},
		methods: {
			getDet(item) {
				uni.navigateTo({
					url: '/shop/orderDet?orderNumber=' + item.orderNumber
				})
			},
			// 收入
			getDataCentre() {
				this.$Request.getA("/admin/goodsShop/selectStoreData?shopId=" + this.shopId).then(res => {
					if (res.code == 0) {
						this.dataCentre = res.hashMap
					}
				});
			},
			getOrderList() {
				let data = {
					page: this.page,
					limit: this.limit,
					status: 4,
					shopId: this.shopId
				}
				this.$Request.getA("/admin/order/selectAllOrder",data).then(res => {
					if (res.code == 0) {
						if(this.page == 1) {
							this.orderList = res.data.list
						} else {
							this.orderList = [...this.orderList, ...res.data.list]
						}
						this.totalCount = res.data.totalCount
					}
					uni.stopPullDownRefresh();
					uni.hideLoading()
				});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getOrderList();
			if(this.totalCount == this.orderList.length) {
				uni.showToast({
					title: '已经到底了~',
					icon: 'none'
				})
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getOrderList();
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background: #EDF1F7;
	}

	.bg {
		background: #FFFFFF;
	}

	.box {
		width: 686upx;
		// height: 329upx;
		background: linear-gradient(90deg, #D0AA72 0%, #DBB984 100%);
		border-radius: 16upx;
		margin: 0 auto;
		color: #ffffff;
	}

	.lins {
		width: 1upx;
		height: 60upx;
		border: 1upx solid #FFFFFF;
	}

	.boxs {
		// width: 686upx;
		/* height: 363upx; */
		
		background: #FFFFFF;
		border-radius: 20upx;
		margin: 0 auto;
		margin-bottom: 20rpx;
	}

	.textRed {
		font-size: 30upx;
		font-family: PingFang SC;
		font-weight: 500;
		color: #D80204;
	}

	.line {
		width: 686upx;
		height: 1upx;
		border: 1upx solid #E6E6E6;
	}

	.look {
		width: 150upx;
		height: 50upx;
		background: #FF6D9D;
		border-radius: 25upx;
		line-height: 50upx;
		text-align: center;
		font-size: 24upx;
		color: #ffffff;
	}
	.page-box {
		position: relative;
		// left: 0;
		height: 50vh;
		z-index: 0;
		top: 70px;
	}
	
	.centre {
		position: absolute;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		margin: auto;
		height: 400rpx;
		text-align: center;
		// padding: 200rpx auto;
		font-size: 32rpx;
	
		image {
			width: 387rpx;
			height: 341rpx;
			// margin-bottom: 20rpx;
			margin: 0 auto 20rpx;
			// border: 1px dotted #000000;
		}
	
		.tips {
			font-size: 32rpx;
			color: #2F3044;
			margin-top: 20rpx;
			font-weight: 700;
		}
	
		.btn {
			margin: 80rpx auto;
			width: 600rpx;
			border-radius: 32rpx;
			line-height: 90rpx;
			color: #ffffff;
			font-size: 34rpx;
			background: #5074FF;
		}
	}
</style>
