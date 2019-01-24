define(function(require, exports, module) {
  const getters = {
    sidebar: function(state){ return state.app.sidebar},
    device: function(state){ return state.app.device},
    token: function(state){ return state.user.token},
    avatar: function(state){ return state.user.avatar},
    name: function(state){ return state.user.name},
    roles: function(state){ return state.user.roles}
  }
  module.exports = getters
})
