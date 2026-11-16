<template>
	<view class="detail_all pages">
		<!-- banner -->
		<view class="detail-banner" v-if="goodsDet.goodsPicture"> 
			<swiper class="swiper" :autoplay="true" interval="2000" duration="500" :circular="true"
				style="width: 100%;height: 410rpx;">
				<swiper-item v-for="(item,index) in goodsDet.goodsPicture" :key='index'>
					<image :src="item" style="width: 100%;" mode="aspectFill"></image>
				</swiper-item>
			</swiper>
		</view>
		<!-- 详情 -->
		<view class="detail">
			<view class="detail_text">{{goodsDet.goodsName}}</view>
			<view class="detail_biao" v-if="goodsDet.goodsLabel">
				<view class="detail_biao_sty" v-for="(ite, ind) in goodsDet.goodsLabel" :key='ind'>{{ite}}</view>
			</view>
			<view class="margin-top" v-for="(item,index) in attrList" :key="index">
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
		<!-- 产品描述 -->
		<view class="detail_describe">
			<view class="detail_describe_ce">
				<view class="detail_describe_text">{{$t('Commodity.cpms')}}</view>
				<u-parse :html="goodsDet.goodsDescribe"></u-parse>
			</view>
		</view>
		<!-- 加购 -->
		<view class="detail_account">
			<view class="detail_account_top">
				<view class="detail_account_top_le">
					<view class="detail_account_text"><text>{{$t('index.qian')}}</text>{{price}}</view>
					<view class="detail_describe_text2">{{checkString}}</view>
				</view>
				
				<u-number-box v-model="value" :min="1" @change="valChange"></u-number-box>
			</view>
			<view class="detail_account_bottom">
				<view class="detail_account_bottom_le" @click="payment()">{{$t('payorder.ljgm')}}</view>
				<view class="detail_account_bottom_ri" @click="orderSel(2)">{{$t('payorder.addgwc')}}</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goodsId: '',
				goodsDet: {},
				skuId: '',
				skuMessage: '',
				skuList: [],
				attrList: [],
				checkString: '',
				checkStateList: [],
				CheckattrValue: false,
				userId: '',
				value: 1,
				shopId: '',
				price: 0,
				echoSkuList: [],
				echoSku: [],
				isEchoSku: {},
				orderType: 1
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('Commodity.title')
			})
			this.userId = uni.getStorageSync('userId')
			this.goodsId = option.id
			this.shopId = option.shopId
			this.orderType = option.orderType
			uni.showLoading({
				title: this.$t('loadingText.jzz')//'加载中...'
			})
			this.getData()
			this.getEchoOrder()
		},
		onShow() {
			this.userId = uni.getStorageSync('userId')
		},
		methods: {
			getEchoOrder() {
				let data = {
					shopId: this.shopId,
					goodsId: this.goodsId
				}
				this.$Request.get("/app/order/echoOrder", data).then(res => {
					if (res.code == 0 && res.data.length) {
						this.echoSkuList = res.data
						this.echoSku = res.data[0].skuMessage.split(',')
					}
				});
			},
			valChange(e) {
				console.log('当前值为: ' + e.value)
				this.value = e.value
			},
			// 立即购买
			payment() {
				if (!this.userId) {
					uni.navigateTo({
						url: '/pages/login/login'
					})
					return
				}
				if (this.attrList.length) {
					if (this.checkString == '' || !this.CheckattrValue) {
						// this.$queue.showToast('请选择正确的商品规格');
						uni.showToast({
							title:this.$t('payorder.plasku'),
							icon:'none'
						})
						return;
					}
				}
				let data = {
					goodsId: this.goodsDet.goodsId,
					skuMessage: this.skuMessage,
					skuId: this.skuId,
					num: this.value,
					orderType: this.orderType,
					shopId: this.shopId,
				}
				this.$Request.post("/app/order/buyGoods", data).then(res => {
					if (res.code == 0) {

						uni.navigateTo({
							url: '/pages/diancan/payOrder?orderId=' + res.data + '&orderType=' + this
								.orderType
						})

					} else {
						this.$queue.showToast(res.msg);
					}
				})
			},
			// 加入购物车
			orderSel(e) {
				if (!this.userId) {
					uni.navigateTo({
						url: '/pages/login/login'
					})
					return
				}
				if (this.attrList.length) {
					if (this.checkString == '' || !this.CheckattrValue) {
						// this.$queue.showToast('请选择正确的商品规格');
						uni.showToast({
							title:this.$t('payorder.plasku'),
							icon:'none'
						})
						return;
					}
				}
				
				
				if (this.echoSku.length) {
					this.echoSkuList.forEach(res => {
						
						if (res.skuMessage == this.skuMessage) {
							this.isEchoSku = res
						}
					})
					
					if (JSON.stringify(this.isEchoSku) != "{}") {
						let data = {
							orderGoodsId: this.isEchoSku.id,
							type: 1,
							num: this.value,
							shopId: this.shopId,
						}
						this.$Request.get("/app/order/updateGoodsNum", data).then(res => {
							if (res.code == 0) {
								setTimeout(function() {
									uni.navigateBack()
								}, 1000)
							} else {
								this.$queue.showToast(res.msg);
							}
						});
						return
					}
				}
				let data = {
					shopId: this.shopId,
					ordersGoodsVoList: [{
						goodsId: this.goodsDet.goodsId,
						skuId: this.skuId,
						num: this.value,
						skuMessage: this.skuMessage,
					}],
					isCart: 1
				}
				this.$Request.postJson("/app/order/insertOrder", data).then(res => {
					if (res.code == 0) {
						// this.$queue.showToast('添加成功');
						uni.showToast({
							title:this.$t('index.addok'),
							icon:'none'
						})
						if (e == 1) {
							uni.navigateTo({
								url: '/pages/diancan/confirmOrder?shopId=' + this.shopId
							})
						} else {
							setTimeout(function() {
								uni.navigateBack()
							}, 1000)
						}
					} else {
						this.$queue.showToast(res.msg);
					}
				})
			},
			// 选择规格
			skuSel(item, index, ind) {
				this.attrList[index].goodsId = index
				this.attrList[index].attrId = ind
				this.checkStateList[index].name = item.name;
				this.checkString = '';
				this.checkStateList.forEach(d => {
					if (d.name) {
						if (this.checkString) {
							this.checkString = this.checkString + ',' + d.name;
						} else {
							this.checkString = d.name;
						}
					}
				});
				console.log(this.attrList)
				for (var i = 0; i < this.skuList.length; i++) {
					let d = this.skuList[i];
					if (d.detailJson == this.checkString) {
						
						this.skuId = d.id
						this.skuMessage = d.detailJson
						this.price = d.skuPrice
						this.CheckattrValue = true;
						break
					} else {
						this.CheckattrValue = false;
					}
				}
			},
			// 获取商品详情
			getData() {
				this.$Request.get("/app/goods/selectGoodsById?goodsId=" + this.goodsId).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.goodsDet = res.data
						if(this.goodsDet.goodsLabel){
							this.goodsDet.goodsLabel =this.goodsDet.goodsLabel.split(',') 
						}else{
							this.goodsDet.goodsLabel = []
						}
						if(this.goodsDet.goodsPicture){
							this.goodsDet.goodsPicture = this.goodsDet.goodsPicture.split(',')
						}
						

						this.price = this.goodsDet.goodsMoney
						this.skuList = this.goodsDet.sku
						if (this.goodsDet.attr.length) {
							this.attrList = this.goodsDet.attr[0] ? this.goodsDet.attr[0].attrValue : []
						} else {
							this.skuId = this.goodsDet.sku[0].id
						}

						console.log(this.attrList)
						this.attrList.forEach(res => {
							let data = {
								name: ''
							}
							this.checkStateList.push(data);
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
					}
				});
			}

		}
	}
