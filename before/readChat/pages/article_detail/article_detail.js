const req = require("../../api/article_detail.js")
const BASE_URL = 'https://rc.devinwang.club'
Page({

    /**
     * 页面的初始数据
     */
    data: {
        user: {
            openid: "",
            avatarUrl: "",
            nickName: "",
            gmtCreate: "",
            motto: ""
        },
        article: {
            time: '',
            articleContent: '',
        },
        attention: false,
        articleId: undefined,
        loading: true,
        like: false,
        collect: false,
        likeNum: 0,
        collectNum: 0,

        //评论数据
        comment_list: [],

        //回复数据
        comment_list2: [],

        /*定义一些数据*/
        focus: false, //输入框是否聚焦
        placeholder: '说点什么...', //底部输入框占字符
        comment_text: null, //底部评论框内容

        /*
         *以下初始化数据是用户点击任意一条评论或回复时需要设置的数据
         *然后将设置好的数据传递给评论时新创建的评论数据对象
         */
        now_reply_name: null, //当前点击的评论或回复评论的用户昵称
        now_reply_type: 0, //当前回复类型 默认为0 1为回复评论 2为回复回复
        now_parent_id: 0, //当前点击的评论或回复评论的所属评论id
        now_reply: 0, //当前点击的评论或回复评论的id
    },

    //底部输入框提交内容时触发
    confirm(e) {
        let self = this
        //获取输入框输入的内容
        var comment_text = e.detail.value;
        //判断用户是否输入内容为空
        if (comment_text == '') {
            //用户评论输入内容为空时弹出
            wx.showToast({
                title: '请输入内容', //提示内容
                icon: 'none' //提示图标
            })
        } else {
            var reply_name = null; //回复评论用户的昵称
            var parent_id = 0; //评论所属哪个评论的id
            var reply_id = this.data.now_reply; //回复谁的评论id
            //通过回复谁的评论id判断现在是评论还是回复
            if (reply_id != 0) {
                //现在是回复
                var reply_type = this.data.now_reply_type; //回复类型
                //通过回复类型判断是回复评论还是回复回复
                if (reply_type == 1) {
                    //回复评论
                    parent_id = this.data.now_reply; //回复评论所属评论id
                    reply_name = this.data.now_reply_name; //回复评论用户昵称
                } else {
                    //回复回复
                    parent_id = this.data.now_parent_id; //回复评论所属评论id
                    reply_name = this.data.now_reply_name; //回复评论用户昵称
                }
            } else {
                //现在是评论
            }
            var params = {} //评论/回复对象
            params.articleId = self.data.articleId //文章的id
            params.content = comment_text; //评论内容      
            params.replyId = reply_id; //回复谁的评论的id      
            params.pid = parent_id; //评论所属哪个评论id      
            params.replyName = reply_name; //回复评论人的昵称
            req.submitComment(params).then((res) => {
                console.log(res)
                let comment_list2 = self.data.comment_list2
                let comment_list = self.data.comment_list
                if (res.data.comment.pid > 0) {
                    //回复
                    comment_list2.unshift(res.data.comment);
                } else {
                    //评论
                    comment_list.unshift(res.data.comment);
                }
                //动态渲染
                self.setData({
                    //发表评论后将以下数据初始化 为下次发表评论做准备
                    comment_text: null, //评论内容        
                    now_reply: 0, //当前点击的评论id        
                    now_reply_name: null, //当前点击的评论的用户昵称        
                    now_reply_type: 0, //评论类型        
                    now_parent_id: 0, //当前点击的评论所属哪个评论id        
                    placeholder: "说点什么...", //输入框占字符
                    //将加入新数据的数组渲染到页面        
                    comment_list, //评论列表        
                    comment_list2 //回复列表
                })
            }).catch((res) => {
                console.log(res)
            })

        }
    },

    //点击用户评论或回复时触发
    replyComment(e) {
        var cid = e.currentTarget.dataset.cid; //当前点击的评论id
        var name = e.currentTarget.dataset.name; //当前点击的评论昵称
        var pid = e.currentTarget.dataset.pid; //当前点击的评论所属评论id
        var type = e.currentTarget.dataset.type; //当前回复类型
        this.setData({
            focus: true, //输入框获取焦点
            placeholder: '回复' + name + '：', //更改底部输入框占字符
            now_reply: cid, //当前点击的评论或回复评论id
            now_reply_name: name, //当前点击的评论或回复评论的用户名
            now_parent_id: pid, //当前点击的评论或回复评论所属id
            now_reply_type: type, //获取类型(1回复评论/2回复-回复评论)
        })
    },

    //下面的方法可以绑定在输入框的bindblur属性上	
    blur(e) {
        const text = e.detail.value.trim();
        if (text == '') {
            this.setData({
                now_reply: 0, //当前点击的评论或回复评论的id        
                now_reply_name: null, //当前点击的评论或回复评论的用户昵称        
                now_reply_type: 0, //当前回复类型        
                now_parent_id: 0, //当前点击的评论或回复评论的所属评论id        
                placeholder: "说点什么呢，万一火了呢", //占字符        
                focus: false //输入框获取焦点
            })
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad(options) {
        let self = this
        // 获取用户信息和文章信息
        let params = {
            articleId: options.articleId
        }
        // 将articleId存储在本页面
        this.setData({
            articleId: options.articleId
        })
        req.getUserAndArticle(params).then((res) => {
            let userAndArticle = res.data.userAndArticle
            let user = {
                avatarUrl: userAndArticle.avatarUrl,
                nickName: userAndArticle.nickName,
                gmtCreate: userAndArticle.userGmtCreate,
                motto: userAndArticle.motto,
                openid: userAndArticle.openid
            }
            let article = {
                time: userAndArticle.articleGmtCreate,
                articleContent: userAndArticle.articleContent
            }
            self.setData({
                user: user,
                article: article,
                loading: false
            })
            // 查看是否对当前用户关注
            wx.request({
                url: BASE_URL + "/article/isAttention?otherOpenid=" + userAndArticle.openid,
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
        }).catch((res) => {})
        // 增加浏览次数
        req.addPageViews(params).then((res) => {
            // 将上一个页面的浏览次数加一
            let pages = getCurrentPages()
            let prevPage = pages[pages.length - 2]
            let article = prevPage.data.article
            for (let i = 0; i < article.length; i++) {
                if (article[i].articleId == options.articleId) {
                    console.log("数值加1")
                    article[i].pageViews += 1
                    break
                }
            }
            prevPage.setData({
                article: article
            })
        }).catch((res) => console.log(res))

        // 查看该用户是否点赞
        wx.request({
            url: BASE_URL + "/article/isLike",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded',
                'token': wx.getStorageSync('token') || ''
            },
            data: params,
            success(res) {
                if (res.data != null && res.data.data != null && res.data.data.isLike == true) {
                    self.setData({
                        like: true
                    })
                }
            },
            fail(res) {

            }
        })
        // 查看该文章的点赞数量
        req.getLikeNum(params).then((res) => {
            self.setData({
                likeNum: res.data.likeNum
            })
        }).catch((res) => {
            console.log(res)
        })
        // 查看该用户是否收藏
        wx.request({
            url: BASE_URL + "/article/isCollect",
            method: 'POST',
            header: {
                'content-type': 'application/x-www-form-urlencoded',
                'token': wx.getStorageSync('token') || ''
            },
            data: params,
            success(res) {
                if (res.data != null && res.data.data != null && res.data.data.collect == true) {
                    self.setData({
                        collect: true
                    })
                }
            },
            fail(res) {

            }
        })
        // 查看该文章的收藏量
        req.getCollectNum(params).then((res) => {
            self.setData({
                collectNum: res.data.collectNum
            })
        }).catch((res) => {
            console.log(res)
        })

        // 查看当前文章的评论
        req.getComments(params).then((res) => {
            self.setData({
                comment_list: res.data.comment_list,
                comment_list2: res.data.comment_list_reply
            })
        }).catch((res) => {
            console.log(res)
        })

    },

    // 跳转到用户界面
    // 点击用户信息实现跳转到“用户界面”
    clickUser: function (res) {
        let self = this
        wx.navigateTo({
            url: '../user_detail/user_detail?id=' + self.data.user.openid
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

    // 点赞
    tapLike() {
        let self = this
        let params = {
            articleId: self.data.articleId,
            like: self.data.like
        }
        req.tapLike(params).then((res) => {
            self.setData({
                like: res.data.like
            })
            if (res.data.like) {
                self.setData({
                    likeNum: self.data.likeNum + 1
                })
                wx.showToast({
                    title: '点赞成功🤩',
                    icon: 'none',
                    duration: 1500 //持续的时间
                })
            } else {
                self.setData({
                    likeNum: self.data.likeNum - 1
                })
            }
        }).catch((res) => {
            console.log(res)
        })
    },

    // 收藏
    tapCollect(e) {
        let self = this
        let params = {
            articleId: self.data.articleId,
            collect: self.data.collect
        }
        req.tapCollect(params).then((res) => {
            self.setData({
                collect: res.data.collect
            })
            if (res.data.collect) {
                self.setData({
                    collectNum: self.data.collectNum + 1
                })
                wx.showToast({
                    title: '收藏成功🤩',
                    icon: 'none',
                    duration: 1500 //持续的时间
                })
            } else {
                self.setData({
                    collectNum: self.data.collectNum - 1
                })
            }
        })
    },

    /**
     * 生命周期函数--监听页面初次渲染完成
     */
    onReady() {

    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow() {

    },

    /**
     * 生命周期函数--监听页面隐藏
     */
    onHide() {

    },

    /**
     * 生命周期函数--监听页面卸载
     */
    onUnload() {

    },

    /**
     * 页面相关事件处理函数--监听用户下拉动作
     */
    onPullDownRefresh() {

    },

    /**
     * 页面上拉触底事件的处理函数
     */
    onReachBottom() {

    },

    /**
     * 用户点击右上角分享
     */
    onShareAppMessage() {

    }
})