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
							<view class="text-xl text-bold margin-right-xs">
								{{shop.shopName?shop.shopName:$t('user.niming')}}
							</view>
							<image src="../static/images/diancan/xia.png" style="width: 20rpx;height: 10rpx;"></image>
						</view>
						<view class="text-sm margin-top-xs">{{$t('shop.yrz')}}</view>
					</view>
				</view>
				<view class="flex align-center margin-right">
					<view class="">
						<view @click="reviewImg()" class="ma" style="margin-bottom: 10rpx;">{{$t('dc.title')}}</view>
						<!-- <view @click="reviewImgpay()" class="ma">店铺收银码</view> -->
					</view>
					<view class=" langebtn" @tap.stop="qhTypeSel">
						<image src="../static/images/guoji.png" style="width: 53rpx;height:51rpx;"></image>
					</view>
				</view>
			</view>
			<view class="flex justify-between align-center padding-left" v-else>
				<view class="">
					<image class="round" src="../static/logo.png" mode="" style="width: 100rpx;height: 100rpx;">
					</image>
				</view>
				<view class="flex-sub flex align-center justify-between margin-left-xs">
					<view @click="goNav('/pages/loginphone')">
						<view class="text-xxl text-bold ">{{$t('login.home')}}</view>
					</view>
					<view class=" langebtn margin-right" @tap.stop="qhTypeSel">
						<image src="../static/images/guoji.png" style="width: 53rpx;height:51rpx;"></image>
					</view>
				</view>
			</view>
		</view>

		<view style="position: relative;top: -113rpx;z-index: 9;">
			<view class="header">
				<view class="text-xl text-bold padding-lr padding-top-sm">{{$t('shop.zijin')}}</view>
				<view class="header_bottom clear" @click="goNav('/pages/earnings?shopId='+shop.shopId)">
					<view class="header_bottom_sty">
						<!-- 本年收益 -->
						<view class="header_text1">
							<text class="text-df">{{$t('index.qian')}}</text>
							{{dataCentre.yearMoney?dataCentre.yearMoney:0}}
						</view>
						<view class="header_text2">{{$t('shop.yearsy')}}</view>
					</view>
					<!-- 本日收益 -->
					<view class="header_bottom_sty">
						<view class="header_text1">
							<text class="text-df">{{$t('index.qian')}}</text>
							{{dataCentre.dayMoney ?dataCentre.dayMoney :0}}
						</view>
						<view class="header_text2">{{$t('shop.daysy')}}</view>
					</view>
					<!-- 本月收益 -->
					<view class="header_bottom_sty">
						<view class="header_text1">
							<text class="text-df">{{$t('index.qian')}}</text>
							{{dataCentre.monthMoney ?dataCentre.monthMoney :0}}
						</view>
						<view class="header_text2">{{$t('shop.monthsy')}}</view>
					</view>
				</view>
			</view>

			<!-- 订单中心 -->
			<view class="bg margin-sm radius">
				<view class="flex justify-between padding-top padding-lr">
					<view style="font-size: 28upx;font-weight: bold;">{{$t('order.name')}}</view>
					<view class="flex align-center" @click="Goorder()">
						<text class="text-sm" style="color:#999999;margin-top:-4rpx;">{{$t('shop.more')}}</text>
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
						<!-- <view class="text-df" style="line-height: 50upx;">{{$t('order.zhizuo')}}</view> -->
					</view>
					<!-- 待取餐 -->
					<view class="padding-lr padding-tb" @click="goNavOrder(2)">
						<image src="../static/images/my/order3.png" style="width: 60upx;height: 60upx;" mode="">
						</image>
						<!-- <view class="text-df" style="line-height: 50upx;">{{$t('order.dqc')}}</view> -->
					</view>
					<!-- 已完成 -->
					<view class="padding-lr padding-tb" @click="goNavOrder(3)">
						<image src="../static/images/my/order4.png" style="width: 60upx;height: 60upx;" mode="">
						</image>
						<!-- <view class="text-df" style="line-height: 50upx;">{{$t('order.ywc')}}</view> -->
					</view>
				</view>
			</view>

			<!-- 列表 -->
			<view class="list_all">
				<view class="list" @click="goNav('/pages/meals/meals')">
					<u-icon name="order" color="#000" size="48"></u-icon>
					<view class="list_le text-lg margin-left-sm">{{$t('shop.ycgl')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/pages/publish?shopId='+shop.shopId)">
					<image src="../static/images/my/shangpu.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">{{$t('shop.title')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/pages/goodlist?shopId='+shop.shopId)">
					<image src="../static/images/my/shangpin.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">{{$t('Commodity.list')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/pages/goodsType')">
					<!-- <image src="../static/images/my/shangpin.png" style="width:48upx;height:48upx;"></image> -->
					<u-icon name="grid" color="#000" size="48"></u-icon>
					<view class="list_le text-lg margin-left-sm">{{$t('Commodity.typeTitle')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/pages/guige')">
					<!-- <image src="../static/images/my/shangpin.png" style="width:48upx;height:48upx;"></image> -->
					<u-icon name="file-text" color="#000" size="48"></u-icon>
					<view class="list_le text-lg margin-left-sm">{{$t('Commodity.attrs')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/pages/books')">
					<!-- <image src="../static/images/my/shangpin.png" style="width:48upx;height:48upx;"></image> -->
					<u-icon name="bag" color="#000" size="48"></u-icon>
					<view class="list_le text-lg margin-left-sm">{{$t('Table.title')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/pages/earnings?shopId='+shop.shopId)">
					<image src="../static/images/my/money.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">{{$t('sy.title')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/my/setting/customer')">
					<image src="../static/images/my/kefu.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">{{$t('kefu.title')}}</view>
					<view class="list_ri">
						<image src="../static/images/order/right1.png" mode=""></image>
					</view>
				</view>
				<view class="list" @click="goNav('/my/setting/index')">
					<image src="../static/images/my/set.png" style="width:48upx;height:48upx;"></image>
					<view class="list_le text-lg margin-left-sm">{{$t('set.title')}}</view>
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
						<view class="nearby_text3">{{$t('payorder.data')}}：{{item.openTime}} - {{item.closeTime}}</view>
					</view>
				</view>
			</view>
		</u-popup>


		<u-select v-model="Languageshow" :list="listLanguage" @cancel="Languageshow = false" @confirm="confirm"
			:cancelText="$t('index.qx')" :confirmText="$t('index.sure')"></u-select>
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
				updataShopId: '',
				userId: '',

				zyType: '',
				Languageshow: false,
				listLanguage: [],
			}
		},

		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('my.title')
			})
			
		},
		onShow() {
			this.xcxSelect = uni.getStorageSync('xcxSelect')
			let that = this
			that.shopToken = uni.getStorageSync('shopToken') ? uni.getStorageSync('shopToken') : ''
			that.updataShopId = uni.getStorageSync('updataShopId') ? uni.getStorageSync('updataShopId') : ''
			console.log(that.updataShopId)
			if (that.shopToken) {
				that.getShopList()
			} else {
				this.userName = ''
				this.msgData.userIntegral = 0
				this.msgData.countCoupon = 0
				this.msgData.userMoney = 0
				this.dataCentre = {}
			}
			setTimeout(function() {
				that.getOrderMsg()
			}, 10000)
		},
		methods: {
			confirm(e) {
				console.log(e)
				let languagelabel = e[0].label
				let languagevalue = e[0].value.language
				let languageObj = e[0].value
				this.confimChange(languagevalue, languageObj)

			},
			confimChange(languagevalue, languageObj) {
				let that = this;
				that.$Request.get('/app/configI18n/getLanguageByKey?language=' + languagevalue +
					'&primaryKey=language.tit,index.wxts,index.qx,index.sure').then(res => { // 选择语言弹框
					if (res.code == 0 && res.data) {
						let ts = ''
						let title = ''
						let no = ''
						let yes = ''
						res.data.forEach((d, index) => {
							if (d.primaryKey == 'index.wxts') {
								ts = d.languageValue
							}
							if (d.primaryKey == 'language.tit') {
								title = d.languageValue
							}
							if (d.primaryKey == 'index.qx') {
								no = d.languageValue
							}
							if (d.primaryKey == 'index.sure') {
								yes = d.languageValue
							}
						})
						uni.showModal({
							title: ts,
							content: title,
							showCancel: true,
							cancelText: no,
							confirmText: yes,
							success: res => {
								if (res.confirm) {
									that.zyType = languagevalue;
									uni.setLocale(languagevalue);
									uni.setStorageSync("UNI_LOCALE", languagevalue)
									uni.setStorageSync("language", languagevalue)
									uni.setStorageSync('mapLanguage', languageObj.mapLanguage)
									that.$i18n.locale = languagevalue;
									// #ifdef APP-PLUS
									plus.runtime.restart();
									// #endif
									// #ifdef H5
									that.$router.go(0);
									// #endif
								}
							}
						});
					}
				});
			},
			qhTypeSel() {
				this.listLanguage = []
				this.$Request.get('/app/languageType/getLanguageTypeList').then(res => { // 获取国际化语种类别
					if (res.code == 0 && res.data) {
						res.data.records.map(item => {
							this.listLanguage.push({
								label: item.remark,
								value: item,
							})
						})
						uni.setStorageSync("languageList", res.data.records)
						this.Languageshow = true
					}
				});
			},

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
					if (res.code == 0 && res.data > 0) {
						this.palyAudio()
					}
				});
			},
			selectShop(e) {
				this.shop = e
				uni.setStorageSync('shopName', this.shop.shopName)
				uni.setStorageSync('updataShopId', this.shop.shopId)
				this.getOrderMsg()
				this.getDataCentre()
				this.show = false
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
			getShopList(flag) {
				let data = {
					page: this.page,
					limit: this.limit,
					shopName: this.shopName
				}
				this.$Request.getA("/admin/goods/selectAllShop", data).then(res => {
					if (res.code == 0) {
						this.shopList = res.data.list
						this.shop = this.shopList[0]
						uni.setStorageSync('shopName', this.shop.shopName)
						if (this.updataShopId) {
							this.shopList.forEach(res => {
								if (res.shopId == this.updataShopId) {
									this.shop = res
								}
							})
						}
						uni.setStorageSync('updataShopId', this.shop.shopId)
						this.getOrderMsg()
						this.getDataCentre()
					}
				});
			},
			//查看收银码
			reviewImgpay() {
				let that = this
				this.$Request.getA('/invite/shopPaySeatQr?shopId=' + that.shop.shopId).then(res => {
					if (res) {
						uni.previewImage({
							urls: [res]
						})
					} else {
						uni.showToast({
							title: '查看失败，请重试',
							icon: 'none'
						})
					}
				})

			},
			// 查看二维码
			reviewImg() {
				let that = this
				this.$Request.getA('/invite/shopQr?shopId=' + that.shop.shopId).then(res => {
					if (res) {
						uni.previewImage({
							urls: [res]
						})
					} else {
						uni.showToast({
							title: '查看失败，请重试',
							icon: 'none'
						})
					}
				})
				
			},
			Goorder() {
				uni.switchTab({
					url: '/pages/order?shopId=' + this.shop.shopId
				})
			},
			goNavOrder(index) {
				let shopToken = this.$queue.getData('shopToken');
				if (shopToken) {
					uni.setStorageSync('status', index)
					uni.switchTab({
						url: '/pages/order'
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
			goNav(url) {
				if (this.shopToken) {
					uni.navigateTo({
						url
					})
				} else {
					uni.navigateTo({
						url: '/pages/loginphone'
					})
				}
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
		border: 2rpx solid #FF9CBD;
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