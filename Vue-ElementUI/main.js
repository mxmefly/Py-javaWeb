define(function(require, exports, module) {

	"use strict";
	var router = require('router');
	var store = require('store');
	Vue.prototype.$axios = axios;

	router.beforeEach(function(to, from, next) {
		console.log("----拉取store登录信息----");
		if(to.path!='/login'){
			if(store.getters.token==''){
				console.log("----用户未登录，跳转login----");
				next('/login?redirect=${to.path}');
			}else{
				next();
			}	
		}
		next();		
	});

	var App = httpVueLoader('App.vue');
	new Vue({
		el: '#app',
		'router': router,
		'store': store,
		data: function() {
			return {
				visible: false
			}
		},
		components: {
			'my-app': App
		}
	})
})
