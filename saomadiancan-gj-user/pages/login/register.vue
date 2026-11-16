<template>
	<view class="container">
		<!-- #ifndef H5 -->
		<u-navbar :title="$t('login.registers')" bgColor="transparent" :border="false" :border-bottom="false"
			@leftClick="leftClick" :autoBack="true"></u-navbar>

		<!-- #endif -->

		<image src="../../static/images/index/pagebg.png" class="bgpage"></image>

		<view class="wrapper">

			<view class="dlogin">{{$t('login.registers')}}</view>

			<view class="input-content">
				<view class="cu-form-group" style="margin-bottom: 20px;border-radius: 12rpx">
					<view class="title text-black">{{$t('user.name')}}</view>
					<input :value="userName" :placeholder="$t('user.planame')" data-key="userName" @input="inputChange" />
				</view>
				<view class="cu-form-group" v-if="tabIndex == 1" style="margin-bottom: 20px;border-radius: 12rpx">
					<view class="margin-right flex align-center" @click="goquhao">
						{{quhao}}
						<u-icon name="arrow-down-fill" style="margin-left: 10rpx;" color="#fffff" size="16"></u-icon>
					</view>
					<input type="number" :value="mobile" :placeholder="$t('login.plaphoe')" data-key="mobile"
						@input="inputChange" placeholder-style="font-size:26rpx;" />
				</view>

				<view class="cu-form-group" v-if="tabIndex == 2" style="margin-bottom: 20px;border-radius: 12rpx">
					<view class="title">{{$t('login.yxname')}}</view>
					<input type="text" :value="mobile" :placeholder="$t('login.emailpla')" maxlength="-1"
						data-key="mobile" @input="inputChange" placeholder-style="font-size:26rpx;" />
				</view>

				<view class="cu-form-group" style="margin-bottom: 20px;border-radius: 12rpx">
					<text class="title">{{$t('login.code')}}</text>
					<input type="number" :value="code" :placeholder="$t('login.codeText')" maxlength="6" data-key="code"
						@input="inputChange" @confirm="toLogin" placeholder-style="font-size:26rpx;" />
					<button class="send-msg" @click="sendMsg" :disabled="sending">{{ sendTime }}</button>
				</view>

				<view class="cu-form-group" style="margin-bottom: 20px;border-radius: 12rpx">
					<text class="title">{{$t('login.pwd')}}</text>
					<input type="password" :value="password" :placeholder="$t('login.pwd')"
						placeholder-class="input-empty" maxlength="20" minlength="6" data-key="password"
						@input="inputChange" @confirm="toLogin" placeholder-style="font-size:26rpx;" />
				</view>
				<view class="cu-form-group" style="margin-bottom: 20px;border-radius: 12rpx">
					<text class="title">{{$t('login.invitationcode')}}</text>
					<input type="text" v-if="registerCode == '是'" :value="invitation"
						:placeholder="$t('login.invitationcodes')" data-key="invitation" maxlength="20"
						@input="inputChange" @confirm="toLogin" placeholder-style="font-size:26rpx;" />
					<input type="text" v-if="registerCode == '否'" :value="invitation"
						:placeholder="$t('login.invitationcode')+'('+$t('login.kong')+')'" data-key="invitation"
						maxlength="20" @input="inputChange" @confirm="toLogin" placeholder-style="font-size:24rpx;" />
				</view>
			</view>
			<button class="confirm-btn" @click="toLogin">{{$t('login.register')}}</button>
			<view class="footer">
				<text @tap="isShowAgree" class="cuIcon"
					:class="showAgree ? 'cuIcon-radiobox' : 'cuIcon-round'">{{$t('login.ty')}}</text>
				<!-- 协议地址 -->
				<text @click.stop="goTo('/my/setting/mimi')" style="color: #FF6D9E">《{{$t("set.ysxy")}}》</text>
				{{$t('index.he')}}
				<text @click.stop="goTo('/my/setting/xieyi')" style="color: #FF6D9E">《{{$t("set.yhxy")}}》</text>

			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				showAgree: false,
				code: '',
				mobile: '',
				password: '',
				sending: false,
				sendTime: '', //获取验证码
				count: 60,
				relation: "",
				invitation: '',
				registerCode: '否',
				tabIndex: 1,
				quhao: '',
				zyType: 'zh',
				qdCodeion: '',
				userName: ''
			};
		},
		onLoad(d) {
			if (uni.getStorageSync('qdCodeion')) {
				this.qdCodeion = uni.getStorageSync('qdCodeion')
			}
			if (d.index) {
				this.tabIndex = d.index;
			}
			uni.setNavigationBarTitle({
				title: this.$t('login.registers')
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
			let relation = uni.getStorageSync('invitation')
			if (relation) {
				this.relation = relation;
				this.invitation = relation;
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
			goTo(url) {
				uni.navigateTo({
					url
				});
			},
			isShowAgree() {
				//是否选择协议
				this.showAgree = !this.showAgree;
			},
			showMa() {
				//查询官方邀请码
				this.$Request.getT('/common/type/88').then(res => {
					if (res.code == 0) {
						this.invitation = res.data.value;
					}
				});
			},
			sendMsg() {
				const {
					mobile
				} = this;
				if (this.tabIndex == 1) {
					if (!mobile) {
						uni.showToast({
							title: "" + this.$t("login.plaphoe"),
							icon: 'none',
							duration: 1000
						})
					} else {
						this.$queue.showLoading("" + this.$t("login.lamsg"));
						let quhao = this.quhao;
						
						this.$Request.get('/app/Login/sendMsg/' + (quhao + '' + mobile) + '/login').then(res => {
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
					this.$Request.post('/app/Login/sendEmailMsg?email=' + mobile + '&type=1&language=' + this.zyType)
						.then(
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
				});
			},
			toLogin() {
				const {
					mobile,
					password,
					code,
					showAgree,
					invitation,
					registerCode,
					qdCodeion,
					userName
				} = this;

				if (this.tabIndex == 1) {
					if (!userName) {
						uni.showToast({
							title: "" + this.$t("user.planame"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (!mobile) {
						uni.showToast({
							title: "" + this.$t("login.plaphoe"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (!code) {
						uni.showToast({
							title: "" + this.$t("login.codeText"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (!password) {
						uni.showToast({
							title: "" + this.$t("login.password"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (password.length < 6) {
						uni.showToast({
							title: "" + this.$t("login.minPwdText"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (invitation.length == 0 && registerCode == '是') {
						uni.showToast({
							title: "" + this.$t("login.invitationcodes"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (!showAgree) {
						uni.showToast({
							title: "" + this.$t("login.ty") + '《' + "" + this.$t("set.ysxy") + '》' + "" + this
								.$t(
									"index.he") + '《' + this.$t("set.yhxy") + '》',
							icon: 'none',
							duration: 1000
						})
						return
					}
					this.logining = true;
					uni.showLoading({
						title: '' + this.$t("login.registerlog")
					})
					let platform = ''
					// #ifdef APP
					platform = 'app'
					// #endif
					// #ifdef H5
					platform = 'h5'
					// #endif
					let data = {
						password: password,
						phone: (this.quhao + '' + mobile),
						msg: code,
						inviterCode: invitation,
						platform: platform,
						userType: '1',
						userName: userName
					}
					this.$Request.postT('/app/Login/registApp', data).then(res => {
						if (res.code === 0) {
							// this.$queue.remove('invitation');
							uni.setStorageSync('token', res.token)
							uni.setStorageSync('userId', res.user.userId)
							uni.setStorageSync('phone', res.user.phone)
							uni.setStorageSync('invitationCode', res.user.invitationCode)

							uni.showToast({
								title: '' + this.$t("login.registerlogok"),
								icon: 'none'
							})
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/index/index'
								});
							}, 1000)

						} else {
							uni.hideLoading();
							uni.showModal({
								showCancel: false,
								title: '' + this.$t("login.registerlogerr"),
								cancelText: this.$t('index.qx'),
								confirmText: this.$t('index.sure'),
								content: res.msg
							});
						}
					});
				} else {
					if (!mobile) {
						uni.showToast({
							title: "" + this.$t("login.emailpla"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (!code) {
						uni.showToast({
							title: "" + this.$t("login.codeText"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (!password) {
						uni.showToast({
							title: "" + this.$t("login.password"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (password.length < 6) {
						uni.showToast({
							title: "" + this.$t("login.minPwdText"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (invitation.length == 0 && registerCode == '是') {
						uni.showToast({
							title: "" + this.$t("login.invitationcodes"),
							icon: 'none',
							duration: 1000
						})
						return
					}
					if (!showAgree) {
						uni.showToast({
							title: "" + this.$t("login.ty") + '《' + "" + this.$t("set.ysxy") + '》' + "" + this
								.$t(
									"index.he") + '《' + this.$t("set.yhxy") + '》',
							icon: 'none',
							duration: 1000
						})
						return
					}
					this.logining = true;
					uni.showLoading({
						title: '' + this.$t("login.registerlog")
					})
					let platform = ''
					let sysPhone = 1
					// #ifdef APP
					platform = 'app'
					if (plus.os.name.toLowerCase() === 'android') {
						sysPhone = 1;
					} else {
						sysPhone = 2;
					}
					// #endif
					// #ifdef H5
					platform = 'h5'
					// #endif
					this.$Request.postT('/app/Login/emailRegister?password=' + password + '&email=' + mobile +
						'&code=' +
						code +
						'&inviterCode=' + invitation + '&platform=' + platform + '&sysPhone=' + sysPhone +
						'&userType=1').then(
						res => {
							if (res.code === 0) {

								uni.setStorageSync('token', res.token)
								uni.setStorageSync('userId', res.user.userId)
								uni.setStorageSync('phone', res.user.phone)
								uni.setStorageSync('invitationCode', res.user.invitationCode)

								uni.showToast({
									title: '' + this.$t("login.registerlogok"),
									icon: 'none'
								})
								setTimeout(function() {
									uni.switchTab({
										url: '/pages/index/index'
									});
								}, 1000)

							} else {
								uni.hideLoading();
								uni.showModal({
									showCancel: false,
									title: '' + this.$t("login.registerlogerr"),
									cancelText: this.$t('index.qx'),
									confirmText: this.$t('index.sure'),
									content: res.msg
								});
							}
						});

				}
			},
			//校验邮箱格式
			checkEmail(email) {
				return RegExp(
						/^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/)
					.test(email);
			},
			getUserInfo(userId, token) {
				this.$u.api.userinfo().then(res => {
					if (res.code === 0) {
						// #ifdef APP-PLUS
						if (uni.getSystemInfoSync().platform == "android") {
							let clientid = plus.push.getClientInfo().clientid;
							this.$Request.postT('/appLogin/updateClientId?clientId=' + clientid + '&userId=' +
								userId).then(res => {

							});
						}
						// #endif
						uni.setStorageSync('image_url', res.data.imageUrl ? res.data.imageUrl :
							'/static/img/common/logo.jpg')
						uni.setStorageSync('relation_id', res.data.relationId)
						uni.setStorageSync('relation', res.data.invitationCode)
						uni.setStorageSync('grade', res.data.grade)
						uni.setStorageSync('phone', res.data.phone)
						uni.setStorageSync('token', res.token)
						uni.setStorageSync('userId', res.data.userId)
						uni.setStorageSync('isInvitation', res.data.isInvitation)
						uni.setStorageSync('userName', res.data.userName ? res.data.userName : res.data.phone)


						uni.switchTab({
							url: '/pages/index/index'
						});
					} else {
						uni.showModal({
							showCancel: false,
							content: res.msg,
							title: this.$t('modalTitle.wxts'),
							cancelText: this.$t('index.qx'),
							confirmText: this.$t('index.sure'),
						});
						this.$queue.logout();
					}
					uni.hideLoading();
				})

			}
		}
	};
</script>

<style lang="scss">
	button:after {
		border: none !important;
	}

	page {
		background: #fff;
	}

	.footer {
		// margin-top: 32upx;
		// display: flex;
		// align-items: center;
		// justify-content: center;

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

	.send-msg {
		max-width: 200rpx;
		border-radius: 30px;
		color: #FF6D9E;
		height: 50rpx;
		font-size: 20rpx;
		line-height: 53rpx;
		border: 1px solid #FF6D9E;
		background: #FFFFFF;
	}

	.send-msgs {
		border-radius: 30px;
		color: #999999;
		height: 30px;

		font-size: 14px;
		line-height: 30px;
		background: white;
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
		margin-top: 140rpx;
	}


	.confirm-btn {
		width: 90%;
		height: 88rpx;
		line-height: 88rpx;
		border-radius: 15rpx;
		margin: 40px auto 0;
		background: #FF6D9E;
		color: #fff;
		font-size: 32rpx;

		&:after {
			border-radius: 60px;
		}
	}

	.cu-form-group {
		background: #F2F2F2;
	}
</style>