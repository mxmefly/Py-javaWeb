<template>
    <div class="wrapper">
        <v-head v-on:collapse-change="onCollapse"></v-head>
        <v-sidebar :collapse="collapse"></v-sidebar>
        <div class="content-box" :class="{'content-collapse':collapse}">
            <v-tags v-on:set-tags="setTags"></v-tags>
            <div class="content">
                <transition name="move" mode="out-in">
                    <keep-alive :include="tagsList">
                        <router-view></router-view>
                    </keep-alive>
                </transition>
            </div>
        </div>
    </div>
</template>

<script>
    var vHead = httpVueLoader('./components/Header.vue');
    var vSidebar = httpVueLoader('./components/Sidebar.vue');
    var vTags = httpVueLoader('./components/Tags.vue');

    module.exports = {
        data: function () {
            return {
                tagsList: [],
                collapse: false
            }
        },
        components: {
            vHead: vHead,
            vSidebar: vSidebar,
            vTags: vTags
        },
        methods:{
            onCollapse: function(msg){
                this.collapse = msg;
            },
            // 只有在标签页列表里的页面才使用keep-alive，即关闭标签之后就不保存到内存中了。
            setTags: function(msg){
               var arr = [];
               for (var i = 0, len = msg.length; i < len; i++) {
                   msg[i].name && arr.push(msg[i].name);
               }
               this.tagsList = arr;
            }
        }
    }
</script>
