import request from "@/utils/request.js";


// 预览资源
export const getPreviewFileService = ( cid, aid ) =>
    request.get('/api/assets/preview', { params: {cid: cid, aid: aid} })

export const deleteItemsService = ( cid, aids ) =>
    request.post('/api/assets/delete', {cid: cid, aid: aids})

export const getDirectoryService = ( path ) =>
    request.get('/api/assets/catalogue', { params: {path: path} })

export const getDownloadFileService = ( cid, aid ) =>
    request.get('/api/assets/generateDownloadLink', { params: { cid, aid } })

export const uploadCourseWareService = (file, cid, tPath, aid) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    formData.append("tpath", tPath);
    formData.append("aid", aid);
    return request.post('/api/assets/upload', formData)
}

export const createFolderService = ( folderPath ) =>
    request.post('/api/assets/folder/create', { folderPath: folderPath } )