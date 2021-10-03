import {
    SET_USER_AUTHORITY,
    CLEAR_USER_AUTHORITY,
} from "./constants"

const defaultState = {
    userAuthority: {}
}

function reducer(state=defaultState, action) {
    switch(action.type) {
        case SET_USER_AUTHORITY:
            return {...state, userAuthority: action.userAuthority}
       case CLEAR_USER_AUTHORITY:
           return {userAuthority: {}}
        default:
            return state
    }
}

export default reducer
