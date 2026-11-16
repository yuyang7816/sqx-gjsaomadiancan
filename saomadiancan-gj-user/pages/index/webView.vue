<template>
	<web-view :src="url"></web-view>
</template>

<script>
	export default {
		data() {
			return {
				url: null, //要打开的外部链接
				oldUrl: '',
				userId: '',
				strlist: '',
				markUuid: ''
			}
		},
		onBackPress(e) { //响应返回事件
			if (e.from === 'navigateBack') { //这个地方必须要有的，否则会死循环
				return false;
			}
			console.log('点击返回....');
			let pages = getCurrentPages()
			let page = pages[pages.length - 1];
			let currentPages = page.$getAppWebview()
			// currentPages.close()
			// uni.navigateBack({
			// 	delta: 1
			// })
			return true;
		},
		onLoad: function(option) {
			
			uni.setNavigationBarTitle({
				title:''
			})
			this.userId = uni.getStorageSync('userId')
			this.oldUrl = option.url

			this.getUrl()



		},
		methods: {
			randomString(length) {
				let chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
				let randomChars = '';
				for (let i = 0; i < length; i++) {
					randomChars += chars.charAt(Math.floor(Math.random() * chars.length));
				}
				return randomChars;
			},
			getUrl() {
				this.markUuid = this.randomString(32); //获取标识
				this.strlist = setInterval(() => {
					this.biaoshi()
				}, 1000)
				console.log(this.markUuid, '8585855555555555')
				this.url = this.oldUrl + '?userId=' + this.userId + '&uuid=' + this.markUuid + '&type=1'



			},
			biaoshi() {
				let data = {
					uuid: this.markUuid
				}
				this.$Request.getT('/app/message/selectMessageUser', data).then(res => {
					if (res.code === 0) {
						if (res.data) {
							// console.log(res.data, '标识返回', res.data.type)
							uni.showLoading({
								title:this.$t('loadingText.jzz')
							})
							if (res.data.type == 1) {
								clearInterval(this.strlist)
								// this.saveAdd(res.data)
								uni.$emit('Appaddress',res.data)
								uni.navigateBack({
									delta: 1
								})
							}else{
								clearInterval(this.strlist)
								uni.hideLoading()
								uni.navigateBack({
									delta: 1
								})
							}

						}
					}
				});

			},
			saveAdd(item) {
				let add = JSON.parse(item.content)
				console.log(item.userId, '======', add.address)
				// return
				let data = {
					userName: '',
					userPhone: '',
					address: add.address,
					lat: add.lat,
					lng: add.lng,
					province: add.province,
					city: add.city,
					county: add.county,
					userId: item.userId,
					addressId: '',
					isDefault: '0',
					addressId: ''
				}
				// this.$Request.postT('/app/address/saveAddress', data).then(res => {
					this.$Request.postT('/app/address/saveAddress', data).then(res => {
					if (res.code == 0) {
						console.log('地址保存返回')
						uni.hideLoading()
						this.$queue.showToast(this.$t('user.baocun') + "!");
						uni.navigateBack({
							delta: 1
						})
					} else {
						uni.hideLoading()
						this.$queue.showToast(res.msg);
						uni.navigateBack({
							delta: 1
						})
					}
				});
			}
		}
	}
</script>