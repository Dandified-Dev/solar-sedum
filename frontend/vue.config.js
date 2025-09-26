const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  configureWebpack: {
    resolve: {
      fallback: {
        util: require.resolve('util/'),
        stream: require.resolve('stream-browserify'),
        crypto: require.resolve('crypto-browserify')
      }
    }
  }
});


