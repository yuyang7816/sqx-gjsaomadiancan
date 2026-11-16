<template>
	<view style="padding-bottom: 140rpx;">
		<view class="list flex justify-center">
			<view class="list-info">
				<view class="list-info-item" style="border: none !important;font-size: 36rpx;">
					{{$t('meal.yxdcp')}}
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
										<text>{{$t('index.qian')}}{{item.goodsPrice}}/{{$t('meal.jian')}}</text>
										<text style="margin-left: 10rpx;">共{{item.goodsNum}}{{$t('meal.jian')}}</text>
									</view>
									<view>
										x {{item.goodsNum}}
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
				<!-- <view @click="jiezhang()"
					class="addMeal-box-right addMeal-box-rights addMeal-boxs flex justify-center align-center">
					{{$t('meal.jiezhang')}}
				</view> -->

			</view>
		</view>

		<!-- 结账付款弹窗 -->
		<u-popup v-model="propPrice" mode="bottom" border-radius="14" height="500" :closeable="true">
			<view class="order-title">订单支付</view>
			<view class="order-title" style="font-size: 30rpx;font-weight: 500;">¥:{{goodsPrice}}</view>
			<u-radio-group v-model="valuepay" active-color="#FF6D9D">
				<view class="order-pay flex justify-between align-center" style="padding: 0 40rpx 0 40rpx;">
					<view class="order-pay-left flex align-center">
						<image style="width: 70rpx;height: 70rpx;border-radius: 50%;margin-right: 20rpx;"
							src="../../static/images/diancan/icon_weixin.png" mode=""></image>
						微信支付
					</view>
					<view class="order-pay-right">
						<u-radio name="微信">
						</u-radio>
					</view>
				</view>
				<view class="order-pay flex justify-between align-center" style="padding: 0 40rpx 0 40rpx;">
					<view class="order-pay-left flex align-center">
						<image style="width: 70rpx;height: 70rpx;border-radius: 50%;margin-right: 20rpx;"
							src="../../static/images/diancan/jinbi.png" mode=""></image>
						余额支付
					</view>
					<view class="order-pay-right">
						<u-radio name="余额">
						</u-radio>
					</view>
				</view>
			</u-radio-group>
			<view class="submit flex justify-center align-center" @click="submit()">
				确认支付
			</view>

		</u-popup>
	</view>
</template>

