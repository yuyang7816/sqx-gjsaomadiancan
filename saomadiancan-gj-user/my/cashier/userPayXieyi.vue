<template>
	<view>
		<view class="content" v-html="content">
			
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				content: '',
			};
		},
		onLoad() {
			this.getUserPayXieyi()
		},
		methods:{
			//获取用户支付协议 333
			getUserPayXieyi(){
				this.$Request.getT('/app/common/type/333').then(res => {
					if (res.code == 0) {
						if (res.data && res.data.value) {
							this.content = res.data.value
						}
					}
				});
			}
		},
	}
</script>

<style lang="scss">
	page{
		background-color: #ffffff;
	}
	.content{
		width: 100%;
		padding: 30rpx;
	}
</style>
