<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="my-el-icon-emoji"></i> elementUI官方图标</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <h2>使用方法</h2>
            <p style="line-height: 50px;">
                直接通过设置类名为 el-icon-iconName 来使用即可。例如：（共{{iconList.length}}个图标）
            </p>
            <p class="example-p">
                <i class="el-icon-edit" style="font-size: 30px;color: #ff5900"></i>
                <span>&lt;i class=&quot;el-icon-edit&quot;&gt;&lt;/i&gt;</span>
            </p>
            <p class="example-p">
                <i class="el-icon-share" style="font-size: 30px;color:#fd5656"></i>
                <span>&lt;i class=&quot;el-icon-share&quot;&gt;&lt;/i&gt;</span>
            </p>
            <p class="example-p">
                <i class="el-icon-delete" style="font-size: 30px;color: #ffc300"></i>
                <span>&lt;i class=&quot;el-icon-delete&quot;&gt;&lt;/i&gt;</span>
            </p>
            <br>
            <h2>图标</h2>
            <div class="search-box">
                <el-input class="search" size="large" v-model="keyword" clearable placeholder="请输入图标名称"></el-input>
            </div>
            <ul>
                <li class="icon-li" v-for="(item,index) in list" :key="index">
                    <div class="icon-li-content">
                        <i :class="`el-icon-${item}`"></i>
                        <span>{{item}}</span>
                    </div>
                </li>
            </ul>
        </div>

    </div>
</template>

<script>
    module.exports = {
        data: function () {
            return {
                keyword: '',
                iconList: [
                    "info",
                    "error",
                    "success",
                    "warning",
                    "question",
                    "back",
                    "arrow-left",
                    "arrow-down",
                    "arrow-right",
                    "arrow-up",
                    "caret-left",
                    "caret-bottom",
                    "caret-top",
                    "caret-right",
                    "d-arrow-left",
                    "d-arrow-right",
                    "minus",
                    "plus",
                    "remove",
                    "circle-plus",
                    "remove-outline",
                    "circle-plus-outline",
                    "close",
                    "check",
                    "circle-close",
                    "circle-check",
                    "circle-close-outline",
                    "circle-check-outline",
                    "zoom-out",
                    "zoom-in",
                    "d-caret",
                    "sort",
                    "sort-down",
                    "sort-up",
                    "tickets",
                    "document",
                    "goods",
                    "sold-out",
                    "news",
                    "message",
                    "date",
                    "printer",
                    "time",
                    "bell",
                    "mobile-phone",
                    "service",
                    "view",
                    "menu",
                    "more",
                    "more-outline",
                    "star-on",
                    "star-off",
                    "location",
                    "location-outline",
                    "phone",
                    "phone-outline",
                    "picture",
                    "picture-outline",
                    "delete",
                    "search",
                    "edit",
                    "edit-outline",
                    "rank",
                    "refresh",
                    "share",
                    "setting",
                    "upload",
                    "upload2",
                    "download",
                    "loading"
                ]
            }
        },
        computed: {
            list: function () {
                var _this = this;
                return this.iconList.filter(function (item) {
                    return item.indexOf(_this.keyword) !== -1;
                })
            }
        }
    }
</script>

<style scoped>
    .example-p {
        height: 45px;
        display: flex;
        align-items: center;
    }

    .search-box {
        text-align: center;
        margin-top: 10px;
    }

    .search {
        width: 300px;
    }

    ul, li {
        list-style: none;
    }

    .icon-li {
        display: inline-block;
        padding: 10px;
        width: 120px;
        height: 120px;
    }

    .icon-li-content {
        display: flex;
        height: 100%;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        cursor: pointer;
    }

    .icon-li-content i {
        font-size: 36px;
        color: #606266;
    }

    .icon-li-content span {
        margin-top: 10px;
        color: #787878;
    }
</style>