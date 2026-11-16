<template>
	<div class="nf-page nf-shopslist-admin-page">
		<div class="nf-page-hero nf-shopslist-admin-hero">
			<a href="#" class="nf-shopslist-admin-back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-shopslist-admin-back__icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-badge">门店</span>
			<h2 class="nf-shopslist-admin-hero__title">商品列表</h2>
			<p class="nf-shopslist-admin-hero__desc">管理当前门店商品，支持按标题、类型、语言版本与状态筛选，可添加、修改库存与上下架</p>
		</div>

		<div class="nf-filter-card nf-shopslist-admin-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-shopslist-admin-filter__body">
				<div class="nf-shopslist-admin-filter__item">
					<span class="nf-shopslist-admin-filter__label">商品标题：</span>
					<el-input
						class="nf-shopslist-admin-filter__input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</div>
				<div class="nf-shopslist-admin-filter__item">
					<span class="nf-shopslist-admin-filter__label">商品类型语言版本：</span>
					<el-select v-model="languageTypeT" class="nf-shopslist-admin-filter__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
							:value="item.language">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopslist-admin-filter__item">
					<span class="nf-shopslist-admin-filter__label">商品类型：</span>
					<el-select v-model="classifyId" class="nf-shopslist-admin-filter__select nf-shopslist-admin-filter__select--wide" placeholder="请选择商品类型"
						@change="handleChange()">
						<el-option v-for="(item,index) in classDatas" :key="index" :label="item.classifyName"
							:value="item.classifyId">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopslist-admin-filter__item">
					<span class="nf-shopslist-admin-filter__label">商品状态：</span>
					<el-select v-model="status" class="nf-shopslist-admin-filter__select" placeholder="请选择商品状态" @change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopslist-admin-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary"
						icon="document" @click="addShop">添加商品</el-button>
					<el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary"
						icon="document" @click="toAddShop">新增商品</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-shopslist-admin-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="goodsId" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品封面图">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCover" alt="" class="nf-shopslist-admin-thumb">
					</template>
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品图" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.goodsPicture" :key="index"
							class="nf-shopslist-admin-picture-item">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="nf-shopslist-admin-picture-thumb" :src="item" alt="" slot="reference">
								<img class="nf-shopslist-admin-picture-preview" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="languageName" label="语言">
				</el-table-column>
				<el-table-column prop="goodsName" label="商品类型">
					<template slot-scope="scope">
						<span v-if="scope.row.classifyName == null">暂无分类</span>
						<span v-else>{{scope.row.classifyName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="goodsName" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="goodsDescribe" label="商品描述" width="300">
				</el-table-column>
				<el-table-column prop="inventory" label="库存" width="180">
					<template slot-scope="scope">
						<span>{{scope.row.inventory}}</span>
						<div class="nf-shopslist-admin-inventory-edit">
							<el-input class="nf-shopslist-admin-inventory-input" @keydown.enter.native="kcselect(scope.row,scope.row.kucun)"
								clearable placeholder="请输入修改库存" v-model="scope.row.kucun">
							</el-input>
							<span class="nf-shopslist-admin-inventory-icon" @click="kcselect(scope.row,scope.row.kucun)">
								<icon-svg name="bianji" class="site-sidebar__menu-icon"></icon-svg>
							</span>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="goodsMoney" label="商品售价">
					<template slot-scope="scope">
						<span>{{scope.row.goodsMoney | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="packMoney" label="打包费">
					<template slot-scope="scope">
						<span>{{scope.row.packMoney | numFilter}}/单</span>
					</template>
				</el-table-column>
				<el-table-column prop="goodsLabel" label="标签">
				</el-table-column>
				<el-table-column prop="status" label="商品状态">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.gsrStatus" @change="change(scope.row.gsrStatus,scope.row.goodsId)"
							:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949">
						</el-switch>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" v-if="scope.row.shopId==0" type="warning" disabled class="nf-shopslist-admin-op-btn">
							平台商品
						</el-button>
						<el-button size="mini" v-if="scope.row.shopId!=0" type="primary"
							:disabled="!isAuth('shopAdmin:update')" @click="shopUpdates(scope.row)" class="nf-shopslist-admin-op-btn">
							修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('shopAdmin:delete')"
							@click="shopdeletes(scope.row)" class="nf-shopslist-admin-op-btn">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-shopslist-admin-pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10,  20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog :visible.sync="dialogFormVisible" center title="商品列表">
			<div class="nf-shopslist-admin-dialog-filter">
				<span class="nf-shopslist-admin-dialog-filter__label">商品标题：</span>
				<el-input class="nf-shopslist-admin-dialog-filter__input" @keydown.enter.native="select" clearable placeholder="请输入商品标题"
					v-model="title1">
				</el-input>
			</div>
			<div class="nf-shopslist-admin-dialog-actions">
				<el-button size="mini" type="primary" icon="document" @click="select1">查询</el-button>
				<el-button size="mini" type="primary" icon="document" @click="cleans1">重置</el-button>
				<el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary"
					icon="document" @click="add(1)">批量添加</el-button>
			</div>
			<el-table v-loading="tableDataLoading1" :data="tableData1.list" @selection-change="handleSelectionChange">
				<el-table-column type="selection" width="55">
				</el-table-column>
				<el-table-column fixed prop="goodsId" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="goodsName" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="goodsCover" label="商品封面图">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCover" alt="" class="nf-shopslist-admin-thumb">
					</template>
				</el-table-column>
				<el-table-column prop="goodsCover" label="商品图" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.goodsPicture" :key="index"
							class="nf-shopslist-admin-picture-item">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="nf-shopslist-admin-picture-thumb" :src="item" alt="" slot="reference">
								<img class="nf-shopslist-admin-picture-preview" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="goodsName" label="商品类型">
					<template slot-scope="scope">
						<span v-if="scope.row.classifyName == null">暂无分类</span>
						<span v-else>{{scope.row.classifyName}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="goodsDescribe" label="商品描述" width="300">
				</el-table-column>
				<el-table-column prop="goodsMoney" label="商品售价">
					<template slot-scope="scope">
						<span>{{scope.row.goodsMoney | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="packMoney" label="打包费">
					<template slot-scope="scope">
						<span>{{scope.row.packMoney | numFilter}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="goodsLabel" label="标签">
				</el-table-column>
				<el-table-column prop="status" label="商品状态">
					<template slot-scope="scope">
						<span class="nf-shopslist-admin-status" v-if="scope.row.status === 0 ">上架</span>
						<span class="nf-shopslist-admin-status" v-if="scope.row.status === 1 ">下架</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:add')"
							@click="add(scope.row)">添加
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-shopslist-admin-pagination">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[10, 20, 30,50,100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData1.totalCount">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 0,
				closeValue: 1,
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				pagelist: [],
				id: '',
				img: '',
				title: '',
				title1: '',
				type: '',
				typeId: '',
				readonly: true,
				merchants: '',
				price: '',
				memberPrice: '',
				originalPrice: '',
				sales: '',
				status: '',
				status2: '',
				descrition: '',
				totalnum: 0,
				imgUrl: '',
				linkUrl: '',
				classifyId: '',
				classDatas: [],
				statusmain: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '上架'
					},
					{
						value: 1,
						label: '下架'
					}
				],
				statusmain2: [{
						value: 1,
						label: '上架'
					},
					{
						value: 2,
						label: '下架'
					}
				],
				formLabelWidth: '200px',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableDataLoading1: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: [],
				tableData1: [],
				shopId: '',
				xuanze: [],
				kucun: '',
				languageTypeT: '',
				statesnum: [{ //语言分类
					classificationName: '全部',
					classificationId: ''
				}],
				languageType: '',
			}
		},
		filters: {
			numFilter(value) {
				let realVal = ''
				if (!isNaN(value) && value !== '') {
					// 截取当前数据到小数点后三位
					let tempVal = parseFloat(value).toFixed(3)
					realVal = tempVal.substring(0, tempVal.length - 1)
				} else {
					realVal = '--'
				}
				return realVal
			}
		},
		methods: {
			handleChange(value) {
				this.shopSelect()
			},

			handleAvatarSuccess(file) {
				this.img = file.data;
			},
			handleSizeChange(val) {
				this.size = val;
				this.shopSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.shopSelect()
			},
			handleSizeChange1(val) {
				this.size1 = val;
				this.shopSelect1()
			},
			handleCurrentChange1(val) {
				this.page1 = val;
				this.shopSelect1()
			},
			//搜索
			select() {
				this.page = 1
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.title = ''
				this.status = ''
				this.classifyId = ''
				this.languageTypeT = this.statesnum[0].language
				this.shopSelect()
			},
			//搜索
			select1() {
				this.shopSelect1()
			},
			// 重置
			cleans1() {
				this.page = 1
				this.title1 = ''
				this.shopSelect1()
			},
			//添加商品
			addShop() {
				this.shopSelect1()
				this.dialogFormVisible = true
			},
			// 修改库存
			kcselect(row, kucun) {
				console.log('修改库存', row, kucun)
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/updateGoodsByShopId'),
					method: 'post',
					data: this.$http.adornData({
						'goodsId': row.goodsId,
						'shopId': this.shopId,
						'inventory': kucun
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
						this.xuanze = []
						this.dialogFormVisible = false
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
			// 选择商品
			handleSelectionChange(val) {
				console.log('选择商品', val)
				var arr = []

				if (val.length > 0) {
					for (var i in val) {
						var data = {}
						data.shopId = this.shopId
						data.goodsId = val[i].goodsId
						arr.push(data)
					}

				}
				this.xuanze = arr
			},
			add(row) {
				var goodsShopRelevancyList = []
				var datas = {}
				if (row == 1) {

					goodsShopRelevancyList = this.xuanze
					console.log('多个添加商品', goodsShopRelevancyList)
				} else {
					datas.shopId = this.shopId
					datas.goodsId = row.goodsId
					goodsShopRelevancyList.push(datas)
					console.log('单个添加商品', goodsShopRelevancyList)

				}
				if (goodsShopRelevancyList.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择商品',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/addGoodsByShopId'),
					method: 'post',
					data: this.$http.adornData(
						goodsShopRelevancyList)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '添加成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
						this.xuanze = []
						this.dialogFormVisible = false
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
			// 修改商品
			shopUpdates(rows) {
				this.$router.push({
					path: '/shopAmend',
					query: {
						goodsId: rows.goodsId,
						shopId:rows.shopId,
						languageType: rows.languageType
					}
				});
			},
			// 删除商品
			shopdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/goodsShop/deleteGoodsByShopId'),
						method: 'post',
						params: this.$http.adornParams({
							'shopId': this.shopId,
							'goodsId': row.goodsId
						})
					}).then(({
						data
					}) => {
						if(data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.shopSelect()
								}
							})
						}else{
							this.$message({
							  message: data.msg,
							  type: 'warning',
							  duration: 1500,
							  onClose: () => {
							  }
							})
						}
						
					})
				}).catch(() => {})
			},
			// 获取门店商品数据
			shopSelect() {
				let page = this.page
				this.tableDataLoading = true
				this.shopId = this.$route.query.shopId
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/selectGoodsByShopId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size,
						'goodsName': this.title,
						'classifyId': this.classifyId,
						'status': this.status,
						'shopId': this.shopId,
						'languageType': this.languageTypeT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].goodsPicture) {
							data.data.list[i].goodsPicture = data.data.list[i].goodsPicture.split(',')
						}

					}
					let returnData = data.data;

					this.tableData = returnData
					this.totalnum = returnData.totalElements
				})
			},
			// 获取添加的商品数据
			shopSelect1() {
				let page = this.page1
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/selectGoodsByNotShopId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size1,
						'goodsName': this.title1,
						'shopId': this.shopId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					for (var i in data.data.list) {
						if (data.data.list[i].goodsPicture) {
							data.data.list[i].goodsPicture = data.data.list[i].goodsPicture.split(',')
						}
					}
					let returnData = data.data;
					this.tableData1 = returnData
					for (var i in this.tableData1.list) {
						this.tableData1.list[i].kucun = ''
					}

					this.totalnum = returnData.totalElements
				})
			},
			// 商品分类
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/goods/selectAllClassify'),
					method: 'get',
					params: this.$http.adornParams({
						'languageType': this.languageTypeT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.classDatas = returnData
				})
			},
			// 返回上一级
			prev() {
				this.$router.go(-1)
			},
			// 状态
			change(status, goodsId) {
				this.$http({
					url: this.$http.adornUrl('admin/goodsShop/soldOutOrPutaway'),
					method: 'post',
					params: this.$http.adornParams({
						'shopId': this.shopId,
						'goodsId': goodsId,
						'status': status
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.shopSelect()
							}
						})
					}else{
							this.$message({
							  message: data.msg,
							  type: 'warning',
							  duration: 1500,
							  onClose: () => {
								  this.shopSelect()
							  }
							})
						}
					
				})
			},
			// 新增商品跳转
			toAddShop() {
				this.$router.push({
					path: '/shopPublish',
					query: {
						shopId: this.shopId
					}
				})
			},
			// 语言类型
			dataSelectYy() {
				this.$http({
					url: this.$http.adornUrl('admin/languageType/getLanguageTypeList'),
					method: 'get',
					params: this.$http.adornParams({
						
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						let returnData = data.data
						this.statesnum = returnData.records
						this.languageTypeT = returnData.records[0].language
						
						this.dataSelect()
					}
				})
			},
			// select选择事件
			animeDat() {
				this.classifyId = ''
				this.dataSelect()
			},
			
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.shopSelect()
			// this.dataSelect()
			this.dataSelectYy()
		},
		watch: {
			'$route': 'shopSelect'

		}
	};
