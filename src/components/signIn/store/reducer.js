import {
    SET_USER_IDInfo,
    CLEAR_USER_IDInfo,
} from "./constants"

const defaultState = {
    UserIDInfo: {}
}

function reducer(state=defaultState, action) {
    switch(action.type) {
        case SET_USER_IDInfo:
            const tem = {...state.UserIDInfo, ...action.userIDInfo}
            return {UserIDInfo: tem}
       case CLEAR_USER_IDInfo:
           return {UserIDInfo: {}}
        default:
            return state
    }
}

export default reducer
