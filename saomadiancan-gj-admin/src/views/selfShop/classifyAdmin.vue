<template>
	<div class="nf-page nf-classify-page">
		<div class="nf-page-hero nf-classify-hero">
			<span class="nf-page-badge">商品分类</span>
			<h2 class="nf-classify-hero__title">商品分类管理</h2>
			<p class="nf-classify-hero__desc">管理自营商城商品分类，支持多语言版本配置与分类的添加、修改、删除</p>
		</div>

		<div class="nf-filter-card nf-classify-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-classify-filter__body">
				<div class="nf-classify-filter__item">
					<span class="nf-classify-filter__label">语言版本：</span>
					<el-select v-model="languageTypeT" class="nf-classify-filter__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
							:value="item.language">
						</el-option>
					</el-select>
				</div>
				<div class="nf-classify-filter__actions">
					<el-button :disabled="!isAuth('classifyAdmin:add')" icon="document" size="mini"
						type="primary" @click="addNotice">添加分类</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-classify-table-card">
			<el-table v-loading="tableDataLoading" :data="typeDatas" row-key="id">
				<el-table-column label="编号" prop="classifyId" width="100">
				</el-table-column>
				<el-table-column prop="languageName" label="语言">
				</el-table-column>
				<el-table-column label="名称" prop="classifyName">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button :disabled="!isAuth('classifyAdmin:update')" size="mini" type="primary"
							@click="updates(scope.$index, scope.row)">修改
						</el-button>
						<el-button :disabled="!isAuth('classifyAdmin:delete')" size="mini" type="danger"
							@click="deletes(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 添加分类 -->
		<el-dialog :visible.sync="dialogFormVisible" center :title="titles">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言版本：</span>
				<el-radio-group v-model="languageType">
					<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language" style="margin: 5px;">{{item.remark}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">分类名称：</span>
				<el-input v-model="classifyName" placeholder="请输入分类名称" style="width:50%;"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>

	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 5,
				page: 1,
				classifyId: '',
				classifyName: '',
				tableDataLoading: false,
				dialogFormVisible: false,
				typeDatas: [],
				titles: '添加',
				languageTypeT:'',
				statesnum: [{ //语言分类
					classificationName: '全部',
					classificationId: ''
				}],
				languageType:'',
			}
		},
		methods: {

			// 添加分类弹框
			addNotice() {
				this.languageType = ''
				this.dialogFormVisible = true
				this.titles = '添加'
				this.classifyId = ''
				this.classifyName = ''
				this.classSelect()
			},
			// 添加分类
			releasNoticeTo() {
				if (this.languageType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择语言版本',
						type: 'warning'
					})
					return
				}
				if (this.classifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入分类名称',
						type: 'warning'
					})
					return
				}
				if (this.titles == '添加') {
					var url = 'admin/goods/insertClassify'
				}
				if (this.titles == '修改') {
					var url = 'admin/goods/updateClassify'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'classifyId': this.classifyId,
						'classifyName': this.classifyName,
						'languageType':this.languageType,
						'shopId':0,
					})
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
				this.classifyId = rows.classifyId
				this.classifyName = rows.classifyName
				this.languageType = rows.languageType
				this.titles = '修改'
				this.dialogFormVisible = true

			},

			// 删除分类
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/goods/deleteClassify'),
						method: 'get',
						params: this.$http.adornParams({
							'classifyId': row.classifyId
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

			},
			// 获取分类数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/goods/selectAllClassify'),
					method: 'get',
					params: this.$http.adornParams({
						'languageType':this.languageTypeT,
						'shopId':0,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.typeDatas = returnData
				})
			},
			animeDat() {
				this.page = 1
				this.dataSelect()
			},
			
			// 语言类型
			dataSelectYy () {
				this.$http({
					url: this.$http.adornUrl('admin/languageType/getLanguageTypeList'),
					method: 'get',
					params: this.$http.adornParams({
					})
				}).then(({data}) => {
					if (data && data.code === 0) {
						let returnData = data.data
						this.statesnum = returnData.records
						this.languageTypeT = returnData.records[0].language
						this.dataSelect()
					}
				})
			}
		},
		mounted() {
			
			this.dataSelectYy()
		}
	}
</script>

<style scoped>
.nf-classify-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-classify-hero {
  margin-bottom: 12px;
}

.nf-classify-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-classify-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-classify-filter {
  margin-bottom: 12px;
}

.nf-classify-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-classify-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-classify-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-classify-filter__select {
  width: 150px;
}

.nf-classify-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-classify-table-card {
  margin-bottom: 0;
}
</style>
