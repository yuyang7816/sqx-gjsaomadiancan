<template>
	<view class="container">
		<!-- #ifndef H5 -->

		<u-navbar :title="$t('login.home')" bgColor="transparent" :border="false" :border-bottom="false"
			@leftClick="leftClick" :autoBack="true"></u-navbar>
		<!-- #endif -->
		<image src="../../static/images/index/pagebg.png" class="bgpage"></image>


		<view class="wrapper">

			<view class="dlogin" v-if="tabIndex == 1">{{$t('login.home')}}</view>
			
			<view class="dlogin" v-if="tabIndex == 2">{{$t('login.yxlogin')}}</view>
			
			<view class="input-content">
				<view class="cu-form-group" v-if="tabIndex == 1" style="margin-bottom: 20px;border-radius: 10rpx">
					<view class="margin-right flex align-center" @click="goquhao">
						{{quhao}}
						<u-icon name="arrow-down-fill" style="margin-left: 10rpx;" color="#fffff" size="16"></u-icon>
					</view>
					<input type="number" :value="mobile" :placeholder="$t('login.plaphoe')" data-key="mobile"
						@input="inputChange" placeholder-style="font-size:26rpx;" />
				</view>
				<view class="cu-form-group" v-if="tabIndex == 2" style="margin-bottom: 20px;border-radius: 10rpx">
					<view class="title">{{$t('login.yxname')}}</view>
					<input type="text" :value="mobile" :placeholder="$t('login.emailpla')" maxlength="-1"
						data-key="mobile" @input="inputChange" placeholder-style="font-size:26rpx;" />
				</view>
				<view class="cu-form-group" style="border-radius: 10rpx; margin-bottom: 20px;">
					<view class="title" style="letter-spacing: 0rpx;">{{$t('login.mima')}}</view>
					<input type="password" :placeholder="$t('login.password')" maxlength="20" :value="password"
						data-key="password" @input="inputChange" @confirm="toLogin"
						placeholder-style="font-size:26rpx;" />
				</view>

			</view>

			<button class="confirm-btn" @click="toLogin">{{$t('login.home')}}</button>
			<view style="margin-top:30rpx;" :class="wjmmSel != '否'?'justify-between':''"
				class="flex align-center padding-lr-xl">
				<view>
					<text style="color:#FF6D9E" @click="register()">{{$t('login.register')}}</text>
				</view>
				<view class="" v-if="wjmmSel != '否'">
					<view class="" style="color: #FF6D9E" @click="forget">{{$t('login.wjmm')}}</view>
				</view>
			</view>
		</view>


	</view>
</template>

