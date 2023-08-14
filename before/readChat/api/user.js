const {getRequest} = require('../utils/request');
const {postRequest} = require('../utils/request');

function getMessageById(id) {
    return getRequest({
        url: "/user/getMessage?openid=" + id
    })
}

module.exports = {
    getMessageById
}