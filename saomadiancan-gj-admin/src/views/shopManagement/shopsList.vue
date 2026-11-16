<template>
	<div class="nf-page nf-shopslistsm-page">
		<div class="nf-page-hero nf-shopslistsm-hero">
			<span class="nf-page-badge">门店</span>
			<h2 class="nf-shopslistsm-hero__title">门店中心</h2>
			<p class="nf-shopslistsm-hero__desc">管理当前门店信息，支持修改门店资料、查看店铺二维码与收款码及营业状态</p>
		</div>

		<div class="nf-filter-card nf-shopslistsm-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-s-operation"></i>
				<span>快捷操作</span>
			</div>
			<div class="nf-filter-card__body nf-shopslistsm-filter__body">
				<div class="nf-shopslistsm-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="cleans2">刷新</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-shopslistsm-table-card">
		<el-table v-loading="tableDataLoading" :data="typeDatas.list" row-key="id" fixed="left">
			<el-table-column label="编号" prop="shopId" width="80" fixed="left">
			</el-table-column>
			<el-table-column label="名称" prop="shopName">
			</el-table-column>
			<el-table-column prop="goodsCover" label="轮播图" width="150">
				<template slot-scope="scope">
					<!-- <img :src="scope.row.goodsCover" alt="" width="60" height="60"> -->
					<div v-for="(item,index) in scope.row.shopBanner" :key="index"
						class="nf-shopslistsm-banner-item">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
							<img style="width: 300px; height: auto" :src="item" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column label="开店时间" prop="openTime">
			</el-table-column>
			<el-table-column label="闭店时间" prop="closeTime">
			</el-table-column>
			<el-table-column label="打印机类型" prop="facility">
				<template slot-scope="scope">
					<span v-if="scope.row.facility==1">飞鹅</span>
				</template>
			</el-table-column>
			<el-table-column label="设备sn编码" prop="snCode">
			</el-table-column>
			<el-table-column label="设备key秘钥" prop="value">
			</el-table-column>
			<el-table-column label="地址" prop="detailedAddress" width="200">
			</el-table-column>
			<el-table-column label="经度" prop="shopLng">
			</el-table-column>
			<el-table-column label="纬度" prop="shopLat">
			</el-table-column>
			<el-table-column label="营业状态" prop="putawayFlag" fixed="right">
				<template slot-scope="scope">
					<span>{{scope.row.putawayFlag==1?'上班啦':'打烊啦'}}</span>
				</template>
			</el-table-column>
			<el-table-column label="店铺二维码" width="100">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" class="nf-shopslistsm-op-btn" @click="erweima(scope.row)">查看
					</el-button>
				</template>
			</el-table-column>
			<el-table-column label="操作" width="100" fixed="right">
				<template slot-scope="scope">
					<div class="nf-shopslistsm-op-col">
					<!-- <el-button size="mini" type="primary" @click="shopsListAdmin(scope.row)" style="margin: 5px;">商品列表
          </el-button> -->

					<el-button :disabled="!isAuth('shopsListSm:update')" size="mini" type="primary"
						class="nf-shopslistsm-op-btn" @click="updates(scope.$index, scope.row)">修改
					</el-button>
					<el-button size="mini" type="warning" class="nf-shopslistsm-op-btn" @click="shopMoney(scope.row)">收款码
					</el-button>
					<!-- <el-button size="mini" type="primary" @click="shopsFenlei(scope.row)" style="margin: 5px;">商品分类
					</el-button>
					<el-button size="mini" type="warning" @click="shopsGuige(scope.row)" style="margin: 5px;">规格
					</el-button>
          <el-button size="mini" type="primary" @click="shopsListOrder(scope.row)" style="margin: 5px;">订单列表
          </el-button> -->
					<!-- <el-button :disabled="!isAuth('shopsListSm:delete')" size="mini" type="danger" @click="deletes(scope.row)" style="margin: 5px;">删除
          </el-button> -->
					<!-- <el-button size="mini" type="warning" @click="shopsZhuoma(scope.row)" style="margin: 5px;">桌码
		  </el-button>
          <el-button size="mini" type="primary" @click="shopsListSr(scope.row)" style="margin: 5px;">收入统计
          </el-button> -->
					</div>
				</template>
			</el-table-column>
		</el-table>
		<div class="nf-shopslistsm-pagination">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10,  20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="typeDatas.totalCount">
			</el-pagination>
		</div>
		</div>
		<!-- 添加分类 -->
		<el-dialog :visible.sync="dialogFormVisible" center :title="titles">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺名称：</span>
				<el-input v-model="shopName" placeholder="请输入店铺名称" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">轮播图：</span>
				<div class="imgs" v-for="(item,index) in img" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="dels(index)"></i>
					</span>
				</div>
				<div class="imgs" style="width: 50%;">
					<el-upload :action="$http.adornUrlMsg('alioss/upload')" list-type="picture-card"
						:show-file-list="false" :on-success="handleRemove" :on-progress="onprogress1">
						<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
							:percentage="percentage1"></el-progress>
						<i v-else class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="dialogImageUrl" alt="">
					</el-dialog>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">开店时间：</span>
				<!-- <el-input v-model="openTime" placeholder="请输入排序号" type="data" min="0" style="width:50%;"></el-input> -->
				<el-time-select v-model="openTime" :picker-options="{
            start: '00:00',
            step: '00:15',
            end: '24:00'
          }" placeholder="选择开店时间" style="width:50%;">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">闭店时间：</span>
				<!-- <el-input v-model="closeTime" placeholder="请输入排序号" type="data" min="0" style="width:50%;"></el-input> -->
				<el-time-select v-model="closeTime" :picker-options="{
            start: '00:00',
            step: '00:15',
            end: '24:00'
          }" placeholder="选择开店时间" style="width:50%;">
				</el-time-select>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">打印机类型：</span>
				<el-radio-group v-model="facility">
					<el-radio :label="1">飞鹅</el-radio>
				</el-radio-group>
				<div style="font-size: 8px;color: red;margin-left: 200px;">* 一个设备只能关联一个商铺</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">设备sn编码：</span>
				<el-input v-model="snCode" placeholder="请输入设备sn编码" type="text" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">设备key秘钥：</span>
				<el-input v-model="value" placeholder="请输入设备key秘钥" type="text" style="width:50%;"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">营业状态：</span>
				<el-radio-group v-model="putawayFlag">
					<el-radio :label="1">上班啦</el-radio>
					<el-radio :label="2">打烊啦</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">店铺地址：</span>
				<el-input v-model="detailedAddress" placeholder="请输入店铺地址" type="text" style="width:50%;"></el-input>
				<el-button type="primary" @click="phoneSelect()" plain>获取经纬度</el-button>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">纬度：</span>
				<el-input v-model="latitude" style="width:50%;" placeholder="请输入纬度" disabled></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">经度：</span>
				<el-input v-model="longitude" style="width:50%;" placeholder="请输入经度" disabled></el-input>
			</div>
			<!-- <div>
        <div id="container1" style="width:80%;height:500px;margin-left: 10%;"></div>
      </div> -->
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 店铺二维码 -->
		<el-dialog :visible.sync="dialogFormVisible2" center title="店铺二维码">
			<div>
				<img :src="erweimaImg" style="width: 50%;margin-left: 25%;" />
				<div style="text-align: center;margin-top: 20px;">
					<el-button type="primary" @click="chakan()">打开窗口查看</el-button>
				</div>

			</div>
		</el-dialog>
		<!-- 店铺收款码 -->
		<el-dialog :visible.sync="showMoney" center title="店铺收款码" @close="closeerweima">
			<div>
				<img :src="moenyMa" style="width: 50%;margin-left: 25%;" />
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		jsonp
	} from 'vue-jsonp'
	var geocoder, map, markersArray = [];
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				shopId: '',
				shopName: '',
				openTime: '',
				closeTime: '',
				detailedAddress: '',
				shopLng: '',
				shopLat: '',

				img: '',
				classn: '',
				value: '',
				parentId: 0,
				parentIdnum: '',

				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				typeDatas: [],
				titles: '添加',
				// 地图相关
				longitude: '', //维度
				latitude: '', //经度
				loadingMap: true,
				BMap: '',
				map: '',
				geoc: '',
				keyword: '',
				showMap: false,
				pointLngLat: '',
				center: {
					lng: 109.45744048529967,
					lat: 36.49771311230842
				},
				storeAddress: [],
				storeAddre: '请选择城市',
				form: {
					province: '',
					city: '', //市
					district: '', //区
				},
				percentage1: 0, //进度条
				img: [], //任务轮播图
				dialogImageUrl: [],
				dialogVisible: false,
				erweimaImg: '',
				facility: 1,
				shopUserId: '',
				shopUserName: '',
				shopPhone: '', //商户关联手机号
				snCode: '',
				putawayFlag: 1,
				moenyMa: '', //收款码
				showMoney: false, //显示收款码弹窗
			}
		},
		methods: {
			//关闭收款码弹窗时清空图片
			closeerweima() {
				this.moenyMa = ''
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			// 图标上传
			handleAvatarSuccess(file) {
				this.img = file.data
			},
			// 添加分类弹框
			addNotice() {
				this.titles = '添加'
				this.shopId = ''
				this.shopName = ''
				this.openTime = ''
				this.closeTime = ''
				this.dialogImageUrl = []
				this.img = []
				this.detailedAddress = ''
				this.shopLng = ''
				this.shopLat = ''
				this.longitude = '' //维度
				this.latitude = '' //经度
				this.facility = 1
				this.snCode = ''
				this.value = ''
				this.putawayFlag = 1
				this.dialogFormVisible = true
				this.getMyLocation();
			},
			// 添加分类
			releasNoticeTo() {
				if (this.shopName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入店铺名称',
						type: 'warning'
					})
					return
				}
				if (this.openTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择开店时间',
						type: 'warning'
					})
					return
				}
				if (this.closeTime == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择闭店时间',
						type: 'warning'
					})
					return
				}
				if (this.detailedAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入店铺地址',
						type: 'warning'
					})
					return
				}
				if (this.longitude == '' || this.latitude == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请在地图上定位获取经纬度',
						type: 'warning'
					})
					return
				}
				var datas = {}
				datas.shopName = this.shopName
				datas.shopBanner = this.img.toString()
				datas.openTime = this.openTime
				datas.closeTime = this.closeTime
				datas.detailedAddress = this.detailedAddress
				datas.shopLng = this.longitude
				datas.shopLat = this.latitude
				datas.facility = this.facility
				datas.snCode = this.snCode
				datas.value = this.value
				datas.putawayFlag = this.putawayFlag

				if (this.titles == '添加') {
					var url = 'admin/goods/insertGoodsShop'

				}
				if (this.titles == '修改') {
					var url = 'admin/goods/updateGoodsShop'
					datas.shopId = this.shopId
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			// 修改弹框
			updates(index, rows) {
				this.titles = '修改'
				this.shopId = rows.shopId
				this.shopName = rows.shopName
				this.openTime = rows.openTime
				this.closeTime = rows.closeTime
				if (rows.shopBanner) {
					this.dialogImageUrl = rows.shopBanner
					this.img = rows.shopBanner
				}
				this.detailedAddress = rows.detailedAddress
				this.shopLng = rows.shopLng
				this.shopLat = rows.shopLat
				this.longitude = rows.shopLng //维度
				this.latitude = rows.shopLat //经度
				if (rows.facility) {
					this.facility = rows.facility
				}
				this.snCode = rows.snCode
				this.value = rows.value
				if (rows.putawayFlag) {
					this.putawayFlag = rows.putawayFlag
				}
				this.getMyLocation();
				this.dialogFormVisible = true

			},
			// 查看二维码
			erweima(row) {
				this.dialogFormVisible2 = true
				this.erweimaImg = this.$http.adornUrl('invite/shopQr?shopId=' + row.shopId)
			},
			chakan() {

				window.open(this.erweimaImg, '_blank');
			},
			// 删除分类
			deletes(row) {
				// if (row.children == null || row.children.length == 0) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/goods/deleteGoodsShop'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.shopId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						} else {
							this.$message({
								message: data.msg,
								type: 'warning',
								duration: 1500,
								onClose: () => {}
							})
						}

					})
				}).catch(() => {})
				// } else {
				//   this.$message({
				//     message: '请先删除',
				//     type: 'error',
				//     duration: 1500,
				//   })
				//   return
				// }
			},
			// 商品类别
			shopsListAdmin(row) {
				this.$router.push({
					path: '/shopsListAdminSm',
					query: {
						shopId: row.shopId
					}
				})
			},
			// 订单列表
			shopsListOrder(row) {
				this.$router.push({
					path: '/shopsOrderSm',
					query: {
						shopId: row.shopId
					}
				})
			},
			// 收入统计
			shopsListSr(row) {
				this.$router.push({
					path: '/shopDatasSm',
					query: {
						shopId: row.shopId
					}
				})
			},
			// 获取分类数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/goods/selectAllShop'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'shopName': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].shopBanner) {
							data.data.list[i].shopBanner = data.data.list[i].shopBanner.split(',')
						}

					}
					let returnData = data.data
					this.typeDatas = returnData
				})
			},
			//定位获得当前位置信息
			getMyLocation() {
				var geolocation = new qq.maps.Geolocation("ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75", "来点小收入");
				geolocation.getIpLocation(this.showPosition, this.showErr);
				// geolocation.getLocation(this.showPosition, this.showErr);//或者用getLocation精确度比较高
			},
			showPosition(position) {
				console.log(position);
				// this.latitude = position.lat;
				// this.longitude = position.lng;
				// this.city = position.city;
				this.setMap();
			},
			showErr(e) {
				console.log("定位失败", e);
				this.getMyLocation(); //定位失败再请求定位，测试使用
			},
			//位置信息在地图上展示
			setMap() {
				//步骤：定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(this.latitude, this.longitude);
				//定义工厂模式函数
				var myOptions = {
					zoom: 13, //设置地图缩放级别
					center: myLatlng, //设置中心点样式
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				}
				// //获取dom元素添加地图信息
				var map = new qq.maps.Map(document.getElementById("container1"), myOptions);
				//给地图添加点击事件

				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				// `````````````
				var that = this;
				if (that.longitude == '') {
					var center = new qq.maps.LatLng(34.34281541842994, 108.93970884382725);
				} else {
					var center = new qq.maps.LatLng(that.latitude, that.longitude);
				}

				var map = new qq.maps.Map(document.getElementById("container1"), {
					center: center,
					zoom: 13
				});
				var marker = new qq.maps.Marker({
					position: center,
					map: map
				});
				var latlngBounds = new qq.maps.LatLngBounds();
				qq.maps.event.addListener(map, "click", function(event) {
					console.log(event, qq.maps);
					that.longitude = event.latLng.getLng(); // 经度
					that.latitude = event.latLng.getLat(); // 纬度
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng
						.getLng() + '&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						console.log('response', response, response.result.formatted_addresses.recommend)
						that.detailedAddress = response.result.formatted_addresses.recommend

					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});
					if (markersArray) {
						for (let i in markersArray) {
							markersArray[i].setMap(null);
						}
					}
					var marker = new qq.maps.Marker({
						map: map,
						position: event.latLng
					});
					markersArray.push(marker);

				});
				geocoder = new qq.maps.Geocoder({
					complete: function(result) {
						console.log(result);
						that.longitude = result.detail.location.lng;
						that.latitude = result.detail.location.lat;
						map.setCenter(result.detail.location);
						var marker = new qq.maps.Marker({
							map: map,
							position: result.detail.location
						});
						markersArray.push(marker);
					}
				});
			},
			// 地图定位
			select() {
				console.log(this.detailedAddress)
				// this.address = this.province+ this.city+this.district+this.address
				var addr = this.province + this.city + this.district + this.address
				let that = this
				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address==' + addr +
					'&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.longitude = response.result.location.lng; // 经度
					that.latitude = response.result.location.lat; // 纬度
					// that.address = response.result.title
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			onprogress1(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage1 = parseInt(event.percent)
			},
			// 轮播图片上传
			handleRemove(file) {
				console.log(file, this.img)
				this.img.push(file.data);
			},
			handleDownload(file) {

				console.log(file, this.img)
			},
			// 删除轮播图
			dels(index) {
				this.img.splice(index, 1);
				console.log(this.img)
			},
			cleans2() {
				this.dataSelect()
			},
			// 桌码
			shopsZhuoma(row) {
				this.$router.push({
					path: '/tableCode',
					query: {
						shopId: row.shopId
					}
				})
			},
			// 商品分类
			shopsFenlei(row) {
				this.$router.push({
					path: '/shopClassifyAdmin',
					query: {
						shopId: row.shopId
					}
				})
			},
			// 规格
			shopsGuige(row) {
				this.$router.push({
					path: '/shopSpecification',
					query: {
						shopId: row.shopId
					}
				})
			},
			//门店收款码
			shopMoney(row) {
				this.$http({
					url: this.$http.adornUrl('invite/shopPaySeatQr'),
					method: 'get',
					params: this.$http.adornParams({
						'shopId': row.shopId,
					})
				}).then(({
					data
				}) => {
					if (data) {
						this.moenyMa = data
						this.showMoney = true
						console.log(data, '获取二维码')
					} else {
						this.$message({
							message: '生成二维码失败，请重试',
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}
				})
			},
			// 谷歌地图获取经纬度
			getCoordinates() {
				// if (this.city == '') {
				// 	this.$notify({
				// 		title: '提示',
				// 		duration: 1800,
				// 		message: '城市不能为空',
				// 		type: 'warning'
				// 	})
				// 	return
				// }
				if (this.detailedAddress == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '地址不能为空',
						type: 'warning'
					})
					return
				}
				var addr = this.detailedAddress
				const geocoder = new google.maps.Geocoder();
				geocoder.geocode({
					address: addr
				}, (results, status) => {
					if (status === google.maps.GeocoderStatus.OK) {
						this.longitude = results[0].geometry.location.lng(); // 经度
						this.latitude = results[0].geometry.location.lat() // 纬度
						// that.address = response.result.title
						// this.coordinates = {
						//   latitude: results[0].geometry.location.lat(),
						//   longitude: results[0].geometry.location.lng()
						// };
					} else {
						console.error('Geocode was not successful for the following reason: ' + status);
					}
				});
			},
			phoneSelect() {
				this.getCoordinates()
			},
		},
		mounted() {
			this.dataSelect()

		},
		watch: {
			'$route': 'dataSelect'
		}
	}
