define(function(require, exports, module) {
//import request from '@/utils/request'
  var request = require('../utils/request.js');

  exports.login =  function(username, password) {
    return request({
      url: '/user/login',
      method: 'post',
      data: {
        username:username,
        password:password
      }
    })
  }

  exports.getInfo = function(token) {
    return request({
      url: '/user/info',
      method: 'get',
      params: { token:token }
    })
  }

  exports.logout =  function() {
    return request({
      url: '/user/logout',
      method: 'post'
    })
  }
})
