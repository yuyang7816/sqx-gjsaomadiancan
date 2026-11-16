<template>
	<view class="padding">
		<view class="padding bg radius">
			<view class="">
				<view class="text-df text-bold">{{$t('sku.shopName')}}</view>
				<view>
					<u-input v-model="shopName" :placeholder="$t('sku.plashopName')" />
				</view>
			</view>
		</view>

		<view class="bg margin-tb padding-sm radius" v-for="(item,index) in shopList" :key='index'>
			<view class="">
				<view style="display: flex;width: 100%;">
					<view style="width: 70%;" class="text-df text-bold">{{$t('sy.sku')}}</view>
					<view style="width: 30%;text-align: right;" class="text-df text-bold" v-if="index != 0" @tap="delteItem(index)">{{$t('index.dete')}}</view>
				</view>
				<view>
					<u-input v-model="item.value" :placeholder="$t('shop.pla')" />
				</view>
			</view>
			<view class="">
				<view class="text-df text-bold">{{$t('sku.ggz')}}</view>
				<!-- <view>
					<u-input v-model="detailName" placeholder="请填写" />
				</view> -->
			</view>
			<view class="flex align-center flex-wrap margin-bottom-sm">
				<view v-for="(item1,index1) in item.detail" :key="index1" class="btn flex align-center margin-top"
					:class="listIndex1 == index1&&listIndex==index?'active':''" @click="bindqie(index1,item1,index)">
					<view>{{item1}}</view>
					<view class="margin-left-sm" @tap.stop="bindupdata(index1,item1,index)">x</view>
				</view>
				<view class="btns margin-top" @click="addtype(1,item1,index)">+{{$t('Commodity.addl')}}</view>
			</view>

		</view>


		<u-button class="margin-top addguiges" :custom-style="customStyle" shape="square" :hair-line="false"
			@click="addguige()">{{$t('sku.addsku')}}
		</u-button>

		<view class="margin-top">
			<u-button @click="submit" class=" addguige" :custom-style="customStyle" shape="square" :hair-line="false">{{$t('index.sure')}}
			</u-button>
		</view>

		<!-- 添加规格弹框 -->
		<u-popup v-model="show" mode="center" border-radius="14" width="500rpx" height="300rpx">
			<view>
				<view class="padding">
					<view>{{$t('sku.plaksutype')}}</view>
					<view>
						<u-input v-model="typeName" :placeholder="$t('shop.pla')" clearable="false" />
					</view>
				</view>
				<view class="addguiges" @click="bindclose(aa)">{{$t('index.sure')}}</view>
			</view>
		</u-popup>
	</view>
</template>

