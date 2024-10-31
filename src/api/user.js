import request from '../utils/request'

// 获取用户基本信息
export const userGetInfoService = () => request.get('/api/userinfo')

// 更新用户基本信息
export const userUpdateInfoService = ({ id, nickname, email }) =>
    request.put('/my/userinfo', { id, nickname, email })

// 更新用户头像
export const userUpdateAvatarService = (avatar) =>
    request.patch('/my/update/avatar', { avatar })

// 登录接口
export const userLoginService = ({ username, password }) =>
    request.post('/api/login', { username, password })

export const changePasswordService = ( password ) =>
    request.post('/api/change', { password: password })







export const getDirectoryService = ( path ) =>
    request.get('/api/assets/catalogue', { params: {path: path} })

export const getDownloadFileService = ( filePath ) =>
    request.get('/api/assets/generateDownloadLink', { params: {fileName: filePath} })

export const getCourseListService = ( sno ) =>
    request.post('/api/course', { username: sno } )

export const getHomeworkListService = ( cid, sno ) =>
    request.post('/api/course/homework/display', { cid, sno } )

export const getCourseInfoService = (cid, cno) =>
    request.post('/api/course/info', {}, { params: { cid, cno } })

export const uploadCourseWareService = (file, cid) =>{
    const formData = new FormData();
    formData.append("file", file);
    formData.append("cid", cid);
    return request.post('/api/assets/upload', formData)
}

export const createFolderService = ( folderPath ) =>
    request.post('/api/assets/folder/create', { folderPath } )