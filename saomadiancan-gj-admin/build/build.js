'use strict'
require('./check-versions')()

process.env.NODE_ENV = 'production'

const ora = require('ora')
const rm = require('rimraf')
const path = require('path')
const fs = require('fs')
const chalk = require('chalk')
const webpack = require('webpack')
const config = require('../config')
const webpackConfig = require('./webpack.prod.conf')

const spinner = ora('building for production...')
spinner.start()

rm(path.join(config.build.assetsRoot, config.build.assetsSubDirectory), err => {
  if (err) throw err
  webpack(webpackConfig, (err, stats) => {
    spinner.stop()
    if (err) throw err
    process.stdout.write(stats.toString({
      colors: true,
      modules: false,
      children: false,
      chunks: false,
      chunkModules: false
    }) + '\n\n')

    if (stats.hasErrors()) {
      console.log(chalk.red('  Build failed with errors.\n'))
      process.exit(1)
    }

    const statsJson = stats.toJson({ all: false, entrypoints: true, assets: true })
    fs.writeFileSync(
      path.join(config.build.assetsRoot, 'build-stats.json'),
      JSON.stringify(statsJson, null, 2),
      'utf8'
    )

    console.log(chalk.cyan('  Build complete.\n'))
    console.log(chalk.yellow(
      '  Tip: built files are meant to be served over an HTTP server.\n' +
      '  Opening index.html over file:// won\'t work.\n'
    ))
    // UglifyJsPlugin(parallel) / 部分 loader 会留下未关闭句柄，导致进程不退出；
    // release.js 用 spawnSync 等待本脚本，不主动 exit 会一直卡住。
    process.exit(0)
  })
})
