import {
    GET_PRODUCTS_INFO,
    GET_PRODUCT_INFO,
} from "./constants";

import { getProductsInfo, getProductInfo } from "@/services/product"

const setProductsInfoAction = arr => ({
    type: GET_PRODUCTS_INFO,
    productsInfo: arr
})

const setProductInfoAction = obj => ({
    type: GET_PRODUCT_INFO,
    productInfo: obj
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
