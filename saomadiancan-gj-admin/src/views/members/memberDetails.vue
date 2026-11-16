<template>
	<div class="nf-page nf-memberdetails-page">
		<div class="nf-page-hero nf-memberdetails-hero">
			<span class="nf-page-badge">会员</span>
			<h2 class="nf-memberdetails-hero__title">会员配置</h2>
			<p class="nf-memberdetails-hero__desc">管理各语言版本的会员套餐，支持月/季/年会员类型的新增、编辑与删除</p>
		</div>

		<div class="nf-filter-card nf-memberdetails-filter">
			<div class="nf-filter-card__head">
				<i class="el-icon-search"></i>
				<span>筛选条件</span>
			</div>
			<div class="nf-filter-card__body nf-memberdetails-filter__body">
				<div class="nf-memberdetails-filter__item">
					<span class="nf-memberdetails-filter__label">语言版本：</span>
					<el-select v-model="languageTypeT" class="nf-memberdetails-filter__select" @change="animeDat()">
						<el-option v-for="item in statesnum" :key="item.language" :label="item.remark"
							:value="item.language">
						</el-option>
					</el-select>
				</div>
				<div class="nf-memberdetails-filter__actions">
					<el-button size="mini" type="primary" :disabled="!isAuth('memberDetails:add')" icon="document"
						@click="addNotice">添加会员
					</el-button>
				</div>
			</div>
		</div>

		<div class="nf-table-card nf-memberdetails-table-card">
			<el-table v-loading="tableDataLoading" :data="configData.list">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="languageName" label="语言">
				</el-table-column>
				<el-table-column prop="vipName" label="会员名称">
				</el-table-column>
				<el-table-column prop="money" label="会员价格">
				</el-table-column>
				<el-table-column prop="vipNameType" label="会员等级" width="150">
					<template slot-scope="scope">
						<span class="nf-memberdetails-type-tag" v-if="scope.row.vipNameType == 0">月</span>
						<span class="nf-memberdetails-type-tag" v-if="scope.row.vipNameType == 1">季</span>
						<span class="nf-memberdetails-type-tag" v-if="scope.row.vipNameType == 2">年</span>
					</template>
				</el-table-column>
				<el-table-column label="操作" fixed='right' width="200">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" :disabled="!isAuth('memberDetails:update')"
							@click="amendone(scope.$index, scope.row)">编辑
						</el-button>
						<el-button size="mini" type="danger" :disabled="!isAuth('memberDetails:delete')"
							@click="deletes(scope.row)">
							删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div class="nf-memberdetails-pagination">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[5, 10, 15, 20]" :page-size="size" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="configData.totalCount">
				</el-pagination>
			</div>
		</div>

		<el-dialog title="添加会员" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">语言版本：</span>
				<el-radio-group v-model="languageType">
					<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
						style="margin: 5px;">{{item.remark}}</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">会员类型：</span>
				<el-radio-group v-model="vipNameType">
					<el-radio :label="0">月会员</el-radio>
					<el-radio :label="1">季会员</el-radio>
					<el-radio :label="2">年会员</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">会员名称：</span>
				<el-input style="width:50%;" v-model="vipName" placeholder="请输入会员名称"></el-input>
			</div>
			<div style="margin-bottom: 20px;">
				<span style="width: 200px;display: inline-block;text-align: right;">会员价格：</span>
				<el-input style="width:50%;" v-model="money" type="number" placeholder="请输入会员价格" min="0"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="编辑会员配置" :visible.sync="dialogFormVisible1" center>
			<el-form :model="form">
				<el-form-item label="语言版本：" :label-width="formLabelWidth">
					<el-radio-group v-model="languageType">
						<el-radio v-for="(item,index) in statesnum" :key="item.language" :label="item.language"
							style="margin: 5px;">{{item.remark}}</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="是否有专属标识：" :label-width="formLabelWidth">
					<el-radio-group v-model="form.vipNameType">
						<el-radio :label="0">月会员</el-radio>
						<el-radio :label="1">季会员</el-radio>
						<el-radio :label="2">年会员</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item label="会员名称：" :label-width="formLabelWidth">
					<el-input v-model="form.vipName" style="width:65%;"></el-input>
				</el-form-item>
				<el-form-item label="会员价格：" :label-width="formLabelWidth">
					<el-input v-model="form.money" type="number" style="width:65%;" min="0"
						onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible1 = false">取 消</el-button>
				<el-button type="primary" @click="amendoneNoticeTo()">确 定</el-button>
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
				totalnum: 0,
				totalnum2: 0,
				ids: '',
				content: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				memberGrade: '',
				vipName: '',
				money: '',
				award: '',
				vipNameType: 0,
				formLabelWidth: '200px',
				checkBoxData: [],
				tableData: [],
				configData: {},
				form: {
					id: '',
					vipName: '',
					money: 0.00,
					award: 0.00,
					vipNameType: 0,
				},
				form2: {
					picture: '',
				},
				languageTypeT: '',
				statesnum: [{
					classificationName: '全部',
					classificationId: ''
				}],
				languageType: '',
			}
		},
		methods: {
			changeInput() {
				var pattern = /^[1-9][1-9]*$/
				if (!pattern.test(this.memberGrade)) {
					this.memberGrade = ''
				}
			},
			deletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('vipDetails/deleteVipDetails'),
						method: 'post',
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
			handleAvatarSuccess1(file) {
				this.memberIcon = file.data;
			},
			handleAvatarSuccess2(file) {
				this.memberImg = file.data;
			},
			handleAvatarSuccess3(file) {
				this.form.memberIcon = file.data;
			},
			handleAvatarSuccess4(file) {
				this.form.memberImg = file.data;
			},
			handleSizeChange(val) {
				this.size = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			lookMember(rows) {
				this.dialogFormVisible2 = true
				this.form2.picture = rows.picture
			},
			selectTrigger() {
				this.page = 1
				this.size = 5
				this.dataSelect()
			},
			addNotice() {
				this.languageType = ''
				this.vipName = ''
				this.money = ''
				this.award = ''
				this.vipNameType = 0
				this.dialogFormVisible = true
			},
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
				if (this.vipName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员名称',
						type: 'warning'
					});
					return
				}

				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员价格',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('vipDetails/insertVipDetails'),
					method: 'post',
					data: this.$http.adornData({
						'vipName': this.vipName,
						'money': this.money,
						'vipNameType': this.vipNameType,
						'languageType':this.languageType,
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

							this.dataSelect()
						}
					})
				})
			},
			amendone(index, rows) {
				this.dialogFormVisible1 = true
				this.form.id = rows.id
				this.form.vipName = rows.vipName
				this.form.money = rows.money
				this.form.vipNameType = rows.vipNameType
				this.languageType = rows.languageType

			},
			amendoneNoticeTo() {
				if (this.languageType == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择语言版本',
						type: 'warning'
					})
					return
				}
				if (this.form.vipName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员名称',
						type: 'warning'
					});
					return
				}
				if (this.form.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入会员价格',
						type: 'warning'
					});
					return
				}

				this.$http({
					url: this.$http.adornUrl('vipDetails/updateVipDetails'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'vipName': this.form.vipName,
						'money': this.form.money,
						'vipNameType': this.form.vipNameType,
						'languageType': this.languageType
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.dialogFormVisible1 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					} else {
						this.$message.error(data.msg)
					}
				})
			},
			dataSelect() {
				this.tableDataLoading = true
				let page = this.page
				this.$http({
					url: this.$http.adornUrl('vipDetails/selectVipDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': page,
						'limit': this.size,
						'languageType': this.languageTypeT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.configData = returnData
					this.totalnum2 = returnData.totalElements
				})
			},
			animeDat() {
				this.page = 1
				this.dataSelect()
			},
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
			
			this.dataSelectYy()
		}
	}
</script>

<style scoped>
.nf-memberdetails-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-memberdetails-hero {
  margin-bottom: 12px;
}

.nf-memberdetails-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-memberdetails-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-memberdetails-filter {
  margin-bottom: 12px;
}

.nf-memberdetails-filter__body {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
}

.nf-memberdetails-filter__item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.nf-memberdetails-filter__label {
  font-size: 13px;
  color: var(--nf-text-regular, #303133);
  white-space: nowrap;
}

.nf-memberdetails-filter__select {
  width: 150px;
}

.nf-memberdetails-filter__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-left: auto;
}

.nf-memberdetails-table-card {
  margin-bottom: 0;
}

.nf-memberdetails-pagination {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.nf-memberdetails-type-tag {
  color: var(--nf-brand, #ffa8c2);
  cursor: default;
}
</style>
