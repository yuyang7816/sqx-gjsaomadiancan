<template>
  <div
    class="site-wrapper nf-shell-layout"
    :class="{ 'site-sidebar--fold': sidebarFold }"
    v-loading.fullscreen.lock="loading"
    element-loading-text="拼命加载中"
    element-loading-background="rgba(244, 246, 250, 0.85)">
    <template v-if="!loading">
      <main-navbar />
      <main-sidebar />
      <div
        class="site-content__wrapper nf-shell-layout__content"
        :style="{ 'min-height': documentClientHeight + 'px' }">
        <main-content v-if="!$store.state.common.contentIsNeedRefresh" />
      </div>
    </template>
  </div>
</template>

<script>
  import MainNavbar from './main-navbar'
  import MainSidebar from './main-sidebar'
  import MainContent from './main-content'
  export default {
    provide () {
      return {
        // 刷新
        refresh () {
          this.$store.commit('common/updateContentIsNeedRefresh', true)
          this.$nextTick(() => {
            this.$store.commit('common/updateContentIsNeedRefresh', false)
          })
        }
      }
    },
    data () {
      return {
        loading: true
      }
    },
    components: {
      MainNavbar,
      MainSidebar,
      MainContent
    },
    computed: {
      documentClientHeight: {
        get () { return this.$store.state.common.documentClientHeight },
        set (val) { this.$store.commit('common/updateDocumentClientHeight', val) }
      },
      sidebarFold: {
        get () { return this.$store.state.common.sidebarFold }
      },
      userId: {
        get () { return this.$store.state.user.id },
        set (val) { this.$store.commit('user/updateId', val) }
      },
      userName: {
        get () { return this.$store.state.user.name },
        set (val) { this.$store.commit('user/updateName', val) }
      }
    },
    created () {
      this.getUserInfo()
    },
    mounted () {
      this.resetDocumentClientHeight()
    },
    methods: {
      // 重置窗口可视高度
      resetDocumentClientHeight () {
        this.documentClientHeight = document.documentElement['clientHeight']
        window.onresize = () => {
          this.documentClientHeight = document.documentElement['clientHeight']
        }
      },
      // 获取当前管理员信息
      getUserInfo () {
        this.$http({
          url: this.$http.adornUrl('/sys/user/info'),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.loading = false
            this.userId = data.user.userId
            this.userName = data.user.username
            this.$cookie.set('userId', data.user.userId)
          }
        })
      }
    }
  }
</script>

<style lang="scss">
/* 主入口 Shell 布局 · yanglao-admin-v1 · Brand #ffa8c2 */
.site-wrapper.nf-shell-layout {
  position: relative;
  min-width: 1180px;
  overflow-x: hidden;
  background: var(--nf-page-bg, #f4f6fa);
}

.nf-shell-layout .site-content__wrapper {
  position: relative;
  padding-top: var(--nf-navbar-height, 56px);
  margin-left: var(--nf-sidebar-width, 230px);
  min-height: 100%;
  background: var(--nf-page-bg, #f4f6fa);
  transition: margin-left 0.3s;
}

.nf-shell-layout.site-sidebar--fold .site-content__wrapper {
  margin-left: var(--nf-sidebar-fold-width, 64px);
}

.nf-shell-layout .site-content {
  padding: 16px;
  overflow-x: hidden;
}

.nf-shell-layout .site-content--tabs {
  padding-top: calc(var(--nf-navbar-height, 56px) + 4px);
}

.nf-shell-layout .site-content > .el-card {
  border: none;
  border-radius: 0;
  box-shadow: none;
  background: transparent;
}

.nf-shell-layout .site-content > .el-card > .el-card__body {
  padding: 0;
}

.nf-shell-layout .site-content > .el-tabs > .el-tabs__header {
  top: var(--nf-navbar-height, 56px);
  left: var(--nf-sidebar-width, 230px);
  margin-bottom: 0;
  border-bottom: 1px solid var(--nf-border, #e8eaed);
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.06);
  transition: left 0.3s;
}

.nf-shell-layout.site-sidebar--fold .site-content > .el-tabs > .el-tabs__header {
  left: var(--nf-sidebar-fold-width, 64px);
}

.nf-shell-layout .site-content > .el-tabs > .el-tabs__content {
  padding: 0;
}

.nf-shell-layout .site-content > .el-tabs > .el-tabs__content > .site-tabs__tools {
  top: var(--nf-navbar-height, 56px);
  background: var(--nf-page-bg, #f4f6fa);
}

.nf-shell-layout .el-loading-spinner .path {
  stroke: var(--nf-brand, #ffa8c2);
}

.nf-shell-layout .el-loading-spinner .el-loading-text {
  color: var(--nf-text-secondary, #909399);
}
</style>
