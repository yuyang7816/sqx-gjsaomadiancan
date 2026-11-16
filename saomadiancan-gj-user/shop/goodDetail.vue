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
				<view v-if="type==1">库存：{{dataCentre.inventory?dataCentre.inventory:0}}</view>
				<view class="text-lg text-red"><text class="text-sm">¥</text>{{dataCentre.goodsMoney}}</view>
			</view>
		</view>
		
		<view class="detail">
			<!-- <view class="detail_text">{{dataCentre.goodsName}}</view>
			<view class="detail_biao" v-if="dataCentre.goodsLabel.length">
				<view class="detail_biao_sty" v-for="(ite, ind) in dataCentre.goodsLabel" :key='ind'>{{ite}}</view>
			</view> -->
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
			<text class="text-bold text-black text-lg ">介绍</text>
			<u-parse class='margin-top' :html="dataCentre.goodsDescribe"></u-parse>
		</view>
		<view class="padding margin-top-sm bg-white">
			<view class="text-bold text-black text-lg ">其他信息</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>折扣价</view>
				<view class="text-black">{{dataCentre.goodsMoney?dataCentre.goodsMoney:'暂无'}}</view>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>打包费</view>
				<view class="text-black">{{dataCentre.packMoney?dataCentre.packMoney:'暂无'}}</view>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>标签</view>
				<view class="text-black">{{dataCentre.goodsLabel?dataCentre.goodsLabel:'暂无'}}</view>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>创建时间</view>
				<view class="text-black">{{dataCentre.createTime}}</view>
			</view>
		</view>
		<view class="padding margin-top-sm bg-white" v-if="type==1">
			<view class="text-bold text-black text-lg ">其他设置</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>商品状态</view>
				<u-switch @change="change" active-color="#FFCC00" v-model="checked"></u-switch>
			</view>
			<view class="flex justify-between align-center margin-tb-sm">
				<view>库存编辑</view>
				<input type="number" placeholder="请输入修改库存数" style="text-align: right;" v-model="inventory" />
			</view>
		</view>
		

		<!-- <view class="bg padding">
			<view class="flex justify-between ">
				<view style="font-size:40upx;"><text class="text-sm">￥</text>12.00</view>
				<view style="color:#999999;" class="text-sm">库存236</view>
			</view>
			<view class="text-lg text-bold padding-tb-xs">草莓沫沫酸奶奶昔</view>
			<view class="flex">
				<view class="btn">人气爆款</view>
				<view class="btn">福利必点系列</view>
			</view>
			<view class="text-sm padding-tb-sm">规格</view>
			<view class="flex">
				<view class="box" v-for="(item,index) in list" :key="index" @click="bindge(index)"
					:class="count == index?'boxs':''">{{item.name}}</view>
			</view>
			<view class="text-sm padding-tb-sm">甜度</view>
			<view class="flex">
				<view class="box" v-for="(item,index) in lists" :key="index" @click="bindtd(index)"
					:class="counts == index?'boxs':''">{{item.name}}</view>
			</view>
		</view>
		<view class="margin-top-sm padding-lr bg">
			<view class="text-sm padding-top-sm">规格</view>
			<view class="text-sm padding-tb-sm">蜂蜜黄油，辣辣海苔2款口味可选。精选优质新鲜马铃薯，使用低
				温真空油炸VF技术锁鲜，香甜不腻，咖哧酥脆，净含量：50</view>
		</view> -->
		<!-- <view class="bg padding margin-top-sm">
			<view class="flex justify-between padding-tb-sm">
				<view>打包费</view>
				<view>￥5</view>
			</view>
			<view class="flex justify-between padding-tb-sm">
				<view>折扣价</view>
				<view>￥9.9</view>
			</view>
		</view> -->
		<view v-if="type==1" class="addgood" @click="save()">保存</view>
		<view v-if="type==2" class="addgood" @click="add()">添加商品</view>
		<view v-if="type==1" class="addgood" @click="del()">删除</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				list: [{
					name: '标准'
				}],
				count: 0,
				lists: [{
					name: '无糖'
				}, {
					name: '少糖'
				}, {
					name: '半糖'
				}],
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
			console.log(option)
			this.goodsId = option.goodsId
			this.shopId = option.shopId
			this.type = option.type
			this.getGoodDet()
		},
		methods: {
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
						// that.getGoodsList()
						uni.showToast({
							title:e?'上架成功':'下架成功',
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
							title: '保存成功',
							icon: 'none'
						})
						uni.setStorageSync('updata', true)
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
							title: '添加成功',
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
					title: '提示',
					content: '确认删除商品吗',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
							let data = {
								shopId: that.shopId,
								goodsId: this.goodsId,
							}
							that.$Request.postA("/admin/goodsShop/deleteGoodsByShopId", data).then(res => {
								if (res.code == 0) {
									// that.getGoodsList()
									uni.showToast({
										title: '删除成功',
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
		background: #FFCC00;
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
