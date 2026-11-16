<template>
	<view class="container">
		<view class="wrapper">
			<!-- <view style="text-align: center;">
				<image src="../static/logo.png" style="border-radius: 200upx;width: 300upx;height: 300upx;"></image>
			</view> -->
			<view style="padding-left:60upx;" class="margin-tb-lg">
				<view style="color:#333333;font-size:42upx;">{{$t('dclogin.tit')}}</view>
				<view style="color:#999999" class="text-sm margin-top-xs">{{$t('dclogin.tits')}}</view>
			</view>

			<view class="input-content">
				<view class="cu-form-group" style="border-bottom: 2upx solid whitesmoke;margin-bottom: 20px;">
					<!-- <view class="title text-black">账号</view> -->
					<input type="text" :value="phone" :placeholder="$t('dclogin.plazh')" maxlength="11" data-key="phone"
						@input="inputChange" />
				</view>
				<view class="cu-form-group" style="border-bottom: 2upx solid whitesmoke;margin-bottom: 20px;">
					<!-- <view class="title text-black">密码</view> -->
					<input type="password" :placeholder="$t('dclogin.plamima')" maxlength="20" :value="password" data-key="password"
						@input="inputChange" @confirm="toLogin" />
					<!-- <text class="send-msg" @click="forget">忘记密码</text> -->
				</view>


				<view class="cu-form-group flex align-center"
					style="border-bottom: 2upx solid whitesmoke;margin-bottom: 20px;">
					<!-- <view class="title text-black">验证码</view> -->
					<input type="text" :placeholder="$t('dclogin.playzm')" maxlength="20" :value="captcha" data-key="captcha"
						@input="inputChange" @confirm="toLogin" />
					<view> 
						<image @click="getCaptcha" :src="captchaPath" style="height: 60rpx;width: 200rpx;" ></image>
					</view>
				</view>
				<view style="text-align: center;">
					<view class="footer flex align-center flex-wrap">
						<u-checkbox active-color="#FF6D9D" shape="circle" v-model="checked"></u-checkbox>
						<text>{{$t('purse.czxy')}}</text>
						<!-- 协议地址 -->
						<navigator url="/my/setting/mimi" open-type="navigate">《{{$t('set.ysxy')}}》</navigator>
						{{$t('index.he')}}
						<navigator url="/my/setting/xieyi" open-type="navigate">《{{$t('set.yhxy')}}》</navigator>
					</view>
				</view>


			</view>
			<button class="confirm-btn" @click="toLogin">{{$t('login.ljdl')}}</button>


		</view>
	</view>
</template>

<script>
	import config from '../common/config.js'
	export default {
		data() {
			return {
				checked: false,
				phone: '',
				password: '',
				banners: [],
				invitation: '',
				loginName: '',
				sending: false,
				
				count: 60,
				captcha: '',
				captchaPath: '',
				uuid: ''
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('dclogin.title')
			})
			this.getCaptcha()
		},
		methods: {
			// 获取验证码
			getCaptcha() {
				this.uuid = this.$queue.getUUID()

				uni.downloadFile({
					url: config.APIHOST + '/captcha.jpg?uuid=' + this.uuid,
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
					// that.$queue.showToast("请输入门店管理员账号");
					uni.showToast({
						title:that.$t('dclogin.plazh'),
						icon:'none'
					})
				} else if (!password) {
					// that.$queue.showToast("请输入门店管理员密码");
					uni.showToast({
						title:that.$t('dclogin.plamima'),
						icon:'none'
					})
				} else if (!captcha) {
					// that.$queue.showToast("请输入图片验证码");
					uni.showToast({
						title:that.$t('login.tpyzm'),
						icon:'none'
					})
				} else {
					if (!that.checked) {
						uni.showToast({
							title: that.$t('purse.czxy')+that.$t('set.ysxy') +that.$t('index.he')+that.$t('set.yhxy'),//'请阅读并同意《隐私政策》和《用户服务协议》'
							icon: 'none'
						})
						return
					}
					// that.$queue.showLoading("正在登录中...");
					uni.showLoading({
						title:that.$t('login.denglu')
					})
					that.$Request.postJsonA("/sys/login", {
						password: password,
						username: phone,
						captcha: captcha,
						uuid: that.uuid,
					}).then(res => {
						if (res.code == 0) {
							that.$queue.setData("shopToken", res.token);
							uni.hideLoading()
							that.getUserInfo()
						} else {
							that.getCaptcha()
							uni.hideLoading();
							that.$queue.showToast(res.msg);
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

						uni.navigateBack()
					}
				});
			},
			getIsVip() {
				this.$Request.get("/app/UserVip/isUserVip").then(res => {
					if (res.code == 0) {
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

	::v-deep .u-checkbox__label {
		margin-left: 0 !important;
		margin-right: 10rpx !important;
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