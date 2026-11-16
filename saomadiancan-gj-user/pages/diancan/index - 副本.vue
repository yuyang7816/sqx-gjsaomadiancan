<template>
	<view>
		<view class="food_all u-skeleton" v-if="show">
			<!-- banner -->
			<swiper class="swiper u-skeleton-rect" :autoplay="true" interval="2000" duration="500" :circular="true"
				style="width: 100%;height: 380rpx;">
				<swiper-item v-for="(item,index) in shop.shopBanner" :key='index'>
					<image :src="item" mode="scaleToFill" style="width: 100%;"></image>
				</swiper-item>
			</swiper>
			<!-- 食品 -->
			<view class="food u-skeleton-rect">
				<!-- 店名地址 -->
				<view class="food_address">
					<view class="food_address_le">
						<view class="food_address_le_top" @click="reselect()">
							<view class="food_address_le_top_le">
								<image src="../../static/images/diancan/dianlogo.png" mode="aspectFit"></image>
							</view>
							<view class="food_address_le_top_ce">{{shop.shopName}}</view>
							<view class="food_address_le_top_ce_ri">
								<image src="../../static/images/diancan/right2.png" mode="widthFix"></image>
							</view>
						</view>

					</view>
					<view class="flex Switch">
						<view @click="tabSwidth(1)" :class="orderType==1?'selSwitch':''">自取</view>
						<view @click="tabSwidth(2)" :class="orderType==2?'selSwitch':''">外卖</view>
					</view>
				</view>
				<view class="flex justify-between" style="margin: 3%;">
					<view class="food_address_text">距离您{{shop.distancess}}</view>
				</view>

				<view class="VerticalBox" :style="{height:(screenHeight -360)+'px'}" v-if="dataList && dataList.length>0">
					<scroll-view class="VerticalNav nav bg-gray" scroll-y scroll-with-animation
						:scroll-top="verticalNavTop">
						<view style="font-size: 28upx;" class="cu-item text-lg" :class="index==tabCur?'cur':'text-gray'"
							v-for="(item,index) in dataList" :key="index" @tap="TabSelect" :data-id="index">
							{{item.classifyName}}
						</view>
					</scroll-view>
					<scroll-view class="VerticalMain" scroll-y scroll-with-animation :scroll-into-view="'main-'+mainCur"
						@scroll="VerticalMain">
						<view class="bg-white padding-sm  margin-bottom-sm" v-for="(item,index) in dataList"
							:key="index" :id="'main-'+index">
							<view class="flex justify-between padding-bottom-sm" @click="goDet(ite.goodsId)"
								v-for="(ite,ind) in item.goodsList" :key='ind'>
								<image :src="ite.goodsCover" mode=""
									style="width: 184rpx;height: 184rpx;border-radius: 10rpx;"></image>
								<view class="flex-sub margin-left-sm padding-tb-xs flex flex-direction justify-between">
									<view class="text-black text-lg text-bold" style="font-size: 30upx;">
										{{ite.goodsName}}
									</view>
									<view class="text-gray text-sm u-line-2"
										style="overflow: hidden;line-clamp: 2;-webkit-line-clamp: 2;width: 100%;">
										{{ite.goodsSynopsis?ite.goodsSynopsis:''}}
									</view>
									<view class="flex justify-between">
										<view class="text-sm text-red">
											¥ <text class="text-lg">{{ite.goodsMoney}}</text>
										</view>
										<image @click.stop="selSku(ite)" src="../../static/images/diancan/add.png"
											mode="" style="width: 40rpx;height: 40rpx"></image>
									</view>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
				<empty v-if="!dataList.length"></empty>
			</view>
			<!-- 结算 -->
			<view class="settlement" @click="isPopupShow">
				<view class="settlement_img">
					<image src="../../static/images/index/diancan1.png" mode=""></image>
					<view class="settlement_hot">{{goodsNum}}</view>
				</view>
				<view class="settlement_le">
					<text>￥</text>{{totalPrice}}
				</view>
				<view class="settlement_ri" @click.stop="goConfirm()">去结算</view>
			</view>
			<!-- 购物车弹窗 -->
			<u-popup v-model="popupShow" mode="bottom" border-radius="20">
				<view class="padding">
					<view class="flex justify-between align-center margin-bottom-sm">
						<view class="text-bold text-black text-df">已选餐品</view>
						<view class="flex align-center">
							<image src="../../static/images/diancan/delete.png" style="width: 28rpx;height: 31rpx;"
								mode=""></image>
							<text class="margin-left-xs" @click="empty">清空购物车</text>
						</view>
					</view>
					<scroll-view scroll-y='true' class="popup" v-if="goodsList && goodsList.orderGoodsList">
						<view v-for="(item,index) in goodsList.orderGoodsList[0]" :key='index'>
							<view class="flex align-center margin-tb-sm">
								<image :src="item.goodsPicture[0]"
									style="width: 96rpx;height: 96rpx;border-radius: 10rpx;">
								</image>
								<view class="margin-left-sm">
									<view>{{item.goodsName}}</view>
									<view>{{item.skuMessage}}</view>
								</view>
							</view>
							<view class="flex justify-between align-center">
								<view class="text-bold text-sm">¥<text class="text-lg">{{item.goodsPrice}}</text>
								</view>
								<view class="flex align-center justify-between" style="width: 155rpx;">
									<view @click.stop="noAdd(item,index)">
										<image src="../../static/images/diancan/jian.png"
											style="width: 54rpx;height: 54rpx;"></image>
									</view>
									<view class="text-center margin-lr-xs margin-bottom-xs">{{item.goodsNum}}</view>
									<view @click="add(item,index)">
										<image src="../../static/images/diancan/add.png"
											style="width: 50rpx;height: 50rpx;"></image>
									</view>
								</view>
							</view>
						</view>
					</scroll-view>
				</view>
				<view class="settlement1 margin-top-lg">
					<view class="settlement_img">
						<image src="../../static/images/index/diancan1.png" mode=""></image>
						<view class="settlement_hot">{{goodsNum}}</view>
					</view>
					<view class="settlement_le">
						<text>￥</text>{{totalPrice}}
					</view>
					<view class="settlement_ri" @click.stop="goConfirm()">去结算</view>
				</view>
			</u-popup>
			<!-- 选择规格弹窗 -->
			<u-popup v-model="skuShow" mode="center" :closeable='true' border-radius="20">
				<view style="width: 700rpx;">
					<view class="text-center text-lg text-bold padding-tb">{{goodsDet.goodsName}}</view>
					<view class="margin-top-sm padding-lr" v-for="(item,index) in attrList" :key="index">
						<view class="text-bold text-black">{{item.value}}</view>
						<view class="flex margin-tb-sm flex-wrap">
							<view v-for="(ite, ind) in item.detail" :key="ind" @click="skuSel(ite,index,ind)"
								class="margin-bottom-sm">
								<view class="skuBtn" :class="item.goodsId == index && item.attrId == ind?'active': ''">
									{{ite.name}}
								</view>
							</view>
						</view>
					</view>
					<view class="flex justify-between padding-lr padding-top">
						<view>
							<view class="text-xl text-bold text-black"><text class="text-sm">￥</text>{{price}}
							</view>
							<view class="detail_describe_text2">{{checkString}}</view>
						</view>
						<u-number-box v-model="value" min="1" @change="valChange"></u-number-box>
					</view>
					<view class="detail_account_bottom padding">
						<view class="detail_account_bottom_le" @click="payment()">立即购买</view>
						<view class="detail_account_bottom_ri" @click="orderSel(2)">加入购物车</view>
					</view>
				</view>
			</u-popup>
		</view>

		<view class="select_all" v-else>
			<!-- 餐厅 -->
			<view class="select">
				<!-- 搜索 -->
				<view class="select_search">
					<view class="select_search_ri">
						<input type="text" placeholder="请输入店铺名称搜索" v-model="shopName" @confirm="search" />
						<view class="action">
							<button @click="search"
								class="cu-btn margin-left bg-yellow light shadow-blur round">搜索</button>
						</view>
					</view>
				</view>
				<!-- 附近 -->
				<view class="nearby">
					<view class="nearby_text">附近店铺</view>
					<view class="nearby_address_active" v-for="(item,index) in shopList" :key='index'
						@click="selectShop(item)">
						<view class="nearby_address_le">
							<view class="nearby_text2">
								<text class="nearby_text2_">{{item.shopName}}</text>
								<view class="flex align-center">
									<img src="../../static/images/diancan/location.png" alt=""
										style="width: 30rpx;height: 34rpx;">
									<text class="margin-left-xs">距您{{item.distancess}}</text>
								</view>
							</view>
							<view class="nearby_text3">{{item.detailedAddress}}</view>
							<view class="nearby_text3">营业时间：{{item.openTime}} - {{item.closeTime}}</view>
						</view>
					</view>
				</view>
			</view>
			<!-- @click.stop="hintShow=false" -->
			<view v-if="hintShow" class="hintPopul" @click="hintShow= false">
				<view class="content">
					<image src="../../static/images/diancan/shop.png" mode="" style="width: 200rpx;height: 180rpx;">
					</image>
					<view class="text-xl text-bold">店铺打烊啦</view>
					<view class="hintText margin-top-sm text-gray">现在店铺已经打烊了,营业时间</view>
					<view class="margin-top-xs text-gray">{{shop.openTime}}-{{shop.closeTime}}</view>
					<view class="skuBtn margin-top" @click="hintShow=false">知道了</view>
				</view>
			</view>
		</view>
		<u-skeleton :loading="loading" :animation="true" bgColor="#FFF"></u-skeleton>
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
				loading: true, // 是否显示骨架屏组件
				list: [],
				tabCur: 0,
				mainCur: 0,
				verticalNavTop: 0,
				load: true,
				dataList: [],
				shop: {},
				shopList: [],
				city: '西安',
				lat: '',
				lng: '',
				shopName: '',
				page: 1,
				limit: 10,
				show: true,
				totalPrice: 0,
				goodsNum: 0,
				goodsList: [],
				popupShow: false,
				skuShow: false,
				count: 1,
				userId: '',
				hintShow: false, //
				shouquan: 0,
				orderType: 1,

				value: 1,
				goodsDet: {},
				skuId: '',
				skuMessage: '',
				skuList: [],
				attrList: [],
				checkString: '',
				checkStateList: [],
				CheckattrValue: false,
				price: 0,
				echoSkuList: [],
				echoSku: [],
				isEchoSku: {},
				shopId: '',
				screenHeight: ''
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('payorder.title')
			})
			this.xcxSelect = this.$queue.getData('xcxSelect');
			let systemInfo = uni.getSystemInfoSync();
			console.log(systemInfo)
			this.screenHeight = systemInfo.screenHeight
			console.log(this.screenHeight)
			uni.showLoading({
				title: this.$t('loadingText.jzz') //'加载中...'
			})
			this.dizhi()
		},
		onShow() {
			this.shopId = uni.getStorageSync('shopId')

			uni.$on("orderType", (data) => {
				console.log("接收事件test成功,data=", data);
				this.orderType = data.orderType
			});
			uni.$emit('send')
			let that = this

			that.popupShow = false
			that.userId = uni.getStorageSync('userId')
			if (that.shop.shopId) {
				that.getOrderList()
			}

			if (this.shouquan == 1) {
				uni.showModal({
					title: '提示',
					content: '请授权您的位置权限：点击顶部"..."按钮，弹框后点击弹框底部设置按钮，选择位置信息，设置为："仅在使用小程序期间"，返回首页再次点击选择位置信息',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}


		},
		methods: {

			getEchoOrder() {
				let data = {
					shopId: this.shop.shopId,
					goodsId: this.goodsDet.goodsId
				}
				this.$Request.get("/app/order/echoOrder", data).then(res => {
					if (res.code == 0 && res.data.length) {
						this.echoSkuList = res.data
						this.echoSku = res.data[0].skuMessage.split(',')
					}
				});
			},
			// 弹窗展示商品详情
			selSku(item) {
				this.isEchoSku = {}
				this.value = 1
				this.attrList = []
				this.checkStateList = []
				this.checkString = ''
				this.$Request.get("/app/goods/selectGoodsById?goodsId=" + item.goodsId).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						this.goodsDet = res.data
						this.goodsDet.goodsLabel = this.goodsDet.goodsLabel.split(',')
						this.goodsDet.goodsPicture = this.goodsDet.goodsPicture.split(',')
						this.price = this.goodsDet.goodsMoney
						this.skuList = this.goodsDet.sku
						this.getEchoOrder()
						console.log(this.skuList, '******')
						if (!this.skuList.length) {
							let that = this
							setTimeout(function() {
								that.orderSel(2)
							}, 500)
							return
						} else {
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
								console.log(res.detail, '11111')
								if (res.detail) {
									if (typeof res.detail == 'string') {
										res.detail.split(',').forEach(d => {
											let data = {
												name: '',
												state: ''
											}
											data.name = d;
											detail.push(data);
										});
										res.detail = detail;
									}

								}

							})
							this.skuShow = true
						}


					}
				});
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
				for (var i = 0; i < this.skuList.length; i++) {
					let d = this.skuList[i];
					if (d.detailJson == this.checkString) {
						console.log(d.detailJson, this.checkString)
						// if (d.stock > 0) {
						this.skuId = d.id
						this.skuMessage = d.detailJson
						this.price = d.skuPrice
						this.CheckattrValue = true;
						// } else {
						// 	this.$queue.showToast('库存不足请选择其他规格')
						// }

						break
					} else {
						this.CheckattrValue = false;
					}
				}
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
						this.$queue.showToast('请选择正确的商品规格');
						return;
					}
				}
				let data = {
					goodsId: this.goodsDet.goodsId,
					skuMessage: this.skuMessage,
					skuId: this.skuId,
					num: this.value,
					orderType: this.orderType,
					shopId: this.shop.shopId,
				}
				this.$Request.post("/app/order/buyGoods", data).then(res => {
					if (res.code == 0) {
						this.skuShow = false
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
						this.$queue.showToast('请选择正确的商品规格');
						return;
					}
				}
				if (this.echoSku.length > 0) {
					this.echoSkuList.forEach(res => {
						if (res.skuMessage === this.skuMessage) {
							this.isEchoSku = res
						}
					})
					if (JSON.stringify(this.isEchoSku) != "{}") {
						let data = {
							orderGoodsId: this.isEchoSku.id,
							type: 1,
							num: this.value,
							shopId: this.shop.shopId,
						}

						this.$Request.get("/app/order/updateGoodsNum", data).then(res => {
							if (res.code == 0) {
								this.$queue.showToast('添加成功');
								this.skuShow = false
								this.getOrderList()
							} else {
								this.$queue.showToast(res.msg);
							}
						});
						return
					}
				}
				let data = {
					shopId: this.shop.shopId,
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
						this.skuMessage = ''
						this.$queue.showToast('添加成功');
						if (e == 1) {
							uni.navigateTo({
								url: '/pages/diancan/confirmOrder?shopId=' + this.shop.shopId
							})
						} else {
							this.skuShow = false
							this.getOrderList()
						}
					} else {
						this.$queue.showToast(res.msg);
					}
				})
			},
			// 切换外卖/自提
			tabSwidth(e) {
				this.orderType = e
				this.getOrderList()
			},
			dizhi() {
				let that = this
				uni.getLocation({
					type: "gcj02",
					success(res) {
						// console.log(res)
						that.shouquan = 0
						that.lat = res.latitude
						that.lng = res.longitude
						that.getShopList('1')
					},
					fail: function(ree) {
						that.shouquan = 1
					}
				})
			},
			// 展示购物车弹窗
			isPopupShow() {
				//判断 goodsList 是否是数组，是数组则走数组的length判断
				if (Array.isArray(this.goodsList)) {
					if (this.goodsList && this.goodsList.length > 0 && this.goodsList.orderGoodsList[0].length > 0) {
						this.popupShow = true
					} else {
						uni.showToast({
							title: '请先添加商品',
							icon: "none"
						})
					}
				} else { //不是数组走对象的判断 防止出现 ’0‘为undefined的报错导致逻辑停止
					if (this.goodsList && this.goodsList.orderGoodsList[0].length > 0) {
						this.popupShow = true
					} else {
						uni.showToast({
							title: '请先添加商品',
							icon: "none"
						})
					}
				}
				console.log(this.goodsList, '--------')

			},
			// 添加数量
			add(item, index) {
				// this.count++;
				console.log(item, index)
				this.goodsList.orderGoodsList[0][index].goodsNum++

				let data = {
					orderGoodsId: this.goodsList.orderGoodsList[0][index].id,
					type: 1,
					num: 1,
					shopId: this.shop.shopId
				}
				this.$Request.get("/app/order/updateGoodsNum", data).then(res => {
					if (res.code == 0) {
						this.getOrderList()
					} else {
						this.$queue.showToast(res.msg);
						this.goodsList.orderGoodsList[0][index].goodsNum--
					}
				});
			},
			// 减少数量
			noAdd(item, index) {
				console.log(item, index)
				this.goodsList.orderGoodsList[0][index].goodsNum--
				// this.count--;
				let data = {
					orderGoodsId: this.goodsList.orderGoodsList[0][index].id,
					type: 2,
					num: 1,
					shopId: this.shop.shopId
				}
				this.$Request.get("/app/order/updateGoodsNum", data).then(res => {
					if (res.code == 0) {
						this.getOrderList()
					}
				});
			},
			// 清空购物车
			empty() {
				let data = {
					shopId: this.shop.shopId,
				}
				this.$Request.post("/app/order/emptyShoppingTrolley", data).then(res => {
					if (res.code == 0) {
						this.popupShow = false
						this.goodsList = []
						this.echoSkuList = []
						this.echoSku = []
						this.getOrderList()
						this.getEchoOrder()
					}
				});
			},
			// 获取购物车商品列表
			getOrderList() {
				let data = {
					shopId: this.shop.shopId,
					page: 1,
					limit: 1000,
					status: 1,
					isCart: 1
				}
				this.$Request.get("/app/order/selectAllOrderList", data).then(res => {
					if (res.code == 0) {
						if (res.data && res.data.tbOrderList.length > 0) {
							this.goodsList = res.data.tbOrderList[0]
							if (this.goodsList.orderGoodsList) {
								this.goodsList.orderGoodsList[0].forEach(res => {
									res.goodsPicture = res.goodsPicture.split(',')
								})
								if (!this.goodsList.orderGoodsList[0].length) {
									this.popupShow = false
								}
							}
							this.totalPrice = res.data.money
							this.goodsNum = 0

							if (res.data.tbOrderList[0] && res.data.tbOrderList[0].orderGoodsList[0]) {
								res.data.tbOrderList[0].orderGoodsList[0].forEach(res => {
									this.goodsNum += res.goodsNum
								})
							}
							this.$forceUpdate()
						} else {
							this.goodsList = []
							this.totalPrice = 0
							this.goodsNum = 0
							this.popupShow = false
							this.$forceUpdate()
						}

					}
				});
			},
			// 搜索店铺
			search() {
				this.getShopList()
			},
			// 获取店铺列表
			getShopList(ret) {
				let data = {
					lng: this.lng,
					lat: this.lat,
					shopName: this.shopName,
					shopId: this.shopId,
					page: this.page,
					limit: this.limit
				}
				this.$Request.post("/app/goods/selectShortShop", data).then(res => {
					uni.hideLoading()
					if (res.code == 0) {
						if (res.data.list && res.data.list.length > 0) {
							res.data.list.forEach(res => {
								res.distancess = res.distancess > 1000 ? (Math.round(res.distancess) /
										1000)
									.toFixed(1) + 'km' : Math.round(res.distancess) + 'm'
							})
							// this.shop = res.data.list[0]
							if (ret == '1') {
								this.selectShop(res.data.list[0])
							}
							this.totalPage = res.data.totalPage
							if (this.page == 1) {
								this.shopList = res.data.list
							} else {
								this.shopList = [...this.shopList, ...res.data.list]
							}
						} else {
							this.shopList = []
						}

						this.loading = false;
					}
				});
			},
			// 选择城市
			goSelectCity() {
				uni.navigateTo({
					url: '/pages/diancan/citys/citys'
				});
			},
			// 选择店铺
			selectShop(e) {
				if (e.putawayFlag == 2) {
					this.shop = e
					this.show = false
					// this.putawayFlag = e.putawayFlag
					this.hintShow = true

					console.error(e.putawayFlag, '----------')
				} else {
					this.shop = e
					this.show = true
					if (this.shop.shopBanner) {
						this.shop.shopBanner = typeof(this.shop.shopBanner) == 'string' ? this.shop.shopBanner.split(',') :
							e.shopBanner
					}
					this.getData()
					this.getOrderList()
				}
			},
			// 再次选择店铺
			reselect() {
				if (!this.shopId) {
					this.show = false
				}

			},
			// 获取店铺商品列表
			getData() {
				let data = {
					shopId: this.shop.shopId
				}
				// console.log('**********')
				this.$Request.get("/app/goods/selectGoodsList", data).then(res => {
					if (res.code == 0 && res.data) {
						this.dataList = res.data
						for (var i = 0; i < this.dataList.length; i++) {
							this.dataList[i].id = i
						}
					}
				});
			},
			// 跳转商品详情
			goDet(id) {
				uni.navigateTo({
					url: '/pages/diancan/goodsDet?id=' + id + '&shopId=' + this.shop.shopId + '&orderType=' + this
						.orderType
				})
			},
			// 去结算
			goConfirm() {
				if (!this.userId) {
					uni.navigateTo({
						url: '/pages/login/login'
					})
					return
				}
				if (this.goodsList && this.goodsList.orderGoodsList[0].length > 0) {
					uni.navigateTo({
						url: '/pages/diancan/confirmOrder?shopId=' + this.shop.shopId + '&orderType=' + this
							.orderType
					})
				} else {
					uni.showToast({
						title: '请先添加商品',
						icon: "none"
					})
				}
			},
			TabSelect(e) {
				console.log(e.currentTarget.dataset.id)
				this.tabCur = e.currentTarget.dataset.id;
				this.mainCur = e.currentTarget.dataset.id;
				this.verticalNavTop = (e.currentTarget.dataset.id - 1) * 50
			},
			VerticalMain(e) {
				let that = this;
				let tabHeight = 0;
				if (this.load) {
					for (let i = 0; i < this.dataList.length; i++) {
						let view = uni.createSelectorQuery().select("#main-" + this.dataList[i].id);
						view.fields({
							size: true
						}, data => {
							// console.log(data,'',i)
							this.dataList[i].top = tabHeight;
							tabHeight = tabHeight + data.height;
							this.dataList[i].bottom = tabHeight;
						}).exec();
					}
					this.load = false
				}
				let scrollTop = e.detail.scrollTop;
				// console.log(scrollTop,'111')
				for (let i = 0; i < this.dataList.length; i++) {
					if (scrollTop > this.dataList[i].top && scrollTop < this.dataList[i].bottom) {
						this.verticalNavTop = (this.dataList[i].id - 1) * 50
						this.tabCur = this.dataList[i].id
						// console.log(scrollTop) //不能删除
						return false
					}
				}
			},
		},
		onReachBottom: function() {
			if (this.page < this.totalPage) {
				this.page = this.page + 1;
				this.getShopList();
			}

		},
	}
