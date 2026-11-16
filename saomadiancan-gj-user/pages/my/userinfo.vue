<template>
	<view>
		<view class="usermain">
			<view class="usermain-item ">
				<view>{{$t('user.avatr')}}</view>
				<view>
					<!-- #ifdef MP-WEIXIN -->
					<button open-type="chooseAvatar" @chooseavatar="onChooseAvatar"
						style="width: 111rpx;height: 111rpx;border-radius: 50%;">
						<image :src="avatar?avatar:'../../static/logo.png'" mode=""
							style="width: 111rpx;height: 111rpx;border-radius: 50%;">
						</image>
					</button>
					<!-- #endif -->
					<!-- #ifndef MP-WEIXIN -->
					<image @click="show = true" :src="avatar?avatar:'../../static/logo.png'" mode=""
						style="width: 111rpx;height: 111rpx;border-radius: 50%;">
					</image>
					<!-- #endif -->
				</view>
			</view>
			<view class="usermain-item item-padding ">
				<view>{{$t('user.name')}}</view>
				<view>
					<view class="cu-form-group">
						<input v-model="userName" type="nickname" :placeholder="$t('user.planame')" />
					</view>
				</view>
			</view>
			<view class="usermain-item item-padding ">
				<view>{{$t('user.age')}}</view>
				<view>
					<view class="cu-form-group">
						<input v-model="age" :placeholder="$t('user.plaage')"/>
					</view>
				</view>
			</view>

			<view class="usermain-item item-padding ">
				<view>{{$t('user.phone')}}</view>
				<view>
					<view class="cu-form-group">
						<input :disabled="true" v-model="phone" :placeholder="$t('user.plaphone')" />
					</view>
				</view>
			</view>
			<view class="usermain-item item-padding ">
				<view>{{$t('user.sex')}}</view>
				<view>
					<view class="cu-form-group">
						<u-radio-group v-model="sex">
							<u-radio shape="circle" :name="1">{{$t('user.man')}}</u-radio>
							<u-radio shape="circle" active-color="red" :name="2">{{$t('user.woman')}}</u-radio>
						</u-radio-group>
					</view>
				</view>
			</view>
		</view>
		<view class="footer-btn">
			<view class="usermain-btn" @click="messagebtn()">{{$t('user.save')}}</view>
		</view>

		<u-action-sheet :list="list" v-model="show" :cancelText="cancelText" @click="uploadImg"
			@close="show = false"></u-action-sheet>
	</view>
</template>

