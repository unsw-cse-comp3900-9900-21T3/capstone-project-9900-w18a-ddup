import {
    SET_USER_IDInfo,
    CLEAR_USER_IDInfo,
} from "./constants";

import { getUserInfo } from "@/services/user"

export const setUserIDInfoAction = obj => ({
    type: SET_USER_IDInfo,
    userIDInfo: obj
})

export const clearUserIDInfoAction = () => ({
    type: CLEAR_USER_IDInfo
})

export const getUserInfoAction = token => (
    dispatch => {
        getUserInfo(token).then( res => {
            dispatch(setUserIDInfoAction(res.data))
        })
    }
)
