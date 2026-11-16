<template>
	<view class="container" style="padding-bottom: 60rpx;">
		<view v-for="(item,index) in guigeAttrList">
			<view class="main">
				<view class="order-des">
					<view class="title">{{$t('Commodity.detailJson')}}</view>
					<view class="textarea-wrap">
						<input type="text" v-model="item.detailJson" disabled class="input" />
					</view>
				</view>
				<view class="order-des">
					<view class="title">{{$t('Commodity.skuOriginalPrice')}}</view>
					<view class="textarea-wrap">
						<input type="digit" v-model="item.skuOriginalPrice" class="input"
							:placeholder="$t('Commodity.plaskuOriginalPrice')" />
					</view>
				</view>
				<view class="order-des">
					<view class="title">{{$t('Commodity.skuPrice')}}</view>
					<view class="textarea-wrap">
						<input type="digit" v-model="item.skuPrice" class="input"
							:placeholder="$t('Commodity.plaskuPrice')" />
					</view>
				</view>
				<!-- <view class="order-des">
					<view class="title">库存</view>
					<view class="textarea-wrap">
						<input type="number" v-model="item.stock" class="input" placeholder="请输入商品库存" />
					</view>
				</view> -->
			</view>
		</view>
		<view class="save_btn" @tap="addSave">{{$t('user.save')}}</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				guigeAttrList: [],
			}
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title: this.$t('Commodity.shopsx')
			})
			this.guigeAttrList = uni.getStorageSync('guigeSelectItemList');
		},
		methods: {
			addSave() {
				let indexs = this.guigeAttrList.findIndex(item => !item.skuOriginalPrice)
				console.log(indexs)
				if (indexs != -1) {
					uni.showToast({
						title: this.$t('Commodity.plaskuOriginalPrice'),
						icon: 'none'
					})
				} else {
					uni.setStorageSync('guigeSelectItemList', this.guigeAttrList)
										uni.navigateBack();
				}


			}
		}
	}
</script>

<style lang="scss">
	page {
		width: 100%;
	}

	.save_btn {
		margin: 50upx;
		width: 650upx;
		height: 88upx;
		text-align: center;
		line-height: 88upx;
		background: #FF9CBD;
		border-radius: 16upx;
		color: #ffffff;
	}

	.container {
		width: 100%;
		padding-bottom: calc(98upx + env(safe-area-inset-bottom));
		padding-bottom: calc(98upx + constant(safe-area-inset-bottom));

		.main {
			padding: 20upx 20upx 20upx;

			.order-des {
				padding: 30upx;
				// border-radius: 20upx;
				background-color: #fff;
				// margin-bottom: 20upx;
				position: relative;

				.title {
					font-size: 30upx;
					font-weight: bold;
					color: #333333;
					line-height: 32upx;
					padding-bottom: 20upx;

				}


				.textarea-wrap {
					padding-top: 20upx;
					width: 100%;
					border-bottom: 1upx solid #E6E6E6;

					.textarea {
						width: 100%;
						font-size: 28upx;
						line-height: 35upx;
						color: #333;
					}
				}

				.textarea-counter {
					text-align: right;
					font-size: 28upx;
					font-weight: 500;
					color: #999999;
					margin-top: 10upx;
				}
			}

			.order-list {
				margin-top: 20upx;
				padding: 0 30upx;
				border-radius: 20upx;
				background-color: #fff;

				.order-list-item {
					width: 100%;
					height: 110upx;
					border-bottom: 1upx solid #E6E6E6;
					display: flex;
					align-items: center;
					justify-content: space-between;

					.tit {
						font-size: 34upx;
						font-weight: 500;
						color: #333333;
					}

					.right {
						font-size: 34upx;
						font-weight: 500;
						color: #666;
						display: flex;
						align-items: center;

						.input {
							flex: 1;
							display: flex;
							align-items: center;
							text-align: right;
							padding-right: 10upx;
						}
					}
				}

				.order-list-item:last-child {
					border-bottom: 0;
				}
			}

		}

		.btns {
			button {
				// width: 80%;
				margin: 22upx;
				color: #fff;
				border: none;
				border-radius: 10upx;
			}

			.qr {
				background: #FF4701;
			}
		}

		.footer {
			width: 100%;
			height: calc(98upx + env(safe-area-inset-bottom));
			height: calc(98upx + constant(safe-area-inset-bottom));
			padding-bottom: env(safe-area-inset-bottom);
			/*兼容IOS>11.2*/
			padding-bottom: constant(safe-area-inset-bottom);
			/*兼容IOS<11.2*/
			background: #FFFFFF;
			position: fixed;
			bottom: 0;
			padding: 0 30upx;
			z-index: 10;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.payinfo {
				font-size: 32upx;
				font-weight: bold;
				color: #333333;

				text {
					color: #FF3737;
				}
			}

			.tui-button-primar {
				width: 300upx;
				height: 78upx;
				line-height: 78upx;
				background: #FF332F;
				border-radius: 10upx;
				margin: 0;
			}
		}

		.dragImg {
			position: absolute;
			left: 266rpx;
			top: 88rpx;
			height: 99%;
			width: 50%;

			.area-con {
				width: 186rpx !important;
			}

			image {
				width: 186rpx !important;
				height: 186rpx !important;
			}
		}
	}
</style>