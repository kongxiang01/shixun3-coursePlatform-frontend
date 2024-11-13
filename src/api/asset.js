import request from "@/utils/request.js";

export const deleteItemsService = ( selectedItems ) =>
    request.post('/api/assets/delete', {fileName: selectedItems})