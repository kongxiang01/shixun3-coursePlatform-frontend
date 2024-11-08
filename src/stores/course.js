import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCourseStore = defineStore(
    'my-course', () => {
        const course = ref()
        const getCourse = async () => {
            // const res = await getCourseInfo() // 请求获取数据
            // course.value = res.data.data
        }
        const setCourse = (obj) => {
            course.value = obj
        }

        const teachingWeek = ref('')

        const setTeachingWeek = (newTeachingWeek) => {
            teachingWeek.value = newTeachingWeek
        }

        return {
            course,
            getCourse,
            setCourse,
            teachingWeek,
            setTeachingWeek
        }
    },
    {
        persist: true
    }
)
