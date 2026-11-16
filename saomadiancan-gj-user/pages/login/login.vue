<template>
	<view class="container">
		<!-- #ifndef H5 -->
		<u-navbar  :title="$t('login.home')" bgColor="transparent" :border="false"
			:border-bottom="false" @leftClick="leftClick" :autoBack="true"></u-navbar>
		<!-- #endif -->

		<image src="../../static/images/index/pagebg.png" class="bgpage"></image>


		<view style="position: relative;z-index: 99;">

			<view class="dlogin">{{$t('login.home')}}</view>

			<button class='confirm-btn' v-if=" yxLogin != '否'" @click="register(2)">{{$t('login.yxlogin')}}</button>
			<button class='confirm-btn' :style="yxLogin != '否'?'margin-top: 20rpx;':'margin-top: 520rpx;'"
				v-if="phoneSel != '否'" @click="register(1)">{{$t('login.sjlogin')}}</button>

		</view>
		<!-- #ifdef APP-PLUS -->
		<view v-if="checkGoogoleLogin || CheckiosLogin"
			style="display: flex;align-items: center;width: 100%;justify-content: center;margin-top: 250upx;">
			<view style="width: 100rpx;height: 1rpx;background: #d1d1d1;"></view>
			<view style="color: #333333;margin: 0 20rpx;">{{$t('login.more')}}</view>
			<view style="width: 100rpx;height: 1rpx;background: #d1d1d1;"></view>
		</view>
		<view style="display: flex;margin-top: 80upx;width: 750rpx;justify-content: center;"
			v-if="checkGoogoleLogin || CheckiosLogin">

			<view style="flex: 1;text-align: center;" v-if="checkGoogoleLogin">
				<image src="/static/images/me/google.png" @tap="googleLogin()" style="width: 90upx;height: 90upx;">
				</image>
			</view>

			<view style="flex: 1;text-align: center;" v-if="CheckiosLogin">
				<image src="/static/images/me/appleid_button.png" @tap="ioslogin" style="width: 90upx;height: 90upx;">
				</image>
			</view>

		</view>
		<!-- #endif -->

		<view class="footer">
			<view style="display: flex;align-items: center;justify-content: center;">

				<u-checkbox-group placement="column">
					<u-checkbox v-model="checked" label-size='24upx' shape="circle" @change="radioChange"
						activeColor="#FF6D9E">
					</u-checkbox>
				</u-checkbox-group>
				<view>{{$t('login.ty')}}</view>
				<!-- 协议地址 -->
				<text @click.stop="goTo('/my/setting/mimi')">《{{$t("set.ysxy")}}》</text>
				{{$t('index.he')}}
				<text @click.stop="goTo('/my/setting/xieyi')">《{{$t("set.yhxy")}}》</text>
			</view>

		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				checked: false,
				phoneSel: '否',
				yxLogin: '否',
				CheckiosLogin: false,
				checkGoogoleLogin: false
			};
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('login.home')
			})
			this.phoneSel = this.$queue.getData('loginPhoneSel');
			this.sendTime = this.$t("login.ghmsg");
			this.yxLogin = this.$queue.getData('yxLogin');
			// #ifdef APP-PLUS

			let checkGoogle = this.$queue.getData('checkGoogoleLogin');
			if (checkGoogle === '是') {
				this.checkGoogoleLogin = true;
			} else {
				this.checkGoogoleLogin = false
			}

			switch (uni.getSystemInfoSync().platform) {
				case 'android':
					break;
				case 'ios':
					let checkIosLogins = this.$queue.getData('checkIosLogin');
					if (checkIosLogins != '否') {
						this.CheckiosLogin = true;
					} else {
						this.CheckiosLogin = false;
					}
					break;
				default:
					break;
			}
			// #endif
			if (this.$queue.getData('UNI_LOCALE')) {
				this.zyType = this.$queue.getData('UNI_LOCALE');
				if (this.zyType === 'zh') {
					this.quhao = '+86'
				} else {
					this.quhao = '+267'
				}
			} else {
				this.zyType = 'en'
				this.quhao = '+267'
			}
		},
		methods: {
			leftClick() {
				uni.navigateBack()
			},
			// google 登录
			googleLogin() {
				if (!this.checked) {
					uni.showToast({
						title: "" + this.$t("login.ty") + '《' + "" + this.$t("set.ysxy") + '》' + "" + this
							.$t(
								"index.he") + '《' + this.$t("set.yhxy") + '》',
						icon: 'none',
						duration: 1000
					})
					return;
				}
				let that = this;
				uni.login({
					provider: 'google',
					success: function(loginRes) {
						// 登录成功
						uni.getUserInfo({
							provider: 'google',
							success: function(info) {
								console.log(info)

								uni.showLoading({
									title: "" + that.$t("login.denglu"),
								})
								let datas = {
									googleOpenId: info.userInfo.openId
								}
								that.$queue.setData('googleOpenId', info.userInfo.openId)
								//在这里获取到苹果手机的ID后可以继续你的操作，请求后台进行登录
								that.$Request.postT('/app/Login/googleLogin', datas).then(re => {
									if (re.code == 0) {
										that.$queue.setData('token', re.data.token);
										that.$queue.setData('userId', re.data.user.userId);
										uni.switchTab({
											url: '/pages/index/index'
										})
									} else {
										uni.hideLoading();
										uni.navigateTo({
											url: './iosmobile?name=google'
										});
									}
								});

								// 获取用户信息成功, info.authResult保存用户信息
							}
						})
					},
					fail: function(err) {
						// 登录授权失败
						// err.code是错误码
						console.log(err)
						uni.showToast({
							icon: 'none',
							title: err,
							duration: 2000
						});
					}
				});
			},
			//app苹果登录
			ioslogin() {
				if (!this.checked) {
					uni.showToast({
						title: "" + this.$t("login.ty") + '《' + "" + this.$t("set.ysxy") + '》' + "" + this
							.$t(
								"index.he") + '《' + this.$t("set.yhxy") + '》',
						icon: 'none',
						duration: 1000
					})
					return;
				}
				let that = this;
				uni.login({
					provider: 'apple',
					success: function(loginRes) {
						let iosopenid = loginRes.authResult.openid
						// 获取用户信息
						uni.getUserInfo({
							provider: 'apple',
							success(ress) {
								uni.showLoading({
									title: "" + this.$t("login.denglu"),
								})
								let datas = {
									identityToken: JSON.stringify(ress)
								}
								//在这里获取到苹果手机的ID后可以继续你的操作，请求后台进行登录
								that.$Request.postT('/app/Login/appleLogin', datas).then(re => {
									if (re.code == 0) {
										that.$queue.setData('appleId', re.data)
										that.$Request.getT(
											'/app/Login/insertAppleUser?appleId=' + re.data
										).then(res => {
											console.log(JSON.stringify(res))
											if (res.code == 0) {
												that.$queue.setData('token', res
													.token);
												that.$queue.setData('userId', res.user
													.userId);
												uni.switchTab({
													url: '/pages/index/index'
												})
											} else {
												uni.hideLoading();
												if (res.code == 201) {
													uni.navigateTo({
														url: './iosmobile?name=ios'
													});
												} else if (res.code == 500) {
													that.$queue.showToast(res.msg);
												} else {
													that.$queue.showToast(res.msg);
												}
											}
										});
									} else {
										uni.hideLoading();
										that.$queue.showToast(re.msg)
									}
								});
							}
						});
					},
					fail: function(err) {
						uni.showToast({
							icon: 'none',
							title: err,
							duration: 2000
						});
					}
				});
			},

			goTo(url) {
				uni.navigateTo({
					url
				});
			},

			// 注册
			register(index) {
				if (this.checked) {
					uni.navigateTo({
						url: '/pages/login/loginPhone?index=' + index
					});
				} else {
					uni.showToast({
						title: "" + this.$t("login.ty") + '《' + "" + this.$t("set.ysxy") + '》' + "" + this
							.$t(
								"index.he") + '《' + this.$t("set.yhxy") + '》',
						icon: 'none',
						duration: 1000
					})
				}

			},

			radioChange(e) {
				console.log(e);
				this.checked = !this.checked
			},

		}
	};
