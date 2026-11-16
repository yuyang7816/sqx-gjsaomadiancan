<script>
	export default {
		globalData: {
			$t: ''
		},
		onLaunch: function() {
			this.globalData.$t = val => {
				return this.$t(val);
			}

			//小程序是否上线审核
			this.$Request.getT('/app/common/type/261').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.$queue.setData('xcxSelect', res.data.value)
					}
				}
			});
			//门店端小程序appid
			this.$Request.getT('/app/common/type/335').then(res => {
				if (res.code === 0) {
					uni.setStorageSync('SHOPAPPID', res.data.value)
				}
			});
			//门店端小程序密钥
			this.$Request.getT('/app/common/type/336').then(res => {
				if (res.code === 0) {
					uni.setStorageSync('SHOPKEY', res.data.value)
				}
			});
			this.$Request.getT('/app/common/type/239').then(res => {
				if (res.code === 0) {
					uni.setStorageSync('tuiguang', res.data.value)
				}
			});
			this.$Request.getT('/app/common/type/238').then(res => {
				if (res.code === 0) {
					uni.setStorageSync('tuiguangImg', res.data.value)
				}
			});
			this.$Request.getT('/app/common/type/329').then(res => {
				if (res.code === 0) {
					uni.setStorageSync('vipZheKou', res.data.value)
				}
			});
			
			// 是否开启paypal支付 437
			
			this.$Request.getT('/app/common/type/437').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('ttPaySel', res.data.value)
					}
				}
			});
			
			// 是否开启sripe支付 438
			this.$Request.getT('/app/common/type/438').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('ttSripe', res.data.value)
					}
				}
			});
			
			// paypal支付clientId
			this.$Request.getT('/app/common/type/434').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('paypalClientId', res.data.value)
					}
				}
			});
			// stripe支付公钥
			this.$Request.getT('/app/common/type/439').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						uni.setStorageSync('publishKey', res.data.value)
					}
				}
			});
			
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	@import "uview-ui/index.scss";
	@import 'components/colorui/main.css';
	@import 'components/colorui/icon.css';
</style>