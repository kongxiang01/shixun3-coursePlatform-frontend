import request from '../utils/request'

export const outlineUploadService = ( file, cid ) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    return request.post('/api/course/outline/upload', formData)
}

export const outlinePreviewService = ( cid ) =>
    request.get('/api/course/outline', { params: {cid} })

export const calendarUploadService = ( file, cid ) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    return request.post('/api/course/calendar/upload', formData)
}

export const calendarPreviewService = ( cid ) =>
    request.get('/api/course/calendar', { params: {cid} })

// export const deleteItemsService = ( selectedItems ) =>
//     request.post('/api/assets/delete', selectedItems )