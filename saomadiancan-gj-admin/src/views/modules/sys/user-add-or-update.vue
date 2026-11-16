<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
      <el-form-item label="用户名" prop="userName">
        <el-input v-model="dataForm.userName" placeholder="登录帐号"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.password" type="password" placeholder="密码"></el-input>
      </el-form-item>
      <el-form-item label="确认密码" prop="comfirmPassword" :class="{ 'is-required': !dataForm.id }">
        <el-input v-model="dataForm.comfirmPassword" type="password" placeholder="确认密码"></el-input>
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input v-model="dataForm.email" placeholder="邮箱"></el-input>
      </el-form-item>
      <el-form-item label="手机号" prop="mobile">
        <el-input v-model="dataForm.mobile" placeholder="手机号"></el-input>
      </el-form-item>
      <el-form-item label="角色" size="mini" prop="roleIdList">
        <el-checkbox-group v-model="dataForm.roleIdList" @change="changes">
          <el-checkbox v-for="role in roleList" :key="role.roleId" :label="role.roleId">{{ role.roleName }}</el-checkbox>
        </el-checkbox-group>
      </el-form-item>
      <el-form-item label="门店" size="mini" prop="shopIdList" v-if="show">
		  <el-radio-group v-model="dataForm.shopIdListD">
		  	<el-radio v-for="role in manageList" :key="role.shopId" :label="role.shopId" style="margin: 8px;">{{ role.shopName }}</el-radio>
		  </el-radio-group>
		
      	<!-- <el-checkbox-group v-model="dataForm.shopIdList" >
      		<el-checkbox v-for="role in manageList" :key="role.shopId" :label="role.shopId" >
      			{{ role.shopName }}</el-checkbox>
      	</el-checkbox-group> -->
      </el-form-item>
      <el-form-item label="状态" size="mini" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">禁用</el-radio>
          <el-radio :label="1">正常</el-radio>
        </el-radio-group>
      </el-form-item>
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
  import { isEmail, isMobile } from '@/utils/validate'
  export default {
    data () {
      var validatePassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('密码不能为空'))
        } else {
          callback()
        }
      }
      var validateComfirmPassword = (rule, value, callback) => {
        if (!this.dataForm.id && !/\S/.test(value)) {
          callback(new Error('确认密码不能为空'))
        } else if (this.dataForm.password !== value) {
          callback(new Error('确认密码与密码输入不一致'))
        } else {
          callback()
        }
      }
      var validateEmail = (rule, value, callback) => {
        if (!isEmail(value)) {
          callback(new Error('邮箱格式错误'))
        } else {
          callback()
        }
      }
      var validateMobile = (rule, value, callback) => {
        if (!isMobile(value)) {
          callback(new Error('手机号格式错误'))
        } else {
          callback()
        }
      }
      return {
        visible: false,
        roleList: [],
        dataForm: {
          id: 0,
          userName: '',
          password: '',
          comfirmPassword: '',
          salt: '',
          email: '',
          mobile: '',
          roleIdList: [],
          shopIdList: [],
		  shopIdListD:'',
          status: 1,

        },
        dataRule: {
          userName: [
            { required: true, message: '用户名不能为空', trigger: 'blur' }
          ],
          password: [
            { validator: validatePassword, trigger: 'blur' }
          ],
          comfirmPassword: [
            { validator: validateComfirmPassword, trigger: 'blur' }
          ],
          email: [
            { required: true, message: '邮箱不能为空', trigger: 'blur' },
            { validator: validateEmail, trigger: 'blur' }
          ],
          mobile: [
            { required: true, message: '手机号不能为空', trigger: 'blur' },
            { validator: validateMobile, trigger: 'blur' }
          ]
        },
        show:false
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.$http({
          url: this.$http.adornUrl('/sys/role/select'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          this.roleList = data && data.code === 0 ? data.list : []
        }).then(() => {
          this.visible = true
          this.$nextTick(() => {
            this.$refs['dataForm'].resetFields()
          })
        }).then(() => {
					this.dataForm.shopIdList = []
					this.dataForm.shopIdListD = ''
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/sys/user/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.userName = data.user.username
                this.dataForm.salt = data.user.salt
                this.dataForm.email = data.user.email
                this.dataForm.mobile = data.user.mobile
                this.dataForm.roleIdList = data.user.roleIdList
                this.dataForm.shopIdList = data.user.shopIdList
				if(data.user.shopIdList.length>0){
					this.dataForm.shopIdListD = data.user.shopIdList[0]
				}
				
                this.dataForm.status = data.user.status
                var shows = 1
                for(var i in data.user.roleIdList){
                	if(data.user.roleIdList[i]===12){
                		shows = 2
                	}
                }
                if(shows==2){
                	this.show = true
                }
                if(shows==1){
                	this.show = false
                }
              }
            })
          }
        })
      },
      // 获取店铺数据列表
      homeSelect(id) {
      	this.$http({
      		url: this.$http.adornUrl('admin/goods/selectAllShopList'),
      		method: 'get',
      		params: this.$http.adornParams({
      		})
      	}).then(({
      		data
      	}) => {
      		let returnData = data.data;
      		this.manageList = returnData
      	})
      },
      changes(val){
        console.log('val',val)
      	var shows = 1
      	for(var i in val){
      		if(val[i]===12){
      			shows = 2
      		}
      	}
      	if(shows==2){
      		this.show = true
      	}
      	if(shows==1){
      		this.show = false
      	}
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
			if(this.dataForm.roleIdList.length<1){
								this.$message({
								  message: '请选择角色',
								  type: 'error',
								  duration: 1500,
								})
			}else{
				this.dataForm.shopIdList = []
				console.log('this.dataForm.shopIdListD',this.dataForm.shopIdList.push(this.dataForm.shopIdListD))
				this.$http({
				  url: this.$http.adornUrl(`/sys/user/${!this.dataForm.id ? 'save' : 'update'}`),
				  method: 'post',
				  data: this.$http.adornData({
					'userId': this.dataForm.id || undefined,
					'username': this.dataForm.userName,
					'password': this.dataForm.password,
					'salt': this.dataForm.salt,
					'email': this.dataForm.email,
					'mobile': this.dataForm.mobile,
					'status': this.dataForm.status,
					'roleIdList': this.dataForm.roleIdList,
					'shopIdList': this.dataForm.shopIdList
				  })
				}).then(({data}) => {
				  if (data && data.code === 0) {
					this.$message({
					  message: '操作成功',
					  type: 'success',
					  duration: 1500,
					  onClose: () => {
						this.visible = false
						this.$emit('refreshDataList')
					  }
					})
				  } else {
					this.$message.error(data.msg)
				  }
				})
			}
          }
        })
      }
    }
  }
</script>