<script>
	import config from '../../common/config';
	export default {
		data() {
			return {
				valuepay: '微信',
				propPrice: false,
				list: [],
				list2: [],
				value: 0,
				shopId: 1,
				goodsPrice: 0,
				orderId: '',
				seatId: 2,
			};
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('meal.title')
			})
			//微信扫码进来携带的参数（店铺的id、桌号的id）
			if (option.q && option.q != "undefined") {
				// 获取到二维码原始链接内容
				const qrUrl = decodeURIComponent(option.q)
				//此处就是我们要获取的参数 json，通过方法解析
				let jsonUrl = this.GetwxUrlParam(qrUrl);
				//比如我要得到id的值，直接取值即可
				this.shopId = jsonUrl.shopId;
				this.seatId = jsonUrl.seatId
				this.getOrderInfo()
			}
			if (option.u && option.u != "undefined") {
				//此处就是我们要获取的参数 json，通过方法解析
				const qrUrl = decodeURIComponent(option.u)
				let jsonUrl = this.GetwxUrlParam(qrUrl);
				//比如我要得到id的值，直接取值即可
				this.shopId = jsonUrl.shopId;
				this.seatId = jsonUrl.seatId
				this.getOrderInfo()
			}

			if (option.shopId) {
				this.shopId = option.shopId;
				this.seatId = option.seatId
				this.getOrderInfo()
			}
			// if (uni.getStorageSync('token')) {
			// 	this.token = uni.getStorageSync('token')
			// } else {
			// 	uni.navigateTo({
			// 		url: '/pages/login/login'
			// 	})
			// }
		},
		onShow() {
			if (this.shopId && this.seatId) {
				this.getOrderInfo()
			}
		},
		methods: {
			//订单支付
			submit() {
				uni.showLoading({
					title: '支付中...'
				})
				console.log(this.valuepay);
				let data = {
					parentId: this.orderId,
					couponId: '',
					addressId: '',
					orderType: 3
				}
				if (this.valuepay == '微信') { //微信小程序支付
					this.$Request.post("/app/wxPay/wxPayJsApiOrder", data).then(res => {
						uni.hideLoading()
						if (res.code == 0) {
							uni.requestPayment({
								provider: 'wxpay',
								timeStamp: res.data.timestamp,
								nonceStr: res.data.noncestr,
								package: res.data.package,
								signType: res.data.signType,
								paySign: res.data.sign,
								success: function(suc) {
									uni.showToast({
										title: '支付成功',
										icon: 'success'
									})
									setTimeout(function() {
										uni.switchTab({
											url: '/pages/index/index'
										})
									}, 1000)
								},
								fail: function(err) {
									console.log('fail:' + JSON.stringify(err));
									uni.showToast({
										title: '支付失败',
										icon: 'none'
									})
								}
							});
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
				} else { //余额支付
					this.$Request.post("/app/wxPay/balanceOrder", data).then(res => {
						uni.hideLoading()
						if (res.code == 0) {
							uni.showToast({
								title: '支付成功',
								icon: 'success'
							})
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/order/index'
								})
							}, 1000)
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					});
				}
			},
			//结账弹窗
			jiezhang() {
				if (this.list.length == 0) {
					uni.showToast({
						title: this.$t('meal.platit'),//'还没有点餐，无需结账',
						icon: 'none'
					})
					return
				}
				if (uni.getStorageSync('token')) {
					this.token = uni.getStorageSync('token')
					this.propPrice = true
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					})
				}

			},
			//解析链接地址
			GetwxUrlParam(url) {
				let theRequest = {};
				if (url.indexOf("#") != -1) {
					const str = url.split("#")[1];
					const strs = str.split("&");
					for (let i = 0; i < strs.length; i++) {
						theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
					}
				} else if (url.indexOf("?") != -1) {
					const str = url.split("?")[1];
					const strs = str.split("&");
					for (let i = 0; i < strs.length; i++) {
						theRequest[strs[i].split("=")[0]] = decodeURI(strs[i].split("=")[1]);
					}
				}
				return theRequest;
			},
			//完成订单
			completeOrder() {
				let data = {
					orderId: this.orderId
				}
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('meal.plawcdd'),
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					complete(ret) {
						if (ret.confirm) {
							that.$Request.postT('/app/order/adminCompleteOrder', data).then(res => {
								if (res.code == 0) {
									uni.showToast({
										title: that.$t('order.ywc')//'已完成'
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
						}
					}
				})
			},
			//增加餐品数量
			pluss(info) {
				let data = {
					orderGoodsId: info.id,
					type: 1, //1:加 2:减
					num: 1,
					shopId: this.shopId
				}
				this.$Request.getT('/app/order/updateGoodsNum', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('index.addok')//'添加成功'
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
				this.$Request.getT('/app/order/updateGoodsNum', data).then(res => {
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
				this.$Request.getT('/app/order/getSeatOrder', data).then(res => {
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
					url: '/my/meals/addMeal?seatId=' + this.seatId + '&shopId=' + this.shopId
				})

			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #EDF1F7;
	}

	.submit {
		width: 686rpx;
		height: 70rpx;
		border-radius: 40rpx;
		background-color: #FF6D9D;
		color: #ffffff;
		margin: 0 auto;
		margin-top: 40rpx;
	}

	::v-deep .u-radio {
		display: block !important;
	}

	.order-pay {
		margin-top: 40rpx;
	}

	.order-title {
		width: 100%;
		text-align: center;
		font-weight: bold;
		color: #000;
		margin-top: 30rpx;
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