<template>
	<view class="pages">
		<view class="task_top">
			<!-- 用户信息 -->
			<view class="task_top_infor">
				<view class="task_top_user">
					<view class="task_top_user_le">
						<image :src="avatar?avatar:'../../static/logo.png'" mode=""></image>
					</view>
					<view class="task_top_user_ce">
						{{userName?userName:$t('user.niming')}}
					</view>
				</view>
				<view class="task_top_text">{{$t('jifen.total')}}：{{integral}}</view>
			</view>
			<!-- 周期任务 -->
			<view class="task_top_task">
				<view class="task_top_text2">{{$t('jifen.renwu')}}</view>
				<view class="task_top_task_sty">
					<view class="task_top_task_sty_le">
						<view class="task_top_task_sty_le_to">
							<view class="task_top_task_sty_le_to_le">{{$t('jifen.qd')}}</view>
							<view class="task_top_task_sty_le_to_ri">+{{todayIntegral}}</view>
							<view class="task_top_task_img">
								<image src="../static/task/goldcoins.png" mode=""></image>
							</view>
						</view>
						<view class="task_top_text3">{{$t('jifen.qds')}}{{today.dayNum}}{{$t('jifen.day')}}</view>
					</view>
					<view class="task_top_task_sty_ri" v-if="isSignIn">{{$t('jifen.yqd')}}</view>
					<view class="task_top_task_sty_ri2" v-else @click="check">{{$t('jifen.qiandao')}}</view>
				</view>
			</view>
		</view>

		<view class="bg-white margin-lr margin-top padding-sm radius" style="text-align: left;padding-bottom: 10rpx;"
			v-if="list.length">
			<view v-for="(item, index) in list" :key="index" class="item">
				<view>
					<view style="color: #999999;font-size: 28upx;">
						<view style="margin-bottom: 8upx">{{item.title}}</view>
						<view style="margin-bottom: 8upx">{{item.content}}</view>
						<view style="margin-bottom: 8upx"> {{$t('purse.cjsj')}}：{{item.createTime}}</view>
						<view style="margin-bottom: 8upx;text-align: right;">
							<text v-if="item.type == 1" style="color: #ecd4b4;font-size: 32upx;font-weight: 600"><text
									class="text-olive">+</text>{{item.num}}{{$t('jifen.jifen')}}</text>
							<text v-if="item.type == 2" style="color: #ecd4b4;font-size: 32upx;font-weight: 600"><text
									class="text-red">-</text>{{item.num}}{{$t('jifen.jifen')}}</text>
						</view>
					</view>
				</view>
			</view>

		</view>
		<empty v-if="list.length == 0" :content="$t('empty.zwmx')"></empty>

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
				userName: '',
				avatar: '',
				isSignIn: false,
				integral: 0,
				todayIntegral: 0, //今日签到应得分数
				list: [],
				page: 1,
				limit: 10,
				today: {}
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('jifen.title')
			})
			this.getUserInfo()
			this.getIsSignIn()
			this.getIntegral()
			this.getTodayIntegral()
			this.getToday()
			this.getList()
		},
		methods: {
			getUserInfo() {
				this.$Request.get("/app/user/selectUserMessage").then(res => {
					if (res.code == 0) {
						this.userName = res.data.userName
						this.avatar = res.data.avatar
						this.$queue.setData("member", res.data.member); //1==已开通会员
					}
				});
			},
			// 我的积分
			getIntegral() {
				this.$Request.get("/app/userintegral/findUserMessage").then(res => {
					if (res.code == 0) {
						this.integral = res.data.userIntegral
					}
				});
			},
			// 是否签到
			getIsSignIn() {
				this.$Request.get("/app/userintegral/isSignIn").then(res => {
					if (res.code == 0) {
						this.isSignIn = res.data == this.$t('jifen.wqd') ? false : true
					}
				});
			},
			// 获取今日签到应得分数
			getTodayIntegral() {
				this.$Request.get("/app/userintegral/todayIntegral").then(res => {
					if (res.code == 0) {
						this.todayIntegral = res.data
					}
				});
			},
			// 获取今日签到天数
			getToday() {
				this.$Request.get("/app/userintegral/continuousDay").then(res => {
					if (res.code == 0) {
						this.today = res.data
					}
				});
			},
			// 签到
			check() {
				this.$Request.get("/app/userintegral/signIn").then(res => {
					if (res.code == 0) {
						// this.todayIntegral = res.data
						this.isSignIn = true
						uni.showToast({
							title: this.$t('jifen.qdok'),//'签到成功',
							icon: 'none'
						})
						this.getIntegral()
						this.getList()
						this.getToday()
					}
				});
			},
			getList() {
				let data = {
					page: this.page,
					limit: this.limit,
					classify: 1
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
	}
</script>

<style scoped>
	.task_top {
		width: 100%;
		overflow: hidden;
		/* margin: 3% 0; */
		background-color: #FFFFFF;
	}

	/* 用户信息 */
	.task_top_infor {
		width: 94%;
		overflow: hidden;
		margin: 4% auto 0;
		padding: 2% 3%;
		background: #FF6D9E;
		color: #FFFFFF;
		border-radius: 18rpx;
	}

	.task_top_user {
		display: flex;
	}

	.task_top_user_le image {
		width: 80rpx;
		height: 80rpx;
		border-radius: 50%;
	}

	.task_top_user_ce {
		padding-left: 2%;
		font-size: 38rpx;
		font-weight: 500;
		/* color: #333333; */
		line-height: 2;
	}

	.task_top_user_ri {
		margin: 3% 0 0 2%;
	}

	.task_top_user_ri image {
		width: 118rpx;
		height: 34rpx;
	}

	.task_top_user_ri view {
		position: relative;
		bottom: 43rpx;
		left: 45rpx;
		font-size: 24rpx;
		font-weight: 800;
		color: #D3B17B;
	}

	.task_top_text {
		color: #FFFFFF;
		line-height: 2;
	}

	/* 周期任务 */
	.task_top_task {
		width: 94%;
		overflow: hidden;
		margin: 3% auto 0;
	}

	.task_top_text2 {
		font-size: 34rpx;
		font-weight: bold;
		color: #333333;
		/* margin-bottom: 2%; */
		/* line-height: 32rpx; */
	}

	.task_top_task_sty {
		width: 100%;
		overflow: hidden;
		background: #FCFBF5;
		border-radius: 18rpx;
		display: flex;
		padding: 4% 3%;
		margin: 3% 0;
	}

	.task_top_task_sty_le {
		/* width: 80%; */
		flex: 1;
	}

	.task_top_task_sty_le_to {
		display: flex;
		margin: 2% 0;
	}

	.task_top_task_sty_le_to_le {
		/* width: 150rpx; */
		margin-right: 10rpx;
		font-family: PingFang SC;
		font-weight: bold;
		color: #333333;
	}

	.task_top_task_sty_le_to_ri {
		width: 110rpx;
		height: 40rpx;
		background: rgba(255, 247, 226, 0.5);
		border: 2rpx solid #FFA800;
		/* border-right: 2rpx solid #FFA800; */
		border-radius: 50rpx;
		text-align: right;
		color: #FFA800;
		padding-right: 2%;
	}

	.task_top_task_img image {
		width: 36rpx;
		height: 36rpx;
		position: relative;
		right: 110rpx;
		top: 2rpx;
	}

	.task_top_text3 {
		font-size: 26upx;
		/* font-family: PingFang SC; */
		font-weight: 500;
		color: #999999;
	}

	.task_top_text3 text {
		color: #FF130A;
	}

	.task_top_task_sty_ri {
		padding: 10rpx 20rpx;
		height: 50rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 2rpx solid #999999;
		color: #999999;
		font-size: 28upx;
		border-radius: 18rpx;
	}

	.task_top_task_sty_ri2 {
		padding: 10rpx 20rpx;
		height: 50rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		border: 2rpx solid #FF130A;
		color: #FF130A;
		font-size: 28upx;
		border-radius: 8px;
	}
</style>