import originAxios from 'axios';

export const baseURL =  'https://ecomm.shawnking07.dev'

export default function request(option) {
    return new Promise((resolve, reject) => {
        const instance = originAxios.create({
            baseURL,
            timeout: 8000
        });

        instance(option).then(res => {
            resolve(res)
        }).catch(err => {
            console.error(err)
            reject(err)
        })
    })
}
