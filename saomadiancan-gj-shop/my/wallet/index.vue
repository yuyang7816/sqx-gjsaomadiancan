<template>
	<view class="pages">
		<!-- 余额 -->
		<view class="wallet">
			<view class="wallet_header">
				<view class="wallet_header_text">当前余额</view>
				<view class="wallet_header_xin">
					<view class="wallet_header_le">{{myMoney}} <text> 元</text></view>
					<!-- <view class="wallet_header_ri">
						<image src="../static/wallet/pay.png" mode=""></image> <text>扫码支付</text>
					</view> -->
				</view>
			</view>
		</view>
		<!-- 充值 -->
		<view class="wallet_topup">
			<view class="wallet_header_text">充值金额</view>
			<view class="wallet_header_xin2">
				<view class="wallet_header_xin2_sty" v-for="(item,index) in wallet" :key='index' :class="{topup:item.isSelect}" @click="active(item)">
					<view class="topup_img" v-if="item.isSelect">
						<image src="../static/wallet/face.png" mode=""></image>
					</view>
					充值<text>{{item.money}}</text>元
				</view>
			</view>
			<!-- <view class="wallet_header_xin2">
				<view class="wallet_header_xin2_sty">充值<text>200</text>元</view>
				<view class="wallet_header_xin2_sty">充值<text>300</text>元</view>
			</view> -->
		</view>
		<!-- 消费 -->
		<view class="wallet_header_consum" @click="record()">
			<view class="wallet_header_consum_le">收支明细</view>
			<view class="wallet_header_consum_ri">
				<image src="../static/wallet/right1.png" mode="scaleToFill"></image>
			</view>
		</view>
		<!-- 立即充值 -->
		<view class="wallet_bottom" @click="payment()" >立即充值</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				myMoney: 0,
				wallet: [{
						id: 1,
						price: '50',
						isSelect: true
					},
					{
						id: 2,
						price: '100',
						isSelect: false
					},
					{
						id: 3,
						price: '200',
						isSelect: false
					},
					{
						id: 4,
						price: '500',
						isSelect: false
					}
				],
				thisSelect: {
					// price: 50
				}
			}
		},
		onShow() {
			this.deploy()
			this.getMoneyNum()
			
		},
		methods: {
			deploy() {
				this.$Request.get("/app/topupmoney/selectTopUpMoney").then(res => {
					if (res.code == 0) {
						this.wallet = res.data
						this.wallet.forEach(res=> {
							res.isSelect = false
						})
					}
				});
			},
			active(e) {
				this.wallet.forEach(res => {
					if (res.id == e.id) {
						res.isSelect = true
						this.thisSelect = e
						console.log(this.thisSelect)
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
			payment() {
				let that = this
				that.show = false
				if(!this.thisSelect.money) {
					uni.showToast({
						title: '请选择充值金额！',
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
		font-size: 44rpx;
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
		bottom: 61rpx;
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
</style>
