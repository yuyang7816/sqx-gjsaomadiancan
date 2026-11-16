<template>
	<view class="pages">
		<view>
			<!-- banner图 -->
			<swiper class="swiper" :autoplay="true" interval="2000" duration="500" :circular="true"
				style="width: 100%;height: 480rpx;">
				<swiper-item v-for="(item,index) in bannerList" :key='index'>
					<image :src="item.imageUrl" mode="scaleToFill" style="width: 100%;"></image>
				</swiper-item>
			</swiper>
			<view class="content">

				<!-- 会员好友送礼 -->
				<view class="vip">
					<view class="vip_sty1" @click="goNav('/my/task/index')">
						<image src="../../static/images/index/jifenrenwu.png" mode=""></image>
						<view class="vip_sty1_tit">{{$t('jifen.renwu')}}</view>
						<view class="vip_sty1_tits ">{{$t('jifen.renwus')}}</view>
					</view>
					<view class="vip_sty1" @click="goNav('/my/integral/index')">
						<image src="../../static/images/index/jifenshop.png" mode=""></image>
						<view class="vip_sty1_tit">{{$t('jifen.sc')}}</view>
						<view class="vip_sty1_tits ">{{$t('jifen.scfh')}}</view>
					</view>
				</view>
				<!-- 积分 -->
				<view class="integral" @click="goNav('/my/integral/index')">
					<view class="integral_le">
						<view class="cont_order_sty_text3">{{$t('jifen.title')}}</view>
						<view class="cont_order_sty_text2">{{$t('jifen.dhyhq')}}</view>
					</view>
					<view class="integral_ri">
						<image src="../../static/images/index/right.png" mode=""></image>
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
				bannerList: [],
				userId: uni.getStorageSync('userId') ? uni.getStorageSync('userId') : '',

			}
		},
		onLoad(e) {
			uni.setNavigationBarTitle({
				title: this.$t('index.title')
			})
			if (e.shopId) {
				uni.setStorageSync('shopId', e.shopId)
				setTimeout(function() {
					uni.switchTab({
						url: '/pages/diancan/index',
					})
				}, 10)
			}
			// #ifdef MP-WEIXIN
			if (e.scene) {
				console.error('首次' + e.scene)
				const scene = decodeURIComponent(e.scene);
				let shopId = scene.split(',')[0]; //获取shopid
				uni.setStorageSync('shopId', shopId)
				setTimeout(function() {
					uni.switchTab({
						url: '/pages/diancan/index',
					})
				}, 10)
			}
			// #endif

			this.getBannerList()
		},
		onShow() {
			this.token = uni.getStorageSync('token')
		},
		methods: {
			goNav(url) {
				if (this.token) {
					uni.navigateTo({
						url
					})
				} else {
					uni.navigateTo({
						url: '/pages/login/login'
					});
				}
			},
			getBannerList() {
				let data = {
					classify: 1
				}
				this.$Request.get("/app/banner/selectBannerList", data).then(res => {
					if (res.code == 0) {
						this.bannerList = res.data
					}
				});
			},
		}
	}
</script>

<style scoped lang="scss">
	page {
		background-color: #FFFFFF;
	}

	.pages {
		padding-bottom: 100rpx;
	}

	.bg1 {
		background: #FFFFFF;
	}

	.scanSelect {
		width: 100%;
		height: auto;
		padding: 10rpx 20rpx 40rpx 40rpx;
	}

	.sticky-tabs {
		z-index: 990;
		position: sticky;
		top: var(--window-top);
		// background-color: #fff;
	}

	/* // 使用mescroll-uni,则top为0 */
	.mescroll-uni,
	::v-deep .mescroll-uni {
		.sticky-tabs {
			top: 0;
		}
	}



	/* 文字 */
	.cont_order_sty_text {
		font-size: 38rpx;
		font-weight: bold;
		color: #FF6D9E;
	}

	.cont_order_sty_text2 {
		font-size: 24rpx;
		font-weight: 500;
		color: #666666;
		margin-top: 10rpx;
	}

	.cont_order_sty_text3 {
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;
	}

	/* .banner */
	.banner {
		width: 100%;
		height: 430rpx;
	}

	.banner image {
		width: 100%;
		height: 430rpx;
	}

	.content {
		position: relative;
		top: -80rpx;
	}

	/* 会员好友送礼 */
	.vip {
		display: flex;
		background: #FFFFFF;
		border-radius: 24rpx;

		margin: 0 30rpx;
	}

	.vip_img {
		/* width: 100%; */
		text-align: right;
		/* margin-top: 20rpx; */
	}

	.vip_sty1 {
		flex: 1;
		border-radius: 18rpx;
		padding: 2% 2% 0 2%;
		position: relative;
	}

	.vip_sty1 image {
		width: 100%;
		height: 199rpx;
	}

	.vip_sty1_tit {
		position: absolute;
		top: 30rpx;
		left: 40rpx;
		color: #FFFFFF;
		font-size: 38rpx;
		font-weight: bold;
	}

	.vip_sty1_tits {
		position: absolute;
		top: 90rpx;
		left: 40rpx;
		color: #FFFFFF;
		font-size: 24rpx;
		font-weight: bold;
	}



	/* 积分 */
	.integral {
		width: 94%;
		margin: 3% auto 0;
		padding: 4%;
		background-color: #FFFFFF;
		border-radius: 18rpx;
		display: flex;
		align-items: center;
	}

	.integral_le {
		flex: 2;
		line-height: 1.5;
	}

	.integral_ri {
		flex: 1;
		text-align: right;
		line-height: 2.3;
	}

	.integral_ri image {
		width: 14rpx;
		height: 24rpx;
	}
</style>