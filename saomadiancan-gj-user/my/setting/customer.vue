<template>
	<view style="height: 100vh;margin: 32upx;">
		<view style="text-align: center;background: #fff;padding: 40upx;border-radius: 32upx;">
			<view style="font-size: 38upx;color: #333333">{{$t('kefu.platit')}}</view>
			<view style="font-size: 32upx;margin-top: 32upx;color: #333333">{{$t('kefu.wxh')}}：{{weixin}}</view>
			<view @click="copyHref" class="copybtn">{{$t('index.copy')}}</view>

			<image @click="saveImg" mode="aspectFit" style="margin-top: 32upx" :src="image"></image>
			<view style="font-size: 28upx;color: #333333;margin-top: 32upx" v-if="isWeiXin">
				{{ isWeiXin ? $t('kefu.sbewm') : '' }}
			</view>

			<!-- <button open-type="contact" class="btn">{{$t('kefu.zxkf')}}</button> -->
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				image: '',
				isWeiXin: false,
				weixin: '710070994',
				webviewStyles: {
					progress: {
						color: '#1A1929 '
					}
				}
			};
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('kefu.title')
			})
			// #ifdef H5
			let ua = navigator.userAgent.toLowerCase();
			if (ua.indexOf('micromessenger') !== -1) {
				this.isWeiXin = true;
			}
			// #endif
			//获取客服二维码
			this.$Request.getT('/app/common/type/1').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						console.log(res.data.value)
						this.image = res.data.value;
					}
				}
			});
			this.$Request.getT('/app/common/type/44').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.weixin = res.data.value;
					}
				}
			});
		},
		onPullDownRefresh: function() {
			uni.stopPullDownRefresh(); // 停止刷新
		},
		methods: {
			//邀请码复制
			copyHref() {
				uni.setClipboardData({
					data: this.weixin,
					success: r => {
						// this.$queue.showToast('复制成功');
						uni.showToast({
							title: this.$t('index.copyok'),
							icon: 'none'
						})
					}
				});
			},
			saveImg() {
				let that = this;
				let _this = this;
				let imgsArray = [];
				imgsArray[0] = that.image
				uni.previewImage({
					current: 0,
					urls: imgsArray
				});
			},

			// 在线客服
			goChat() {
				let token = this.$queue.getData('token');
				if (token) {
					uni.navigateTo({
						url: '/my/setting/chat'
					});
				} else {
					this.goLoginInfo();
				}
			},
			//统一登录跳转
			goLoginInfo() {
				uni.navigateTo({
					url: '/pages/login/loginphone'
				});
			},
		}
	};
</script>

<style>
	/* @import '../../static/css/index.css'; */

	page {
		/* background: #1c1b20; */
	}

	.btn {
		border: none;
		background-color: #fff;
		color: #FF6D9D;
		margin-top: 32upx;
		font-size: 30upx;
	}

	button::after {
		border: none;
	}

	.copybtn {
		display: inline-flex;
		align-items: center;justify-content: center;
		background: #FF6D9D;
		margin-top: 32upx;
		font-size: 30upx;
		color: #333333;
		padding: 8upx 20upx;
		border-radius: 24upx;
		color: #FFFFFF;
	}
</style>