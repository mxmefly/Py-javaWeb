define(function(require, exports, module) {
	//import request from '@/utils/request'
	/* var request = require('../utils/request.js'); */
  require('../modules/axios/axios.js');
	exports.login = function(username, password) {
		var obj = [{
			username: username,
			password: password
		}]
		var res = axios({
			method: 'post',
			url: LoginApi,
			data: obj
		})
		return res;

	}

	exports.getInfo = function(token) {
		return request({
			url: '/user/info',
			method: 'get',
			params: {
				token: token
			}
		})
	}

	exports.logout = function() {
		return request({
			url: '/user/logout',
			method: 'post'
		})
	}
})
