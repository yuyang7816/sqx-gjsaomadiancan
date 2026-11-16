<template>
	<view class="content">
		<view v-for="(item, index) in list" :key="index" class="item"  >
			<view class="flex justify-between"
				style="font-size: 30upx;width: 100%;overflow: hidden;text-overflow: ellipsis;white-space:nowrap">
				<view class="text-bold" >{{ item.title }}</view>
			</view>
			<view class="flex justify-between">
				<view style="color: #999999;font-size: 28upx;margin-top: 10upx;">{{ item.content }}</view>
				
				<view style="margin-top: 10upx;color: #999999;font-size: 28upx;text-align: right;">{{ item.createAt }}</view>
			</view>
		</view>
		<empty v-if="list.length === 0" show="false"></empty>
	</view>
</template>

<script>
	import empty from '@/components/empty';

	export default {
		components: {
			empty
		},
		data() {
			return {
				tabFromIndex: 5,
				tabCurrentIndex: 0,
				fromInfo: 5,
				list: [],
				page: 1,
				limit: 20,
				scrollTop: false,
				tabList: [{
						state: 5,
						text: '',  //用户消息
						totalElements: 0
					},
					{
						state: 4,
						text: '', //订单消息
						totalElements: 0
					}
				]
			};
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.loadData();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.loadData();
		},
		onLoad(options) {
			uni.setNavigationBarTitle({
				title:this.$t('msg.title')
			})
			this.tabList[0].text=this.$t('msg.yhxx')
			this.tabList[1].text=this.$t('msg.orderxx')
			// this.$queue.showLoading("加载中...")
			uni.showLoading({
				title:this.$t('loadingText.jzz')
			})
			this.loadData();
		},
		methods: {
			
			//顶部渠道点击
			tabClicks(index) {
				this.list = [];
				this.page = 1;
				this.tabFromIndex = index;
				// this.$queue.showLoading("加载中...")
				uni.showLoading({
					title:this.$t('loadingText.jzz')
				})
				this.loadData();
			},
			//获取消息列表
			loadData() {
				let that = this;
				let number = 10;
				let token = this.$queue.getData('token');
				if (token) {
					let data = {
						page: this.page,
						limit: this.limit,
					}
					this.$Request.getT('/app/message/selectMessageByUserId', data).then(res => {
						if (res.code === 0) {
							if(this.page == 1) {
								this.list = res.data.list
							} else {
								res.data.list.forEach(d => {
									this.list.push(d);
								});
							}
						}
						uni.hideLoading();
						uni.stopPullDownRefresh();
					});
				}
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.loadData();
		},
	};
</script>

<style lang="scss">
	page,
	page {
		// background: #111224;
	}

	.content {
		// background: #111224;
		height: 100%;
	}

	.navbar {
		display: flex;
		height: 40px;
		padding: 0 5px;
		// background: #1E1F31;
		box-shadow: 0 1px 5px rgba(0, 0, 0, 0.06);
		position: relative;
		z-index: 10;

		.nav-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			height: 100%;
			font-size: 15px;
			// color: #FFFFFF;
			position: relative;

			&.current {
				color: #5E81F9;

				&:after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 44px;
					height: 0;
					border-bottom: 2px solid #5E81F9;
				}
			}
		}
	}

	.item {
		// background: #1E1F31;
		padding: 16rpx 25rpx;
		margin: 16rpx;
		font-size: 28rpx;
		box-shadow: 7rpx 9rpx 34rpx rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
		background: #fff;
	}


</style>
