// app.js
const req = require('./api/login.js')
App({
  onLaunch() {},

  async login() {
    wx.getStorageSync('token') || '';
    const isLogin = await isAuth();
    if (isLogin == 0) {
      await handleLogin();
    }
    return await getMessage();
  },
  globalData: {
    userInfo: null,
    BASE_URL: "https://rc.devinwang.club"
  }
})

// 验证token
function isAuth() {
  return new Promise((resolve, reject) => {
    req.isAuth().then(value => {
      resolve(1);
    }).catch((reason) => {
      resolve(0);
    })
  })
}
// 登录操作
function handleLogin() {
  const login = new Promise((resolve, reject) => {
    // 得到code
    wx.login({
      success(res) {
        let params = {
          code: res.code
        };
        resolve(params);
      }
    })
  })
  return login.then((params) => {
    return new Promise((resolve, reject) => {
      // 得到token
      req.login(params).then((res) => {
        wx.setStorage({
          key: "token",
          data: res.data.token
        })
        resolve(res.data.token)
      })
    })
  })
}

// 得到用户相关信息
function getMessage() {
  return new Promise((resolve, reject) => {
    // 显示弹框
    wx.showModal({
      title: '提示',
      content: '确定获取您的头像和昵称吗？',
      success: function (res) {
        if (res.confirm) {
          resolve();
        } else {
          reject();
        }
      }
    })
  }).then((res) => {
    // 得到 iv 和 encryptedData
    return new Promise((resolve, reject) => {
      wx.getUserProfile({
        desc: '用于完善会员资料',
        success: (res) => {
          let params = {
            encryptedData: res.encryptedData,
            iv: res.iv
          }
          resolve(params);
        }
      })
    })
  }).then((params) => {
    // 向后端发送请求
    return new Promise((resolve, reject) => {
      req.getMessage(params).then(res => {
        // app.globalData.userInfo = res.data.userInfo;
        resolve(res.data.userInfo);
      })
    })
  })
}