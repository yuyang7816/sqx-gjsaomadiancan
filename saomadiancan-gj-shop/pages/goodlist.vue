<template>
	<view>
		<view class="bg padding-lr padding-tb-sm">
			<u-search @change="search" :placeholder="$t('Commodity.plasearch') " v-model="goodsName" bg-color="#F7F7F7"
				shape="square" :show-action="false"></u-search>
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
			<view class="padding flex justify-between">
				<view class="">
					{{$t('purse.cjsj')}}：{{item.createTime}}
				</view>
				<u-switch @change="down(item)" v-model="item.select" active-color="#FF6D9D" active-value="0"
					inactive-value="1"></u-switch>
			</view>
			<view style="width: 686upx;height: 1upx;border: 1upx solid #E6E6E6;margin:0 auto;"></view>
			<view class="flex padding" @click.stop="goDet(item)">
				<view>
					<image :src="item.goodsCover?item.goodsCover:'../static/logo.png'" class="radius"
						style="width:210upx;height:170upx;"></image>
				</view>
				<view class="margin-left flex flex-direction justify-between">
					<view class="text-lg text-bold u-line-1">{{item.goodsName}}</view>
					<view class="margin-tb-xs u-line-3" style="color:#999999;">
						{{item.goodsLabel}}
					</view>
					<view class="text-sm text-gray" v-if="item.inventory">{{$t('Commodity.kc')}}：{{item.inventory}}
					</view>
					<view class="text-sm text-gray" v-if="item.goodsMoney">
						{{$t('Commodity.sj')}}：{{item.goodsMoney}}{{$t('index.yuan')}}</view>
					<view class="text-sm text-gray" v-if="item.packMoney">
						{{$t('payorder.dbf')}}：{{item.packMoney}}{{$t('index.yuan')}}</view>
				</view>
			</view>
			<view style="width: 686upx;height: 1upx;border: 1upx solid #E6E6E6;margin:0 auto;"></view>
			<view class="flex justify-end padding">
				<view @click.stop="del(item)" style="order: 2;" class="btn">{{$t('index.dete')}}</view>
				<view @click.stop="updataGoods(item.goodsId)" style="order: 1;" v-if="item.shopId!=0" class="btn">
					{{$t('index.updete')}}
				</view>

			</view>
		</view>
		<view class="page-box" v-if="goodsList.length == 0">
			<view class="centre">
				<image src="../static/images/empty.png" mode=""></image>
				<view class="tips">{{$t('empty.content')}}</view>
			</view>
		</view>
		<view style="height: 140upx;"></view>
		<view class="addgood-box flex justify-center align-center">
			<view class="addgood" @click="bindAddgood()">{{$t('Commodity.add')}}</view>
		</view>

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
					name: '' //出售中
				}, {
					name: '' //已下架
				}],
				status: 0,
				lists: [],
				counts: '',
				goodsList: [],
				page: 1,
				limit: 10,
				shopId: '',
				goodsName: '',
				totalCount: 0
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title: this.$t('Commodity.list')
			})
			this.list[0].name = this.$t('Commodity.csz');
			this.list[1].name = this.$t('Commodity.yxj');

			this.shopId = option.shopId
			this.getClassifyList()

		},
		onShow() {
			if (this.counts) {
				this.getGoodsList()
			}

		},
		methods: {
			//修改商品
			updataGoods(goodsId) {
				uni.navigateTo({
					url: '/pages/setAddGoods?goodsId=' + goodsId
				})
			},
			search() {
				this.getGoodsList()
			},
			del(e) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('Commodity.pladetesp'), //确认删除商品吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
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
										title: that.$t('add.deteok'), //'删除成功'
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
				if (!e.select) {
					e.select = true
				} else {
					e.select = false
				}
				let that = this

				let content = that.status == 0 ? that.$t('Commodity.plaxj') : that.$t('Commodity.plasj')
				uni.showModal({
					title: that.$t('index.wxts'),
					content: content,
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
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
										title: that.status == 0 ? that.$t('Commodity.xjok') :
											that.$t('Commodity.sjok'),
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
						res.data.list.map(item => {
							if (item.gsrStatus == 0) {
								item.select = true
							} else {
								item.select = false
							}
						})
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
				let data = {
					shopId: this.shopId
				}
				this.$Request.getA("/admin/goods/selectAllClassify", data).then(res => {
					if (res.code == 0) {
						this.lists = res.data
						this.counts = this.lists[0].classifyId
						this.getGoodsList()
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
					url: '/pages/addgood?shopId=' + this.shopId
				})
			},
			goDet(e) {
				uni.navigateTo({
					url: '/pages/goodDetail?goodsId=' + e.goodsId + "&shopId=" + this.shopId + '&type=1'
				})
			},
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getGoodsList();

			if (this.totalCount == this.goodsList.length) {
				uni.showToast({
					title: this.$t('index.ddl') + '~', //已经到底了
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
		background: #FF6D9D;
		height: 88upx;
		line-height: 88upx;
		color: #FFFFFF;
	}

	.boxs {
		width: 150upx;
		height: 55upx;
		border-radius: 28upx;
		text-align: center;
		line-height: 55upx;
		color: #333333;
		margin-right: 30rpx;
		display: inline-block;
		background: #FFFFFF;
	}

	.actives {
		width: 150upx;
		height: 55upx;
		background: #FF6D9D;
		border-radius: 28upx;
		text-align: center;
		line-height: 55upx;
		color: #FFFFFF;
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
		margin-left: 10rpx;
	}

	.addgood-box {
		width: 100%;
		height: 140rpx;
		position: fixed;
		bottom: 0rpx;
		left: 0;
		right: 0;
		background-color: #EDF1F7;
		border: none;
		z-index: 999;
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