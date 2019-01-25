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
                                <i :class="subItem.icon"></i><span slot="title">{{ subItem.title }}</span>
                                <el-menu-item v-for="(threeItem,i) in subItem.subs" :key="i" :index="threeItem.index">
									<i :class="threeItem.icon"></i><span slot="title">{{ threeItem.title }}</span>
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.index" :key="subItem.index">
                                <i :class="subItem.icon"></i><span slot="title">{{ subItem.title }}</span>
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
                items: this.$store.getters.menuList
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
