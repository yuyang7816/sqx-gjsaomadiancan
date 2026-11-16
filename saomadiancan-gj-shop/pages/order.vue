<template>
	<view class="pages">
		<u-sticky>
			<view class="tabbox">
				<view class="tabbox_top ">
					<view class="flex justify-around" style="width: 75%;">
						<u-tabs :list="list" :is-scroll="true" :current="orderType" @change="changeTab"
							active-color="#FF6D9D"></u-tabs>
					</view>
					<view class="shaix" @click="bindopen()">
						{{$t('order.tjsx')}}
						<image src="../static/images/order/down.png" style="width:15upx;height:9upx;"></image>
					</view>
				</view>
				<view>
					<u-tabs v-if="orderType == 0" active-color="#FF6D9D" :list="qucanList" :is-scroll="true"
						:current="current" @change="change"></u-tabs>
					<u-tabs v-if="orderType == 1||orderType == 2" active-color="#FF6D9D" :list="waimaiList"
						:is-scroll="true" :current="current1" @change="change1">
					</u-tabs>
				</view>

			</view>
		</u-sticky>

		<!-- 全部订单 -->
		<view>
			<view v-for="(item,index) in orderList" :key='index'
				@click="goNav('/pages/orderDet?orderNumber='+item.orderNumber)">
				<view class="cont">
					<view class="flex justify-between padding align-center">
						<view class="text-red text-lg" v-if="item.status == 6">{{$t('order.zhizuo')}}</view>
						<view class="text-green text-lg" v-if="item.status == 3&&item.orderType==1">{{$t('order.dqc')}}
						</view>
						<view class="text-green text-lg" v-if="item.status == 3&&item.orderType==2">{{$t('order.dps')}}
						</view>
						<view class="text-grey text-lg" v-if="item.status == 2 || item.status == 1 &&item.orderType==3">
							{{$t('order.dzf')}}
						</view>
						<view class="text-grey text-lg" v-if="item.status == 4">{{$t('order.ywc')}}</view>
						<view class="text-gray text-lg" v-if="item.status == 5">{{$t('order.yqx')}}</view>
						<view class="text-gray text-sm ">{{item.createTime}}</view>

					</view>
					<u-line color="#E6E6E6" />
					<view class="text-red text-xl padding-lr padding-top" v-if="item.orderType==1"><text
							style="font-size: 30upx;">{{$t('order.qchm')}}：</text>{{item.orderCode}}</view>

					<view class="padding-bottom padding-lr margin-top-sm" v-for="(ite,ind) in item.orderGoodsList"
						:key='ind'>
						<view class="text-black text-lg flex justify-between align-center">
							{{ite.goodsName}}
							<view class="" v-if="ite.status==2" style="font-size: 28rpx;color: #aaaaaa;">
								{{$t('order.ytk')}}
							</view>
							<block v-else>
								<view class=""
									v-if="item.orderGoodsList.length>1 && item.status != 5 && item.status != 4"
									@click.stop="tuiMoney(item.orderId,ite.id)"
									style="font-size: 28rpx;padding: 6rpx 15rpx;border-radius: 8rpx; border: 1rpx solid #333333;">
									{{$t('order.tk')}}
								</view>
							</block>
						</view>
						<view class="flex justify-between align-center text-gray text-sm margin-top-xs">
							<view v-if="ite.skuMessage">{{ite.skuMessage}}</view>
							<view class="text-gray text-sm">X{{ite.goodsNum}}</view>
						</view>
						<view v-if="ite.goodRemarks" class="text-gray text-sm margin-top-xs">
							<view>{{$t('payorder.bz')}}：{{ite.goodRemarks}}</view>
						</view>

					</view>
					<u-line color="#E6E6E6" />
					<view class="padding-bottom padding-lr margin-top-sm text-gray">
						<view v-if="item.phone">{{$t('order.ohones')}}：{{item.phone}}</view>
						<view>{{$t('payorder.ddbh')}}：{{item.orderNumber}}</view>
						<view v-if="item.orderRemarks">{{$t('order.bz')}}：{{item.orderRemarks}}</view>

					</view>

					<u-line color="#E6E6E6" />
					<view class="flex  align-center padding-sm">
						<view class="text-gray text-sm">{{$t('order.ss')}}：</view>
						<view class="text-sm text-black text-bold">{{$t('index.qian')}}<text
								class="text-xl">{{item.payMoney?item.payMoney:item.orderMoney}}</text>
						</view>
					</view>

					<view class="flex-sub flex flex-wrap justify-end padding-bottom">
						<view class="flex justify-end padding-lr-sm " v-if="item.status == 6||item.status == 3">
							<view class="btn" @click.stop="refund(item)">{{$t('order.tk')}}</view>
						</view>
						<view class="flex justify-end padding-lr-sm " v-if="item.status == 6">
							<view class="btnCm" @click.stop="finish(item)">{{$t('order.zzwc')}}</view>
						</view>

						<view class="flex justify-end padding-lr-sm " v-if="item.status == 3&&item.orderType==1">
							<view class="btnCm" @click.stop="peisong(item)">{{$t('order.wcdd')}}</view>
						</view>
						<view class="flex justify-end padding-lr-sm " v-if="item.status == 3&&item.orderType==2">
							<view class="btnCm" @click.stop="peisong(item)">{{$t('order.wcdd')}}</view>
						</view>
						<block v-if="item.orderType != 3">
							<view class="flex justify-end padding-lr-sm "
								v-if="item.status == 3||item.status == 4||item.status == 6&&item.orderType==2">
								<view class="btnCm" @click.stop="xiaoPiao(item)">{{$t('order.dyxp')}}</view>
							</view>
						</block>
						<block v-else>
							<view class="flex justify-end padding-lr-sm "
								v-if="item.status == 1 || item.status == 3||item.status == 4">
								<view class="btnCm" @click.stop="xiaoPiao(item)">{{$t('order.dyxp')}}</view>
							</view>
							<!-- 到店堂食 完成按钮 -->
							<view class="flex justify-end padding-lr-sm " v-if="item.status == 1 ">
								<view class="btnCm" @click.stop="shopwancheng(item)">{{$t('order.wcdd')}}</view>
							</view>
						</block>
					</view>
				</view>
			</view>
		</view>

		<!-- 筛选弹框 -->
		<u-popup v-model="show" mode="bottom" border-radius="14" @close="show =false">
			<view class="popbox">
				<view class="padding">
					<view>
						<view>{{$t('payorder.ddbh')}}</view>
						<view>
							<input type="number" v-model="orderNumber" class="impute padding-lr"
								:placeholder="$t('payorder.pladdbh')" />
						</view>
					</view>
					<view>
						<view>{{$t('order.phone')}}</view>
						<view>
							<input type="number" v-model="phone" class="impute padding-lr"
								:placeholder="$t('order.plaphone')" />
						</view>
					</view>
					<view>
						<view>{{$t('user.nc')}}</view>
						<view>
							<input type="text" v-model="userName" class="impute padding-lr"
								:placeholder="$t('user.planc')" />
						</view>
					</view>
				</view>
				<view class="btns" @click.stop="bindx()">{{$t('order.chaxun')}}</view>
			</view>
		</u-popup>


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
						name: '' //待配送
					},
					{
						name: '' //已完成
					},
					{
						name: '' //已取消
					}
				],
				list: [{
					name: '', //到店取餐
					id: 1
				}, {
					name: '', //外卖配送
					id: 2
				}, {
					name: '', //到店堂食
					id: 3
				}],
				orderType: 0,
			};
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title: this.$t('order.title')
			})

			this.list[0].name = this.$t('order.daodian'); //到店取餐
			this.list[1].name = this.$t('order.wmps'); //外卖配送
			this.list[2].name = this.$t('order.ddts'); //到店堂食


			this.qucanList[0].name = this.$t('order.all'); //全部
			this.qucanList[1].name = this.$t('order.zhizuo'); //制作中
			this.qucanList[2].name = this.$t('order.dqc'); //待取餐
			this.qucanList[3].name = this.$t('order.ywc'); //已完成
			this.qucanList[4].name = this.$t('order.yqx'); //已取消

			this.waimaiList[0].name = this.$t('order.all'); //全部
			this.waimaiList[1].name = this.$t('order.zhizuo'); //制作中
			this.waimaiList[2].name = this.$t('order.dps'); //待配送
			this.waimaiList[3].name = this.$t('order.ywc'); //已完成
			this.waimaiList[4].name = this.$t('order.yqx'); //已取消

		},
		onShow() {

			if (uni.getStorageSync('shopToken')) {
				uni.showLoading({
					title: this.$t('loadingText.jzz') //'加载中...'
				})
				this.shopId = uni.getStorageSync('updataShopId')

				this.getOrderList()
				this.clearOrderMsg()
			} else {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.login'), //请登录后查看
					confirmText: that.$t('order.qlogin'), //'去登录'
					cancelText: that.$t('index.qx'),
					showCancel: false,
					complete(ret) {
						if (ret.confirm) {
							uni.switchTab({
								url: '/pages/index'
							})
						}
					}
				})
			}

		},
		methods: {
			// 到底堂食 完成按钮
			shopwancheng(ite) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plawcm'), //确认完成订单吗
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
					complete(ret) {
						if (ret.confirm) {
							that.$Request.getA('/app/order/completeOrder', {
								orderId: ite.orderId
							}).then(re => {
								if (re.code == 0) {
									uni.showToast({
										title: that.$t('order.wcdd') //'完成订单',
									})
									that.page = 1
									that.getOrderList()
								} else {
									uni.showToast({
										title: re.msg,
										icon: 'none'
									})
								}
							})
						}
					}
				})
			},
			//部分退款
			tuiMoney(orderId, id) {
				let that = this
				let data = {
					orderId: orderId,
					orderGoodsId: id
				}
				this.$Request.getA('/admin/order/selectCancelMoney', data).then(res => {
					if (res.code == 0) {
						uni.showModal({
							title: that.$t('index.wxts'),
							content: that.$t('order.plawkje') + that.$t('index.qian') + res.data + '，' +
								that.$t('order.plaqrtkm'), //'退款金额为¥' + res.data + '，确定退款吗？'
							confirmText: that.$t('index.sure'),
							cancelText: that.$t('index.qx'),
							complete(ret) {
								if (ret.confirm) {
									that.$Request.postA('/admin/order/adminCancelOrder', data).then(re => {
										if (re.code == 0) {
											uni.showToast({
												title: that.$t('order.ytk') //'已退款',
											})
											that.page = 1
											that.getOrderList()
										} else {
											uni.showToast({
												title: re.msg,
												icon: 'none'
											})
										}
									})
								}
							}
						})
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			peisong(e) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plawcm'), //确认完成订单吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
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
									uni.showToast({
										title: that.$t('order.ywc') //'已完成'
									})
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
					title: that.$t('index.wxts'),
					content: that.$t('order.platkm'), //确认退款吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId
							}
							that.$Request.postA("/admin/order/adminCancelOrder", data).then(res => {
								if (res.code == 0) {
									uni.showToast({
										title: that.$t('order.ytk'), // '已退款',
										icon: 'none'
									})
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
					title: that.$t('index.wxts'),
					content: that.$t('order.plaqrzzwcm'), //确认制作完成吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
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
			xiaoPiao(e) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plabdco'), //确认补打小票吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
					success: function(re) {
						if (re.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId,
							}
							that.$Request.post("/app/order/print", data).then(res => {
								if (res.code == 0) {

								} else {
									that.$queue.showToast(res.msg)
								}
							});
						} else if (re.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			changeTab(index) {
				this.orderType = index
				this.page = 1
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
				uni.removeStorageSync('status')
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
					phone: this.phone,
					userName: this.userName,
					orderNumber: this.orderNumber,
					shopId: this.shopId,
					status: this.status,
					orderType: type,
					isPay: 1
				}
				this.$Request.getA("/admin/order/selectAllOrder", data).then(res => {
					if (res.code == 0) {

						if (this.page == 1) {
							this.orderList = res.data.list
						} else {
							this.orderList = [...this.orderList, ...res.data.list]
						}
						this.totalCount = res.data.totalCount
					}
					this.$forceUpdate()
					uni.stopPullDownRefresh();
					uni.hideLoading()

				});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getOrderList();

			if (this.totalCount == this.orderList.length) {
				uni.showToast({
					title: this.$t('index.ddl') + '~', //已经到底了
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

<style scoped lang="scss">
	::v-deep .u-tabs {
		width: 100% !important;
	}


	.bg {
		background-color: #FFFFFF;
	}

	/* tab 顶部*/
	.tabbox {
		background-color: #FFFFFF;

		.tabbox_top {
			display: flex;
			align-items: center;
			justify-content: space-between;

			.shaix {
				width: 25%;
				text-align: center;
				display: flex;
				align-items: center;
				justify-content: center;
				letter-spacing: 4rpx;
				font-size: 27rpx;
			}
		}
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
		padding: 10rpx 10rpx;
		text-align: center;
		background: #FF6D9D;
		font-size: 28rpx;
		border: 2rpx solid #FF6D9D;
		color: #FFFFFF;
		opacity: 0.6;
		border-radius: 8rpx;
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

	.popbox {
		width: 100%;
		background: #FFFFFF;
		padding-bottom: 72rpx;
		z-inde: 999;
	}

	.impute {
		background: #F2F2F2;
		height: 80rpx;
		margin: 20rpx 0;
	}

	.btns {
		width: 690upx;
		height: 88upx;
		background: #FF6D9D;
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