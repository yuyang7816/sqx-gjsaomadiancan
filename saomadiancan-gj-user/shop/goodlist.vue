<template>
	<view>
		<view class="bg padding-lr padding-tb-sm">
			<u-search @change="search" placeholder="搜索商品标题 " v-model="goodsName" bg-color="#F7F7F7" shape="square"
				:show-action="false"></u-search>
		</view>
		<view class="flex bg">
			<view v-for="(item,index) in list" :key="index" class="flex-sub text-center box"
				:class="status == index?'active':''" @click="bindTab(index)">
				{{item.name}}
			</view>
		</view>
		<scroll-view scroll-x="true" class="flex align-center padding-sm"
			style="height: 100rpx;width: 100%;white-space: nowrap">
			<view v-for="(item,index) in lists" :key="index" class="boxs"
				:class="counts == item.classifyId?'actives':''" @click="bindTabs(item.classifyId)">{{item.classifyName}}
			</view>
		</scroll-view>

		<view class="bg margin-lr margin-bottom radius" v-for="(item,index) in goodsList" :key='index'>
			<view class="flex padding" @click.stop="goDet(item)">
				<view>
					<image :src="item.goodsCover?item.goodsCover:'../static/logo.png'" class="radius"
						style="width:210upx;height:170upx;"></image>
				</view>
				<view class="margin-left flex flex-direction justify-between">
					<view class="text-lg text-bold u-line-1">{{item.goodsName}}</view>
					<!-- <view class="margin-tb-xs u-line-3"style="color:#999999;">
						{{item.goodsLabel}}
					</view> -->
					<view class="text-sm text-gray" v-if="item.inventory">库存：{{item.inventory}}</view>
					<view class="text-sm text-gray" v-if="item.goodsMoney">售价：{{item.goodsMoney}}元</view>
					<view class="text-sm text-gray" v-if="item.packMoney">打包费：{{item.packMoney}}元</view>
				</view>
			</view>
			<view style="width: 686upx;height: 1upx;border: 1upx solid #E6E6E6;margin:0 auto;"></view>
			<view class="flex justify-between padding">
				<view>
					{{item.createTime}}
				</view>


				<view @click.stop="del(item)" class="btn">删除</view>
				<view @click.stop="down(item)" class="btn">{{status==0?'下架':'上架'}}</view>
			</view>
		</view>
		<view class="page-box" v-if="goodsList.length == 0">
			<view class="centre">
				<image src="../static/images/empty.png" mode=""></image>
				<view class="tips">暂无内容</view>
			</view>
		</view>
		<view style="height: 120upx;"></view>
		<view class="addgood" @click="bindAddgood()">添加商品</view>
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
				list: [{
					name: '出售中'
				}, {
					name: '已下架'
				}],
				status: 0,
				lists: [{
					name: '奶茶热饮'
				}, {
					name: '鲜榨果汁'
				}, {
					name: '牛奶系列'
				}, {
					name: '超低折扣'
				}],
				counts: 1,
				goodsList: [],
				page: 1,
				limit: 10,
				shopId: '',
				goodsName: '',
				totalCount: 0
			}
		},
		onLoad(option) {
			this.shopId = option.shopId
			this.getClassifyList()
			this.getGoodsList()
		},
		onShow() {
			let updata = uni.getStorageSync('updata')
			if (updata) {
				this.getGoodsList()
			}
			uni.removeStorageSync('updata')
		},
		methods: {
			search() {
				this.getGoodsList()
			},
			del(e) {
				let that = this
				uni.showModal({
					title: '提示',
					content: '确认删除商品吗',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								shopId: that.shopId,
								goodsId: e.goodsId,
							}
							that.$Request.postA("/admin/goodsShop/deleteGoodsByShopId", data).then(res => {
								if (res.code == 0) {
									that.getGoodsList()
									uni.showToast({
										title: '删除成功',
										icon: 'none'
									})
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			// 下架
			down(e) {
				let that = this
				console.log(that.status)
				let content = that.status == 0 ? '确认下架商品吗?' : '确认上架商品吗?'
				uni.showModal({
					title: '提示',
					content: content,
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								shopId: that.shopId,
								goodsId: e.goodsId,
								status: that.status == 1 ? 0 : 1
							}
							that.$Request.postA("/admin/goodsShop/soldOutOrPutaway", data).then(res => {
								if (res.code == 0) {
									that.getGoodsList()
									uni.showToast({
										title: that.status == 0 ? '下架成功' : '上架成功',
										icon: 'none'
									})
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			// 商品列表
			getGoodsList() {
				let data = {
					page: this.page,
					limit: this.limit,
					status: this.status,
					goodsName: this.goodsName,
					classifyId: this.counts,
					shopId: this.shopId
				}
				this.$Request.getA("/admin/goodsShop/selectGoodsByShopId", data).then(res => {
					if (res.code == 0) {
						if (this.page == 1) {
							this.goodsList = res.data.list
						} else {
							this.goodsList = [...this.goodsList, ...res.data.list]
						}
						this.totalCount = res.data.totalCount
					}
					uni.stopPullDownRefresh();
					uni.hideLoading()
				});
			},
			// 商品类型
			getClassifyList() {
				this.$Request.getA("/admin/goods/selectAllClassify").then(res => {
					if (res.code == 0) {
						this.lists = res.data
						this.counts = this.lists[0].classifyId
					}
				});
			},
			// 切换上下架
			bindTab(index) {
				this.status = index
				this.page = 1
				this.getGoodsList()
			},
			// 切换类型
			bindTabs(index) {
				this.counts = index
				this.page = 1
				this.getGoodsList()
			},
			bindAddgood() {
				uni.navigateTo({
					url: '/shop/addgood?shopId=' + this.shopId
				})
			},
			goDet(e) {
				uni.navigateTo({
					url: '/shop/goodDetail?goodsId=' + e.goodsId + "&shopId=" + this.shopId + '&type=1'
				})
			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getGoodsList();

			if (this.totalCount == this.goodsList.length) {
				uni.showToast({
					title: '已经到底了~',
					icon: 'none'
				})
			}
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getGoodsList();
		},
	}
</script>

<style lang="scss" scoped>
	page {
		background: #EDF1F7;
	}

	.bg {
		background: #FFFFFF;
	}

	.box {
		background: #FFFFFF;
		height: 88upx;
		line-height: 88upx;
	}

	.active {
		background: #FFFBEC;
		height: 88upx;
		line-height: 88upx;
	}

	.boxs {
		width: 150upx;
		height: 55upx;
		border: 2upx solid #CCCCCC;
		border-radius: 28upx;
		text-align: center;
		line-height: 55upx;
		color: #cccccc;
		margin-right: 30rpx;
		display: inline-block;
	}

	.actives {
		width: 150upx;
		height: 55upx;
		border: 2upx solid #FF6D9D;
		border-radius: 28upx;
		text-align: center;
		line-height: 55upx;
		color: #000000;
		margin-right: 30rpx;
	}

	.btn {
		width: 160upx;
		height: 64upx;
		border: 2upx solid #999999;
		border-radius: 8upx;
		text-align: center;
		line-height: 64rpx;
		color: #000000;
	}

	.addgood {
		width: 685upx;
		height: 88upx;
		background: #FF6D9D;
		color: #ffffff;
		border-radius: 8upx;
		text-align: center;
		line-height: 88rpx;
		margin: 0 auto;
		position: fixed;
		bottom: 35rpx;
		left: 0;
		right: 0;
		font-size: 32rpx;
		font-weight: bold;
	}

	.page-box {
		position: relative;
		// left: 0;
		height: 50vh;
		z-index: 0;
		top: 70px;
	}

	.centre {
		position: absolute;
		left: 0;
		top: 0;
		right: 0;
		bottom: 0;
		margin: auto;
		height: 400rpx;
		text-align: center;
		// padding: 200rpx auto;
		font-size: 32rpx;

		image {
			width: 387rpx;
			height: 341rpx;
			// margin-bottom: 20rpx;
			margin: 0 auto 20rpx;
			// border: 1px dotted #000000;
		}

		.tips {
			font-size: 32rpx;
			color: #2F3044;
			margin-top: 20rpx;
			font-weight: 700;
		}

		.btn {
			margin: 80rpx auto;
			width: 600rpx;
			border-radius: 32rpx;
			line-height: 90rpx;
			color: #ffffff;
			font-size: 34rpx;
			background: #5074FF;
		}
	}
</style>
