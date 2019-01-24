define(function(require, exports, module) {
	const menuList= [{
                        icon: 'el-icon-ex-homepage',
                        index: 'dashboard',
                        title: '系统首页',
						path:'./views/business/Dashboard.vue',
						isMoudel:false
                    },
                    {
                        icon: 'el-icon-ex-createtask',
                        index: '1',
                        title: '参考功能',
						isMoudel:true,
                        subs: [
                            {
								icon: 'el-icon-ex-createtask',
                                index: 'BaseForm',
                                title: '基础表单',
								path:'./views/demo/BaseForm.vue',
								isMoudel:false
                            },
                            {
								icon: 'el-icon-ex-createtask',
                                index: 'BaseTable',
                                title: '基础表格',
								path:'./views/demo/BaseTable.vue',
								isMoudel:false
                            },
                            {
								icon: 'el-icon-ex-createtask',
                                index: 'DragList',
                                title: '拖拽列表',
								path:'./views/demo/DragList.vue',
								isMoudel:false
                            },
                            {
								icon: 'el-icon-ex-createtask',
                                index: 'Upload',
                                title: '文件上传',
								path:'./views/demo/Upload.vue',
								isMoudel:false
                            },
							 {
							    icon: 'el-icon-ex-barrage',
							    index: 'Tabs',
							    title: 'tab选项卡',
								path:'./views/demo/Tabs.vue',
								isMoudel:false
							},
                        ]
                    },          
                    {
                        icon: 'el-icon-ex-emoji',
                        index: '2',
                        title: '系统图标',
						isMoudel:true,
                        subs: [
                            {
								icon: 'el-icon-ex-emoji',
                                index: 'Icon',
                                title: '自定义图标',
								path:'./views/demo/Icon.vue',
								isMoudel:false
                            },
                            {
								icon: 'el-icon-ex-emoji',
                                index: 'ElIcon',
                                title: 'EL官方图标',
								path:'./views/demo/ElIcon.vue',
								isMoudel:false
                            }
                        ]
                    },
					{
					    icon:'el-icon-ex-emoji'	,
						index:'Test',
						title:'测试',
						path:'./views/business/Test.vue',
						isMoudel:false
					}
                ];
	
	exports.menuList= menuList;				
    var getRouterList = function(arr,list){	
		for(var i=0; i< list.length ;i++){
			if(list[i].isMoudel){
				getRouterList(arr,list[i].subs);
			}else{
				var routerArr={
					path: '/'+list[i].index,
					name: list[i].title,
					component: httpVueLoader(list[i].path)
				}
				arr.push(routerArr);
			}
		}
		return arr;
	}
	exports.routerList = getRouterList([],menuList);
})