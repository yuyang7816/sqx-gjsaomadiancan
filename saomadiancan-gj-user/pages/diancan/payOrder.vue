<template>
	<view class="pages">
		<!-- 地址 -->
		<view v-if="orderType == 2">
			<view class="goods_address" v-if="JSON.stringify(address) == '{}'" @click="goAddress">
				<view class="address_box">
					<view class="address_name">{{$t('payorder.plaxzadd')}}</view>
					<view class="address_image">
						<image src="../../static/images/index/right.png"></image>
					</view>
				</view>
			</view>
			<view class="flex padding-lr margin-top bg-white radius" style="width: 94%;margin: 20rpx auto 0;" v-else
				@click="goAddress">
				<view class="flex-sub padding-tb-sm">
					<view class="text-lg text-bold text-black">
						<text>{{address.userName}}</text>
						<text class="margin-left-sm">{{address.userPhone}}</text>
					</view>
					<view class="text-df margin-top-xs" style="color: #999999;">
						{{address.address}} {{address.addressDetail}}
					</view>
				</view>
				<view class="address_image">
					<image src="../../static/images/index/right.png"></image>
				</view>
			</view>
		</view>
		<!-- 商品 -->
		<view class="food">
			<view class="tosend_header_food" v-for="(item,index) in dataList.orderGoodsList" :key='index'>
				<view class="tosend_header_food_le">
					<image :src="item.goodsPicture[0]" style="border-radius: 10rpx;" mode=""></image>
				</view>
				<view class="tosend_header_food_ce">
					<view class="tosend_header_food_text">{{item.goodsName}}</view>
					<view class="tosend_header_food_text2">{{item.skuMessage}}</view>
				</view>
				<view class="tosend_header_food_ri">
					<view class="tosend_header_food_text"><text>{{$t('index.qian')}}</text>{{item.goodsPrice}}</view>
					<view class="tosend_header_food_text2">X{{item.goodsNum}}</view>
				</view>
			</view>
			<view class="tosend_header_do do_top">
				<view>{{$t('payorder.dbf')}}</view>
				<view class="tosend_header_do_ri">{{$t('index.qian')}}{{dabaoMoney}}</view>
			</view>
			<view class="tosend_header_do" v-if="paotuiMoney>0&&orderType==2">
				<view>{{$t('payorder.ptf')}}</view>
				<view class="tosend_header_do_ri">{{$t('index.qian')}}{{paotuiMoney}}</view>
			</view>
			<view class="tosend_header_do" v-if="member == 1">
				<view>{{$t('payorder.vipzk')}}</view>
				<view class="tosend_header_do_ri">{{vipZheKou * 10}}{{$t('payorder.zhe')}}</view>
			</view>
			<view class=" flex align-center justify-between padding-top-sm" @click="show=true">
				<view class="flex-sub">{{$t('yjq.title')}}</view>
				<view class="tosend_header_do_ri" v-if="coupon">- {{$t('index.qian')}}{{coupon.money}}</view>
				<u-icon name="arrow-right" size="24" v-else></u-icon>
			</view>
			<view class="tosend_header_do">
				<view class="tosend_header_do_le2">{{$t('payorder.hj')}}</view>
				<view class="tosend_header_do_ri2">
					<text>{{$t('index.qian')}}</text>{{totalPrice1}}
				</view>
			</view>
		</view>
		<view class="radius bg-white" style="width: 94%;margin: 0 auto;border-radius: 18rpx;padding: 30rpx;">
			<view class="text-lg text-bold text-bold text-black">{{$t('payorder.bz')}}</view>
			<view style="width: 100%;background-color: #f1f1f1;padding: 10rpx;margin-top: 20rpx;border-radius: 12rpx;">
				<u-input v-model="orderRemarks" type="textarea" height="200" maxlength="-1" :border="false"
					:placeholder="$t('payorder.plabz')" style="border-radius: 12rpx;" />
			</view>
		</view>
		<!-- 支付方式 -->
		<view class="margin-top padding-lr radius bg-white" style="width: 94%;margin: 20rpx auto;border-radius: 18rpx;">
			<view class="padding-tb-sm text-lg text-bold text-bold text-black">{{$t('purse.paytype')}}</view>
			<view class="flex align-center justify-between padding-tb" style="height: 100upx;"
				v-for="(item,index) in openList" :key='index'>
				<image :src="item.image" style="width: 55rpx;height: 55rpx;border-radius: 50upx;"></image>
				<view class="flex-sub text-lg margin-left">{{item.text}}</view>
				<radio-group name="openWay" style="margin-left: 20upx;" @change='selectWay(item)'>
					<label class="tui-radio">
						<radio :checked="openWay === item.id ? true : false" color="#FF6D9E" />
					</label>
				</radio-group>
			</view>

		</view>
		<!-- 结算 -->
		<view class="goorder">
			<view class="goorder_but" @click="pay">{{$t('purse.ljjs')}}</view>
		</view>

		<u-popup v-model="show" mode="center" :closeable="true" close-icon-pos="top-right" close-icon="close-circle"
			close-icon-size="50" border-radius="20" width="80%" @close="show = false">
			<view class="padding bg-gray">
				<view class="text-center text-lg text-bold margin-bottom-sm">{{$t('yjq.title')}}</view>
				<view class="flex justify-between align-center radius margin-tb-sm padding-sm bg-white"
					v-for="(item,index) in couponList" :key='index' @click="selCoupon(item)">
					<view>
						<view>{{item.couponName}}</view>
						<view>{{$t('yjq.yxq')}}{{item.expirationTime}}</view>
					</view>
					<view class="text-sm text-bold">{{$t('index.qian')}}<text class="text-lg">{{item.money}}</text>
					</view>
				</view>
				<view v-if="couponList.length==0"
					style="height: 100rpx;line-height: 100rpx;text-align: center;font-weight: 700;">{{$t('yhq.empoty')}}
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderRemarks: '',
				vipZheKou: '',
				storeCommodityId: 0,
				openList: [],
				member: 0,
				openWay: 1,
				page: 1,
				limit: 100,
				dataList: [],
				packMoney: 0, //打包费
				totalPrice: 0,
				totalPrice1: 0,
				address: {}, //地址
				addressId: '',
				parentId: '',
				orderId: '',
				show: false,
				coupon: '',
				couponList: [],
				orderType: 1,
				paotuiMoney: 0,
				dabaoMoney: 0,
				errandMoney: 0
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title: this.$t('payorder.titlename')
			})
			this.member = this.$queue.getData('member') ? this.$queue.getData('member') : 0;
			this.vipZheKou = this.$queue.getData('vipZheKou');
			this.orderId = option.orderId
			this.orderType = option.orderType
			this.getPaotui()
			this.getAddressList()
			uni.showLoading({
				title: this.$t('loadingText.jzz') //'加载中...'
			})
			this.getOrderList()
			let ttPaySel = this.$queue.getData('ttPaySel')
			let ttSripe = this.$queue.getData('ttSripe')
			let openType = {}

			if (ttPaySel != '否') {
				openType = {
					image: '/static/pay/pay.png',
					text: this.$t('pay.gjzp'),
					id: 1
				}
				this.openList.push(openType)
				this.openWay = 1
			}
			if (ttSripe != '否') {
				openType = {
					image: '/static/pay/sct.png',
					text: this.$t('pay.str'),
					id: 2
				}
				this.openList.push(openType)
				this.openWay = 2
			}

			openType = {
				image: '/static/images/diancan/jinbi.png',
				text: this.$t('pay.lingqian'),
				id: 3
			}
			this.openList.push(openType)
			this.openWay = 3
		},
		onShow() {

			if (this.addressId) {
				this.getAddressDet(this.addressId)
			}

		},
		methods: {
			getPaotui() {
				this.$Request.get("/common/type/123").then(res => {
					if (res.code == 0) {
						this.paotuiMoney = Number(res.data.value)
					}
				});
				this.$Request.get("/common/type/124").then(res => {
					if (res.code == 0) {
						this.errandMoney = Number(res.data.value)
					}
				});
			},
			// 删除优惠券
			detCoupon() {
				let data = {
					orderId: this.dataList.orderId,
				}
				this.$Request.post("/app/order/deleteCouponByOrderId", data).then(res => {
					if (res.code == 0) {

					}
				});
			},
			truncateDecimal(num, digits = 2) {
				const factor = Math.pow(10, digits);
				return Math.floor(num * factor) / factor;
			},
			// 选择优惠券
			selCoupon(item) {
				this.coupon = item
				if (this.orderType == 2) {
					this.totalPrice1 = this.truncateDecimal(this.totalPrice + this.paotuiMoney + this.dabaoMoney)
				} else {
					this.totalPrice1 = this.truncateDecimal(this.totalPrice + this.dabaoMoney)
				}
				let totalMoney = this.truncateDecimal(this.totalPrice1 - (this.coupon.money ? this.coupon.money : 0))
				if (totalMoney <= 0) {
					this.totalPrice1 = 0.01
				} else {
					this.totalPrice1 = totalMoney
				}
				if (this.member == 1) {
					this.totalPrice1 = this.truncateDecimal(parseFloat(this.totalPrice1) * parseFloat(this.vipZheKou))
				}
				let data = {
					parentId: this.dataList.parentId,
					couponId: this.coupon ? this.coupon.id : ''
				}
				this.$Request.post("/app/order/employCoupon", data).then(res => {
					if (res.code == 0) {
						this.show = false
					}
				});
			},
			// 获取优惠券列表
			getCouponList() {
				let data = {
					page: 1,
					limit: 10,
					minMoney: this.orderType == 2 ? (this.totalPrice * 1).toFixed(2) * 1 + this.paotuiMoney * 1 + this
						.dabaoMoney * 1 : (this.totalPrice * 1).toFixed(2)
				}
				this.$Request.get("/app/coupon/selectUserCouponList", data).then(res => {
					if (res.code == 0) {
						this.couponList = res.data.list
					}
				});
			},
			// 商品列表
			getOrderList() {
				let data = {
					orderId: this.orderId,
				}
				this.$Request.get("/app/order/selectBuyGoods", data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.dataList = res.data
						
						if (this.dataList) {
							this.dataList.orderGoodsList.forEach(res => {
								res.goodsPicture = res.goodsPicture.split(',')
								this.dabaoMoney += res.goodsPack * res.goodsNum
							})
						}
						this.totalPrice = res.data.payMoney * 1
						
						if (this.orderType == 2) {
							if (this.totalPrice >= this.errandMoney) {
								this.paotuiMoney = 0
							}
							this.totalPrice1 = this.truncateDecimal(this.totalPrice + this.paotuiMoney + this
								.dabaoMoney)
						} else {
							this.totalPrice1 = this.truncateDecimal(this.totalPrice + this.dabaoMoney)
						}
						if (this.member == 1) {
							this.totalPrice1 = this.truncateDecimal(parseFloat(this.totalPrice1) * parseFloat(this
								.vipZheKou))
						}
						this.getCouponList()
						this.detCoupon()
					}
				});
			},
			selectWay: function(item) {
				this.openWay = item.id;
			},
			pay() {
				if (this.orderType == 2 && !this.address.addressId) {
					uni.showToast({
						title: this.$t('payorder.plaxzadd'), //'请选择收货地址',
						icon: 'none'
					})
					return
				}
				if (this.openWay == 1) {
					let that = this
					let payWay = ''
					// #ifdef H5
					payWay = 1
					// #endif
					// #ifdef APP
					payWay = 2
					// #endif
					let data = {
						businessType:1,
						redirectUri: '/pages/order/index',
						amount: that.totalPrice1,
						outTraceNo: that.dataList.parentId,
						payChannel: payWay,
						clientId: 19,
						couponId: that.coupon ? that.coupon.id : '',
						orderType: that.orderType,
					}
					that.$Request.postJson('/app/paypal/pay', data).then(
						red => {
							uni.hideLoading();
							if (red.code == 0) {
								// #ifdef H5
								uni.hideLoading();
								window.location.href = red.msg; //H5端跳转到paypal支付页面
								// #endif
								// #ifdef APP-PLUS
								let clientId = uni.getStorageSync('paypalClientId')
								let orderInfo = {
									"clientId": clientId, //开发者中心clientId
									"orderId": red.msg, //订单id
									"userAction": "continue", //按钮样式 paynow/continue
									"currency": "USD", //币种
									"environment": "sandbox" //运行环境 sandbox(沙盒) / live(线上)
								};
								uni.getProvider({
									service: 'payment',
									success: function(res) {
										uni.requestPayment({
											"provider": "paypal",
											"orderInfo": orderInfo,
											success: function(response) {
												uni.showLoading({
													title: that.$t(
														'index.jiazai'
													)
												})
												that.$Request.getT(
													'/app/payPal/checkPay?payWay=1&token=' +
													red.msg).then(ret => {
													console.log(ret)
													if (ret === 'ok') {
														uni
															.hideLoading();
														setTimeout(function() {
															that.getUserInfo();
														}, 1000)
													} else {
														uni
															.hideLoading();
														that.$queue
															.showToast(
																that
																.$t(
																	'cz.payerr'
																))
													}
												});
											},
											fail: function(err) {
												console.log('fail:' + JSON
													.stringify(
														err));
												that.$queue.showToast(that.$t(
													'cz.payerr'));
											}
										});
									},
									fail(e) {
										console.log(e)
									}
								});
								// #endif
							}
						});
			
			
				} else if (this.openWay == 2) {
					let that = this
					let payWay = ''
					// #ifdef H5
					payWay = 1
					// #endif
					// #ifdef APP
					payWay = 2
					// #endif
					let publishKey = uni.getStorageSync('publishKey');
					let data = {
						businessType:1,
						redirectUri: '/pages/order/index',
						amount: that.totalPrice1,
						outTraceNo: that.dataList.parentId,
						payChannel: payWay,
						clientId: 19,
						orderType: that.orderType,
						couponId: that.coupon ? that.coupon.id : '',
					}
					that.$Request.postJson('/app/stripe/pay', data)
						.then(
							red => {
								uni.hideLoading();
								if (red.code == 0) {
									// #ifdef H5
									uni.hideLoading();
									window.location.href = red.msg; //H5端跳转到stripe支付页面
									// #endif
									// #ifdef APP-PLUS
									//订单对象
									var orderInfo = {
										"customer": "", //Customer
										"ephemeralKey": "", //临时访问Customer的Key
										"isAllowDelay": true, //是否支持延迟支付  默认false
										"merchantName": "DCloud", //商户名
										"paymentIntent": "" + red.msg, //订单信息
										"publishKey": "" + publishKey, //公钥
										"billingDetails": { //账单信息(可选)
											"name": "",
											"email": "",
											"phone": "",
											"address": {
												"city": "",
												"country": "CN", //国家代码(ISO 3166-1 alpha-2)
												"line1": "",
												"line2": "",
												"postalCode": "",
												"state": ""
											}
										}
									};
			
									//发起支付
									uni.getProvider({
										service: 'payment',
										success: function(res) {
											uni.requestPayment({
												"provider": "stripe",
												"orderInfo": orderInfo,
												success(res) {
													uni.showLoading({
														title: that.$t(
															'index.jiazai')
													})
													uni.hideLoading();
													setTimeout(function() {
														that.getUserInfo();
													}, 1000)
												},
												fail: function(err) {
													console.log('fail:' + JSON
														.stringify(
															err));
													that.$queue.showToast(that.$t(
														'cz.payerr'));
												}
											});
										},
										fail: function(err) {
											console.log('fail:' + JSON.stringify(
												err));
										}
									});
									// #endif
			
								} else {
									console.log('fail:' + JSON.stringify(red));
									that.$queue.showToast(red.msg)
								}
							});
				}else if (this.openWay == 3){
					let data = {
						parentId: this.dataList.parentId,
						couponId: this.coupon ? this.coupon.id : '',
						addressId: this.address.addressId ? this.address.addressId : '',
						orderType: this.orderType,
						orderRemarks: this.orderRemarks,
					}
					this.$Request.post("/app/wxPay/balanceOrder", data).then(res => {
						uni.hideLoading()
						if (res.code == 0) {
							uni.showToast({
								title: this.$t('purse.payok'),//'支付成功',
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
			// 立即结算
			toSettlement() {
				if (this.orderType == 2 && !this.address.addressId) {
					uni.showToast({
						title: '请选择收货地址',
						icon: 'none'
					})
					return
				}
				uni.showLoading({
					title: '支付中...'
				})
				let data = {
					parentId: this.dataList.parentId,
					couponId: this.coupon ? this.coupon.id : '',
					addressId: this.address.addressId ? this.address.addressId : '',
					orderType: this.orderType,
					orderRemarks: this.orderRemarks,
				}
				if (this.openWay == 1) {
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
											url: '/pages/order/index'
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
				} else {
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
			// 生成订单
			getOrder(data) {
				this.$Request.postJson("/app/order/insertOrder", data).then(res => {
					// this.goodsList = res.data.listsDetail;
					if (res.msg == "success") {
						uni.navigateTo({
							url: '/pages/confirmorder/payment?orderId=' + res.data.orderId
						})
					}
				});
			},
			// 获取选择的地址
			getAddressDet(e) {
				this.$Request.get("/app/address/selectAddressById?addressId=" + e).then(res => {
					if (res.code == 0) {
						this.address = res.data
					}
				});
			},
			// 获取地址列表
			getAddressList() {
				let that = this
				let data = {
					page: 1,
					limit: 100
				}
				that.$Request.get("/app/address/selectAddressList", data).then(res => {
					if (res.code == 0) {
						that.address = res.data.list[0] ? res.data.list[0] : {}
						res.data.list.forEach(ret => {
							if (ret.addressDefault == 1) {
								that.address = ret
							}
						})
						console.log(that.address)
					}
				});
			},
			goAddress() {
				uni.navigateTo({
					url: '/my/address/index?add=1'
				})
			}
		}
	}
</script>

<style scoped>
	.pages {
		padding-bottom: 200rpx;
	}

	.goods_address {
		width: 94%;
		background: #FFFFFF;
		height: 120rpx;
		margin: 20rpx auto 0;
		border-radius: 18rpx;
	}

	.address_box {
		width: 90%;
		margin: 0 auto;
		height: 120rpx;
		display: flex;
		line-height: 120rpx;
	}

	.address_name {
		flex: 1;
		font-size: 30rpx;
	}

	.address_image {
		/* flex: 1; */
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.address_image image {
		width: 25rpx;
		height: 40rpx;
	}


	/* 地址 */
	.address {
		width: 88%;
		margin: 3% auto;
		padding: 3%;
		display: flex;
		background-color: #FFFFFF;
		border-radius: 18rpx;
	}

	.adderss_le {
		width: 90%;
		line-height: 1.5;
	}

	.adderss_le_text {
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;
	}

	.adderss_le_text2 {
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		display: flex;
	}

	.adderss_le_text2 view {
		margin-left: 2%;
	}

	.adderss_le_text2 view:first-child {
		margin-left: 0;
	}

	.adderss_ri {
		width: 10%;
		text-align: right;
		line-height: 3;
	}

	.adderss_ri image {
		width: 18rpx;
		height: 30rpx;
	}

	/* 商品 */
	.food {
		width: 94%;
		margin: 3% auto;
		padding: 3%;
		background-color: #FFFFFF;
		border-radius: 18rpx;
	}

	.tosend_header_food {
		width: 100%;
		display: flex;
		margin-top: 3%;
	}

	.tosend_header_food_le {
		width: 15%;
	}

	.tosend_header_food_le image {
		width: 110rpx;
		height: 110rpx;
	}

	.tosend_header_food_ce {
		margin: 0 0 0 4%;
		width: 57%;
	}

	.tosend_header_food_ri {
		text-align: right;
		width: 25%;
	}

	.tosend_header_food_text {
		font-size: 34rpx;
		font-weight: 500;
		color: #333333;
		line-height: 1.8;
	}

	.tosend_header_food_text text {
		font-size: 25rpx;
	}

	.tosend_header_food_text2 {
		font-size: 30rpx;
		font-weight: 500;
		color: #999999;
	}

	.do_top {
		padding-top: 3%;
	}

	.do_bot {
		padding-bottom: 3%;
		border-bottom: 2rpx solid #E6E6E6;
	}

	.tosend_header_do {
		width: 100%;
		color: #333333;
		font-size: 30rpx;
		display: flex;
		margin-top: 20rpx;
		line-height: 1.5;
	}

	.tosend_header_do view {
		flex: 1;
	}

	.tosend_header_do_ri {
		text-align: right;
	}

	.tosend_header_do_le2 {
		font-family: PingFang SC;
		font-weight: 500;
		color: #999999;
		padding-top: 1.5%;
	}

	.tosend_header_do_ri2 {
		text-align: right;
		color: #EA0000;
		font-size: 40rpx;
	}

	.tosend_header_do_ri2 text {
		font-size: 30rpx;
	}

	/* 支付 */
	/* .payment_all{
		width: 88%;margin: 3% auto;padding: 3%;background-color: #FFFFFF;
		border-radius: 18rpx;
	}
	.payment{
		width: 100%;display: flex;margin-top: 3%;
	}
	.payment_le{
		width: 13%;
	}
	.payment_le image{
		width: 60rpx;height: 60rpx;
	}
	.payment_ce{
		width: 52%;
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;line-height: 60rpx;
	}
	.payment_ri{
		width: 35%;text-align: right;line-height: 60rpx;
	} */
	/* 结算 */
	.goorder {
		width: 100%;
		padding: 2% 3%;
		position: fixed;
		bottom: 0;
		background-color: #FFFFFF;
		z-index: 9999;
	}

	.goorder_but {
		width: 100%;
		line-height: 3.5;
		text-align: center;
		background: #FF6D9E;
		color: #FFFFFF;
		border-radius: 36rpx;
		font-weight: 700;
	}
</style>