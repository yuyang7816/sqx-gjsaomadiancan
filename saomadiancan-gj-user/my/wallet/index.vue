<template>
	<view class="pages">
		<!-- 余额 -->
		<view class="wallet">
			<view class="wallet_header">
				<view class="wallet_header_text">{{$t('purse.ye')}}</view>
				<view class="wallet_header_xin">
					<view class="wallet_header_le">{{myMoney}} <text>{{$t('index.yuan')}}</text></view>
				</view>
			</view>
		</view>
		<!-- 充值 -->
		<view class="wallet_topup">
			<view class="wallet_header_text">{{$t('purse.czye')}}</view>
			<view class="wallet_header_xin2">
				<view class="wallet_header_xin2_sty" v-for="(item,index) in wallet" :key='index'
					:class="{topup:item.isSelect}" @click="active(item)">
					<view class="topup_img" v-if="item.isSelect">
						<image src="../static/wallet/face.png" mode=""></image>
					</view>
					{{$t('purse.top-up')}}<text>{{item.money}}</text>{{$t('index.yuan')}}
				</view>
			</view>
		</view>
		<!-- 消费 -->
		<view class="wallet_header_consum" @click="record()">
			<view class="wallet_header_consum_le">{{$t('purse.qbmx')}}</view>
			<view class="wallet_header_consum_ri">
				<image src="../static/wallet/right1.png" mode="scaleToFill"></image>
			</view>
		</view>
		<!-- 立即充值 -->
		<view class="wallet_bottom" @click="openPay()">{{$t('purse.cz')}}</view>
		
		
		<u-popup v-model="show" mode="bottom" border-radius="24">
			<view class="padding">
				<view class="flex align-center justify-between -lr margin-bottom-sm" v-for="(item,index) in openList"
					:key='index' style="border-radius: 24rpx;height: 100rpx;">
					<image :src="item.image" style="width: 56rpx;height: 56rpx;border-radius: 50upx;"></image>
					<view style="font-size: 30upx;color: #000000;width: 73%;">{{item.text}}
					</view>
					<radio-group name="openWay" style="margin-left: 20upx;" @tap='selectWay(item)'>
						<label class="tui-radio">
							<radio color="#FE6D70" :checked="openWay === item.id ? true : false"
								style="transform:scale(0.9)" />
						</label>
					</radio-group>
				</view>
		
				<view class="popupbox_btn" @click="pay">{{$t('pay.surepay')}}</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				myMoney: 0,
				wallet: [],
				thisSelect: {},
				show: false,
				openList: [],
				openWay: 1,
			}
		},
		onShow() {
			uni.setNavigationBarTitle({
				title: this.$t('purse.title')
			})
			
			let ttPaySel = this.$queue.getData('ttPaySel')
			let ttSripe = this.$queue.getData('ttSripe')
			let openType = {}
			this.openList = []
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
			
			this.deploy()
			this.getMoneyNum()

		},
		methods: {
			deploy() {
				this.$Request.get("/app/topupmoney/selectTopUpMoney").then(res => {
					if (res.code == 0) {
						res.data.forEach(res => {
							res.isSelect = false
						})
						this.wallet = res.data
					}
				});
			},
			active(e) {
				this.wallet.forEach(res => {
					if (res.id == e.id) {
						res.isSelect = true
						this.thisSelect = e
					} else {
						res.isSelect = false
					}
				})
			},
			record() {
				uni.navigateTo({
					url: '/my/wallet/walletDet'
				})
			},
			getMoneyNum() {
				this.$Request.get("/app/userMoney/selectMyMoney").then(res => {
					if (res.code == 0) {
						this.myMoney = res.data.money
					}
				});
			},
			openPay() {
				if (!this.thisSelect.money) {
					uni.showToast({
						title: this.$t('purse.plaother'),//'请选择充值金额！',
						icon: 'none'
					})
					return
				}
				this.show = true
			},
			selectWay: function(item) {
				this.openWay = item.id;
			},
		pay() {
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
					businessType: 2,
					redirectUri: '/my/wallet/index',
					amount:that.thisSelect.money,
					vipId:'',
					payChannel: payWay,
					clientId: 19
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
					businessType: 2,
					redirectUri: '/my/wallet/index',
					amount: that.thisSelect.money,
					vipId:'',
					payChannel: payWay,
					clientId: 19
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
			}
		
		},
			payment() {
				let that = this
				that.show = false
				if (!that.thisSelect.money) {
					uni.showToast({
						title: that.$t('purse.plaother'),//'请选择充值金额！',
						icon: 'none'
					})
					return
				}
				// 微信小程序支付
				// #ifdef MP-WEIXIN
				this.$Request.post('/app/wxPay/wxPayJsApiMoney', {
					money: this.thisSelect.money
				}).then(ret => {
					uni.hideLoading()
					uni.requestPayment({
						provider: 'wxpay',
						timeStamp: ret.data.timestamp,
						nonceStr: ret.data.noncestr,
						package: ret.data.package,
						signType: ret.data.signType,
						paySign: ret.data.sign,
						success: function(suc) {
							uni.showToast({
								title: '支付成功',
								icon: 'success'
							})
							// that.change(1)
						},
						fail: function(err) {
							console.log('fail:' + JSON.stringify(err));
							uni.showToast({
								title: '支付失败',
								icon: 'none'
							})
						}
					});
				});
				// #endif

			},
		}
	}
