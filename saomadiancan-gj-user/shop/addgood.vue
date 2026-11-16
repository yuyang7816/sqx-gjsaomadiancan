<template>
	<view>
		<view class="bg padding-lr padding-tb-sm">
			<u-search @change="search" v-model="goodsName" placeholder="搜索商品标题 " bg-color="#F7F7F7" shape="square" :show-action="false"></u-search>

		</view>
		<!-- <scroll-view scroll-x="true" class="flex align-center padding-sm" style="height: 100rpx;width: 100%;white-space: nowrap">
			<view v-for="(item,index) in lists" :key="index" class="boxs"
				:class="counts == item.classifyId?'actives':''" @click="bindTabs(item.classifyId)">{{item.classifyName}}
			</view>
		</scroll-view> -->

		<view class="bg margin-lr margin-top radius" v-for="(item,index) in goodsList" :key='index' >
			<view class="flex padding" @click="goDet(item)" >
				<view>
					<image :src="item.goodsCover?item.goodsCover:'../static/logo.png'" class="radius" style="width:210upx;height:170upx;"></image>
				</view>
				<view class="margin-left flex flex-direction justify-between">
					<view class="text-lg text-bold">{{item.goodsName}}</view>
					<view class="margin-tb-xs u-line-2"
						style="color:#999999;">
						{{item.goodsSynopsis}}
					</view>
					<view class="text-lg"><text class="text-sm">￥</text>{{item.goodsMoney}}</view>
				</view>
			</view>
			<view style="width: 686upx;height: 1upx;border: 1upx solid #E6E6E6;margin:0 auto;"></view>
			<view class="flex justify-between padding">
				<view></view>
				<view @click="add(item)" class="btn">添加商品</view>
			</view>
		</view>
		
		<view class="page-box" v-if="goodsList.length == 0" >
			<view class="centre">
				<image src="../static/images/empty.png" mode=""></image>
				<view class="tips">暂无内容</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

				lists: [{
					name: '奶茶热饮'
				}, {
					name: '鲜榨果汁'
				}, {
					name: '牛奶系列'
				}, {
					name: '超低折扣'
				}],
				counts: 0,
				
				page: 1,
				limit: 10,
				shopId: '',
				goodsName: '',
				goodsList: [],
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
			if(updata) {
				this.getGoodsList()
			}
		},
		methods: {
			search() {
				this.getGoodsList()
			},
			getGoodsList() {
				let data = {
					page: this.page,
					limit: this.limit,
					goodsName: this.goodsName,
					shopId: this.shopId
				}
				this.$Request.getA("/admin/goodsShop/selectGoodsByNotShopId", data).then(res => {
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
			add(item) {
				let that = this
				let data = [{
					goodsId: item.goodsId,
					shopId: this.shopId
				}]
				this.$Request.postJsonA("/admin/goodsShop/addGoodsByShopId",data).then(res => {
					if (res.code == 0) {
						// this.page = 1
						// this.getGoodsList()
						uni.showToast({
							title: '添加成功',
							icon: 'none'
						})
						uni.setStorageSync('updata', true)
						setTimeout(function() {
							that.getGoodsList()
						},1000)
					}
				});
			},
			goDet(e) {
				uni.navigateTo({
					url: '/shop/goodDetail?goodsId='+e.goodsId+"&shopId="+this.shopId+'&type=2'
				})
			},
			bindDetail() {
				uni.navigateTo({
					url: './goodDetail'
				})
			},
			bindTabs(index) {
				this.counts = index
				this.getGoodsList()
			},
			bindAddgood() {
				uni.navigateTo({
					url: './addgood'
				})
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getGoodsList();
			if(this.totalCount == this.goodsList.length) {
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

	.boxs {
		width: 150upx;
		height: 55upx;
		border: 2upx solid #CCCCCC;
		border-radius: 28upx; 
		text-align: center;
		line-height: 55upx;
		color: #999999;
		margin-right: 30rpx;
		display: inline-block;
	}

	.actives {
		width: 150upx;
		height: 55upx;
		border: 2upx solid #FFCC00;
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

	.box {
		width: 131upx;
		height: 45rpx;
		background: #FFEAF0;
		border-radius: 4upx;
		text-align: center;
		line-height: 45rpx;
		font-size: 24upx;
		font-weight: 500;
		color: #FE215B;
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
