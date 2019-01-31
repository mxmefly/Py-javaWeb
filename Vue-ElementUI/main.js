define(function (require, exports, module) {

    "use strict";
    var router = require('router');
    var store = require('store');
	/* var dynamicRouter= require('dynamicRouter'); */
	var auth  = require('./utils/auth.js');
	var getToken = auth.getToken;
	axios.defaults.headers.post['Content-Type'] = 'text/plain';
    Vue.prototype.$axios = axios;

    router.beforeEach(function (to, from, next) {
		var token = getToken();
		token = (typeof(token)=="undefined")?"":token;
		if(to.path==='/login'){
			next();
		}else{
			if(token.length!=0){
				if(store.getters.menuList.length==0){
					console.log("---重新拉取用户数据---");
					store.dispatch('GetInfo').then(function(res){
						router.addRoutes(store.getters.routerList);
						next({path:to.path});
					}).catch(function(err){
						next({path:'login'});
					})
				}else{
					next();
				}
			}else{
				if(to.path==='/login'){
					next();
				}else{
					var path = '/login?redirect=' + to.path;
					next(path);
				}
			}
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