</script>

<style scoped lang="scss">
	page {
		background-color: #FFFFFF;
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

	.zh-content {
		width: 100%;
		height: 400rpx;
		margin: 20rpx 0;

		.zh-content-box {
			width: 686rpx;
			height: 100%;

			// background-color: red;
			.zh-content-box-item {
				margin-bottom: 20rpx;
				width: 150rpx;
				height: 100rpx;
				border: 1rpx solid #FF6D9D;
				color: #FF6D9D;
				border-radius: 16rpx;
			}
		}
	}

	.zh-title {
		margin-top: 30rpx;
		font-weight: bold;
		text-align: center;
	}

	.bg1 {
		background: #FFFFFF;
	}

	.sticky-tabs {
		z-index: 990;
		position: sticky;
		top: var(--window-top);
		// background-color: #fff;
	}

	/* // 使用mescroll-uni,则top为0 */
	.mescroll-uni,
	::v-deep .mescroll-uni {
		.sticky-tabs {
			top: 0;
		}
	}


	page {
		background: #fff !important;
	}

	.skuBtn {
		padding: 10rpx 30rpx;
		border-radius: 10rpx;
		text-align: center;
		margin: 10rpx 20rpx;
		font-size: 28rpx;
		color: #333333;
		border: 2px solid #F2F2F2;
	}

	.active {
		background: rgba(255, 109, 157, 0.2) !important;
		border: 2px solid #FF6D9D !important;
		opacity: 0.6;
	}

	.popup {
		/* height: 500rpx; */
		max-height: 500rpx;
		/* overflow-y: auto; */
	}

	.Switch {
		// width: 164rpx;
		height: 62rpx;
		color: #FFFFFF;
		background: #FF6D9D;
		border-radius: 30rpx;
		display: flex;
		align-items: center;
		padding: 2rpx;
	}

	.Switch>view {
		/* flex: 1; */
		width: 80rpx;
		text-align: center;
		line-height: 62rpx;
		height: 58rpx;
	}

	.selSwitch {

		color: #333333;
		background: #FFFFFF;
		border-radius: 30rpx;
	}

	.hintPopul {
		width: 100%;
		height: 100%;

		position: absolute;
		top: 0;
		background: rgba(0, 0, 0, .4);
	}

	.content {
		position: absolute;
		left: 0;
		right: 0;
		top: 0;
		bottom: 0;
		margin: auto;
		text-align: center;
		width: 500rpx;
		height: 400rpx;
		border-radius: 20rpx;
		background-color: #fff;
		padding-top: 120rpx;
	}

	.content image {
		position: absolute;
		top: -50rpx;
		left: 0;
		right: 0;
		margin: auto;
	}

	.hintText {
		font-size: 30rpx;

	}


	.VerticalNav.nav {
		width: 200upx;
		white-space: initial;
	}

	.VerticalNav.nav .cu-item {
		width: 100%;
		text-align: center;
		background-color: #f1f1f1;
		margin: 0;
		border: none;
		height: 50px;
		position: relative;
	}

	.VerticalNav.nav .cu-item.cur {
		background-color: #fff;
	}


	.VerticalBox {
		display: flex;
	}

	.VerticalMain {
		background-color: #f1f1f1;
		flex: 1;
	}

	.detail_describe_text2 {
		font-weight: 500;
		margin-top: 2%;
		color: #999999;
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
		border: 2rpx solid #FF6D9D;
	}

	.detail_account_bottom_ri {
		width: 47.5%;
		border-radius: 44rpx;
		text-align: center;
		line-height: 2.8;
		background-color: #FF6D9D;
		color: #ffffff;
	}

	.food_all {
		position: relative;
	}

	.text-through {
		text-decoration: line-through
	}

	/* 食物 */
	.food {
		width: 100%;
		overflow: hidden;
		position: absolute;
		top: 350rpx;
		background-color: #FFFFFF;
		border-top-left-radius: 18rpx;
		border-top-right-radius: 18rpx;
	}

	.food_address {
		margin: 3%;
		display: flex;
		justify-content: space-between;
		// align-items: center;
	}

	.food_address_le {
		width: 75%;
	}

	.food_address_le_top {
		display: flex;
		align-items: center;
	}

	.food_address_le_top_le {
		width: 40rpx;
		height: 37rpx;
	}

	.food_address_le_top_le image {
		width: 40rpx;
		height: 37rpx;
	}

	.food_address_le_top_ce {
		line-height: 1.6;
		margin-left: 2%;
		font-size: 32rpx;
		font-weight: 600;
		color: #333333;
	}

	.food_address_le_top_ce_ri {
		width: 14rpx;
		/* margin: 1% 0 0 2%; */
		height: 24rpx;

	}

	.food_address_le_top_ce_ri image {
		width: 14rpx;
		height: 24rpx;
	}

	.food_address_text {
		font-weight: 500;
		font-size: 24rpx;
		color: #999999;
	}

	.food_address_ri {
		width: 24%;
		height: 55rpx;
		display: flex;
		padding: 0.5% 1%;
		background-color: #FF6D9D;
		border-radius: 31rpx;
		color: #FFFFFF;
	}

	.food_address_ri_sty {
		flex: 1;
		text-align: center;
		padding-top: 4%;
		font-size: 24rpx;
	}

	.food_address_ri_sty_active {
		width: 100%;
		overflow: hidden;
		background-color: #FFFFFF;
		border-radius: 31rpx;
		color: #333333;
		text-align: center;
	}

	/* 结算 */
	.settlement {
		width: 94%;
		background-color: #FF6D9D;
		color: #ffffff;
		line-height: 3.4;
		border-radius: 49rpx;
		position: fixed;
		bottom: 120rpx;
		left: 3%;
		display: flex;
		justify-content: space-between;
	}

	.settlement1 {
		width: 94%;
		background-color: #FF6D9D;
		line-height: 2.8;
		border-radius: 49rpx;
		position: relative;
		/* bottom: 10rpx; */
		left: 0;
		right: 0;
		/* left: 3%; */
		display: flex;
		justify-content: space-between;
		margin: 20rpx auto;
	}

	.settlement_le {
		width: 45%;
		padding-left: 20%;
		color: #FFFFFF;
		font-size: 30rpx;
	}

	.settlement_le text {
		font-size: 22rpx;
	}

	.settlement_ri {
		width: 35%;
		background-color: #8BC6FF;
		font-family: PingFang SC;
		font-weight: 800;
		color: #FFFFFF;
		// font-size: 38upx;
		text-align: center;
		border-top-right-radius: 49rpx;
		border-bottom-right-radius: 49rpx;
	}

	.settlement_img {
		width: 120rpx;
		height: 110rpx;
		position: absolute;
		bottom: 30rpx;
		left: 2%;
	}

	.settlement_img image {
		width: 120rpx;
		height: 110rpx;
	}

	.settlement_hot {
		width: 38rpx;
		line-height: 1.7;
		text-align: center;
		border-radius: 50%;
		position: absolute;
		bottom: 65%;
		right: -15rpx;
		background-color: #8BC6FF;
		color: #FFFFFF;
		font-size: 20rpx;
		font-weight: bold;
	}


	.select_all {
		width: 100%;
		position: relative;
	}

	/* 餐厅 */
	.select {
		width: 100%;
		padding: 3% 3% 10%;
		background: #FFFFFF;
		/* position: absolute;top: 360rpx; */
		/* border-radius: 32rpx 32rpx 0px 0px; */
	}

	.select_line {
		width: 15%;
		height: 6rpx;
		margin: 0 auto 4%;
		background: #E6E6E6;
		border-radius: 4rpx;
	}

	.select_search {
		width: 100%;
		margin: 2% 0;
		display: flex;
		align-items: center;
	}

	.select_search_le {
		/* width: 10%; */
		font-size: 30rpx;
		line-height: 2.5;
		margin-right: 1%;
	}

	.select_search_ce {
		/* width: 5%; */
		/* margin-top: 1%; */
		margin-right: 20rpx;
		margin-left: 6rpx;
		/* margin: 0 20rpx; */
		width: 20rpx;
		height: 10rpx;
	}

	.select_search_ri {
		/* width: 84%; */
		height: 72rpx;
		flex: 1;
		display: flex;
	}

	.select_search_ri input {
		flex: 1;
		/* width: 100%; */
		height: 72rpx;
		background: #F5F5F5;
		color: #999999;
		border-radius: 36rpx;
		text-decoration: 42rpx;
		text-align: center;
	}

	/* 附近 */
	.nearby {
		width: 100%;
		position: relative;
	}

	.nearby_text {
		width: 18%;
		font-size: 30rpx;
		font-weight: 800;
		color: #333333;
		text-align: center;
		border-bottom: 16rpx solid #FF6D9D;
		/* line-height: 20rpx; */
		margin-top: 10rpx;
	}

	.nearby_address_active {
		border: 2rpx solid #FF6D9D;
		width: 100%;
		padding: 3%;
		margin-top: 3%;
		border-radius: 10upx;
		display: flex;
	}

	.nearby_address {
		width: 100%;
		padding: 3%;
		margin-top: 3%;
		border-radius: 10upx;
		display: flex;
	}

	.nearby_address_le {
		flex: 1;
	}

	.nearby_address_ri {
		line-height: 2.5;
		text-align: right;
		/* flex: 1; */
	}

	.nearby_text2 {

		/* line-height: 1.5; */
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.nearby_text2_ {
		font-size: 30rpx;
		font-weight: 800;
		color: #333333;
	}

	.nearby_text3 {
		font-size: 24rpx;
		font-weight: 500;
		color: #999999;
		margin-top: 2%;
	}

	/* 添加地址 */
	.goorder {
		width: 100%;
		padding: 2% 3%;
		position: fixed;
		bottom: 0;
		background-color: #FFFFFF;
		border-top: 1rpx solid #999999;
	}

	.goorder_but {
		width: 100%;
		line-height: 2.5;
		text-align: center;
		background: #FF6D9D;
		border-radius: 36rpx;
	}
</style>