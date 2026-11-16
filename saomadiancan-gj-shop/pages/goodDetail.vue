<template>
	<view>
		<view class="detail-banner ">
			<swiper class="swiper" :autoplay="true" interval="2000" duration="500" :circular="true" style="width: 100%;height: 410rpx;">
				<swiper-item v-for="(item,index) in dataCentre.goodsPicture" :key='index' >
					<image :src="item" mode="scaleToFill" style="width: 100%;"></image>
				</swiper-item>
			</swiper>
		</view>
		<view class="flex padding bg-white margin-top-sm">
			<image :src="dataCentre.goodsCover" style="width:200rpx;height:200rpx;border-radius: 10rpx;"></image>
			<view class="flex flex-direction justify-between margin-left">
				<view class="text-black text-bold text-lg">{{dataCentre.goodsName}}</view>
				<view class="flex">
					<view v-if="dataCentre.goodsLabel1.length>0" class="btn margin-right-xs"
						v-for="(item,index) in dataCentre.goodsLabel1" :key='index'>{{item}}</view>
				</view>
				<view v-if="type==1">{{$t('Commodity.kc')}}：{{dataCentre.inventory?dataCentre.inventory:0}}</view>
				<view class="text-lg text-red"><text class="text-sm">{{$t('index.qian')}}</text>{{dataCentre.goodsMoney}}</view>
			</view>
		</view>
		
		<view class="detail">
			
			<view class="margin-top" v-for="(item,index) in attrList" :key="index" >
				<view class="text-bold text-black">{{item.value}}</view>
				<view class="flex margin-tb-sm flex-wrap">
					<view v-for="(ite, ind) in item.detail" :key="ind" @click="skuSel(ite,index,ind)"
						class="margin-bottom-sm">
						<view class="btn" :class="item.goodsId == index && item.attrId == ind?'active': ''">
							{{ite.name}}
						</view>
					</view>
				</view>
			</view>
		</view>
		<view class="padding bg-white">
			<text class="text-bold text-black text-lg ">{{$t('Commodity.js')}}</text>
			<u-parse class='margin-top' :html="dataCentre.goodsDescribe"></u-parse>
		</view>
		<view class="padding margin-top-sm bg-white">
			<view class="text-bold text-black text-lg ">{{$t('payorder.info')}}</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>{{$t('Commodity.goodsMoney')}}</view>
				<view class="text-black">{{dataCentre.goodsMoney?dataCentre.goodsMoney:$t('Commodity.zw')}}</view>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>{{$t('Commodity.packMoney')}}</view>
				<view class="text-black">{{dataCentre.packMoney?dataCentre.packMoney:$t('Commodity.zw')}}</view>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>{{$t('Commodity.label')}}</view>
				<view class="text-black">{{dataCentre.goodsLabel?dataCentre.goodsLabel:$t('Commodity.zw')}}</view>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>{{$t('purse.cjsj')}}</view>
				<view class="text-black">{{dataCentre.createTime}}</view>
			</view>
		</view>
		<view class="padding margin-top-sm bg-white" v-if="type==1">
			<view class="text-bold text-black text-lg ">{{$t('Commodity.set')}}</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>{{$t('Commodity.status')}}</view>
				<u-switch @change="change" active-color="#FF6D9D" v-model="checked"></u-switch>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>{{$t('Commodity.kc')}}</view>
				<input type="number" :placeholder="$t('Commodity.plakc')" style="text-align: right;" v-model="inventory" />
			</view>
		</view>
		

		<view v-if="type==1" class="addgood" @click="save()">{{$t('user.save')}}</view>
		<view v-if="dataCentre.shopId!=0" class="addgood" @click="updateGoods()">{{$t('index.updete')}}</view>
		<view v-if="type==2" class="addgood" @click="add()">{{$t('Commodity.add')}}</view>
		<view v-if="type==1" class="addgood" @click="del()">{{$t('index.dete')}}</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				count: 0,
				counts: 0,
				goodsId: '',
				good: {},
				dataCentre: '',
				checked: false,
				shopId: '',
				inventory: '',
				type: 1,
				status: 1,
				attrList: [],
				skuList: [],
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('Commodity.title')
			})
			console.log(option)
			this.goodsId = option.goodsId
			this.shopId = option.shopId
			this.type = option.type
			this.getGoodDet()
		},
		onShow() {
			if(this.goodsId){
				this.getGoodDet()
			}
		},
		methods: {
			updateGoods(){
				uni.navigateTo({
					url:'/pages/setAddGoods?goodsId='+this.goodsId
				})
			},
			change(e) {
				console.log()
				let that = this
				let data = {
					shopId: that.shopId,
					goodsId: that.goodsId,
					status: e?0:1
				}
				that.$Request.postA("/admin/goodsShop/soldOutOrPutaway", data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title:e?that.$t('Commodity.sjok'):that.$t('Commodity.xjok'),
							icon: 'none'
						})
						uni.setStorageSync('updata', true)
						setTimeout(function() {
							let pages = getCurrentPages(); // 当前页面
							let beforePage = pages[pages.length - 2]; // 上一页
							uni.navigateBack();
						}, 1000)
						
					}
				});
			},
			getGoodDet() {
				if(this.type == 1) {
					let data = {
						goodsId: this.goodsId,
						shopId: this.shopId
					}
					this.$Request.getA("/admin/goodsShop/selectGoodsByShopIdAndGoodsId", data).then(res => {
						if (res.code == 0) {
							this.dataCentre = res.data
							this.dataCentre.goodsPicture = this.dataCentre.goodsPicture.split(',')
							this.dataCentre.goodsLabel1 = this.dataCentre.goodsLabel ? this.dataCentre.goodsLabel
								.split(',') : []
							console.log(this.dataCentre)
							this.checked = this.dataCentre.gsrStatus == 0 ? true : false
							this.skuList = this.dataCentre.sku
							if (this.dataCentre.attr.length) {
								this.attrList = this.dataCentre.attr[0] ? this.dataCentre.attr[0].attrValue : []
							} else {
								this.skuId = this.dataCentre.sku[0].id
							}
							
							
							this.attrList.forEach(res => {
								let data = {
									name: ''
								}
								// this.checkStateList.push(data);
								let detail = [];
								res.detail.split(',').forEach(d => {
									let data = {
										name: '',
										state: ''
									}
									data.name = d;
									detail.push(data);
								});
								res.detail = detail;
							})
							console.log(this.attrList)
						}
					});
				} else {
					let data = {
						goodsId: this.goodsId
					}
					this.$Request.getA("/admin/goods/selectGoodsById", data).then(res => {
						if (res.code == 0) {
							this.dataCentre = res.data
							this.dataCentre.goodsPicture = this.dataCentre.goodsPicture.split(',')
							this.dataCentre.goodsLabel1 = this.dataCentre.goodsLabel ? this.dataCentre.goodsLabel
								.split(',') : []
							console.log(this.dataCentre)
							this.checked = this.dataCentre.gsrStatus == 0 ? true : false
							this.skuList = this.dataCentre.sku
							if (this.dataCentre.attr.length) {
								this.attrList = this.dataCentre.attr[0] ? this.dataCentre.attr[0].attrValue : []
							} else {
								this.skuId = this.dataCentre.sku[0].id
							}
							
							this.attrList.forEach(res => {
								let data = {
									name: ''
								}
								// this.checkStateList.push(data);
								let detail = [];
								res.detail.split(',').forEach(d => {
									let data = {
										name: '',
										state: ''
									}
									data.name = d;
									detail.push(data);
								});
								res.detail = detail;
							})
							console.log(this.attrList)
						}
					});
				}
				
			},
			// 修改库存
			save() {
				let data = {
					goodsId: this.goodsId,
					shopId: this.shopId,
					inventory: this.inventory
				}
				this.$Request.postJsonA("/admin/goodsShop/updateGoodsByShopId", data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('user.baocun'),//'保存成功',
							icon: 'none'
						})
						setTimeout(function() {
							uni.navigateBack({
								delta: 1
							})
						}, 1000)
					}
				});
			},
			// 添加
			add(e) {
				let data = [{
					goodsId: this.goodsId,
					shopId: this.shopId
				}]
				this.$Request.postJsonA("/admin/goodsShop/addGoodsByShopId", data).then(res => {
					if (res.code == 0) {
						// this.page = 1
						// this.getGoodsList()
						uni.showToast({
							title: this.$t('index.addok'),//'添加成功',
							icon: 'none'
						})
						uni.setStorageSync('updata', true)
						setTimeout(function() {
							uni.navigateBack({
								delta: 2
							})
						}, 1000)
					}
				});
			},
			// 删除
			del(e) {
				let that = this
				uni.showModal({
					title:that.$t('index.wxts'),
					content:that.$t('Commodity.pladetesp'),// '确认删除商品吗',
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								shopId: that.shopId,
								goodsId: that.goodsId,
							}
							that.$Request.postA("/admin/goodsShop/deleteGoodsByShopId", data).then(res => {
								if (res.code == 0) {
									// that.getGoodsList()
									uni.showToast({
										title:that.$t('删除成功'),// '删除成功',
										icon: 'none'
									})
									setTimeout(function() {
										let pages = getCurrentPages(); // 当前页面
										let beforePage = pages[pages.length - 2]; // 上一页
										uni.navigateBack({
											success: function() {
												beforePage.onLoad(); // 执行上一页的onLoad方法
											}
										});
									}, 1000)
								}
							});
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			},

		}
	}