</script>

<style scoped>
.nf-shopslistsm-page {
	padding: 0;
	overflow-x: hidden;
}

.nf-shopslistsm-hero {
	margin-bottom: 12px;
}

.nf-shopslistsm-hero__title {
	margin: 8px 0 4px;
	font-size: 18px;
	font-weight: 600;
	color: var(--nf-text-primary, #1a2433);
	line-height: 1.4;
}

.nf-shopslistsm-hero__desc {
	margin: 0;
	font-size: 13px;
	color: var(--nf-text-secondary, #909399);
	line-height: 1.5;
}

.nf-shopslistsm-filter {
	margin-bottom: 12px;
}

.nf-shopslistsm-filter__body {
	display: flex;
	flex-wrap: wrap;
	align-items: center;
	gap: 12px;
}

.nf-shopslistsm-filter__actions {
	display: flex;
	flex-wrap: wrap;
	gap: 8px;
	margin-left: auto;
}

.nf-shopslistsm-table-card {
	margin-bottom: 0;
	overflow-x: auto;
}

.nf-shopslistsm-op-col {
	display: flex;
	flex-wrap: wrap;
	gap: 4px;
}

.nf-shopslistsm-pagination {
	display: flex;
	justify-content: flex-end;
	margin-top: 12px;
}

.nf-shopslistsm-banner-item {
	display: inline-block;
	margin: 3px;
}

.nf-shopslistsm-op-btn {
	margin: 0;
}

.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}
</style>