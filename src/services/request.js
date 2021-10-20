import originAxios from 'axios';

export default function request(option) {
    return new Promise((resolve, reject) => {
        const instance = originAxios.create({
            baseURL: 'https://ecomm.shawnking07.dev',
            timeout: 6000
        });

        instance(option).then(res => {
            resolve(res)
        }).catch(err => {
            reject(err)
        })
    })
}
