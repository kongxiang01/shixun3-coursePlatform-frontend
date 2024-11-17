import request from '../utils/request'

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

// 上传教学日历
export const calendarUploadService = ( file, cid ) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    return request.post('/api/course/calendar/upload', formData)
}

// 预览教学日历
export const calendarPreviewService = ( cid ) =>
    request.get('/api/course/calendar', { params: {cid} })

// export const deleteItemsService = ( selectedItems ) =>
//     request.post('/api/assets/delete', selectedItems )

// 上传课程通知
export const notificationUploadService = ( name, cid, tno, uploadTime, content ) =>{
    return request.post('/api/course/notification/upload', { name, cid, tno, uploadTime, content })
}

// 删除课程通知
export const deleteNotificationService = ( cid, nid ) =>{
    return request.post('/api/course/notification/upload', {cid, nid})
}

export const getNotificationListService = ( cid ) =>
    request.get('/api/course/notification/display', { params: {cid} })