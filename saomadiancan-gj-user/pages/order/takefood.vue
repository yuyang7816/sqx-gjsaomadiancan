<template>
	<view class="" style="padding-bottom: 40rpx;">
		<!-- 订单 -->
		<view class="tosend">
			<view class="tosend_text" v-if="dataDet.status == 6">{{$t('order.zhizuo')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 3 && dataDet.orderType == 1">{{$t('order.dqc')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 3 && dataDet.orderType == 2">{{$t('order.psz')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 4">{{$t('order.ywc')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 5">{{$t('order.yqx')}}</view>
			<view class="tosend_header">
				<!-- 排序 -->
				<view class="cont_two_top" v-if="dataDet.status == 3">
					<view class="cont_two_top_le">{{$t('order.qchm')}}</view>
				</view>
				<view v-if="dataDet.status == 3 && dataDet.orderType == 1" class="cont_two_text">{{dataDet.orderCode}}
				</view>
				<view v-if="dataDet.status == 3 && dataDet.orderType == 1" class="cont_two_text2">
					{{$t('order.qmhy')}}<text>{{dataDet.countOrder?dataDet.countOrder:0}}</text>{{$t('order.pd')}}
				</view>
				<block v-if="dataDet.status != 5">
					<view v-if="dataDet.status != 3">
						<view class="flex justify-between">
							<view>{{$t('order.qchm')}}</view>
							<view class="zhizuo" v-if="dataDet.status == 6">{{$t('order.zhizuo')}}</view>
							<view class="zhizuo" v-if="dataDet.status == 3 && dataDet.orderType == 1">{{$t('order.dqc')}}</view>
							<view class="zhizuo" v-if="dataDet.status == 3 && dataDet.orderType == 2">{{$t('order.psz')}}</view>
							<view class="zhizuo" v-if="dataDet.status == 4">{{$t('order.ywc')}}</view>
							<view class="zhizuo" v-if="dataDet.status == 5">{{$t('order.yqx')}}</view>
							
						</view>
						<view style="font-size:58upx;" class="text-center text-bold padding-tb">{{dataDet.orderCode}}
						</view>
					</view>
					<view style="width: 100%;height:1upx; background:#E6E6E6;margin-top: 20upx;"></view>
				</block>

				<!-- 商品列表 -->
				<view class="" v-for="(item,index) in dataDet.orderGoodsList" :key='index'>
					<view class="tosend_header_food">
						<view class="tosend_header_food_le">
							<image :src="item.goodsPicture[0]" style="border-radius: 10rpx;" mode="scaleToFill"></image>
						</view>
						<view class="tosend_header_food_ce justify-between">
							<view class="tosend_header_food_text">{{item.goodsName}}</view>
							<view class="tosend_header_food_text2">{{item.skuMessage}}</view>
						</view>
						<view class="tosend_header_food_ri">
							<view class="tosend_header_food_text"><text>¥</text>{{item.goodsPrice}}</view>
							<view class="tosend_header_food_text2">X{{item.goodsNum}}</view>
						</view>
					</view>

					<view v-if="item.goodRemarks" class="tosend_header_food_text2">
						{{$t('payorder.bz')}}：{{item.goodRemarks}}
					</view>
				</view>
				<view class="tosend_header_do " v-if="dataDet.packMoney && dataDet.packMoney>0">
					<view>{{$t('payorder.dbf')}}</view>
					<view class="tosend_header_do_ri">{{$t('index.qian')}}{{dataDet.packMoney}}</view>
				</view>
				<view class="tosend_header_do"
					v-if="dataDet.errandMoney>0 && dataDet.orderType == 2 && dataDet.errandUser ==2">
					<view>{{$t('payorder.ptf')}}</view>
					<view class="tosend_header_do_ri">{{$t('index.qian')}}{{dataDet.errandMoney}}</view>
				</view>
				<view class="tosend_header_do " v-if="dataDet.couponMoney>0">
					<view>{{$t('yjq.title')}}</view>
					<view class="tosend_header_do_ri">-{{$t('index.qian')}}{{dataDet.couponMoney}}</view>
				</view>
				<view class="tosend_header_do " v-if="dataDet.vipMoney && dataDet.vipMoney>0">
					<view>{{$t('payorder.vipzk')}}</view>
					<view class="tosend_header_do_ri">{{$t('index.qian')}}{{dataDet.vipMoney}}</view>
				</view>
				<view style="width: 100%;height:1upx; background:#E6E6E6;margin-top: 20upx;"></view>
				<view class="tosend_header_do margin-top-xs">
					<view class="tosend_header_do_le2">{{$t('payorder.sf')}}</view>
					<view class="tosend_header_do_ri2"><text>{{$t('index.qian')}}</text>{{dataDet.payMoney}}</view>
				</view>
			</view>
			<!-- 其他信息 -->
			<view class="tosend_cont">
				<view class="tosend_header_text">{{$t('payorder.info')}}</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType == 2">
					<view class="tosend_cont_infor_le">{{$t('add.username')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.address.userName}}</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType == 2">
					<view class="tosend_cont_infor_le">{{$t('add.phone')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.address.userPhone}}</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType == 2">
					<view class="tosend_cont_infor_le">{{$t('add.xxdz')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.address.address}}{{dataDet.address.addressDetail}}
					</view>
				</view>
				<view class="tosend_cont_infor">
					<view class="tosend_cont_infor_le">{{$t('order.qchm')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.orderCode}}</view>
				</view>
				<view class="tosend_cont_infor">
					<view class="tosend_cont_infor_le">{{$t('payorder.xsdj')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.payTime}}</view>
				</view>
				<view class="flex justify-between align-center" style="margin-top: 3%;">
					<view class="tosend_cont_infor_le">{{$t('payorder.ddbh')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.orderNumber}}</view>
					<view class="text-orange margin-left-lg" @click="copy(dataDet.orderNumber)">{{$t('index.copy')}}</view>
				</view>
				<view class="tosend_cont_infor">
					<view class="tosend_cont_infor_le">{{$t('purse.paytype')}}</view>
					<view class="tosend_cont_infor_ri" v-if="dataDet.payType==1">微信支付</view>
					<view class="tosend_cont_infor_ri" v-if="dataDet.payType==2">{{$t('pay.lingqian')}}</view>
				</view>
				<view class="tosend_cont_infor">
					<view class="tosend_cont_infor_le">{{$t('payorder.bz')}}</view>
					<view class="tosend_cont_infor_ri" v-if="dataDet.orderRemarks">{{dataDet.orderRemarks}}</view>
					<view class="tosend_cont_infor_ri" v-else>{{$t('order.wu')}}</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderId: '',
				dataDet: {}
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('order.detailtit')
			})
			uni.showLoading({
				title: this.$t('loadingText.jzz')//'加载中...'
			})
			this.orderId = option.orderId
			this.getDataDet()
		},
		methods: {
			getDataDet() {
				let data = {
					orderId: this.orderId
				}
				this.$Request.get("/app/order/selectOrderById", data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.dataDet = res.data
						this.dataDet.orderGoodsList.forEach(res => {
							res.goodsPicture = res.goodsPicture.split(',')
						})
						this.dataDet.address = JSON.parse(this.dataDet.address)
						console.log(this.dataDet)
					}
				});
			},
			copy(value) {
				uni.setClipboardData({
					data: value, //要被复制的内容
					success: () => { //复制成功的回调函数
						uni.showToast({ //提示
							title: this.$t('index.copyok')//'复制成功'
						})
					}
				});
			}
		}
	}
