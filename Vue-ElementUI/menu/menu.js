define(function (require, exports, module) {
    var Layout = httpVueLoader('./views/layout/Layout.vue');
	var Page404 = httpVueLoader('./views/404.vue');
    const menuList = [{
        icon: 'el-icon-ex-homepage_fill',
        index: 'dashboard',
        title: '系统首页',
        path: 'views/business/Dashboard.vue',
        isMoudel: false
    },
        {
            icon: 'el-icon-ex-createtask_fill',
            index: '1',
            title: '参考功能',
            isMoudel: true,
            subs: [{
                icon: 'el-icon-ex-createtask',
                index: 'BaseForm',
                title: '基础表单',
                path: 'views/demo/BaseForm.vue',
                isMoudel: false
            },
                {
                    icon: 'el-icon-ex-createtask',
                    index: 'BaseTable',
                    title: '基础表格',
                    path: 'views/demo/BaseTable.vue',
                    isMoudel: false
                },
                {
                    icon: 'el-icon-ex-createtask',
                    index: 'DragList',
                    title: '拖拽列表',
                    path: 'views/demo/DragList.vue',
                    isMoudel: false
                },
                {
                    icon: 'el-icon-ex-createtask',
                    index: 'Upload',
                    title: '文件上传',
                    path: 'views/demo/Upload.vue',
                    isMoudel: false
                },
                {
                    icon: 'el-icon-ex-barrage',
                    index: 'Tabs',
                    title: 'tab选项卡',
                    path: 'views/demo/Tabs.vue',
                    isMoudel: false
                },
                {
                    icon: 'el-icon-ex-barrage',
                    index: 'BaseEcharts',
                    title: 'V-charts',
                    path: 'views/demo/BaseEcharts.vue',
                    isMoudel: false
                },
            ]
        },
        {
            icon: 'el-icon-ex-emoji_fill',
            index: '2',
            title: '系统图标',
            isMoudel: true,
            subs: [{
                icon: 'el-icon-ex-emoji',
                index: 'Icon',
                title: '自定义图标',
                path: 'views/demo/Icon.vue',
                isMoudel: false
            },
                {
                    icon: 'el-icon-ex-emoji',
                    index: 'ElIcon',
                    title: 'EL官方图标',
                    path: 'views/demo/ElIcon.vue',
                    isMoudel: false
                }
            ]
        },
		{
		    icon: 'el-icon-ex-remind_fill',
		    index: 'SysMsg',
		    title: '系统消息',
		    path: 'views/business/SysMsg.vue',
		    isMoudel: false
		},
        {
            icon: 'el-icon-ex-dynamic_fill',
            index: 'Test',
            title: '测试',
            path: 'views/business/Test.vue',
            isMoudel: false
        }
    ];
    exports.menuList = menuList;
    /* var getRouterList = function (arr, list) {
        for (var i = 0; i < list.length; i++) {
            if (list[i].isMoudel) {
                getRouterList(arr, list[i].subs);
            } else {
                var routerArr = {
                    path: '/' + list[i].index,
                    name: list[i].title,
                    component: httpVueLoader(list[i].path)
                }
                arr.push(routerArr);
            }
        }
        return arr;
    }
 
    var routerArr = {
        path: '/',
        component: Layout,
        children: []
    };
    routerArr.children = getRouterList([], menuList);
	var routerList=[];
	routerList.push(routerArr);
	routerList.push({path: '*', redirect: '/404', hidden: true});
    exports.routerList = [routerArr];
	 */
	exports.getRouters = function(RouList){
		var routerArr = {
		    path: '/',
		    component: Layout,
		    children: []
		};
		for(var i=0;i<RouList.length;i++){
			var arr= {
		        path: '/' + RouList[i].id,
		        name: RouList[i].name,
		        component: httpVueLoader(RouList[i].path)
		    }
			routerArr.children.push(arr);
		}
		var routerList=[];
		routerList.push(routerArr);
		routerList.push({path: '*', redirect: '/404', hidden: true});
		return routerList;
	}
	
})
