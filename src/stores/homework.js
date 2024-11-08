import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useHomeworkStore = defineStore(
    'my-homework', () => {
        const homework = ref()
        // const getHomework = async () => {
        //     // const res = await getCourseInfo() // 请求获取数据
        //     // course.value = res.data.data
        // }
        const setHomework = (obj) => {
            homework.value = obj
        }

        return {
            homework,
            setHomework,
        }
    },
    {
        persist: true
    }
)
