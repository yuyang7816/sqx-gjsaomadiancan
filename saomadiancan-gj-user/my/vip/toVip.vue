<template>
	<view>
		<view class="page_view">
			<image src="../static/vip/viptopImage.png" class="top_viewImage"></image>
			<view class="top_viewCenter">
				<view class="top_viewCenterName">{{userName}}</view>
				<view class="top_viewCenterTime" v-if="member!=1">{{$t('vip.tit')}}</view>
				<view class="top_viewCenterTime" v-if="member==1&&endTime">{{$t('vip.dqdata')}}：{{endTime}}</view>
			</view>
			<view class="center_view">
				<view class="center_viewTitle">{{$t('vip.type')}}</view>
				<view class="margin-top  padding-lr">
					<view class="center_ImageView margin-bottom" v-for="(item,index) in MemberList" :key="index">
						<image :src="item.memberImg"></image>
						<view class="margin-left-xs">
							<view class="ImageView_name">{{item.memberName}}</view>
							<view class="ImageView_title">{{item.memberDescribe}}</view>
						</view>
					</view>
				</view>

				<view class="money_view" v-if="member!=1">
					<view class="money_itemSel" v-for="(item,index) in vipList" :key="index"
						:class="tabIndex==index?'active':''" @click="bindselect(item,index)">
						<view class="item_name">{{item.vipName}}</view>
						<view class="item_money">{{item.money}}<text
								style="font-size: 20rpx;">{{$t('index.yuan')}}</text></view>
					</view>
				</view>

				<view class="footer flex align-center" v-if="member!=1">
					<radio-group name="openWay" @tap='isShowAgree()'>
						<label class="tui-radio">
							<radio color="#FE6D70" :checked="showAgree" style="transform:scale(0.5)" />
						</label>
					</radio-group>
					<view class="margin-left-xs flex-sub flex flex-wrap text-left"
						style="font-size: 22rpx;font-family: PingFang SC;font-weight: 500;colorxf #999999;white-space: break-spaces;">
						{{$t('vip.xiy')}}
						<text @click="gove('/my/vip/vipxieyi')" style="color: #FE6D70;">《{{$t('vip.vipxiy')}}》</text>
						{{$t('vip.vipxf')}}
					</view>
				</view>

				<view class="money_btn" @click="openPay" v-if="member!=1">{{price}}{{$t('vip.kt')}}</view>

			</view>
		</view>

		<u-popup v-model="show" mode="bottom" border-radius="24">
			<view class="popupbox">
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
				showAgree: false,
				userName: '',
				MemberList: [],
				vipList: [],
				price: 0,
				tabIndex: 0,
				vipTypeId: '',
				member: '',
				endTime: '',

				show: false,
				openList: [],
				openWay: 1,
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('vip.title')
			})

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
			this.getUserInfo()
			this.getMemberList()
			this.getVipList()
		},
		methods: {
			gove(e) {
				console.log(e)
				uni.navigateTo({
					url: e
				})
			},
			bindselect(e, index) {
				this.vipTypeId = e.id
				this.price = e.money
				this.tabIndex = index
			},
			//获取VIP列表
			getVipList() {
				this.$Request.get('/app/VipDetails/selectVipDetails').then(res => {
					if (res.code == 0) {
						this.vipList = res.data
						this.price = this.vipList[0].money
						this.vipTypeId = this.vipList[0].id
					}
				})
			},
			// 获取特权列表
			getMemberList() {
				this.$Request.get('/app/member/selectMemberList').then(res => {
					if (res.code == 0) {
						this.MemberList = res.data
					}
				})
			},
			isShowAgree() {
				this.showAgree = !this.showAgree
			},
			getUserInfo() {
				this.$Request.getT('/app/user/selectUserMessage').then(res => {
					console.log(res)
					if (res.code == 0) {
						this.$queue.setData("avatar", res.data.avatar ? res.data.avatar :
							'../../static/logo.png');
						this.$queue.setData("member", res.data.member); //1==已开通会员
						this.$queue.setData("userId", res.data.userId);
						this.$queue.setData("status", res.data.status);
						this.$queue.setData("userName", res.data.userName ? res.data.userName : res
							.data.nickName);
						this.userName = res.data.userName ? res.data.userName : res.data.nickName
						this.member = res.data.member
						this.$queue.setData("member", res.data.member); //1==已开通会员
						this.endTime = res.data.endTime
					}
				});
			},
			openPay() {
				if (!this.showAgree) {
					uni.showToast({
						title: this.$t('vip.plaxy'), //'请同意会员服务协议',
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
						businessType: 3,
						redirectUri: '/pages/my/index',
						amount: that.price,
						vipId: that.vipTypeId,
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
						businessType: 3,
						redirectUri: '/pages/my/index',
						amount: that.price,
						vipId: that.vipTypeId,
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
				}

			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #FF6D9D;
	}

	.page_view {
		margin: 30upx;

		.top_viewImage {
			width: 703rpx;
			height: 330rpx;
			position: absolute;
			z-index: 1;
		}

		.center_view {
			width: 686rpx;
			// height: 775rpx;
			background: #FFFFFF;
			border-radius: 40rpx;
			position: absolute;
			z-index: 2;
			margin-top: 260rpx;
			margin-bottom: 110rpx;
			padding-bottom: 30rpx;

			.footer {
				padding-left: 30rpx;
				margin-top: 32rpx;
				font-size: 24rpx;
				margin-top: 30rpx;
				color: #666666;
				text-align: center;
				display: flex;
			}

			.money_btn {
				width: 638rpx;
				height: 88rpx;
				background: #FF6D9D;
				border-radius: 12rpx;
				font-size: 28rpx;
				font-family: PingFang SC;
				font-weight: bold;
				line-height: 88rpx;
				color: #FFFFFF;
				text-align: center;
				// margin-top: 30rpx;
				margin: 60rpx auto;
				// margin: 30rpx 25rpx 25rpx 0rpx;
			}

			.money_view {
				width: 95%;
				margin: 30rpx;
				display: flex;

				.money_itemSel {
					width: 202rpx;
					height: 240rpx;
					background: #FFFFFF;
					border: 4rpx solid #E6E6E6;
					border-radius: 8rpx;
					margin-right: 10rpx;
					text-align: center;
				}

				.active {
					background: #FFE2EC;
					border: 4rpx solid #FF6D9D;
				}

				.money_item {
					width: 202rpx;
					height: 240rpx;
					background: #FFFFFF;
					border: 4rpx solid #E6E6E6;
					border-radius: 8rpx;
					margin-right: 10rpx;
					text-align: center;
				}

				.item_name {
					font-size: 28rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #333333;
					width: 100%;
					text-align: center;
					margin-top: 20rpx;
				}

				.item_money {
					font-size: 56rpx;
					font-family: DIN;
					font-weight: bold;
					color: #333333;
					margin-top: 30rpx;
				}

				.item_moneymsg {
					font-size: 24rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #999999;
					margin-top: 10rpx;
				}
			}

			.center_viewTitle {
				width: 100%;
				text-align: center;
				margin-top: 38rpx;
				font-size: 32rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #333333;
			}

			.center_ImageView {
				display: flex;
				align-items: center;

				.ImageView_name {
					font-size: 28rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #333333;
				}

				.ImageView_title {
					font-size: 22rpx;
					font-family: PingFang SC;
					font-weight: 500;
					color: #999999;
					margin-top: 10rpx;
				}

				image {
					width: 60rpx;
					height: 60rpx;
				}
			}

		}

		.top_viewCenter {
			margin: 100rpx 40rpx 0rpx;
			position: absolute;
			z-index: 1;

			.top_viewCenterName {
				font-size: 42rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #333333;
			}

			.top_viewCenterTime {
				margin-top: 15rpx;
				font-size: 24rpx;
				font-family: PingFang SC;
				font-weight: 500;
				color: #D26488;
			}
		}
	}

	.popupbox {
		padding: 30rpx;

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
	}
</style>