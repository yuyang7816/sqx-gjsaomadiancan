'use strict'

const fs = require('fs')
const path = require('path')
const { spawnSync } = require('child_process')

const config = require('../config')

const distPath = path.resolve(__dirname, '../dist')
const env = process.env.npm_config_qa ? 'qa' : process.env.npm_config_uat ? 'uat' : 'prod'

function createVersion () {
  const d = new Date()
  const yy = d.getFullYear().toString().slice(2)
  const MM = String(d.getMonth() + 1).padStart(2, '0')
  const DD = String(d.getDate()).padStart(2, '0')
  const hh = String(d.getHours()).padStart(2, '0')
  const mm = String(d.getMinutes()).padStart(2, '0')
  return `${yy}${MM}${DD}${hh}${mm}`
}

function replaceAll (filePath, replacer) {
  const content = fs.readFileSync(filePath, 'utf8')
  fs.writeFileSync(filePath, replacer(content), 'utf8')
}

function concatFiles (filePaths, targetPath) {
  const content = filePaths.map(filePath => fs.readFileSync(filePath, 'utf8')).join('\n')
  fs.mkdirSync(path.dirname(targetPath), { recursive: true })
  fs.writeFileSync(targetPath, content, 'utf8')
}

function getEntrypointAssets (ext) {
  const statsPath = path.join(distPath, 'build-stats.json')
  const buildLogPath = path.resolve(__dirname, '../build.log')
  const extRe = new RegExp('\\.' + ext + '($|\\?)')

  if (fs.existsSync(statsPath)) {
    const stats = JSON.parse(fs.readFileSync(statsPath, 'utf8'))
    const assets = (((stats.entrypoints || {}).app || {}).assets || [])
      .map(asset => typeof asset === 'string' ? asset : asset.name)
      .filter(asset => asset && extRe.test(asset))

    if (assets.length) {
      return assets
    }
  }

  if (fs.existsSync(buildLogPath)) {
    const buildLog = fs.readFileSync(buildLogPath, 'utf8')
    const entrypointLine = buildLog.split(/\r?\n/).find(line => line.includes('Entrypoint') && line.includes('static/js/manifest.js'))

    if (entrypointLine) {
      const matches = entrypointLine.match(new RegExp('static\\/(js|css)/[^\\s]+\\.' + ext, 'g'))
      if (matches && matches.length) {
        return matches
      }
    }
  }

  if (ext === 'css') {
    return [
      'static/css/manifest.css',
      'static/css/vendor.css',
      'static/css/app.css'
    ]
  }

  return [
    'static/js/manifest.js',
    'static/js/vendor.js',
    'static/js/app.js'
  ]
}

function getEntrypointJsAssets () {
  return getEntrypointAssets('js')
}

function getEntrypointCssAssets () {
  return getEntrypointAssets('css')
}

function replaceRuntimeAssetList (filePath, entryAssets, kind) {
  const lines = entryAssets
    .map(asset => `      window.SITE_CONFIG.cdnUrl + '/${asset}',`)
    .join('\n')

  const pattern = kind === 'css'
    ? /      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/css\/manifest\.css',[\s\S]*?      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/css\/app\.css'/
    : /      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/js\/manifest\.js',[\s\S]*?      window\.SITE_CONFIG\.cdnUrl \+ '\/static\/js\/app\.js'/

  replaceAll(filePath, content => content.replace(pattern, lines.replace(/,$/, '')))
}

function replaceRuntimeJsList (filePath, entryJsAssets) {
  replaceRuntimeAssetList(filePath, entryJsAssets, 'js')
}

function removePath (targetPath) {
  fs.rmSync(targetPath, { recursive: true, force: true })
}

function copyDirectory (sourcePath, targetPath) {
  fs.mkdirSync(path.dirname(targetPath), { recursive: true })
  fs.cpSync(sourcePath, targetPath, { recursive: true })
}

function runWebpackBuild () {
  const result = spawnSync(process.execPath, [path.resolve(__dirname, 'build.js')], {
    stdio: 'inherit',
    env: process.env
  })

  if (result.status !== 0) {
    process.exit(result.status || 1)
  }
}

function runPostBuild () {
  const version = createVersion()
  const versionPath = path.join(distPath, version)
  const staticPath = path.join(distPath, 'static')
  const sourceStaticPath = path.resolve(__dirname, '../static')
  const versionStaticPath = path.join(versionPath, 'static')
  const manifestPath = path.join(versionStaticPath, 'js', 'manifest.js')
  const versionConfigPath = path.join(versionStaticPath, 'config', `index-${env}.js`)
  const initConfigPath = path.join(versionStaticPath, 'config', 'init.js')
  const outputConfigPath = path.join(distPath, 'config', 'index.js')
  const entryJsAssets = getEntrypointJsAssets()
  const entryCssAssets = getEntrypointCssAssets()

  removePath(versionPath)
  copyDirectory(sourceStaticPath, staticPath)
  copyDirectory(staticPath, versionStaticPath)

  replaceAll(manifestPath, content => content.replace(new RegExp(`"${config.build.assetsPublicPath}"`, 'g'), 'window.SITE_CONFIG.cdnUrl + "/"'))
  replaceAll(versionConfigPath, content => content.replace(/window\.SITE_CONFIG\['version'\] = '.*'/g, `window.SITE_CONFIG['version'] = '${version}'`))

  concatFiles([versionConfigPath, initConfigPath], outputConfigPath)
  replaceRuntimeJsList(outputConfigPath, entryJsAssets)
  replaceRuntimeAssetList(outputConfigPath, entryCssAssets, 'css')

  removePath(staticPath)
  removePath(path.join(versionStaticPath, 'config'))

  process.stdout.write(`\nRelease output: dist/${version}\n`)
  process.stdout.write(`Runtime config: dist/config/index.js\n`)
  process.exit(0)
}

if (process.argv.includes('--post-only')) {
  runPostBuild()
} else if (process.argv.includes('--build-only')) {
  runWebpackBuild()
} else {
  runWebpackBuild()
  runPostBuild()
}
