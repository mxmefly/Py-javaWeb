<template lang="html">
    <el-row>
        <el-col :span="12" :offset="6">
            <div class="login">
                <el-row slot="body" :gutter="0">
                    <el-col :span="24" :xs="24" :sm="16" :md="16" :lg="16">
                        <div class="login-form">
                            <div class="card-block">
                                <h1>Vue-ElementUI</h1>
                                <p class="text-muted">任意用户名/密码登录</p>
                                <div class="input-group m-b-1">
                                    <span class="input-group-addon"><i class="el-icon-ex-people"></i></span>
                                    <input type="text" class="form-control" placeholder="user name"
                                           v-model="loginForm.username">
                                </div>
                                <div class="input-group m-b-2">
                                    <span class="input-group-addon"><i class="el-icon-ex-unlock"></i></span>
                                    <input type="password" class="form-control" placeholder="password"
                                           v-model="loginForm.password"
                                           @keyup.enter="handleLogin">
                                </div>
                                <div class="row">
                                    <el-row>
                                        <el-col :span="12">
                                            <el-button type="primary" class="btn btn-primary p-x-2"
                                                       @click="handleLogin">登录
                                            </el-button>
                                        </el-col>
                                        <el-col :span="12">
                                            <el-button type="button" class="btn btn-link forgot" style="float:right;">
                                                忘记密码?
                                            </el-button>
                                        </el-col>
                                    </el-row>
                                </div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="24" :xs="24" :sm="8" :md="8" :lg="8">
                        <div class="login-register">
                            <div class="card-block">
                                <h2>注册</h2>
                                <p>暂时还不能注册哦</p>
                                <el-button type="info" style="background-color: #0086B3; border: 0;"> 马上注册</el-button>
                            </div>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </el-col>
    </el-row>
</template>

<script>

    module.exports = {

        name: 'login',
        data: function () {
            return {
                loginForm: {
                    username: 'admin',
                    password: 'admin'
                },
            }
        },
        watch: {
            $route: {
                handler: function (route) {
                    console.log(route.query, route.query.redirect)
                    this.redirect = route.query && route.query.redirect
                    console.log(this.redirect)
                },
                immediate: true
            }
        },
        components: {},
        methods: {
            handleLogin: function () {
                _this = this;
                this.$store.dispatch('Login', this.loginForm).then(function () {
                    /* 动态添加路由*/
                    _this.$router.push({
                        path: '/dashboard'
                    });
                    console.log("登录成功，跳转首页")
                }).catch(function (err) {
                    _this.$message.error(err);
                });
            },
			
        }
    }

</script>

<style>
    .login {
        margin-top: 160px;
        width: 100%;
        border: 1px solid #cfd8dc;
        margin-right: auto !important;
        margin-left: auto !important;
        display: table;
        table-layout: fixed;
        background-color: #20a8d8;
    }

    .login .el-button {
        border-radius: 0;
    }

    .login .el-button.forgot, .login .el-button.forgot:hover {
        border: none;
    }

    .login .login-form {
        background-color: #FFFFFF;
        width: 100%;
        height: 100%;
        display: block;

    }

    .login .login-form .card-block {
        padding: 35px;
    }

    .login .login-form .card-block p {
        margin: 15px 0;
    }

    .input-group {
        width: 100%;
        display: table;
        border-collapse: separate;
        margin-bottom: 20px !important;
    }

    .input-group, .input-group-btn, .input-group-btn > .btn, .navbar {
        position: relative;
    }

    .input-group-addon:not(:last-child) {
        border-right: 0;
    }

    .input-group-addon, .input-group-btn {
        min-width: 40px;
        white-space: nowrap;
        vertical-align: middle;
        width: 1%;
    }

    .btn-link:focus, .btn-link:hover {
        color: #167495;
        text-decoration: underline;
        background-color: transparent;
    }

    .btn-link, .btn-link:active, .btn-link:focus, .btn-link:hover {
        border-color: transparent;
    }

    .btn.focus, .btn:focus, .btn:hover {
        text-decoration: none;
    }

    .input-group-addon {
        padding: .5rem .75rem;
        margin-bottom: 0;
        font-size: .875rem;
        font-weight: 400;
        line-height: 1.75rem;
        color: #607d8b;
        text-align: center;
        background-color: #cfd8dc;
        border: 1px solid rgba(0, 0, 0, .15);
    }

    .input-group .form-control, .input-group-addon, .input-group-btn {
        display: table-cell;
    }

    .input-group .form-control {
        position: relative;
        z-index: 2;
        float: left;
        margin-bottom: 0;
    }

    .form-control {
        width: 90%;
        padding: .5rem .75rem;
        font-size: 1.5rem;
        line-height: 1.75rem;
        color: #607d8b;
        background: #fff none;
        background-clip: padding-box;
        border: 1px solid rgba(0, 0, 0, .15);
        transition: border-color ease-in-out .15s, box-shadow ease-in-out .15s;
    }

    .login .login-form .card-block .row {
        display: block;
        margin: 15px 0;
    }

    .login .login-register {
        width: 100%;
        height: 100%;
        display: block;
        background-color: #20a8d8;
        color: #fff;
    }

    .login .login-register .card-block {
        text-align: center !important;
        padding: 30px;
    }

    .login .login-register .card-block p {
        text-align: left !important;
        margin: 15px 0;
        height: 100px;
    }
</style>
