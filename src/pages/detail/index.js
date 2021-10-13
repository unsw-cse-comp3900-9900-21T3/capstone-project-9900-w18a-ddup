import React, { memo } from "react";
import { shallowEqual, useSelector } from "react-redux";
import { Image, Card, Button } from "antd";

import { imgBaseURL } from "@/constants"
import { DetailWrapper } from "./style";

function Detail({ match, history }) {
    const id = match.params.id

    const { productsArr } = useSelector(state => ({
        productsArr: state.Product.ProductsInfo,
    }), shallowEqual)

    const productInfo = productsArr.find(item => 
        item.id === +id
    ) || {}

    return (
        <DetailWrapper>
            <div className='main'>
                {productInfo.imagePaths &&
                    (<Image
                        alt='img...'
                        src={imgBaseURL + productInfo.imagePaths[0]}
                        width={500}
                    />)}
                <div className='main-right'>
                    <Card title={<h2> {productInfo.name} </h2>}>
                        <h3> Remaining: {productInfo.amount} </h3>
                        <h3> Description: </h3>
                        <p>{productInfo.description} 111111111
                            11111111111111111111111111111111111111111111111111
                            111111111111111111111
                        </p>
                        <h3> Price: {productInfo.price} </h3>
                        <h3> DiscountPrice: {productInfo.discountPrice} </h3>
                    </Card>
                    <div className='option'>
                        <Button size='large'> add </Button>
                        <Button disabled size='large'> join group </Button>
                    </div>
                </div>
            </div>
        </DetailWrapper>
    )
}

export default memo(Detail)
