import React, { lazy } from "react";
import { Redirect } from "react-router-dom";

const Main = lazy(() => import("@/pages/main"))
const Show = lazy(() => import("@/pages/main/c-pages/show"))
const Search = lazy(() => import("@/pages/main/c-pages/search"))
const Detail = lazy(() => import("@/pages/detail"))
const Profile = lazy(() => import("@/pages/profile"))
const Cart = lazy(() => import("@/pages/cart"))
const Order = lazy(() => import("@/pages/order"))
const Management = lazy(() => import("@/pages/management"))
const AllOrder = lazy(() => import("@/pages/all-order"))
const Share = lazy(() => import("@/pages/share"))
const Pay = lazy(() => import("@/pages/pay")) 

// import Main from "@/pages/main";
// import Show from "@/pages/main/c-pages/show";
// import Search from "@/pages/main/c-pages/search";
// import Detail from "@/pages/detail";
// import Profile from "@/pages/profile";
// import Cart from "@/pages/cart";
// import Order from "@/pages/order";
// import Management from "@/pages/management";
// import AllOrder from "@/pages/all-order";
// import Share from "@/pages/share";
// import Pay from "@/pages/pay";

const routes = [
    {
        path: '/',
        exact: true,
        render() {
            return <Redirect  to='main'/>
        }
    },
    {
        path: '/main',
        component: Main,
        routes: [
            {
                path: '/main',
                exact: true,
                render() {
                    return <Redirect  to='main/show'/>
                }
            },
            {
                path: '/main/show',
                component: Show
            },
            {
                path: '/main/search',
                component: Search
            }
        ]
    },
    {
        path: '/detail/:id',
        component: Detail
    },
    {
        path: '/profile',
        component: Profile
    },
    {
        path: '/cart',
        component: Cart
    },
    {
        path: '/order',
        component: Order
    },
    {
        path: '/management',
        component: Management
    },
    {
        path: '/allOrder',
        component: AllOrder
    },
    {
        path: '/share/:orderNumber',
        component: Share
    },
    {
        path: '/pay/:orderNumber',
        component: Pay
    },
]

export default routes
