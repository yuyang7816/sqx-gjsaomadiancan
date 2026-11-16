<template>
	<view class="">
		<form>
			<view class="cu-form-group">
				<view class="title">{{$t('add.username')}}</view>
				<input :placeholder="$t('add.plausername')" name="input" v-model="form.userName"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">{{$t('add.phone')}}</view>
				<input :placeholder="$t('add.plaphone')" name="input" v-model="form.userPhone"></input>
			</view>
			<view class="cu-form-group" @click="pickerShow">
				<view class="title">{{$t('add.dq')}}</view>
				<input :placeholder="$t('add.pladq')" name="input" disabled v-model="form.address"></input>
				<text class='cuIcon-locationfill text-orange'></text>
			</view>
			<view class="cu-form-group">
				<view class="title">{{$t('add.xxdz')}}</view>
				<input :placeholder="$t('add.plaxxdz')" name="input" v-model="form.addressDetail"></input>
			</view>
			<view class="cu-form-group">
				<view class="title">{{$t('add.setmr')}}</view>
				<!-- <switch @change="SwitchA" :class="form.addressDefault?'checked':''"
					:checked="form.addressDefault?true:false"></switch> -->

				<u-switch v-model="form.addressDefault" active-color="#FF6D9D"></u-switch>
			</view>
		</form>

		<view class="btn" v-if="!id">
			<view class="address_push" @click="submit">{{$t('user.save')}}</view>
		</view>
		<view class="btn" v-if="id">
			<view class="address_push" @click="updata">{{$t('add.updetetit')}}</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					addressId: '',
					userName: '',
					userPhone: '',
					address: '',
					addressDetail: '',
					addressDefault: 0, //默认地址 0不默认 1默认
					lng: '',
					lat: ''
				},
				// switchA: false,
				region: '',
				// pickerShow: false,
				id: '',
				latitude: '',
				longitude: ''
			}
		},
		onLoad(option) {
			this.id = option.id

			if (option.id) {
				this.getAddressDet(option.id)
				uni.setNavigationBarTitle({
					title: this.$t('add.updetetit') //"修改地址"
				})
			} else {
				uni.setNavigationBarTitle({
					title: this.$t('add.addtit')
				})
			}
		},
		onShow() {
			let that = this
			uni.$once('Appaddress', function(data) {
				console.log('监听到事件来自 Appaddress ，携带参数 msg 为：' + data);
				let add = JSON.parse(data.content)
				that.form.lng = add.lng
				that.form.lat = add.lat
				that.form.address = add.address
				that.form.addressDetail = add.addressDetail
			})
		},
		methods: {
			pickerShow() {
				let that = this
				// #ifdef APP
				uni.navigateTo({
					url: '/pages/index/webView?url=http://192.168.0.126:8080/my/address/addy'
				});
				// #endif

				// #ifdef H5
				uni.chooseLocation({
					success: function(res) {
						console.log(res)
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						let latitude = res.latitude; //纬度
						let longitude = res.longitude; //经度
						that.form.lng = longitude
						that.form.lat = latitude
						that.form.address = res.address
						that.form.addressDetail = res.name
					}
				});
				// #endif

			},
			SwitchA(e) {
				console.log(e.detail.value)
				this.form.addressDefault = e.detail.value
			},
			// 选择地区回调
			regionConfirm(e) {
				console.log(e)
				this.form.province = e.province.label
				this.form.city = e.city.label
				this.form.area = e.area.label
				this.region = e.province.label + '-' + e.city.label + '-' + e.area.label;
				console.log(this.region)
			},
			// 提交
			submit() {
				if (!this.form.userName) {
					uni.showToast({
						title: this.$t('add.plausername'), //'请输入姓名',
						icon: 'none'
					})
					return
				}
				if (!this.form.userPhone) {
					uni.showToast({
						title: this.$t('add.plaphone'), //'请输入联系电话',
						icon: 'none'
					})
					return
				}
				if (!this.form.address) {
					uni.showToast({
						title: this.$t('add.pladq'), //'请选择所在地区',
						icon: 'none'
					})
					return
				}
				if (!this.form.addressDetail) {
					uni.showToast({
						title: this.$t('add.plaxxdz'), //'请输入详细地址',
						icon: 'none'
					})
					return
				}
				this.form.addressDefault = this.form.addressDefault ? 1 : 0
				this.$Request.postJson("/app/address/insertAddress", this.form).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('user.baocun'), // '保存成功',
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1000)
					}
				});
			},
			// 修改
			updata() {
				if (!this.form.userName) {
					uni.showToast({
						title: this.$t('add.plausername'), //'请输入姓名',
						icon: 'none'
					})
					return
				}
				if (!this.form.userPhone) {
					uni.showToast({
						title: this.$t('add.plaphone'), //'请输入联系电话',
						icon: 'none'
					})
					return
				}
				if (!this.form.address) {
					uni.showToast({
						title: this.$t('add.pladq'), //'请选择所在地区',
						icon: 'none'
					})
					return
				}
				if (!this.form.addressDetail) {
					uni.showToast({
						title: this.$t('add.plaxxdz'), //'请输入详细地址',
						icon: 'none'
					})
					return
				}

				let data = {
					addressId: this.form.addressId,
					userName: this.form.userName,
					userPhone: this.form.userPhone,
					address: this.form.address,
					addressDetail: this.form.addressDetail,
					addressDefault: this.form.addressDefault ? 1 : 0,
					lng: this.form.lng,
					lat: this.form.lat,
				}
				this.$Request.postJson("/app/address/updateAddress", data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('add.updeteok'), //'修改成功',
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack()
						}, 1500)
					}
				});
			},
			// 根据id获取地址详情
			getAddressDet(e) {
				this.$Request.get("/app/address/selectAddressById?addressId=" + e).then(res => {
					if (res.code == 0) {
						this.form.addressId = res.data.addressId
						this.form.userName = res.data.userName
						this.form.userPhone = res.data.userPhone
						this.form.address = res.data.address
						this.form.addressDetail = res.data.addressDetail
						this.form.lng = res.data.lng
						this.form.lat = res.data.lat
						this.form.addressDefault = res.data.addressDefault
					}
				});
			}

		}

	}
</script>

<style>
	page {
		background-color: #fff !important;
	}

	/* 添加收货地址 */
	.btn {
		/* position: fixed;
		bottom: 0rpx; */
		width: 100%;
		height: 100rpx;
		line-height: 100rpx;
		background-color: white;
		margin-top: 30rpx;
	}

	.address_push {
		width: 90%;
		height: 80rpx;
		margin: 0 auto;
		background: #FF6D9D;
		border-radius: 20rpx;
		color: white;
		text-align: center;
		line-height: 80rpx;
		font-size: 35rpx;
	}
</style>