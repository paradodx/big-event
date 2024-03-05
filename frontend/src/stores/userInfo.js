import {defineStore} from 'pinia'
import { ref } from 'vue'
const useUserInfoStore = defineStore('userinfo', ()=>{
    // 定义状态相关内容
    const info = ref({})
    // 修改状态
    const setInfo = (newInfo)=>{
        info.value = newInfo
    }
    //移除状态
    const removeInfo = ()=>{
        info.value = {}
    }
    return {info, setInfo, removeInfo}
}, {
    persist: true
})

export default useUserInfoStore