<script>
	import configdata from '@/common/config.js';
	export default {

		data() {
			return {
				shopName: '',
				customStyle: {
					backgroundColor: '#FF6D9D',
					color: '#ffffff',
					border: 0
				},
				shopList: [{
					value: '',
					detail: [],
				}],
				listIndex1: 0,
				listIndex: 0,
				show: false,
				typeName: '',
				ruleValues: [],
				id: '',
				guigeLIst: {},
				detailName: '',
				guigeIndex: 0,
				guigezhiIndex: 0,
				aa: '',
				shopId: '',
			}
		},
		onLoad(option) {
			uni.setNavigationBarTitle({
				title:this.$t('sku.title')
			})
			this.id = option.id
			this.shopId = this.$queue.getData("updataShopId")
			if (option.id) {
				this.guigeLIst = uni.getStorageSync('guige')
				this.shopName = this.guigeLIst.ruleName
				this.shopList = this.guigeLIst.ruleValue

				for (var i = 0; i < this.shopList.length; i++) {
					// #ifdef APP
					this.shopList[i].detail = this.shopList[i].detail.split(',')
					// #endif
					// #ifndef APP
					this.shopList[i].detail = this.shopList[i].detail.split('/')
					// #endif
				}
			}


		},
		onShow() {

		},
		methods: {
			delteItem(index){
				this.shopList.splice(index, 1)
			},
			//删除规格
			bindupdata(index1, item1, index) {

				this.shopList[index].detail.splice(index1, 1)
			},
			bindqie(index1, item1, index) {
				this.listIndex = index
				this.listIndex1 = index1
				this.shopList[index].detail[index1] = item1
				this.typeName = item1
				this.aa = index1
				this.show = true

			},
			addtype(index1, item, index) {
				this.guigeIndex = index
				this.guigezhiIndex = index1

				if (index1 == 1) {
					this.typeName = ''
					this.aa = ''
					this.show = true

				}
			},
			bindclose(index) {
				if (!this.typeName) {
					uni.showToast({
						title:this.$t('sku.plasku'),  // '请填写规格',
						icon: 'none',
						duration: 1000
					})
					return
				}
				console.log('aaaaaaa', index)
				if (index === '') {
					this.show = false
					// this.list.push(this.typeName)
					this.shopList[this.guigeIndex].detail.push(this.typeName)
				} else {
					this.show = false
					this.shopList[this.guigeIndex].detail[this.aa] = this.typeName
				}
			},
			// 发布
			submit() {
				if (!this.shopName) {
					uni.showToast({
						title:this.$t('sku.plaskuname'),// '请填写规格标题',
						icon: 'none',
						duration: 1000
					})
					return
				}
				
				if(this.shopList.length == 0){
					uni.showToast({
						title:this.$t('Commodity.plaattrsType'),// '请选择规格',
						icon: 'none',
						duration: 1000
					})
					return
				}
				
				var arrLength = this.shopList[this.shopList.length - 1]
				if (arrLength.value == '' || arrLength.detail.length == 0) {
					uni.showToast({
						title: this.$t('sku.plaggz'),//'请填写规格或是规格值',
						icon: 'none'
					})
					return
				}
				let guigeList = {}

				let shopList = JSON.parse(JSON.stringify(this.shopList))
				for (var i in shopList) {
					shopList[i].detail = shopList[i].detail.toString()
				}
				guigeList.ruleName = this.shopName
				guigeList.ruleValue = shopList
				guigeList.shopId = this.shopId
				
				if (this.id) {
					guigeList.id = this.id
					this.$Request.postJsonA("/selfGoodsRule/update", guigeList).then(res => {
						if (res.code == 0) {
							uni.showToast({
								title:this.$t('add.updeteok'),// '修改成功',
								icon: 'none'
							})
							uni.navigateBack()
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				} else {
					this.$Request.postJsonA("/selfGoodsRule/save", guigeList).then(res => {
						if (res.code == 0) {
							uni.navigateBack()
						}
					})
				}

			},
			// 添加规格
			addguige() {
				var arrLength = this.shopList[this.shopList.length - 1]
				if (arrLength.value == '' || arrLength.detail.length == 0) {
					uni.showToast({
						title: this.$t('sku.plaggz'),//'请填写规格或是规格值',
						icon: 'none'
					})
				} else {
					var data = {
						value: '',
						detail: [],
					}
					this.shopList.push(data)
				}

			}
		}
	}
</script>

<style>
	page {
		background-color: #F5F5F5;
	}

	.bg {
		background-color: #FFFFFF;
	}


	.btn {
		border: 1upx solid #CCCCCC;
		border-radius: 28px;
		padding: 15rpx 30rpx;
		margin-right: 25rpx;
	}

	.btns {
		border: 1upx dashed #333333;
		border-radius: 28px;
		padding: 10rpx 30rpx;
		margin-right: 25rpx;
	}

	.active {
		/* background: #FCD202; */
		/* border: none; */
	}

	.addguige {
		width: 90%;
		margin: 0 auto;
		background: #FCD202;
		box-shadow: 0px 10upx 20upx 0px #FFD9B3;
		border-radius: 16upx;
		text-align: center;
		height: 88upx;
		line-height: 88upx;
		/* position: fixed;
		bottom: 25upx;
		left: 0;
		right: 0; */
	}

	.addguiges {
		width: 90%;
		margin: 0 auto;
		background: #FF6D9D;
		box-shadow: 0px 10upx 20upx 0px #FF6D9D;
		border-radius: 16upx;
		text-align: center;
		height: 88upx;
		line-height: 88upx;
		color: #FFFFFF;
		/* position: fixed;
		bottom: 25upx;
		left: 0;
		right: 0; */
	}
</style>
