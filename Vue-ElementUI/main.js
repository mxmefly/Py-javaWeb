define(function(require, exports, module) {
	
    "use strict";
    var router = require('router');
    var store = require('store');
    Vue.prototype.$axios = axios;

    var App = httpVueLoader('App.vue');

    new Vue({
        el: '#app',
        'router':router,
        'store':store,
        data: function() {
            return { visible: false }
        }
        ,
        components: {
            'my-app':App
        }
    })
})

