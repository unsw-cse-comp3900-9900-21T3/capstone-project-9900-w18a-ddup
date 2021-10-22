import React, { memo, useEffect } from "react";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import { Table } from 'antd';

import { AllOrderWrapper } from "./style";
import { getOrderInfoAction } from "@/pages/order/store/actionCreators"

function AllOrder() {
    const dispatch = useDispatch()
    const { token, orderInfo } = useSelector(state => ({
        token: state.User.UserIDInfo.token,
        orderInfo: state.Order.orderInfo.admin,
    }), shallowEqual)

    useEffect(() => {
        dispatch(getOrderInfoAction(token))
    }, [dispatch, token])

    const columns = [
        {
            title: 'ID',
            dataIndex: 'id',
            key: 'id',
            width: 1,
        },
        {
            title: 'PaidTime',
            dataIndex: 'paidTime',
            key: 'padiTime',
            width: 1,
            render: paidTime => {
                const index = paidTime.indexOf('.')
                return paidTime.slice(0, index)
            }
        },
        {
            title: 'Price',
            dataIndex: 'totalPrice',
            key: 'totalPrice',
            width: 1,
        },
        {
            title: 'Payer',
            dataIndex: 'payer',
            key: 'payer',
            width: 1,
        },
        {
            title: 'Buyer',
            dataIndex: 'buyer',
            key: 'buyer',
            width: 1,
        },
        {
            title: 'Address',
            dataIndex: 'shippingAddress',
            key: 'shippingAddress',
            width: 1,
        },
        {
            title: 'Products',
            dataIndex: 'products',
            key: 'products',
            width: 1,
            render: products => (
                <div>
                    {products.map((item, index) => {
                        return <pre key={index}> {item.product.name}{`($${item.product.price})`} * {item.amount} </pre>
                    })}
                </div>
            )
        },
        {
            title: 'Discount',
            dataIndex: 'discount',
            key: 'discount',
            width: 1,
            render: discount => discount ? 'YES' : 'NO'
        }
    ]

    return (
        <AllOrderWrapper>
            <div className='main'>
                <Table
                    columns={columns}
                    dataSource={orderInfo}
                />
            </div>
        </AllOrderWrapper>
    )
}

export default memo(AllOrder)
