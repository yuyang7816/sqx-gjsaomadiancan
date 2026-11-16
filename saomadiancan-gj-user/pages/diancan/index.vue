<template>
	<view>
		<view class="food_all u-skeleton" v-if="show">
			<!-- banner -->
			<swiper class="swiperbox u-skeleton-rect" :autoplay="true" interval="2000" duration="500" :circular="true">
				<swiper-item v-for="(item,index) in shop.shopBanner" :key='index'>
					<image :src="item" mode="scaleToFill" style="width: 100%;"></image>
				</swiper-item>
			</swiper>

			<!-- 食品 -->
			<view class="food u-skeleton-rect" :style="{height:(screenHeight - 420)+'px'}">
				<!-- 店名地址 -->
				<view class="food_address">
					<view class="food_address_le" @click="reselect()">
						<view class="food_address_le_top_le">
							<image src="../../static/images/diancan/dianlogo.png" mode="aspectFit"></image>
						</view>
						<view class="food_address_le_top_ce">{{shop.shopName}}</view>
						<view class="food_address_le_top_ce_ri">
							<image src="../../static/images/diancan/right2.png" mode="widthFix"></image>
						</view>
					</view>
					<view class=" Switch">
						<view @click="tabSwidth(1)" class="Switchbox" :class="orderType==1?'selSwitch':''">
							{{$t('payorder.ziqu')}}
						</view>
						<view @click="tabSwidth(2)" class="Switchbox" :class="orderType==2?'selSwitch':''">
							{{$t('index.waimai')}}
						</view>
					</view>
				</view>
				<view class="flex justify-between margin-lr padding-bottom-sm">
					<view class="food_address_text">{{$t('payorder.juli')}}{{shop.distancess}}</view>
				</view>


				<view class="VerticalBox" :style="{height:(screenHeight -360)+'px'}"	v-if="dataList && dataList.length>0">
					<scroll-view class="VerticalNav  " scroll-y scroll-with-animation :scroll-top="verticalNavTop">
						<view class="cu-item " :class="index==tabCur?'cur':'text-gray'" v-for="(item,index) in dataList"
							:key="index" @tap="TabSelect" :data-id="index">
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
											{{$t('index.qian')}} <text class="text-lg">{{ite.goodsMoney}}</text>
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
					<text>{{$t('index.qian')}}</text>{{totalPrice}}
				</view>
				<view class="settlement_ri" @click.stop="goConfirm()">{{$t('payorder.qjs')}}</view>
			</view>

			<!-- 购物车弹窗 -->
			<u-popup v-model="popupShow" mode="bottom" border-radius="20">
				<view class="padding bg-white">
					<view class="flex justify-between align-center margin-bottom-sm">
						<view class="text-bold text-black text-df">{{$t('payorder.yxcp')}}</view>
						<view class="flex align-center">
							<image src="../../static/images/diancan/delete.png" style="width: 28rpx;height: 31rpx;"
								mode=""></image>
							<text class="margin-left-xs" @click="empty">{{$t('payorder.qkgwc')}}</text>
						</view>
					</view>
					<scroll-view scroll-y='true' class="popupshop" v-if="goodsList && goodsList.orderGoodsList">
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
								<view class="text-bold text-sm">{{$t('index.qian')}}<text
										class="text-lg">{{item.goodsPrice}}</text>
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
						<text>{{$t('index.qian')}}</text>{{totalPrice}}
					</view>
					<view class="settlement_ri" @click.stop="goConfirm()">{{$t('payorder.qjs')}}</view>
				</view>
			</u-popup>
			<!-- 选择规格弹窗 -->
			<u-popup v-model="skuShow" mode="center" :closeable='true' border-radius="20">
				<view class="skubox ">
					<view class="skubox_title">{{goodsDet.goodsName}}</view>
					<view class="margin-top-sm padding-lr" v-for="(item,index) in attrList" :key="index">
						<view class="text-bold text-black">{{item.value}}</view>
						<view class="flex margin-tb-sm flex-wrap">
							<view v-for="(ite, ind) in item.detail" :key="ind" @click="skuSel(ite,index,ind)"
								class="margin-bottom-sm">
								<view class="skulabel"
									:class="item.goodsId == index && item.attrId == ind?'skulabelactive': ''">
									{{ite.name}}
								</view>
							</view>
						</view>
					</view>
					<view class="flex justify-between padding-lr padding-top">
						<view>
							<view class="text-xl text-bold text-black"><text
									class="text-sm">{{$t('index.qian')}}</text>{{price}}
							</view>
							<view class="detail_describe_text2">{{checkString}}</view>
						</view>
						<u-number-box v-model="value" :min="1" @change="valChange"></u-number-box>
					</view>
					<view class="detail_account_bottom padding">
						<view class="detail_account_bottom_le" @click="payment()">{{$t('payorder.ljgm')}}</view>
						<view class="detail_account_bottom_ri" @click="orderSel(2)">{{$t('payorder.addgwc')}}</view>
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
						<input type="text" :placeholder="$t('payorder.pladp')" v-model="shopName" @confirm="search" placeholder-style="font-size:26rpx"/>
						<view>
							<button @click="search"
								class="cu-btn margin-left bg-yellow light shadow-blur round">{{$t('payorder.ss')}}</button>
						</view>
					</view>
				</view>
				<!-- 附近 -->
				<view class="nearby">
					<view class="nearby_text">{{$t('payorder.shopfj')}}</view>
					<view class="nearby_address_active" v-for="(item,index) in shopList" :key='index'
						@click="selectShop(item)">
						<view class="nearby_text2">
							<text class="nearby_text2_">{{item.shopName}}</text>
							<view class="flex align-center">
								<image src="../../static/images/diancan/location.png" mode="widthFix"
									style="width: 24rpx;height: 28rpx;"></image>
								<text class="margin-left-xs text-sm">{{$t('payorder.juli')}}{{item.distancess}}</text>
							</view>
						</view>
						<view class="nearby_text3">{{item.detailedAddress}}</view>
						<view class="nearby_text3">{{$t('payorder.data')}}：{{item.openTime}} - {{item.closeTime}}</view>
					</view>
				</view>
			</view>

			<!-- 店铺打烊弹框 -->
			<u-popup v-model="hintShow" mode="center" border-radius="24" @close="hintShow = false">
				<view class="hintPopul">
					<view class="content">
						<image src="../../static/images/diancan/shop.png" mode="" style="width: 200rpx;height: 180rpx;">
						</image>
						<view class="text-xl text-bold">{{$t('payorder.shopdy')}}</view>
						<view class="hintText margin-top-sm text-gray">
							{{$t('payorder.pladpdy')}},{{$t('payorder.data')}}
						</view>
						<view class="margin-top-xs text-gray">{{shop.openTime}}-{{shop.closeTime}}</view>
						<view class="skuBtn margin-top" @click="hintShow=false">{{$t('payorder.ok')}}</view>
					</view>
				</view>
			</u-popup>

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
				hintShow: false, //店铺打样弹框
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
						// this.$queue.showToast('请选择正确的商品规格');
						uni.showToast({
							title:this.$t('payorder.plasku'),
							icon:'none'
						})
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
								// this.$queue.showToast('添加成功');
								uni.showToast({
									title:this.$t('index.addok'),
									icon:'none'
								})
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
						// this.$queue.showToast('添加成功');
						uni.showToast({
							title:this.$t('index.addok'),
							icon:'none'
						})
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
					type: "wgs84",
					success(res) {
						console.log(res)
						that.shouquan = 0
						that.lat = res.latitude
						that.lng = res.longitude
						that.getShopList('1')
					},
					fail: function(ree) {
						console.log(ree)
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
							title:this.$t('payorder.plaadd'),// '请先添加商品'
							icon: "none"
						})
					}
				} else { //不是数组走对象的判断 防止出现 ’0‘为undefined的报错导致逻辑停止
					if (this.goodsList && this.goodsList.orderGoodsList[0].length > 0) {
						this.popupShow = true
					} else {
						uni.showToast({
							title:this.$t('payorder.plaadd'),// '请先添加商品'
							icon: "none"
						})
					}
				}

			},
			// 添加数量
			add(item, index) {
				// this.count++;
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
				this.goodsList.orderGoodsList[0][index].goodsNum--
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
					this.hintShow = true
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
						title:this.$t('payorder.plaadd'),// '请先添加商品'
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
							
							this.dataList[i].top = tabHeight;
							tabHeight = tabHeight + data.height;
							this.dataList[i].bottom = tabHeight;
						}).exec();
					}
					this.load = false
				}
				let scrollTop = e.detail.scrollTop;
				
				for (let i = 0; i < this.dataList.length; i++) {
					if (scrollTop > this.dataList[i].top && scrollTop < this.dataList[i].bottom) {
						this.verticalNavTop = (this.dataList[i].id - 1) * 50
						this.tabCur = this.dataList[i].id
						
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
		background: #F2F2F2;
	}

	.food_all {
		width: 100%;

		.swiperbox {
			width: 100%;
			height: 380rpx;
		}
	}

	/* 食物 */
	.food {
		width: 100%;
		background: #FFFFFF;
		padding: 20rpx 0rpx;

		// 店铺地址
		.food_address {

			margin: 5rpx 30rpx;
			display: flex;
			justify-content: space-between;

			.food_address_le {
				flex: 1;
				display: flex;
				align-items: center;

				.food_address_le_top_le {
					width: 40rpx;
					height: 37rpx;

					image {
						width: 40rpx;
						height: 37rpx;
					}
				}

				.food_address_le_top_ce {
					margin-left: 10rpx;
					font-size: 32rpx;
					font-weight: 600;
					color: #333333;
				}

				.food_address_le_top_ce_ri {
					width: 14rpx;
					height: 24rpx;
					margin-left: 10rpx;

					image {
						width: 14rpx;
						height: 24rpx;
					}
				}

			}

			.Switch {
				height: 62rpx;
				color: #FFFFFF;
				background: #FF6D9D;
				border-radius: 30rpx;
				display: flex;
				align-items: center;
				padding: 2rpx;
			}

			.Switchbox {
				padding: 0 15rpx;
				// width: 80rpx;
				text-align: center;
				line-height: 62rpx;
				height: 58rpx;
			}

			.selSwitch {
				color: #333333;
				background: #FFFFFF;
				border-radius: 30rpx;
			}
		}

		.food_address_text {
			font-weight: 500;
			font-size: 24rpx;
			color: #999999;
		}
	}

	// 点餐框
	.VerticalBox {
		display: flex;


		// 左边
		.VerticalNav {
			width: 200upx;
			white-space: initial;
			background: #F2F2F2;
			padding-bottom: 200rpx;

			.cu-item {
				width: 100%;
				background: #F2F2F2;
				height: 50px;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 28upx;
			}

			.cur {
				background-color: #fff;
			}
		}

		// 右边
		.VerticalMain {
			background: #F2F2F2;
			flex: 1;
			padding-bottom: 180rpx;
		}

	}


	/* 结算 */
	.settlement {
		// width: 95%;
		background-color: #FF6D9D;
		color: #ffffff;
		line-height: 3.4;
		border-radius: 49rpx;
		position: fixed;
		/* #ifdef H5 */
		bottom: 120rpx;
		/* #endif */
		/* #ifdef APP */
		bottom:30rpx;
		/* #endif */
		left: 30rpx;
		right: 30rpx;

		display: flex;
		justify-content: space-between;

		.settlement_img {
			width: 120rpx;
			height: 110rpx;
			position: absolute;
			bottom: 30rpx;
			left: 2%;

			image {
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
		}

		.settlement_le {
			width: 45%;
			padding-left: 20%;
			color: #FFFFFF;
			font-size: 30rpx;

			text {
				font-size: 22rpx;
			}
		}

		.settlement_ri {
			width: 35%;
			background-color: #8BC6FF;
			font-family: PingFang SC;
			font-weight: 800;
			color: #FFFFFF;
			text-align: center;
			border-radius: 0 49rpx 49rpx 0;
			display: flex;
			align-items: center;
			justify-content: center;
		}

	}

	// 选择规格弹框
	.skubox {
		width: 700rpx;
		background: #FFFFFF;

		.skubox_title {
			text-align: center;
			font-size: 32rpx;
			font-weight: bold;
			padding: 30rpx 0;
		}


		.skulabel {
			padding: 15rpx 30rpx;
			border-radius: 10rpx;
			text-align: center;
			margin-right: 30rpx;
			font-size: 28rpx;
			color: #333333;
			border: 2px solid #F2F2F2;
		}

		.skulabelactive {
			background: rgba(255, 109, 157, 0.2) !important;
			border: 2px solid #FF6D9D !important;
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

			.detail_account_bottom_le {
				flex: 1;
				border-radius: 44rpx;
				text-align: center;
				border: 2rpx solid #FF6D9D;
				padding: 20rpx 0;
				margin-right: 20rpx;
			}

			.detail_account_bottom_ri {
				flex: 1;
				border-radius: 44rpx;
				text-align: center;
				background-color: #FF6D9D;
				color: #ffffff;
				padding: 20rpx 0;
				margin-left: 20rpx;
			}

		}


	}

	// 店铺打烊弹框
	::v-deep .u-mode-center-box {
		background: transparent !important;
	}

	.hintPopul {
		width: 500rpx;
		height: 600rpx;
		position: relative;
		// background:transparent;

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

			image {
				position: absolute;
				top: -50rpx;
				left: 0;
				right: 0;
				margin: auto;
			}

			.hintText {
				font-size: 30rpx;

			}
		}

		.skuBtn {
			padding: 15rpx 30rpx;
			border-radius: 10rpx;
			text-align: center;
			margin: 30rpx 20rpx;
			font-size: 28rpx;
			color: #333333;
			border: 2px solid #F2F2F2;
		}


	}

	// 购物车弹框
	.popupshop {
		max-height: 500rpx;
	}

	.settlement1 {
		width: 94%;
		background-color: #FF6D9D;
		line-height: 2.8;
		border-radius: 49rpx;
		position: relative;
		left: 0;
		right: 0;
		display: flex;
		justify-content: space-between;
		margin: 20rpx auto;

		.settlement_img {
			width: 120rpx;
			height: 110rpx;
			position: absolute;
			bottom: 30rpx;
			left: 2%;

			image {
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
		}

		.settlement_le {
			width: 45%;
			padding-left: 20%;
			color: #FFFFFF;
			font-size: 30rpx;

			text {
				font-size: 22rpx;
			}
		}

		.settlement_ri {
			width: 35%;
			background-color: #8BC6FF;
			font-family: PingFang SC;
			font-weight: 800;
			color: #FFFFFF;
			text-align: center;
			border-radius: 0 49rpx 49rpx 0;
			display: flex;
			align-items: center;
			justify-content: center;
		}

	}


	// 店铺页面
	.select_all {
		width: 100%;

		/* 餐厅 */
		.select {
			width: 100%;
			padding: 20rpx 30rpx;
			background: #FFFFFF;

		}

		// 搜索
		.select_search {
			width: 100%;
			display: flex;
			align-items: center;

			.select_search_ri {
				height: 72rpx;
				flex: 1;
				display: flex;

				input {
					flex: 1;
					height: 72rpx;
					background: #F5F5F5;
					color: #999999;
					border-radius: 36rpx;
					text-decoration: 42rpx;
					text-align: center;
				}
			}
		}

		/* 附近 */
		.nearby {
			width: 100%;
			position: relative;

			.nearby_text {
				display: inline-flex;
				font-size: 30rpx;
				font-weight: 800;
				color: #333333;
				text-align: center;
				border-bottom: 16rpx solid #FF6D9D;
				margin-top: 10rpx;
			}

			.nearby_address_active {
				border: 2rpx solid #FF6D9D;
				width: 100%;
				padding: 3%;
				margin-top: 3%;
				border-radius: 10upx;

				.nearby_text2 {
					display: flex;
					justify-content: space-between;
					align-items: center;

					.nearby_text2_ {
						font-size: 30rpx;
						font-weight: 800;
						color: #333333;
					}
				}

				.nearby_text3 {
					font-size: 24rpx;
					font-weight: 500;
					color: #999999;
					margin-top: 2%;
				}
			}
		}
	}
</style>