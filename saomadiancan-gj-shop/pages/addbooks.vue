<template>
	<view>
		<view class="from flex justify-center">
			<view class="from-box">
				<u-form :model="form" ref="uForm" label-position="top">
					<u-form-item :label="$t('Table.seatName')">
						<u-input v-model="form.seatName" :placeholder="$t('Table.plaseatName')" />
					</u-form-item>
					<u-form-item :label="$t('Table.sort')">
						<u-number-box :disabled-input="true" v-model="form.sort" @change="valChange"></u-number-box>
						<!-- <u-input type="number" v-model="form.sort" placeholder="请输入桌位名称" /> -->
					</u-form-item>
					<u-form-item :label="$t('Table.isEnable')">
						<u-radio-group v-model="form.isEnable" active-color="#FF6D9D">
							<u-radio :name="1">{{$t('Table.qy')}}</u-radio>
							<u-radio :name="0">{{$t('Table.bqy')}}</u-radio>
						</u-radio-group>
					</u-form-item>

				</u-form>
			</view>
		</view>
		<view class="submit flex justify-center align-center">
			<view class="submit-box flex justify-center align-center" @click="addbooks()">
				{{$t('index.sure')}}
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					seatName: '', //桌位名称
					shopId: uni.getStorageSync('updataShopId'), //店铺id
					isEnable: 1, //是否启用 1:启用 0:不启用
					sort: 0, //排序
				}
			};
		},
		onLoad(option) {
			if(option.seatId){
				uni.setNavigationBarTitle({
					title:this.$t('Table.updetezw')//'修改桌位'
				})
				this.getInfo(option.seatId)
			}else{
				uni.setNavigationBarTitle({
					title:this.$t('Table.addzw')
				})
			}
		},
		methods:{
			getInfo(seatId){
				let data = {
					seatId:seatId
				}
				this.$Request.getA('/admin/shopSeat/getSeatInfo',data).then(res=>{
					if(res.code == 0){
						this.form.seatName = res.data.seatName
						this.form.isEnable = res.data.isEnable
						this.form.seatId = res.data.seatId
						this.form.sort = res.data.sort
						
					}else{
						uni.showToast({
							title:res.msg,
							icon:'none'
						})
					}
				})
			},
			//添加/修改桌位
			addbooks(){
				if(!this.form.seatName){
					uni.showToast({
						title:this.$t('Table.plaseatName'),//'请输入桌位名称',
						icon:'none'
					})
					return
				}
				this.$Request.postA('/admin/shopSeat/insertSeat',this.form).then(res=>{
					if(res.code==0){
						uni.showToast({
							title:this.$t('index.tjok')//'提交成功'
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
		}
	}
</script>

<style lang="scss">
	.from {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.from-box {
			width: 686rpx;
			height: 100%;
			border-radius: 16rpx;
			background-color: #ffffff;
			padding: 0 40rpx;
		}
	}
	.submit{
		width: 100%;
		height: 130rpx;
		margin-top: 40rpx;
		.submit-box{
			width: 686rpx;
			height: 80rpx;
			background-color: #FF6D9D;
			color: #ffffff;
			border-radius: 16rpx;
		}
	}
</style>
