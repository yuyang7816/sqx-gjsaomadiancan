<template>
	<view>
		<view class="content flex">
			<!-- 左侧类型选择 -->
			<view class="typeLeft">
				<scroll-view scroll-y="true" style="width: 100%;height: 100%;">
					<view class="typeLeft-item flex justify-center" @click="setCurrentLeft(item.classifyId,index)"
						:class="currentLeft==index?'active':''" v-for="(item,index) in listLift" :key="index">
						<view class="typeLeft-items">
							{{item.classifyName}}
						</view>
					</view>
				</scroll-view>
			</view>
			<!-- 右侧商品选择 -->
			<view class="typeRight">
				<scroll-view scroll-y="true" style="width: 100%;height: 100%;padding-bottom: 20rpx;">
					<view class="typeRight-item flex justify-center" v-for="(item,index) in listRight" :key="index">
						<view class="typeRight-item-box flex justify-between align-center">
							<view class="typeRight-item-box-l flex align-center justify-between">
								<image :src="item.goodsCover?item.goodsCover:'../../static/logo.png'" mode=""></image>
								<view class="typeRight-item-box-l-info flex flex-wrap">
									<view class="typeRight-item-box-l-info-t">
										{{item.goodsName}}
									</view>
									<view class="typeRight-item-box-l-info-b">
										{{$t('index.qian')}}{{item.goodsMoney}}/{{$t('meal.fen')}}
									</view>
								</view>
							</view>
							<view class="typeRight-item-box-r flex">
								
								<u-icon @click="selectAttr(item)" name="plus-circle" color="#FF6D9D" size="38"></u-icon>
							</view>
						</view>
					</view>
					<view class="empty flex justify-center flex-wrap" v-if="listRight.length == 0">
						<image src="../../static/images/empty.png" mode=""></image>
						<view class="empty-text">
							{{$t('empty.content')}}
						</view>
					</view>
				</scroll-view>
			</view>
		</view>

		<!-- 确认加餐 -->
		<view class="addMeal flex justify-center align-center">
			<view class="addMeal-box flex justify-center align-center">
				<view class="settlement" @click="openChart()">
					<view class="settlement_img">
						<image src="../../static/images/index/diancan1.png" mode=""></image>
						<view class="settlement_hot">{{goodsNum}}</view>
					</view>
					<view class="settlement_le flex align-center">
						<text>{{$t('index.qian')}}</text>{{totalPrice}}
					</view>
					<view class="settlement_ri flex justify-center align-center" @click.stop="openChart()">{{$t('index.sure')}}</view>
				</view>
			</view>
		</view>



		<!-- 选择规格弹窗 -->
		<u-popup v-model="skuShow" mode="center" :closeable='true' border-radius="20">
			<view style="width: 700rpx;">
				<view class="text-center text-lg text-bold padding-tb">{{goodsName}}</view>
				<view class="margin-top-sm padding-lr" v-for="(item,index) in attrList" :key="index">
					<view class="text-bold text-black">{{item.value}}</view>
					<view class="flex margin-tb-sm flex-wrap">
						<view v-for="(ite, ind) in item.detail" :key="ind" @click="skuSel(ite,index,ind)"
							class="margin-bottom-sm">
							<view class="skuBtn" :class="item.goodsId == index && item.attrId == ind?'activesku': ''">
								{{ite.name}}
							</view>
						</view>
					</view>
				</view>
				<view class="flex justify-between padding-lr padding-top">
					<view>
						<view class="text-xl text-bold text-black"><text class="text-sm">{{$t('index.qian')}}</text>{{price}}
						</view>
						<view class="detail_describe_text2">{{checkString}}</view>
					</view>
					<u-number-box v-model="value" :min="1" @change="valChange"></u-number-box>
				</view>
				<view class="detail_account_bottom padding">
					<view class="detail_account_bottom_ri" style="width: 100%;" @click="orderSel(2)">{{$t('index.sure')}}</view>
				</view>
			</view>
		</u-popup>
		<!-- 购物车弹窗 -->
		<u-popup v-model="popupShow" mode="bottom" border-radius="20" z-index="999">
			<view class="padding">
				<view class="flex justify-between align-center margin-bottom-sm">
					<view class="text-bold text-black text-df">{{$t('payorder.yxcp')}}</view>
					<view class="flex align-center">
						<image src="../../static/images/diancan/delete.png" style="width: 28rpx;height: 31rpx;" mode="">
						</image>
						<text class="margin-left-xs" @click="empty">{{$t('payorder.qk')}}</text>
					</view>
				</view>
				<scroll-view scroll-y='true' style="height: 700rpx;" class="popup" v-if="selectGoods && selectGoods.length>0">
					<view v-for="(item,index) in selectGoods" :key='index'>
						<view class="flex align-center margin-tb-sm" style="position: relative;">
							<image :src="item.goodsPicture.split(',')[0]"
								style="width: 96rpx;height: 96rpx;border-radius: 10rpx;">
							</image>
							<view class="margin-left-sm">
								<view>{{item.goodsName}}</view>
								<view v-if="item.skuMessage">{{item.skuMessage}}</view>
							</view>
							<view @click="upIsShow(item)" class="beizhu" style="position: absolute;right: 0;top: 0;">
								{{$t('payorder.bz')}}
							</view>
						</view>
						<view class="flex justify-between align-center">
							<view class="text-bold text-sm">{{$t('index.qian')}}<text class="text-lg">{{item.goodsPrice}}</text>
							</view>
							<view class="flex align-center justify-between" style="width: 155rpx;">
								<view @click.stop="noAdd(index)">
									<image src="../../static/images/diancan/jian.png"
										style="width: 54rpx;height: 54rpx;"></image>
								</view>
								<view class="text-center margin-lr-xs margin-bottom-xs">{{item.goodsNum}}</view>
								<view @click="add(index)">
									<image src="../../static/images/diancan/add.png"
										style="width: 50rpx;height: 50rpx;"></image>
								</view>
							</view>
						</view>
						<view v-show="item.isShow" class="margin-bottom-sm" style="margin-top: 20rpx;">
							<view class="text-bold text-black text-df">{{$t('payorder.bz')}}</view>
							<view class="flex align-center" style="width: 100%;height: 200rpx;margin-top: 20rpx;">
								<u-input v-model="item.goodRemarks" type="textarea" :border="false" :placeholder="$t('payorder.plabz')" style="border-radius: 12rpx;width: 100%;background-color: #f1f1f1;height: 100%;padding: 10rpx 20rpx;"/>
							</view>
						</view>
					</view>
				</scroll-view>
			</view>
			<view class="settlement1 margin-top-lg" style="height: 80rpx;">
				<view class="settlement_img" style="bottom: 10rpx;">
					<image src="../../static/images/index/diancan1.png" mode=""></image>
					<view class="settlement_hot">{{goodsNum}}</view>
				</view>
				<view class="settlement_le flex align-center">
					<text>{{$t('index.qian')}}</text>{{totalPrice}}
				</view>
				<view class="settlement_ri flex justify-center align-center" @click.stop="goConfirm()">{{$t('index.sure')}}</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				goodsList: [],
				popupShow: false,
				goodsName: '',
				attrList: [],
				skuShow: false,
				currentLeft: 0,
				listLift: [],
				listRight: [],
				shopId: '',
				skuId: '',
				skuList: [],
				checkStateList: [],
				CheckattrValue: false,
				skuMessage: '',
				price: 0,
				checkString: '',
				value: 1,
				selectGoods: [],
				goodInfo: {},
				// totalPrice: 0, //商品总价
				goodsNum: 0, //商品数量
				seatId: '', //桌位id
			};
		},
		computed:{
			//用于计算商品数量变化后的商品总价
			totalPrice(){
				this.goodsNum = this.selectGoods.length
				//总价格(每当购物车商品数据发生变化的时候都重新计算其价格)
				let price = 0
				this.selectGoods.map(item => {
					price += parseFloat(item.goodsPrice) * parseInt(item.goodsNum)
				})
				return price.toFixed(2)
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('meal.diancan')
			})
			this.seatId = option.seatId
			this.shopId = this.$queue.getData("updataShopId")
			this.getClassifyList()
		},
		methods: {
			upIsShow(item){
				item.isShow = !item.isShow
				this.$forceUpdate()
			},
			openChart(){
				if(this.selectGoods.length==0){
					uni.showToast({
						title:this.$t('meal.plaxzsp'),//'还未选择商品，请先选择商品'
						icon:'none'
					})
					return
				}
				this.popupShow = true
			},
			//增加商品数量
			add(index){
				let arr = JSON.parse(JSON.stringify(this.selectGoods))
				arr[index].goodsNum += 1
				this.selectGoods = arr
				this.$forceUpdate()
				console.log(this.selectGoods)
			},
			//减少商品数量
			noAdd(index){
				let arr = JSON.parse(JSON.stringify(this.selectGoods))
				arr[index].goodsNum -= 1
				if(arr[index].goodsNum==0){
					arr.splice(index,1)
				}
				this.selectGoods = arr
				this.$forceUpdate()
				
			},
			//确认选择商品
			goConfirm() {
				if (this.selectGoods.length == 0) {
					uni.showToast({
						title:this.$t('meal.plaxzsps'),// '请选择商品'
						icon: 'none'
					})
					return
				}
				let newData = []
				//使用Object.assign过滤出不需要的属性，留下需要的属性
				this.selectGoods.map(i => {
					newData.push(Object.assign({}, {
						goodsId: i.goodsId,
						skuId: i.skuId,
						skuMessage: i.skuMessage,
						num: i.goodsNum,
						goodRemarks:i.goodRemarks
					}))
				})
				let data = {
					shopId: this.shopId,
					seatId: this.seatId,
					orderType: 3,
					ordersGoodsVoList: newData
					
				}
				let that = this
				uni.showModal({
					title: that.$t('index.wxts'),
					content: that.$t('meal.platjm'), //确认提交吗？
					confirmText:that.$t('index.sure'),
					cancelText:that.$t('index.qx'),
					complete(ret){
						if(ret.confirm){
							that.$Request.postJsonA('/admin/order/insertOrder',data).then(res=>{
								if(res.code == 0){
									uni.showToast({
										title:that.$t('meal.ytj')//'已添加'
									})
									setTimeout(()=>{
										uni.navigateBack()
									},1000)
								}else{
									uni.showToast({
										title:res.msg,
										icon:'none'
									})
								}
							})
						}
					},
				})
				

			},
			//清空购物车
			empty() {
				this.selectGoods = []
				uni.showToast({
					title: this.$t('order.yqk')//'已清空'
				})
			},
			//选择商品至购物车
			orderSel() {
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
				//选择完规格确认的时候把选择的规格放进存储的商品信息对象中
				this.goodInfo.skuMessage = this.skuMessage
				this.goodInfo.goodsPrice = this.price
				this.goodInfo.goodsNum = this.value
				this.goodInfo.skuId = this.skuId
				this.goodInfo.isShow = false
				this.goodInfo.goodRemarks = ''
				//购物车中存在商品
				if(this.selectGoods.length>0){
					let arr = JSON.parse(JSON.stringify(this.selectGoods))
					//查找选择的skuid是否已经存在
					let result = arr.some(ele => ele.skuId === this.skuId) //true
					if(result){
						//如果skuid已经存在则修改其中的数量
						arr.map(item=>{
							if(item.skuId === this.skuId){
								item.goodsNum += this.value
							}
						})
						this.selectGoods = arr
					}else{
						//如果不存在则直接加入购物车
						this.selectGoods.push(this.goodInfo)
					}
				}else{
					//购物车为空则直接添加
					this.selectGoods.push(this.goodInfo)
				}
				this.skuShow = false
				this.skuMessage = ''
				this.price = 0
				this.skuId = ''
				this.value = 1
			},
			//监听选择的商品数量
			valChange(e) {
				this.value = e.value
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
			//选择商品
			selectAttr(info) {
				let obj = JSON.parse(JSON.stringify(info))
				this.goodInfo = obj //打开规格弹窗的时候本地存储一下商品信息
				this.skuList = obj.sku
				this.checkStateList = []
				this.attrList = []
				this.checkString = ''
				this.price = obj.goodsMoney
				this.goodsName = obj.goodsName
				if (obj.attr.length) {
					this.attrList = obj.attr[0] ? obj.attr[0].attrValue : []
				} else {
					this.skuId = obj.sku[0].id
				}
				console.log(this.attrList)
				this.attrList.forEach(res => {
					let data = {
						name: ''
					}
					this.checkStateList.push(data);
					let detail = [];
					if (res.detail) {
						//判断是是字符串 是字符串则处理为数组/否则不做处理
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
			},
			//切换分类重新获取商品数据
			setCurrentLeft(classifyId, index) {
				this.currentLeft = index
				this.getGoodsList(classifyId)
			},
			checkboxChange(item) {
				this.$forceUpdate()
				// item.select = !item.select
			},
			//根据shopId查询分类
			getClassifyList() {
				let data = {
					shopId: this.shopId
				}
				this.$Request.getA('/admin/goods/selectAllClassify', data).then(res => {
					if (res.code == 0) {
						this.listLift = res.data
						if (this.listLift.length > 0) {
							this.getGoodsList(this.listLift[0].classifyId)
						} else {
							this.listRight = []
						}

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
			//根据店铺id分类id查询商品
			getGoodsList(classifyId) {
				let data = {
					status: 0,
					classifyId: classifyId,
					shopId: this.shopId
				}
				this.$Request.getA('/admin/goodsShop/selectGoodsByShopId', data).then(res => {
					if (res.code == 0) {
						if (res.data.list.length > 0) {
							this.listRight = res.data.list
							this.listRight.map(item => {
								item.select = false
							})
						} else {
							this.listRight = []
						}

					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				})
			},
		}
	}
</script>

<style lang="scss">
	page {
		background: #EDF1F7;
	}
	.beizhu{
		padding: 10rpx 20rpx;
		background-color: #FF6D9D;
		color: #FFFFFF;
		border-radius: 12rpx;
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
		border-top-right-radius: 49rpx;
		border-bottom-right-radius: 49rpx;
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

	.settlement_img {
		width: 120rpx;
		height: 110rpx;
		position: absolute;
		bottom: 30rpx;
		// left: 2%;
	}

	.settlement_img image {
		width: 120rpx;
		height: 110rpx;
	}

	/* 结算 */
	.settlement {
		width: 100%;
		background-color: #FF6D9D;
		color: #ffffff;
		height: 100%;
		border-radius: 49rpx;
		display: flex;
		justify-content: space-between;
	}

	::v-deep .u-checkbox {
		display: inline-block !important;
	}

	.activesku {
		background: rgba(255, 109, 157, 0.2) !important;
		border: 2px solid #FF6D9D !important;
		opacity: 0.6;
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

	.detail_describe_text2 {
		font-weight: 500;
		margin-top: 2%;
		color: #999999;
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

	.empty {
		width: 100%;
		padding-top: 300rpx;

		image {
			width: 387rpx;
			height: 341rpx;
		}

		.empty-text {
			width: 100%;
			text-align: center;
			margin-top: 20rpx;
		}
	}

	.active {
		background: #EDF1F7 !important;
	}

	.content {
		width: 100%;
		/* #ifdef MP-WEIXIN */
		height: calc(100vh - 120rpx);
		/* #endif */
		/* #ifndef MP-WEIXIN */
		height: calc(100vh - 200rpx);
		/* #endif */
	}

	.typeLeft {
		width: 30%;
		height: 100%;
		background-color: #ffffff;

		.typeLeft-item {
			width: 100%;
			height: 80rpx;
			background-color: #ffffff;

			.typeLeft-items {
				width: 70%;
				height: 100%;
				line-height: 80rpx;
				overflow: hidden;
				white-space: nowrap;
				text-overflow: ellipsis;
				-o-text-overflow: ellipsis;
			}
		}
	}

	.typeRight {
		width: 70%;
		height: 100%;
		background: #EDF1F7;

		.typeRight-item {
			width: 100%;
			height: 180rpx;
			margin-top: 20rpx;
		}

		.typeRight-item-box {
			width: 90%;
			height: 100%;
			background-color: #ffffff;
			border-radius: 8rpx;

			.typeRight-item-box-l {
				width: 86%;
				height: 100%;

				image {
					width: 120rpx;
					height: 120rpx;
					border-radius: 8rpx;
					margin-left: 20rpx;
				}

				.typeRight-item-box-l-info {
					width: calc(100% - 160rpx);
					height: 120rpx;
					align-content: space-around;

					.typeRight-item-box-l-info-t {
						width: 100%;
						font-size: 32rpx;
						overflow: hidden;
						white-space: nowrap;
						text-overflow: ellipsis;
						-o-text-overflow: ellipsis;
					}

					.typeRight-item-box-l-info-b {
						width: 100%;
						color: #ff4483;
					}
				}
			}

			.typeRight-item-box-r {
				height: 100%;
				align-items: flex-end;
				padding-bottom: 30rpx;
				padding-right: 20rpx;
			}
		}

		// background-color: aqua;
	}

	.addMeal {
		width: 100%;
		height: 120rpx;
		position: fixed;
		bottom: 0;
		background-color: #ffffff;

		.addMeal-box {
			width: 686rpx;
			height: 80rpx;
			border-radius: 8rpx;
			// background-color: #ff9cbd;
			color: #ffffff;
		}
	}
</style>
