<template>
	<view>
		<view class="money flex justify-center align-center">
			<view class="money-box">
				<view class="money-box-title">
					付款金额
				</view>
				<view class="money-box-input">
					<u-input v-model="money" :type="type" placeholder="请输入付款金额" :border="border" :clearable="false" />
				</view>
			</view>
		</view>
		<view class="pay flex justify-center align-center">
			<view class="pay-box">
				<view class="pay-box-item flex justify-between align-center">
					<view class="pay-box-item-l flex align-center">
						<image src="../../static/images/diancan/icon_weixin.png" mode=""></image>
						<text>微信支付</text>
					</view>
					<view class="pay-box-item-r">
						<u-radio-group v-model="value" active-color="#FF6D9D">
							<u-radio name="微信">
							</u-radio>
						</u-radio-group>
					</view>
				</view>
			</view>
		</view>
		<view class="xieyi flex justify-center align-center">
			<view class="xieyi-box flex align-center">
				<u-checkbox v-model="checked" shape="circle" active-color="#FF6D9D"></u-checkbox>
				我已阅读并同意<text @click="gotoxoieyi()">《用户支付协议》</text>
			</view>
		</view>
		<view class="sub flex justify-center align-center">
			<view class="sub-box flex justify-center align-center" @click="pay">
				立即支付
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				checked:false,
				value: '微信',
				money: '',
				type: 'digit',
				border: false,
				shopId:'',
			};
		},
		onLoad(option) {
			if(!uni.getStorageSync('token')){
				this.nologin()
			}
			//微信扫码进来携带的参数（店铺的id、桌号的id）
			if (option.q && option.q != "undefined") {
				// 获取到二维码原始链接内容
				const qrUrl = decodeURIComponent(option.q)
				//此处就是我们要获取的参数 json，通过方法解析
				let jsonUrl = this.GetwxUrlParam(qrUrl);
				//比如我要得到id的值，直接取值即可
				this.shopId = jsonUrl.shopId;
			}
		},
		methods:{
			nologin(){
				uni.navigateTo({
					url: '/pages/login/login'
				})
			},
			//支付
			pay(){
				if(uni.getStorageSync('token')){
					if(!this.isPInt(this.money)){
						uni.showToast({
							title:'请输入正确的充值金额',
							icon:'none'
						})
						return
					}
					if(this.checked==false){
						uni.showToast({
							title:'请阅读并同意《用户支付协议',
							icon:'none'
						})
						return
					}
					let data = {
						shopId:this.shopId,
						money:this.money,
						type:3
					}
					this.$Request.postT('/app/wxPay/wxPayMoney', data).then(res => {
						if(res && res.code==0){
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
										uni.navigateTo({
											url:'/my/cashier/paySuccess'
										})
									},1000)
								},
								fail: function(err) {
									console.log('fail:' + JSON.stringify(err));
									uni.showToast({
										title: '支付失败',
										icon: 'none'
									})
								}
							});
						}else{
							uni.showToast({
								title:res.msg,
								icon:'none'
							})
						}
					})
				}else{
					this.nologin()
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
			//正则表达式方法--判断正整数、最多两位小数
			isPInt(str) {
			    var g = /(^[1-9]([0-9]+)?(\.[0-9]{1,2})?$)|(^(0){1}$)|(^[0-9]\.[0-9]([0-9])?$)/;
			    return g.test(str);
			},
			//用户支付协议
			gotoxoieyi(){
				uni.navigateTo({
					url:'/my/cashier/userPayXieyi'
				})
			},
		}
	}
</script>

<style lang="scss">
	.money {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.money-box {
			width: 686rpx;
			height: auto;
			background-color: #ffffff;
			border-radius: 14rpx;
			padding: 30rpx;

			.money-box-title {
				font-size: 38rpx;
			}

			.money-box-input {
				margin-top: 20rpx;
			}
		}
	}

	::v-deep .u-radio {
		display: block !important;
	}
	::v-deep .u-checkbox{
		display: block !important;
		margin-right: 10rpx;
	}
	.pay {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.pay-box {
			width: 686rpx;
			height: auto;
			padding: 30rpx;
			background-color: #ffffff;
			border-radius: 14rpx;

			.pay-box-item {
				width: 100%;
			}

			.pay-box-item-l {
				image {
					width: 70rpx;
					height: 70rpx;
					border-radius: 50%;
					margin-right: 20rpx;
				}
			}
		}
	}

	.xieyi {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.xieyi-box {
			width: 686rpx;
			height: auto;
			padding: 30rpx;
			text{
				color: #FF6D9D;
			}
		}
	}
	.sub{
		width: 100%;
		height: 80rpx;
		position: fixed;
		bottom: 100rpx;
		left: 0;
		.sub-box{
			width: 686rpx;
			border-radius: 14rpx;
			background-color: #FF6D9D;
			color: #ffffff;
			height: 100%;
		}
	}
</style>
