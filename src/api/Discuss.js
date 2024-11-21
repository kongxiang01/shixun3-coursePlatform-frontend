import request from '../utils/request'
//发起讨论
export const createDiscussion=(data)=>{
    return request.post('/api/discussion/createDiscussion',data)
}

// export const getAllDiscussions = (params) => {
//     return request.get('/api/discussion/getAllDiscussions', { params })
// }
// 获取讨论详情
export const getDiscussionDetails = (data) => {
    return request.post('/api/discussion/getDiscussionDetails', data)
}

// 回复讨论
export const createComment = (data) => {
    return request.post('/api/discussion/createComment', data)
}
// 获取所有讨论
export const getDiscussions = () => {
    return request.get('/api/discussion/getAllDiscussions');
};
// 获取所有评论
export const getComments = (discussionUuid) => {
    console.log('Sending request with discussionuuid:', discussionUuid);
    return request.get('/api/discussion/getAllComments', {
        params: {
            discussionUuid: discussionUuid // 以查询参数形式传递
        }
    });
};
/*export const getComments = (discussionUuid) => {
    return request.get('/api/discussion/getAllComments',    discussionUuid);
};*/
// 删除讨论
export const deleteDiscussionApi = (data) => {
    return request.post('/api/discussion/deleteDiscussion', data)
}