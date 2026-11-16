<template>
	<view style="padding-bottom: 140rpx;">
		<view class="list flex justify-center">
			<view class="list-info">
				<view class="list-info-item" style="border: none !important;font-size: 36rpx;">
					{{$t('meal.dclb')}}
				</view>
				<block v-if="list.length>0">
					<view class="list-info-item" v-for="(item,index) in list" :key="index">
						<!-- 商品信息 -->
						<view class="list-info-item-l flex align-center">
							<image :src="item.goodsPicture?item.goodsPicture.split(',')[0]:'../../static/logo.png'"
								style="width: 150rpx;height: 150rpx;border-radius: 8rpx;margin-right: 20rpx;" mode="">
							</image>
							<view class="list-info-item-ls flex flex-wrap"
								style="width: calc(100% - 170rpx);align-content: space-around;height: 150rpx;">
								<view class="list-info-item-ls-b flex justify-between"
									style="font-size: 32rpx;width: 100%">
									<view class=""
										style="width: 80%;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;-o-text-overflow:ellipsis;">
										{{item.goodsName}}
									</view>
									<view class="" style="font-size: 20rpx;color: #ff4483;">
										{{$t('index.qian')}}
										<text style="font-size: 32rpx;">{{item.goodsPrice * item.goodsNum}}</text>
									</view>
								</view>
								<view class="list-info-item-ls-b flex justify-between"
									style="font-size: 32rpx;width: 100%">
									<view class=""
										style="width: 80%;overflow:hidden;white-space: nowrap;text-overflow: ellipsis;-o-text-overflow:ellipsis;">
										{{item.skuMessage}}
									</view>
								</view>

								<view class="list-info-item-ls-b flex justify-between" style="width: 100%;">
									<view class="" style="color: #cccccc;">
										<text>{{$t('index.qian')}}{{item.goodsPrice}}/件</text>
										<text style="margin-left: 10rpx;">共{{item.goodsNum}}件</text>
									</view>
									<view>
										<u-number-box v-model="list2[index].goodsNum" @minus="minus(list2[index])"
											@plus="plus(list2[index])"></u-number-box>
									</view>
								</view>
							</view>
						</view>
						<view class="" v-if="item.goodRemarks" style="margin-top: 20rpx;">
							{{$t('payorder.bz')}}：{{item.goodRemarks}}
						</view>

					</view>
				</block>

				<view class="list-info-item flex justify-center flex-wrap" style="height: 1000rpx;"
					v-if="list.length==0">
					<image src="@/static/images/empty.png" style="width: 387rpx;height: 341rpx;margin-top: 60rpx;"
						mode=""></image>
					<view class="" style="width: 100%;text-align: center;margin-top: 30rpx;">
						{{$t('empty.content')}}
					</view>
				</view>
			</view>
		</view>

		<!-- 加餐 -->
		<view class="addMeal flex justify-center align-center">
			<view class="addMeal-box flex justify-between align-center">
				<view class="addMeal-box-left flex justify-start align-end">
					{{$t('payorder.hj')}}：{{$t('index.qian')}}<text style="font-size: 38rpx;font-weight: bold;">{{goodsPrice}}</text>
				</view>
				<view class="addMeal-box-right addMeal-boxs flex justify-center align-center" @click="gotoAdd()">
					{{$t('meal.add')}}
				</view>
				<view @click="completeOrder"
					class="addMeal-box-right addMeal-box-rights addMeal-boxs flex justify-center align-center">
					{{$t('meal.jiezhang')}}
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [],
				list2: [],
				value: 0,
				shopId: '',
				goodsPrice: 0,
				orderId: '',
			};
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('meal.title')
			})
			this.shopId = this.$queue.getData("updataShopId")
			this.seatId = option.seatId
			this.getOrderInfo()
		},
		onShow() {
			if (this.shopId && this.seatId) {
				this.getOrderInfo()
			}
		},
		methods: {
			//完成订单
			completeOrder() {
				let data = {
					orderId: this.orderId
				}
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('meal.plawcdd'), //确认后将完成订单，确认吗？
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					complete(ret) {
						if (ret.confirm) {
							if (that.orderId) { //有餐品则走完成订单逻辑
								that.$Request.postA('/admin/order/adminCompleteOrder', data).then(res => {
									if (res.code == 0) {
										uni.showToast({
											title:that.$t('order.ywc')//'已完成'
										})
										setTimeout(() => {
											uni.navigateBack()
										}, 1000)
									} else {
										uni.showToast({
											title: res.msg,
											icon: 'none'
										})
									}
								})
							} else { //没有餐品则直接把桌位状态修改为未使用
								that.$Request.postA('/admin/shopSeat/insertSeat', {
									seatId: that.seatId,
									status: 2,
								}).then(ress => {
									if (ress.code == 0) {
										uni.showToast({
											title:that.$t('order.ywc')//'已完成'
										})
										setTimeout(() => {
											uni.navigateBack()
										}, 1000)
									} else {
										uni.showToast({
											title: ress.msg,
											icon: 'none'
										})
									}
								})
							}

						}
					}
				})
			},
			//增加餐品数量
			plus(info) {
				let data = {
					orderGoodsId: info.id,
					type: 1, //1:加 2:减
					num: 1,
					shopId: this.shopId
				}
				this.$Request.getA('/admin/order/updateGoodsNum', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title:this.$t('index.addok')// '添加成功'
						})
						this.getOrderInfo()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						this.getOrderInfo()
					}
				})
			},
			//减少餐品数量
			minus(info) {
				let data = {
					orderGoodsId: info.id,
					type: 2, //1:加 2:减
					num: 1,
					shopId: this.shopId
				}
				this.$Request.getA('/admin/order/updateGoodsNum', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('add.deteok')//'删除成功'
						})
						this.getOrderInfo()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
						this.getOrderInfo()
					}
				})
			},
			//获取桌码订单
			getOrderInfo() {
				let data = {
					shopId: this.shopId,
					seatId: this.seatId
				}
				this.$Request.getA('/admin/order/getSeatOrder', data).then(res => {
					if (res.code == 0) {
						if (res.data && res.data.orderGoodsList.length > 0) {
							this.list = JSON.parse(JSON.stringify(res.data.orderGoodsList))
							this.list2 = JSON.parse(JSON.stringify(res.data.orderGoodsList))
							this.goodsPrice = res.data.orderMoney
							this.orderId = res.data.orderId
						} else {
							this.list = []
							this.list2 = []
							this.goodsPrice = 0
							this.orderId = ''
						}
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//去点餐
			gotoAdd() {
				uni.navigateTo({
					url: '/pages/meals/addMeal?seatId=' + this.seatId
				})

			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #EDF1F7;
	}

	.list {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.list-info {
			width: 686rpx;
			height: auto;
			background-color: #ffffff;
			border-radius: 24rpx;

			.list-info-item {
				padding: 20rpx 20rpx;
				width: 100%;
				border-bottom: 1rpx solid #EDF1F7;
			}

			.list-info-item-r {
				height: 28rpx;
				margin-right: 10rpx;
			}
		}
	}

	.addMeal {
		width: 100%;
		height: 120rpx;
		position: fixed;
		bottom: 0;
		background: #ffffff;
		z-index: 9999;

		.addMeal-box {
			width: 686rpx;
			height: 80rpx;
			color: #ffffff;

			.addMeal-box-left {
				color: #ff4483;
				padding-left: 20rpx;
			}

			.addMeal-boxs {
				width: 30%;
				height: 100%;
				background-color: $uni-color-syscolor;
				border-radius: 40rpx;
			}

			.addMeal-box-rights {
				background-color: #ff4483;
			}
		}
	}
</style>