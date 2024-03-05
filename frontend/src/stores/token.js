// 定义store
import {defineStore} from 'pinia'
import {ref} from 'vue'
/* 
    param1: 名字--唯一性
    param2: 函数--函数的内部可以定义状态的所有内容
    return: 函数
*/

export const useTokenStore = defineStore('token', ()=>{
    // 定义状态的内容
    // 1. 响应式变量
    const token = ref('')

    // 2. 定义函数修改token的值
    const setToken = (newToken)=>{
        token.value = newToken
    }

    // 3. 定义移除token的值
    const removeToken = ()=>{
        token.value = ''
    }

    return{
        token, setToken, removeToken
    }
}/* ,{
    persist: true // 持久化存储
} */)