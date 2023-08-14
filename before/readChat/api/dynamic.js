const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');

// 分页获取收藏的数据
function getDynamicByPage(params) {
    return getRequest({
      url: '/article/getDynamicByPage?pageNo=' + params.pageNo + "&pageSize=" + params.pageSize 
    })
}

module.exports = {
  getDynamicByPage
}