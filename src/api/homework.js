import request from "@/utils/request.js";
import {getPreviewFileService} from "@/api/user.js";

export const getAssignedHomeworkListService = ( cid ) =>
    request.post('/api/homework/display/homework', { cid } )

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

export const getSubmittedHomeworkListService = ( cid ) =>
    request.post('/api/homework/display', { cid: cid } )

export const getDownloadAssignedService = ( fileName ) =>
    request.get('/api/homework/generateDownloadLink', { params: {fileName: fileName} })

export const getHomeworkPreviewUrlService = ( workid ) =>
    request.get('/api/homework/preview/assign', { params: {workid: workid} })

export const correctHomeworkService = ( workid ) =>
    request.get('/api/homework/preview/assign', { params: {workid: workid} })