</script>

<style scoped>
	.tosend {
		width: 100%;

		background: linear-gradient(0deg, rgba(255, 255, 255, 0) 0%, #FF9CBD 100%);
	}

	.tosend_text {
		padding: 3% 3% 4%;
		font-size: 38upx;
		font-weight: 800;
		color: #FFFFFF;
	}

	.tosend_header {
		width: 94%;
		background-color: #FFFFFF;
		margin: 0 auto;
		border-radius: 18rpx;
		padding: 3%;
	}

	/* 排序 */
	.cont_two_top {
		width: 100%;
		display: flex;
		justify-content: space-between;
	}

	.cont_two_top_le {
		font-size: 30rpx;
		font-weight: 500;
		color: #333333;
	}

	.zhizuo {
		background: rgba(255, 109, 157, 0.2);
		border: 1rpx solid #FF6D9D;
		opacity: 0.6;
		border-radius: 8rpx;
		color: #FF6D9D;
		padding: 7rpx 25rpx;
	}

	.cont_two_text {
		font-size: 56rpx;
		text-align: center;
		font-weight: bold;
		color: #333333;
		margin: 5% 0;
		line-height: 32rpx;
	}

	.cont_two_text2 {
		font-size: 30rpx;
		width: 100%;
		font-weight: 500;
		color: #333333;
		text-align: center;
		padding-bottom: 4%;
		line-height: 32rpx;
	}

	.cont_two_text2 text {
		color: #FF130A;
	}

	/* 商品列表 */
	.tosend_header_text {
		font-family: PingFang SC;
		font-weight: 800;
		color: #333333;
		line-height: 2;
		font-size: 30rpx;
	}

	.tosend_header_food {
		width: 100%;
		display: flex;
		margin-top: 35rpx;
		justify-content: space-between;
	}

	.tosend_header_food_le {
		width: 15%;
	}

	.tosend_header_food_le image {
		width: 110rpx;
		height: 110rpx;
	}

	.tosend_header_food_ce {
		/* margin: 0 0 0 4%; */
		width: 57%;
	}

	.tosend_header_food_ri {
		text-align: right;
		width: 25%;
	}

	.tosend_header_food_text {
		font-size: 34rpx;
		font-weight: 500;
		color: #333333;
		line-height: 1.8;
	}

	.tosend_header_food_text text {
		font-size: 25rpx;
	}

	.tosend_header_food_text2 {
		font-size: 30rpx;
		font-weight: 500;
		color: #999999;
	}

	.do_top {
		padding-top: 3%;
	}

	.do_bot {
		padding-bottom: 3%;
		border-bottom: 2rpx solid #E6E6E6;
	}

	.tosend_header_do {
		width: 100%;
		color: #333333;
		font-size: 30rpx;
		display: flex;
		line-height: 1.5;
	}

	.tosend_header_do view {
		flex: 1;
	}

	.tosend_header_do_ri {
		text-align: right;
	}

	.tosend_header_do_le2 {
		font-family: PingFang SC;
		font-weight: 500;
		color: #999999;
		padding-top: 1.5%;
	}

	.tosend_header_do_ri2 {
		text-align: right;
		color: #FF6D9D;
		font-size: 40rpx;
	}

	.tosend_header_do_ri2 text {
		font-size: 30rpx;
	}

	/* 其他信息 */
	.tosend_cont {
		width: 94%;
		background-color: #FFFFFF;
		margin: 3% auto;
		border-radius: 18rpx;
		padding: 3%;
	}

	.tosend_cont_infor {
		display: flex;
		margin-top: 30rpx;
	}

	.tosend_cont_infor_le {
		
		color: #999999;
	}

	.tosend_cont_infor_ri {
		flex: 1;
		text-align: right;
		color: #333333;
	}

	.tosend_cont_text {
		text-align: right;
	}
</style>