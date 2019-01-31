define(function(require, exports, module) {
	//import request from '@/utils/request'
	/* var request = require('../utils/request.js'); */
	require('../modules/axios/axios.js');
	exports.login = function(username, password) {
	    var res = axios({
			method: 'post',
			url: LoginApi,
			data: {
				id: username,
				pwd: password
			}
		})
		return res;

	}

	exports.getInfo = function(token) {
		console.log("token",token)
		var res = axios({
			method: 'post',
			url: getInfoApi,
			data: {
				id: token,
			}
		})
		return res;
	}

	exports.logout = function() {
		return;
	}
})
