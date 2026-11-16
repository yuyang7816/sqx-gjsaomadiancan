<template>
	<view class="content">

		<view class="head">
			<view class="head_image" @click="bindlogin">
				<image :src="avatar?avatar:'../../static/logo.png'"></image>
			</view>
			<view class="head_name flex align-center justify-between" @click.stop="bindlogin">
				<view class="flex align-center">
					<view class="name  margin-right-sm" v-if="token">{{ userName }}</view>
					<view class="name" v-if="!token">{{$t('login.home')}}</view>
					<view v-if="token &&member==1" @tap.stop="goVip">
						<view class="" style="position: relative;width: 130upx;height: 30upx;">
							<image src="../../static/images/my/vipimage.png" style="width: 114upx;height: 30upx;">
							</image>
							<view
								style="position: absolute;top:-2rpx;left:35rpx;z-index: 2;font-size: 22upx;font-family: PingFang SC;font-weight: bold;color: #BD9160;">
								{{$t('vip.title')}}</view>
						</view>
					</view>
				</view>
				<view class=" langebtn" @tap.stop="qhTypeSel">
					<image src="../../static/images/guoji.png" style="width: 53rpx;height:51rpx;"></image>
				</view>
			</view>
		</view>
		<view class="margin-lr padding-lr-sm padding-tb radius flex justify-between" v-if="xcxSelect=='否'">
			<view class="text-center text-black" @click="goShop('/my/coupon/index')">
				<view class="text-lg text-bold">{{msgData.countCoupon}}</view>
				<view>{{$t('yjq.title')}}</view>
			</view>
			<view class="text-center text-black" @click="goShop('/my/wallet/index')">
				<view class="text-lg text-bold">{{msgData.userMoney}}</view>
				<view>{{$t('purse.title')}}</view>
			</view>
			<view class="text-center text-black" @click="goShop('/my/task/index')">
				<view class="text-lg text-bold">{{msgData.userIntegral}}</view>
				<view>{{$t('jifen.title')}}</view>
			</view>
		</view>
		<view class="padding-lr" style="position: relative;" v-if="xcxSelect=='否'">
			<image src="../../static/images/my/bg.png" style="width: 100%;height: 110rpx;"></image>
			<view class="flex align-center justify-between  margin-lr padding-tb-sm radius"
				style="position: absolute;top: 0;width: 640rpx;">
				<image src="../../static/images/my/huiyuan.png" style="width: 70rpx;height: 70rpx;"></image>
				<view class="flex-sub margin-left-xs text-cut" style="line-height: 74rpx;color: #604320;">
					{{$t('vip.mytit')}}
				</view>
				<view class="btn-bg" v-if="member !=1" @click="goShop('/my/vip/toVip')">{{$t('vip.qkt')}}
				</view>
				<view v-else class="btn-bg" @click="goShop('/my/vip/toVip')">{{$t('vip.ykt')}}</view>
			</view>
		</view>
		<view class="center flex justify-between bg-white margin-top padding-lr padding-tb-lg radius">
			<view class="flex justify-between flex-sub padding-right" style="border-right: 2rpx solid #CCCCCC;"
				@click="goShop('/my/task/index')">
				<view class="">
					<view class="text-black text-xl text-bold">{{$t('jifen.renwu')}}</view>
					<view class="text-sm margin-top-xs">{{$t('jifen.renwutit')}}</view>
				</view>
				<image src="../../static/images/my/renwu.png" mode="" style="width: 88rpx;height: 88rpx;"></image>
			</view>
			<view class="flex justify-between flex-sub padding-left" @click="goShop('/my/integral/index')">
				<view class="">
					<view class="text-black text-xl text-bold">{{$t('jifen.sc')}}</view>
					<view class="text-sm margin-top-xs flex align-center">{{$t('jifen.dhyhq')}}</view>
				</view>
				<image src="../../static/images/my/jifen.png" mode="" style="width: 88rpx;height: 88rpx;"></image>
			</view>
		</view>

		<view class="margin padding bg-white radius">
			<view class="flex justify-between align-center">
				<view class="text-lg text-bold text-black">推荐工具</view>
			</view>
			<view class="">

				<view class="flex align-center justify-between margin-tb-sm" @click="goNav('/pages/my/invitationUser')">
					<view class="flex align-center ">
						<image src="../../static/images/my/4.png" style="width: 60rpx;height: 60rpx;"
							mode="scaleToFill"></image>
						<view class="text-sm margin-left-sm">{{$t('yao.title')}}</view>
					</view>
					<image src="/static/images/order/right1.png" style="width: 14rpx;height:24rpx;" mode="widthFix">
					</image>
				</view>
				<view class="flex align-center justify-between margin-tb-sm" @click="goNav('/my/wallet/walletDet')">
					<view class="flex align-center ">
						<image src="../../static/images/my/2.png" style="width: 60rpx;height: 60rpx;"
							mode="scaleToFill"></image>
						<view class="text-sm margin-left-sm">{{$t('purse.qbmx')}}</view>
					</view>
					<image src="/static/images/order/right1.png" style="width: 14rpx;height:24rpx;" mode="widthFix">
					</image>
				</view>

				<view class="flex align-center justify-between margin-tb-sm" @click="goNav('/my/task/index')">
					<view class="flex align-center ">
						<image src="../../static/images/my/renwu.png" style="width: 60rpx;height: 60rpx;"
							mode="scaleToFill"></image>
						<view class="text-sm margin-left-sm">{{$t('renwu.title')}}</view>
					</view>
					<image src="/static/images/order/right1.png" style="width: 14rpx;height:24rpx;" mode="widthFix">
					</image>
				</view>

				<view class="flex align-center justify-between margin-tb-sm" @click="goNav('/my/setting/customer')">
					<view class="flex align-center ">
						<image src="../../static/images/my/6.png" style="width: 60rpx;height: 60rpx;"
							mode="scaleToFill"></image>
						<view class="text-sm margin-left-sm">{{$t('kefu.title')}}</view>
					</view>
					<image src="/static/images/order/right1.png" style="width: 14rpx;height:24rpx;" mode="widthFix">
					</image>
				</view>

				<view class="flex align-center justify-between margin-tb-sm" @click="goNav('/my/msg/index')">
					<view class="flex align-center ">
						<image src="../../static/images/my/3.png" style="width: 60rpx;height: 60rpx;"
							mode="scaleToFill"></image>
						<view class="text-sm margin-left-sm">{{$t('msg.title')}}</view>
					</view>
					<image src="/static/images/order/right1.png" style="width: 14rpx;height:24rpx;" mode="widthFix">
					</image>
				</view>

				<view class="flex align-center justify-between margin-tb-sm" @click="goNav('/my/address/index')">
					<view class="flex align-center ">
						<image src="../../static/images/my/7.png" style="width: 60rpx;height: 60rpx;"
							mode="scaleToFill"></image>
						<view class="text-sm margin-left-sm">{{$t('add.title')}}</view>
					</view>
					<image src="/static/images/order/right1.png" style="width: 14rpx;height:24rpx;" mode="widthFix">
					</image>
				</view>

				<view class="flex align-center justify-between margin-tb-sm" @click="goNav('/my/setting/index')">
					<view class="flex align-center ">
						<image src="../../static/images/my/8.png" style="width: 60rpx;height: 60rpx;"
							mode="scaleToFill"></image>
						<view class="text-sm margin-left-sm">{{$t('set.title')}}</view>
					</view>
					<image src="/static/images/order/right1.png" style="width: 14rpx;height:24rpx;" mode="widthFix">
					</image>
				</view>

			</view>
		</view>

		<u-select v-model="Languageshow" :list="listLanguage" @cancel="Languageshow = false" @confirm="confirm"
			:cancelText="$t('index.qx')" :confirmText="$t('index.sure')"></u-select>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				mylist: [{
						id: 1,
						name: '收支明细',
						image: '../../static/images/my/2.png',
						url: '/my/wallet/walletDet'
					}, {
						id: 2,
						name: '任务中心',
						image: '../../static/images/my/renwu.png',
						url: '/my/task/index'
					}, {
						id: 3,
						name: '联系客服',
						image: '../../static/images/my/6.png',
						url: '/my/setting/customer'
					}, {
						id: 5,
						name: '消息中心',
						image: '../../static/images/my/3.png',
						url: '/my/msg/index'
					}, {
						id: 6,
						name: '地址管理',
						image: '../../static/images/my/7.png',
						url: '/my/address/index'
					},
					{
						id: 10,
						name: '系统设置',
						image: '../../static/images/my/8.png',
						url: '/my/setting/index'
					},
				],
				mylist1: [{
						id: 3,
						name: '消息中心',
						image: '../../static/images/my/3.png',
						url: '/my/msg/index'
					}, {
						id: 6,
						name: '联系客服',
						image: '../../static/images/my/6.png',
						url: '/my/setting/chat'
					}, {
						id: 7,
						name: '地址管理',
						image: '../../static/images/my/7.png',
						url: '/my/address/index'
					}, {
						id: 9,
						name: '聊天室',
						image: '../../static/images/order/kefu.png',
						url: '/my/chat/index'
					},
					{
						id: 10,
						name: '系统设置',
						image: '../../static/images/my/8.png',
						url: '/my/setting/index'
					},
				],
				avatar: '',
				userName: '',
				checkCertification: -1,
				arr: [],
				showModal: true,
				msgData: {
					userIntegral: 0,
					countCoupon: 0,
					userMoney: 0
				},

				tuiguang: '',
				tuiguangImg: '',
				token: '',
				xcxSelect: '否',
				shopStatus: '',
				shopAdminFlag: 1,
				member: '',

				zyType: '',
				Languageshow: false,
				listLanguage: [],

			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('my.title')
			})

			this.getZiZhi()
			this.xcxSelect = this.$queue.getData('xcxSelect') ? this.$queue.getData('xcxSelect') : '否'
		},
		onShow() {
			this.token = this.$queue.getData("token") ? this.$queue.getData("token") : ''

			if (this.token) {
				this.getUserInfo();
				this.getMsgData()
			} else {

				this.userName = ''
				this.avatar = ''
				this.msgData.userIntegral = 0
				this.msgData.countCoupon = 0
				this.msgData.userMoney = 0
			}
		},
		onShareAppMessage(res) { //发送给朋友
			return {
				title: this.tuiguang,
				path: '/pages/index/index',
				imageUrl: this.tuiguangImg,
			}
		},
		onShareTimeline(res) { //分享到朋友圈
			return {
				title: this.tuiguang,
				path: '/pages/index/index',
				imageUrl: this.tuiguangImg,
			}
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

			goVip() {
				if (this.token) {
					uni.navigateTo({
						url: '/my/vip/toVip'
					});
				} else {
					this.bindlogin();
				}
			},
			getMsgData() {
				this.$Request.get("/app/userintegral/findUserMessage").then(res => {
					if (res.code == 0) {
						this.msgData = res.data
					}
				});
			},
			// 分享文案和图片
			getZiZhi() {
				this.$Request.getT('/app/common/type/239').then(res => {
					if (res.code === 0) {
						this.tuiguang = res.data.value;
					}
				});
				this.$Request.getT('/app/common/type/238').then(res => {
					if (res.code === 0) {
						this.tuiguangImg = res.data.value;
					}
				});
			},
			goSwt(e) {
				uni.setStorageSync('current', e)
				setTimeout(function() {
					uni.switchTab({
						url: '/pages/order/index',
					})
				}, 10)
			},
			goApplet(e) {
				uni.navigateToMiniProgram({
					appId: e,
					path: 'pages/index/index',
					success(res) {
						// 打开成功
					}
				})
			},
			goNav(url) {
				if (this.token) {
					uni.navigateTo({
						url
					})
				} else {
					this.bindlogin();
				}
			},
			goShop(url) {
				if (this.token) {
					uni.navigateTo({
						url
					})
				} else {
					this.bindlogin();
				}
			},
			getUserInfo() {
				this.$Request.getT('/app/user/selectUserMessage').then(res => {
					console.log(res)
					if (res.code == 0) {
						if (parseInt(res.data.checkCertification)) {
							this.checkCertification = parseInt(res.data.checkCertification)
						} else {
							this.checkCertification = -1;
						}
						this.member = res.data.member
						this.$queue.setData("member", res.data.member); //1==已开通会员
						this.shopStatus = res.data.shopStatus
						this.$queue.setData("avatar", res.data.avatar ? res.data.avatar : '../../static/logo.png');
						this.$queue.setData("userId", res.data.userId);
						this.$queue.setData("status", res.data.status);
						this.$queue.setData("userName", res.data.userName ? res.data.userName : res
							.data.nickName);
						this.avatar = res.data.avatar ? res.data.avatar : '../../static/logo.png';
						this.userName = res.data.userName ? res.data.userName : res.data.nickName
						this.shopAdminFlag = res.data.shopAdminFlag
					}
				});
			},
			bindlogin() {
				if (!this.token) {
					uni.navigateTo({
						url: '/pages/login/login'
					})
				} else {
					uni.navigateTo({
						url: '/pages/my/userinfo'
					})
				}
			},
			bindTo(name) {
				console.log(name)
				if (this.token) {
					if (name == '我的红包') {
						uni.navigateTo({
							url: '/pages/my/hongbao/hongbao'
						})
					} else if (name == '注册骑手') {
						uni.navigateToMiniProgram({
							appId: 'wx5ed22ce813e47796',
							path: '/pages/index/index',
							extraData: {
								'data1': 'test'
							},
							success(res) {
								// 打开成功
								console.log("打开成功")
							}
						})
					} else if (name == '意见反馈') {
						uni.navigateTo({
							url: '/pageA/feedback/feedback'
						})
					} else if (name == '联系客服') {
						uni.navigateTo({
							url: '/pageA/kefu/kefu'
						})
					} else if (name == '系统设置') {
						uni.navigateTo({
							url: '/pages/my/set/set'
						})
					} else if (name == '地址管理') {
						uni.navigateTo({
							url: '/pageA/address/address'
						})
					}
				} else {
					this.bindlogin();
				}
			},
			bindapprove() {

				if (this.token) {
					uni.navigateTo({
						url: '/pages/my/approve/approve'
					})
				} else {
					this.bindlogin();
				}
			},
			binduser() {

				if (this.token) {
					// uni.navigateTo({
					// 	url: '/pages/my/userphone/userphone'
					// })
				} else {
					this.bindlogin();
				}
			}
		}
	}
