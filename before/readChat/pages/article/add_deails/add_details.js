// pages/article/add_deails/add_details.js
Page({

    /**
     * 页面的初始数据
     */
    data: {
        article: ""
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        this.setData({
            article: wx.getStorageSync("priviewHtml")
        })
    }
})