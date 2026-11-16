<template>
	<view style="padding-bottom: 100rpx;">
		<view class="add flex justify-center">
			<view class="add-box">
				<u-form :model="form" ref="uForm" label-position='top'>
					<u-form-item :label="$t('Commodity.goodsName')">
						<u-input v-model="form.goodsName" :placeholder="$t('Commodity.plagoodsName')" />
					</u-form-item>
					<u-form-item :label="$t('Commodity.classifyName')">
						<u-input disabled @click="getTypeList" v-model="form.classifyName"
							:placeholder="$t('Commodity.placlassifyName')" type="select" />
					</u-form-item>
					<u-form-item :label="$t('Commodity.originalMoney')">
						<u-input v-model="form.originalMoney" @input="ZheKouInput" type="number"
							:placeholder="$t('Commodity.plaoriginalMoney')" />
					</u-form-item>
					<u-form-item :label="$t('Commodity.goodsMoney')">
						<u-input v-model="form.goodsMoney" @input="ZheKouInput" type="number"
							:placeholder="$t('Commodity.plagoodsMoney')" />
					</u-form-item>
					<u-form-item :label="$t('Commodity.packMoney')">
						<u-input v-model="form.packMoney" type="number" :placeholder="$t('Commodity.plapackMoney')" />
					</u-form-item>

					<u-form-item :label="$t('Commodity.label')">
						<view class="flex">
							<view class="label" v-for="(item,index) in label" :key="index">
								{{item}}
								<u-icon class="closeLabel" @click="closeLabel(index)" name="close-circle"
									color="#FF6D9D" size="38"></u-icon>
							</view>
							<view class="label" @click="showLabel = true">
								+{{$t('Commodity.addl')}}
							</view>
						</view>

					</u-form-item>
					<u-form-item :label="$t('Commodity.goodsCover')">
						<view class="upswiper" v-if="form.goodsCover">
							<image :src="form.goodsCover" mode=""></image>
							<u-icon class="closeLabel" @click="form.goodsCover = ''" name="close-circle-fill"
								color="#FF6D9D" size="38"></u-icon>
						</view>
						<view v-if="form.goodsCover==''" class="upswiper flex justify-center flex-wrap align-center">
							<view class="upswiper-box" @click="upSwiper(1)">
								<u-icon name="plus" color="#000" size="28"></u-icon>
								<view class="upswiper-txt">{{$t('shop.addImg')}} </view>
							</view>
						</view>
					</u-form-item>
					<u-form-item :label="$t('Commodity.goodsPicture')">
						<view class="upswiper" v-for="(item,index) in goodsPicture" :key="index">
							<image :src="item" mode=""></image>
							<u-icon class="closeLabel" @click="closeAttr(index)" name="close-circle-fill"
								color="#FF6D9D" size="38"></u-icon>
						</view>
						<view v-if="goodsPicture.length<9" class="upswiper flex justify-center flex-wrap align-center">
							<view class="upswiper-box" @click="upSwiper(2)">
								<u-icon name="plus" color="#000" size="28"></u-icon>
								<view class="upswiper-txt">
									{{$t('shop.addImg')}}
								</view>
							</view>
						</view>
					</u-form-item>
					<u-form-item :label="$t('Commodity.goodsSynopsis')">
						<view class=""
							style="border-radius: 16rpx;background-color: #EDF1F7;width: 100%;padding: 10rpx;">
							<u-input v-model="form.goodsSynopsis" type="textarea"
								:placeholder="$t('Commodity.plagoodsSynopsis')" />
						</view>

					</u-form-item>
					<u-form-item :label="$t('Commodity.goodsDescribe')">
						<view class="" style="width: 100%;min-height: 500rpx;">
							<maxiongEditor v-if="showEditor" :action="action" :value="form.goodsDescribe"
								:placeholder="$t('Commodity.plagoodsDescribe')" @editorChange="editorChange" />
						</view>

					</u-form-item>
					<u-form-item :label="$t('Commodity.attrsType')">
						<u-input v-model="attrsType" :placeholder="$t('Commodity.plaattrsType')" :disabled="true"
							@click="ggshow = true" />
					</u-form-item>
					<u-form-item :label="$t('Commodity.attrs')" :border-bottom="false" v-if="attrsid==2">
						<view style="width: 100%;">
							<view style="display: flex;width: 100%;">
								<u-input style="width: 80%;" v-model="attrs" :placeholder="$t('Commodity.plaattrs')"
									:disabled="true" @click="openguige()" />
								<view style="width: 20%;text-align: right;color: #FF4701;" v-if="attrs"
									@click="goGuiGeItem()">
									{{$t('Commodity.plaxgjg')}}
								</view>
							</view>
							<view style="color: #FF4701;font-size: 24upx;">*{{$t('Commodity.plaattrTip')}}</view>
						</view>
					</u-form-item>
				</u-form>
			</view>
		</view>
		<view class="submitGoods flex justify-center">
			<view class="submitGoods-box flex justify-center align-center" @click="submitGoods()">
				{{$t('shop.tj')}}
			</view>
		</view>
		<!-- 商品类型 -->
		<u-select v-model="show" :list="list" value-name="classifyId" label-name="classifyName" @confirm="confirm"
			:cancelText="$t('index.qx')" :confirmText="$t('index.sure')"></u-select>
		<!-- 商品标签 -->
		<u-popup v-model="showLabel" mode="center" border-radius="14" width="500rpx" height="340rpx">
			<view>
				<view class="padding" style="margin-bottom: 20rpx;">
					<view>{{$t('Commodity.label')}}</view>
					<view class="flex align-center" style="margin-top: 15rpx;">
						<label style="margin-right: 10rpx;">{{$t('Commodity.labelname')}}</label>
						<u-input v-model="labelName" :placeholder="$t('Commodity.plalabelname')" :clearable="false" />
					</view>
				</view>
				<view class="addguiges" @click="bindAdd()">{{$t('index.sure')}}</view>
			</view>
		</u-popup>
		<!-- 规格弹框 -->
		<u-popup v-model="Guigeshow" mode="bottom" border-radius="14" :closeable="true">
			<view>
				<scroll-view scroll-y="true">
					<view class="padding" style="margin-bottom: 70px;">
						<view>{{$t('Commodity.attrsType')}}</view>
						<view class="flex align-center margin-top-sm flex-wrap">
							<view class="" v-for="(item,indexs) in listattr" :key="indexs">
								<view class="btn margin-top-sm flex align-center flex-wrap"
									:class="listIndex == indexs?'boxH':''" @click="bindguige(indexs,1,item.ruleName)">
									{{item.ruleName}}
								</view>
							</view>
						</view>
						<view v-for="(name,index) in attrList.ruleValue" :key="index">
							<view class="margin-top-sm">{{name.value}}</view>
							<view class="flex align-center flex-wrap margin-top-sm">
								<view class="btn margin-top-sm" v-for="(ite,ind) in name.detail1" :key='ind'
									@click="bindguige(ind,2,name.detail,index)" v-if="name.detail1">
									{{ite}}
								</view>
							</view>
						</view>
					</view>
				</scroll-view>
				<view class="addguige" @click="bindget()">{{$t('index.sure')}}</view>
			</view>
		</u-popup>
		<!-- 规格类型 -->
		<u-select v-model="ggshow" :list="attrList1" valueName='id' labelName="name" @confirm="ggconfirm"
			:cancelText="$t('index.qx')" :confirmText="$t('index.sure')"></u-select>

	</view>