<script>
	export default {
		data() {
			return {
				mobile: '',
				code: '',
				sessionkey: '',
				flag: '1',
				password: '',
				weixinPhone: false,
				sending: false,
				sendDataList: {},
				phone: '',
				sendTime: '',
				count: 60,
				checked: false,
				tabIndex: 1,
				quhao: '',
				wjmmSel: '否',
				zyType: 'zh',
				invitation: ''
			};
		},
		onLoad(d) {
			if (d.index) {
				this.tabIndex = d.index;
				if (this.tabIndex == 2) {
					// 邮箱登录是否显示忘记密码	432
					this.$Request.get('/app/common/type/432').then(res => {
						if (res.code == 0) {
							if (res.data && res.data.value) {
								this.wjmmSel = res.data.value;
							}
						}
					});
				} else {
					this.wjmmSel = '是';
				}
			}
			uni.setNavigationBarTitle({
				title: this.$t('login.home')
			})
			this.sendTime = this.$t("login.ghmsg");
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
		onShow() {
			if (this.$queue.getData('zone')) {
				this.quhao = this.$queue.getData('zone');
				this.$queue.remove('zone')
			}
		},
		methods: {
			leftClick() {
				uni.navigateBack()
			},
			// 注册
			register() {
				uni.navigateTo({
					url: '/pages/login/register?index=' + this.tabIndex
				});
			},
			// 忘记密码
			forget() {
				uni.navigateTo({
					url: '/pages/login/forgetPwd?index=' + this.tabIndex
				});
			},
			goquhao() {
				uni.navigateTo({
					url: '/pages/login/countryCode'
				})
			},
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			radioChange(e) {
				console.log(e);
			},

			toLogin() {
				let openid = uni.getStorageSync('openId')
				const {
					mobile,
					password
				} = this;

				if (this.tabIndex == 1) {
					if (!mobile) {
						uni.showToast({
							title: "" + this.$t("login.plaphoe"),
							icon: 'none',
							duration: 1000
						})
						return;
					} else if (!password) {
						uni.showToast({
							title: "" + this.$t("login.password"),
							icon: 'none',
							duration: 1000
						})
						return;
					} else {
						uni.showLoading({
							title: "" + this.$t("login.denglu"),
						})
						let data = {
							password: password,
							phone: (this.quhao + '' + mobile),
							userType: 1,
							inviterCode: this.invitation
						}
						this.$Request.postT('/app/Login/registerCode', data).then(res => {
							if (res.code === 0) {
								uni.setStorageSync('token', res.token)
								uni.setStorageSync('userId', res.user.userId)
								uni.setStorageSync('userName', res.user.userName)
								uni.setStorageSync('avatar', res.user.avatar ? res.user.avatar :
									'../../static/images/logo.png')
								uni.setStorageSync('phone', res.user.phone)
								uni.setStorageSync('invitationCode', res.user.invitationCode)
								uni.setStorageSync('sex', res.user.sex)
								uni.setStorageSync('userId', res.user.userId)
								// uni.setStorageSync('openId', res.user.openId)
								// this.getWeixinInfo(this.sendDataList);
								uni.showToast({
									title: "" + this.$t("login.dengluok"),
									icon: 'none'
								})
								setTimeout(function() {
									uni.switchTab({
										url: '/pages/my/index'
									});
								}, 1000)


							} else {
								uni.hideLoading();
								uni.showToast({
									title: res.msg,
									icon: 'none',
									duration: 1000
								})
							}
						});
					}
				} else {
					if (!mobile) {
						uni.showToast({
							title: "" + this.$t("login.emailpla"),
							icon: 'none',
							duration: 1000
						})
						return;
					} else if (!password) {
						uni.showToast({
							title: "" + this.$t("login.password"),
							icon: 'none',
							duration: 1000
						})
						return;
					} else {
						uni.showLoading({
							title: "" + this.$t("login.denglu"),
						})

						this.$Request.post('/app/Login/emailLogin?email=' + mobile + '&inviterCode=' + this
							.invitation + '&password=' + password).then(res => {
							if (res.code === 0) {
								uni.setStorageSync('token', res.token)
								uni.setStorageSync('userId', res.user.userId)
								uni.setStorageSync('userName', res.user.userName)
								uni.setStorageSync('avatar', res.user.avatar ? res.user.avatar :
									'../../static/images/logo.png')
								uni.setStorageSync('phone', res.user.phone)
								uni.setStorageSync('invitationCode', res.user.invitationCode)
								uni.setStorageSync('sex', res.user.sex)
								uni.setStorageSync('userId', res.user.userId)
								uni.showToast({
									title: "" + this.$t("login.dengluok"),
									icon: 'none'
								})
								setTimeout(function() {
									uni.switchTab({
										url: '/pages/my/index'
									});
								}, 1000)


							} else {
								uni.hideLoading();
								uni.showToast({
									title: res.msg,
									icon: 'none',
									duration: 1000
								})
							}
						});
					}
				}
			},
			//校验邮箱格式
			checkEmail(email) {
				return RegExp(
						/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/)
					.test(email);
			},

		}
	};
</script>

<style lang="scss">
	button:after {
		border: none !important;
	}



	.headers {
		text-align: center;
	}

	.headers>image {
		width: 400upx;
		height: 400upx;
	}

	.footer {
		padding-left: 150rpx;
		margin-top: 32upx;
		font-size: 24upx;
		color: #666666;
		text-align: center;
		display: flex;
	}

	page {
		background: #fff;
	}

	.cu-form-group {
		background: #F2F2F2;
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

	.bgpage {
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		width: 100%;
		height: 100%;
	}

	.dlogin {
		font-family: PingFang SC;
		font-weight: bold;
		font-size: 90rpx;
		color: #FF6D9E;
		margin: 180rpx 0 0 60rpx;
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
		padding-bottom: 20px;
	}


	.input-content {
		padding: 0 20px;
		margin-top: 140rpx;
	}



	.confirm-btn {
		width: 90%;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 15rpx;
		margin: 40px auto 0;
		background: #FF6D9E;
		color: #fff;
		// font-size: $font-lg;

		&:after {
			border-radius: 60px;
		}
	}
</style>