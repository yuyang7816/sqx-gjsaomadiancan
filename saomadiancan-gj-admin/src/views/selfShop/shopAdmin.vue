<template>
	<div class="nf-page nf-shopadmin-page">
		<div class="nf-page-hero nf-shopadmin-hero">
			<span class="nf-page-badge">商品</span>
			<h2 class="nf-shopadmin-hero__title">商品管理</h2>
			<p class="nf-shopadmin-hero__desc">管理自营商品信息，支持按标题、类型、语言版本与状态筛选，可发布、修改与删除商品</p>
		</div>

		<div class="nf-filter-card nf-shopadmin-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-shopadmin-filter__body">
				<div class="nf-shopadmin-filter__item">
					<span class="nf-shopadmin-filter__label">商品标题：</span>
					<el-input
						class="nf-shopadmin-filter__input"
						@keydown.enter.native="select"
						clearable
						placeholder="请输入商品标题"
						v-model="title">
					</el-input>
				</div>
				<div class="nf-shopadmin-filter__item">
					<span class="nf-shopadmin-filter__label">商品类型语言版本：</span>
					<el-select v-model="languageTypeT" class="nf-shopadmin-filter__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark" :value="item.language">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopadmin-filter__item">
					<span class="nf-shopadmin-filter__label">商品类型：</span>
					<el-select v-model="classifyId" class="nf-shopadmin-filter__select nf-shopadmin-filter__select--wide" placeholder="请选择商品类型"
						@change="handleChange()">
						<el-option v-for="(item,index) in classDatas" :key="index" :label="item.classifyName"
							:value="item.classifyId">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopadmin-filter__item">
					<span class="nf-shopadmin-filter__label">商品状态：</span>
					<el-select v-model="status" class="nf-shopadmin-filter__select" placeholder="请选择商品状态" @change="select(status)">
						<el-option v-for="item in statusmain" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shopadmin-filter__actions">
					<el-button size="mini" type="primary" icon="document" @click="select">查询</el-button>
					<el-button size="mini" type="primary" icon="document" @click="cleans">重置</el-button>
					<el-button :disabled="!isAuth('shopAdmin:add')" size="mini" type="primary"
						icon="document" @click="addShop">发布商品</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-shopadmin-table-card">
			<el-table v-loading="tableDataLoading" :data="tableData.list">
				<el-table-column fixed prop="goodsId" label="编号" width="80">
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品封面图">
					<template slot-scope="scope">
						<img :src="scope.row.goodsCover" alt="" class="nf-shopadmin-thumb">
					</template>
				</el-table-column>
				<el-table-column fixed prop="goodsCover" label="商品图" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.goodsPicture" :key="index"
							class="nf-shopadmin-picture-item">
							<el-popover placement="top-start" title="" trigger="hover">
								<img class="nf-shopadmin-picture-thumb" :src="item" alt="" slot="reference">
								<img class="nf-shopadmin-picture-preview" :src="item" alt="">
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
				<el-table-column prop="goodsName" label="商品标题" width="200">
				</el-table-column>
				<el-table-column prop="goodsDescribe" label="商品描述" width="300">
				</el-table-column>
				<el-table-column prop="goodsMoney" label="售价">
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
						<span class="nf-shopadmin-status" v-if="scope.row.status === 0 ">上架</span>
						<span class="nf-shopadmin-status" v-if="scope.row.status === 1 ">下架</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="150">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('shopAdmin:update')"
							@click="shopUpdates(scope.$index, scope.row)">修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('shopAdmin:delete')"
							@click="shopdeletes(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-shopadmin-pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10,  20, 30, 50, 100]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
				</el-pagination>
			</div>
		</div>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				pagelist: [],
				id: '',
				img: '',
				title: '',
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
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				typeDatas: [],
				advertData: [],
				tableData: [],
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
		computed: {
			// page() {
			//   this.pagelist = this.$store.state.common.pagelist;
			//   var page = 1;
			//   for (let val of this.pagelist) {
			//     if (val.router == "shopAdmin") {
			//       page = val.page;
			//     }
			//   }
			//   return page;
			// },
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
				// for (let item of this.pagelist) {
				//   if (item.router == "shopAdmin") {
				//     item.page = val;
				//   }
				// }
				// this.$store.commit("common/updataPage", this.pagelist);
				this.shopSelect()
			},
			//搜索
			select() {
				this.shopSelect()
			},
			// 重置
			cleans() {
				this.title = ''
				this.status = ''
				this.classifyId = ''
				this.languageTypeT = this.statesnum[0].language
				this.shopSelect()
			},
			//发布商品
			addShop() {
				this.$router.push({
					path: '/shopPublish',
					query: {
						shopId: 0
					}
				})
			},
			// 修改商品
			shopUpdates(index, rows) {
				this.$router.push({
					path: '/shopAmend',
					query: {
						goodsId: rows.goodsId,
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
						url: this.$http.adornUrl('admin/goods/deleteGoodsById'),
						method: 'post',
						params: this.$http.adornParams({
							'goodsId': row.goodsId
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
									this.shopSelect()
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
			},
			// 获取商品数据
			shopSelect() {
				let page = this.page
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/goods/selectGoodsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size,
						'goodsName': this.title,
						'classifyId': this.classifyId,
						'status': this.status,
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
			// 商品分类
			dataSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/goods/selectAllClassify'),
					method: 'get',
					params: this.$http.adornParams({
						'languageType': this.languageTypeT,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.classDatas = returnData

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
						this.shopSelect()
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
			this.shopSelect()
			this.dataSelectYy()

		}
	};
</script>

<style scoped>
.nf-shopadmin-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-shopadmin-hero {
  margin-bottom: 12px;
}

.nf-shopadmin-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-shopadmin-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-shopadmin-filter {
  margin-bottom: 12px;
}

.nf-shopadmin-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-shopadmin-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-shopadmin-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-shopadmin-filter__input {
  width: 150px;
}

.nf-shopadmin-filter__select {
  width: 150px;
}

.nf-shopadmin-filter__select--wide {
  width: 200px;
}

.nf-shopadmin-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-shopadmin-table-card {
  margin-bottom: 0;
}

.nf-shopadmin-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-shopadmin-thumb {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-shopadmin-picture-item {
  display: inline-block;
  margin: 3px;
}

.nf-shopadmin-picture-thumb {
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 4px;
}

.nf-shopadmin-picture-preview {
  width: 300px;
  height: auto;
}

.nf-shopadmin-status {
  color: var(--nf-brand, #ffa8c2);
  cursor: pointer;
}
</style>
