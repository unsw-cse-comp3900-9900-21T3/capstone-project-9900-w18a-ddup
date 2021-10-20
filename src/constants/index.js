export const customerAvatarMenu = [
    {
        title: 'My profile',
        key: 'profile'
    },
    {
        title: 'My cart',
        key: 'cart'
    },
    {
        title: 'My order',
        key: 'order'
    },
    {
        title: 'Log out',
        key: 'logout'
    }
]

export const adminAvatarMenu = [
    {
        title: 'Management',
        key: 'management'
    },
    {
        title: 'All order',
        key: 'allOrder'
    },
    {
        title: 'Log out',
        key: 'logout'
    }
]

export const adminArr = ['admin@test.com']

export const imgBaseURL = 'https://ecomm.shawnking07.dev'

export const itemsPerPageForAdmin = 8

export const itemsPerPageForCustomer = 6

export const productTags = ['foods', 'electornics', 'daily necessities', 'clothing']

export const numberOfDiscountShow = 6

export const numberOfRecommendationShow = 6

export const copyBase = process.env.NODE_ENV === 'development' ? 'localhost:3000' : '118.31.49.77'
