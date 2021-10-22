import {
    GET_PRODUCTS_INFO,
    GET_PRODUCT_INFO,
    GET_RECOMMENDATION
} from "./constants"

const defaultState = {
    ProductsInfo: [],
    TopRecommendation: {},
}

function reducer(state=defaultState, action) {
    switch(action.type) {
        case GET_PRODUCTS_INFO:
            return {...state, ProductsInfo: action.productsInfo}        
        case GET_PRODUCT_INFO:
            return {...state, ProductInfo: action.productInfo}
        case GET_RECOMMENDATION:
            return {...state, TopRecommendation: action.recommendation}
        default:
            return state
    }
}

export default reducer
