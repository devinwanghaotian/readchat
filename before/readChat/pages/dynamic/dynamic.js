const req = require("../../api/dynamic.js")
Page({

  /**
   * 页面的初始数据
   */
  data: {
    article: [],
    loading: true,
    pageSize: 10,
    pageNo: 1
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
    req.getDynamicByPage(params).then((res) => {
      let article = res.data.article
      if (article.length == 0) {
        this.setData({
          loading: false
        })
      } else {
        this.setData({
          article: article,
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
    req.getDynamicByPage(params).then((res) => {
      let article = res.data.article
      if (article.length == 0) {
        this.setData({
          loading: false
        })
      } else {
        this.setData({
          article: self.data.article.concat(article),
          pageNo: self.data.pageNo + 1
        })
      }
    }).catch((res) => {
      console.log(res)
    })
  },
  // 点击用户信息实现跳转到“用户界面”
  clickUser: function (res) {
    // console.log(res.currentTarget.id)
    wx.navigateTo({
      url: '../user_detail/user_detail?id=' + res.currentTarget.id
    })
  },

  // 点击文章实现跳转到“文章界面”
  clickArticle: function (res) {
    // console.log(res)
    wx.navigateTo({
      url: '../article_detail/article_detail?articleId=' + res.currentTarget.id,
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
      pageNo: 1
    })

    // 重新请求数据
    let self = this
    let params = {
      pageSize: self.data.pageSize,
      pageNo: self.data.pageNo
    }
    req.getDynamicByPage(params).then((res) => {
      let article = res.data.article
      if (article.length == 0) {
        this.setData({
          loading: false
        })
      } else {
        this.setData({
          article: article,
          pageNo: self.data.pageNo + 1
        })
      }
      wx.hideLoading();
      wx.hideNavigationBarLoading();
      //停止下拉刷新
      wx.stopPullDownRefresh();
    }).catch((res) => {
      console.log(res)
      wx.hideLoading();
      wx.hideNavigationBarLoading();
      //停止下拉刷新
      wx.stopPullDownRefresh();
    })
  }
})