<template>
    <div class="login-container">
        <el-row class="bg-img">
            <el-col :span="18" class="login-left">
                <!--<img class="bg-img" src="assets/images/login/login-bg4.jpg">-->
            </el-col>
            <el-col :span="6" class="login-right">
                <div class="login-header">
                    <img src="assets/images/bank/HF-Logo-120-white.png"></img>
                    <h3 class="title">Vue-ElementUI</h3>
                </div>
                <el-form ref="loginForm" :model="loginForm" class="login-form" auto-complete="on"
                         label-position="left">
                    <!--<h3 class="title">Vue-ElementUI</h3>-->
                    <el-form-item prop="username">
                        <el-input v-model="loginForm.username"
                                  prefix-icon="el-icon-ex-addressbook"
                                  name="username"
                                  type="text"
                                  auto-complete="on"
                                  placeholder="username"
                                  class="login-input"/>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input
                                :type="pwdType"
                                v-model="loginForm.password"
                                prefix-icon="el-icon-ex-unlock"
                                name="password"
                                auto-complete="on"
                                placeholder="password"
                                @keyup.enter.native="handleLogin"
                                class="login-input"/>
                    </el-form-item>
                    <el-form-item>
                        <el-button :loading="loading" type="primary" style="width:100%;"
                                   @click.native.prevent="handleLogin">
                            Sign in
                        </el-button>
                    </el-form-item>
                </el-form>
                <div class="login-footer">仅用于测试</div>
            </el-col>
        </el-row>
    </div>
</template>

<script>
    module.exports = {
        name: 'Login',
        data: function () {
            return {
                loginForm: {
                    username: 'admin',
                    password: 'admin'
                },
                loading: false,
                pwdType: 'password',
                redirect: undefined
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
        methods: {
            handleLogin: function () {

                console.log("登录成功，跳转首页")

                /* this.$router.push({ path: this.redirect || '/' }) */

                this.$router.push({
                    path: '/dashboard'
                });

                /*this.$store.dispatch('Login', this.loginForm).then(function(){
                   this.loading = false
                   this.$router.push({ path: this.redirect || '/' })
               }).catch(function() {
                   this.loading = false
               })*/
            }
        }
    }
</script>


<style scoped>
    .bg-img {
        width: 100%;
        height: 100%;
        opacity: 0.8;
        background: url("../../assets/images/login/login-bg1.jpg");
        background-size: cover;
    }

    .login-container {
        height: 100%;
    }

    .login-container .el-row {
        height: 100%;
    }

    .login-container .el-row .el-col {
        height: 100%;
    }

    .login-left {
        background: black;
        opacity: 0.1;
    }

    .login-right {
        padding: 0 30px;
        background: #0d2650;
        opacity: 0.9;
        position: relative;
    }

    .login-form {
        top: 17%;
        position: relative;
    }

    .login-input {
        -webkit-box-flex: 1;
        -ms-flex: 1;
        flex: 1;
        padding: 0;
        overflow: hidden;
        font-family: inherit;
        font-size: inherit;
        font-weight: inherit;
        background: transparent;
        border: none;
        outline: none;
        resize: none;
        width: 100%;
    }

    .title {
        font-size: 26px;
        font-weight: 400;
        color: rgb(202, 192, 192);
        margin: 20px auto;
        text-align: center;
        font-weight: bold;
    }

    .el-form-item {
        margin-bottom: 10px;
    }

    body {
        overflow: hidden
    }

    .login-header {
        margin: 15px 0 20px;
        font-size: 18px;
        text-align: center;
        top: 12%;
        position: relative;
    }

    .login-footer {
        bottom: 50px;
        position: absolute;
        text-align: center;
        color: white;
        left: 0;
        right: 0;
    }
</style>
