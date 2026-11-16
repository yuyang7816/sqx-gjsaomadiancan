<template>
	<view class="padding">
		<view class="text-white padding bg radius">
			<u-form :model="shop" label-position="top">
				<u-form-item :label="$t('shop.shopName')">
					<u-input v-model="shop.shopName" :placeholder="$t('shop.plashopName')" />
				</u-form-item>
				<u-form-item :label="$t('shop.openTime')">
					<u-input v-model="shop.openTime" :placeholder="$t('shop.pla')" :disabled="true" @click="startShow = true" />
				</u-form-item>
				<u-form-item :label="$t('shop.closeTime')">
					<u-input v-model="shop.closeTime" :disabled="true" :placeholder="$t('shop.pla')" @click="endShow = true" />
				</u-form-item>

				<u-form-item :label="$t('shop.detailedAddress')">
					<u-input v-model="shop.detailedAddress" :disabled="true" :placeholder="$t('shop.pla')" @click="bindOpen()" />
				</u-form-item>
				<u-form-item :label="$t('shop.putawayFlag')">
					<u-switch v-model="shop.putawayFlag" active-color="#FF6D9D" size="38"></u-switch>
				</u-form-item>
			</u-form>
		</view>

		<view class="text-white padding bg radius margin-tb">
			<u-form :model="shop" label-position="top">
				<u-form-item :label="$t('shop.facility')">
					<text class="text-sm"
						style="color: red;position: absolute;top: 25rpx;left: 105rpx;">*{{$t('shop.plafacility')}}</text>
					<u-radio-group v-model="shop.facility">
						<u-radio name=1>{{$t('shop.fe')}}</u-radio>
					</u-radio-group>
				</u-form-item>
				<u-form-item :label="$t('shop.snCode')">
					<u-input v-model="shop.snCode" :placeholder="$t('shop.plasnCode')" />
				</u-form-item>
				<u-form-item :label="$t('shop.value')">
					<u-input v-model="shop.value" :placeholder="$t('shop.plavalue')" />
				</u-form-item>
			</u-form>
		</view>

		<view class="text-white padding bg radius margin-tb">
			<view>
				<view class="text-lg margin-top-sm text-black">{{$t('shop.banner')}}</view>
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
								<view class="text-center text-black">{{$t('shop.addImg')}}</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 时间筛选 -->
		<u-picker v-model="startShow" mode="time" :params="params" @confirm="getStarttime()" :cancelText="$t('index.qx')" :confirmText="$t('index.sure')"></u-picker>

		<u-picker v-model="endShow" mode="time" :params="paramss" @confirm="getEndtime()" :cancelText="$t('index.qx')" :confirmText="$t('index.sure')"></u-picker>


		<u-button @click="submit" class="margin-top" :custom-style="customStyle" shape="square" :hair-line="false">{{$t('shop.tj')}}
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
					putawayFlag: 0
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
			uni.setNavigationBarTitle({
				title: this.$t('shop.title')
			})
			this.shopId = option.shopId

			this.getShopList()
		},
		onShow() {

		},
		methods: {
			// 店铺列表
			getShopList() {
				let data = {
					page: this.page,
					limit: this.limit,
				}
				this.$Request.getA("/admin/goods/selectAllShop", data).then(res => {
					if (res.code == 0) {

						this.shopList = res.data.list
						this.shopList.forEach(res => {
							if (res.shopId == this.shopId) {
								this.shop = res
							}
						})
						this.shop.putawayFlag = this.shop.putawayFlag == 1 ? true : false
						if (this.shop.shopBanner) {
							this.detailsImg = this.shop.shopBanner.split(',')
						}
					}
				});
			},
			getStarttime(e) {
				this.shop.openTime = e.hour + ':' + e.minute
			},
			getEndtime(e) {
				
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
				this.shop.shopBanner = this.detailsImg
				this.shop.shopBanner = this.shop.shopBanner.toString();

				if (!this.shop.shopName) {
					uni.showToast({
						title: this.$t('shop.plashopName'),  //'请填写店铺名称',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.shop.openTime) {
					uni.showToast({
						title:  this.$t('shop.plaopenTime'),  //'请选择开店时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.shop.closeTime) {
					uni.showToast({
						title: this.$t('shop.placloseTime'),  //'请选择闭店时间',
						icon: 'none',
						duration: 1000
					})
					return
				}
				if (!this.shop.detailedAddress) {
					uni.showToast({
						title: this.$t('shop.pladetailedAddress'),  //'请填写店铺地址',
						icon: 'none',
						duration: 1000
					})
					return
				}


				if (!this.shop.shopBanner) {
					uni.showToast({
						title: this.$t('shop.plashopBanner'),  // '请上传店铺轮播图',
						icon: 'none',
						duration: 1000
					})
					return
				}
				let data = JSON.parse(JSON.stringify(this.shop))
				data.putawayFlag = data.putawayFlag == true ? 1 : 2
				uni.setStorageSync('updataShopId', this.shop.shopId)
				this.$Request.postJsonA("/admin/goods/updateGoodsShop", data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('add.updeteok'),//'修改成功',
							icon: 'none'
						})

						setTimeout(function() {
							uni.navigateBack()
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