</script>

<style scoped>
	/* 余额 */
	.wallet {
		width: 100%;
		height: 260rpx;
		background: linear-gradient(0deg, rgba(255, 255, 255, 0) 0%, #FF9CBD 100%);
	}

	.wallet_header {
		width: 94%;
		margin: 0 auto;
		background-color: #FFFFFF;
		border-radius: 18rpx;
		position: relative;
		top: 100rpx;
	}

	.wallet_header_text {
		padding: 3% 3% 1%;
		line-height: 32rpx;
		font-size: 30rpx;
		font-weight: 500;
		color: #333333;
		line-height: 32px;
	}

	.wallet_header_xin {
		padding: 2% 3% 5%;
		display: flex;
	}

	.wallet_header_le {
		flex: 2;
		font-size: 68rpx;
		font-weight: 500;
		color: #333333;
	}

	.wallet_header_le text {
		font-size: 34rpx;
	}

	.wallet_header_ri {
		margin-top: 20rpx;
	}

	.wallet_header_ri image {
		width: 40rpx;
		height: 30rpx;
	}

	.wallet_header_ri text {
		font-size: 34rpx;
		font-weight: 500;
		color: #333333;
	}

	/* 充值 */
	.wallet_topup {
		width: 94%;
		margin: 13% auto 0;
		background-color: #FFFFFF;
		border-radius: 18rpx;
		padding: 0 0 3%;
	}

	.wallet_header_xin2 {
		/* padding: 0 3% 2%; */
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		padding: 0 2%;
	}

	.wallet_header_xin2_sty {
		width: 48.5%;
		/* margin: 1% 0 0 3%; */
		border: 2rpx solid #E6E6E6;
		border-radius: 18rpx;
		text-align: center;
		line-height: 3;
		margin-bottom: 2%;
	}

	.wallet_header_xin2_sty text {
		font-size: 38rpx;
	}

	.wallet_header_xin2_sty:first-child {
		margin-left: 0;
	}

	.topup {
		color: #FF130A;
		border: 2rpx solid #FF130A;
		position: relative;
	}

	.topup_img {
		position: absolute;
		bottom: 42rpx;
	}

	.topup_img image {
		width: 48rpx;
		height: 40rpx;
	}

	/* 消费 */
	.wallet_header_consum {
		width: 94%;
		margin: 3% auto;
		background-color: #FFFFFF;
		border-radius: 18rpx;
		padding: 3%;
		display: flex;
	}

	.wallet_header_consum_le {
		width: 97%;
		font-family: PingFang SC;
		font-weight: 500;
		color: #333333;
	}

	.wallet_header_consum_ri {
		line-height: 1.5;
	}

	.wallet_header_consum_ri image {
		width: 16rpx;
		height: 28rpx;
	}

	/* 立即充值 */
	.wallet_bottom {
		width: 94%;
		position: fixed;
		bottom: 3%;
		left: 3%;
		line-height: 2.5;
		background: #FF6D9D;
		text-align: center;
		border-radius: 49rpx;
		font-size: 38rpx;
		font-weight: bold;
		color: #FFFFFF;
	}
	
	
	.popupbox_btn {
		background: #FF6D9D;
		color: #FFFFFF;
		height: 80rpx;
		border-radius: 24rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-top: 40rpx;
		font-size: 30rpx;
	}
</style>