import request from "@/utils/request.js";


// 预览资源
export const getPreviewFileService = ( fileName ) =>
    request.get('/api/assets/preview', { params: {fileName: fileName} })

export const deleteItemsService = ( selectedItems ) =>
    request.post('/api/assets/delete', {fileName: selectedItems})

export const getDirectoryService = ( path ) =>
    request.get('/api/assets/catalogue', { params: {path: path} })

export const getDownloadFileService = ( fileName ) =>
    request.get('/api/assets/generateDownloadLink', { params: {fileName: fileName} })

export const uploadCourseWareService = (file, cid, tPath) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    formData.append("tpath", tPath);
    return request.post('/api/assets/upload', formData)
}

export const createFolderService = ( folderPath ) =>
    request.post('/api/assets/folder/create', { folderPath: folderPath } )