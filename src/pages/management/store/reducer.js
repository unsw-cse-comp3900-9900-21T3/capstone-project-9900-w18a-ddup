import {
    GET_PRODUCTS_INFO,
    GET_PRODUCT_INFO,
    GET_RECOMMENDATION,
    GET_C_RECOMMENDATION,
} from "./constants"

const defaultState = {
    ProductsInfo: [],
    TopRecommendation: [],
    CRecommendation: [],
}

function reducer(state=defaultState, action) {
    switch(action.type) {
        case GET_PRODUCTS_INFO:
            return {...state, ProductsInfo: action.productsInfo}        
        case GET_PRODUCT_INFO:
            return {...state, ProductInfo: action.productInfo}
        case GET_RECOMMENDATION:
            return {...state, TopRecommendation: action.recommendation}
        case GET_C_RECOMMENDATION:
            return {...state, CRecommendation: action.recommendation}
        default:
            return state
    }
}

export default reducer
