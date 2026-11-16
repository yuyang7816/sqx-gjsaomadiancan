<template>
	<view class="integral_all">
		<!-- 积分信息 -->
		<view class="information">
			<view class="information_le">
				<view class="information_text1">当前可用积分</view>
				<view class="information_text2">{{integralNum}}</view>
				<view class="information_text3" @click="goDet(1)" >积分明细 <u-icon name="arrow-right" size="28"></u-icon></view>
			</view>
			<view class="information_ri">
				
				<image src="../static/integral/rectangular.png" mode="r"></image>
				
				<view class="information_text4" @click="goDet(2)">兑换记录</view>
			</view>
			<!-- <view class="duihuan"  @click="goDet(2)">
				兑换记录
			</view> -->
		</view>
		<!-- 兑换商品 -->
		<view class="integral_goods">
			<view class="integral_goods_sty">
				<view class="integral_goods_sty1" v-for="(item,index) in dataList" :key='index' >
					<image :src="item.couponPicture" mode=""></image>
					<view class="integral_goods_text">{{item.couponName}}</view>
					<view class="integral_goods_sty_bo">
						<view class="integral_goods_sty_bo_le"><text
								class="integral_goods_text2">{{item.needIntegral}}</text>积分</view>
						<view class="integral_goods_sty_bo_ri" @click="exchange(item)">兑换</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				btnnum: 0,
				page: 1,
				limit: 10,
				dataList: [],
				integralNum: 0
			};
		},
		onLoad() {
			this.getIntegral()
			this.getData()
		},
		methods: {
			exchange(e) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确定兑换' + e.couponName + '吗?',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							that.$Request.post("/app/coupon/buyCoupon", {
								couponId: e.couponId
							}).then(res => {
								if (res.code == 0) {
									uni.showToast({
										title: '兑换成功'
									})
									that.getIntegral()
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			goDet(e) {
				uni.navigateTo({
					url:'/my/integral/integralDet?classify='+e
				})
			},
			change(e) {
				this.btnnum = e
				console.log(this.btnnum)
			},
			getIntegral() {
				this.$Request.get("/app/userintegral/selectUserIntegral").then(res => {
					if (res.code == 0) {
						this.integralNum = res.data.integralNum
					}
				});
			},
			getData() {
				let data = {
					page: this.page,
					limit: this.limit
				}
				this.$Request.get("/app/coupon/selectCouponList", data).then(res => {
					if (res.code == 0) {
						if (this.page == 1) {
							this.dataList = res.data.list
						} else {
							this.dataList = [...this.dataList, ...res.data.list]
						}
					}
				});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getData();
		},
	}
</script>

<style scoped>
	.integral_all {
		width: 100%;
		background-color: #FF6D9E;
	}

	/* 积分信息 */
	.information {
		width: 94%;
		padding: 4% 3% 5%;
		display: flex;
		align-items: center;
		justify-content: space-between;
	}

	.information_le {
		/* width: 70%; */
		color: #FFFFFF;
	}

	.information_text1 {
		font-size: 28rpx;
		font-weight: 500;
		color: #FFFFFF;
		margin-bottom: 10upx;
		line-height: 32rpx
	}

	.information_text2 {
		font-size: 60rpx;
		font-weight: bold;
		color: #FFFFFF;
		margin-bottom: 10upx;
	}

	.information_text3 {
		font-size: 28rpx;
		font-weight: 500;
		color: #FFFFFF;
		line-height: 32rpx;
	}

	.information_ri {
		/* width: 30%; */
		/* padding-top: 50rpx; */
		position: relative;
	}

	.information_ri image {
		width: 218rpx;
		height: 98rpx;
	}

	.information_text4 {
		color: #FFFFFF;
		position: absolute;
		/* bottom: 0; */
		left: 51rpx;
		top: 23rpx;
		font-size: 28upx;
		/* right: 0; */
		/* margin: auto; */
	}
	.duihuan {
		padding: 8rpx 18rpx;
		background-color: #FF6D9E;
		box-shadow: 0 0 10rpx #FF6D9E;
		color: #FFFFFF;
	}

	/* 兑换商品 */
	.integral_goods {
		width: 100%;
		background-color: #F5F5F5;
		border-top-left-radius: 18rpx;
		border-top-right-radius: 18rpx;
		padding-bottom: 3%;
	}

	.integral_goods_sty {
		width: 94%;
		margin: 0 auto;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-between;
		padding-top: 3%;
	}

	.integral_goods_sty1 {
		width: 48.5%;
		/* margin-left: 3%; */
		color: #FFFFFF;
		background-color: #FFFFFF;
		padding: 0 2% 4%;
		border-radius: 18rpx;
		margin-bottom: 3%;
	}

	/* .integral_goods_sty1:first-child {
		margin-left: 0;
	} */

	.integral_goods_sty1 image {
		width: 100%;
		height: 240upx;
	}

	.integral_goods_text {
		font-size: 35rpx;
		font-weight: bold;
		color: #333333;
		line-height: 1.8;
	}

	.integral_goods_sty_bo {
		display: flex;
		margin-top: 2%;
	}

	.integral_goods_sty_bo_le {
		width: 65%;
		color: #999999;
		font-size: 30rpx;
	}

	.integral_goods_text2 {
		color: #D80204;
		
		font-size: 28rpx;
		margin-right: 4upx;
		font-weight: bold;
	}

	.integral_goods_sty_bo_ri {
		width: 35%;
		background-color:#FF6D9E;
		font-size: 24rpx;
		font-weight: bold;
		color: #FFFFFF;
		padding: 2% 0;
		text-align: center;
		border-radius: 50rpx;
	}
</style>