</script>

<style>
	page {
		background: #EDF1F7;
	}

	.bg {
		background: #FFFFFF;
	}

	.btn {
		background: rgba(216, 2, 4, 0.2);
		opacity: 0.6;
		border-radius: 4upx;
		color: #D80204;
		padding: 8rpx 13rpx;
		font-size: 23rpx;
		margin-right: 16rpx;
	}

	.box {
		border: 2upx solid #CCCCCC;
		/* opacity: 0.6; */
		border-radius: 8upx;
		color: #999999;
		font-size: 30upx;
		padding: 13rpx 44rpx;
		margin-right: 35rpx;
	}

	.boxs {
		background: rgba(252, 210, 2, 0.2);
		border: 2upx solid #FCD202;
		/* opacity: 0.6; */
		border-radius: 8upx;
		color: #333333 !important;
		font-size: 30upx;
		padding: 13rpx 44rpx;
	}

	.addgood {
		width: 685upx;
		height: 88upx;
		background: #FF6D9D;
		border-radius: 8upx;
		text-align: center;
		line-height: 88rpx;
		margin: 40rpx auto;
		/* position: fixed;
		bottom: 35rpx;
		left: 0;
		right: 0; */
		font-size: 32rpx;
		font-weight: bold;
		color: #FFFFFF;
	}
	
	.detail-banner {
		width: 100%;
		height: 410rpx;
	}
	
	.detail-banner image {
		width: 100%;
		height: 410rpx;
	}
	
	.detail {
		width: 100%;
		padding: 3%;
		overflow: hidden;
		/* position: relative; */
		/* top: -20rpx; */
		background-color: #FFFFFF;
		border-radius: 32rpx 0 0 0;
	}
	
	.detail_text {
		font-size: 36rpx;
		font-weight: 800;
		color: #333333;
	}
	
	.detail_biao {
		display: flex;
		padding: 2% 0;
	}
	
	.detail_biao_sty {
		/* padding: 1% 1.5%; */
		padding: 10rpx 20rpx;
		background: rgba(216, 2, 4, 0.2);
		opacity: 0.6;
		color: #D80204;
		border-radius: 8rpx;
		font-size: 24rpx;
		margin-right: 2%;
	}
	
	.detail_text2 {
		font-size: 24rpx;
		font-family: PingFang SC;
		color: #333333;
		margin-top: 1%;
	}
	
	.detail_biao2 {
		display: flex;
		padding: 1% 0;
		font-size: 30rpx;
	}
	
	.detail_biao2_sty {
		padding: 1% 2.5%;
		/* width: 13%; */
		text-align: center;
		background: rgba(252, 210, 2, 0.2);
		border: 2rpx solid #FCD202;
		opacity: 0.6;
		border-radius: 8rpx;
		margin-right: 2%;
	}
	
	.detail_biao2_sty2 {
		padding: 1% 2.5%;
		/* width: 13%; */
		text-align: center;
		border: 2rpx solid #999999;
		opacity: 0.6;
		color: #999999;
		border-radius: 8rpx;
		margin-right: 3%;
	}
</style>
