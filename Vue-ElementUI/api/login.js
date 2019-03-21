define(function(require, exports, module) {
	
	var store = require('../store/getters.js')
	
	exports.touristLogin = function() {
	    var res = axios({
			method: 'post',
			url: touristLoginApi,
		})
		return res;
	}
	
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
		var res = axios({
			method: 'post',
			url: getInfoApi,
			data: {
				sessionId: token
			}
		})
		return res;
	}

	exports.logout = function(token) {
		var res = axios({
			method: 'post',
			url: LogoutApi,
			data: {
				sessionId: token
			}
		})
		return res;
	}
})
