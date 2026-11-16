<template>
	<view style="padding-bottom: 130rpx;">
		<view class="content flex justify-center">
			<view class="content-box">
				<view class="content-box-item" v-for="(item,index) in list" :key="index" @click="gotoInfo(item.seatId)">
					<view class="content-box-item-header flex justify-between align-center">
						<view v-if="item.status==1" class="flex align-center" style="width: 140rpx;">
							<view
								style="width: 20rpx;height: 20rpx;border-radius: 50%;background-color: red;margin-right: 20rpx;">
							</view>
							{{$t('meals.syz')}}
						</view>
						<view v-else class="flex align-center" style="width: 140rpx;">
							<view
								style="width: 20rpx;height: 20rpx;border-radius: 50%;background-color: green;margin-right: 20rpx;">
							</view>
							{{$t('meals.kxz')}}
						</view>
					</view>
					<view class="line"></view>
					<view class="content-box-item-name" style="font-size: 32rpx;">
						{{item.seatName}}
					</view>
					<view class="line"></view>
					<view class="content-box-item-name flex justify-between align-center"
						style="padding-bottom: 20rpx;">
						<text style="color: #cccccc;">
							{{item.createTime}}
						</text>
						<view class="content-box-item-nameb flex align-center">
							<view class="content-box-item-namebs" @click.stop="corOrder(item.seatId)">
								{{$t('meal.jiezhang')}}
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0" />
		<view class="submit flex justify-center align-center">
			<view class="submit-box flex justify-center align-center" @click="openDesk()">
				{{$t('meal.kz')}}
			</view>
		</view>
		<!-- 开桌弹窗 -->
		<u-popup v-model="openShow" mode="bottom" border-radius="24" :closeable="true" :mask-close-able="false">
			<view class="zh-title">{{$t('meal.xzcw')}}</view>
			<view class="zh-content flex justify-center">
				<view class="zh-content-box">
					<scroll-view scroll-y="true" style="width: 100%;height: 100%;">
						<view class="flex flex-wrap">
							<view class="zh-content-box-item flex justify-center align-center" @click="current = index"
								:class="index==current?'zhactive':''" v-for="(item,index) in zhuohao" :key="index">
								{{item.seatName}}
							</view>
						</view>

					</scroll-view>
				</view>
			</view>
			<view class="zh-btn flex justify-center">
				<view class="zh-btn-box flex justify-center">
					<view class="zh-btn-box-b flex justify-center align-center bgsubc" @click="openShow = false">
						{{$t('index.qx')}}
					</view>
					<view class="zh-btn-box-b flex justify-center align-center bgsubq" @click="openDisk()">
						{{$t('index.sure')}}
					</view>
				</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import empty from '../../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				current: 0,
				zhuohao: [],
				openShow: false,
				list: [],
				shopId: '',
				seatId: '',
			};
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title:this.$t('shop.ycgl')
			})
			this.shopId = this.$queue.getData("updataShopId")
			this.getDataList()
		},
		onShow() {
			this.getDataList()
		},
		methods: {
			corOrder(seatId){
				let data = {
					shopId: this.shopId,
					seatId: seatId
				}
				let that = this
				that.$Request.getA('/admin/order/getSeatOrder', data).then(res => {
					if (res.code == 0) {
						let orderId = res.data?res.data.orderId:''
						uni.showModal({
							title: that.$t('index.wxts'),
							content: that.$t('meal.plawcdd'),
							confirmText:that.$t('index.sure'),
							cancelText:that.$t('index.qx'),
							complete(ret){
								if(ret.confirm){
									if(orderId){ //有订单则完成订单关闭桌位
										that.$Request.postA('/admin/order/adminCompleteOrder',{
											orderId:orderId
										}).then(rest=>{
											if(rest.code==0){
												uni.showToast({
													title:that.$t('order.ywc')//'已完成'
												})
												that.getDataList()
											}else{
												uni.showToast({
													title:rest.msg,
													icon:'none'
												})
											}
										})
									}else{
										that.$Request.postA('/admin/shopSeat/insertSeat', {
											seatId: seatId,
											status: 2,
										}).then(ress => {
											if (ress.code == 0) {
												uni.showToast({
													title:that.$t('order.ywc')//'已完成'
												})
												that.getDataList()
											} else {
												uni.showToast({
													title: ress.msg,
													icon: 'none'
												})
											}
										})
									}
									
								}
							}
						})
						// 
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			// 用餐详情
			gotoInfo(seatId){
				uni.navigateTo({
					url:'/pages/meals/mealInfo?seatId='+seatId
				})
			},
			//开桌
			openDisk() {
				//修改桌位状态为正在使用
				let data = {
					seatId: this.zhuohao[this.current].seatId,
					status: 1,
				}
				this.$Request.postA('/admin/shopSeat/insertSeat', data).then(res => {
					if (res.code == 0) {
						// this.getDataList()
						uni.showToast({
							title: this.$t('meal.ykz')//'已开桌'
						})
						this.openShow = false
						uni.navigateTo({
							url:'/pages/meals/mealInfo?seatId='+data.seatId
						})
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//获取桌位列表冰打开选择弹窗
			openDesk() {
				let data = {
					isEnable: 1, //桌位是否启用：1、启用中 0、不启用
					status: 2, //桌位状态：1、正在使用中，2、空闲中
					shopId:this.shopId
				}
				//拿出空闲中并且在启用中的桌位列表
				this.$Request.getA('/admin/shopSeat/getShopSeatList', data).then(res => {
					if (res.code == 0) {
						if (res.data.records.length > 0) {
							this.zhuohao = res.data.records
							this.openShow = true
						} else {
							this.openShow = false
							uni.showToast({
								title: this.$t('meal.zwkxz'),//'暂无空闲桌位'
								icon: 'none'
							})
						}
					} else {
						this.openShow = false
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//获取桌位列表
			getDataList() {
				let data = {
					shopId: this.shopId,
					status: 1
				}
				this.$Request.getA("/admin/shopSeat/getShopSeatList", data).then(res => {
					if (res.code == 0) {
						res.data.records.map(item => {
							if (item.isEnable == 1) {
								item.checked = true
							} else {
								item.checked = false
							}
						})
						this.list = res.data.records
						let arr = []
						this.list.map((item, index) => {
							let obj = {
								value: index,
								label: index
							}
							arr.push(obj)
						})
						this.sortlist = arr
					}
				});
			}
		},
	}
</script>

<style lang="scss">
	page {
		background: #EDF1F7;
	}

	.zh-title {
		margin-top: 30rpx;
		font-weight: bold;
		text-align: center;
	}

	.zh-content {
		width: 100%;
		height: 400rpx;
		margin: 20rpx 0;

		.zh-content-box {
			width: 686rpx;
			height: 100%;

			// background-color: red;
			.zh-content-box-item {
				margin-right: 20rpx;
				padding: 10rpx 20rpx;
				margin-bottom: 20rpx;
				// width: 150rpx;
				// height: 100rpx;
				border: 1rpx solid #FF6D9D;
				color: #FF6D9D;
				border-radius: 16rpx;
			}
		}
	}

	.zh-btn {
		width: 100%;
		height: 70rpx;
		margin-bottom: 40rpx;

		.zh-btn-box {
			width: 686rpx;
			height: 100%;

			.zh-btn-box-b {
				width: 200rpx;
				height: 100%;
				border-radius: 16rpx;
			}
		}
	}

	.zhactive {
		border-color: #FF6D9D !important;
		color: #FFFFFF !important;
		background-color: #FF6D9D;
	}

	.bgsubc {
		border: 1rpx solid #FF6D9D;
		color: #FF6D9D;
	}

	.bgsubq {
		background-color: #FF6D9D;
		color: #ffffff;
		margin-left: 40rpx;
	}

	.line {
		width: 646rpx;
		border: 1rpx solid #f2f2f2;
		margin: 20rpx auto;
		// padding: 0 20rpx;
	}

	.content {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.content-box {
			width: 686rpx;
			height: 100%;

			.content-box-item {
				width: 100%;
				border-radius: 16rpx;
				background-color: #ffffff;
				margin-bottom: 20rpx;
			}

			.content-box-item-header {
				width: 100%;
				padding: 20rpx 20rpx 0 20rpx;
			}

			.content-box-item-name {
				padding: 0 20rpx;
			}

			.content-box-item-namebs {
				padding: 10rpx 20rpx;
				border: 1rpx solid #000;
				border-radius: 8rpx;
				margin-left: 20rpx;
			}
		}
	}

	.submit {
		width: 100%;
		height: 130rpx;
		position: fixed;
		bottom: 0;
		z-index: 998;
		background: #EDF1F7;

		.submit-box {
			width: 686rpx;
			height: 80rpx;
			background-color: #FF6D9D;
			color: #ffffff;
			border-radius: 8rpx;
		}
	}
</style>
