<template>
	<view class="container">
		<!-- #ifndef H5 -->
			<u-navbar :title="$t('login.newmima')" bgColor="transparent" :border="false" :border-bottom="false"
				@leftClick="leftClick" :autoBack="true"></u-navbar>
		<!-- #endif -->
		<image src="../../static/images/index/pagebg.png" class="bgpage"></image>

		<view class="wrapper">

			<view class="dlogin">{{$t('login.newmima')}}</view>

			<view class="input-content">
				<view class="cu-form-group" v-if="tabIndex == 1" style="margin-bottom: 20px;border-radius: 12rpx">
					<view class="margin-right flex align-center" @click="goquhao">
						{{quhao}}
						<u-icon name="arrow-down-fill" style="margin-left: 10rpx;" color="#fffff" size="20"></u-icon>
					</view>
					<input type="number" :value="mobile" :placeholder="$t('login.plaphoe')" data-key="mobile"
						@input="inputChange" placeholder-style="font-size:26rpx;" />
				</view>
				<view class="cu-form-group" v-if="tabIndex == 2" style="margin-bottom: 20px;border-radius: 12rpx">
					<view class="title">{{$t('login.yxname')}}</view>
					<input type="text" :value="mobile" :placeholder="$t('login.emailpla')" maxlength="-1" data-key="mobile"
						@input="inputChange" placeholder-style="font-size:26rpx;" />
				</view>
				<view class="cu-form-group" style="margin-bottom: 20px;border-radius: 12rpx">
					<text class="title">{{$t('login.code')}}</text>
					<input type="number" :value="code" :placeholder="$t('login.codeText')" maxlength="6" data-key="code"
						@input="inputChange" @confirm="toLogin" placeholder-style="font-size:26rpx;" />
					<button class="send-msg" @click="sendMsg" :disabled="sending">{{sendTime}}</button>
				</view>
				<view class="cu-form-group" style="margin-bottom: 20px;border-radius: 12rpx">
					<text class="title">{{$t('login.pwd')}}</text>
					<input type="password" :value="password" :placeholder="$t('login.pwd')" placeholder-class="input-empty"
						maxlength="20" minlength="6" data-key="password" @input="inputChange" @confirm="toLogin" />
				</view>
			</view>
			<button class="confirm-btn" @click="toLogin">{{$t('login.newmima')}}</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				background: {
					backgroundColor: 'transparent',
				},

				code: '',
				mobile: '',
				password: '',
				sending: false,
				sendTime: '',
				count: 60,
				logining: false,
				tabIndex: 1,
				quhao: '',
				zyType: 'zh'
			}
		},
		onLoad(d) {
			if (d.index) {
				this.tabIndex = d.index;
			}
			uni.setNavigationBarTitle({
				title: this.$t('login.newmima')
			})
			this.sendTime = this.$t("login.code");
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
			goquhao() {
				uni.navigateTo({
					url: '/pages/login/countryCode'
				})
			},
			sendMsg() {
				const {
					mobile
				} = this;
				if (this.tabIndex == 1) {
					if (!mobile) {
						this.$queue.showToast("" + this.$t("login.plaphoe"));
						return;
					} else {
						this.$queue.showLoading("" + this.$t("login.lamsg"));
						let quhao = this.quhao;
						// if (this.zyType === 'zh') {
						// 	if(this.quhao === '+86'){
						// 		quhao = ''
						// 	}
						// }
						this.$Request.get('/app/Login/sendMsg/' + (quhao + '' + mobile) + '/forget').then(res => {
							if (res.code === 0) {
								this.sending = true;

								uni.showToast({
									title: '' + this.$t("login.yanok"),
									icon: 'none',
									duration: 1000
								})
								this.countDown();
								uni.hideLoading();
							} else {
								uni.hideLoading();
								uni.showModal({
									showCancel: false,
									title: this.$t('modalTitle.wxts'),
									cancelText: this.$t('index.qx'),
									confirmText: this.$t('index.sure'),
									content: res.msg ? res.msg : '' + this.$t("login.plamsu")
								});
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
						return
					}

					this.$queue.showLoading("" + this.$t("login.lamsg"));
					this.$Request.post('/app/Login/sendEmailMsg?email=' + mobile + '&type=1&language=' + this.zyType).then(
						res => {
							if (res.code === 0) {
								this.sending = true;
								uni.showToast({
									title: '' + this.$t("login.yanok"),
									icon: 'none',
									duration: 1000
								})
								this.countDown();
								uni.hideLoading();
							} else {
								uni.hideLoading();
								uni.showModal({
									showCancel: false,
									title: this.$t('modalTitle.wxts'),
									cancelText: this.$t('index.qx'),
									confirmText: this.$t('index.sure'),
									content: res.msg ? res.msg : '' + this.$t("login.plamsu")
								});
							}
						});
				}
			},
			countDown() {
				const {
					count
				} = this;
				if (count === 1) {
					this.count = 60;
					this.sending = false;
					this.sendTime = '' + this.$t("login.code")
				} else {
					this.count = count - 1;
					this.sending = true;
					this.sendTime = count - 1 + 's';
					setTimeout(this.countDown.bind(this), 1000);
				}
			},
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},


			navTo(url) {
				uni.navigateTo({
					url
				})
			},
			//校验邮箱格式
			checkEmail(email) {
				return RegExp(
						/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/)
					.test(email);
			},
			toLogin() {
				const {
					mobile,
					password,
					code
				} = this;
				if (this.tabIndex == 1) {
					if (!mobile) {
						this.$queue.showToast("" + this.$t("login.plaphoe"));
					} else if (!code) {
						this.$queue.showToast("" + this.$t("login.codeText"))
					} else if (!password) {
						this.$queue.showToast("" + this.$t("login.password"));
					} else if (password.length < 6) {
						this.$queue.showToast("" + this.$t("login.minPwdText"));
					} else {
						this.logining = true;
						this.$queue.showLoading("" + this.$t("login.updetemima"));
						let data = {
							pwd: password,
							phone: (this.quhao + '' + mobile),
							msg: code
						}
						this.$Request.postT('/app/Login/forgetPwd', data).then(res => {
							uni.hideLoading();
							if (res.code === 0) {
								uni.showToast({
									title: '' + this.$t("login.czmm"),
									icon: 'none'
								});
								setTimeout(function() {
									uni.redirectTo({
										url: '/pages/login/login'
									});
								}, 1000)

							} else {

								uni.showModal({
									showCancel: false,
									title: '' + this.$t("login.czmmno"),
									cancelText: this.$t('index.qx'),
									confirmText: this.$t('index.sure'),
									content: res.msg,
								});
							}
						});
					}
				} else {
					if (!mobile) {
						this.$queue.showToast("" + this.$t("login.emailpla"));
					} else if (!code) {
						this.$queue.showToast("" + this.$t("login.codeText"))
					} else if (!password) {
						this.$queue.showToast("" + this.$t("login.password"));
					} else if (password.length < 6) {
						this.$queue.showToast("" + this.$t("login.minPwdText"));
					} else {
						this.logining = true;
						this.$queue.showLoading("" + this.$t("login.updetemima"));
						this.$Request.postT('/app/Login/emailForgetPwd?pwd=' + password + '&email=' + mobile + '&msg=' +
							code).then(
							res => {
								uni.hideLoading();
								if (res.code === 0) {
									uni.showToast({
										title: '' + this.$t("login.czmm"),
										icon: 'none'
									});
									setTimeout(function() {
										uni.navigateTo({
											url: '/pages/login/login'
										});
									}, 1000)

								} else {

									uni.showModal({
										showCancel: false,
										title: '' + this.$t("login.czmmno"),
										cancelText: this.$t('index.qx'),
										confirmText: this.$t('index.sure'),
										content: res.msg,
									});
								}
							});
					}
				}
			}
		},
	}
</script>

<style lang='scss' scoped>
	page {
		background: #fff;
	}

	.send-msg {
		max-width: 240rpx;
		border-radius: 30px;
		color: #FF6D9E !important;
		/* height: 50rpx; */
		font-size: 20rpx;
		/* line-height: 53rpx; */
		border: 1px solid #FF6D9E;
		background: #FFFFFF;

		&:after {
			color: #FF6D9E;
		}
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
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100vh;
		overflow: hidden;
		background: #fff;
	}

	.wrapper {
		position: relative;
		z-index: 90;

		padding-bottom: 20px;
	}


	.input-content {
		padding: 0 20px;
		margin-top: 120rpx;
	}



	.confirm-btn {
		width: 90%;
		height: 88rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border-radius: 12rpx;
		margin: 40px auto 0;
		background: #FF6D9E;

		color: #fff;
		font-size: 32rpx;

		&:after {
			border-radius: 60px;
		}
	}
</style>