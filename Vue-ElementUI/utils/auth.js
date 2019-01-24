define(function(require, exports, module) {
  require('../modules/js-cookie/js.cookie.js');
  //import Cookies from 'js-cookie'

  const TokenKey = 'Admin-Token'

  exports.getToken = function() {
    return Cookies.get(TokenKey)
  }

  exports.setToken = function(token) {
    return Cookies.set(TokenKey, token)
  }

  exports.removeToken = function() {
    return Cookies.remove(TokenKey)
  }
})
