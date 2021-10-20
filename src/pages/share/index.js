import React, { useState, useEffect } from "react";
import { Button, message } from "antd";

import { ShareWrapped } from "./style";
import { getOrderNumberInfo, confrimSharedOrder } from "@/services/order";

function Share({ match }) {
    const orderNumber = match.params.orderNumber
    const [orderInfo, setOrderInfo] = useState({})

    useEffect(() => {
        getOrderNumberInfo(orderNumber).then(res => {
            setOrderInfo(res.data)
        })
    }, [orderNumber])

    function getDiscount() {
        confrimSharedOrder(orderNumber).then(()=>{
            message.success('help success')
        }).catch(() => {
            message.error('help failed')
        })
    }

    return (
        <ShareWrapped>
            <div className='main'>
                {
                    orderInfo.products &&
                    (
                        <>
                            <pre>
                                {`Your friend ${orderInfo.buyer} wants to buy ${orderInfo.products.map(item => item.product.name)}. Please help him to get a discount! You can also surf our web.`}
                            </pre>
                            <Button type='primary' size='large' onClick={() => {getDiscount()}}> get discount </Button>
                        </>
                    )
                }
            </div>
        </ShareWrapped>
    )
}

export default Share
