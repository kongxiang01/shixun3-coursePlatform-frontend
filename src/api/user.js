import request from '../utils/request'

// 获取用户基本信息
export const userGetInfoService = () => request.get('/api/userinfo')

// 更新用户基本信息
export const userUpdateInfoService = ({ id, nickname, email }) =>
    request.put('/my/userinfo', { id, nickname, email })

// 更新用户头像
export const userUpdateAvatarService = (avatar) =>
    request.patch('/my/update/avatar', { avatar })

// 登录接口
export const userLoginService = ({ username, password }) =>
    request.post('/api/login', { username, password })

export const changePasswordService = ( password ) =>
    request.post('/api/change', { password: password })







export const getDirectoryService = ( path ) =>
    request.get('/api/assets/catalogue', { params: {path: path} })

export const getDownloadFileService = ( filePath ) =>
    request.get('/api/assets/generateDownloadLink', { params: {fileName: filePath} })

export const getCourseListService = ( sno ) =>
    request.post('/api/course', { username: sno } )

export const getHomeworkListService = ( cid, sno ) =>
    request.post('/api/course/homework/display', { cid, sno } )

export const getCourseInfoService = (cid, cno) =>
    request.post('/api/course/info', {}, { params: { cid, cno } })

export const uploadCourseWareService = (file, cid) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    return request.post('/api/assets/upload', formData)
}

export const createFolderService = ( folderPath ) =>
    request.post('/api/assets/folder/create', { folderPath } )


// // 获取作业列表（学生查看可以评价的作业）
// export const getAssignmentListService = (courseId) =>
//     request.get('/api/peerEvaluation/assignments', { params: { courseId } })
//
// // 获取作业详情（弹窗显示的详细作业内容，不包含学生姓名）
// export const getAssignmentDetailsService = (assignmentId) =>
//     request.get(`/api/peerEvaluation/assignment/${assignmentId}`)
//
// // 提交评价（学生对作业进行评分和评语）
// export const submitEvaluationService = (assignmentId, score, comment) =>
//     request.post(`/api/peerEvaluation/assignment/${assignmentId}/evaluate`, {
//         score,
//         comment
//     })
//
// // 查看评价结果（学生查看自己对作业的评价，或可以在此基础上修改）
// export const getEvaluationResultService = (assignmentId) =>
//     request.get(`/api/peerEvaluation/assignment/${assignmentId}/evaluationResult`)
//
// // 修改评价（学生可以更新已提交的评价）
// export const updateEvaluationService = (assignmentId, score, comment) =>
//     request.put(`/api/peerEvaluation/assignment/${assignmentId}/updateEvaluation`, {
//         score,
//         comment
//     })


//展示课程作业信息
    export const getCourseHomework = ( reviewerSno, cid, workid ) =>
        request.post('/api/student/peerDisplay', { reviewerSno, cid, workid} )




    // 课程作业下载
        export const peerDownload = (reviewersno) => {
        return request.get('/api/student/peerDownload',

            {reviewersno})
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