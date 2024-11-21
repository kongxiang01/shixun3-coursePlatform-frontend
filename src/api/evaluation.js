import request from '../utils/request'

//展示课程作业信息
export const getCourseHomework = ( reviewerSno, cid, workid ) =>
    request.post('/api/student/peerDisplay', { reviewerSno, cid, workid} )




// 课程作业下载
export const peerDownload = ( revieweeSno, cid, workid ) => {
    return request.get('/api/student/generatePeerDownloadLink', {params: { revieweeSno, cid, workid }})
}

// 提交作业评分
export const submitReview = (reviewData) => {
    return request.post('/api/student/submitReview', reviewData)
}

// 修改作业评分
export const modifyReview = ( reviewData) => {
    return request.post('/api/student/modifyReview', reviewData)
}

// 展示作业评分
export const viewPeerReviews = (reviewData) => {
    return request.post('/api/student/viewPeerReviews', reviewData)
}