import request from "@/utils/request.js";

export const uploadVideoService = (video, cid, vid, start, end) =>{
    const formData = new FormData();
    formData.append("video", video);
    formData.append("cid", cid);
    formData.append("vid", vid);
    formData.append("start", start);
    formData.append("end", end);
    return request.post('/api/video/upload', formData)
}

export const deleteVideoService = ( selectedItems ) =>
    request.post('/api/video/delete', {fileName: selectedItems})

export const getVideoListService = ( cid ) =>
    request.post('/api/video/display', { cid })

export const previewVideoService = ( cid, vid ) =>
    request.get('/api/video/preview', { params: { cid, vid } } )