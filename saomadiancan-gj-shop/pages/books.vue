<template>
	<view style="padding-bottom: 130rpx;">
		<view class="content flex justify-center">
			<view class="content-box">
				<view class="content-box-item" v-for="(item,index) in list" :key="index">
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
						<u-switch @change="changeStatus(item.seatId,item.isEnable)" v-model="item.checked"
							active-color="#FF6D9D" inactive-color="#eee"></u-switch>
					</view>
					<view class="line"></view>
					<view class="content-box-item-name" style="font-size: 32rpx;">
						{{item.seatName}}
					</view>
					<view class="line"></view>
					<view class="flex padding-left-sm " style="color: #cccccc;">{{item.createTime}}</view>
					<view class="content-box-item-nameb flex align-center justify-end padding-tb-sm padding-right-sm">
						<view class="content-box-item-namebs" @click="geterweima(item.seatId)">
							{{$t('Table.zm')}}
						</view>
						<view class="content-box-item-namebs" @click="gotoAdd(item.seatId)">
							{{$t('index.updete')}}
						</view>
						<view class="content-box-item-namebs" @click="deleteZw(item.seatId,item.status)">
							{{$t('index.dete')}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0" />
		<view class="submit flex justify-center align-center">
			<view class="submit-box flex justify-center align-center" @click="gotoAdd()">
				{{$t('Commodity.addl')}}
			</view>
		</view>
		<!-- 排序弹窗 -->
		<u-select v-model="show" :list="sortlist" @confirm="confirm" :cancelText="$t('index.qx')"
			:confirmText="$t('index.sure')"></u-select>
		<!-- 桌码弹窗 -->
		<u-popup v-model="shower" mode="center" width="80%" border-radius="14">
			<view class="" style="width: 100%;margin-top: 30rpx;text-align: center;">
				{{$t('Table.zm')}}
			</view>
			<view @click="dowloudzhuoma(erweima)" style="width: 100%;margin-top: 20rpx;"
				class="flex justify-center align-center">
				<image style="width: 300rpx;height: 300rpx;" :src="erweima" mode=""></image>
			</view>
			<view class="" style="width: 100%;margin-top: 20rpx;text-align: center;margin-bottom: 30rpx;">
				{{$t('Table.savezm')}}
			</view>
		</u-popup>
	</view>
</template>

<script>
	import empty from '../components/empty.vue'
	export default {
		components: {
			empty
		},
		data() {
			return {
				sortlist: [],
				show: false,
				list: [],
				shopId: '',
				seatId: '',
				shower: false,
				erweima: '', //二维码
			};
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('Table.title')
			})
			this.shopId = this.$queue.getData("updataShopId")
			this.getDataList()
		},
		onShow() {
			this.getDataList()
		},
		methods: {
			//长按下载桌码
			dowloudzhuoma(urlbase64) {
				this.shower = false
				uni.previewImage({
					urls: [urlbase64]
				})

			},
			//查看桌码
			geterweima(seatId) {
				this.$Request.getA('/invite/shopSeatQr', {
					seatId: seatId
				}).then(res => {
					if (res) {
						this.erweima = res
						this.shower = true
					} else {
						uni.showToast({
							title: this.$t('Table.plazmerr'), //'生成桌码失败，请重试!',
							icon: 'none'
						})
					}
				})
			},
			//排序
			confirm(e) {
				let data = {
					seatId: this.seatId,
					sort: e[0].value
				}
				this.$Request.postA('/admin/shopSeat/insertSeat', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('add.updeteok'), //'修改成功'
						})
						this.seatId = ''
						this.getDataList()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'noen'
						})
					}
				})
				console.log(e)
			},
			//打开排序选择
			upSort(seatId, sort) {
				this.seatId = seatId
				this.show = true
			},
			//删除桌位
			deleteZw(seatId, status) {
				if (status == 1) {
					uni.showToast({
						title: this.$t('Table.plazmerrdete'), //'此桌位正在使用中，无法删除',
						icon: 'none'
					})
					return
				}
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('Table.plazmdete'), //确认删除该桌位吗?
					confirmText: that.$t('index.sure'),
					cancelText: that.$t('index.qx'),
					confirmColor: '#FF6D9D',
					complete(ret) {
						if (ret.confirm) {
							that.$Request.getA('/admin/shopSeat/deleteShopSeatById?seatId=' + seatId).then(res => {
								if (res.code == 0) {
									uni.showToast({
										title: that.$t('add.deteok'), //'删除成功'
									})
									that.getDataList()
								} else {
									uni.showToast({
										title: res.msg,
										icon: 'none'
									})
								}
							})
						}
					}
				})

			},
			//启用/不启用桌位
			changeStatus(seatId, isEnable) {
				let data = {
					seatId: seatId,
					isEnable: isEnable == 1 ? 0 : 1
				}
				this.$Request.postA('/admin/shopSeat/insertSeat', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: isEnable == 1 ? this.$t('Table.yty') : this.$t('Table.yqy'),
							icon: 'none'
						})
						this.getDataList()
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//添加桌位/修改桌位
			gotoAdd(seatId) {
				if (seatId) {
					uni.navigateTo({
						url: '/pages/addbooks?seatId=' + seatId
					})
				} else {
					uni.navigateTo({
						url: '/pages/addbooks'
					})
				}

			},
			//获取桌位列表
			getDataList() {
				let data = {
					shopId: this.shopId
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