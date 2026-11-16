<template>
	<view>
		<!-- 收银台收入 -->
		<view class="money flex justify-center">
			<view class="money-box flex justify-center">
				<view class="money-box-con">
					<view class="money-box-title flex justify-between align-center">
						<view class="money-box-titles">
							{{$t('Cashier.mx')}}
						</view>
						<view class="money-box-titlephoto">
							<pie :number_value="bili" :PieStyle="PieStyle" />
						</view>
					</view>
					<view class="money-box-cz flex justify-between align-center">
						<view class="money-box-cz-time flex align-center">
							<u-icon name="calendar" color="#999999" style="margin-right: 10rpx;" size="28"></u-icon>
							<view class="" @click="selectTime('start')">
								{{startTime?startTime:$t('Cashier.kssj')}}
							</view>
							<text style="margin: 0 10rpx;">{{$t('Cashier.zhi')}}</text>
							<view class="" @click="selectTime('end')">
								{{endTime?endTime:$t('Cashier.jssj')}}
							</view>
							
						</view>
						<view class="money-box-cz-money flex align-center">
							{{$t('Cashier.sr')}}<text style="color: #FF6D9E;">{{$t('index.qian')}}{{money}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		<empty v-if="list.length==0" />
		<!-- 收入明细 -->
		<view v-if="list.length>0" class="moneyList flex justify-center">
			<view class="moneyList-box">
				<view class="moneyList-item" v-for="(item,index) in list" :key="index">
					<view class="moneyList-item-top flex justify-between align-center">
						{{$t('Cashier.sr')}}
						<text style="color: #FF6D9E;">{{$t('index.qian')}}{{item.money}}</text>
					</view>
					<view class="moneyList-item-bom">
						{{item.createTime}}
					</view>
				</view>
			</view>
		</view>
		<!-- 时间选择器 -->
		<u-picker v-model="show" :params="params" mode="time" @confirm="confirm" :cancelText="$t('index.qx')" :confirmText="$t('index.sure')"></u-picker>
	</view>
</template>

<script>
	import pie from '../../components/mushan-pie/mushan-pie.vue'
	import empty from '../../components/empty.vue'
	export default {
		components: {
			pie,
			empty
		},
		data() {
			return {
				bili:0.2,
				type:'start',//start:开始时间 end:结束时间
				show: false,
				params: {
					year: true,
					month: true,
					day: true,
					hour: false,
					minute: false,
					second: false
				},
				PieStyle: {
					circular: '100rpx', // 当前圆的大小
					animite: true, // 是否要有动画效果
					ring: 0, // 中间是空心的宽度，0是不显示，0.5是半圆大小
					color: '#FF6D9E', // 自定义饼图的颜色,
					numberShow: false, // 是否要显示圆中间的文本
					numberSize: '12px', // 中间文字大小默认是12px
					// numberColor:'#F71F7E',   // 中间文字字体颜色
					rotate: 0, // 是否需要旋转图形
				},
				startTime: '', //开始时间
				endTime: '', //结束时间
				list: [],
				shopId:'',
				page:1,
				limit:10,
				totals:1,
				zMoney:'',//总收入
				money:'',//筛选收入
			};
		},
		//加载更多
		onReachBottom() {
			if(this.page < this.totals){
				this.page += 1
				this.getList()
			}
		},
		onPullDownRefresh() {
			this.page = 1
			this.getList()
		},
		onLoad() {
			uni.setNavigationBarTitle({
				title:this.$t('shop.syt')
			})
			this.shopId = uni.getStorageSync('updataShopId')
			if(this.shopId){
				this.getList()
			}
		},
		methods:{
			//比较两个日期的大小
			getMaxOrMin(time1,time2){
				let odate1 = new Date(time1)
				let odate2 = new Date(time2)
				// alert(odate1+'-'+odate2)
				if(odate1.getTime() <= odate2.getTime()){
					return true
				}else{
					return false
				}
				
			},
			//获取收银明细
			getList(){
				let data = {
					shopId:this.shopId,
					startTime:this.startTime,
					endTime:this.endTime,
					page:this.page,
					limit:this.limit,
					type:1
				}
				this.$Request.getA('/Details/queryUserMoneyDetails',data).then(res=>{
					if(res.code == 0){
						if(!this.startTime){
							this.zMoney = res.data.money
							this.money = res.data.money
							this.bili = 1
						}else{
							this.money = res.data.money
							//求出百分比后需要转成number类型 否则默认为string类型 会报错
							this.bili = parseFloat((this.money / this.zMoney).toFixed(1))
						}
						this.totals = res.data.pageUtils.pages
						if(this.page==1){
							this.list = res.data.pageUtils.records
						}else{
							this.list = [...this.list,...res.data.pageUtils.records]
						}
					}else{
						uni.showToast({
							title:res.msg,
							icon:'none'
						})
					}
				})
			},
			//选择日期
			confirm(e){
				console.log(e)
				if(this.type=='start'){//开始时间
					this.startTime = e.year+'-'+e.month+'-'+e.day
					this.endTime = ''
				}else{//结束时间
					let endtime = e.year+'-'+e.month+'-'+e.day
					let rest = this.getMaxOrMin(this.startTime,endtime)
					if(rest){
						this.endTime = e.year+'-'+e.month+'-'+e.day
						this.getList()
					}else{
						uni.showToast({
							title:this.$t('Cashier.plakssj'),//'结束时间需大于开始时间，请重新选择',
							icon:'none'
						})
					}
					
				}
			},
			//选择日期类型
			selectTime(type){
				if(type=='end'){
					if(this.startTime){
						this.type = type
						this.show = true
					}else{
						uni.showToast({
							title:this.$t('Cashier.plakssjs'),//'请先选择开始时间',
							icon:'none'
						})
					}
				}else{
					this.type = type
					this.show = true
				}
				
			},
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #ffffff;
	}

	.money {
		width: 100%;
		height: 200rpx;
		box-shadow: 5rpx 5rpx 10rpx #cccccc;
		position: fixed;
		/* #ifdef H5 */
		top: 80rpx;
		/* #endif */
		/* #ifndef H5 */
		top: 0rpx;
		/* #endif */
		background-color: #ffffff;
		z-index: 888;
		.money-box {
			width: 686rpx;
			height: 100%;

			.money-box-con {
				width: 646rpx;
				height: 100%;
			}

			.money-box-title {
				margin-top: 10rpx;
			}

			.money-box-titles {
				font-size: 50rpx;
				font-weight: bold;
			}

			.money-box-titlephoto {
				// width: 120rpx;
				// height: 120rpx;
				border-radius: 50%;
				// background-color: #ee7c51;
			}

			.money-box-cz {
				width: 100%;
				margin-top: 20rpx;
			}

			.money-box-cz-time {
				color: #999999;
			}

			.money-box-cz-money {
				color: #999999;
			}
		}
	}

	.moneyList {
		width: 100%;
		margin-top: 200rpx;

		.moneyList-box {
			width: 686rpx;
			height: auto;

			.moneyList-item {
				width: 100%;
				border-bottom: 1rpx solid #cccccc;
				padding: 20rpx;
			}

			.moneyList-item-top {
				font-size: 32rpx;
			}

			.moneyList-item-bom {
				margin-top: 20rpx;
				color: #999999;
			}
		}

	}
</style>
