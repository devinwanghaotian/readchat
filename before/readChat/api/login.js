const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');
// 验证token是否过期
function isAuth() {
  return getRequest({
    url: '/login/auth'
  })
}

// 登录
function login(params) {
    return postRequest({
        url: '/login',
        data: params
    })
}
// 得到用户信息
function getMessage(params) {
    return postRequest({
        url: '/login/message',
        data: params
    })
}

// 退出登录
function logout() {
    return getRequest({
        url: '/login/logout'
    })
}

// 尝试发送请求
function hello() {
    return getRequest({
        url: '/hello'
    })
}
module.exports = {
  isAuth,
  login,
  getMessage,
  logout,
  hello
}