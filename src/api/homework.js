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

// 下载布置的作业
export const getDownloadAssignedService = ( fileName ) =>
    request.get('/api/homework/generateDownloadLink', { params: {fileName: fileName} })

// 预览布置的作业
export const getPreviewFileService = ( fileName ) =>
    request.get('/api/assets/preview', { params: {fileName: fileName} })

// 预览学生提交的作业
export const getHomeworkPreviewService = ( cid, workid, sno ) =>
    request.get('/api/teacher/sendHomework', { params: {cid, workid, sno} })

// 预览学生交的作业
export const correctHomeworkService = ( workid ) =>
    request.get('/api/homework/preview/assign', { params: {workid: workid} })

//
export const deleteAssignedHomeworkService = ( selectedItems ) =>
    request.post('/api/homework/delete', {fileName: selectedItems})