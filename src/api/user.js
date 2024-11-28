import request from '../utils/request'

// 获取用户基本信息
export const userGetInfoService = () =>
    request.get('/api/userinfo')

// // 更新用户头像
// export const userUpdateAvatarService = (avatar) =>
//     request.patch('/my/update/avatar', { avatar })

// 登录接口
export const userLoginService = ({ username, password }) =>
    request.post('/api/login', { username, password })

// 修改密码
export const changePasswordService = ( password ) =>
    request.post('/api/change', { password: password })

// 编辑邮箱
export const updateUserEmail = ( pno, email ) =>
    request.post('/api/change/email', { pno, email })