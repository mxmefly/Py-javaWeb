define(function(require, exports, module) {
  //import Cookies from 'js-cookie'
  require('../../modules/js-cookie/js.cookie.js');
  const app = {
    state: {
      sidebar: {
        opened: !+Cookies.get('sidebarStatus'),
        withoutAnimation: false
      },
      device: 'desktop'
    },
    mutations: {
      TOGGLE_SIDEBAR: function(state) {
        if (state.sidebar.opened) {
          Cookies.set('sidebarStatus', 1)
        } else {
          Cookies.set('sidebarStatus', 0)
        }
        state.sidebar.opened = !state.sidebar.opened
        state.sidebar.withoutAnimation = false
      },
      CLOSE_SIDEBAR: function(state, withoutAnimation) {
        Cookies.set('sidebarStatus', 1)
        state.sidebar.opened = false
        state.sidebar.withoutAnimation = withoutAnimation
      },
      TOGGLE_DEVICE: function(state, device) {
        state.device = device
      }
    },
    actions: {
      ToggleSideBar: function(obj) {
        obj.commit('TOGGLE_SIDEBAR')
      },
      CloseSideBar: function(obj1, obj2) {
        obj1.commit('CLOSE_SIDEBAR', obj2.withoutAnimation)
      },
      ToggleDevice: function(obj, device) {
        obj.commit('TOGGLE_DEVICE', device)
      }
    }
  }

  module.exports = app
})