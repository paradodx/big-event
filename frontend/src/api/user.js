// 导入request.js
import request from '@/utils/request.js'

// 提供调用注册接口的函数
export const userRegisterService = (registerData)=>{
    const params = new URLSearchParams()
    for(let key in registerData){
        params.append(key, registerData[key]);
    }
    return request.post('user/register', params);
}

// 提供调用登录接口的函数
export const userLoginService = (loginData)=>{
    const params = new URLSearchParams()
    for(let key in loginData){
        params.append(key, loginData[key])
    }
    return request.post('user/login', params)
}

// 获取用户详细信息
export const userInfoService = ()=>{
    return request.get('/user/userInfo')
}

// 更新用户信息
export const userInfoUpdateService = (userInfoData)=>{
    return request.put('/user/update', userInfoData)
}

// 更新用户头像
export const userAvatarUpdateService = (AvatarUrl)=>{
    const params = new URLSearchParams()
    params.append('avatarUrl', AvatarUrl)
    return request.patch('/user/updateAvatar', params)
}

// 更新密码
export const userPwdUpdateService = (pwdData)=>{
    return request.patch('/user/updatePwd', pwdData)
}