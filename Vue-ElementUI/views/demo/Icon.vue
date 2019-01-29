<template>
    <div>
        <div class="crumbs">
            <el-breadcrumb separator="/">
                <el-breadcrumb-item><i class="el-icon-ex-emoji"></i> 自定义图标</el-breadcrumb-item>
            </el-breadcrumb>
        </div>
        <div class="container">
            <h2>使用方法</h2>
            <p style="line-height: 50px;">
                直接通过设置类名为 el-icon-ex-iconName 来使用即可。例如：（共{{iconList.length}}个图标）
            </p>
            <p class="example-p">
                <i class="el-icon-ex-clock_fill" style="font-size: 30px;color: #ff5900"></i>
                <span>&lt;i class=&quot;el-icon-ex-clock_fill&quot;&gt;&lt;/i&gt;</span>
            </p>
            <p class="example-p">
                <i class="el-icon-ex-coordinates" style="font-size: 30px;color:#fd5656"></i>
                <span>&lt;i class=&quot;el-icon-ex-coordinates&quot;&gt;&lt;/i&gt;</span>
            </p>
            <p class="example-p">
                <i class="el-icon-ex-share_fill" style="font-size: 30px;color: #ffc300"></i>
                <span>&lt;i class=&quot;el-icon-ex-share_fill&quot;&gt;&lt;/i&gt;</span>
            </p>
            <br>
            <h2>图标</h2>
            <div class="search-box">
                <el-input class="search" size="large" v-model="keyword" clearable placeholder="请输入图标名称"></el-input>
            </div>
            <ul>
                <li class="icon-li" v-for="(item,index) in list" :key="index">
                    <div class="icon-li-content">
                        <i :class="`el-icon-ex-${item}`"></i>
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
                    "appreciate",
                    "check",
                    "close",
                    "edit",
                    "accessory",
                    "activity",
                    "activity_fill",
                    "add",
                    "addition_fill",
                    "addition",
                    "addpeople_fill",
                    "addpeople",
                    "addressbook_fill",
                    "addressbook",
                    "barrage_fill",
                    "barrage",
                    "browse_fill",
                    "browse",
                    "brush",
                    "brush_fill",
                    "businesscard_fill",
                    "businesscard",
                    "camera_fill",
                    "camera",
                    "clock_fill",
                    "clock",
                    "close1",
                    "collection_fill",
                    "collection",
                    "computer_fill",
                    "computer",
                    "coordinates_fill",
                    "coordinates",
                    "coupons_fill",
                    "coupons",
                    "createtask_fill",
                    "createtask",
                    "customerservice_fill",
                    "customerservice",
                    "delete_fill",
                    "delete",
                    "document",
                    "document_fill",
                    "dynamic_fill",
                    "dynamic",
                    "editor",
                    "eit",
                    "emoji_fill",
                    "emoji",
                    "empty",
                    "empty_fill",
                    "enter",
                    "enterinto",
                    "enterinto_fill",
                    "feedback_fill",
                    "feedback",
                    "flag_fill",
                    "flag",
                    "flashlight",
                    "flashlight_fill",
                    "flip",
                    "flip_fill",
                    "fullscreen",
                    "group",
                    "group_fill",
                    "headlines_fill",
                    "headlines",
                    "homepage_fill",
                    "homepage",
                    "integral_fill",
                    "integral",
                    "interactive_fill",
                    "interactive",
                    "keyboard",
                    "label",
                    "label_fill",
                    "like_fill",
                    "like",
                    "live_fill",
                    "live",
                    "lock_fill",
                    "lock",
                    "mail",
                    "mail_fill",
                    "manage_fill",
                    "manage",
                    "message",
                    "message_fill",
                    "mine",
                    "mine_fill",
                    "mobilephone_fill",
                    "mobilephone",
                    "more",
                    "narrow",
                    "offline_fill",
                    "offline",
                    "order_fill",
                    "order",
                    "other",
                    "people_fill",
                    "people",
                    "picture_fill",
                    "picture",
                    "play",
                    "play_fill",
                    "playon_fill",
                    "playon",
                    "praise_fill",
                    "praise",
                    "prompt_fill",
                    "prompt",
                    "qrcode_fill",
                    "qrcode",
                    "redpacket_fill",
                    "redpacket",
                    "refresh",
                    "remind_fill",
                    "remind",
                    "return",
                    "right",
                    "scan",
                    "select_fill",
                    "select",
                    "send",
                    "service_fill",
                    "service",
                    "setup_fill",
                    "setup",
                    "share_fill",
                    "share",
                    "shielding_fill",
                    "shielding",
                    "smallscreen_fill",
                    "smallscreen",
                    "stealth_fill",
                    "stealth",
                    "success_fill",
                    "success",
                    "suspend",
                    "switch",
                    "systemprompt_fill",
                    "systemprompt",
                    "tailor",
                    "task",
                    "task_fill",
                    "tasklist_fill",
                    "tasklist",
                    "text",
                    "time_fill",
                    "time",
                    "translation_fill",
                    "translation",
                    "trash",
                    "trash_fill",
                    "undo",
                    "unlock_fill",
                    "unlock",
                    "video",
                    "video_fill",
                    "warning_fill",
                    "warning",
                    "workbench_fill",
                    "workbench",
                    "search",
                    "searchfill",
                    "qianniu",
                    "publishgoods_fill",
                    "shop_fill",
                    "transaction_fill",
                    "packup",
                    "unfold",
                    "wangwang",
                    "financial_fill",
                    "marketing_fill",
                    "shake",
                    "decoration_fill",
                    "budaidise",
                    "qianniudaidise",
                    "questions",
                    "supply",
                    "tools",
                    "int",
                    "commodity"
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