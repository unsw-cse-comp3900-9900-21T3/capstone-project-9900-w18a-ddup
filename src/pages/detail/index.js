import React, { memo } from "react";
import { shallowEqual, useSelector, useDispatch } from "react-redux";
import { Image, Card, Button, message } from "antd";

import { imgBaseURL } from "@/constants";
import { addProductAction } from "@/pages/cart/store/actionCreators";
import { DetailWrapper } from "./style";


function Detail({ match }) {
    const id = match.params.id

    const dispatch = useDispatch()
    const { productsArr, productsArrInCart } = useSelector(state => ({
        productsArr: state.Product.ProductsInfo,
        productsArrInCart: state.Cart.productsArrInCart
    }), shallowEqual)

    const productInfo = productsArr.find(item => 
        item.id === +id
    ) || {}

    function addIntoCart(id) {
        const index = productsArrInCart.find(item => {
            return +item === +id
        })
        if(index) {
            message.error('please do not add repeatly')
        } else {
            dispatch(addProductAction(id))
            message.success('add success')
        }
    }

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
                        <h3> Amount: {productInfo.amount} </h3>
                    </Card>
                    <div className='option'>
                        <Button size='large' onClick={() => { addIntoCart(productInfo.id) }}> add </Button>
                        <Button disabled size='large'> join group </Button>
                    </div>
                </div>
            </div>
        </DetailWrapper>
    )
}

export default memo(Detail)
