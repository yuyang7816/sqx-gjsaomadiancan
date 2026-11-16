<template>
	<view style="padding-bottom: 80upx;">
		<view class=" u-flex padding-lr bg padding-top">
			<view class="u-m-r-10">
				<image :src="avatar" style="width: 100rpx;height: 100rpx;border-radius: 100rpx;"
					@click="goNav('/my/store/index')"></image>
			</view>
			<view class="u-flex-1 u-m-l-10" v-if="shopToken">
				<view class="u-font-18  text-bold">
					<view class="margin-left-sm margin-top-xs">
						<view class="flex align-end">{{shopName}}
						</view>
					</view>
				</view>

			</view>
			<view v-else class="text-xl u-p-l-20 text-bold" @click="goLogin('/pages/loginphone')">
				{{$t('login.home')}}
			</view>
		</view>

		
		<view class="flex align-center box justify-between text-center" @click="goNav('/pages/earnings?shopId='+shopId)">
			<view>
				{{$t('index.qian')}}
				<text class="text-bold text-xl">{{dataCentre.yearMoney?dataCentre.yearMoney:0}}</text>
				<view>{{$t('shop.yearsy')}}</view>
			</view>
			<view class="">
				<text class="text-bold text-xl">{{dataCentre.dayMoney ?dataCentre.dayMoney :0}}</text>
				<view>{{$t('shop.daysy')}}</view>
			</view>
			<view>
				<text class="text-bold text-xl">{{dataCentre.monthMoney ?dataCentre.monthMoney :0}}</text>
				<view>{{$t('shop.monthsy')}}</view>
			</view>
		</view>
		<view class="padding">
			<view class="text-bold text-lg margin-bottom-sm">{{$t('shop.syt')}}</view>
			<view class="orderbox " @click="goblack()">
				<view class="padding-top">
					{{$t('index.qian')}}<text class="text-bold text-xl">{{dataCentre.cashierYearMoney?dataCentre.cashierYearMoney:'0'}}</text>
					<view class="margin-top-xs">{{$t('shop.yearsy')}}</view>
				</view>
				<view class="padding-top">
					<text class="text-bold text-xl">{{dataCentre.cashierMonthMoney?dataCentre.cashierMonthMoney:'0'}}</text>
					<view class="margin-top-xs">{{$t('shop.daysys')}}</view>
				</view>
				<view class="padding-top">
					{{$t('index.qian')}}<text class="text-bold text-xl">{{dataCentre.cashierDayMoney?dataCentre.cashierDayMoney:'0'}}</text>
					<view class="margin-top-xs">{{$t('shop.monthsys')}}</view>
				</view>
			</view>
		</view>
		<view style="padding: 0upx 30upx;">
			<view class="text-bold text-lg margin-bottom-sm flex justify-between">
				<view>{{$t('order.name')}}</view>
				<view @click="goDetail(1)">
					<text style="color: #666666;font-size: 26upx;margin-right: 10upx;">{{$t('order.more')}}</text>
					<image src="/static/images/right1.png" style="width: 12upx;height: 20upx;"></image>
				</view>
			</view>
			<view class="orderbox ">
				<view class="padding-top">
					<text class="text-bold text-xl">{{orderStatus.forgeCount?orderStatus.forgeCount:0}}</text>
					<view class="margin-top-xs">{{$t('order.zhizuo')}}</view>
				</view>
				<view class="padding-top">
					<text class="text-bold text-xl">{{orderStatus.pickUpCount?orderStatus.pickUpCount:0}}</text>
					<view class="margin-top-xs">{{$t('order.dqc')}}</view>
				</view>
				<view class="padding-top">
					<text class="text-bold text-xl">{{orderStatus.finishCount?orderStatus.finishCount:0}}</text>
					<view class="margin-top-xs">{{$t('order.ywc')}}</view>
				</view>
				<view class="padding-top">
					<text class="text-bold text-xl">{{orderStatus.cancelCount?orderStatus.cancelCount:0}}</text>
					<view class="margin-top-xs">{{$t('order.yqx')}}</view>
				</view>
			</view>
		</view>
		<view class="padding">
			<view class="text-bold text-lg margin-bottom-sm flex justify-between">
				<view>{{$t('shop.mysp')}}</view>
				<view @click="goDetail(2)">
					<text style="color: #666666;font-size: 26upx;margin-right: 10upx;">{{$t('order.more')}}</text>
					<image src="/static/images/right1.png" style="width: 12upx;height: 20upx;"></image>
				</view>
			</view>
			<view class="orderbox ">
				<view class="padding-top">
					<text class="text-bold text-xl">{{goodsStatus.enableCount?goodsStatus.enableCount:0}}</text>
					<view class="margin-top-sm">{{$t('shop.sjz')}}</view>
				</view>
				<view class="padding-top">
					<text class="text-bold text-xl">{{goodsStatus.notEnableCount?goodsStatus.notEnableCount:0}}</text>
					<view class="margin-top-sm">{{$t('shop.yxj')}}</view>
				</view>
				<view class="padding-top" @click="bindFb()">
					<!-- <image src="../../static/images/index/add.png" style="width: 46upx;height: 46upx;"></image> -->
					<u-icon name="plus-circle-fill" color="#FF6D9D" size="46"></u-icon>
					<view class="margin-top-sm">{{$t('Commodity.add')}}</view>
				</view>

			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				avatar: '../../static/logo.png',
				page: 1,
				limit: 50,
				shopToken: '',
				shop: '',
				shopId:'',
				shopName:'',
				dataCentre: {},
				orderStatus:{},//订单状态
				goodsStatus:{},//商品状态
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title:this.$t('shop.titlename')
			})
		},
		onShow() {
			this.shopToken = uni.getStorageSync('shopToken')
			this.shopId = uni.getStorageSync('updataShopId')
			if(this.shopToken){
				this.getShopList()
			}else{
				this.dataCentre = {}
				this.orderStatus = {}
				this.goodsStatus = {}
				this.shopName = ''
				this.shopId = ''
				this.shopToken = ''
			}

		},
		methods: {
			goblack(){
				let shopToken = uni.getStorageSync('shopToken')
				if (shopToken) {
					uni.navigateTo({
						url: '/my/payBlack/payBlack'
					})
				} else {
					uni.showModal({
						title: this.$t('index.wxts'),
						content: this.$t('login.tit'), //您还未登录,请先登录
						confirmText:this.$t('index.sure'),
						cancelText:this.$t('index.qx'),
						success: function(res) {
							if (res.confirm) {
								// console.log('用户点击确定');
								uni.navigateTo({
									url: '/pages/loginphone'
								})
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					})
				}
			},
			//获取商品统计
			getGoodsStatus(){
				this.$Request.getA('/admin/order/goodStatusCount?shopId='+this.shopId).then(res=>{
					if(res.code==0){
						this.goodsStatus = res.data
					}else{
						uni.showToast({
							title:res.msg,
							icon:'none'
						})
					}
				})
			},
			//获取订单统计
			getOrderStatus(){
				this.$Request.getA('/admin/order/shopOrderStatistics?shopId='+this.shopId).then(res=>{
					if(res.code==0){
						this.orderStatus = res.data
					}else{
						uni.showToast({
							title:res.msg,
							icon:'none'
						})
					}
				})
			},
			// 店铺列表
			getShopList() {
				let data = {
					page: this.page,
					limit: this.limit,
					shopName: this.shopName
				}
				this.$Request.getA("/admin/goods/selectAllShop", data).then(res => {
					if (res.code == 0) {
						let shopList = res.data.list
						this.shop = shopList[0]
						if(this.shopId) {
							shopList.forEach(res=>{
								if(res.shopId == this.shopId) {
									this.shop = res
								}
							})
						}
						uni.setStorageSync('shopName',this.shop.shopName)
						this.shopName = this.shop.shopName
						uni.setStorageSync('updataShopId',this.shop.shopId)
						this.shopId = this.shop.shopId
						this.getDataCentre()
						this.getOrderStatus()
						this.getGoodsStatus()
					}
				});
			},
			// 收入
			getDataCentre() {
				this.$Request.getA("/admin/dataCentre/selectShopMoney?shopId=" + this.shopId).then(res => {
					if (res.code == 0) {
						this.dataCentre = res.data
					}
				});
			},
			goDetail(index) {
				// #ifdef MP-WEIXIN
				if (uni.getStorageSync('sendindexMsg')) {
					uni.requestSubscribeMessage({
						tmplIds: this.arr,
						success(re) {
							// console.log(re,'**********')
							var datas = JSON.stringify(re);
							if (datas.indexOf("accept") != -1) {
								console.log(re)
							}
						},
						fail: (res) => {
							console.log(res)
						}
					})
				}
				// #endif
				if(uni.getStorageSync('shopToken')){
					if (index == 1) {
						uni.switchTab({
							url: '/pages/order'
						})
					} else if (index == 2) {
						uni.navigateTo({
							url: '/pages/goodlist?shopId='+uni.getStorageSync('updataShopId')
						})
					}
				}else{
					uni.showModal({
						title: this.$t('index.wxts'),
						content: this.$t('login.tit'), //您还未登录,请先登录
						confirmText:this.$t('index.sure'),
						cancelText:this.$t('index.qx'),
						success: function(res) {
							if (res.confirm) {
								// console.log('用户点击确定');
								uni.navigateTo({
									url: '/pages/loginphone'
								})
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					})
				}
				
			},
			bindFb() {
				let shopToken = uni.getStorageSync('shopToken')
				if (shopToken) {
					uni.navigateTo({
						url: '/pages/setAddGoods'
					})

				} else {
					uni.showModal({
					title: this.$t('index.wxts'),
					content: this.$t('login.tit'), //您还未登录,请先登录
					confirmText:this.$t('index.sure'),
					cancelText:this.$t('index.qx'),
						success: function(res) {
							if (res.confirm) {
								// console.log('用户点击确定');
								uni.navigateTo({
									url: '/pages/loginphone'
								})
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					})
				}
			},
			//时间弹框开关
			bindData(index) {
				if (index == 1) {
					this.startshow = true
				} else if (index == 2) {
					this.endshow = true

				}
			},
			//开始时间
			startData(e) {

				this.startTime = e.year + '-' + e.month + '-' + e.day
			},
			// 结束时间
			endData(e) {
				this.endTime = e.year + '-' + e.month + '-' + e.day

				this.getFb()
			},
			goNav(e, name) {
				if (this.shopToken) {
					// #ifdef MP-WEIXIN
					if (uni.getStorageSync('sendindexMsg')) {
						uni.requestSubscribeMessage({
							tmplIds: this.arr,
							success(re) {
								// console.log(re,'**********')
								var datas = JSON.stringify(re);
								if (datas.indexOf("accept") != -1) {
									console.log(re)
								}
							},
							fail: (res) => {
								console.log(res)
							}
						})
					}
					// #endif
					uni.navigateTo({
						url: e
					})
				} else {
					uni.showModal({
						title: this.$t('index.wxts'),
						content: this.$t('login.tit'), //您还未登录,请先登录
						confirmText:this.$t('index.sure'),
						cancelText:this.$t('index.qx'),
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: '/pages/loginphone'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			},
			goLogin(e) {
				uni.navigateTo({
					url: e
				})
			},

		}
	}
</script>

<style lang="scss">
	page {
		background-color: #FFFFFF;
	}

	.bg {
		background-color: #FFFFFF;
	}

	.box {
		width: 690upx;
		margin: 0 auto;
		height: 120rpx;
		background: #FF6D9E;
		border-radius: 16upx 16upx 0px 0px;
		color: #FFFFFF;
		padding: 0upx 20upx;
		margin-top: 20upx;
	}

	.orderbox {
		width: 690upx;
		margin: 0 auto;
		background: #FAFAFA;
		border-radius: 16upx;
		display: flex;
		align-items: center;
		justify-content: space-between;
		text-align: center;
		flex-wrap: wrap;
		padding: 0upx 30upx 30rpx;
	}
</style>
