<template>
	<view class="">
		<view class="u-skeleton">
			<view class="u-skeleton-fillet" style="width: 100%;height: 490rpx;">
				<image class="" style="width: 100%;height: 100%;" :src="order.homepageImg"></image>
			</view>
			<view class="padding-sm bgs u-skeleton-fillet">
				<view class=" padding flex justify-between bg radius ">
					<!-- <u-avatar :src="order.avatar?order.avatar:'../../../static/logo.png'" mode="square"></u-avatar> -->
					<view class="flex-sub ">
						<view class="text-xxl " style="color: #557EFD;">
							<text class="text-df ">￥</text>{{isVip? order.memberMoney :order.money}}<text
								style="font-size: 28rpx;color: #000000;">元/{{order.unit}}</text>
							<text class=""
								style="color: #999999;font-size: 25rpx;margin-left: 17rpx;">最低{{order.minNum}}{{order.unit}}</text>
						</view>
						<view class="text-lg margin-top-sm margin-bottom-sm u-line-1" style="width: 670rpx;">
							{{order.myLevel}}
						</view>

						<view class="margin-top-xs ">
							<text class="padding-xs margin-right-xs text-sm"
								style="color: #557EFD;background-color: #E1E8FF;" v-for="(item,index) in order.gameName"
								:key="index">{{item}}</text>
							<!-- <text class="padding-xs margin-right-xs text-sm"
							style="color: #557EFD;background-color: #E1E8FF;">做饭家.半天保姆</text>
						<text class="padding-xs margin-right-xs text-sm"
							style="color: #557EFD;background: #E1E8FF;">3到5口之家</text> -->
						</view>
					</view>
				</view>


				<view class="margin-top-sm padding bg radius u-skeleton-fillet">
					<view class="text-lg">用户评论</view>
					<view v-if="commentList.length<=0" class="margin-top-sm"> 暂无评论</view>
					<view class="margin-tb-sm" v-for="(item, index) in commentList" :key='index' v-else>
						<view class="flex justify-between">
							<u-avatar :src="item.avatar" size="48"></u-avatar>
							<view class="flex-sub margin-left-sm" style="line-height: 46upx;">{{item.userName}}</view>
							<view class="flex">
								<u-icon v-for="ite in item.score" :key='ite' color="#2087fe" name="star-fill"></u-icon>
							</view>
						</view>
						<view class="margin-top-sm">{{item.content}}</view>
					</view>
				</view>
				<view class="margin-top-sm bg radius u-skeleton-fillet">
					<view class="text-center padding">服务详情</view>
					<!-- <image src="../../../static/images/detail.png" style="width: 100%;"></image> -->
					<view v-for="(item,index) in order.detailsImg" :key="index">
						<image :src="item" style="width: 100%;"></image>
					</view>
				</view>
			</view>

			
		</view>
		<!-- <u-picker v-model="show" mode="time" :params="params" @confirm="statusChange"></u-picker> -->
		<u-skeleton :loading="loading" :animation="true" bgColor="#FFF"></u-skeleton>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				game: [],
				loading: false, // 是否显示骨架屏组件
				customStyle: {
					width: '400upx',
					color: '#FFFFFF',
					background: "#2087fe",
					border: 0
				},
				id: '',
				orderDet: {},
				page: 1,
				limit: 10,
				order: {},
				commentList: [],
				isFollow: false,
				myId: '',
				isVip: false,
				AUDIO: uni.createInnerAudioContext(),
				isPlay: false,
				show: false,
				params: {
					year: false,
					month: true,
					day: true,
					hour: true,
					minute: true,
					second: true,
					timestamp: true
				},
				startTime: '',
				mobile: '',
				name: '',
				cityaddress: '',
				detailaddress: '',
				latitude: '',
				longitude: '',
				province: '',
				city: '',
				district: '',

			}
		},
		onLoad(option) {
			// this.$queue.showLoading("加载中...");
			this.id = option.id
			this.getDet()
			this.getOrderComment()
			this.myId = uni.getStorageSync('userId')
			this.isVip = uni.getStorageSync('isVIP')
			console.log(this.isVip)

		},
		onShow() {
			
		},
		onReady() {
			this.AUDIO.onEnded(function(res) {
				this.isPlay = false;
			});
		},
		methods: {
			// 地址
			bindAdd() {
				uni.navigateTo({
					url: '../../../my/address/address'
				})
			},
			
			// 选择上门时间
			statusChange(e) {
				console.log('选择时间:' + e.timestamp)
				// var myDate = new Date;
				// var year = myDate.getFullYear(); //获取当前年
				// var month = myDate.getMonth() + 1; //获取当前月
				// var day = myDate.getDate(); //获取当前日
				// var hour = myDate.getHours();//获取当前小时数(0-23)
				// var minute = myDate.getMinutes();//获取当前分钟数(0-59)
				// var s = myDate.getSeconds();//获取当前秒
				// var Time = month + '-' + day + ' ' + hour + ':' + minute
				// var oldTime = (new Date(Time)).getTime() / 1000;

				var time = Date.parse(new Date()).toString(); //获取到毫秒的时间戳，精确到毫秒
				time = time.substr(0, 10); //精确到秒

				console.log('当前时间:' + time)
				if (parseInt(e.timestamp) > parseInt(time)) {

					this.startTime = e.month + '-' + e.day + ' ' + e.hour + ':' + e.minute + ':' + e.second

				} else {
					uni.showToast({
						title: '请选择正确时间',
						icon: 'none'
					});
				}

			},
			// 详情
			getDet() {
				uni.showLoading({
					title:'加载中...',
					icon:'none'
				});
				this.$Request.getJ("/app/orderTaking/queryTakingDetails", {
					id: this.id,
				}).then(res => {
					uni.hideLoading();
					if (res.code == 0) {
						
						this.order = res.data
						this.order.gameName = this.order.gameName.split(",");
						this.order.detailsImg = this.order.detailsImg.split(",");
						this.selectFollow()
						// uni.hideLoading();
					} else {
						// this.loading = false;
						// uni.hideLoading();
					}
					// this.loading = true;
					
				});
				
			},
			// 评论
			getOrderComment() {
				this.$Request.getJ("/app/takingComment/selectOrderTakingComment", {
					id: this.id,
					page: this.page,
					limit: this.limit
				}).then(res => {
					if (res.code == 0) {
						this.commentList = [...this.commentList, ...res.data.list]
					}
				});
			},
			// 是否关注
			selectFollow() {
				this.$Request.getJ("/app/userFollow/selectFollowUser", {
					followUserId: this.order.userId
				}).then(res => {
					if (res.data == true) {
						this.isFollow = true
					} else {
						this.isFollow = false
					}

				});
			},
			playVoice() {
				console.log(this.isPlay)
				this.AUDIO.src = this.order.voiceIntroduce;
				if (this.isPlay) {
					this.AUDIO.stop();
				} else {
					this.AUDIO.play();
				}
				this.isPlay = !this.isPlay;
			},
			goNav() {
				// if (this.startTime == '') {
				// 	uni.showToast({
				// 		icon: 'none',
				// 		title: '请选择上门时间'
				// 	});
				// 	return;
				// }
				uni.navigateTo({
					url: "/pages/index/game/orderDet?id=" + this.id
				})
			},
			goMsg() {
				let data = {
					userId: this.myId,
					focusedUserId: this.order.userId
				}
				this.$Request.postJsonJ('/app/chat/insertChatConversation ', data).then(res => {
					if (res.code == 0) {
						let id = this.order.userId == res.data.userId ? res.data.focusedUserId : this.order.userId
						uni.navigateTo({
							url: '/pages/msg/im?chatConversationId=' + res.data.chatConversationId +
								'&byUserId=' + id
						})
					}
				})
			},
			// 关注
			follow() {
				let that = this
				that.$Request.getJ("/app/userFollow/insert", {
					followUserId: that.order.userId
				}).then(res => {
					uni.showToast({
						title: res.msg,
						icon: 'none'
					})
					setTimeout(function() {
						that.selectFollow()
					}, 500)
				});
			}
		}

	}
</script>

<style>
	page {
		background-color: #FFFFFF;
	}

	.bgs {
		background-color: #F7F7F7;
	}

	.bg {
		background-color: #FFFFFF;
	}

	.line_s {
		display: inline-flex;
		width: 14rpx;
		height: 14rpx;
		background: #1AD566;
		border-radius: 50%;
		margin-right: 10rpx;
	}

	.line_x {
		display: inline-flex;
		width: 14rpx;
		height: 14rpx;
		background: #000000;
		border-radius: 50%;
		margin-right: 10rpx;
	}

	.u-size-default {
		background: #557EFD !important;
	}
</style>
