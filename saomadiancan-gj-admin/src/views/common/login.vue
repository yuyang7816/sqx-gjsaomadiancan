<template>
  <div class="site-wrapper site-page--login nf-login-page">
    <div class="nf-login-page__bg">
      <span class="nf-login-page__orb nf-login-page__orb--1"></span>
      <span class="nf-login-page__orb nf-login-page__orb--2"></span>
    </div>
    <div class="nf-login-page__container">
      <div class="nf-login-brand">
        <span class="nf-login-brand__badge">省钱兄</span>
        <h1 class="nf-login-brand__title">{{ entryName }}</h1>
        <p class="nf-login-brand__desc">高效管理扫码点餐业务，数据一目了然，助力运营决策。</p>
        <ul class="nf-login-brand__features">
          <li><i class="el-icon-check"></i><span>多维度数据统计与可视化</span></li>
          <li><i class="el-icon-check"></i><span>用户、财务、消息一体化管理</span></li>
          <li><i class="el-icon-check"></i><span>安全可靠的权限与审核机制</span></li>
        </ul>
        <span class="nf-login-brand__line"></span>
      </div>
      <div class="nf-login-card">
        <div class="nf-login-card__head">
          <h2 class="nf-login-card__title">欢迎登录</h2>
          <p class="nf-login-card__subtitle">请输入您的账号信息</p>
        </div>
        <el-form
          class="nf-login-form"
          :model="dataForm"
          :rules="dataRule"
          ref="dataForm"
          label-position="top"
          @keyup.enter.native="dataFormSubmit()">
          <el-form-item label="账号" prop="userName" class="nf-login-form__item">
            <el-input v-model="dataForm.userName" placeholder="请输入账号"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password" class="nf-login-form__item">
            <el-input v-model="dataForm.password" type="password" placeholder="请输入密码"></el-input>
          </el-form-item>
          <el-form-item label="验证码" prop="captcha" class="nf-login-form__item">
            <div class="nf-login-captcha-row">
              <el-input v-model="dataForm.captcha" placeholder="请输入验证码"></el-input>
              <div class="login-captcha" @click="getCaptcha()">
                <img :src="captchaPath" alt="验证码">
              </div>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button class="login-btn-submit nf-login-submit" type="primary" @click="dataFormSubmit()">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script>
  import { getUUID } from '@/utils'
  import { entryName } from '@/utils/httpRequest'
  export default {
    data () {
      return {
        dataForm: {
          userName: '',
          password: '',
          uuid: '',
          captcha: ''
        },
        dataRule: {
          userName: [
            { required: true, message: '帐号不能为空', trigger: 'blur' }
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' }
          ],
          captcha: [
            { required: true, message: '验证码不能为空', trigger: 'blur' }
          ]
        },
        captchaPath: '',
        entryName: ''
      }
    },
    created () {
      this.entryName = entryName
      this.getCaptcha()
    },
    methods: {
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl('sys/login'),
              method: 'post',
              data: this.$http.adornData({
                'username': this.dataForm.userName,
                'password': this.dataForm.password,
                'uuid': this.dataForm.uuid,
                'captcha': this.dataForm.captcha
              })
            }).then(({ data }) => {
              if (data && data.code === 0) {
                this.$cookie.set('token', data.token)
                this.$cookie.set('language', 'zh')
                this.$router.replace({ name: 'home' })
              } else {
                this.getCaptcha()
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
      getCaptcha () {
        this.dataForm.uuid = getUUID()
        this.captchaPath = this.$http.adornUrl(`/captcha.jpg?uuid=${this.dataForm.uuid}`)
      }
    }
  }
</script>

<style lang="scss">
.site-wrapper.site-page--login.nf-login-page {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: var(--nf-page-bg, #f4f6fa);
}

.nf-login-page__bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 0% 0%, rgba(255, 168, 194, 0.22) 0%, transparent 45%),
    radial-gradient(circle at 100% 100%, rgba(255, 117, 158, 0.18) 0%, transparent 45%),
    #ffffff;
  z-index: 0;
  overflow: hidden;
}

.nf-login-page__orb {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}

.nf-login-page__orb--1 {
  width: 360px;
  height: 360px;
  top: -120px;
  left: -120px;
  background: radial-gradient(circle, rgba(255, 168, 194, 0.18) 0%, transparent 70%);
}

.nf-login-page__orb--2 {
  width: 320px;
  height: 320px;
  bottom: -120px;
  right: -120px;
  background: radial-gradient(circle, rgba(255, 117, 158, 0.16) 0%, transparent 70%);
}

.nf-login-page__container {
  position: relative;
  z-index: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 64px;
  width: 980px;
  max-width: calc(100vw - 48px);
  background: transparent;
  border: none;
  box-shadow: none;
}

.nf-login-brand {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 24px 0;
  background: transparent;
  border: none;
  position: relative;
}

