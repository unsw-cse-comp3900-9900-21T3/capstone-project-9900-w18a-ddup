import request from "./request";

export function createOrder(order, token) {
    return request({
        url: '/orders',
        method: 'POST',
        data: order,
        headers: {
            'authorization': 'Bearer '+ token
        }
    })
}

export function confirmOrder(orderNumber, token) {
    return request({
        url: '/orders/confirm',
        method: 'POST',
        data: orderNumber,
        headers: {
            'authorization': 'Bearer '+ token,
        }
    })
}

export function getOrderInfo(token) {
    return request({
        url: '/orders',
        method: 'GET',
        headers: {
            'authorization': 'Bearer '+ token
        }
    })
}

export function getOrderNumberInfo(orderNumber) {
    return request({
        url: '/orders/' + orderNumber,
    })
}

export function confrimSharedOrder(orderNumber) {
    return request({
        url: '/orders/discount',
        params: {
            orderNumber
        }
    })
}
