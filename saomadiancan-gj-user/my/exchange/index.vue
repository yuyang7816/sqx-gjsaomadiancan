<template>
	<view class="pages">
		<!-- 切换选项 -->
		<view class="exchange_title">
			<view @tap="change(0)" :class="{exchange_title_btna:btnnum == 0}">兑换优惠劵</view>
			<view @tap="change(1)" :class="{exchange_title_btna:btnnum == 1}">兑换礼品卡</view>
		</view>
		<!-- 兑换优惠劵 -->
		<view class="exchange_cont1" :class="{exchange_cont_dis:btnnum == 0}">
			<view class="exchange_cont1_text">优惠劵</view>
			<input type="text" placeholder="请输入优惠劵兑换码"  />
			<view class="exchange_cont1_text2">立即兑换</view>
		</view>
		<!-- 兑换礼品卡 -->
		<view class="exchange_cont1" :class="{exchange_cont_dis:btnnum == 1}">
			<view class="exchange_cont1_text">礼品卡</view>
			<input type="text" value="请输入卡号" />
			<input type="text" value="请输入卡密" />
			<view class="exchange_cont1_text2">立即兑换</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				btnnum: 0,
			};
		},
		methods: {
			change(e) {
				this.btnnum = e
				console.log(this.btnnum)
			}
		}
	}
</script>

<style scoped>
	/* 切换选项 */
	.exchange_title {
		display: flex;
		background-color: #FFFFFF;
		color: #999999;
	}
	
	.exchange_title view {
		flex-grow: 1;
		margin: 3% 12% 2%;
		text-align: center;
	}
	
	.exchange_title_btna {
		font-size: 42rpx;line-height:34rpx;
		border-bottom: 14rpx solid #FCD202;
		color: #000000;
		font-weight: bold;	
	}
	/* 兑换优惠劵 */
	.exchange_cont1 {
		display: none;width: 88%;margin: 4% auto;
		padding: 3% 3% 5%;background-color: #FFFFFF;
		background: #FFFFFF;
		border-radius: 18rpx;
	}
	.exchange_cont1_text{
		font-size: 38rpx;text-align: center;
		font-weight: 500;
		color: #333333;
	}
	input{
		width: 94%;padding: 0 3%;
		height: 88rpx;margin: 4% 0;
		background: #F7F5F5;
		border-radius: 18rpx;
	}
	.exchange_cont1_text2{
		width: 100%;line-height: 2.8;
		background: linear-gradient(90deg, #FFDB27 0%, #FCC402 100%);
		border-radius: 100rpx;
		font-weight: bold;margin-top: 40rpx;
		color: #333333;text-align: center;
	}
	
	
	
	.exchange_cont {
		display: none;
	}
	.exchange_cont_dis {
		display: block;
	}
</style>
