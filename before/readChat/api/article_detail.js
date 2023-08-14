const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');
// 得到文章信息
function getUserAndArticle(params) {
    return getRequest({
        url: '/article/getUserAndArticle?articleId=' + params.articleId
    })
}

// 增加阅读量
function addPageViews(params) {
    return postRequest({
        url:'/article/addPageViews',
        data: params
    })
}

// 查看当前用户是否点赞
function isLike(params) {
    return postRequest({
        url:'/article/isLike',
        data: params
    })
}

// 查看当前文章的点赞数
function getLikeNum(params) {
    return postRequest({
        url:'/article/getLikeNum',
        data: params
    })
}

// 点击点赞按钮
function tapLike(params) {
    return postRequest({
        url: '/article/tapLike',
        data: params
    })
}

// 查看当前文章的收藏数
function getCollectNum(params) {
    return postRequest({
        url: '/article/getCollectNum',
        data: params
    })
}

// 点击收藏操作
function tapCollect(params) {
    return postRequest({
        url: '/article/tapCollect',
        data: params
    })
}

// 点击关注操作
function tapAttention(params) {
    return postRequest({
        url: '/article/tapAttention',
        data: params
    })
}

// 获取评论的数据
function getComments(params) {
    return getRequest({
        url: '/article/getComments?articleId=' + params.articleId
    })
}

// 提交评论
function submitComment(params) {
    return postRequest({
        url: "/article/submitComment",
        data: params
    })
}


module.exports = {
    getUserAndArticle,
    addPageViews,
    isLike,
    getLikeNum,
    tapLike,
    getCollectNum,
    tapCollect,
    tapAttention,
    getComments,
    submitComment
}