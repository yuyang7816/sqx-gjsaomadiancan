<template>
  <nav class="site-navbar nf-navbar" :class="'site-navbar--' + navbarLayoutType">
    <div class="site-navbar__header">
      <h1 class="site-navbar__brand" @click="$router.push({ name: 'home' })">
        <img class="site-navbar__brand-logo" src="~@/assets/img/avatar.png" alt="">
        <a class="site-navbar__brand-lg" href="javascript:;">{{ entryName }}</a>
        <a class="site-navbar__brand-mini" href="javascript:;">
          <img src="~@/assets/img/avatar.png" style="width: 25px;border-radius: 50%;" alt="">
        </a>
      </h1>
    </div>
    <div class="site-navbar__body">
      <div class="site-navbar__inner">
        <el-menu class="site-navbar__menu" mode="horizontal">
          <el-menu-item class="site-navbar__switch" index="0" @click="sidebarFold = !sidebarFold">
            <icon-svg name="zhedie"></icon-svg>
          </el-menu-item>
        </el-menu>
        <div class="site-navbar__context">
          <span class="nf-page-badge">{{ pageBadge }}</span>
          <span class="site-navbar__context-title">{{ pageTitle }}</span>
        </div>
        <div class="site-navbar__right">
          <el-menu class="site-navbar__menu site-navbar__menu--right" mode="horizontal">
            <el-menu-item index="1" style="position: relative;" @click="$router.push({ name: 'missionSm' })" v-if="show">
              <template slot="title">
                <span class="left-number" v-if="chatNum">{{ chatNum }}</span>
                <icon-svg name="xiaoxi" class="el-icon-setting" style="font-size: 28px;"></icon-svg>
                <audio id="audio" autoplay="autoplay" v-if="chatNum>0"
                  src="https://pw.xianmxkj.com/file/uploadPath/2022/01/12/01beb0bc15549b9aa4d18027a7751bae.mp3" ref='audio'
                  controls hidden></audio>
              </template>
            </el-menu-item>
            <el-menu-item class="site-navbar__avatar" index="3">
              <el-dropdown :show-timeout="0" placement="bottom">
                <div class="user-pill">
                  <img class="user-pill__avatar" src="~@/assets/img/avatar.png" :alt="userName">
                  <div class="user-pill__info">
                    <span class="user-pill__name">{{ userName }}</span>
                    <span class="user-pill__role">管理员</span>
                  </div>
                  <i class="el-icon-arrow-down user-pill__arrow"></i>
                </div>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item @click.native="logoutHandle()">退出</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>
    <update-password v-if="updatePassowrdVisible" ref="updatePassowrd"></update-password>
  </nav>
</template>

<script>
  import UpdatePassword from './main-navbar-update-password'
  import {
    clearLoginInfo
  } from '@/utils'
  import { entryName } from '@/utils/httpRequest'
  export default {
    data () {
      return {
        updatePassowrdVisible: false,
        chatNum: 0,
        intervalId: 0,
        errCount: 0,
        shopIds: [],
        show: false,
        entryName: ''
      }
    },
    components: {
      UpdatePassword
    },
    computed: {
      navbarLayoutType: {
        get () { return this.$store.state.common.navbarLayoutType }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold },
        set (val) { this.$store.commit('common/updateSidebarFold', val) }
      },
      mainTabs: {
        get () { return this.$store.state.common.mainTabs },
        set (val) { this.$store.commit('common/updateMainTabs', val) }
      },
      userName: {
        get () { return this.$store.state.user.name }
      },
      pageTitle () {
        return (this.$route.meta && this.$route.meta.title) || '首页'
      },
      pageBadge () {
        const title = this.pageTitle
        if (title === '首页') return '数据中心'
        return title.length > 4 ? title.slice(0, 4) : title
      }
    },
    mounted () {
      this.entryName = entryName
      var userId = this.$cookie.get('userId')
      this.$http({
        url: this.$http.adornUrl(`/sys/user/info`),
        method: 'get',
        params: this.$http.adornParams()
      }).then(({ data }) => {
        if (data && data.code === 0) {
          var shows = 1
          for (var i in data.user.roleIdList) {
            if (data.user.roleIdList[i] == 12) {
              shows = 2
            } else {
              shows = 1
            }
          }
          if (shows == 2) {
            this.show = true
          }
          if (shows == 1) {
            this.show = false
          }
          if (this.show == true) {
            this.dataSelect()
          }
        }
      })
    },
    methods: {
      updatePasswordHandle () {
        this.updatePassowrdVisible = true
        this.$nextTick(() => {
          this.$refs.updatePassowrd.init()
        })
      },
      logoutHandle () {
        this.$confirm(`确定进行[退出]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/sys/logout'),
            method: 'post',
            data: this.$http.adornData()
          }).then(({ data }) => {
            if (data && data.code === 0) {
              clearLoginInfo()
              this.$cookie.set('token', '')
              sessionStorage.clear()
              this.$router.push({ name: 'login' })
            }
          })
        }).catch(() => {})
      },
      newsSelect () {
        var _this = this
        this.$http({
          url: this.$http.adornUrl('admin/ordermessage/selectReadFlagCount'),
          method: 'post',
          params: this.$http.adornParams({
            'shopIds': this.shopIds.toString()
          })
        }).then(({ data }) => {
          if (data.code == 0) {
            if (data.data > 0) {
              if (this.errCount != data.data) {
                this.$notify.info({
                  title: '消息',
                  dangerouslyUseHTMLString: true,
                  message: '有' + data.data + '条订单，请前往订单中心处理。',
                  offset: 80,
                  duration: 0,
                  type: 'warning',
                  onClose () {
                    _this.defineCallBack()
                  }
                })
                this.errCount = data.data
                this.chatNum = data.data
              }
            } else {
              this.errCount = 0
              this.chatNum = 0
            }
          }
        })
      },
      defineCallBack () {
        this.$http({
          url: this.$http.adornUrl('admin/ordermessage/allcheckOrderMessage'),
          method: 'post',
          params: this.$http.adornParams({
            'shopIds': this.shopIds.toString()
          })
        }).then(({ data }) => {
          if (data.code === 0) {
            this.errCount = 0
            this.chatNum = 0
            this.$router.replace({ name: 'missionSm' })
          }
        })
      },
      aplayAudio () {
        const audio = document.getElementById('audio')
        audio.play()
      },
      dataSelect () {
        this.tableDataLoading = true
        this.$http({
          url: this.$http.adornUrl('admin/goods/selectAllShop'),
          method: 'get',
          params: this.$http.adornParams({
            'page': 1,
            'limit': 10,
            'shopName': ''
          })
        }).then(({ data }) => {
          this.tableDataLoading = false
          var arr = []
          for (var i in data.data.list) {
            if (data.data.list[i].shopId) {
              arr.push(data.data.list[i].shopId)
            }
          }
          this.shopIds = arr
          this.$cookie.set('shopId', data.data.list[0].shopId)
          this.newsSelect()
        })
      }
    },
    created () {
      setInterval(() => {
        this.newsSelect()
      }, 10000)
    },
    watch: {
      'chatNum': function () {
        this.aplayAudio()
      }
    }
  }
</script>

<style lang="scss" scoped>
.site-navbar__inner {
  display: flex;
  align-items: center;
  width: 100%;
}
.site-navbar__menu:first-child {
  flex-shrink: 0;
  border-bottom: none;
  background: transparent;
  .el-menu-item {
    border-bottom: none !important;
  }
}
</style>
