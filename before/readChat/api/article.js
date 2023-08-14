const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');
// 保存文章信息
function upload(params) {
    return postRequest({
        url: '/article/upload',
        data: params
    })
}
function getByPage(params) {
    return getRequest({
        url: '/article/getByPage?currentIdx=' + params.currentIdx + "&pageSize=" + params.pageSize
    })
}

function getByOpenidAndPage(params) {
    return getRequest({
        url: '/article/getByOpenidAndPage?openid=' 
        + params.openid + "&currentIdx=" + params.currentIdx + "&pageSize=" + params.pageSize
    })
}

module.exports = {
    upload,
    getByPage,
    getByOpenidAndPage
}