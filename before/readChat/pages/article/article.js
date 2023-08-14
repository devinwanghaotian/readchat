// var time = require('../../../utils/util.js');
const app = getApp();
const recorderManager = wx.getRecorderManager()
const req = require("../../api/article.js")
Page({
    data: {
        formats: {},
        bottom: 0,
        readOnly: false,
        placeholder: '开始输入...',
        _focus: false,
        navTop: 0,
        themeMessage: [],
        speech: false,
        src: ""
    },
    //预览
    priview() {
        this.editorCtx.getContents({
            success(res) {
                wx.setStorageSync("priviewHtml", res.html);
                wx.navigateTo({
                    url: '/pages/article/add_deails/add_details',
                });
            }
        })
    },
    onLoad(options) {
        let that = this;
        wx.showToast({
            title: '已开启自动保存',
            icon: "none"
        })

        wx.createSelectorQuery().select('#editor').context(function (res) {
            that.editorCtx = res.context
            that.editorCtx.setContents({
                "html": wx.getStorageSync("priviewHtml")
            })
        }).exec()
    },
    // 提交文章
    formSubmit(e) {
        let that = this
        this.editorCtx.getContents({
            success(res) {

                if (res.html == "<p><br></p>") {
                    wx.showToast({
                        title: '输入点东西吧',
                        icon: 'none',
                        image: '',
                        duration: 1500,
                        mask: false,
                    });
                    return
                }

                wx.showModal({
                    title: '提示',
                    content: '确定发布么？',
                    showCancel: true,
                    cancelText: '取消',
                    cancelColor: '#000000',
                    confirmText: '确定',
                    confirmColor: '#3CC51F',
                    success: (result) => {

                        if (result.confirm) {
                            wx.showLoading({
                                title: "发布中",
                            });
                            let params = { articleContent: res.html }
                            req.upload(params).then((res) => {
                                wx.hideLoading();
                                wx.redirectTo({
                                    url: '/pages/index/index'
                                });
                                wx.setStorageSync("priviewHtml", "");
                            }).catch(res => {
                                console.log(res);
                                wx.hideLoading();
                                wx.showToast({
                                    title: res.message,
                                    icon: "error",
                                    duration: 1500
                                  })
                            })
                        }
                    },
                })
            }
        })
    },

    readOnlyChange() {
        this.setData({
            readOnly: !this.data.readOnly
        })
    },
    onEditorReady() {
        const that = this
        wx.createSelectorQuery().select('#editor').context(function (res) {
            that.editorCtx = res.context
        }).exec()
    },

    // 撤回
    undo() {
        this.editorCtx.undo()
    },
    // 前进
    redo() {
        this.editorCtx.redo()
    },
    format(e) {
        let {
            name,
            value
        } = e.target.dataset
        if (!name) return
        // console.log('format', name, value)
        this.editorCtx.format(name, value)

    },
    onStatusChange(e) {
        const formats = e.detail
        this.setData({
            formats: formats
        })
    },
    // 加入分割线
    insertDivider() {
        this.editorCtx.insertDivider({
            success: function () {
                console.log('insert divider success')
            }
        })
    },
    clear() {
        this.editorCtx.clear({
            success: function (res) {
                console.log("clear success")
            }
        })
    },
    // 清除数据格式
    removeFormat(e) {
        console.log(e)
        this.editorCtx.removeFormat()
    },
    // 增加日期
    insertDate() {
        const date = new Date()
        const formatDate = `${date.getFullYear()}/${date.getMonth() + 1}/${date.getDate()}`
        this.editorCtx.insertText({
            text: formatDate
        })
    },
    input() {
        this.editorCtx.getContents({
            success(res) {
                if (res.html.length != 0) {
                    wx.setStorageSync("priviewHtml", res.html);
                }

            }
        })
    },

    // 图片识别
    ImageRecognition() {
        let self = this
        wx.chooseMedia({
            count: 1,
            mediaType: ['image'],
            sourceType: ['album', 'camera'],
            success(res) {
                wx.uploadFile({
                    filePath: res.tempFiles[0].tempFilePath,
                    name: 'file',
                    url: app.globalData.BASE_URL + '/ocr/upload',
                    header: {
                        "content-type": "multipart/form-data",
                        "token": wx.getStorageSync('token') || ''
                    },
                    success(res) {
                        const message = JSON.parse(res.data).data.message;
                        console.log(message);
                        self.editorCtx.getContents({
                            success(e) {
                                wx.createSelectorQuery().select('#editor').context(function (res) {
                                    self.editorCtx = res.context
                                    self.editorCtx.setContents({
                                        "html": e.html + message
                                    })
                                }).exec()
                            }
                        })
                        wx.hideLoading({})
                    }
                })
            }
        })
    },

    // 插入图片
    insertImage() {
        const self = this;
        wx.chooseImage({
            count: 1,
            sizeType: ["original", "compressed"], // 可以指定是原图还是压缩图，默认二者都有
            sourceType: ["album", "camera"], // 可以指定来源是相册还是相机，默认二者都有
            success: function (res) {
                // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
                var tempFilePaths = res.tempFilePaths
                wx.uploadFile({
                    url: app.globalData.BASE_URL + '/oss/upload',
                    filePath: tempFilePaths[0],
                    name: "file",
                    header: {
                        "content-type": "multipart/form-data",
                        "token": wx.getStorageSync('token') || ''
                    },
                    success: function (res) {
                        console.log(res.data)
                        self.editorCtx.insertImage({
                            src: JSON.parse(res.data).data.url,
                            width: "75%",
                            success: function (e) {
                                wx.hideLoading();
                            }
                        })
                        self.editorCtx.getContents({
                            success(res) {
                                console.log(res.html)
                            }
                        })
                    },
                    fail: function (err) {
                        wx.showToast({
                            title: "上传失败",
                            icon: "none",
                            duration: 2000
                        })
                    }
                })
            }
        })
    }

})