</script>

<style lang="scss">
	.headers {
		text-align: center;
	}

	.headers>image {
		width: 400upx;
		height: 400upx;
	}

	.footer {
		// padding-left: 150rpx;
		margin-top: 32upx;
		font-size: 24upx;
		color: #666666;
		// text-align: center;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
		z-index: 99;
	}

	page {
		background: #fff;
	}

	.send-msg {
		border-radius: 30px;
		color: black;
		background: white;
		height: 30px;
		font-size: 14px;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.dlogin {
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 90rpx;
		color: #FF6D9E;
		margin: 180rpx 0 0 60rpx;
	}

	.bgpage {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		width: 100%;
		height: 100%;
	}

	.container {
		top: 0;
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100vh;
		overflow: hidden;
		background: #fff;

		.mp_wxBox {
			.headers {
				margin: 35% auto 50rpx;
				text-align: center;
				border-radius: 60rpx;
				width: 650rpx;
				height: 300rpx;
				line-height: 450rpx;

				image {
					width: 300rpx;
					height: 300rpx;
				}
			}

			.content {
				text-align: center;
			}

			text {
				display: block;
				color: #9d9d9d;
				margin-top: 40rpx;
			}

			.bottom {
				line-height: 80upx;
				border-radius: 80upx;
				margin: 70rpx 50rpx;
				height: 80upx;
				font-size: 35rpx;
			}
		}
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #fff;
		padding-bottom: 20px;
	}

	// .back-btn {
	// 	position: absolute;
	// 	left: 20px;
	// 	z-index: 9999;
	// 	padding-top: var(--status-bar-height);
	// 	top: 20px;
	// 	font-size: 20px;
	// 	color: $font-color-dark;
	// }

	// .left-top-sign {
	// 	font-size: 80px;
	// 	color: $page-color-base;
	// 	position: relative;
	// }

	// .right-top-sign {
	// 	position: absolute;
	// 	top: 40px;
	// 	right: -15px;
	// 	z-index: 95;

	// 	&:before,
	// 	&:after {
	// 		display: block;
	// 		content: '';
	// 		width: 20px;
	// 		height: 40px;
	// 		background: #e10a07;
	// 	}

	// 	&:before {
	// 		transform: rotate(50deg);
	// 		border-radius: 0 50px 0 0;
	// 	}

	// 	&:after {
	// 		position: absolute;
	// 		right: -198px;
	// 		top: 0;
	// 		transform: rotate(-50deg);
	// 		border-radius: 50px 0 0 0;
	// 		/* background: pink; */
	// 	}
	// }

	// .left-bottom-sign {
	// 	position: absolute;
	// 	left: -270px;
	// 	bottom: -320px;
	// 	/*border: 100upx solid #d0d1fd;*/
	// 	border-radius: 50%;
	// 	padding: 90px;
	// }

	// .welcome {
	// 	position: relative;
	// 	left: 30px;
	// 	top: -55px;
	// 	font-size: 28px;
	// 	color: #555;
	// 	text-shadow: 1px 0px 1px rgba(0, 0, 0, 0.3);
	// }

	.input-content {
		padding: 0 20px;
	}

	// .input-item {
	// 	display: flex;
	// 	flex-direction: column;
	// 	align-items: flex-start;
	// 	justify-content: center;
	// 	padding: 0 30px;
	// 	background: $page-color-light;
	// 	height: 64px;
	// 	border-radius: 4px;
	// 	margin-bottom: 30px;

	// 	&:last-child {
	// 		margin-bottom: 0;
	// 	}

	// 	.tit {
	// 		height: 30px;
	// 		line-height: 28px;
	// 		font-size: $font-sm + 2upx;
	// 		color: $font-color-base;
	// 	}

	// 	input {
	// 		height: 40px;
	// 		font-size: $font-base + 2upx;
	// 		color: $font-color-dark;
	// 		width: 100%;
	// 	}
	// }

	.confirm-btn {
		width: 300px;
		height: 42px;
		line-height: 42px;
		border-radius: 30px;
		margin-top: 100px;
		background: #FF6D9E;
		color: #fff;
		// font-size: $font-lg;

		&:after {
			border-radius: 60px;
		}
	}

	
</style>