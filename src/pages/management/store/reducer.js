import {
    GET_PRODUCTS_INFO,
    GET_PRODUCT_INFO,
} from "./constants"

const defaultState = {
    ProductsInfo: [],
}

function reducer(state=defaultState, action) {
    switch(action.type) {
        case GET_PRODUCTS_INFO:
            return {...state, ProductsInfo: action.productsInfo}        
        case GET_PRODUCT_INFO:
            return {...state, ProductInfo: action.productInfo}
        default:
            return state
    }
}

export default reducer
