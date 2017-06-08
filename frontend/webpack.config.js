const webpack = require('webpack');
const path = require('path');

const isDev = process.env.NODE_ENV === 'dev';

module.exports = {
  entry: {
    app: ['babel-polyfill', './src/app.js'],
  },
  output: {
    path: isDev ?
      path.join(__dirname, '../', 'public') :
      path.join(__dirname, '../', 'deploy', 'proxy', 'static'),
    filename: '[name].js',
  },
  module: {
    loaders: [
      {
        test: /\.css$/,
        loader: 'style!css',
      },
      {
        test: /\.js$/,
        exclude: /node_modules/,
        loader: 'babel-loader',
        query: {
          presets: ['es2015', 'react', 'stage-2'],
        },
      },
    ],
  },
  plugins: [
    new webpack.DefinePlugin({
      __DEV__: JSON.stringify(isDev),
      'process.env': {
        NODE_ENV: JSON.stringify(isDev ? 'dev' : 'production'),
      },
    }),
  ],
  watch: isDev,
};