.nf-login-brand__badge {
  display: inline-block;
  align-self: flex-start;
  margin-bottom: 22px;
  padding: 5px 14px;
  font-size: 12px;
  font-weight: 500;
  color: #fff;
  background: var(--nf-brand-deep, #ff759e);
  border-radius: 20px;
  letter-spacing: 0.5px;
}

.nf-login-brand__title {
  margin: 0 0 14px;
  font-size: 28px;
  font-weight: 700;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.3;
}

.nf-login-brand__desc {
  margin: 0 0 30px;
  font-size: 14px;
  color: var(--nf-text-secondary, #909399);
  line-height: 1.6;
  max-width: 320px;
}

.nf-login-brand__features {
  margin: 0 0 28px;
  padding: 0;
  list-style: none;
}

.nf-login-brand__features li {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
  font-size: 14px;
  color: var(--nf-text-regular, #303133);
}

.nf-login-brand__features li:last-child {
  margin-bottom: 0;
}

.nf-login-brand__features li i {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  font-size: 13px;
  font-weight: 700;
  color: #fff;
  background: var(--nf-brand-deep, #ff759e);
  border-radius: 50%;
  flex-shrink: 0;
}

.nf-login-brand__line {
  display: block;
  width: 64px;
  height: 6px;
  border-radius: 3px;
  background: var(--nf-brand-deep, #ff759e);
}

.nf-login-card {
  flex: 0 0 400px;
  width: 400px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  padding: 40px 36px;
  position: relative;
  background: #fff;
  border-radius: 16px;
  box-shadow: 0 12px 40px rgba(26, 36, 51, 0.1);
  border: 1px solid var(--nf-border, #e8eaed);
  overflow: hidden;
}

.nf-login-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: var(--nf-brand-deep, #ff759e);
}

.nf-login-card__head {
  margin-bottom: 28px;
}

.nf-login-card__title {
  margin: 0 0 8px;
  font-size: 24px;
  font-weight: 700;
  color: var(--nf-text-primary, #1a2433);
}

.nf-login-card__subtitle {
  margin: 0;
  font-size: 13px;
  color: var(--nf-text-secondary, #909399);
}

.nf-login-form .el-form-item {
  margin-bottom: 20px;
}

.nf-login-form .el-form-item:last-child {
  margin-bottom: 0;
}

.nf-login-form .el-form-item__label {
  padding-bottom: 6px;
  font-size: 13px;
  font-weight: 500;
  color: var(--nf-text-regular, #303133);
  line-height: 1.4;
}

.nf-login-form .el-form-item__label::before {
  color: #f56c6c;
  margin-right: 4px;
}

.nf-login-form .el-input__inner {
  height: 42px;
  line-height: 42px;
  border-radius: 8px;
  border-color: var(--nf-border, #e8eaed);
  transition: border-color 0.2s, box-shadow 0.2s;
}

.nf-login-form .el-input__inner:focus {
  border-color: var(--nf-brand, #ffa8c2);
  box-shadow: 0 0 0 2px rgba(255, 168, 194, 0.15);
}

.nf-login-captcha-row {
  display: flex;
  align-items: center;
  gap: 12px;
  width: 100%;
}

.nf-login-captcha-row .el-input {
  flex: 1;
  min-width: 0;
}

.site-page--login .login-captcha {
  flex: 0 0 130px;
  width: 130px;
  height: 42px;
  overflow: hidden;
  border-radius: 8px;
  border: 1px solid var(--nf-border, #e8eaed);
  transition: border-color 0.2s;
  cursor: pointer;
  background: #f5f7fa;
}

.site-page--login .login-captcha:hover {
  border-color: var(--nf-brand, #ffa8c2);
}

.site-page--login .login-captcha > img {
  width: 100%;
  height: 100%;
  object-fit: contain;
  object-position: center;
  cursor: pointer;
  display: block;
}

.site-page--login .login-btn-submit {
  width: 100%;
  margin-top: 8px;
  height: 46px;
  font-size: 15px;
  font-weight: 500;
  border-radius: 8px;
  letter-spacing: 4px;
  background: var(--nf-brand-deep, #ff759e);
  border-color: var(--nf-brand-deep, #ff759e);
}

.site-page--login .login-btn-submit:hover,
.site-page--login .login-btn-submit:focus {
  background: var(--nf-brand, #ffa8c2);
  border-color: var(--nf-brand, #ffa8c2);
}

@media (max-width: 900px) {
  .nf-login-page__container {
    flex-direction: column;
    align-items: stretch;
    gap: 32px;
    width: 420px;
  }

  .nf-login-brand {
    padding: 12px 0 0;
  }

  .nf-login-brand__features,
  .nf-login-brand__line {
    display: none;
  }

  .nf-login-card {
    flex: none;
    width: 100%;
    padding: 28px;
  }
}
</style>
