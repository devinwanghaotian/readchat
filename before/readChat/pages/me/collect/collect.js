const req = require('../../../api/collect.js')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        loading: true,
        pageNo: 1,
        pageSize: 10,
        article: []
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {
        let self = this
        let params = {
            pageNo: self.data.pageNo,
            pageSize: self.data.pageSize
        }
        req.getCollectByPage(params).then((res) => {

            if (res.data.articleVoList == null || res.data.articleVoList.length < self.data.pageSize) {
                this.setData({
                    article: res.data.articleVoList,
                    loading: false,
                    pageNo: self.data.pageNo + 1
                })
            } else {
                this.setData({
                    article: res.data.articleVoList,
                    pageNo: self.data.pageNo + 1
                })
            }
        }).catch((res) => {
            console.log(res)
        })
    },
    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {
        let self = this
        let params = {
            pageNo: self.data.pageNo,
            pageSize: self.data.pageSize
        }
        req.getCollectByPage(params).then((res) => {
            if (res.data.articleVoList != null && res.data.articleVoList.length == self.data.pageSize) {
                self.setData({
                    article: self.data.article.concat(res.data.articleVoList),
                    pageNo: self.data.pageNo + 1
                })
            } else {
                self.setData({
                    loading: false,
                    article: self.data.article.concat(res.data.articleVoList),
                    pageNo: self.data.pageNo + 1
                })
            }

        }).catch((res) => {
            console.log(res)
        })
    },

    // 跳转到用户界面
    // 点击用户信息实现跳转到“用户界面”
    clickUser: function (res) {
        wx.navigateTo({
            url: '../../user_detail/user_detail?id=' + wx.getStorageSync('userInfo').openid
        })
    },
  // 点击文章实现跳转到“文章界面”
    clickArticle: function (res) {
        wx.navigateTo({
            url: '../../article_detail/article_detail?articleId=' + res.currentTarget.id,
        })
    }

})