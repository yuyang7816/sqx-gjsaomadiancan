<template>
	<view class="pages" style="position: relative;">
		<view style="position: fixed;top: 0;left: 0;right: 0;z-index: 9999;" class="bg">
			<view class="flex bg justify-between">
				<view class="flex justify-around" style="width: 50%;">
					<view @click="switchTab(1)" :class="orderType==1? 'select':''" class="tabBtn">
						到店取餐
						<view :class="orderType==1? 'active':''"></view>
					</view>
					<view @click="switchTab(2)" :class="orderType==2? 'select':''" class="tabBtn">
						外卖配送
						<view :class="orderType==2? 'active':''"></view>
					</view>
				</view>
				<view class="shaix" @click="bindopen()">
					条件筛选
					<image src="../static/images/order/down.png" style="width:15upx;height:9upx;"></image>
				</view>
			</view>
			<view>
				<u-tabs v-if="orderType == 1" active-color="#FF6D9D" :list="qucanList" :is-scroll="false"
					:current="current" @change="change"></u-tabs>
				<u-tabs v-if="orderType == 2" active-color="#FF6D9D" :list="waimaiList" :is-scroll="false"
					:current="current1" @change="change1">
				</u-tabs>
			</view>

		</view>

		<!-- 全部订单 -->
		<view class="cont_one">
			<view v-for="(item,index) in orderList" :key='index'
				@click="goNav('/shop/orderDet?orderNumber='+item.orderNumber)">
				<view class="cont">
					<view class="flex justify-between padding align-center">
						<view class="text-red text-lg" v-if="item.status == 6">制作中</view>
						<view class="text-green text-lg" v-if="item.status == 3&&item.orderType==1">待取餐</view>
						<view class="text-green text-lg" v-if="item.status == 3&&item.orderType==2">待配送</view>
						<view class="text-grey text-lg" v-if="item.status == 4">已完成</view>
						<view class="text-gray text-lg" v-if="item.status == 5">已取消</view>
						<view class="text-gray text-sm">{{item.payTime}}</view>
						
					</view>
					<u-line color="#E6E6E6" />
					<view class="text-red text-xl padding-lr padding-top" v-if="item.orderType==1"><text style="font-size: 30upx;">取餐号：</text>{{item.orderCode}}</view>

					<view class="padding-bottom padding-lr margin-top-sm" v-for="(ite,ind) in item.orderGoodsList"
						:key='ind'>
						<view class="text-black text-lg">{{ite.goodsName}}</view>
						<view class="flex justify-between align-center text-gray text-sm margin-top-xs">
							<view>{{ite.skuMessage}}</view>
							<view class="text-gray text-sm">X{{ite.goodsNum}}</view>
						</view>
					</view>
					<u-line color="#E6E6E6" />
					<view class="padding-bottom padding-lr margin-top-sm text-gray">
						<view>手机号：{{item.phone}}</view>
						<view>订单号：{{item.orderNumber}}</view>
					</view>
					
					<u-line color="#E6E6E6" />
					<view class="flex">
						<view class="flex  align-center padding">
							<view class="text-gray text-sm">实收：</view>
							<view class="text-sm text-black text-bold">¥<text class="text-xl">{{item.payMoney}}</text>
							</view>
						</view>
						<view class="flex-sub flex justify-end">
							<view class="flex justify-end padding-sm u-border-top" v-if="item.status == 6||item.status == 3">
								<view class="btn" @click.stop="refund(item)">退款</view>
							</view>
							<view class="flex justify-end padding-sm u-border-top" v-if="item.status == 6">
								<view class="btnCm" @click.stop="finish(item)">制作完成</view>
							</view>
							<!-- <view class="flex justify-end padding-sm u-border-top" v-if="item.status == 3&&item.orderType==2">
								<view class="btn" @click.stop="refund(item)">退款</view>
							</view> -->
							<view class="flex justify-end padding-sm u-border-top" v-if="item.status == 3&&item.orderType==1">
								<view class="btnCm" @click.stop="peisong(item)">制作完成</view>
							</view>
							<view class="flex justify-end padding-sm u-border-top" v-if="item.status == 3&&item.orderType==2">
								<view class="btnCm" @click.stop="peisong(item)">制作完成</view>
							</view>
							<view class="flex justify-end padding-sm u-border-top" v-if="item.status == 3||item.status == 4||item.status == 6&&item.orderType==2">
								<view class="btnCm" @click.stop="xiaoPiao(item)">打印小票</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 筛选弹框 -->
		<view class="box" v-if="show">
			<view class="popbox" @click.stop="shop = false">
				<view class="padding">
					<view>
						<view>订单号</view>
						<view>
							<input type="number" v-model="orderNumber" class="impute padding-lr" placeholder="请输入订单号" />
						</view>
					</view>
					<view>
						<view>手机号</view>
						<view>
							<input type="number" v-model="phone" class="impute padding-lr" placeholder="请输入手机号码" />
						</view>
					</view>
					<view>
						<view>昵称</view>
						<view>
							<input type="text" v-model="userName" class="impute padding-lr" placeholder="请输入昵称" />
						</view>
					</view>
				</view>
				<view class="btns" @click.stop="bindx()">查询</view>
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
				show: false,
				page: 1,
				limit: 10,
				phone: '',
				userName: '',
				orderNumber: '',
				shopId: '',
				orderType: 1,
				orderList: [],
				status: '',
				xcxSelect: '',
				current: 0,
				current1: 0,
				qucanList: [{
						name: '全部'
					},
					{
						name: '制作中'
					},
					{
						name: '待取餐'
					},
					{
						name: '已完成'
					},
					{
						name: '已取消'
					}
				],
				waimaiList: [{
						name: '全部'
					},
					{
						name: '制作中'
					},
					{
						name: '待配送'
					},
					{
						name: '已完成'
					},
					{
						name: '已取消'
					}
				]
			};
		},
		onLoad(option) {
			// this.shopId = uni.getStorageSync('shopId')
			this.shopId = option.shopId
			if (option.status) {
				this.change(option.status)
			}
			uni.showLoading({
				title: '加载中...'
			})
		},
		onShow() {
			this.getOrderList()
			this.clearOrderMsg()
		},
		methods: {
			peisong(e) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认开始配送吗?',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId,
								orderNumber: e.orderNumber,
								status: 4
							}
							that.$Request.postJsonA("/admin/order/updateOrder", data).then(res => {
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
			// 清除订单未读消息
			clearOrderMsg() {
				let data = {
					shopIds: this.shopId
				}
				this.$Request.postA("/admin/ordermessage/allcheckOrderMessage", data).then(res => {
					if (res.code == 0 && res.data > 0) {

					}
				});
			},
			bindopen() {
				console.log(111)
				this.show = !this.show
			},
			bindx() {
				this.show = false
				this.getOrderList()
				this.phone = ''
				this.userName = ''
				this.orderNumber = ''
			},
			refund(e) {
				let that = this
				console.log(e)
				uni.showModal({
					title: '提示',
					content: '确认退款吗?',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId
							}
							that.$Request.postA("/admin/order/adminCancelOrder", data).then(res => {
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
			finish(e) {
				let that = this
				console.log(e)
				uni.showModal({
					title: '提示',
					content: '确认订单完成吗?',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId,
								orderNumber: e.orderNumber,
								status: 3
							}
							that.$Request.postJsonA("/admin/order/updateOrder", data).then(res => {
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
			// 补打小票
			xiaoPiao(e){
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认补打小票吗?',
					success: function(re) {
						if (re.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId,
								// orderNumber: e.orderNumber,
								// status: 8
							}
							that.$Request.post("/app/order/print", data).then(res => {
								if (res.code == 0) {
									// that.page = 1
									// that.getOrderList()
								}else{
									that.$queue.showToast(res.msg)
								}
							});
						} else if (re.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			switchTab(e) {
				this.page = 1
				this.orderType = e
				this.current = 0
				this.current1 = 0
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
					case '1':
						this.status = 6 //制作中
						break;
					case '2':
						this.status = 3 //制作中
						break;
					case '3':
						this.status = 4 //制作中
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
				let data = {
					page: this.page,
					limit: this.limit,
					phone: this.phone,
					userName: this.userName,
					orderNumber: this.orderNumber,
					shopId: this.shopId,
					status: this.status,
					orderType: this.orderType
				}
				this.$Request.getA("/admin/order/selectAllOrder", data).then(res => {
					if (res.code == 0) {
						// this.msgData = res.data
						if (this.page == 1) {
							this.orderList = res.data.list
						} else {
							this.orderList = [...this.orderList, ...res.data.list]
						}
						this.totalCount = res.data.totalCount
					}
					uni.stopPullDownRefresh();
					uni.hideLoading()

				});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getOrderList();
			
			if(this.totalCount == this.orderList.length) {
				uni.showToast({
					title: '已经到底了~',
					icon: 'none'
				})
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getOrderList();
		},
	}
</script>

<style scoped>
	.active {
		width: 82rpx;
		height: 6rpx;
		background: #333333;
		margin-left: 12rpx;
	}

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
		padding: 10rpx 20rpx;
		text-align: center;
		/* background: rgba(255, 19, 10, 0.2); */
		font-size: 28rpx;
		border: 2rpx solid #8BC6FF;
		color: #333333;
		opacity: 0.6;
		border-radius: 8rpx;
	}

     .btnCm {
		padding: 10rpx 20rpx;
		text-align: center;
		background: #FF6D9D;
		font-size: 28rpx;
		border: 2rpx solid #FF6D9D;
		color: #FFFFFF;
		opacity: 0.6;
		border-radius: 8rpx;
	}
	.tabBtn {
		/* background-color: #f6f6fa; */
		height: 60rpx;
		line-height: 60rpx;
		color: #000000;
		font-size: 31rpx;
		font-weight: bold;
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

	.select {
		color: #000000;
		font-size: 31rpx;
		font-weight: bold;
		background-color: #fff;
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
		border-bottom: 14rpx solid #FF6D9D;
	}

	/* 内容 */
	/* 全部订单 */
	.cont_one {
		margin-top: 160rpx;
	}

	.cont_one image {
		width: 80%;
		height: 353rpx;
	}

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
		width: 85%;
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
		background: #FF6D9D;
		font-size: 24rpx;
		border: 2rpx solid #FF6D9D;
		color: #fff;
		/* opacity: 0.6; */
		border-radius: 8rpx;
	}

	.cont_two_text {
		font-size: 58rpx;
		text-align: center;
		font-weight: bold;
		color: red;
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
		padding: 3%;
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

	.box {
		width: 100%;
		height: 100vh;
		background-color: rgba(0, 0, 0, 0.2);

		/* opacity: 0.2; */
		position: absolute;
		top: 40px;
		z-index: 999
	}

	.popbox {
		width: 749upx;
		/* height: 764upx; */
		background: #FFFFFF;
		padding-bottom: 72rpx;
		z-inde: 999;
		position: fixed;
		top: 66rpx;
		left: 0;
		right: 0;
	}

	.impute {
		background: #F2F2F2;
		height: 80rpx;
		margin: 20rpx 0;
	}

	.btns {
		width: 690upx;
		height: 88upx;
		background:#FF6D9D;
		box-shadow: 0upx 10upx 20upx 0upx #FFD9B3;
		border-radius: 16upx;
		text-align: center;
		line-height: 88rpx;
		font-size: 32rpx;
		font-weight: bold;
		margin: 0 auto;
		color: #FFFFFF;
	}
</style>
