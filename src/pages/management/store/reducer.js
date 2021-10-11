import {
    GET_PRODUCTS_INFO
} from "./constants"

const defaultState = {
    ProductsInfo: [],
}

function reducer(state=defaultState, action) {
    switch(action.type) {
    //     case SET_USER_IDInfo:
    //         const tem = {...state.UserIDInfo, ...action.userIDInfo}
    //         return {UserIDInfo: tem}
    //    case CLEAR_USER_IDInfo:
    //        return {UserIDInfo: {}}
        case GET_PRODUCTS_INFO:
            return {...state, ProductsInfo: action.productsInfo}
        default:
            return state
    }
}

export default reducer
