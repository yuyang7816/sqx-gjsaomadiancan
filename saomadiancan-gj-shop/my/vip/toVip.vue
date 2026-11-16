<template>
	<view>
		<view class="page_view">
			<image src="../static/vip/viptopImage.png" class="top_viewImage"></image>
			<view class="top_viewCenter">
				<view class="top_viewCenterName">{{userName}}</view>
				<view class="top_viewCenterTime" v-if="member!=1">您暂未开通会员</view>
				<!-- <view class="top_viewCenterTime" v-else>您已开通会员</view> -->
				<view class="top_viewCenterTime" v-if="member==1&&endTime">到期时间：{{endTime}}</view>
			</view>
			<view class="center_view">
				<view class="center_viewTitle">会员特权</view>
				<view class="flex flex-wrap margin-top justify-between padding-lr">
					<view class="center_ImageView margin-bottom" v-for="(item,index) in MemberList" :key="index">
						<image :src="item.memberImg"></image>
						<view class="ImageView_name">{{item.memberName}}</view>
						<view class="ImageView_title">{{item.memberDescribe}}</view>
					</view>
				</view>

				<view class="money_view" v-if="member!=1">
					<view class="money_itemSel" v-for="(item,index) in vipList" :key="index"
						:class="tabIndex==index?'active':''" @click="bindselect(item,index)">
						<view class="item_name">{{item.vipName}}</view>
						<view class="item_money">{{item.money}}<text style="font-size: 20rpx;">元</text></view>
						<!-- <view class="item_moneymsg">￥1.0/元</view> -->
					</view>
				</view>

				<view class="footer flex align-center" v-if="member!=1">
					<image v-if="showAgree" @tap="isShowAgree"
						src="https://api.shengqianxiong.com.cn/img/20201112/669aa8946cfb4ebdb459d57193c0ebca.png"
						style="width: 30rpx;height: 30rpx;"></image>
					<image v-else @tap="isShowAgree"
						src="https://api.shengqianxiong.com.cn/img/20201112/1e9102fc891f4d86a13c7b2ba6921cba.png"
						style="width: 30rpx;height: 30rpx;"></image>
					<view class="margin-left-xs"
						style="font-size: 22rpx;font-family: PingFang SC;font-weight: 500;color: #999999;">
						开通即同意
						<text @click="gove('/my/vip/vipxieyi')">《会员服务协议》</text>
						请放心开通到期不会自动续费
					</view>
				</view>

				<view class="money_btn" @click="pay()" v-if="member!=1">{{price}}元开通</view>
				<!-- <view class="money_btn" @click="pay()">{{price}}元开通</view> -->
			</view>
		</view>
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
				endTime: ''
			}
		},
		onLoad() {
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
			pay() {
				let that = this
				if (!that.showAgree) {
					uni.showToast({
						title: '请同意会员服务协议',
						icon: 'none'
					})
					return
				}
				let data = {
					vipTypeId: that.vipTypeId,
					type: 3
				}
				this.$Request.post("/app/wxPay/payVip", data).then(res => {
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
									that.getUserInfo();
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

				// let data = {
				// 	vipTypeId: that.vipTypeId
				// }
				// uni.showModal({
				// 	title: '提示',
				// 	content: '确定支付' + that.price + '元？',
				// 	success: function(res) {
				// 		if (res.confirm) {
				// 			that.$Request.post('/app/UserVip/payVip', data).then(ret => {
				// 				if (ret.code == 0) {
				// 					uni.showToast({
				// 						title: '开通成功！'
				// 					})
				// 					that.getUserInfo()
				// 					uni.navigateBack()
				// 				}else{
				// 					that.$queue.showToast(ret.msg);
				// 				}
				// 			})
				// 		} else if (res.cancel) {
				// 			console.log('用户点击取消');
				// 		}
				// 	}
				// })

			}
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
				border-radius: 4rpx;
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
				margin-top: 48rpx;
				font-size: 32rpx;
				font-family: PingFang SC;
				font-weight: bold;
				color: #333333;
			}

			.center_ImageView {
				// flex: 1;
				width: 190rpx;
				text-align: center;

				.ImageView_name {
					font-size: 28rpx;
					font-family: PingFang SC;
					font-weight: bold;
					color: #333333;
					margin-top: 16rpx;
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
</style>
