<template>
	<view>
		<view class="icon flex justify-center align-center">
			<view class="icon-box flex justify-center align-center">
				<u-icon name="checkmark-circle-fill" color="#FF6D9D" size="120"></u-icon>
			</view>
		</view>
		<view class="text">
			支付成功
		</view>
		<view class="submit flex justify-center">
			<view class="submit-box flex justify-center align-center" @click="gobackIndex">
				返回首页
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				
			};
		},
		methods:{
			gobackIndex(){
				uni.switchTab({
					url:'/pages/index/index'
				})
			},
		}
	}
</script>

<style lang="scss">
	page{
		background-color: #ffffff;
	}
	.icon{
		width: 100%;
		height: auto;
		margin-top: 50rpx;
		.icon-box{
			width: 686rpx;
			height: auto;
		}
	}
	.text{
		width: 100%;
		margin-top: 30rpx;
		text-align: center;
		
	}
	.submit{
		width: 100%;
		height: 80rpx;
		margin-top: 100rpx;
		.submit-box{
			width: 400rpx;
			height: 100%;
			background-color: #FF6D9D;
			color: #ffffff;
			border-radius: 14rpx;
		}
	}
</style>
