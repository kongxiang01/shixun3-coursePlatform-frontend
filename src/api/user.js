import request from '../utils/request'

// 获取用户基本信息
export const userGetInfoService = () => request.get('/api/userinfo')

// 更新用户基本信息
export const userUpdateInfoService = ({ id, nickname, email }) =>
    request.put('/my/userinfo', { id, nickname, email })

// 更新用户头像
export const userUpdateAvatarService = (avatar) =>
    request.patch('/my/update/avatar', { avatar })

// 更新用户密码
export const userUpdatePasswordService = ({ old_pwd, new_pwd, re_pwd }) =>
    request.patch('/my/updatepwd', { old_pwd, new_pwd, re_pwd })

// 登录接口
export const userLoginService = ({ username, password }) =>
    request.post('/api/login', { username, password })

export const changePasswordService = ( password ) =>
    request.post('/api/change', { password })







export const getDirectory = (path) =>
    request.get('/api/assets/catalogue', { params: {path: path} })

export const getDownloadFile = (filePath) =>
    request.get('/api/assets/download', { params: {filePath} })

export const getCourseList = ( sno ) =>
    request.post('/api/course', { sno: sno } )

export const getHomeworkList = ( cid, sno ) =>
    request.post('/api/course/homework/display', { cid, sno } )

export const getCourseInfo = (cid, cno) =>
    request.post('/api/course/info', {}, { params: { cid, cno } })