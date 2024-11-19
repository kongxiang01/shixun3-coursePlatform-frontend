import request from "@/utils/request.js";


// 预览资源
export const getPreviewFileService = ( cid, aid ) =>
    request.get('/api/assets/preview', { params: {cid: cid, aid: aid} })

export const deleteItemsService = ( selectedItems ) =>
    request.post('/api/assets/delete', {fileName: selectedItems})

export const deleteItemsService1111 = ( selectedItems, aids ) =>
    request.post('/api/assets/delete', {fileName: selectedItems, aid: aids})

export const getDirectoryService = ( path ) =>
    request.get('/api/assets/catalogue', { params: {path: path} })

export const getDownloadFileService = ( fileName ) =>
    request.get('/api/assets/generateDownloadLink', { params: {fileName: fileName} })

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