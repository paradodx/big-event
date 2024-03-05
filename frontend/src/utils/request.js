//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';
import { useTokenStore } from '@/stores/token.js'


import { ElMessage } from 'element-plus'
import router from '@/router';
//定义一个变量,记录公共的前缀  ,  baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({baseURL})

// 添加请求拦截器
instance.interceptors.request.use(
    config=>{
        // 请求前回调
        // 添加token
        const tokenStore = useTokenStore();
        // 判断有无token
        if(tokenStore.token){
            config.headers.Authorization = tokenStore.token
        }
        return config;
    },
    err=>{
        // 请求错误的回调
        return Promise.reject(err)
    }
)    


//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        // 判断业务状态码
        if(result.data.code === 1){
            return result.data;
        }
        ElMessage.error(result.data.msg ? result.data.msg : '服务异常')
        return Promise.reject(result.data);
    },
    err=>{
        if(err.response.status === 401){
            ElMessage.error('请先登录')
            router.push('/login')
        }else{
            ElMessage.error('服务异常');
        }
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;