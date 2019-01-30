define(function (require, exports, module) {

    "use strict";
    var router = require('router');
    var store = require('store');
	var auth  = require('./utils/auth.js');
	var getToken = auth.getToken;
    Vue.prototype.$axios = axios;

    router.beforeEach(function (to, from, next) {
		/* 执行 getInfo */
		var token = getToken();
		console.log("----拉取token登录信息----",token);
        if (to.path != '/login') {
            if (typeof(token)=="undefined") {
                console.log("----用户未登录，跳转login----");
                var path = '/login?redirect=' + to.path;
                next(path);
            } else {
                next();
            }
        }else{
			next();
		}
       
    });

    var App = httpVueLoader('App.vue');
    new Vue({
        el: '#app',
        'router': router,
        'store': store,
        data: function () {
            return {
                visible: false
            }
        },
        components: {
            'my-app': App
        }
    })
})