</script>

<style>
	button::after {
		border: none;
		background-color: none;
	}

	button {
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;
		text-decoration: none;
		line-height: 1.35;
		overflow: hidden;
		color: #666666;
		background-color: #fff;
		width: 100%;
		height: 100%;
	}

	.btn-bg {
		height: 28px;
		background: linear-gradient(90deg, #CDA26E 0%, #DCB78A 100%);
		border-radius: 28px;
		text-align: center;
		line-height: 28px;
		color: #604320;
		padding: 0 15rpx;
	}

	body {
		background: #F5F5F5;
	}

	/* #ifndef MP-WEIXIN */
	page {
		background: #F2EDED;
	}

	/* #endif */

	.content {
		width: 100%;
	}

	.btn {
		font-size: 24upx;
		/* width: 95%; */
		text-align: center;
		background: #FFFFFF;
		margin-top: 6rpx;
	}

	.head {
		display: flex;
		align-items: center;
		padding: 30rpx;
		/* border-radius: 16rpx; */
		background: linear-gradient(0deg, rgba(255, 255, 255, 0) 0%, #FF9CBD 100%);
	}

	.head_image {}

	.head_image>image {
		width: 90rpx;
		height: 90rpx;
		border-radius: 50%
	}

	.head_name {
		margin-left: 20rpx;
		flex: 1;
	}

	.name {
		font-size: 38rpx;
		font-weight: bold;
		margin-bottom: 4rpx;
	}

	.approve {
		position: absolute;
		top: 100rpx;
		font-size: 24rpx;
		color: #999999;
	}

	/* 列表 */
	.use_list {
		width: 100%;
		background: #ffffff;
		margin-top: 20rpx;
	}

	.list_box {
		width: 90%;
		margin: 0 auto;
		display: flex;
		height: 110rpx;
	}

	.box_left {
		flex: 1;
		display: flex;
		justify-content: left;
		align-items: center;
	}

	.box_right {
		flex: 1;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		color: #808080;
	}

	.use_name {
		margin-left: 30rpx;
		font-size: 32rpx;
	}

	.use_image image {
		width: 50rpx;
		height: 50rpx;
	}

	.center {
		width: 94%;

		/* line-height: 1.5; */
		background-color: #FFFFFF;
		border-radius: 18rpx;
		margin: 30rpx auto 0;
		display: flex;
		justify-content: space-between;
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
</style>