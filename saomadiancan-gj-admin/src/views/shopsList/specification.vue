<template>
	<div class="nf-page nf-shop-spec-page">
		<div class="nf-page-hero nf-shop-spec-hero">
			<a href="#" class="nf-shop-spec-back" @click.prevent="prev">
				<icon-svg name="jiantou" class="nf-shop-spec-back__icon"></icon-svg>
				返回
			</a>
			<span class="nf-page-badge">商品规格</span>
			<h2 class="nf-shop-spec-hero__title">门店商品规格</h2>
			<p class="nf-shop-spec-hero__desc">管理当前门店商品规格，支持多语言版本配置与规格的添加、修改、删除</p>
		</div>

		<div class="nf-filter-card nf-shop-spec-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-shop-spec-filter__body">
				<div class="nf-shop-spec-filter__item">
					<span class="nf-shop-spec-filter__label">语言版本：</span>
					<el-select v-model="languageTypeT" class="nf-shop-spec-filter__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
							:value="item.language">
						</el-option>
					</el-select>
				</div>
				<div class="nf-shop-spec-filter__actions">
					<el-button :disabled="!isAuth('shopSpecification:add')" icon="document" size="mini"
						type="primary" @click="addNotice">添加规格</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-shop-spec-table-card">
			<el-table v-loading="tableDataLoading" :data="typeDatas" row-key="id">
				<el-table-column prop="id" label="id" width="80">
				</el-table-column>
				<el-table-column prop="languageName" label="语言">
				</el-table-column>
				<el-table-column prop="ruleName" label="规格名称">
				</el-table-column>
				<el-table-column label="规格值">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.ruleValue" :key="index">
							<div>
								<span>{{ item.value }}:</span>
								<span>{{ item.detail }}</span>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" width="180">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('shopSpecification:update')"
							@click="updates(scope.row)">
							修改
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('shopSpecification:delete')"
							@click="deletes(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
		</div>

		<!-- 添加规格 -->
		<el-dialog title="添加规格" customClass="customWidth" :visible.sync="dialogFormVisible">
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言版本：</span>
				<el-radio-group v-model="languageType">
					<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
						style="margin: 5px;">{{item.remark}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 80px;display: inline-block;text-align: right;">规格标题：</span>
				<el-input style="width:200px" v-model="ruleName" placeholder="请输入标题名称 例:衣服"></el-input>
			</div>
			<div style="margin:40px 0 0 73px;">
				<div class="ruleitem" v-for="(item,index) in this.ruleValue" :key="index">
					<div>{{ item.value }} <span @click="deleterule(index)">
							<icon-svg name="shanchu" class="ruleicon"></icon-svg>
						</span></div>
					<div>
						<el-tag :key="tag" v-for="(tag,j) in item.detail.split(',')" closable
							:disable-transitions="false" @close="handleClose(tag,j,item)">
							{{ tag }}
						</el-tag>
						<el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput" size="small"
							style="width:120px;" placeholder="请输入属性名"
							@keyup.enter.native="handleInputConfirm(index,item)">
						</el-input>
						<el-button class="button-new-tag" size="small"
							@click="handleInputConfirm(index,item)">添加</el-button>
					</div>
				</div>
			</div>
			<div style="margin:30px 0 0 83px;">
				<div v-if="isshow" class="btn_specif">
					<el-button type="primary" @click="btnToclick()">添加新规格</el-button>
				</div>
			</div>
			<div style="margin:10px 0 0 0px;">
				<div v-if="!isshow" style="margin-top: 20px;" :visible.sync="dialogFormVisible">
					<div style="display: inline-block;">
						<span style="width: 80px;display: inline-block;text-align: right;">规格：</span>
						<el-input style="width:50%;" v-model="releobject.value" placeholder="例:颜色"></el-input>
					</div>
					<div style="display: inline-block;">
						<span style="width: 80px;display: inline-block;text-align: right;">规格值：</span>
						<el-input style="width:50%;" v-model="releobject.detail" placeholder="例:黑色"></el-input>
					</div>
					<div style="display: inline-block;">
						<el-button type="primary" @click="speciTo()">确 定</el-button>
						<el-button @click="speciTotwo()">取 消</el-button>
					</div>
				</div>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="specificationTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 修改规格 -->
		<el-dialog title="修改规格" customClass="customWidth" :visible.sync="dialogFormVisible1">
			<el-form :model="form">
				<el-form-item label="语言版本：">
					<el-radio-group v-model="languageType">
						<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
							style="margin: 5px;">{{item.remark}}</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="规格标题：">
					<el-input v-model="form.ruleName" style="width:200px;" placeholder="请输入分类名称"></el-input>
				</el-form-item>
				<el-form-item>
					<div style="margin-left:73px;">
						<div class="ruleitem" v-for="(item,index) in form.ruleValue" :key="index">
							<div>{{ item.value }} <span @click="deleterule1(index)">
									<icon-svg name="shanchu" class="ruleicon"></icon-svg>
								</span></div>
							<div>
								<el-tag :key="tag" v-for="(tag,j) in item.detail.split(',')" closable
									:disable-transitions="false" @close="handleClose1(tag,j,item)">
									{{ tag }}
								</el-tag>
								<el-input class="input-new-tag" v-model="inputValues[index]" ref="saveTagInput"
									size="small" style="width:120px;" placeholder="请输入属性名"
									@keyup.enter.native="handleInputConfirm1(index,item)">
								</el-input>
								<el-button class="button-new-tag" size="small"
									@click="handleInputConfirm(index,item)">添加</el-button>
							</div>
						</div>
					</div>
				</el-form-item>
				<el-form-item>
					<div style="margin-left:83px;">
						<div v-if="isshow" class="btn_specif">
							<el-button type="primary" @click="btnToclick1()">添加新规格</el-button>
						</div>
					</div>
					<div style="margin-top:10px;">
						<div v-if="!isshow" style="margin-top: 20px;" :visible.sync="dialogFormVisible">
							<div style="display: inline-block;">
								<span style="width: 100px;display: inline-block;text-align: right;">规格：</span>
								<el-input style="width:50%;" v-model="releobject.value" placeholder="请输入规格"></el-input>
							</div>
							<div style="display: inline-block;">
								<span style="width: 100px;display: inline-block;text-align: right;">规格值：</span>
								<el-input style="width:50%;" v-model="releobject.detail"
									placeholder="请输入规格值"></el-input>
							</div>
							<div style="display: inline-block;">
								<el-button type="primary" @click="speciTo1()">确 定</el-button>
								<el-button @click="speciTotwo1()">取 消</el-button>
							</div>
						</div>
					</div>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				size: 10,
				page: 1,
				ruleName: '',
				releobject: {
					value: '',
					detail: '',
				},
				detailarr: [],
				detsilshow: true,
				inputVisible: false,
				inputValues: {
					inputValue0: '',
					inputValue1: '',
					inputValue2: '',
					inputValue3: '',
					inputValue4: '',
					inputValue5: ''
				},
				ruleValue: [],
				dynamicTags: [],
				form: {
					id: '',
					ruleName: '',
					ruleValue: [],
					createTime: ''

				},
				isshow: true,
				formLabelWidth: '100px',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				typeDatas: [],
				shopId: '',
				languageTypeT: '',
				statesnum: [{ //语言分类
					classificationName: '全部',
					classificationId: ''
				}],
				languageType: '',
			}
		},
		methods: {
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			deleterule(index) {
				this.ruleValue.splice(index, 1)
			},
			btnToclick() {
				this.isshow = false
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTotwo() {
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTo() {
				if (this.ruleName == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入标题名称',
						type: 'warning'
					})
					return
				}
				if (this.releobject.value == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入规格',
						type: 'warning'
					})
					return
				}
				if (this.releobject.detail == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入规格值',
						type: 'warning'
					})
					return
				}

				this.ruleValue.push({
					value: this.releobject.value,
					detail: this.releobject.detail
				})
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			handleClose(tag, j, item) {
				var detailarr = item.detail.split(',')
				detailarr.splice(j, 1)
				if (detailarr.length < 1) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '规格值至少要有一个',
						type: 'warning'
					})
					return
				} else {
					item.detail = String(detailarr)
				}
			},
			handleInputConfirm(index, item) {
				let inputValue = this.inputValues[index]
				if (inputValue) {
					var detailarr = item.detail.split(',')
					detailarr.push(inputValue)
					for (var i = 0; i < detailarr.length; i++) {
						for (var j = 0; j < detailarr.length; j++) {
							if (detailarr[i] == detailarr[j] && i != j) {
								detailarr.splice(j, 1)
							}
						}
					}
					item.detail = String(detailarr)
					this.inputValues[index] = ''
				}
				this.inputVisible = false

			},
			// 添加规格弹框
			addNotice() {
				this.languageType = ''
				this.ruleName = ''
				this.ruleValue = []
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
				this.dialogFormVisible = true
			},
			// 添加规格
			specificationTo() {
				if (this.languageType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择语言版本',
						type: 'warning'
					})
					return
				}
				if (this.ruleName == '') {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请输入标题名称',
						type: 'warning'
					})
					return
				}
				if (this.ruleValue.length == 0) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '请至少添加一种规格',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('selfGoodsRule/save'),
					method: 'post',
					data: this.$http.adornData({
						'ruleName': this.ruleName,
						'ruleValue': this.ruleValue,
						'shopId': this.shopId,
						'languageType': this.languageType
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.ruleName = ''
							this.ruleValue = []
							this.dataSelect()
						}
					})
				})
			},
			// 修改弹框
			updates(rows) {
				this.dialogFormVisible1 = true
				var rowsValue = JSON.parse(JSON.stringify(rows));
				this.form.id = rowsValue.id
				this.form.ruleName = rowsValue.ruleName
				this.form.ruleValue = rowsValue.ruleValue
				this.form.createTime = rowsValue.createTime
				this.languageType = rowsValue.languageType
			},
			// 修改商品规格
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('selfGoodsRule/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'ruleName': this.form.ruleName,
						'ruleValue': this.form.ruleValue,
						'createTime': this.form.createTime,
						'shopId': this.shopId,
						'languageType': this.languageType
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible1 = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			btnToclick1() {
				this.isshow = false
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			speciTotwo1() {
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			deleterule1(index) {
				this.form.ruleValue.splice(index, 1)
			},
			speciTo1() {
				this.form.ruleValue.push({
					value: this.releobject.value,
					detail: this.releobject.detail
				})
				this.isshow = true
				this.releobject.value = ''
				this.releobject.detail = ''
			},
			handleClose1(tag, j, item) {
				var detailarr = item.detail.split(',')
				detailarr.splice(j, 1)
				if (detailarr.length < 1) {
					this.$message({
						title: '提示',
						type: 'error',
						duration: 1800,
						message: '规格值至少要有一个',
						type: 'warning'
					})
					return
				} else {
					item.detail = String(detailarr)
				}
			},
			handleInputConfirm1(index, item) {
				let inputValue = this.inputValues[index]
				if (inputValue) {
					var detailarr = item.detail.split(',')
					detailarr.push(inputValue)
					for (var i = 0; i < detailarr.length; i++) {
						for (var j = 0; j < detailarr.length; j++) {
							if (detailarr[i] == detailarr[j] && i != j) {
								detailarr.splice(j, 1)
							}
						}
					}
					item.detail = String(detailarr)
					this.inputValues[index] = ''
				}
				this.inputVisible = false
			},
			// 删除分类
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('selfGoodsRule/delete'),
						method: 'get',
						params: this.$http.adornParams({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 获取分类数据
			dataSelect() {
				let page = this.page - 1
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('selfGoodsRule/list'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.size,
						'shopId': this.shopId,
						'languageType': this.languageTypeT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.typeDatas = returnData
				})
			},
			// 返回上一级
			prev() {
				this.$router.back()
			},
			animeDat() {
				this.page = 1
				this.dataSelect()
			},

			// 语言类型
			dataSelectYy() {
				this.$http({
					url: this.$http.adornUrl('admin/languageType/getLanguageTypeList'),
					method: 'get',
					params: this.$http.adornParams({})
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
			}
		},
		mounted() {
			this.shopId = this.$route.query.shopId
			this.dataSelectYy()
		}
	}
</script>

<style scoped>
.nf-shop-spec-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-shop-spec-hero {
  margin-bottom: 12px;
}

.nf-shop-spec-back {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  text-decoration: none;
  margin-bottom: 8px;
}

.nf-shop-spec-back:hover {
  color: var(--nf-brand, #ffa8c2);
}

.nf-shop-spec-back__icon {
  width: 1.2em;
  height: 1.2em;
}

.nf-shop-spec-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-shop-spec-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-shop-spec-filter {
  margin-bottom: 12px;
}

.nf-shop-spec-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-shop-spec-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-shop-spec-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-shop-spec-filter__select {
  width: 150px;
}

.nf-shop-spec-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-shop-spec-table-card {
  margin-bottom: 0;
}
</style>

<style>
.customWidth {
	width: 60% !important;
}

.ruleitem {
	padding-left: 10px;
	margin-top: 15px;
}

.ruleitem .ruleicon {
	position: relative;
	top: 3px;
	left: 2px;
	width: 1.2em;
	height: 1.2em;
}

.el-tag+.el-tag {
	margin-left: 10px;
}

.button-new-tag {
	height: 32px;
	line-height: 31px;
	padding-top: 0;
	padding-bottom: 0;
	border-radius: 4px;
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
	background-color: #f5f7fa;
	position: relative;
	left: -5px;
	border-color: #dcdfe6;
	border: 1px solid #dcdfe6;
}

.input-new-tag {
	width: 90px;
	margin-left: 10px;
	vertical-align: bottom;
}

.el-input--small .el-input__inner {
	border-top-right-radius: 0;
	border-bottom-right-radius: 0;
}
</style>
