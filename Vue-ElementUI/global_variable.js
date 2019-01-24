define(function(require, exports, module) {
    var secondMenu = [
        {
            icon: 'el-icon-ex-homepage',
            index: 'dashboard',
            title: '系统首页'
        },
        {
            icon: 'el-icon-ex-createtask',
            index: '1',
            title: '基础页面',
            subs: [
                {
                    index: 'BaseForm',
                    title: '基础表单'
                },
                {
                    index: 'BaseTable',
                    title: '基础表格'
                },
                {
                    index: '2',
                    title: '基础工具',
                    subs: [
                        {
                            index: 'DragList',
                            title: '拖拽列表'
                        },
                        {
                            index: 'Upload',
                            title: '文件上传'
                        }
                    ]
                }
            ]
        },
        {
            icon: 'el-icon-ex-barrage',
            index: 'Tabs',
            title: 'tab选项卡'
        },
        {
            icon: 'el-icon-ex-emoji',
            index: '3',
            title: '系统图标',
            subs: [
                {
                    index: 'Icon',
                    title: '自定义图标'
                },
                {
                    index: 'ElIcon',
                    title: 'EL官方图标'
                }
            ]
        }
    ];
    var unused = [];
    module.exports = {secondMenu, unused};
})