<script>
	import configdata from '../../common/config.js';
	export default {
		data() {
			return {
				phone: '',
				avatar: '../../static/logo.png',
				userName: '',
				userId: '',
				status: '',
				sex: 1,
				age: 0,

				list: [{
					text: '', //查看头像
				}, {
					text: '' //从相册选择图片
				}],
				show: false,
				cancelText: ''
			};
		},
		onLoad(e) {
			uni.setNavigationBarTitle({
				title: this.$t('user.title')
			})
			this.cancelText = this.$t('index.qx');
			this.list[0].text = this.$t('user.cktx');
			this.list[1].text = this.$t('user.chageImg');
			this.getUserInfo()
		},
		methods: {
			//微信填写能力获取头像
			onChooseAvatar(e) {
				console.log(e.detail.avatarUrl)
				let that = this;
				let userId = this.$queue.getData('userId');
				let token = uni.getStorageSync('token');
				uni.showLoading({
					title: that.$t('loadingText.scz')//'上传中...'
				});
				uni.uploadFile({
					// url: config.APIHOST1 + '/alioss/upload', //仅为示例，非真实的接口地址
					url: that.config("APIHOST1") + '/alioss/upload', //仅为示例，非真实的接口地址
					filePath: e.detail.avatarUrl,
					header: {
						token: token
					},
					name: 'file',
					success: uploadFileRes => {
						let url = JSON.parse(uploadFileRes.data).data;
						that.$Request.postT(
							'/app/user/updateUserImageUrl?avatar=' + url).then(
							res => {
								uni.hideLoading();
								if (res.code === 0) {
									// that.$queue.showToast(
									// 	"更新成功");
									uni.showToast({
										title:this.$t('tastText.gxcg'),
										icon:'none'
									})
									that.getUserInfo()
								}
							});
					}
				});

			},
			uploadImg(index) {
				let token = uni.getStorageSync('token')
				console.log(index)
				if (!token) {
					this.goLoginInfo();
					return;
				}
				let that = this;
				var url = null;
				if (index === 0) {
					// 用户点击了预览当前图片
					// 可以自己实现当前头像链接的读取
					let url = that.avatar;
					let arr = []
					arr.push(url)
					uni.previewImage({
						// 预览功能图片也必须是数组的
						urls: arr
					})
				} else if (index === 1) {
					uni.chooseImage({
						count: 1, //默认9
						sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
						sourceType: ['album'], //从相册选择
						success: function(res) {
							uni.showLoading({
								title: '上传中...'
							});
							let token = uni.getStorageSync('token');
							let userId = uni.getStorageSync('userId');
							uni.uploadFile({
								url: 'https://dcgj.xianmxkj.com/sqx_fast/alioss/upload', //真实的接口地址
								// url: that.config("APIHOST1") +'/alioss/upload', //真实的接口地址
								filePath: res.tempFilePaths[0],
								header: {
									token: token
								},
								name: 'file',
								success: uploadFileRes => {
									url = JSON.parse(uploadFileRes.data);
									that.avatar = url.data
									uni.hideLoading();
								}
							});
						}
					});
				}
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
			getUserInfo() {
				let userId = uni.getStorageSync('userId')
				this.$Request.get("/app/user/selectUserById").then(res => {
					if (res.code == 0) {
						this.$queue.setData('avatar', res.data.avatar);
						this.$queue.setData('userId', res.data.userId);
						this.$queue.setData('userName', res.data.userName);
						this.$queue.setData('phone', res.data.phone);
						if (res.data.sex) {
							this.sex = res.data.sex
						} else {
							this.sex = 1
						}
						if (res.data.age) {
							this.age = res.data.age
						} else {
							this.age = ''
						}
						this.phone = res.data.phone;
						this.avatar = res.data.avatar;
						this.userName = res.data.userName;
						if (this.userName == null) {
							this.userName = res.data.nickName;
						} else {
							this.userName = res.data.userName;
						}
					}
					uni.hideLoading();
				});


			},
			// 保存
			messagebtn() {
				if (!this.userName) {
					uni.showToast({
						title: this.$t('user.planame'),
						icon: "none"
					})
				} else if (!this.phone) {
					uni.showToast({
						title: this.$t('user.plaphone'),
						icon: "none"
					})
				} else {
					uni.showModal({
						title: this.$t('index.wxts'),
						content: this.$t('user.saveinfo'),
						confirmText:this.$t('index.sure'),
						cancelText:this.$t('index.qx'),
						success: e => {
							if (e.confirm) {
								this.$Request.postT("/app/user/updateUser", {
									userName: this.userName,
									avatar: this.avatar,
									phone: this.phone,
									sex: this.sex,
									age: this.age
								}).then(res => {
									if (res.code === 0) {
										uni.showToast({
											title: this.$t('user.baocun'), //'保存成功'
											icon: "none"
										})
										setTimeout(function() {
											uni.navigateBack()
										}, 1000)
									} else {
										uni.showToast({
											title: res.msg,
											icon: "none"
										})
									}
								})
							}
						}
					});
				}
			}
		},
	};
</script>

<style>
	page {
		/* background: #1c1b20; */
	}

	button {
		padding: 0 !important;
	}

	button::after {
		border: none;
	}

	.usermain {
		background: #ffffff;
		/* color: #fff; */
	}

	.usermain-item {
		display: flex;
		align-items: center;
		margin: 0 40rpx;
		padding: 10rpx 0;
		justify-content: space-between;
		border-bottom: 1rpx solid #e5e5e5;
		/* border-bottom: 2rpx solid #f2f2f2; */
	}

	.usermain-item.item-padding {
		/* padding: 0; */
	}

	.cu-form-group {
		padding: 0;
		background: #ffffff;
		text-align: right;
	}

	.cu-form-group input {
		background: #ffffff;
		font-size: 28rpx;
		/* color: #fff; */

	}

	.footer-btn {
		margin-top: 150rpx;
	}

	.footer-btn .usermain-btn {
		color: #FFFFFF;
		background: #FF6D9D;
		text-align: center;
		width: 450rpx;
		height: 80rpx;
		font-size: 28rpx;
		line-height: 80rpx;
		margin: 0 auto;
		border-radius: 40rpx;
	}
</style>