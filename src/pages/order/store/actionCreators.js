import {
    GET_ORDER_INFO
} from "./constants";

import { getOrderInfo } from "@/services/order"

const setOrderInfoAction = res => ({
    type: GET_ORDER_INFO,
    orderInfo: res
})

export const getOrderInfoAction = token => (
    dispatch => {
        getOrderInfo(token).then(res => {
            dispatch(setOrderInfoAction(res.data))
        })
    }
)
