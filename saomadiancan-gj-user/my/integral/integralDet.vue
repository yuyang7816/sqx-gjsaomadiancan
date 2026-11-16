<template>
	<view>
		<view style="text-align: left;padding-bottom: 10rpx;">
			<view v-for="(item, index) in list" :key="index" class="item">
				<view>
					<view style="color: #999999;font-size: 28upx;">
						<view style="margin-bottom: 8upx">{{item.title}}</view>
						<view style="margin-bottom: 8upx">{{item.content}}</view>
						<view style="margin-bottom: 8upx"> {{$t('purse.cjsj')}}：{{item.createTime}}</view>
						<view style="margin-bottom: 8upx;text-align: right;">
							<text v-if="item.type == 1" style="color: #ecd4b4;font-size: 32upx;font-weight: 600"><text
									style="color: #000000;">+</text>{{item.num}}{{$t('jifen.jifen')}}</text>
							<text v-if="item.type == 2" style="color: #ecd4b4;font-size: 32upx;font-weight: 600"><text
									style="color: #000000;">-</text>{{item.num}}{{$t('jifen.jifen')}}</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 加载更多提示 -->
			<empty v-if="list.length == 0"></empty>
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
				list: [],
				page: 1,
				limit: 10,
				tabIndex: 1,
				checkReZhiShu: '否',
				checkReTuanZhang: '否',
				checkReFeiZhiShu: '否',
				scrollTop: false,

				classify: ''
			}
		},
		onLoad(option) {
			this.classify = option.classify

			if (this.classify == 2) {
				uni.setNavigationBarTitle({
					title: this.$t('jifen.dhjl') //"兑换记录"
				});
			} else {
				uni.setNavigationBarTitle({
					title: this.$t('jifen.mingxi')
				})
			}
			this.$queue.showLoading("加载中...");
			this.getList();
		},
		onPageScroll: function(e) {
			this.scrollTop = e.scrollTop > 200;
		},
		methods: {
			getList() {
				let data = {
					page: this.page,
					limit: this.limit,
					classify: this.classify == 2 ? this.classify : ''
				}
				this.$Request.getT('/app/userintegraldetails/selectIntegraldetailsList', data).then(res => {
					if (res.code === 0) {
						if (this.page === 1) {
							this.list = res.data.list;
						} else {
							this.list = [...this.list, ...res.data.list];
						}
					}
					uni.stopPullDownRefresh();
					uni.hideLoading();
				});
			}
		},
		onReachBottom: function() {
			this.page = this.page + 1;
			this.getList();
		},
		onPullDownRefresh: function() {
			this.page = 1;
			this.getList();
		}
	}
</script>

<style lang="less">
	page {
		// background: #1c1b20;
	}

	.tui-tab-item-title {
		// color: #ffffff;
		font-size: 30rpx;
		height: 80rpx;
		line-height: 80rpx;
		flex-wrap: nowrap;
		white-space: nowrap;
	}

	.tui-tab-item-title-active {
		border-bottom: 1px solid #5E81F9;
		color: #5E81F9;
		font-size: 32upx;
		font-weight: bold;
		border-bottom-width: 6upx;
		text-align: center;
	}

	.item {
		background: #FFFFFF;
		padding: 32rpx;
		margin: 32rpx;
		font-size: 28rpx;
		box-shadow: 7px 9px 34px rgba(0, 0, 0, 0.1);
		border-radius: 16upx;
	}
</style>