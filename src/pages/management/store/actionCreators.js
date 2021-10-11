import {
    GET_PRODUCTS_INFO
} from "./constants";

import { getProductsInfo } from "@/services/product"

export const setProductsInfoAction = arr => ({
    type: GET_PRODUCTS_INFO,
    productsInfo: arr
})

export const getProductsInfoAction = () => (
    dispatch => {
        getProductsInfo().then( res => {
            dispatch(setProductsInfoAction(res.data))
        })
    }
)
