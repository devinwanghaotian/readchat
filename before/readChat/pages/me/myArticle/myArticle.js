const req = require('../../../api/myArticle.js')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        article: [],
        pageNo: 1,
        pageSize: 10,
        loading: true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let self = this
        let params = {
            pageNo: self.data.pageNo,
            pageSize: self.data.pageSize
        }
        req.getMyArticleByPage(params).then((res) => {
            if (res.data.article == null || res.data.article.length < self.data.pageSize) {
                this.setData({
                    article: res.data.article,
                    loading: false,
                    pageNo: self.data.pageNo + 1
                })
            } else {
                this.setData({
                    article: res.data.article,
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
    onReachBottom: function () {
        let self = this
        let params = {
            pageSize: self.data.pageSize,
            pageNo: self.data.pageNo
        }
        req.getMyArticleByPage(params).then((res) => {
            // console.log(res)
            if (res.data.article == null || res.data.article.length < self.data.pageSize) {
                this.setData({
                    article: self.data.article.concat(res.data.article),
                    loading: false,
                    pageNo: self.data.pageNo + 1
                })
            } else {
                this.setData({
                    article: self.data.article.concat(res.data.article),
                    pageNo: self.data.pageNo + 1
                })
            }
        }).catch((res) => {
            console.log(res)
        })
    },

    // 点击文章实现跳转到“文章界面”
    clickArticle: function (res) {
        wx.navigateTo({
            url: '../../article_detail/article_detail?articleId=' + res.currentTarget.dataset.id,
        })
    },

    

    delete(e) {
        let self = this
        wx.showModal({
            title: '提示',
            content: '您确定要删除吗？',
            success: function (res) {
                if (res.confirm) { //这里是点击了确定以后
                    let params = {
                        articleId: e.currentTarget.dataset.id
                    }
                    req.deleteArticleById(params).then((res) => {
                        wx.showToast({
                            title: '删除成功',
                            icon: 'none',
                            duration: 1500 //持续的时间
                        })
                        // 重新加载数据
                        self.setData({
                            article: [],
                            pageNo: 1,
                            pageSize: 10,
                            loading: true
                        })
                        let data = {
                            pageSize: self.data.pageSize,
                            pageNo: self.data.pageNo
                        }
                        req.getMyArticleByPage(data).then((res) => {
                            if (res.data.article == null || res.data.article.length < self.data.pageSize) {
                                self.setData({
                                    article: res.data.article,
                                    loading: false,
                                    pageNo: self.data.pageNo + 1
                                })
                            } else {
                                self.setData({
                                    article: res.data.article,
                                    pageNo: self.data.pageNo + 1
                                })
                            }
                        }).catch((res) => {
                            console.log(res)
                        })
                    }).catch((res) => {
                        console.log(res)
                    })
                } else { //这里是点击了取消以后
                }
            }
        })
    }

})