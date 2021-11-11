import request from "./request";

export function userRegister(info) {
    return request({
        url: '/register',
        data: info,
        method: 'POST'
    })
}

export function userLogin(info) {
    return request({
        url: '/login',
        method: 'POST',
        data: info,
    })
}

export function getUserInfo(token) {
    return request({
        url: '/users',
        headers: {
            'authorization': 'Bearer '+ token
        }
    })
}

export function editUserInfo(info, token) {
    return request({
        url: '/users',
        method: 'PATCH',
        data: info,
        headers: {
            'authorization': 'Bearer '+ token
        }
    })
}

export function test() {
    return request({
        url: '/testQL?gender=male',
        method: 'POST',
        params: {age: 25},
        data: {salary: 100}
    })
}
