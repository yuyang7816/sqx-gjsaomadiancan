<template>
	<view class="pages">
		<!-- 余额 -->
		<view class="tosend">
			<view class="tosend_text" v-if="dataDet.status == 6">{{$t('order.zhizuo')}}...</view>
			<view class="tosend_text" v-if="dataDet.status == 3 && dataDet.orderType == 1">{{$t('order.dqc')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 3 && dataDet.orderType == 2">{{$t('order.dps')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 1 && dataDet.orderType == 3">{{$t('order.dzf')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 4">{{$t('order.ywc')}}</view>
			<view class="tosend_text" v-if="dataDet.status == 5">{{$t('order.yqx')}}</view>
			<view class="tosend_header">
				<!-- 排序 -->
				<view class="cont_two_top" v-if="dataDet.status == 3 && dataDet.orderType!=3">
					<view class="cont_two_top_le">{{$t('order.qchm')}}</view>
				</view>
				<view v-if="dataDet.status == 3 && dataDet.orderType == 1" class="cont_two_text">{{dataDet.orderCode}}
				</view>
				<view v-if="dataDet.status == 3 && dataDet.orderType == 1" class="cont_two_text2">
					{{$t('order.qmhy')}}<text>{{dataDet.countOrder?dataDet.countOrder:0}}</text>{{$t('order.pd')}}
				</view>
				<view v-if="dataDet.status != 3 && dataDet.orderType!=3">
					<view class="flex justify-between">
						<view>{{$t('order.qchm')}}</view>
					</view>
					<view style="font-size:58upx;" class="text-center text-bold padding-tb">{{dataDet.orderCode}}</view>
				</view>
				<!-- 商品列表 -->
				<view class="" :style="dataDet.orderType==3?'margin-top: 0;':''"
					v-for="(item,index) in dataDet.orderGoodsList" :key='index'>
					<view class="tosend_header_food">
						<view class="tosend_header_food_ce justify-between">
							<view class="tosend_header_food_text">{{item.goodsName}}</view>
							<view class="tosend_header_food_text2">{{item.skuMessage}}</view>
						</view>
						<view class="tosend_header_food_ri">
							<view class="tosend_header_food_text"><text>{{$t('index.qian')}}</text>{{item.goodsPrice}}</view>
							<view class="tosend_header_food_text2">X{{item.goodsNum}}</view>
						</view>
					</view>

					<view v-if="item.goodRemarks" style="margin-top: 20rpx;">
						<view class="tosend_header_food_text2">{{$t('payorder.bz')}}：{{item.goodRemarks}}</view>
					</view>
				</view>
				<view class="tosend_header_do do_top" v-if="dataDet.errandMoney>0&& dataDet.orderType == 2">
					<view>{{$t('payorder.ptf')}}</view>
					<view class="tosend_header_do_ri">{{$t('index.qian')}}{{dataDet.errandMoney}}</view>
				</view>
				<view class="tosend_header_do" v-if="dataDet.packMoney>0&& dataDet.orderType == 2">
					<view>{{$t('payorder.dbf')}}</view>
					<view class="tosend_header_do_ri">{{$t('index.qian')}}{{dataDet.packMoney}}</view>
				</view>
				<view class="tosend_header_do do_bot" v-if="dataDet.couponMoney>0">
					<view>{{$t('yjq.title')}}</view>
					<view class="tosend_header_do_ri">-{{$t('index.qian')}}{{dataDet.couponMoney}}</view>
				</view>
				<view class="tosend_header_do margin-top">
					<view class="tosend_header_do_le2">{{$t('payorder.sf')}}</view>
					<block v-if="dataDet.orderType!=3">
						<view class="tosend_header_do_ri2"><text>{{$t('index.qian')}}</text>{{dataDet.payMoney}}</view>
					</block>
					<block v-else>
						<view class="tosend_header_do_ri2"><text>{{$t('index.qian')}}</text>{{dataDet.orderMoney}}</view>
					</block>
				</view>
			</view>
			<!-- 其他信息 -->
			<view class="tosend_cont">
				<view class="tosend_header_text">{{$t('payorder.info')}}</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType == 2">
					<view class="tosend_cont_infor_le">{{$t('add.username')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.address.userName}}</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType == 2">
					<view class="tosend_cont_infor_le">{{$t('add.phone')}}</view>
					<view class="tosend_cont_infor_ri" @click="call(dataDet.address.userPhone)">
						{{dataDet.address.userPhone}}
					</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType == 2">
					<view class="tosend_cont_infor_le">{{$t('add.xxdz')}}</view>
					<view class="tosend_cont_infor_ri" @click="openMap(dataDet.address)">
						{{dataDet.address.address}}{{dataDet.address.addressDetail}}
					</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType != 3">
					<view class="tosend_cont_infor_le">{{$t('order.qchm')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.orderCode}}</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType != 3">
					<view class="tosend_cont_infor_le">{{$t('payorder.xsdj')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.payTime}}</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType == 3">
					<view class="tosend_cont_infor_le">{{$t('payorder.xsdj')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.createTime}}</view>
				</view>
				<view class="flex justify-between align-center" style="margin-top: 3%;">
					<view class="tosend_cont_infor_le">{{$t('payorder.ddbh')}}</view>
					<view class="tosend_cont_infor_ri">{{dataDet.orderNumber}}</view>
					<view class="text-orange margin-left-lg" @click="copy(dataDet.orderNumber)">{{$t('index.copy')}}</view>
				</view>
				<view class="tosend_cont_infor" v-if="dataDet.orderType != 3">
					<view class="tosend_cont_infor_le">{{$t('purse.paytype')}}</view>
					<view class="tosend_cont_infor_ri" v-if="dataDet.payType==1">微信支付</view>
					<view class="tosend_cont_infor_ri" v-if="dataDet.payType==2">{{$t('pay.lingqian')}}</view>
				</view>
				<view class="tosend_cont_infor">
					<view class="tosend_cont_infor_le">{{$t('payorder.bz')}}</view>
					<view class="tosend_cont_infor_ri" v-if="dataDet.orderRemarks">{{dataDet.orderRemarks}}</view>
					<view class="tosend_cont_infor_ri" v-else>{{$t('order.wu')}}</view>
				</view>
			</view>
			<view class="addgood" @click="refund()" v-if="dataDet.status == 6">{{$t('order.tk')}}</view>
			<view class="addgood" @click="finish()" v-if="dataDet.status == 6">{{$t('order.zzwc')}}</view>
			<view class="addgood" @click="upBz(dataDet)" v-if="dataDet.orderType != 3 && dataDet.status != 4">{{$t('order.xgbz')}}
			</view>

			
			<view class="addgood" @click="wancheng()" v-if="dataDet.status == 3&&dataDet.orderType==1">{{$t('order.zzywc')}}</view>
			<view class="addgood" @click="peisong()" v-if="dataDet.status == 3&&dataDet.orderType==2">{{$t('order.wcdd')}}</view>
			<block v-if="dataDet.orderType!=3">
				<view class="addgood" @click.stop="xiaoPiao(dataDet)"
					v-if="dataDet.status == 3||dataDet.status == 4||dataDet.status == 6&&dataDet.orderType==2">{{$t('order.dyxp')}}
				</view>
			</block>

			<view class="addgood" @click.stop="xiaoPiao(dataDet)" v-if="dataDet.orderType == 3">{{$t('order.dyxp')}}</view>
		</view>

		<u-popup v-model="orderRemarkShow" width="600" :closeable="true" mode="center" border-radius="14">
			<view class="orderRemarkTitle">
				{{$t('order.xgbz')}}
			</view>
			<view class="orderRemarkInput">
				<u-input v-model="orderRemarks" height="100" type="textarea" :border="true" :placeholder="$t('payorder.plabz')" />
			</view>
			<view @click="saveUpbz()" class="orderRemarkInput-btn flex justify-center align-center">
				{{$t('index.sure')}}
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				orderRemarks: '',
				orderRemarkShow: false,
				orderNumber: '',
				dataDet: {}
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('order.detailtit')
			})
			uni.showLoading({
				title: this.$t('loadingText.jzz')+'...'//加载中
			})
			this.orderNumber = option.orderNumber
			this.getDataDet()
		},
		methods: {
			//修改备注
			saveUpbz() {
				if (!this.orderRemarks) {
					uni.showToast({
						title: this.$t('payorder.plabz'),//'请输入备注内容'
						icon: 'none'
					})
					return
				}
				let data = {
					orderRemarks: this.orderRemarks,
					orderId: this.dataDet.orderId
				}
				this.$Request.postA('/admin/order/updateOrderRemarks', data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('add.updeteok')//'修改成功'
						})
						this.orderRemarkShow = false
						setTimeout(() => {
							this.getDataDet()
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})


			},
			//打开修改备注弹框
			upBz(info) {
				this.orderRemarks = JSON.parse(JSON.stringify(info.orderRemarks))
				this.orderRemarkShow = true
			},
			getDataDet() {
				let data = {
					page: 1,
					limit: 10,
					orderNumber: this.orderNumber
				}
				this.$Request.getA("/admin/order/selectAllOrder", data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.dataDet = res.data.list[0]
						this.dataDet.orderGoodsList.forEach(res => {
							res.goodsPicture = res.goodsPicture.split(',')
						})
						this.dataDet.address = JSON.parse(this.dataDet.address)
						console.log(this.dataDet)
					}
				});
			},
			copy(value) {
				uni.setClipboardData({
					data: value, //要被复制的内容
					success: () => { //复制成功的回调函数
						uni.showToast({ //提示
							title: this.$t('index.copyok')//'复制成功'
						})
					}
				});
			},
			// 打电话
			call(phone) {
				uni.makePhoneCall({
					phoneNumber: phone
				});
			},
			// 打开地图
			openMap(e) {
				let latitude = e.latitude;
				let longitude = e.longitude;
				uni.openLocation({
					latitude: Number(latitude),
					longitude: Number(longitude),
					name: e.address,
					success: function() {
						console.log('success');
					}
				});
			},
			refund() {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.platkm'), //确认退款吗?
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: that.dataDet.orderId
							}
							that.$Request.postA("/admin/order/adminCancelOrder", data).then(res => {
								if (res.code == 0) {
									let pages = getCurrentPages(); // 当前页面
									let beforePage = pages[pages.length - 2]; // 上一页
									uni.navigateBack({
										success: function() {
											beforePage.onLoad(); // 执行上一页的onLoad方法
										}
									});
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			finish(e) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plaqrzzwcm'), //确认制作完成吗?
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: that.dataDet.orderId,
								orderNumber: that.dataDet.orderNumber,
								status: 3
							}
							that.$Request.postJsonA("/admin/order/updateOrder", data).then(res => {
								if (res.code == 0) {
									let pages = getCurrentPages(); // 当前页面
									let beforePage = pages[pages.length - 2]; // 上一页
									uni.navigateBack({
										success: function() {
											beforePage.onLoad(); // 执行上一页的onLoad方法
										}
									});
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			wancheng() {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plawcm'), //确认完成吗?
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: that.dataDet.orderId,
								orderNumber: that.dataDet.orderNumber,
								status: 4
							}
							that.$Request.postJsonA("/admin/order/updateOrder", data).then(res => {
								if (res.code == 0) {
									let pages = getCurrentPages(); // 当前页面
									let beforePage = pages[pages.length - 2]; // 上一页
									uni.navigateBack({
										success: function() {
											beforePage.onLoad(); // 执行上一页的onLoad方法
										}
									});
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			peisong(e) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plawcm'), //确认完成订单吗?
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: that.dataDet.orderId,
								orderNumber: that.dataDet.orderNumber,
								status: 4
							}
							that.$Request.postJsonA("/admin/order/updateOrder", data).then(res => {
								if (res.code == 0) {
									let pages = getCurrentPages(); // 当前页面
									let beforePage = pages[pages.length - 2]; // 上一页
									uni.navigateBack({
										success: function() {
											beforePage.onLoad(); // 执行上一页的onLoad方法
										}
									});
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
			// 补打小票
			xiaoPiao(e) {
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('order.plabdco'), //确认补打小票吗?
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					success: function(re) {
						if (re.confirm) {
							console.log('用户点击确定');
							let data = {
								orderId: e.orderId,
							}
							that.$Request.post("/app/order/print", data).then(res => {
								if (res.code == 0) {
									
								} else {
									that.$queue.showToast(res.msg)
								}
							});
						} else if (re.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},
		}
	}
</script>

<style scoped lang="scss">
	.orderRemarkInput-btn {
		width: calc(100% - 60rpx);
		height: 80rpx;
		background-color: #FF6D9D;
		border-radius: 14rpx;
		color: #FFFFFF;
		font-size: 30rpx;
		font-weight: bold;
		margin: 0 auto;
		margin-bottom: 50rpx;
	}

	.orderRemarkInput {
		padding: 30rpx;
	}

	.orderRemarkTitle {
		width: 100%;
		margin-top: 30rpx;
		font-weight: bold;
		text-align: center;
	}

	/* 余额 */
	.tosend {
		width: 100%;
		height: 280rpx;
		background: linear-gradient(0deg, rgba(255, 255, 255, 0) 0%, #FF9CBD 100%);
	}

	.tosend_text {
		padding: 3% 3% 4%;
		font-size: 38upx;
		font-weight: 800;
		color: #FFFFFF;
	}

	.tosend_header {
		width: 94%;
		background-color: #FFFFFF;
		margin: 0 auto;
		border-radius: 18rpx;
		padding: 3%;
	}

	/* 排序 */
	.cont_two_top {
		width: 100%;
		display: flex;
		justify-content: space-between;
	}

	.cont_two_top_le {
		font-size: 30rpx;
		font-weight: 500;
		color: #333333;
	}

	.cont_two_top_ri {
		padding: 6rpx 10rpx;
		text-align: center;
		background: rgba(255, 19, 10, 0.2);
		font-size: 24rpx;
		border: 2rpx solid #FF130A;
		color: #FF130A;
		opacity: 0.6;
		border-radius: 8rpx;
	}

	.cont_two_text {
		font-size: 56rpx;
		text-align: center;
		font-weight: bold;
		color: #333333;
		margin: 5% 0;
		line-height: 32rpx;
	}

	.cont_two_text2 {
		font-size: 30rpx;
		width: 100%;
		font-weight: 500;
		color: #333333;
		text-align: center;
		padding-bottom: 4%;
		line-height: 32rpx;
		border-bottom: 1rpx solid #E6E6E6;
	}

	.cont_two_text2 text {
		color: #FF130A;
	}

	/* 商品列表 */
	.tosend_header_text {
		font-family: PingFang SC;
		font-weight: 800;
		color: #333333;
		line-height: 2;
		font-size: 30rpx;
	}

	.tosend_header_food {
		width: 100%;
		display: flex;
		margin-top: 35rpx;
		justify-content: space-between;
	}

	.tosend_header_food_le {
		width: 15%;
	}

	.tosend_header_food_le image {
		width: 110rpx;
		height: 110rpx;
	}

	.tosend_header_food_ce {
		/* margin: 0 0 0 4%; */
		width: 57%;
	}

	.tosend_header_food_ri {
		text-align: right;
		width: 25%;
	}

	.tosend_header_food_text {
		font-size: 34rpx;
		font-weight: 500;
		color: #333333;
		line-height: 1.8;
	}

	.tosend_header_food_text text {
		font-size: 25rpx;
	}

	.tosend_header_food_text2 {
		font-size: 30rpx;
		font-weight: 500;
		color: #999999;
	}

	.do_top {
		padding-top: 3%;
	}

	.do_bot {
		padding-bottom: 3%;
		border-bottom: 2rpx solid #E6E6E6;
	}

	.tosend_header_do {
		width: 100%;
		color: #333333;
		font-size: 30rpx;
		display: flex;
		line-height: 1.5;
	}

	.tosend_header_do view {
		flex: 1;
	}

	.tosend_header_do_ri {
		text-align: right;
	}

	.tosend_header_do_le2 {
		font-family: PingFang SC;
		font-weight: 500;
		color: #999999;
		padding-top: 1.5%;
	}

	.tosend_header_do_ri2 {
		text-align: right;
		color: #EA0000;
		font-size: 40rpx;
	}

	.tosend_header_do_ri2 text {
		font-size: 30rpx;
	}

	/* 其他信息 */
	.tosend_cont {
		width: 94%;
		background-color: #FFFFFF;
		margin: 3% auto;
		border-radius: 18rpx;
		padding: 3%;
	}

	.tosend_cont_infor {
		display: flex;
		margin-top: 30rpx;
	}

	.tosend_cont_infor_le {
		flex: 1;
		color: #999999;
	}

	.tosend_cont_infor_ri {
		flex: 2;
		text-align: right;
		color: #333333;
	}

	.tosend_cont_text {
		text-align: right;
	}

	.addgood {
		width: 685upx;
		height: 88upx;
		background: #FF6D9D;
		border-radius: 8upx;
		text-align: center;
		line-height: 88rpx;
		margin: 40rpx auto;
		font-size: 32rpx;
		font-weight: bold;
		color: #FFFFFF;
	}
</style>