<template>
	<view>

	</view>
</template>

<script>
	export default {
		data() {
			return {
				province: '',
				city: '',
				county: '',
				cityaddress: '',
				detailaddress: '',
				latitude: '',
				longitude: '',
				userId: ''
			}
		},
		onLoad(option) {
			console.log('---------')

			if (option.userId) {
				console.log(option.userId)
				this.userId = option.userId
			}
			if (option.uuid) {
				this.uuid = option.uuid
			}
			if (option.type) {
				this.type = option.type
			}
			// this.getAdd()
			let that = this
			uni.chooseLocation({
				success: function(res) {
					console.log(res, '5555555555555' + this.userId)
					console.log('位置名称：' + res.name);
					console.log('详细地址：' + res.address);
					console.log('纬度：' + res.latitude);
					console.log('经度：' + res.longitude);
					that.lat = res.latitude
					that.lng = res.longitude
					that.address = res.address
					that.addressDetail = res.name
					that.shengcheng()
				},
				fail: function(res) {
					console.log('地图加载失败')
					let data = {
						content: '',
						userId: that.userId,
						uuid: that.uuid,
						type: 2
					}
					that.$Request.postJson('/app/message/saveMessageUser', data).then(red => {
						if (red.code == 0) {
							console.log('地址保存失败')
							uni.hideLoading()
							that.$queue.showToast(that.$t('user.baocun') + "!");

						} else {
							uni.hideLoading()
							that.$queue.showToast(red.msg);
						}
					});
				}
			});
		},
		onShow() {

		},
		methods: {
			shengcheng() {
				let add = {
					address: this.address,
					addressDetail: this.addressDetail,
					lat: this.lat,
					lng: this.lng,
					userId: this.userId,
					isDefault: '0',
				}
				let data = {
					content: add,
					userId: this.userId,
					uuid: this.uuid,
					type: this.type
				}
				this.$Request.postJson('/app/message/saveMessageUser', data).then(red => {
					if (red.code == 0) {
						uni.hideLoading()
						this.$queue.showToast(this.$t('user.baocun') + "!");

					} else {
						uni.hideLoading()
						this.$queue.showToast(red.msg);
					}
				});

			},
		}
	}
</script>

<style lang="less">

</style>