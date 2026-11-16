<template>
	<view class="bg-white padding-lr">
		<!-- <view class="flex  padding-tb" @click="goNav('/pages/public/pwd')">
			<view class="flex-sub text-df" style="line-height: 50upx;">修改密码</view>
			<image src="../../static/images/my/right.png" style="line-height: 50upx;width: 20rpx;height: 30rpx;"></image>
		</view> -->
		<!-- <view class="flex  padding-tb">
			<view class="flex-sub text-df" style="line-height: 50upx;">帮助中心</view>
			<image src="../../static/images/my/right.png" style="line-height: 50upx;width: 20rpx;height: 30rpx;"></image>
		</view> -->
		<view class="flex align-center padding-tb" @click="goNav('/my/setting/xieyi')">
			<view class="flex-sub text-df" style="line-height: 50upx;">{{$t('set.yhxy')}}</view>
			<image src="../../static/images/index/right.png" style="line-height: 50upx;width: 15rpx;height: 25rpx;"></image>
		</view>
		<view class="flex align-center padding-tb" @click="goNav('/my/setting/mimi')">
			<view class="flex-sub text-df" style="line-height: 50upx;">{{$t('set.ysxy')}}</view>
			<image src="../../static/images/index/right.png" style="line-height: 50upx;width: 15rpx;height: 25rpx;"></image>
		</view>
		<!-- <view class="flex align-center padding-tb" @click="goNav('/pages/orderReceiving/about')">
			<view class="flex-sub text-df" style="line-height: 50upx;">关于我们</view>
			<image src="../../static/images/my/right_icon.png" style="line-height: 50upx;width: 20rpx;height: 30rpx;"></image>
		</view> -->
		<view class="btn" @click="goOut" >{{$t('set.tcdl')}}</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				checked: true
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title:this.$t('set.title')
			})
			this.getUserInfo()
		},
		methods: {
			change(val) {
				this.checked = val
				this.$Request.post('/app/user/updateSendMsg',{
					 isSendMsg: this.checked==true? 1: 2
				}).then(res =>{
					if(res.code === 0){
						this.content = res.data.value;
					}
				});
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.checked = res.data.isSendMsg==null|| res.data.isSendMsg==1? true: false
					}
				});
			},
 			goNav(e) {
				uni.navigateTo({
					url:e
				})
			},
			goOut() {
				uni.showModal({
					title: this.$t('index.wxts'),
					content: this.$t('set.platcdl'),
					confirmText:this.$t('index.sure'),
					cancelText:this.$t('index.qx'),
					success: function (res) {
						if (res.confirm) {
							console.log('用户点击确定');
							uni.removeStorageSync('userName')
							uni.removeStorageSync('avatar')
							uni.removeStorageSync('userId')
							uni.removeStorageSync('token')
							uni.removeStorageSync('phone')
							uni.removeStorageSync('zhiFuBaoName')
							uni.removeStorageSync('zhiFuBao')
							uni.removeStorageSync('invitationCode')
							uni.removeStorageSync('unionId')
							uni.removeStorageSync('openId')
							uni.removeStorageSync('isVIP')
							uni.removeStorageSync('wxCode')
							uni.removeStorageSync('wxQrCode')
							uni.removeStorageSync('sex')
							uni.showToast({
								title: this.$t('set.tccg'),//'退出成功！',
								icon: 'none'
							})
							setTimeout(function() {
								uni.navigateBack()
							},1000)
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				})
			}
		}
		
	}
</script>

<style>
	page {
		background-color: #FFF;
	}
	.btn {
		width: 100%;
		height: 80upx;
		background: #FF6D9D;
		border-radius: 6upx;
		text-align: center;
		line-height: 80upx;
		margin-top: 40upx;
		font-size: 34upx;
		color: #fff;
	}
</style>
