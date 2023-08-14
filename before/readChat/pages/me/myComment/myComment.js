// pages/me/myComment/myComment.js
const req = require('../../../api/myComment.js')
Page({

    /**
     * 页面的初始数据
     */
    data: {
        commentList: [],
        pageSize: 10,
        pageNo: 1,
        loading: true
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let self = this
        let params = {
            pageSize: self.data.pageSize,
            pageNo: self.data.pageNo
        }
        req.getMyCommentByPage(params).then((res) => {
            if (res.data.commentList == null || res.data.commentList.length < self.data.pageSize) {
                this.setData({
                    commentList: res.data.commentList,
                    loading: false,
                    pageNo: self.data.pageNo + 1
                })
            } else {
                this.setData({
                    commentList: res.data.commentList,
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
        req.getMyCommentByPage(params).then((res) => {
            if (res.data.commentList == null || res.data.commentList.length < self.data.pageSize) {
                this.setData({
                    commentList: self.data.commentList.concat(res.data.commentList),
                    loading: false,
                    pageNo: self.data.pageNo + 1
                })
            } else {
                this.setData({
                    commentList: self.data.commentList.concat(res.data.commentList),
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

    // 删除评论
    delete(e) {
        let self = this
        wx.showModal({
            title: '提示',
            content: '您确定要删除吗？',
            success: function (res) {
                if (res.confirm) { //这里是点击了确定以后
                    let params = {
                        id: e.currentTarget.dataset.id
                    }
                    req.deleteCommentById(params).then((res) => {
                        if (res.data.success) {
                            wx.showToast({
                                title: '删除成功',
                                icon: 'none',
                                duration: 1500 //持续的时间
                            })
                            // 重新加载数据
                            self.setData({
                                commentList: [],
                                pageSize: 10,
                                pageNo: 1,
                                loading: true
                            })
                            let data = {
                                pageSize: self.data.pageSize,
                                pageNo: self.data.pageNo
                            }
                            req.getMyCommentByPage(data).then((res) => {
                                if (res.data.commentList == null || res.data.commentList.length < self.data.pageSize) {
                                    self.setData({
                                        commentList: res.data.commentList,
                                        loading: false,
                                        pageNo: self.data.pageNo + 1
                                    })
                                } else {
                                    self.setData({
                                        commentList: res.data.commentList,
                                        pageNo: self.data.pageNo + 1
                                    })
                                }
                            }).catch((res) => {
                                console.log(res)
                            })
                        } else {
                            wx.showToast({
                                title: '删除失败',
                                icon: 'none',
                                duration: 1500 //持续的时间
                            })
                        }
                    }).catch((res) => {
                        console.log(res)
                    })
                } else { //这里是点击了取消以后
                }
            }
        })
    }
})