</script>

<style scoped>
	.btn {
		padding: 10rpx 30rpx;
		border-radius: 10rpx;
		text-align: center;
		margin-right: 20rpx;
		font-size: 28rpx;
		color: #999999;
		border: 2rpx solid #CCCCCC;
	}

	.active {
		background: rgba(255, 109, 157, 0.2);
		border: 2px solid #FF6D9D;
		opacity: 0.6;
		color: #333333;
	}

	.detail_all {
		position: relative;
	}

	/* banner */
	.detail-banner {
		width: 100%;
		height: 410rpx;
	}

	.detail-banner image {
		width: 100%;
		height: 410rpx;
	}

	/* 详情 */
	.detail {
		width: 100%;
		padding: 3%;
		overflow: hidden;
		position: relative;
		top: -20rpx;
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
		background: rgba(255, 109, 157, 0.2);
		opacity: 0.6;
		color: #FF6D9D;
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
		background: rgba(255, 109, 157, 0.2);
		border: 2px solid #FF6D9D;
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

	/* 产品描述 */
	.detail_describe {
		width: 100%;
		padding: 3% 0;
		margin-bottom: 180rpx;
		position: relative;
		top: -40rpx;
	}

	.detail_describe_ce {
		width: 100%;
		background-color: #FFFFFF;
		padding: 3%;
		font-size: 24rpx;
		padding-bottom: 100upx;
	}

	.detail_describe_text {
		font-size: 30rpx;
		font-weight: 500;
		color: #333333;
		margin-bottom: 20rpx;
	}

	.detail_describe_text2 {
		font-weight: 500;
		margin-top: 2%;
		color: #999999;
	}

	/* 加购 */
	.detail_account {
		width: 100%;
		background-color: #FFFFFF;
		padding: 3%;
		margin-top: 2%;
		position: fixed;
		bottom: 0;
		/* box-shadow: 0 -10rpx 20rpx #CCCCCC; */
	}

	.detail_account_text {
		font-size: 35rpx;
		font-weight: bold;
		color: #333333;
	}

	.detail_account_text text {
		font-size: 27rpx;
	}

	.detail_account_top {
		width: 100%;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.detail_account_top_le {
		width: 80%;
		padding-bottom: 32upx;
		padding-top: 32upx;
	}

	.detail_account_top_ri {
		width: 20%;
		display: flex;
		margin-top: 2%;
	}

	.detail_account_top_text {
		padding: 2%;
		line-height: 54rpx;
	}

	.detail_account_top_ri image {
		width: 54rpx;
		height: 54rpx;
	}

	.detail_account_bottom {
		margin-top: 20rpx;
		width: 100%;
		display: flex;
		justify-content: space-between;
		/* margin: 4% 0 3%; */
	}

	.detail_account_bottom_le {
		width: 47.5%;
		/* margin-right: 5%; */
		border-radius: 44rpx;
		text-align: center;
		line-height: 2.8;
		border: 2rpx solid #8BC6FF;
	}

	.detail_account_bottom_ri {
		width: 47.5%;
		border-radius: 44rpx;
		text-align: center;
		line-height: 2.8;
		background-color: #FF6D9D;
		color: #FFFFFF;
	}
</style>