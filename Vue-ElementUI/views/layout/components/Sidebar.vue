<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" unique-opened router
                 >
        <!--<el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" unique-opened router
                 background-color="#324157" text-color="#bfcbd9" active-text-color="#20a0ff">-->
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index" :key="item.index">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                        </template>
                        <template v-for="subItem in item.subs">
                            <el-submenu v-if="subItem.subs" :index="subItem.index" :key="subItem.index">
                                <template slot="title">{{ subItem.title }}</template>
                                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
                                    {{ threeItem.title }}
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index" :key="subItem.index">
                                {{ subItem.title }}
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index" :key="item.index">
                        <i :class="item.icon"></i><span slot="title">{{ item.title }}</span>
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
    module.exports = {
        props: ['collapse'],
        data: function () {
            return {
                // collapse: false,
                items: [
                    {
                        icon: 'el-icon-ex-homepage',
                        index: 'dashboard',
                        title: '系统首页'
                    },
                    {
                        icon: 'el-icon-ex-createtask',
                        index: '1',
                        title: '参考功能',
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
                                index: 'DragList',
                                title: '拖拽列表'
                            },
                            {
                                index: 'Upload',
                                title: '文件上传'
                            },
							 {
							    icon: 'el-icon-ex-barrage',
							    index: 'Tabs',
							    title: 'tab选项卡'
							},
                        ]
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
                    },
					{
					    icon:'el-icon-ex-emoji'	,
						index:'Test',
						title:'测试'
					}
                ]
            }
        },
        computed: {
            onRoutes: function () {
                return this.$route.path.replace('/', '');
            }
        },
        created: function () {
            // 通过 Event Bus 进行组件间通信，来折叠侧边栏
            this.$on('collapse', function (msg) {
                this.collapse = msg;
            })
        }
    }
</script>

<style scoped>
    .sidebar {
        display: block;
        position: absolute;
        left: 0;
        top: 70px;
        bottom: 0;
        overflow-y: scroll;
    }

    .sidebar::-webkit-scrollbar {
        width: 0;
    }

    .sidebar-el-menu:not(.el-menu--collapse) {
        width: 250px;
    }

    .sidebar > ul {
        height: 100%;
    }
    [class^="el-icon-ex"] {
        font-size: 20px;
    }
</style>
