<template>
	<view class="padding">
		<view class="text-white padding bg radius">
			<u-form :model="shop" label-position="top" :label-style='lableStyle'>
				<u-form-item label="店铺名称">
					<u-input v-model="shop.shopName" placeholder="请输入店铺名称" />
				</u-form-item>
				<u-form-item label="开店时间">
					<u-input v-model="shop.openTime" placeholder="请填写" disabled="true" @click="startShow = true" />
				</u-form-item>
				<u-form-item label="闭店时间">
					<u-input v-model="shop.closeTime" disabled="true" placeholder="请填写" @click="endShow = true" />
				</u-form-item>

				<u-form-item label="店铺地址">
					<u-input v-model="shop.detailedAddress" disabled="true" placeholder="请填写" @click="bindOpen()" />
				</u-form-item>
				<u-form-item label="是否打烊" >
					<u-switch v-model="shop.putawayFlag" active-color="#FF6D9D" active-value="2" inactive-value="1" size="38" @change="change()"></u-switch>
				</u-form-item>
			</u-form>
		</view>

		<view class="text-white padding bg radius margin-tb">
			<u-form :model="shop" label-position="top">
				<u-form-item label="打印机">
					<text class="text-sm"
						style="color: red;position: absolute;top: 25rpx;left: 105rpx;">*一个打印机只能绑定一个商家</text>
					<u-radio-group v-model="shop.facility">
						<!-- <u-radio name=0>上线</u-radio> -->
						<u-radio name=1>飞鹅</u-radio>
					</u-radio-group>
				</u-form-item>
				<u-form-item label="设备sn编码">
					<u-input v-model="shop.snCode" placeholder="请输入设备sn编码" />
				</u-form-item>
				<u-form-item label="设备key秘钥">
					<u-input v-model="shop.value" placeholder="请输入设备key秘钥" />
				</u-form-item>
			</u-form>
		</view>

		<view class="text-white padding bg radius margin-tb">
			<view>
				<view class="text-lg margin-top-sm text-black">轮播图</view>
				<view class="flex" style="overflow: hidden;flex-wrap: wrap;">
					<view v-if="detailsImg.length">
						<view class="margin-top flex margin-right-sm flex-wrap">
							<view class="flex"
								style="width: 200rpx;height: 200rpx;margin-right: 2rpx;position: relative;"
								v-for="(image,index) in detailsImg" :key="index">
								<image :src="image" style="width: 100%;height: 100%;"></image>
								<view style="z-index: 9;position: absolute;top: -15rpx;right: -15rpx;"
									@click="removeImg(index)">
									<u-icon name="close-circle-fill" color="#2979ff" size="50rpx"></u-icon>
								</view>

							</view>
						</view>
					</view>
					<view class="margin-top" @click="addImages(2)" v-if="detailsImg.length<6">
						<view style="width: 200rpx;height: 200rpx;background: #f4f5f6;"
							class="flex justify-center align-center">
							<view>
								<view class="text-center">
									<image src="./static/add.png" style="width: 65rpx;height: 55rpx;">
									</image>
								</view>
								<view class="text-center text-black">添加图片</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 时间筛选 -->
		<u-picker v-model="startShow" mode="time" :params="params" @confirm="getStarttime()"></u-picker>

		<u-picker v-model="endShow" mode="time" :params="paramss" @confirm="getEndtime()"></u-picker>


		<u-button @click="submit" class="margin-top" :custom-style="customStyle" shape="square" :hair-line="false">提交
		</u-button>

	</view>
</template>

