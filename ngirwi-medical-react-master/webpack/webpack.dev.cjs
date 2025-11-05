const config = require('./webpack.dev.js');

module.exports = async (...args) => (typeof config === 'function' ? config(...args) : config);
