<template>
	<view class="container">
		<view class="wrapper">
			<!-- <view style="text-align: center;">
				<image src="../static/logo.png" style="border-radius: 200upx;width: 300upx;height: 300upx;"></image>
			</view> -->
			<view style="padding-left:60upx;" class="margin-tb-lg">
				<view style="color:#333333;font-size:42upx;">门店端登录体验更多功能</view>
				<view style="color:#999999" class="text-sm margin-top-xs">未入驻过的请联系管理员</view>
			</view>

			<view class="input-content">
				<view class="cu-form-group" style="border-bottom: 2upx solid whitesmoke;margin-bottom: 20px;">
					<!-- <view class="title text-black">账号</view> -->
					<input type="text" :value="phone" placeholder="请输入门店管理员账号" maxlength="11" data-key="phone"
						@input="inputChange" />
				</view>
				<view class="cu-form-group" style="border-bottom: 2upx solid whitesmoke;margin-bottom: 20px;">
					<!-- <view class="title text-black">密码</view> -->
					<input type="password" placeholder="请输入门店管理员密码" maxlength="20" :value="password" data-key="password"
						@input="inputChange" @confirm="toLogin" />
					<!-- <text class="send-msg" @click="forget">忘记密码</text> -->
				</view>


				<view class="cu-form-group flex align-center" style="border-bottom: 2upx solid whitesmoke;margin-bottom: 20px;">
					<!-- <view class="title text-black">验证码</view> -->
					<input type="text" placeholder="请输入底部图片验证码" maxlength="20" :value="captcha" data-key="captcha"
						@input="inputChange" @confirm="toLogin" />
					<view>
						<image @click="getCaptcha" :src="captchaPath" style="height: 60rpx;width: 150rpx;"></image>
					</view>
				</view>


			</view>
			<button class="confirm-btn" @click="toLogin">立即登录</button>


		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				phone: '',
				password: '',
				banners: [],
				invitation: '',
				loginName: '',
				sending: false,
				sendTime: '获取验证码',
				count: 60,
				captcha: '',
				captchaPath: '',
				uuid: ''
			}
		},
		onLoad() {
			this.getCaptcha()
		},
		methods: {
			// 获取验证码
			getCaptcha() {
				this.uuid = this.$queue.getUUID()

				// this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.uuid}`)
				// this.captchaPath = this.$Request.getA('/captcha.jpg?uuid='+this.uuid).then(res => {

				// });
				uni.downloadFile({
					url: 'https://diancanshop.xianmxkj.com/sqx_fast/captcha.jpg?uuid=' + this.uuid,
					success: (res) => {
						console.log(res)
						this.captchaPath = res.tempFilePath
					}
				})
			},
			forget() {
				uni.navigateTo({
					url: '/pages/public/forgetPwd'
				});
			},
			register() {
				uni.navigateTo({
					url: '/pages/public/register'
				});
			},
			inputChange(e) {
				const key = e.currentTarget.dataset.key;
				this[key] = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},

			toLogin() {
				let that = this
				that.$queue.loginClear();
				let openid = that.$queue.getData("openid");
				const {
					phone,
					password,
					captcha
				} = that;
				if (!phone) {
					that.$queue.showToast("请输入门店管理员账号");
				} else if (!password) {
					that.$queue.showToast("请输入门店管理员密码");
				} else if (!captcha) {
					that.$queue.showToast("请输入图片验证码");
				} else {
					that.$queue.showLoading("正在登录中...");
					that.$Request.postJsonA("/sys/login", {
						password: password,
						username: phone,
						captcha: captcha,
						uuid: that.uuid,
					}).then(res => {
						if (res.code == 0) {

							that.$queue.setData("shopToken", res.token);
							uni.hideLoading()
							setTimeout(function() {
								that.getUserInfo()
							}, 10)
						} else {
							this.getCaptcha()
							uni.hideLoading();
							this.$queue.showToast(res.msg);
						}
					});
				}
			},
			getUserInfo() {
				this.$Request.getA("/sys/user/info").then(res => {
					if (res.code == 0) {
						this.$queue.setData("shopUserId", res.user.userId);
						this.$queue.setData("shopMobile", res.user.mobile);
						this.$queue.setData("shopUserName", res.user.userName);

						// this.$queue.setData("avatar", res.user.avatar);
						// this.$queue.setData("invitationCode", res.user.invitationCode);
						// this.$queue.setData("inviterCode", res.user.inviterCode);
						uni.reLaunch({
							url: '/shop/index'
						})
					}
				});
			},
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
						// this.isVip = res.data
						console.log(res.data)
						this.$queue.setData("isVip", res.data);
					}
				});
			}
		},

	}
</script>

<style lang='scss'>
	page {
		height: 100%;
		background: #FFFFFF !important;
	}

	.send-msg {
		border-radius: 30px;
		/* color: black; */
		background: white;
		height: 30px;
		font-size: 14px;
		line-height: 30px;
	}

	.container {
		top: 0;
		padding-top: 32upx;
		position: relative;
		width: 100%;
		height: 100%;
		overflow: hidden;
		/* background: #1E1F31 !important; */
	}

	.wrapper {
		position: relative;
		z-index: 90;
		/* background: #1E1F31; */
		padding-bottom: 32upx;
	}


	.input-content {
		/* margin-top: 300upx; */
		/* padding-top: 300upx; */
		padding: 32upx 50upx;

	}


	.confirm-btn {
		width: 600upx;
		height: 80upx;
		line-height: 80upx;
		border-radius: 60upx;
		margin-top: 32upx;
		background: #FF6D9D;
		color: #fff;
		font-size: 32upx;

		&:after {
			border-radius: 60px;
		}
	}
</style>
