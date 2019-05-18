define(function (require, exports, module) {
    "use strict";
    var Login = httpVueLoader('./views/login/login.vue');
    var Page404 = httpVueLoader('./views/404.vue');
    var Layout = httpVueLoader('./views/layout/Layout.vue');
    var menu = require('../menu/menu.js');
    var constantRouterMap = [
        {path: '/login', component: Login, hidden: true},
		{path: '/404', component: Page404, hidden: true}
    ]
    module.exports = new VueRouter({
        scrollBehavior: function () {
            return ({y: 0})
        },
        routes: constantRouterMap
    })
})