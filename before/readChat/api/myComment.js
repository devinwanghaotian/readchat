const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');

// 分页获取我的评论的数据
function getMyCommentByPage(params) {
    return getRequest({
      url: '/article/getMyCommentByPage?pageNo=' + params.pageNo + "&pageSize=" + params.pageSize 
    })
}
// 删除评论
function deleteCommentById(params) {
    return postRequest({
        url: '/article/deleteCommentById',
        data: params
    })
}

module.exports = {
    getMyCommentByPage,
    deleteCommentById
}