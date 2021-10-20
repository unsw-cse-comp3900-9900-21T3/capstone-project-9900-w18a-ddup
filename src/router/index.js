import React from "react";
import { Redirect } from "react-router-dom";

import Main from "@/pages/main";
import Show from "@/pages/main/c-pages/show";
import Search from "@/pages/main/c-pages/search";
import Detail from "@/pages/detail";
import Profile from "@/pages/profile";
import Cart from "@/pages/cart";
import Order from "@/pages/order";
import Management from "@/pages/management";
import AllOrder from "@/pages/all-order";
import Share from "@/pages/share";

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
    }
]

export default routes
