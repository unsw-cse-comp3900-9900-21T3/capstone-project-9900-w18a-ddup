import React, { useState, useEffect } from "react";
import { Button, message } from "antd";
import { shallowEqual, useSelector } from "react-redux";

import { PayWrapped } from "./style";
import { getOrderNumberInfo, confirmOrder } from "@/services/order";

function Pay({ match }) {
    const orderNumber = match.params.orderNumber
    const [orderInfo, setOrderInfo] = useState({})

    const { token } = useSelector(state => ({
        token: state.User.UserIDInfo.token,
    }), shallowEqual)

    useEffect(() => {
        getOrderNumberInfo(orderNumber).then(res => {
            setOrderInfo(res.data)
        })
    }, [orderNumber])

    function confirmPay() {
        if (token) {
            confirmOrder({ orderNumber: orderNumber }, token)
                .then(() => {
                    message.success('pay success', 1)
                }).catch(() => {
                    message.error('pay failed')
                })
        } else {
            message.error('please login first')
        }
    }

    return (
        <PayWrapped>
            <div className='main'>
                {
                    orderInfo.products &&
                    (
                        <>
                            <pre>
                                {`Your friend ${orderInfo.buyer} wants you pay $${orderInfo.totalPrice} for him. Please sign in and help him`}
                            </pre>
                            <Button
                                type='primary'
                                size='large'
                                onClick={() => { confirmPay() }}
                                style={{ margin: '10px' }}
                            >
                                confirm pay
                            </Button>
                        </>
                    )
                }
            </div>
        </PayWrapped>
    )
}

export default Pay
