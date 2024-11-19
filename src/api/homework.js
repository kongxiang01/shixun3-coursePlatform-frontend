import request from "@/utils/request.js";

export const assignHomeworkService = (hwFile, start, end, workid, cid, content, fullScore) =>{
    const formData = new FormData();
    formData.append("homework", hwFile);
    formData.append("start", start);
    formData.append("end", end);
    formData.append("workid", workid);
    formData.append("cid", cid);
    formData.append("content", content);
    formData.append("fullmark", fullScore);
    return request.post('/api/homework/assign', formData)
}

// 学生和老师查看布置的作业
export const getAssignedHomeworkListService = ( cid, pno ) =>
    request.post('/api/homework/display', { cid, pno } )

// 老师查看学生交的作业
export const getSubmittedHomeworkListService = ( cid, workid ) =>
    request.post('/api/teacher/displayCorrect', { cid, workid } )
// // 老师查看学生交的作业
// export const getStudentSubmittedHomeworkListService = ( cid, pno ) =>
//     request.post('/api/homework/display', { cid, pno } )

// 下载布置的作业
export const getDownloadAssignedService = ( cid, workid ) =>
    request.get('/api/homework/assign/generateDownloadLink', { params: {cid, workid} })

// 下载学生自己交的作业
export const getDownloadSubmittedService = ( cid, workid, sno ) =>
    request.get('/api/homework/generateDownloadLink', { params: {cid, workid, sno} })// cid workid sno

// 预览布置的作业
export const getPreviewFileService = ( cid, workid ) =>
    request.get('/api/homework/assign/preview', { params: {cid, workid} })

// 学生提交作业
export const submitHomeworkService = ( homeworkFile, cid, sno, workid ) => {
    const formData = new FormData();
    formData.append("homeworkFile", homeworkFile);
    formData.append("cid", cid);
    formData.append("sno", sno);
    formData.append("workid", workid);
    return request.post('/api/homework/upload', formData)
}

// 预览学生提交的作业
export const getHomeworkPreviewService = ( cid, workid, sno ) =>
    request.get('/api/teacher/sendHomework', { params: {cid, workid, sno} })

// 老师删除布置的作业
export const deleteAssignedHomeworkService = ( cid, workid ) =>
    request.post('/api/homework/assign/delete', { cid, workid })


// 老师删除布置的作业
export const deleteAssignedHomeworkService111 = ( cid, workIds ) =>
    request.post('/api/homework/assign/delete', { cid, workIds })

// 学生预览自己交的作业
export const getStudentHomeworkPreviewService = ( cid, sno, workid ) =>
    request.get('/api/homework/preview', { params: {cid, sno, workid } })

// 设置作业是否发布
export const setHomeworkPublishService = ( publish, cid, workid, ) =>
    request.post('/api/homework/assign/update/publish', {publish, cid, workid})

// 设置作业是否公布分数
export const setHomeworkPublishScoreService = ( publishScore, cid, workid ) =>
    request.post('/api/homework/assign/update/publishScore', { publishScore: publishScore, cid, workid })

// 批改作业（设置分数）
export const correctHomeworkScoreService = ( score, cid, workid, sno ) =>
    request.post('/api/teacher/correctHomework', { score, cid, workid, sno })