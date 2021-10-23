import request from "./request";

export function getProductsInfo() {
    return request({
        url: '/products'
    })
}

export function getProductInfo(id) {
    return request({
        url: '/products/' + id
    })
}

export function addProductItem(formData, token) {
    return request({
        url: '/products',
        method: 'POST',
        data: formData,
        headers: {
            'authorization': 'Bearer '+ token
        }
    })
}

export function deleteProductItem(id, token) {
    return request({
        url: '/products/' + id,
        method: 'DELETE',
        headers: {
            'authorization': 'Bearer '+ token
        }
    })
}

export function editProductItem(id, formData, token) {
    return request({
        url: '/products/' + id,
        method: 'PATCH',
        data: formData,
        headers: {
            'authorization': 'Bearer '+ token
        }
    })
}

export function getRecommendation() {
    return request({
        url: '/products/recommendation',
    })
}

export function getCRecommendation(token) {
    return request({
        url: '/products/c-recommendation',
        headers: {
            'authorization': 'Bearer '+ token
        }  
    })
}
