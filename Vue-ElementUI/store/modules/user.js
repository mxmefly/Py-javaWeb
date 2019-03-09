define(function (require, exports, module) {
	var Layout = httpVueLoader('views/layout/Layout.vue');
	var Page404 = httpVueLoader('views/404.vue');
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
                        var result = response.data.success;
                        if (result) {
							/* cookie 里放的是 sessionId 实现前后端登陆状态的统一 */
                            setToken(response.data.data.sessionId);
                            obj.commit('SET_TOKEN', userInfo.username);
                            obj.commit('SET_MENULIST', response.data.data.menuList);
                            obj.commit('SET_ROUTERLIST', getRouters(response.data.data.routList));
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
                        var data = response.data;
						console.log("data",data)
						/* console.log("menuList",getMenuList(data.data.menuList)) */
						if(data.success){
							// console.log("routerList",getRouters(data.data.routList))
							obj.commit('SET_MENULIST', data.data.menuList);
							obj.commit('SET_ROUTERLIST', getRouters(data.data.routList));
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
                        obj.commit('SET_TOKEN', '')
                        obj.commit('SET_ROLES', [])
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
	
    var getRouters = function(RouList){
		var routerArr = {
		    path: '/',
		    component: Layout,
		    children: []
		};
		for(var i=0;i<RouList.length;i++){
			var arr= {
                path: '/' + RouList[i].id,
                name: RouList[i].name,
                component: httpVueLoader("../../"+RouList[i].path)
            }
			routerArr.children.push(arr);
		}
		var routerList=[];
		routerList.push(routerArr);
		routerList.push({path: '*', redirect: '/404', hidden: true});
		return routerList;
	}
    module.exports = user
})
