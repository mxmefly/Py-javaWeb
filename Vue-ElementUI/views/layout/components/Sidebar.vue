<template>
    <div class="sidebar">
        <el-menu class="sidebar-el-menu" :default-active="onRoutes" :collapse="collapse" unique-opened router
        >
            <template v-for="item in items">
                <template v-if="item.isparent==1">
                    <el-submenu :index="item.id" :key="item.id">
                        <template slot="title">
                            <i :class="item.icon"></i><span slot="title">{{ item.name }}</span>
                        </template>
                        <template v-for="subItem in item.children">
                            <el-submenu v-if="subItem.isparent==1" :index="subItem.id" :key="subItem.id">
                                <i :class="subItem.icon"></i><span slot="title">{{ subItem.name }}</span>
                                <el-menu-item v-for="(threeItem,i) in subItem.children" :key="i" :index="threeItem.id">
                                    <i :class="threeItem.icon"></i><span slot="title">{{ threeItem.name }}</span>
                                </el-menu-item>
                            </el-submenu>
                            <el-menu-item v-else :index="subItem.id" :key="subItem.id">
                                <i :class="subItem.icon"></i><span slot="title">{{ subItem.name }}</span>
                            </el-menu-item>
                        </template>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.id" :key="item.id">
                        <i :class="item.icon"></i><span slot="title">{{ item.name }}</span>
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
