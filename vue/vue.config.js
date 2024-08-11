const path = require('path')
const fs = require('fs')

module.exports = {
    devServer: {
        // open: true, // 自动打开浏览器
        // https: {
        //     cert: fs.readFileSync(path.join(__dirname, 'src/cert/file.crt')),
        //     key: fs.readFileSync(path.join(__dirname, 'src/cert/private.key'))
        // },
        // proxy: {
        //     // 当你的前端代码向 /api/ 开头的地址请求时
        //     // 它将被代理到以下配置的目标地址
        //     '/api': {
        //         target: 'http://localhost:9091', // 代理的目标服务器地址
        //         changeOrigin: true, // 开启跨域
        //         pathRewrite: { '^/api': '' } // 如果需要，你可以配置这个来重写路径
        //     }
        // }
    }
}