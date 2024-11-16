import request from "@/utils/request.js";

export const uploadVideoService = (videoFile, cid, describe, time, vid) =>{
    const formData = new FormData();
    formData.append("videoFile", videoFile);
    formData.append("cid", cid);
    formData.append("content", describe);
    formData.append("vid", vid);
    return request.post('/api/course/uploadVideo', formData)
}

export const deleteVideoService = ( selectedItems ) =>
    request.post('/api/course/video/delete', {fileName: selectedItems})