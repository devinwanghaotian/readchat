const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');

// 分页获取我的评论的数据
function getMyArticleByPage(params) {
    return getRequest({
      url: '/article/getMyArticleByPage?pageNo=' + params.pageNo + "&pageSize=" + params.pageSize 
    })
}
// 删除评论
function deleteArticleById(params) {
    return postRequest({
        url: '/article/deleteArticleById',
        data: params
    })
}

module.exports = {
    getMyArticleByPage,
    deleteArticleById
}