</script>

<style scoped>
.nf-shopslist-admin-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-shopslist-admin-hero {
  margin-bottom: 12px;
}

.nf-shopslist-admin-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  text-decoration: none;
  margin-bottom: 8px;
}

.nf-shopslist-admin-back:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-shopslist-admin-back__icon {
  width: 1.2em;
  height: 1.2em;
}

.nf-shopslist-admin-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-shopslist-admin-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-shopslist-admin-filter {
  margin-bottom: 12px;
}

.nf-shopslist-admin-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-shopslist-admin-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-shopslist-admin-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-shopslist-admin-filter__input {
  width: 150px;
}

.nf-shopslist-admin-filter__select {
  width: 150px;
}

.nf-shopslist-admin-filter__select--wide {
  width: 200px;
}

.nf-shopslist-admin-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-shopslist-admin-table-card {
  margin-bottom: 0;
}

.nf-shopslist-admin-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-shopslist-admin-thumb {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-shopslist-admin-picture-item {
  display: inline-block;
  margin: 3px;
}

.nf-shopslist-admin-picture-thumb {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-shopslist-admin-picture-preview {
  width: 300px;
  height: auto;
}

.nf-shopslist-admin-inventory-edit {
  position: relative;
  display: inline-block;
}

.nf-shopslist-admin-inventory-input {
  width: 160px;
}

.nf-shopslist-admin-inventory-icon {
  position: absolute;
  right: 18px;
  top: 8px;
  cursor: pointer;
}

.nf-shopslist-admin-op-btn {
  margin: 3px;
}

.nf-shopslist-admin-status {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}

.nf-shopslist-admin-dialog-filter {
  margin: 12px 0;
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-shopslist-admin-dialog-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-shopslist-admin-dialog-filter__input {
  width: 150px;
}

.nf-shopslist-admin-dialog-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 12px;
}
</style>
