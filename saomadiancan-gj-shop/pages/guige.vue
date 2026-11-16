<template>
	<view style="padding-bottom: 70px;">
		<view>
			<view class="">
				<view class="bg btnbox" v-for="(item,index) in list" :key="index" v-if="list.length>0">
					<view class="padding-sm" style="color:#666666;">{{$t('purse.cjsj')}}： {{item.createTime}}</view>
					<view style="width:100%;border-top: 1upx solid #E6E6E6;"></view>
					<view class="padding-sm">
						<view class="text-lg text-bold" style="color:#333333;">{{item.ruleName}}</view>
						<view v-for="(name,index) in item.ruleValue">
							<view class="flex align-center padding-top">
								<view style="color:#999999;">{{name.value}}:</view>
								<view v-for="(ite,index) in name.detail" :key="index">{{ite}}
								</view>
							</view>
						</view>
					</view>
					<view class="flex justify-end padding-tb">
						<view class="btn " @click="bindupdete(item)">{{$t('index.dete')}}</view>
						<view class="btn1" @click="bindeditor(item)">{{$t('index.updete')}}</view>
					</view>
				</view>
			</view>
			<view class="addguige text-bold" @click="goEditor()">{{$t('Commodity.addl')}}</view>
		</view>
		<empty v-if="!list.length" style="z-index:0"></empty>
	</view>
</template>

<script>
	import empty from '@/components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				list: [],
				shopId: '',
				shopName: '',
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('Commodity.attrs')
			})
			uni.showLoading({
				title: this.$t('loadingText.jzz') + '...' //加载中
			})
			this.shopId = this.$queue.getData("updataShopId")
			this.shopName = this.$queue.getData("shopUserName")
			this.getlist()
		},
		onShow() {
			this.getlist()
		},
		methods: {
			goEditor() {
				uni.navigateTo({
					url: '/pages/editor'
				})
			},
			getlist() {
				let data = {
					shopId: this.shopId
				}
				this.$Request.getA("/selfGoodsRule/list", data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.list = res.data
						for (var i = 0; i < this.list.length; i++) {
							for (var a = 0; a < this.list[i].ruleValue.length; a++) {
								this.list[i].ruleValue[a].detail = this.list[i].ruleValue[a].detail.replaceAll(',',
									'/')

							}
						}

					}

				});
			},
			//重新编辑
			bindeditor(e) {
				console.log(e)
				uni.setStorageSync('guige', e)
				uni.navigateTo({
					url: '/pages/editor?id=' + e.id
				})
			},
			//删除
			bindupdete(e) {
				let that = this
				uni.showModal({
					showCancel: true, // 是否显示取消按钮，默认为 true
					confirmColor: '#FF6D9D',
					title: that.$t('index.wxts'),
					content: that.$t('Commodity.pladetesku'), //确定要删除当前规格吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
					success: (res) => {
						if (res.confirm) {
							let data = {
								id: e.id
							}
							that.$Request.getA("/selfGoodsRule/delete", data).then(res => {
								if (res.code == 0) {
									uni.showToast({
										title: that.$t('add.deteok'), //"删除成功",
										icon: 'none'
									})
									that.getlist();
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}

							});
						} else {

						}
					}
				})
			},
		},
		onReachBottom: function() {
			// this.page = this.page + 1;
			// this.getlist();

			// if (this.totalCount == this.getlist.length) {
			// 	uni.showToast({
			// 		title: '已经到底了~',
			// 		icon: 'none'
			// 	})
			// }
		},
		onPullDownRefresh: function() {
			// this.page = 1;
			// this.getlist();
		},
	}
</script>

<style>
	page {
		background: #F2F2F2;
	}

	.bg {
		background: #FFFFFF;
	}

	.btnbox {
		margin: 20rpx 30rpx;
		border-radius: 20rpx;
	}

	.btn {
		border-radius: 25px;
		padding: 6rpx 30rpx;
		border: 1px solid #686868;
		color: #686868;
		margin-right: 30rpx;
	}

	.btn1 {
		border-radius: 25px;
		padding: 6rpx 30rpx;
		background: #FF6D9D;
		/* color: #686868; */
		margin-right: 30rpx;
		color: #FFFFFF;
	}

	.addguige {
		width: 90%;
		margin: 0 auto;
		background: #FF6D9D;
		box-shadow: 0px 10upx 20upx 0px #FF6D9D;
		border-radius: 16upx;
		text-align: center;
		height: 88upx;
		line-height: 88upx;
		position: fixed;
		bottom: 25upx;
		left: 0;
		right: 0;
		z-index: 99;
		color: #FFFFFF
	}
</style>