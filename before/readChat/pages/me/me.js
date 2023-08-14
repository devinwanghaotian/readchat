const app = getApp();
const req = require('../../api/login.js')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        nickName: wx.getStorageSync('userInfo').nickName || "默认用户",
        avatarUrl: wx.getStorageSync('userInfo').avatarUrl || "https://devinwang.oss-cn-beijing.aliyuncs.com/spaceman.png"
    },
    // 登录
    async login() {

        const userInfo = await app.login();
        wx.setStorage({
            key: "userInfo",
            data: userInfo
        })
        this.setData({
            show: true,
            nickName: userInfo.nickName,
            avatarUrl: userInfo.avatarUrl
        })
        wx.showToast({
            title: '登录成功',
            icon: 'success',
            duration: 800 //持续的时间
        })

    },

    logout() {
        req.logout().catch(err => console.log(err));
        wx.clearStorage();
        this.setData({
            show: false,
            nickName: "默认用户",
            avatarUrl: "https://devinwang.oss-cn-beijing.aliyuncs.com/spaceman.png"
        })
    },
    // 跳转到我的评论界面
    myComment() {
        wx.navigateTo({
            url: './myComment/myComment'
        })
    },

    // 跳转到我的文章界面
    myArticle() {
        wx.navigateTo({
            url: './myArticle/myArticle'
        })
    },

    // 跳转到收藏夹页面
    toCollectPage() {
        wx.navigateTo({
            url: './collect/collect'
        })
    }

})