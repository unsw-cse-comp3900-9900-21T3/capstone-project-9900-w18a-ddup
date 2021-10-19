import {
    GET_ORDER_INFO
} from "./constants";

const defaultState = {
    orderInfo: {}
}

const reducer = (state = defaultState, action) => {
    switch(action.type){
        case GET_ORDER_INFO:
            return {...state, orderInfo: action.orderInfo}
        default:
            return state
    }
}

export default reducer
