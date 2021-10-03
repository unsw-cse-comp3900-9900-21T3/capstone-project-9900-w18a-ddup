import {
    SET_USER_AUTHORITY,
    CLEAR_USER_AUTHORITY
} from "./constants"

export const setUserAuthorityAction = (obj) => ({
    type: SET_USER_AUTHORITY,
    userAuthority: obj
})

export const clearUserAuthorityAction = () => ({
    type: CLEAR_USER_AUTHORITY
})
