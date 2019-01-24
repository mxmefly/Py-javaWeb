define(function(require, exports, module) {
  //import { login, logout, getInfo } from '@/api/login'
  //import { getToken, setToken, removeToken } from '@/utils/auth'
  var loginAPI = require('../../api/login.js');
  var login = loginAPI.login;
  var logout = loginAPI.logout;
  var getInfo = loginAPI.getInfo;

  var auth = require('../../utils/auth.js');
  var getToken = auth.getToken;
  var setToken = auth.setToken;
  var removeToken = auth.removeToken;

  const user = {
    state: {
      token: getToken(),
      name: '',
      avatar: '',
      roles: []
    },

    mutations: {
      SET_TOKEN: function(state, token) {
        state.token = token
      },
      SET_NAME: function(state, name) {
        state.name = name
      },
      SET_AVATAR: function(state, avatar) {
        state.avatar = avatar
      },
      SET_ROLES: function(state, roles) {
        state.roles = roles
      }
    },

    actions: {
      // 登录
      Login: function(obj, userInfo) {
        console.log("________login1______",userInfo)
        const username = userInfo.username.trim()
        return new Promise(function(resolve, reject) {
          console.log("=======login api======")
          login(username, userInfo.password).then(function(response) {
            console.log(response)
            const data = response.data
            setToken(data.token)
            obj.commit('SET_TOKEN', data.token)
            resolve()
          }).catch(function(error) {
            reject(error)
          })
        })
      },

      // 获取用户信息
      GetInfo: function(obj) {
        return new Promise(function(resolve, reject) {
          getInfo(obj.state.token).then(function(response) {
            const data = response.data
            if (data.roles && data.roles.length > 0) { // 验证返回的roles是否是一个非空数组
              obj.commit('SET_ROLES', data.roles)
            } else {
              reject('getInfo: roles must be a non-null array !')
            }
            obj.commit('SET_NAME', data.name)
            obj.commit('SET_AVATAR', data.avatar)
            resolve(response)
          }).catch(function(error) {
            reject(error)
          })
        })
      },

      // 登出
      LogOut: function(obj) {
        return new Promise(function(resolve, reject) {
          logout(obj.state.token).then(function() {
            obj.commit('SET_TOKEN', '')
            obj.commit('SET_ROLES', [])
            removeToken()
            resolve()
          }).catch(function(error) {
            reject(error)
          })
        })
      },

      // 前端 登出
      FedLogOut: function(obj) {
        return new Promise(function(resolve) {
          obj.commit('SET_TOKEN', '')
          removeToken()
          resolve()
        })
      }
    }
  }

  module.exports = user
})
