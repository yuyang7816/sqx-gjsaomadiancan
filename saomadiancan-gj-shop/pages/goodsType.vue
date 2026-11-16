<template>
	<view>
		<view v-for="(item,index) in list" :key="index" class="flex padding justify-between">
			<view class="flex align-center">
				<view class="text-bold text-xl">{{index+1}}.</view>
				<view class="margin-left-xs">{{item.classifyName}}</view>
			</view>
			<view class="flex align-center">
				<view class="btn margin-right-sm" @click="addtype(2,item)">{{$t('index.updete')}}</view>
				<view class="btn" @click="bindupdata(item)">{{$t('index.dete')}}</view>
			</view>
		</view>

		<empty v-if="list.length == 0"></empty>
		<view class="addguige text-bold" @click="addtype(1)">{{$t('Commodity.addl')}}</view>

		<!-- 添加规格弹框 -->
		<u-popup v-model="show" mode="center" border-radius="14" width="500rpx" height="340rpx">
			<view>
				<view class="padding" style="margin-bottom: 20rpx;">
					<view>{{$t('Commodity.typeTitle')}}</view>
					<view class="flex align-center" style="margin-top: 15rpx;">
						<label style="margin-right: 10rpx;">{{$t('Commodity.typeName')}}</label>
						<u-input v-model="typeName" :placeholder="$t('Commodity.platypeName')" clearable="false" />
					</view>
					<!-- <view class="flex align-center" style="margin-top: 15rpx;">
						<label style="margin-right: 10rpx;">分类排序</label><u-number-box v-model="sort" placeholder="请填写分类排序"></u-number-box>
					</view> -->
				</view>
				<view class="addguiges" @click="bindAdd()">{{$t('index.sure')}}</view>
			</view>
		</u-popup>

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
				show: false,
				typeName: '',
				sort: 0,
				classifyId: '',
				shopId: '',
				shopName: '',
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('Commodity.typeTitle')
			})
			uni.showLoading({
				title: this.$t('loadingText.jzz') + '...', ///加载中
				mask: false
			})
			this.shopId = this.$queue.getData("updataShopId")
			this.shopName = this.$queue.getData("shopUserName")
			this.getlist()
		},
		methods: {
			addtype(index, item) {
				console.log(index, item)
				if (index == 1) {
					this.show = true
					this.typeName = ''
					this.classifyId = ''
					this.sort = 0
				} else if (index == 2) {
					this.show = true
					this.typeName = item.classifyName
					this.classifyId = item.classifyId
					if (item.sort) {
						this.sort = item.sort
					} else {
						this.sort = 0
					}
				}
			},
			//添加商品类型
			bindAdd() {
				if (!this.typeName) {
					uni.showToast({
						title: this.$t('Commodity.platypeName'), //'请填写商品分类',
						icon: 'none',
						duration: 1000
					})
					return
				}

				if (this.classifyId) {
					let data = {
						classifyId: this.classifyId,
						classifyName: this.typeName,
						shopId: this.shopId,
					}
					this.$Request.postJsonA("/admin/goods/updateClassify", data).then(res => {
						if (res.code == 0) {
							this.show = false
							this.typeName = ''
							this.classifyId = ''
							this.sort = 0
							this.getlist()
						}
					})
				} else {
					let data = {
						classifyName: this.typeName,
						shopId: this.shopId,
					}
					this.$Request.postJsonA("/admin/goods/insertClassify", data).then(res => {
						if (res.code == 0) {
							this.show = false
							this.typeName = ''
							this.classifyId = ''
							this.sort = 0
							this.getlist()
						}
					})
				}
			},
			//获取分类列表
			getlist() {
				let data = {
					shopId: this.shopId
				}
				this.$Request.getA("/admin/goods/selectAllClassify", data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						let arr = []
						res.data.map(item => {
							if (item.shopId != 0) {
								arr.push(item)
							}
						})
						this.list = arr
					}
				})
			},
			//删除商品类型
			bindupdata(e) {
				let that = this
				console.log(e)
				uni.showModal({
					showCancel: true, // 是否显示取消按钮，默认为 true
					confirmColor: '#FF6D9D',
					title: that.$t('index.wxts'),
					content: that.$t('Commodity.pladteetype'), //确定要删除当前类型吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
					success: (res) => {
						if (res.confirm) {
							let data = {
								classifyId: e.classifyId
							}
							that.$Request.getA("/admin/goods/deleteClassify", data).then(res => {
								if (res.code == 0) {
									uni.showToast({
										title: that.$t('add.deteok'),  //"删除成功",
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
						}
					}
				})

			}
		}
	}
</script>

<style>
	page {
		background: #FFFFFF;
	}

	.btn {
		border: 2upx solid #999999;
		border-radius: 24px;
		color: #333333;
		padding: 5rpx 30rpx;
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
		color: #FFFFFF;
	}

	.addguiges {
		width: 90%;
		margin: 0 auto;
		background: #FF6D9D;
		box-shadow: 0px 10upx 20upx 0px #FF6D9D;
		border-radius: 16upx;
		text-align: center;
		height: 88upx;
		line-height: 88upx;
		color: #FFFFFF;
	}
</style>