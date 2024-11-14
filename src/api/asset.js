import request from "@/utils/request.js";


// 预览资源
export const getPreviewFileService = ( fileName ) =>
    request.get('/api/assets/preview', { params: {fileName: fileName} })

export const deleteItemsService = ( selectedItems ) =>
    request.post('/api/assets/delete', {fileName: selectedItems})