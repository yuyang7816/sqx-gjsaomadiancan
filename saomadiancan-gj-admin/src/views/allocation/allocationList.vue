<template>
	<div class="nf-page nf-allocation-page">
		<div class="nf-page-hero nf-allocation-hero">
			<span class="nf-page-badge">配置列表</span>
			<h2 class="nf-allocation-hero__title">系统配置管理</h2>
			<p class="nf-allocation-hero__desc">管理全局系统、微信、短信、协议等各类平台配置项，支持分类查看与编辑</p>
		</div>

		<div class="nf-allocation-tabs-wrap">
			<el-tabs v-model="activeName" @tab-click="handleClick" class="nf-allocation-tabs">
			<el-tab-pane label="全局系统配置" name="first">
				<div class="nf-table-card nf-allocation-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<el-tab-pane label="服务费配置" name="second">
				<div class="nf-table-card nf-allocation-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
						<template slot-scope="scope">
						  <div class="nf-cell-clamp">{{scope.row.value}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="首页配置" name="thirdly">
        <el-table v-loading="tableDataLoading" :data="tableData">
          <el-table-column fixed prop="type" label="编号" align="center" width="80">
          </el-table-column>
          <el-table-column prop="min" label="类型">
          </el-table-column>
          <el-table-column prop="value" label="是否启用" width="500">
          </el-table-column>
          <el-table-column prop="createAt" label="创建时间">
          </el-table-column>
          <el-table-column label="操作" prop="id" width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
                @click="amendWhether(scope.$index, scope.row)">编辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        修改弹框
        <el-dialog title="修改" :visible.sync="dialogFormVisible1" center>
          <el-form :model="form2">
            <el-form-item label="配置类型：" :label-width="formLabelWidth">
              <el-input v-model="form2.min" style="width:65%;" readonly></el-input>
            </el-form-item>
            <el-form-item label="是否启用：" :label-width="formLabelWidth">
              <el-select v-model="form2.value" placeholder="请选择类型" style="width:65%;">
                <el-option v-for="item in values" :key="item.value" :label="item.label" :value="item.label">
                </el-option>
              </el-select>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button @click="dialogFormVisible1 = false">取 消</el-button>
            <el-button type="primary" @click="WhetherNoticeTo()">确 定</el-button>
          </div>
        </el-dialog>
      </el-tab-pane> -->
			<el-tab-pane label="微信配置" name="fifth">
				<div class="nf-table-card nf-allocation-table-card">
				<el-table v-loading="tableDataLoading" :data="tableData">
					<el-table-column fixed prop="type" label="编号" align="center" width="80">
					</el-table-column>
					<el-table-column prop="min" label="类型">
					</el-table-column>
					<el-table-column prop="value" label="内容" width="500">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
					<el-table-column label="操作" prop="id" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
								@click="amend(scope.$index, scope.row)">编辑
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="支付宝配置" name="sixth">
        <el-table v-loading="tableDataLoading" :data="tableData">
          <el-table-column fixed prop="type" label="编号" align="center" width="80">
          </el-table-column>
          <el-table-column prop="min" label="类型">
          </el-table-column>
          <el-table-column prop="value" label="内容" width="500">
          </el-table-column>
          <el-table-column prop="createAt" label="创建时间">
          </el-table-column>
          <el-table-column label="操作" prop="id" width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
                @click="amend(scope.$index, scope.row)">编辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane> -->
	  <el-tab-pane label="文件上传配置" name="wenjian">
	    <div class="nf-table-card nf-allocation-table-card">
	    <el-table v-loading="tableDataLoading" :data="tableData">
	      <el-table-column fixed prop="type" label="编号" align="center" width="80">
	      </el-table-column>
	      <el-table-column prop="min" label="类型">
	      </el-table-column>
	      <el-table-column prop="value" label="内容" width="500">
	        <template slot-scope="scope">
	          <div class="nf-cell-clamp">{{scope.row.value}}</div>
	        </template>
	      </el-table-column>
	      <el-table-column prop="createAt" label="创建时间">
	      </el-table-column>
	      <el-table-column label="操作" prop="id" width="120">
	        <template slot-scope="scope">
	          <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
	            @click="amend(scope.$index, scope.row)">编辑
	          </el-button>
	        </template>
	      </el-table-column>
	    </el-table>
	    </div>
	  </el-tab-pane>
      <el-tab-pane label="短信配置" name="seventh">
        <div class="nf-table-card nf-allocation-table-card">
        <el-table v-loading="tableDataLoading" :data="tableData">
          <el-table-column fixed prop="type" label="编号" align="center" width="80">
          </el-table-column>
          <el-table-column prop="min" label="类型">
          </el-table-column>
          <el-table-column prop="value" label="内容" width="500">
          </el-table-column>
          <el-table-column prop="createAt" label="创建时间">
          </el-table-column>
          <el-table-column label="操作" prop="id" width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
                @click="amend(scope.$index, scope.row)">编辑
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        </div>
      </el-tab-pane>
	  <el-tab-pane label="协议配置" name="xieyi">
	    <div class="nf-table-card nf-allocation-table-card">
	    <el-table v-loading="tableDataLoading" :data="tableData">
	      <el-table-column fixed prop="type" label="编号" align="center" width="80">
	      </el-table-column>
	      <el-table-column prop="min" label="类型">
	      </el-table-column>
	      <el-table-column prop="value" label="内容" width="500">
	        <template slot-scope="scope">
	          <div class="nf-cell-clamp">{{scope.row.value}}</div>
	        </template>
	      </el-table-column>
	      <el-table-column prop="createAt" label="创建时间">
	      </el-table-column>
	      <el-table-column label="操作" prop="id" width="120">
	        <template slot-scope="scope">
	          <el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
	            @click="amend(scope.$index, scope.row)">编辑
	          </el-button>
	        </template>
	      </el-table-column>
	    </el-table>
	    </div>
	  </el-tab-pane>
	  <el-tab-pane label="开关配置" name="kaiguan">
	  	<div class="nf-table-card nf-allocation-table-card">
	  	<el-table v-loading="tableDataLoading" :data="tableData">
	  		<el-table-column fixed prop="type" label="编号" align="center" width="80">
	  		</el-table-column>
	  		<el-table-column prop="min" label="类型" width="250">
	  		</el-table-column>
	  		<el-table-column prop="value" label="内容">
	  		</el-table-column>
	  		<el-table-column prop="createAt" label="创建时间" width="180">
	  		</el-table-column>
	  		<el-table-column label="操作" prop="id" width="120">
	  			<template slot-scope="scope">
	  				<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
	  					@click="amend(scope.$index, scope.row)">编辑
	  				</el-button>
	  			</template>
	  		</el-table-column>
	  	</el-table>
	  	</div>
	  </el-tab-pane>
	  <el-tab-pane label="图片配置" name="image">
	  	<div class="nf-table-card nf-allocation-table-card">
	  	<el-table v-loading="tableDataLoading" :data="tableData">
	  		<el-table-column fixed prop="type" label="编号" align="center" width="80">
	  		</el-table-column>
	  		<el-table-column prop="min" label="类型" width="250">
	  		</el-table-column>
	  		<el-table-column prop="value" label="内容">
	  			<template slot-scope="scope">
	  				<div class="nf-cell-clamp">{{scope.row.value}}</div>
	  			</template>
	  		</el-table-column>
	  		<el-table-column prop="createAt" label="创建时间" width="180">
	  		</el-table-column>
	  		<el-table-column label="操作" prop="id" width="120">
	  			<template slot-scope="scope">
	  				<el-button size="mini" type="primary" :disabled="!isAuth('allocationList:update')"
	  					@click="amend(scope.$index, scope.row)">编辑
	  				</el-button>
	  			</template>
	  		</el-table-column>
	  	</el-table>
	  	</div>
	  </el-tab-pane>
		</el-tabs>
		</div>
		<!-- 修改弹框 -->
		<el-dialog title="修改" :visible.sync="dialogFormVisible" center>
			<el-form :model="form">
				<el-form-item label="配置类型：" :label-width="formLabelWidth">
					<el-input v-model="form.min" style="width:65%;" readonly></el-input>
				</el-form-item>
				<el-form-item label="图片：" :label-width="formLabelWidth" v-if="form.conditionFrom=='image' ">
					<div
						style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
						<el-upload class="avatar-uploader" v-model="form.value"
							:action="$http.adornUrlMsg('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess">
							<img v-if="form.value" :src="form.value" class="avatar"
								style="border-radius: 6px;width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</div>
				</el-form-item>
				<el-form-item v-else-if="form.conditionFrom=='xieyi' " label="内容：" :label-width="formLabelWidth">
					<quill-editor  ref="myTextEditor" v-model="form.value" :options="quillOption"
					  style="padding-bottom: 50px;height: 300px;width: 72%;display: inline-table;margin-bottom: 60px;">
					</quill-editor>
				</el-form-item>
				<el-form-item v-else-if="form.conditionFrom=='kaiguan'" label="内容：" :label-width="formLabelWidth">
					<div >
						<div>
							<el-radio-group v-model="form.value">
								<el-radio label="是">是</el-radio>
								<el-radio label="否">否</el-radio>
							</el-radio-group>
						</div>
					</div>
				</el-form-item>
				<el-form-item v-else label="内容：" :label-width="formLabelWidth">
					<el-input v-model="form.value" style="width:65%;"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="amendNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
	  quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from './quill-config.js'
	export default {
	  name: 'news',
	  components: {
	    quillEditor
	  },
		data() {
			return {
				openValue: '是',
				closeValue: '否',
				limit: 10,
				page: 0,
				min: '',
				value: '',
				id: '',
				condition: 'xitong',
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				tableDataLoading: true,
				formLabelWidth: '200px',
				form: {
					id: '',
					min: '',
					value: '',
					type: '',
				},
				form2: {
					id: '',
					min: '',
					value: '',
					type: '',
				},
				values: [{
						value: 1,
						label: '是'
					},
					{
						value: 2,
						label: '否'
					}
				],
				tableData: [],
				checkBoxData: [], //多选框选择的值
				quillOption: quillConfig,
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect();
			},
			handleCurrentChange(val) {
				this.page = val - 1;
				this.dataSelect();
			},
			handleClick(tab, event) {
				if (tab._props.label == '全局系统配置') {
					this.condition = 'xitong'
					this.dataSelect()
				}
				if (tab._props.label == '首页配置') {
					this.condition = 'shouye'
					this.dataSelect()
				}
				if (tab._props.label == '微信配置') {
					this.condition = 'weixin'
					this.dataSelect()
				}
				if (tab._props.label == '支付宝配置') {
					this.condition = 'zhifubao'
					this.dataSelect()
				}
				if (tab._props.label == '短信配置') {
					this.condition = 'duanxin'
					this.dataSelect()
				}
				if (tab._props.label == '任务系统配置') {
					this.condition = 'renwu'
					this.dataSelect()
				}
				if (tab._props.label == '开关配置') {
					this.condition = 'kaiguan'
					this.dataSelect()
				}
				if (tab._props.label == '图片配置') {
					this.condition = 'image'
					this.dataSelect()
				}
				if (tab._props.label == '协议配置') {
				  this.condition = 'xieyi'
				  this.dataSelect()
				}
				if (tab._props.label == '服务费配置') {
				  this.condition = 'fuwufei'
				  this.dataSelect()
				}
				if (tab._props.label == '文件上传配置') {
				  this.condition = 'oss'
				  this.dataSelect()
				}
			},
			// 修改弹框
			amend(index, rows) {
				this.dialogFormVisible = true;
				this.form.id = rows.id;
				this.form.type = rows.type
				this.form.min = rows.min;
				this.form.value = rows.value;
				this.form.max = rows.max;
				this.form.createAt = rows.createAt;
				this.form.conditionFrom = rows.conditionFrom
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form.id,
						'type': this.form.type,
						'value': this.form.value,
						'min': this.form.min,
						'max': this.form.max,
						'createAt': this.form.createAt,
						'conditionFrom': this.form.conditionFrom
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
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
						this.$message.error(data.msg)
					}
				})
			},
			// 修改首页
			amendWhether(index, rows) {
				this.dialogFormVisible1 = true;
				this.form2.id = rows.id;
				this.form2.type = rows.type
				this.form2.min = rows.min;
				this.form2.value = rows.value;
				this.form2.max = rows.max;
				this.form2.createAt = rows.createAt;
				this.form2.conditionFrom = rows.conditionFrom
			},
			WhetherNoticeTo() {
				this.$http({
					url: this.$http.adornUrl('common/update'),
					method: 'post',
					data: this.$http.adornData({
						'id': this.form2.id,
						'type': this.form2.type,
						'value': this.form2.value,
						'min': this.form2.min,
						'max': this.form2.max,
						'createAt': this.form2.createAt,
						'conditionFrom': this.form2.conditionFrom
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
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`common/type/condition/${this.condition}`),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.tableDataLoading = false
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.form.value = file.data
			},
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped>
.nf-allocation-page {
  padding: 0;
  overflow-x: hidden;
}

.nf-allocation-hero {
  margin-bottom: 12px;
}

.nf-allocation-hero__title {
  margin: 8px 0 4px;
  font-size: 18px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.4;
}

.nf-allocation-hero__desc {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.5;
}

.nf-allocation-tabs-wrap {
  margin-bottom: 0;
}

.nf-allocation-tabs {
  margin-bottom: 0;
}

.nf-allocation-tabs >>> .el-tabs__header {
  margin-bottom: 0;
  background: #fff;
  border-radius: 8px 8px 0 0;
  padding: 0 16px;
  border: 1px solid var(--nf-border, #e8eaed);
  border-bottom: none;
}

.nf-allocation-table-card {
  margin-bottom: 0;
  border-radius: 0 0 8px 8px;
  border-top: none;
}

.nf-cell-clamp {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  max-height: 80px;
}

.eit {
  height: 120px;
}
</style>
