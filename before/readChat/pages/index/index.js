const app = getApp()
const req = require('../../api/login.js')
const articleReq = require('../../api/article.js')
Page({
    /**
     * 页面的初始数据
     */
    data: {
        article: [],
        loading: true,
        currentIdx: 9007199254740991,
        pageSize: 10,
        show: true,
        showShow: false
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let accept = wx.getStorageSync('accept') || '';
        let self = this
        if (accept != '') {
            self.setData({
                show: false
            })
        }
        let params = {
            currentIdx: this.data.currentIdx,
            pageSize: this.data.pageSize
        }
        articleReq.getByPage(params).then(res => {
            self.setData({
                article: res.data.list,
                currentIdx: res.data.currentIdx
            })
            if (res.data.list.length === 0) {
                this.setData({
                    loading: false
                })
            }
        }).catch(res => {
            console.log(res)
        })
    },

    onClose1: function (res) {
        this.setData({
            showShow: true
        })
    },

    onClose2: function (res) {
        this.setData({
            show: true
        })
    },
    onConfirm2: function (res) {
        wx.exitMiniProgram({
            success(res) {
                console.log("小程序退出")
            },
            fail(res) {
                console.log("小程序退出失败")
            }
        })
    },
    onConfirm1: function (res) {
        wx.setStorageSync('accept', true)
    },

    // 点击文章实现跳转到“文章界面”
    clickArticle: function (res) {
        // console.log(res)
        wx.navigateTo({
            url: '../article_detail/article_detail?articleId=' + res.currentTarget.id,
        })
    },

    // 点击用户信息实现跳转到“用户界面”
    clickUser: function (res) {
        // console.log(res.currentTarget.id)
        wx.navigateTo({
            url: '../user_detail/user_detail?id=' + res.currentTarget.id
        })
    },

    //  writing 图标点击转到“文章页面”
    writingOnclick: function () {
        req.hello().then(res => {
            wx.redirectTo({
                url: "../../pages/article/article"
            })
        }).catch(res => {});

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom: function () {
        let self = this
        let params = {
            currentIdx: this.data.currentIdx,
            pageSize: this.data.pageSize
        }
        articleReq.getByPage(params).then(res => {
            self.setData({
                article: self.data.article.concat(res.data.list),
                currentIdx: res.data.currentIdx
            })
            if (res.data.list.length === 0) {
                this.setData({
                    loading: false
                })
            }
        }).catch(res => {
            console.log(res)
        })

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh: function () {
        this.onRefresh();
    },

    // 下拉刷新
    onRefresh: function () {
        //导航条加载动画
        wx.showNavigationBarLoading()
        //loading 提示框
        wx.showLoading({
            title: 'Loading...',
        })
        // 重置数据
        this.setData({
            article: [],
            loading: true,
            currentIdx: 9007199254740991
        })

        // 重新请求数据
        let self = this
        let params = {
            currentIdx: this.data.currentIdx,
            pageSize: this.data.pageSize
        }
        articleReq.getByPage(params).then(res => {
            self.setData({
                article: res.data.list,
                currentIdx: res.data.currentIdx
            })
            if (res.data.list.length === 0) {
                this.setData({
                    loading: false
                })
            }
            wx.hideLoading();
            wx.hideNavigationBarLoading();
            //停止下拉刷新
            wx.stopPullDownRefresh();
        }).catch(res => {
            console.log(res)
            wx.hideLoading();
            wx.hideNavigationBarLoading();
            //停止下拉刷新
            wx.stopPullDownRefresh();
        })
    },

})
// 时间格式转换类
// Date.prototype.Format = function (fmt) {
//     var o = {
//         "M+": this.getMonth() + 1, //月份 
//         "d+": this.getDate(), //日 
//         "H+": this.getHours(), //小时 
//         "m+": this.getMinutes(), //分 
//         "s+": this.getSeconds(), //秒 
//         "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
//         "S": this.getMilliseconds() //毫秒 
//     };
//     if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
//     for (var k in o)
//     if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
//     return fmt;
// }