</template>

<script>
	import maxiongEditor from '@/components/maxiong-editor/maxiong-editor.vue'
	import config from '../common/config.js'
	export default {
		components: {
			maxiongEditor
		},
		data() {
			return {
				action: config.APIHOST,
				showEditor: false,
				update: 0,
				attrsType: '', //多规格
				attrList1: [{
						'name': '', //单规格
						'id': 1
					},
					{
						'name': '', //多规格
						'id': 2
					}
				],
				ggshow: false,
				listIndex: 0,
				attrs: '',
				attrList: {},
				Guigeshow: false,
				goodsPicture: [], //商品轮播图
				labelName: '',
				showLabel: false,
				label: [], //商品标签
				show: false,
				list: [],
				form: {
					goodsName: '', //商品名称
					classifyId: '', //商品类型id
					classifyName: '', //商品类型名称
					originalMoney: '', //商品原价
					goodsMoney: '', //商品折扣价
					packMoney: 0, //商品打包费
					shopId: uni.getStorageSync('updataShopId'), //店铺id
					memberPrice: '', //会员价
					salesVolume: 0, //销量
					status: 0, //商品状态：0：上架 1:下架
					goodsCover: '', //商品封面图
					sku: [], //商品规格
					attr: [],
					goodsSynopsis: '', //商品简介
					goodsDescribe: '', //商品详情
				},
				attrsid: 2,
				shopId: '',
				listattr: []
			};
		},
		onLoad(option) {
			this.shopId = this.$queue.getData("updataShopId")
			if (option.goodsId) {
				this.showEditor = false
				this.update = 1
				uni.setNavigationBarTitle({
					title: this.$t('Commodity.xgtit') //'修改商品'
				})
				this.goodsDet(option.goodsId)
			} else {
				this.update = 0
				this.showEditor = true
				uni.setNavigationBarTitle({
					title: this.$t('Commodity.add')
				})
			}

			this.attrsType = this.$t('Commodity.skuduo');
			this.attrList1[0].name = this.$t('Commodity.skudan'); //单规格
			this.attrList1[1].name = this.$t('Commodity.skuduo'); //多规格
			this.getguige()
		},
		onShow() {
			let list = uni.getStorageSync('guigeSelectItemList');
			if (list) {
				this.form.sku = list;
				uni.removeStorageSync('guigeSelectItemList')
			}
		},
		methods: {
			// 获取商铺详情
			goodsDet(goodsId) {
				this.$Request.getA("/admin/goods/selectGoodsById?goodsId=" + goodsId).then(res => {
					if (res.code == 0) {
						this.form = res.data;
						this.getTypeList(1)
						if (this.form.goodsLabel) {
							this.label = this.form.goodsLabel.split(',')
						}
						if (this.form.goodsPicture) {
							this.goodsPicture = this.form.goodsPicture.split(',')

						}
						if (res.data.attr.length == 0) {
							this.attrsid = 1
							this.attrsType = this.$t('Commodity.skudan'); // '单规格'
						} else {
							this.attrsid = 2
							this.attrsType = this.$t('Commodity.skuduo'); //'多规格'
						}
						this.showEditor = true
						this.getguige()
					}
				});
			},
			submitGoods() {
				if (!this.form.goodsName) {
					uni.showToast({
						title: this.$t('Commodity.plagoodsName'), // '请输入商品名称',
						icon: 'none'
					})
					return
				}
				if (!this.form.classifyName) {
					uni.showToast({
						title: this.$t('Commodity.placlassifyName'), // '请选择商品分类',
						icon: 'none'
					})
					return
				}
				if (!this.form.originalMoney) {
					uni.showToast({
						title: this.$t('Commodity.plaoriginalMoney'), // '请输入商品原价',
						icon: 'none'
					})
					return
				}
				if (!this.form.goodsMoney) {
					uni.showToast({
						title: this.$t('Commodity.plagoodsMoney'), // '请输入商品折扣价',
						icon: 'none'
					})
					return
				}
				if (this.label.length == 0) {
					uni.showToast({
						title: this.$t('Commodity.plalabel'), // '请输入商品标签',
						icon: 'none'
					})
					return
				}
				if (!this.form.goodsCover) {
					uni.showToast({
						title: this.$t('Commodity.plagoodsCover'), // '请上传商品封面图',
						icon: 'none'
					})
					return
				}
				if (this.goodsPicture.length == 0) {
					uni.showToast({
						title: this.$t('Commodity.plagoodsPicture'), // '请上传商品轮播图',
						icon: 'none'
					})
					return
				}
				if (!this.form.goodsSynopsis) {
					uni.showToast({
						title: this.$t('Commodity.plagoodsSynopsis'), // '请输入商品简介',
						icon: 'none'
					})
					return
				}
				if (!this.form.goodsDescribe) {
					uni.showToast({
						title: this.$t('Commodity.plagoodsDescribe'), // '请输入商品详情',
						icon: 'none'
					})
					return
				}
				if (this.attrsid == 2) {
					if (this.form.attr.length == 0) {
						uni.showToast({
							title: this.$t('Commodity.plaattrs'), // '请选择商品规格',
							icon: 'none',
							duration: 1000
						})
						return
					}
					let indexs = this.form.sku.findIndex(item => !item.skuOriginalPrice)
					console.log(indexs)
					if (indexs != -1) {
						uni.showToast({
							title: this.$t('Commodity.plaskuOriginalPrices'),
							icon: 'none'
						})
						return
					}
				}
				let data = this.form
				data.goodsPicture = this.goodsPicture.toString()
				data.goodsLabel = this.label.toString()
				for (var i = 0; i < data.attr.length; i++) {
					let attrValues = data.attr[i].attrValue;
					for (var j = 0; j < attrValues.length; j++) {
						let details = attrValues[j].detail;
						let detailss = '';
						for (var z = 0; z < details.length; z++) {
							detailss = detailss + details[z];
						}
						data.attr[i].attrValue[j].detail = detailss
					}
				}
				if (this.update == 0) {

					var url = '/admin/goods/insertGoods'
				}
				if (this.update == 1) {

					var url = '/admin/goods/update'
				}
				this.$Request.postJsonA(url, data).then(res => {
					if (res.code == 0) {
						uni.showToast({
							title: this.$t('Commodity.ok'), // '操作成功',
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
				console.log(data)
			},
			//规格分类
			ggconfirm(e) {
				// console.log(e)
				this.attrsType = e[0].label
				this.attrsid = e[0].value
				if (e[0].value == 1) {
					this.form.attr = []
					this.guigeclect()
				}
			},
			// 单规格
			guigeclect() {
				this.$Request.getA("/admin/goods/onlyFormatAttr?coverImg=" + this.form.goodsCover + '&originalPrice=' +
					this.form.originalMoney + '&price=' + this.form.goodsMoney).then(res => {
					if (res.code == 0) {
						this.form.sku = res.data.value

					}
				});
			},
			//修改价格后同步规格
			ZheKouInput(e) {
				this.attrs = '';
				this.form.attr = []
				if (this.attrsid == 1) {
					this.guigeclect()
				} else {
					this.getgoodslist()
				}
			},
			//修改价格
			goGuiGeItem() {
				uni.setStorageSync('guigeSelectItemList', this.form.sku)
				uni.navigateTo({
					url: '/pages/guigeItem'
				});
			},
			//获取商品规格列表
			getgoodslist() {

				let ruleValues = this.attrList.ruleValue

				for (var j = 0; j < ruleValues.length; j++) {
					let details = ruleValues[j].detail;
					ruleValues[j].detail = ruleValues[j].detail.toString();
				}
				let data = {
					attrName: this.attrList.ruleName,
					attrValue: ruleValues,
					ruleId: this.attrList.id,
				}

				this.$Request.postJsonA("/admin/goods/isFormatAttr?coverImg=" + this.form.goodsCover + '&originalPrice=' +
					this.form.originalMoney + '&price=' + this.form.goodsMoney, data).then(res => {
					if (res.code == 0) {
						this.form.sku = res.data.value
					}
				});

			},
			bindget() {
				this.getgoodslist()
				this.attrs = this.attrList.ruleName
				var obj = {}
				obj.attrName = this.attrList.ruleName
				obj.attrValue = this.attrList.ruleValue
				obj.ruleId = this.attrList.id
				this.form.attr.push(obj)

				this.Guigeshow = false
			},
			//弹框规格切换
			bindguige(index, e, name, index1) {
				// console.log(index, e, name)
				if (e == 1) {
					this.listIndex = index
					this.attrList = this.listattr[index]

				} else if (e == 2) {
					this.typeIndex = index
					this.typeIndex1 = index1

				} else if (e == 3) {
					this.wdIndex = index
				}
			},
			//获取商品规格
			getguige() {
				let data = {
					shopId: this.shopId,
				}
				this.$Request.getA("/selfGoodsRule/list", data).then(res => {
					if (res.code == 0) {
						if (res.data.length > 0) {
							this.listattr = res.data
							this.listattr.forEach(res => {
								res.ruleValue.forEach(ret => {
									ret.detail = ret.detail.split(',')
									ret.detail1 = ret.detail
								})

							})
							this.attrList = this.listattr[0]
							if (this.form.attr && this.form.attr.length > 0) {
								for (var i in res.data) {
									if (this.form.attr[0].ruleId == res.data[i].id) {
										this.attrs = res.data[i].ruleName
									}
								}
							}

						}
					}
				});
			},
			//规格弹框
			openguige() {
				if (!this.attrList.ruleValue) {
					uni.showToast({
						title: this.$t('Commodity.zwgg'), //'暂无规格',
						icon: 'none',
						duration: 1000
					})
					return
				}
				// 打开规格选项弹框
				this.Guigeshow = true

			},
			//富文本编辑器内容回调
			editorChange(e) {
				this.form.goodsDescribe = e
				console.log(e)
			},
			//删除轮播图
			closeAttr(index) {
				this.goodsPicture.splice(index)
			},
			//上传图片 1:上传封面图 2:上传轮播图
			upSwiper(index) {
				let that = this
				uni.chooseImage({
					count: 1,
					sourceType: ['album', 'camera'],
					success: res => {
						for (let i = 0; i < res.tempFilePaths.length; i++) {
							// that.$queue.showLoading("上传中...");
							uni.showLoading({
								title: that.$t('loadingText.scz')
							})
							uni.uploadFile({ // 上传接口
								url: config.APIHOST + '/alioss/upload', //真实的接口地址
								// url: 'https://tcwm.xianmaxiong.com/sqx_fast/alioss/upload',
								filePath: res.tempFilePaths[i],
								name: 'file',
								success: (uploadFileRes) => {
									if (index == 1) {
										that.form.goodsCover = JSON.parse(uploadFileRes.data).data
									} else {
										that.goodsPicture.push(JSON.parse(uploadFileRes.data).data)
									}
									uni.hideLoading();
								}
							});
						}
					}
				})
			},
			//删除标签
			closeLabel(index) {
				this.label.splice(index, 1)
			},
			// 保存标签
			bindAdd() {
				if (!this.labelName) {
					uni.showToast({
						title: this.$t('Commodity.plalabel'), //'请输入标签',
						icon: 'none'
					})
					return
				}
				this.label.push(this.labelName)
				this.labelName = ''
				this.showLabel = false
			},
			//选择分类的回调
			confirm(e) {
				console.log(e)
				this.form.classifyId = e[0].value
				this.form.classifyName = e[0].label
			},
			//获取自己店铺的分类列表
			getTypeList(type) {
				let data = {
					shopId: this.shopId
				}
				this.$Request.getA("/admin/goods/selectAllClassify", data).then(res => {
					if (res.code == 0) {
						if (res.data) {
							this.list = res.data
							if (!type) {
								if (this.list.length > 0) {
									this.show = true
								} else {
									this.show = false
									uni.showModal({
										title: this.$t('index.wxts'),
										content: this.$t('Commodity.zwsp'), //暂无商品分类，请添加商品分类
										confirmText: this.$t('Commodity.gadd'), //去添加
										cancelText: this.$t('index.qx'),
										complete(ret) {
											if (ret.confirm) {
												uni.navigateTo({
													url: '/pages/goodsType'
												})
											}
										},
									})
								}
							}

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

	.submitGoods {
		width: 100%;
		height: 80rpx;
		margin-top: 40rpx;

		.submitGoods-box {
			width: 686rpx;
			height: 100%;
			background-color: #FF6D9D;
			color: #FFFFFF;
			border-radius: 16rpx;
		}
	}

	.addguige {
		width: 90%;
		margin: 0 auto;
		background: #FF6D9D;
		box-shadow: 0px 10upx 20upx 0px #FF6D9D;
		color: #ffffff;
		border-radius: 16upx;
		text-align: center;
		height: 88upx;
		line-height: 88upx;
		position: fixed;
		bottom: 25upx;
		left: 0;
		right: 0;

	}

	.boxH {
		background: #FF6D9D;
		border: none;
		color: #FFFFFF;
	}

	.btn {
		border: 1upx solid #CCCCCC;
		border-radius: 10px;
		padding: 0rpx 30rpx;
		margin-right: 25rpx;
		height: 30px;
		line-height: 30px;
	}

	.subgg {
		width: 100%;
		height: 80rpx;
		margin-top: 20rpx;
		margin-bottom: 60rpx;

		.subgg-box {
			width: 686rpx;
			height: 100%;
			border-radius: 28rpx;
			background-color: #FF6D9D;
			color: #FFFFFF;
		}
	}

	.active {
		background-color: #FF6D9D !important;
		color: #FFFFFF;
		border-radius: 26rpx;
		border: none;
	}

	.gglist {
		width: 100%;

		.gglist-box {
			width: 686rpx;

			.gglist-boxs {
				width: 100%;
			}

			.gg-item {
				margin-right: 10rpx;
				margin-bottom: 20rpx;
			}

			.gg-item-title {
				padding: 10rpx 20rpx;
				border-radius: 26rpx;
				border: 1rpx solid #EDF1F7;
			}

			.childitem {
				padding: 10rpx 20rpx;
				border: 1rpx solid #EDF1F7;
				border-radius: 26rpx;
				margin-right: 10rpx;

			}
		}
	}

	.gg-title {
		margin-top: 30rpx;
		font-weight: bold;
		text-align: center;
	}

	.upswiper {
		width: 190rpx;
		height: 190rpx;
		border-radius: 16rpx;
		background-color: #EDF1F7;
		margin-right: 20rpx;
		margin-bottom: 20rpx;
		position: relative;

		image {
			width: 100%;
			height: 100%;
			border-radius: 16rpx;
		}

		.upswiper-box {

			text-align: center;
		}
	}

	::v-deep .u-form-item--right__content__slot {
		flex-wrap: wrap;
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
	}

	.label {
		// width: 130rpx;
		border-radius: 24rpx;
		border: 1rpx dashed #000;
		padding: 5rpx 30rpx;
		margin-right: 15rpx;
		margin-bottom: 15rpx;
		position: relative;
	}

	.closeLabel {
		position: absolute;
		top: -5rpx;
		right: -5rpx;
	}

	.add {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.add-box {
			width: 686rpx;
			height: 100%;
			padding: 20rpx 30rpx;
			border-radius: 24rpx;
			background-color: #ffffff;
		}
	}
</style>