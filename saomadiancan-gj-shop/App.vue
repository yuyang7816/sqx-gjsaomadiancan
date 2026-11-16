<script>
	export default {
		globalData: {
			$t: ''
		},
		onLaunch: function() {
			this.globalData.$t = val => {
				return this.$t(val);
			}
			//小程序是否上线审核334
			this.$Request.getT('/app/common/type/334').then(res => {
				if (res.code == 0) {
					if (res.data && res.data.value) {
						this.$queue.setData('xcxSelect',res.data.value)
					}
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
