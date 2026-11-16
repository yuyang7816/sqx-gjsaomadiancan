<template>
	<view class="pages">
		<!-- 用户信息 -->
		<view class="header_all">
			<view class="flex justify-between" v-if="shopToken">
				<view class="flex justify-between align-center padding-left">
					<view class="">
						<image class="round" :src="avatar" mode="" style="width: 100rpx;height: 100rpx;"></image>
					</view>
					<view class="flex-sub margin-left-xs">
						<view class="flex align-center" @click="show=true">
							<view class="text-xl text-bold margin-right-xs">{{shop.shopName?shop.shopName:'匿名'}}</view>
							<image src="../static/images/diancan/xia.png" style="width: 20rpx;height: 10rpx;"></image>
						</view>
						<view class="text-sm margin-top-xs">商家已入驻</view>
					</view>
				</view>
				<view @click="reviewImg()" class="ma">店铺二维码</view>
			</view>
			<view class="flex justify-between align-center padding-left" v-else>
				<view class="">
					<image class="round" src="../static/logo.png" mode="" style="width: 100rpx;height: 100rpx;">
					</image>
				</view>
				<view class="flex-sub">
					<view @click="goNav('/shop/loginphone')">
						<view class="text-xxl text-bold margin-left">登录</view>
					</view>
				</view>
			</view>
		</view>

		<view style="position: relative;top: -113rpx;z-index: 9;">
			<view class="header">
				<view class="text-xl text-bold padding-lr padding-top-sm">账户资金</view>
				<view class="header_bottom clear" @click="goNav('/shop/earnings?shopId='+shop.shopId)">
					<view class="header_bottom_sty">
						<view class="header_text1"><text
								class="text-df">￥</text>{{dataCentre.yearMoney?dataCentre.yearMoney:0}}</view>
						<view class="header_text2">本年收益</view>
					</view>
					<view class="header_bottom_sty">
						<view class="header_text1"><text
								class="text-df">￥</text>{{dataCentre.dayMoney ?dataCentre.dayMoney :0}}</view>
						<view class="header_text2">今日收益</view>
					</view>
					<view class="header_bottom_sty">
						<view class="header_text1"><text
								class="text-df">￥</text>{{dataCentre.monthMoney ?dataCentre.monthMoney :0}}</view>
						<view class="header_text2">本月收益</view>
					</view>
				</view>
			</view>

			<!-- 订单中心 -->
			<view class="bg margin-sm radius">
				<view class="flex justify-between padding-top padding-lr">
					<view style="font-size: 28upx;font-weight: bold;">我的订单</view>
					<view class="flex align-center" @click="Goorder()">
						<text class="text-sm" style="color:#999999;margin-top:-4rpx;">查看更多</text>
						<view class="list_ris margin-left-xs">
							<image src="../static/images/order/right1.png" mode=""></image>
						</view>
					</view>
				</view>

				<view class="flex justify-around text-center">
					<!-- 待付款 -->
					<!-- <view class="padding-lr padding-tb" @click="goNavOrder(1)">
						<image src="../static/images/my/order1.png" style="width:60upx;height: 60upx;" mode="">
						</image>
						<view class="text-df" style="line-height: 50upx;">待付款</view>
					</view> -->
					<!-- 进行中 -->
					<view class="padding-lr padding-tb" @click="goNavOrder(1)">
						<image src="../static/images/my/order2.png" style="width: 60upx;height: 60upx;" mode="">
						</image>
						<view class="text-df" style="line-height: 50upx;">制作中</view>
					</view>
					<!-- 已完成 -->
					<view class="padding-lr padding-tb" @click="goNavOrder(2)">
						<image src="../static/images/my/order3.png" style="width: 60upx;height: 60upx;" mode="">
						</image>
						<view class="text-df" style="line-height: 50upx;">待取餐</view>
					</view>
					<!-- 待评价 -->
					<view class="padding-lr padding-tb" @click="goNavOrder(3)">
						<image src="../static/images/my/order4.png" style="width: 60upx;height: 60upx;" mode="">
						</image>
						<view class="text-df" style="line-height: 50upx;">已完成</view>
					</view>
				</view>
			</view>

			<!-- 列表 -->
			<view class="list_all">
				<view class="list" @click="goNav('/shop/publish?shopId='+shop.shopId)">
					<image src="../static/images/my/shangpu.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">店铺中心</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/shop/goodlist?shopId='+shop.shopId)">
					<image src="../static/images/my/shangpin.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">商品列表</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/shop/earnings?shopId='+shop.shopId)">
					<image src="../static/images/my/money.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">收益统计</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/my/setting/customer')">
					<image src="../static/images/my/kefu.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">联系客服</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/my/setting/index')">
					<image src="../static/images/my/set.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">设置中心</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
			</view>
		</view>

		<u-popup v-model="show" mode="center" width="680" border-radius="14">
			<view class="padding">
				<view url="../foodtwo/foodtwo" class="nearby_address_active" v-for="(item,index) in shopList"
					:key='index' @click="selectShop(item)">
					<view class="nearby_address_le">
						<view class="nearby_text2">
							<text class="nearby_text2_">{{item.shopName}}</text>
							<view class="flex align-center">
							</view>
						</view>
						<view class="nearby_text3">{{item.detailedAddress}}</view>
						<view class="nearby_text3">营业时间：{{item.openTime}} - {{item.closeTime}}</view>
					</view>
				</view>
			</view>
		</u-popup>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				show: false,
				page: 1,
				limit: 10,
				shopToken: '',
				shopName: '',
				shopList: [],
				shop: '',
				erweimaImg: '', //店铺二维码
				dataCentre: '',

				userName: '',
				avatar: '../static/logo.png',
				msgData: {
					userIntegral: 0,
					countCoupon: 0,
					userMoney: 0
				},
				xcxSelect: '否',
				Audio: uni.createInnerAudioContext(),
				updataShopId: ''
			}
		},

		onLoad() {
			this.shopToken = uni.getStorageSync('shopToken') ? uni.getStorageSync('shopToken') : ''
			this.userName = uni.getStorageSync('userName')
			if (this.shopToken) {
				this.getShopList()
				
			} else {
				this.userName = ''
				this.msgData.userIntegral = 0
				this.msgData.countCoupon = 0
				this.msgData.userMoney = 0
			}
		},
		onShow() {
			let that= this
			that.shopToken = uni.getStorageSync('shopToken') ? uni.getStorageSync('shopToken') : ''
			that.updataShopId = uni.getStorageSync('updataShopId')?uni.getStorageSync('updataShopId'):''
			console.log(that.updataShopId)
			if (that.updataShopId) {
				that.getShopList()
				
			} else {
				this.userName = ''
				this.msgData.userIntegral = 0
				this.msgData.countCoupon = 0
				this.msgData.userMoney = 0
			}
			setTimeout(function() {
				that.getOrderMsg()
			},10000)
		},
		methods: {
			// 播放音频
			palyAudio() {
				this.Audio.src = 'https://pw.xianmxkj.com/file/uploadPath/2022/01/12/01beb0bc15549b9aa4d18027a7751bae.mp3';
				wx.setInnerAudioOption({
					obeyMuteSwitch: false,
				});
				this.Audio.play();
			},
			// 获取未读消息
			getOrderMsg() {
				let data = {
					shopIds: this.shop.shopId
				}
				this.$Request.postA("/admin/ordermessage/selectReadFlagCount", data).then(res => {
					if (res.code == 0&&res.data>0) {
						this.palyAudio()
					}
				});
			},
			selectShop(e) {
				this.shop = e
				this.show = false
				console.log(this.shop, '店铺信息')
				uni.setStorageSync('updataShopId',this.shop.shopId)
				this.getOrderMsg()
				this.getDataCentre()
			},
			// 收入
			getDataCentre() {
				this.$Request.getA("/admin/dataCentre/selectShopMoney?shopId=" + this.shop.shopId).then(res => {
					if (res.code == 0) {
						this.dataCentre = res.data
					}
				});
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
						this.shopList = res.data.list
						this.shop = this.shopList[0]
						console.log(this.updataShopId)
						if(this.updataShopId) {
							this.shopList.forEach(res=>{
								if(res.shopId == this.updataShopId) {
									this.shop = res
								}
							})
						}
						console.log(this.shop, '店铺信息')
						this.getOrderMsg()
						this.getDataCentre()
					}
				});
			},
			// 查看二维码
			reviewImg() {
				let that = this
				uni.downloadFile({
					url: 'https://diancanshop.xianmxkj.com/sqx_fast/invite/shopQr?shopId=' + that.shop.shopId,
					success: (res) => {
						let images = [];
						images.push(res.tempFilePath);
						uni.previewImage({
							urls: images,
							longPressActions: {
								itemList: ['保存图片'],
								success: function(data) {

								},
								fail: function(err) {
									console.log(err.errMsg);
								}
							}
						});
					}
				})
			},




			Goorder() {
				uni.navigateTo({
					url: '/shop/order?shopId=' + this.shop.shopId
				})
			},
			goNavOrder(index) {
				let shopToken = this.$queue.getData('shopToken');
				if (shopToken) {
					uni.navigateTo({
						url: '/shop/order?status=' + index + '&shopId=' + this.shop.shopId
					})
				} else {
					uni.showModal({
						title: '提示',
						content: '您还未登录,请先登录',
						success: function(res) {
							if (res.confirm) {
								console.log('用户点击确定');
								uni.navigateTo({
									url: '/shop/loginphone'
								})
							} else if (res.cancel) {
								console.log('用户点击取消');
							}
						}
					})
				}
			},
			goNav(url) {
				if (this.shopToken) {
					uni.navigateTo({
						url
					})
				} else {
					uni.navigateTo({
						url: '/shop/loginphone'
					})
				}
			},
			getUserInfo() {
				this.$Request.get("/app/user/selectUserMessage").then(res => {
					if (res.code == 0) {
						this.userName = res.data.userName
						this.avatar = res.data.avatar
						this.$queue.setData("member", res.data.member); //1==已开通会员
					}
				});
			},
			getMsgData() {
				this.$Request.get("/app/userintegral/findUserMessage").then(res => {
					if (res.code == 0) {
						this.msgData = res.data
					}
				});
			}
		}
	}
