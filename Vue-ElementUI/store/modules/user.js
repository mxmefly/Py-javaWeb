                                         define(function (require, exports, module) {
    var loginAPI = require('../../api/login.js');
    var login = loginAPI.login;
	var touristLogin=loginAPI.touristLogin;
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
            routerList: [],
			unreadMsgNum:'', 
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
            },
			SET_UNREADMSGNUM: function (state, unreadMsgNum) {
			    state.unreadMsgNum = unreadMsgNum;
			}
        },

        actions: {
            // 登录
            Login: function (obj, userInfo) {
                const username = userInfo.username.trim()
                return new Promise(function (resolve, reject) {
                    login(username, userInfo.password).then(function (response) {	
                        var result = response.data.data;
                        if (response.data.success) {
							/* cookie 里放的是 sessionId 实现前后端登陆状态的统一 */
                            setToken(result.sessionId);
                            obj.commit('SET_TOKEN', userInfo.username);
							resolve();
                        }else{
							reject("账号或密码错误")
						}
                    }).catch(function (error) {
                        reject(error)
                    })
                })
            },
			
			TouristLogin:function(obj){
				return new Promise(function (resolve, reject) {
				    touristLogin().then(function (response) {
						console.log("response",response)
				        var result = response.data.success;
				        if (result) {
				        	/* cookie 里放的是 sessionId 实现前后端登陆状态的统一 */
				            setToken(response.data.data.sessionId);
				            obj.commit('SET_TOKEN', "游客");
							resolve();
				        }else{
							reject("登陆失败")
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
                        var data = response.data;
						if(data.success){
							obj.commit('SET_MENULIST', data.data.menuList);
							obj.commit('SET_ROUTERLIST', menu.getRouters(data.data.routList));
							obj.commit('SET_NAME', data.data.name);
							obj.commit('SET_UNREADMSGNUM', data.data.unreadMsgNum);
							resolve(response);
						}else{
							reject("response")
						}     
                    }).catch(function (error) {
						console.log("error",error)
                        reject(error)
                    })
                })
            },

            // 登出
            LogOut: function (obj) {
                return new Promise(function (resolve, reject) {
					logout(getToken()).then(function (response) {
                        obj.commit('SET_TOKEN', '');
                        obj.commit('SET_ROLES', []);
						obj.commit('SET_MENULIST', []);
						obj.commit('SET_ROUTERLIST', []);
						obj.commit('SET_NAME', '');
						obj.commit('SET_UNREADMSGNUM', '');
                        removeToken()
                        resolve()
                    }).catch(function (error) {
                        reject(error)
                    })
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
