import request from '../utils/request'

// 获取课程列表
export const getCourseListService = ( sno ) =>
    request.post('/api/course', { username: sno } )

//
export const getCourseInfoService = (cid) =>
    request.post('/api/course/info', { cid })

// 上传大纲
export const outlineUploadService = ( file, cid ) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    return request.post('/api/course/outline/upload', formData)
}

// 预览大纲
export const outlinePreviewService = ( cid ) =>
    request.get('/api/course/outline', { params: {cid} })

// 预览教学日历
export const calendarPreviewService = ( cid ) =>
    request.get('/api/course/calendar', { params: {cid} })

// 上传教学日历
export const calendarUploadService = ( file, cid ) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    return request.post('/api/course/calendar/upload', formData)
}

// 上传课程通知
export const notificationUploadService = ( name, cid, nid, tno, time, content ) =>{
    return request.post('/api/notification/upload', { name, cid, nid, tno, time, content })
}

// 删除课程通知
export const deleteNotificationService = ( cid, nid ) =>{
    return request.post('/api/notification/delete', {cid, nid})
}

export const getNotificationListService = ( cid ) =>
    request.post('/api/notification/display', { cid })