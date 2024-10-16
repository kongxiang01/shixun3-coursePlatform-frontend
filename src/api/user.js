import request from '../utils/request'
// 登录接口
export const userLoginService = ({ sno, password }) =>
    request.post('/api/login/student', { sno, password })

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
