define(function (require, exports, module) {
    //import { login, logout, getInfo } from '@/api/login'
    //import { getToken, setToken, removeToken } from '@/utils/auth'
    var loginAPI = require('../../api/login.js');
    var login = loginAPI.login;
    var logout = loginAPI.logout;
    var getInfo = loginAPI.getInfo;
    var menu = require('../../menu/menu.js');
    var auth = require('../../utils/auth.js');
    var getToken = auth.getToken;
    var setToken = auth.setToken;
    var removeToken = auth.removeToken;

    const user = {
        state: {
            token: '',
            name: '',
            avatar: '',
            roles: [],
            menuList: [],
            routerList: []
        },

        mutations: {
            SET_TOKEN: function (state, token) {
                state.token = token
            },
            SET_NAME: function (state, name) {
                state.name = name
            },
            SET_AVATAR: function (state, avatar) {
                state.avatar = avatar
            },
            SET_ROLES: function (state, roles) {
                state.roles = roles
            },
            SET_MENULIST: function (state, menuList) {
                state.menuList = menuList;
            },
            SET_ROUTERLIST: function (state, routerList) {
                state.routerList = routerList;
            }
        },

        actions: {
            // 登录
            Login: function (obj, userInfo) {
                const username = userInfo.username.trim()
                return new Promise(function (resolve, reject) {
                    login(username, userInfo.password).then(function (response) {
                        var result = response.data.success;
                        if (result) {
                            setToken(userInfo.username);
                            obj.commit('SET_TOKEN', userInfo.username);
                            obj.commit('SET_MENULIST', menu.menuList);
                            obj.commit('SET_ROUTERLIST', menu.routerList);
							resolve();
                        }else{
							reject("账号或密码错误")
						}
                    }).catch(function (error) {
                        reject(error)
                    })
                })
            },

            // 获取用户信息
            GetInfo: function (obj) {
                return new Promise(function (resolve, reject) {
                    getInfo(getToken()).then(function (response) {
                        const data = response.data;
						if(data.success){
							obj.commit('SET_MENULIST', menu.menuList);
							obj.commit('SET_ROUTERLIST', menu.routerList)
							obj.commit('SET_NAME', data.data.name);
						}
                        resolve(response);
                    }).catch(function (error) {
						console.log("error",error)
                        reject(error)
                    })
                })
            },

            // 登出
            LogOut: function (obj) {
                return new Promise(function (resolve, reject) {
                    obj.commit('SET_TOKEN', '');
                    obj.commit('SET_ROLES', []);
                    removeToken();
                    resolve();
					/* logout(obj.state.token).then(function () {
                        obj.commit('SET_TOKEN', '')
                        obj.commit('SET_ROLES', [])
                        removeToken()
                        resolve()
                    }).catch(function (error) {
                        reject(error)
                    }) */
                })
            },

            // 前端 登出
            FedLogOut: function (obj) {
                return new Promise(function (resolve) {
                    obj.commit('SET_TOKEN', '')
                    removeToken()
                    resolve()
                })
            }
        }
    }

    module.exports = user
})
