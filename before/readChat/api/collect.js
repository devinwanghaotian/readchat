const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');

// 分页获取收藏的数据
function getCollectByPage(params) {
    return getRequest({
      url: '/article/getCollectByPage?pageNo=' + params.pageNo + "&pageSize=" + params.pageSize 
    })
}

module.exports = {
    getCollectByPage
}