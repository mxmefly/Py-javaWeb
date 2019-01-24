define(function(require, exports, module) {
  //import Vue from 'vue'
  //var Vuex = require('./modules/vuex/vuex.js');
  var app = require('./modules/app.js');
  var user = require('./modules/user.js');
  var getters = require('./getters.js');

  Vue.use(Vuex)

  const store = new Vuex.Store({
    modules: {
      "app": app,
      "user": user
    },
    "getters": getters
  })

  module.exports = store;
})
