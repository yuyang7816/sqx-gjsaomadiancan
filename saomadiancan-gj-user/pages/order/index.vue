<template>
	<view class="pages" style="position: relative;">
		<u-sticky>
			<view class="bg">
				<view class=" padding-lr-sm">
					<u-tabs :list="list" :is-scroll="true" :current="orderType" @change="changeTab"
						active-color="#FF6D9D"></u-tabs>
				</view>
				<view>
					<u-tabs v-if="orderType == 0" active-color="#FF6D9D" :list="qucanList" :is-scroll="true"
						:current="current" @change="change"></u-tabs>
					<u-tabs v-if="orderType == 1" active-color="#FF6D9D" :list="waimaiList" :is-scroll="true"
						:current="current1" @change="change1">
					</u-tabs>
				</view>

			</view>
		</u-sticky>

		<!-- 全部订单 -->
		<view>
			<view v-for="(item,index) in orderList" :key='index'
				@click="goNav('/pages/order/takefood?orderId='+item.orderId)">
				<view class="cont" v-if="item.orderType == 1">
					<view class="flex justify-between align-center padding-sm">

						<view class="text-lg">{{$t('order.qchm')}}</view>
						<view class="zhizuo" v-if="item.status == 6">{{$t('order.zhizuo')}}</view>
						<view class="order_name" v-if="item.status == 3">{{$t('order.dqc')}}</view>
						<view class="order_name" v-if="item.status == 4">{{$t('order.ywc')}}</view>
						<view class="order_name" v-if="item.status == 5 ||item.status == 8">{{$t('order.yqx')}}</view>
						<view class="order_name" v-if="item.status == 7">{{$t('order.djd')}}</view>
					</view>
					<view class="cont_two_text">{{item.orderCode}}</view>
					<view class="cont_two_text2" v-if="item.status == 6">{{$t('order.qmhy')}}<text>{{item.countOrder}}</text>{{$t('order.pd')}}</view>
					<view class="cont_two_bottom">
						<view class="cont_two_bottom_le">{{$t('order.dcxq')}}</view>
						<view class="cont_two_bottom_ri">
							<image src="../../static/images/order/right1.png" mode=""></image>
						</view>
					</view>
				</view>
				<view class="cont" v-if="item.orderType == 2">
					<view class="flex justify-between align-center padding-sm">
						<view class="text-lg">{{$t('order.qchm')}}</view>
						<view class="order_name" v-if="item.status == 3">{{$t('order.psz')}}</view>
						<view class="order_name" v-if="item.status == 4">{{$t('order.ywc')}}</view>
						<view class="order_name" v-if="item.status == 5 ||item.status == 8">{{$t('order.yqx')}}</view>
						<view class="zhizuo" v-if="item.status == 6">{{$t('order.zhizuo')}}</view>
						<view class="order_name" v-if="item.status == 7">{{$t('order.djd')}}</view>

					</view>
					<u-line color="#E6E6E6" />

					<view class="">
						<view class=" padding-lr margin-tb-sm" v-for="(ite,ind) in item.orderGoodsList" :key='ind'>
							<view class="flex">
								<image :src="ite.goodsPicture[0]" mode=""
									style="width: 120rpx;height: 120rpx;border-radius: 10rpx;"></image>
								<view class="margin-left-sm flex flex-direction justify-between">
									<view class="text-black text-bold text-lg">{{ite.goodsName}}</view>
									<view class="text-gray text-sm">{{ite.skuMessage}}</view>
								</view>
							</view>
						</view>
					</view>
					<u-line color="#E6E6E6" />
					<view class="flex justify-between align-center padding-lr-sm padding-tb-sm">
						<view class="text-sm text-gray">{{$t('order.ss')}} <text
								class="text-lg text-bold text-black margin-left-xs">{{$t('index.qian')}}{{item.payMoney}}</text></view>
						<view class="flex padding-tb-sm">
							<view v-if="item.status == 3" class="btn" @click.stop="finish(item)">{{$t('order.qrsh')}}</view>

						</view>
					</view>
				</view>
				<view class="cont" v-if="item.orderType == 3">
					<view class="flex justify-between align-center padding-sm">

						<view class="text-lg">{{$t('order.qchm')}}</view>
						<view class="order_name" v-if="item.status == 3">{{$t('order.psz')}}</view>
						<view class="order_name" v-if="item.status == 4">{{$t('order.ywc')}}</view>
						<view class="order_name" v-if="item.status == 5 ||item.status == 8">{{$t('order.yqx')}}</view>
						<view class="zhizuo" v-if="item.status == 6">{{$t('order.zhizuo')}}</view>
						<view class="order_name" v-if="item.status == 7">{{$t('order.djd')}}</view>

					</view>
					<u-line color="#E6E6E6" />

					<view class="">
						<view class=" padding-lr margin-tb-sm" v-for="(ite,ind) in item.orderGoodsList" :key='ind'>
							<view class="flex">
								<image :src="ite.goodsPicture[0]" mode=""
									style="width: 120rpx;height: 120rpx;border-radius: 10rpx;"></image>
								<view class="margin-left-sm flex flex-direction justify-between">
									<view class="text-black text-bold text-lg">{{ite.goodsName}}</view>
									<view class="text-gray text-sm">{{ite.skuMessage}}</view>
								</view>
							</view>
							<view class="text-gray text-sm margin-tb-sm" v-if="ite.goodRemarks">
								{{$t('payorder.bz')}}：{{ite.goodRemarks}}
							</view>
						</view>
					</view>
					<u-line color="#E6E6E6" />
					<view class="flex justify-between align-center padding-lr-sm padding-tb-sm">
						<view class="text-sm text-gray">{{$t('order.ss')}} <text
								class="text-lg text-bold text-black margin-left-xs">{{$t('index.qian')}}{{item.payMoney}}</text></view>
						<view class="flex padding-tb-sm">
							<view v-if="item.status == 3" class="btn" @click.stop="finish(item)">{{$t('order.qrsh')}}</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<empty v-if="!orderList.length" style="z-index:0"></empty>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				
				page: 1,
				limit: 10,
				orderList: [],
				status: '',
				xcxSelect: '',
				current: 0,
				current1: 0,
				qucanList: [{
						name: '' //全部
					},
					{
						name: '' //制作中
					},
					{
						name: '' //待取餐
					},
					{
						name: '' //已完成
					},
					{
						name: '' //已取消
					}
				],
				waimaiList: [{
						name: '' //全部
					},
					{
						name: '' //制作中
					},
					{
						name: '' //配送中
					},
					{
						name: '' //已完成
					},
					{
						name: '' //已取消
					}
				],
				list: [{
					name: '' ,//到店取餐
					id:1
				}, {
					name: '' ,//外卖配送
					id:2
				}, {
					name: '', //到店堂食
					id:3
				}],
				orderType:0,
			};
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title: this.$t('order.title')
			})
			if (option.orderType) {
				this.orderType = option.orderType
			}
			uni.showLoading({
				title: this.$t('loadingText.jzz') //'加载中...'
			})
			this.qucanList[0].name = this.$t('order.all'); //全部
			this.qucanList[1].name = this.$t('order.zhizuo'); //制作中
			this.qucanList[2].name = this.$t('order.dqc'); //待取餐
			this.qucanList[3].name = this.$t('order.ywc'); //已完成
			this.qucanList[4].name = this.$t('order.yqx'); //已取消

			this.waimaiList[0].name = this.$t('order.all'); //全部
			this.waimaiList[1].name = this.$t('order.zhizuo'); //制作中
			this.waimaiList[2].name = this.$t('order.ywc'); //已完成
			this.waimaiList[3].name = this.$t('order.yqx'); //已取消

			this.list[0].name = this.$t('order.daodian'); //到店取餐
			this.list[1].name = this.$t('order.wmps'); //外卖配送
			this.list[2].name = this.$t('order.ddts'); //到店堂食
		},
		onShow() {
			this.getOrderList()
			this.xcxSelect = this.$queue.getData('xcxSelect');
		},
		methods: {
		
			finish(e) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plawcdd'),
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId
							}
							that.$Request.post("/app/order/accomplishOrder", data).then(res => {
								if (res.code == 0) {
									that.getOrderList()
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			
			changeTab(index){
				this.orderType = index
				this.page = 1
				this.current = 0
				this.status = ''
				this.getOrderList()
			},
			change(index) {
				this.page = 1
				this.current = index;
				switch (index) {
					case 0:
						this.status = '' //全部
						break;
					case 1:
						this.status = 6 //制作中
						break;
					case 2:
						this.status = 3 //待取餐
						break;
					case 3:
						this.status = 4 //已完成
						break;
					case 4:
						this.status = 5 //已取消
						break;
				}
				this.getOrderList()
			},
			change1(index) {
				this.page = 1
				this.current1 = index;
				switch (index) {
					case 0:
						this.status = ''
						break;
					case 1:
						this.status = 6
						break;
					case 2:
						this.status = 3
						break;
					case 3:
						this.status = 4
						break;
					case 4:
						this.status = 5
						break;
				}
				this.getOrderList()
			},
			goNav(url) {
				uni.navigateTo({
					url
				})
			},
			// 获取数据列表
			getOrderList() {
				let type = this.list[this.orderType].id
				let data = {
					page: this.page,
					limit: this.limit,
					status: this.status,
					orderType: type
				}
				this.$Request.get("/app/order/waitTakeFood", data).then(res => {
					if (res.code == 0) {
						// this.msgData = res.data
						res.data.list.forEach(res => {
							res.orderGoodsList.forEach(ret => {
								if (ret.goodsPicture) {
									ret.goodsPicture = ret.goodsPicture.split(',')
								}
							})

						})

						if (this.page == 1) {
							this.orderList = res.data.list
						} else {
							this.orderList = [...this.orderList, ...res.data.list]
						}
					}
					uni.stopPullDownRefresh();
					uni.hideLoading()

				});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getOrderList();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getOrderList();
		},
	}
</script>

<style scoped>

	.bg {
		background-color: #FFFFFF;
	}

	/* 切换选项 */
	.nav {
		display: flex;
		align-items: center;
		justify-content: center;
		background-color: #FFFFFF;
		color: #999999;
	}

	.btn {
		width: 150rpx;
		height: 60rpx;
		line-height: 60rpx;
		text-align: center;
		background: #F5F5F5;
		font-size: 28rpx;
		border: 2rpx solid ##F5F5F5;
		color: #666666;
		border-radius: 50rpx;
		font-weight: 700;
		margin-left: 10rpx;
	}

	.btn_ {
		width: 150rpx;
		height: 60rpx;
		line-height: 60rpx;
		text-align: center;
		background: #FCD202;
		font-size: 28rpx;
		border: 2rpx solid #FCD202;
		color: #333333;
		border-radius: 50rpx;
		font-weight: 700;
		margin-left: 10rpx;
	}



	.shaix {
		width: 30%;
		text-align: center;
		display: flex;
		align-items: center;
		justify-content: center;
		letter-spacing: 4rpx;
		font-size: 27rpx;
	}



	.nav view {
		flex-grow: 1;
		margin: 3% 6.5% 2%;
		text-align: center;
	}

	.nav_btna {
		font-size: 42rpx;
		line-height: 34rpx;
		color: #000000;
		font-weight: bold;
		border-bottom: 14rpx solid #FCD202;
	}

	/* 内容 */
	/* 全部订单 */

	/* 到店取餐 */
	.cont_two {
		display: none;
		width: 94%;
		margin: 3% auto;
		background-color: #FFFFFF;
		border-radius: 18rpx;
	}

	.cont {
		/* display: none; */
		width: 94%;
		margin: 3% auto;
		background-color: #FFFFFF;
		border-radius: 18rpx;
	}

	.cont_two_top {
		width: 94%;
		padding: 4% 3% 0;
		margin: 0 auto;
		display: flex;
		justify-content: space-between;
	}

	.cont_two_top_le {
		/* width: 85%; */
		font-size: 30rpx;
		font-weight: 500;
		color: #333333;
		/* line-height: 2; */
		margin: 5rpx 0;
	}

	.cont_two_top_ri {
		/* width: 15%; */
		padding: 6rpx 10rpx;
		text-align: center;
		background: rgba(255, 19, 10, 0.2);
		font-size: 24rpx;
		border: 2rpx solid #FF130A;
		color: #FF130A;
		opacity: 0.6;
		border-radius: 8rpx;
	}

	.cont_two_top_ri1 {
		/* width: 15%; */
		text-align: center;
		/* line-height: 2; */
		padding: 6rpx 10rpx;
		background: #62ba8b;
		font-size: 24rpx;
		border: 2rpx solid #62ba8b;
		color: #fff;
		/* opacity: 0.6; */
		border-radius: 8rpx;
	}

	.cont_two_top_ri2 {
		/* width: 15%; */
		text-align: center;
		/* line-height: 2; */
		padding: 6rpx 10rpx;
		background: #FCD202;
		font-size: 24rpx;
		border: 2rpx solid #FCD202;
		color: #fff;
		/* opacity: 0.6; */
		border-radius: 8rpx;
	}

	.cont_two_text {
		font-size: 58rpx;
		text-align: center;
		font-weight: bold;
		/* color: red; */
		margin: 3% 0;
		line-height: 32rpx;
	}

	.cont_two_text2 {
		font-size: 30rpx;
		width: 100%;
		font-weight: 500;
		color: #333333;
		text-align: center;
		padding-bottom: 3%;
		/* line-height: 32rpx; */

	}

	.cont_two_text2 text {
		color: #FF130A;
	}

	.cont_two_bottom {
		width: 94%;
		padding: 30rpx 0rpx;
		margin: 0 auto;
		display: flex;
		border-top: 1rpx solid #E6E6E6;
	}

	.cont_two_bottom_le {
		flex: 1;
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		line-height: 32rpx;
	}

	.cont_two_bottom_ri {
		flex: 1;
		text-align: right;
	}

	.cont_two_bottom_ri image {
		width: 14rpx;
		height: 24rpx;
	}

	/* 外卖订单 */
	.cont_three {
		display: none;
		width: 94%;
		margin: 3% auto;
		background-color: #FFFFFF;
		border-radius: 18rpx;
	}

	.cont_three_top {
		width: 94%;
		padding: 4% 3% 0;
		margin: 0 auto;
		display: flex;
	}

	.cont_three_top_le {
		flex: 2;
		font-size: 30rpx;
		font-weight: 500;
		color: #D80204;
		line-height: 32rpx;
	}

	.cont_three_top_ri {
		flex: 1;
		text-align: right;
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		line-height: 32rpx;
	}

	.cont_three_text {
		padding: 2% 0 2% 3%;
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		line-height: 32rpx;
	}

	.cont_three_cen {
		width: 94%;
		padding: 0.5% 3%;
		margin: 0 auto;
		display: flex;
	}

	.cont_three_cen_le {
		flex: 2;
		font-size: 30rpx;
		font-weight: 500;
		color: #333333;
		line-height: 32rpx;
	}

	.cont_three_cen_ri {
		flex: 1;
		text-align: right;
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		line-height: 32rpx;
	}

	.cont_three_text2 {
		font-size: 24rpx;
		font-weight: 500;
		color: #D80204;
		padding: 0 3%;
		line-height: 32rpx;
	}

	.cont_three_bottom {
		width: 94%;
		padding: 3%;
		margin: 3% auto 0;
		display: flex;
		border-top: 1rpx solid #E6E6E6;
	}

	.cont_three_bottom_le {
		flex: 1;
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		line-height: 32rpx;
	}

	.cont_three_bottom_ri {
		flex: 1;
		text-align: right;
	}

	.cont_dis {
		display: block;
	}

	
	

	.order_success {
		width: 90%;
		margin: 0 auto;
		display: flex;
		height: 80upx;
	}

	.order_name {
		/* flex: 1; */
		display: flex;
		justify-content: left;
		align-items: center;
		font-weight: bold;
		font-size: 31rpx;
		letter-spacing: 1upx;
	}

	.order_data {
		flex: 1;
		color: #999999;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		font-size: 27rpx;
	}

	.zhizuo {
		background: rgba(255, 109, 157, 0.2);
		border: 1rpx solid #FF6D9D;
		opacity: 0.6;
		border-radius: 8rpx;
		color: #FF6D9D;
		padding: 7rpx 25rpx;
	}
</style>