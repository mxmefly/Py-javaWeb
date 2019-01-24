define(function(require, exports, module) {
    "use strict";
    var Login = httpVueLoader('./views/login/login.vue');
    var Page404 = httpVueLoader('./views/404.vue');
    var Layout = httpVueLoader('./views/layout/Layout.vue');

    var Form = httpVueLoader('./views/testpages/Form.vue');
    var Dashboard = httpVueLoader('./views/business/Dashboard.vue');
    var BaseForm = httpVueLoader('./views/demo/BaseForm.vue');
    var BaseTable = httpVueLoader('./views/demo/BaseTable.vue');
    var DragList = httpVueLoader('./views/demo/DragList.vue');
    var Upload = httpVueLoader('./views/demo/Upload.vue');
    var Tabs = httpVueLoader('./views/demo/Tabs.vue');
    var Icon = httpVueLoader('./views/demo/Icon.vue');
    var ElIcon = httpVueLoader('./views/demo/ElIcon.vue');
	var Test = httpVueLoader('./views/business/Test.vue');
	var menu = require('../menu/menu.js');
    var constantRouterMap = [
        { path: '/login', component: Login, hidden: true },
        { path: '/404', component: Page404, hidden: true },  
        {
         path: '/',
         redirect: '/Login'
        },
        {
            path: '/',
            component: Layout,
            iconCls: 'el-icon-message',
            children: menu.routerList
        },
        { path: '*', redirect: '/404', hidden: true }
    ]

    module.exports =  new VueRouter({
        //mode: 'history', //后端支持可开
        scrollBehavior: function() {return ({ y: 0 })},
        routes: constantRouterMap
    })
})