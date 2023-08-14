// pages/user_detail/user_detail.js
const reqUser = require('../../api/user')
const reqArticle = require("../../api/article")
const req = require("../../api/article_detail")
const BASE_URL = 'https://rc.devinwang.club'
Page({

    /**
     * 页面的初始数据
     */
    /**
     * 页面的初始数据
     */
    data: {
        article: [], //最新的几篇文章
        user: {},
        currentIdx: 9007199254740991,
        pageSize: 10,
        openid: null,
        loading: true,
        attention: false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        // 通过 id 获取用户的信息
        reqUser.getMessageById(options.id).then((res) => {
            console.log(res)
            this.setData({
                user: res.data.user
            })
        }).catch(res => {
            console.log(res)
        })
        this.setData({
            openid: options.id
        })
        // 通过 id 获取与该用户关联的文章
        const params = {
            openid: options.id,
            currentIdx: this.data.currentIdx,
            pageSize: this.data.pageSize
        }
        reqArticle.getByOpenidAndPage(params).then((res) => {
            this.setData({
                article: res.data.list,
                currentIdx: res.data.currentIdx
            })
            console.log(res)
        }).catch((res) => {
            console.log(res)
        })
        let self = this
        // 查看是否对当前用户关注
        wx.request({
            url: BASE_URL + "/article/isAttention?otherOpenid=" + options.id,
            method: "GET",
            header: {
                'token': wx.getStorageSync('token') || ''
            },
            success(res) {
                if (res.data != null && res.data.data != null && res.data.data.attention == true) {
                    self.setData({
                        attention: true
                    })
                }
            }
        })
    },
    // 点击关注
    tapAttention() {
        let self = this
        let params = {
            otherOpenid: self.data.user.openid,
            attention: self.data.attention
        }
        req.tapAttention(params).then((res) => {
            self.setData({
                attention: res.data.attention
            })
            if (res.data.attention == true) {
                wx.showToast({
                    title: '感谢关注😍',
                    icon: 'none',
                    duration: 1500 //持续的时间
                })
            }
        }).catch((res) => {
            console.log(res)
        })
    },

    messageDetail(e) {
        wx.navigateTo({
            url: '../article_detail/article_detail?articleId=' + e.currentTarget.id,
        })
    },
    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        const self = this
        // 通过 id 获取与该用户关联的文章
        const params = {
            openid: self.data.openid,
            currentIdx: self.data.currentIdx,
            pageSize: self.data.pageSize
        }
        reqArticle.getByOpenidAndPage(params).then((res) => {
            self.setData({
                currentIdx: res.data.currentIdx,
                article: self.data.article.concat(res.data.list)
            })
            if (res.data.currentIdx == self.data.currentIdx) {
                this.setData({
                    loading: false
                })
            }
        }).catch(res => console.log(res))
    }
})