</script>

<style scoped>
	button::after {
		border: none;
		background-color: none;
	}

	.bg {
		background-color: #fff;
	}

	button {
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;

		text-align: left;
		text-decoration: none;

		line-height: 1.35;


		overflow: hidden;
		color: #333333;
		background-color: #fff;
		width: 100%;
		height: 100%;
	}


	.vip_sty1 {
		width: 128rpx;
		height: 48rpx;
		background: linear-gradient(90deg, #FCD202 0%, #FFDD5B 100%);
		border-radius: 24rpx 0 0 24rpx;
		text-align: center;
		line-height: 48rpx;
		font-size: 24rpx;
		font-weight: 500;
		color: #333333;
	}




	.header_text1 {
		font-size: 38rpx;
		font-weight: bold;
		color: #333333;
	}

	.header_text2 {
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		margin-top: 10rpx;
	}

	.header_text4 {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
	}

	.clear {
		clear: both;
	}

	/* 用户信息 */
	.header_all {
		width: 100%;
		height: 300rpx;
		padding-top: 50rpx;
		background: linear-gradient(0deg, rgba(255, 255, 255, 0) 0%, #FF9CBD 100%);
	}

	.ma {
		background-color: #FFFFFF;
		height: 60rpx;
		line-height: 60rpx;
		width: 175rpx;
		text-align: center;
		border-radius: 35rpx;
		margin-right: 35rpx;
	}

	.header {
		width: 94%;
		/* 	height: 289rpx; */
		background-color: #FFFFFF;
		margin: 0 auto;
		border-radius: 18rpx;

	}

	.header_top_le {
		width: 17%;
		height: 112rpx;
		margin: 2% 0 0 3%;
		float: left;
	}

	.header_top_le image {
		width: 112rpx;
		height: 112rpx;
		flex-shrink: 0;
	}

	.header_top_ce {
		width: 55%;
		margin: 3% 0 0 0;
		float: left;
	}

	.login {
		padding-top: 20rpx;
		padding-left: 10rpx;
		font-size: 22px;
		font-weight: bold;
	}

	.header_top_ce_to>view {
		float: left;
		font-size: 38rpx;
		font-weight: 800;
		color: #333333;
	}

	.header_top_ce_to>image {
		float: left;
		width: 18rpx;
		height: 30rpx;
		padding: 15rpx 0 0 10rpx;
	}

	.header_top_ce_bg {
		width: 200rpx;
		height: 45rpx;
	}

	.header_top_ce_bg image {
		width: 114rpx;
		height: 30rpx;
	}

	.header_top_ce_bg>view {
		position: relative;
		bottom: 38rpx;
		left: 50rpx;
		font-size: 24rpx;
		font-weight: 500;
		color: #FFFFFF;
	}

	.header_top_ri {
		width: 25%;
		float: right;
		margin: 6% 0 0 0;
	}

	.header_top_ri>view {
		line-height: 2;
		background-color: #FCD202;
		text-align: center;
		border-top-left-radius: 50rpx;
		border-bottom-left-radius: 50rpx;
	}

	.header_bottom {
		width: 100%;
		display: flex;
		padding: 3% 0 4%;
	}

	.header_bottom_sty {
		flex: 1;
		text-align: center;
		border-left: 1px solid #E6E6E6;
	}

	.header_bottom_sty:first-child {
		border-left: 0;
	}

	/* 任务兑换中心 */
	.center {
		width: 94%;
		padding: 3%;
		/* line-height: 1.5; */
		background-color: #FFFFFF;
		border-radius: 18rpx;
		margin: 120rpx auto 0;
		display: flex;
		justify-content: space-between;
	}

	.center_le {
		/* width: 65%; */
	}

	.center_ri {
		/* width: 35%; */
	}

	/* 列表 */
	.list_all {
		width: 94%;
		line-height: 2;
		padding: 3%;
		margin: 3% auto 0;
		background-color: #FFFFFF;
		border-radius: 18rpx;
	}

	.list {
		margin-top: 30rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding-top: 10rpx;
	}

	.list:first-child {
		margin-top: 0;
	}

	.list_le {
		flex: 2;
	}

	.list_ri {
		flex: 1;
		height: 28rpx;
		text-align: right;
	}

	.list_ri image {
		width: 16rpx;
		height: 28rpx;
	}

	.list_ris image {
		width: 13rpx;
		height: 24rpx;
	}

	.nearby_address_active {
		border: 2rpx solid #FCD202;
		width: 100%;
		padding: 3%;
		margin-top: 3%;
		border-radius: 10upx;
		display: flex;
	}

	.nearby_address {
		width: 100%;
		padding: 3%;
		margin-top: 3%;
		border-radius: 10upx;
		display: flex;
	}

	.nearby_address_le {
		flex: 1;
	}

	.nearby_address_ri {
		line-height: 2.5;
		text-align: right;
		/* flex: 1; */
	}

	.nearby_text2 {

		/* line-height: 1.5; */
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.nearby_text2_ {
		font-size: 30rpx;
		font-weight: 800;
		color: #333333;
	}

	.nearby_text3 {
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		margin-top: 2%;
	}
</style>
