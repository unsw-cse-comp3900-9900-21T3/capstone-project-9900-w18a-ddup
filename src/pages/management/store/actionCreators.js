import {
    GET_PRODUCTS_INFO,
    GET_PRODUCT_INFO,
    GET_RECOMMENDATION,
    GET_C_RECOMMENDATION,
} from "./constants";

import { getProductsInfo, getProductInfo, getRecommendation, getCRecommendation } from "@/services/product"

const setProductsInfoAction = arr => ({
    type: GET_PRODUCTS_INFO,
    productsInfo: arr
})

const setProductInfoAction = obj => ({
    type: GET_PRODUCT_INFO,
    productInfo: obj
})

const setRecommendation = res => ({
    type: GET_RECOMMENDATION,
    recommendation: res
})

const setCRecommendation = res => ({
    type: GET_C_RECOMMENDATION,
    recommendation: res
})

export const getProductsInfoAction = () => (
    dispatch => {
        getProductsInfo().then( res => {
            dispatch(setProductsInfoAction(res.data))
        })
    }
)

export const getProductInfoAction = id => (
    dispatch => {
        getProductInfo(id).then( res=> {
            dispatch(setProductInfoAction(res.data))
        } )
    }
)

export const getRecommendationAction = () => (
    dispatch => {
        getRecommendation().then(res => {
            dispatch(setRecommendation(res.data))
        })
    }
)

export const getCRecommendationAction = token => (
    dispatch => {
        getCRecommendation(token).then( res => {
            dispatch(setCRecommendation(res.data))
        })
    }
)
