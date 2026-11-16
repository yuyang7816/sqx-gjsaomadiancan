<template>
  <div class="site-wrapper site-page--not-found nf-not-found-page">
    <div class="nf-not-found-page__bg">
      <span class="nf-not-found-page__orb nf-not-found-page__orb--1"></span>
      <span class="nf-not-found-page__orb nf-not-found-page__orb--2"></span>
    </div>
    <div class="nf-not-found-card">
      <div class="nf-not-found-card__hero">
        <span class="nf-page-badge">页面异常</span>
        <div class="nf-not-found-card__code" aria-hidden="true">404</div>
        <h2 class="nf-not-found-card__title">页面未找到</h2>
        <p class="nf-not-found-card__desc">抱歉！您访问的页面<em>失联</em>啦 ...</p>
      </div>
      <div class="nf-not-found-card__actions">
        <el-button class="nf-not-found-btn-back" @click="$router.go(-1)">
          <i class="el-icon-back"></i>返回上一页
        </el-button>
        <el-button type="primary" class="not-found-btn-gohome nf-not-found-btn-gohome" @click="$router.push({ name: 'home' })">
          <i class="el-icon-s-home"></i>进入首页
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
  export default {
  }
</script>

<style lang="scss">
.site-wrapper.site-page--not-found.nf-not-found-page {
  position: fixed;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  background: var(--nf-page-bg, #f4f6fa);
}

.nf-not-found-page__bg {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(ellipse at 20% 70%, rgba(255, 168, 194, 0.18) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 20%, rgba(255, 117, 158, 0.12) 0%, transparent 45%),
    var(--nf-page-bg, #f4f6fa);
  z-index: 0;
  overflow: hidden;
}

.nf-not-found-page__orb {
  position: absolute;
  border-radius: 50%;
  pointer-events: none;
}

.nf-not-found-page__orb--1 {
  width: 280px;
  height: 280px;
  top: -60px;
  left: 12%;
  background: radial-gradient(circle, rgba(255, 168, 194, 0.14) 0%, transparent 70%);
}

.nf-not-found-page__orb--2 {
  width: 220px;
  height: 220px;
  bottom: -50px;
  right: 10%;
  background: radial-gradient(circle, rgba(255, 117, 158, 0.1) 0%, transparent 70%);
}

.nf-not-found-card {
  position: relative;
  z-index: 1;
  width: 480px;
  max-width: calc(100vw - 32px);
  padding: 0;
  border-radius: 12px;
  overflow: hidden;
  background: #fff;
  border: 1px solid var(--nf-border, #e8eaed);
  box-shadow: 0 12px 40px rgba(26, 36, 51, 0.08);
  text-align: center;
}

.nf-not-found-card__hero {
  padding: 32px 32px 24px;
  background: linear-gradient(180deg, var(--nf-brand-lite, #fff5f8) 0%, #fff 60%);
  border-bottom: 3px solid var(--nf-brand, #ffa8c2);
}

.nf-not-found-card__hero .nf-page-badge {
  margin-bottom: 16px;
}

.nf-not-found-card__code {
  margin: 0 0 12px;
  font-size: 96px;
  font-weight: 700;
  line-height: 1;
  letter-spacing: -2px;
  background: linear-gradient(135deg, var(--nf-brand, #ffa8c2) 0%, var(--nf-brand-deep, #ff759e) 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.nf-not-found-card__title {
  margin: 0 0 8px;
  font-size: 22px;
  font-weight: 600;
  color: var(--nf-text-primary, #1a2433);
  line-height: 1.3;
}

.nf-not-found-card__desc {
  margin: 0;
  font-size: 14px;
  line-height: 1.6;
  color: var(--nf-text-secondary, #909399);
}

.nf-not-found-card__desc > em {
  font-style: normal;
  color: var(--nf-brand-deep, #ff759e);
  font-weight: 500;
}

.nf-not-found-card__actions {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  padding: 20px 32px 28px;
}

.nf-not-found-card__actions .el-button {
  height: 40px;
  padding: 0 20px;
  font-size: 14px;
  border-radius: 8px;
}

.nf-not-found-card__actions .el-button i {
  margin-right: 4px;
}

.nf-not-found-btn-back {
  color: var(--nf-text-regular, #303133);
  border-color: var(--nf-border, #e8eaed);
  background-color: #fff;
}

.nf-not-found-btn-back:hover,
.nf-not-found-btn-back:focus {
  color: var(--nf-brand-deep, #ff759e);
  border-color: var(--nf-brand, #ffa8c2);
  background-color: var(--nf-brand-lite, #fff5f8);
}

.site-page--not-found .not-found-btn-gohome.nf-not-found-btn-gohome {
  margin-left: 0;
  color: var(--nf-brand, #ffa8c2);
  background-color: transparent;
  border-color: var(--nf-brand, #ffa8c2);
}

.site-page--not-found .not-found-btn-gohome.nf-not-found-btn-gohome:hover,
.site-page--not-found .not-found-btn-gohome.nf-not-found-btn-gohome:focus {
  color: #fff;
  background-color: var(--nf-brand, #ffa8c2);
  border-color: var(--nf-brand, #ffa8c2);
}

@media (max-width: 480px) {
  .nf-not-found-card__hero {
    padding: 24px 20px 20px;
  }

  .nf-not-found-card__code {
    font-size: 72px;
  }

  .nf-not-found-card__actions {
    flex-direction: column;
    padding: 16px 20px 24px;
  }

  .nf-not-found-card__actions .el-button {
    width: 100%;
  }
}
</style>
