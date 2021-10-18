import {
    ADD_PRODUCT,
    SUB_PRODUCT,
} from "./constants"

const defaultState = {
    productsArrInCart: []
}

function reducer(state = defaultState, action) {
    switch(action.type) {
        case ADD_PRODUCT:
            const newArr = [...state.productsArrInCart]
            newArr.push(action.id)
            return {...state, productsArrInCart: newArr}
        case SUB_PRODUCT:
            const newArr2 = [...state.productsArrInCart].filter(item => +item !== +action.id)   
            return {...state, productsArrInCart: newArr2}
        default:    
            return state
    }
}

export default reducer