<script>
	import configdata from '@/common/config.js';
	export default {

		data() {
			return {
				shop: {
					//详情图
					detailsImg: [],
					city: '',
					myLevel: '',
					openTime: '',
					closeTime: '',
					address: '',
					latitude: '',
					longitude: '',
					facility: '',
					snCode: '',
					value: '',
					putawayFlag:0
				},
				checked: false,
				startShow: false,
				endShow: false,
				customStyle: {
					backgroundColor: '#FF6D9D',
					color: '#ffffff',
					border: 0
				},
				status: 1,
				customStyle1: {
					color: '#000000',
					position: "fixed",
					bottom: "15rpx",
					left: "0rpx",
					right: "0rpx",
					margin: "0rpx 50rpx",
					backgroundColor: '#FF6D9D',
				},
				selarr: [],
				show: false,
				//详情图
				detailsImg: [],
				params: {
					year: false,
					month: false,
					day: false,
					hour: true,
					minute: true,
					second: false
				},
				paramss: {
					year: false,
					month: false,
					day: false,
					hour: true,
					minute: true,
					second: false
				},
				shopId: '',
				shop: {
					shopName: '',
					openTime: '',
					closeTime: '',
					detailedAddress: '',
					detailsImg: [],
				},
				page: 1,
				limit: 100
			}
		},
		onLoad(option) {

			console.log(option, '店铺id')
			this.shopId = option.shopId

			this.getShopList()
		},
		onShow() {

		},
		methods: {
			change(status) {
				console.log(status,'pppppppppp');
				if(status == 1){
					this.shop.putawayFlag = 1
				}else{
					this.shop.putawayFlag = 2
				}
				
			},
			// 店铺列表
			getShopList() {
				let data = {
					page: this.page,
					limit: this.limit,
				}
				this.$Request.getA("/admin/goods/selectAllShop", data).then(res => {
					if (res.code == 0) {
						// for (var i = 0; i < res.data.list.length; i++) {
						// 	if(res.data.list[i].shopId == this.shopId){
						// 		console.log('-------')
						// 		this.shop = res.data.list[i]
						// 	}
						// }
						this.shopList = res.data.list
						this.shopList.forEach(res => {
							if (res.shopId == this.shopId) {
								this.shop = res
							}
						})
						this.detailsImg = this.shop.shopBanner.split(',')
						// console.log(this.shop)

					}
				});
			},
			getStarttime(e) {
				console.log(e)
				console.log(this.shop)
				this.shop.openTime = e.hour + ':' + e.minute
			},
			getEndtime(e) {
				console.log(e)
				this.shop.closeTime = e.hour + ':' + e.minute
			},
			// 详情图删除
			removeImg(index) {
				this.detailsImg.splice(index, 1)
			},

			bindOpen(e) {
				console.log(e)
				let that = this
				uni.chooseLocation({
					success: function(res) {
						console.log('位置名称：' + res.name);
						console.log('详细地址：' + res.address);
						console.log('纬度：' + res.latitude);
						console.log('经度：' + res.longitude);
						that.shop.detailedAddress = res.address
						that.shop.shopLat = res.latitude
						that.shop.shopLng = res.longitude
					}
				});
			},

			// 图片上传
			addImages(e) {
				let that = this
				uni.chooseImage({
					count: 6,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							that.$queue.showLoading("上传中...");
							uni.uploadFile({ // 上传接口
								url: that.config("APIHOST1") + '/alioss/upload', //真实的接口地址
								// url: 'https://jiazheng.xianmxkj.com/sqx_fast/alioss/upload',
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									if (that.detailsImg.length < 6) {
										that.detailsImg.push(JSON.parse(uploadFileRes.data).data)
									}

									console.log(that.detailsImg)
									uni.hideLoading();
								}
							});
						}
					}
				})
			},
			config: function(name) {
				var info = null;
				if (name) {
					var name2 = name.split("."); //字符分割
					if (name2.length > 1) {
						info = configdata[name2[0]][name2[1]] || null;
					} else {
						info = configdata[name] || null;
					}
					if (info == null) {
						let web_config = cache.get("web_config");
						if (web_config) {
							if (name2.length > 1) {
								info = web_config[name2[0]][name2[1]] || null;
							} else {
								info = web_config[name] || null;
							}
						}
					}
				}
				return info;
			},
			// 发布
			submit() {
				console.log(this.shop,'aaaaaaaaaaaaaaaaaa')
				this.shop.shopBanner = this.detailsImg
				this.shop.shopBanner = this.shop.shopBanner.toString();

				if (!this.shop.shopName) {
					uni.showToast({
						title: '请填写店铺名称',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.shop.openTime) {
					uni.showToast({
						title: '请填写开店时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.shop.closeTime) {
					uni.showToast({
						title: '请填写闭店时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.shop.detailedAddress) {
					uni.showToast({
						title: '请填写店铺地址',
						icon: 'none',
						duration: 1000
					})
					return
				}


				if (!this.shop.shopBanner) {
					uni.showToast({
						title: '请上传店铺轮播图',
						icon: 'none',
						duration: 1000
					})
					return
				}
				console.log(this.shop)
				uni.setStorageSync('updataShopId', this.shop.shopId)
				this.$Request.postJsonA("/admin/goods/updateGoodsShop", this.shop).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: '修改成功',
							icon: 'none'
						})

						setTimeout(function() {
							uni.navigateBack()
							// uni.navigateTo({
							// 	url: '/my/publish/index'
							// })
						}, 1000)
					} else {
						uni.showToast({
							title: res.msg,
							icon: 'none'
						})
					}
				});
			},
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


	.tabBox {
		border: 1rpx solid #999999;
		padding: 15rpx 20rpx;
		border-radius: 15rpx;
		font-size: 28rpx;
	}

	.btnnum {
		color: #005DFF;
		border: 1rpx solid #005DFF;
	}
</style>
