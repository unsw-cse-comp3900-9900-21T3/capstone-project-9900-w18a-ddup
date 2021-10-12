import React, { memo, useEffect } from "react";
import { Carousel } from 'antd';
import { shallowEqual, useDispatch, useSelector } from "react-redux";

import { ShowWrapper } from './style';
import ProductCard from "@/components/product-card"; 
import { getProductsInfoAction } from "@/pages/management/store/actionCreators";
import { productTags } from "@/constants"

function Show({history}) {

    const dispatch = useDispatch()
    const { productsArr, token } = useSelector(state => ({
        productsArr: state.Product.ProductsInfo,
        token: state.User.UserIDInfo.token
    }), shallowEqual)

    useEffect(() => {
        dispatch(getProductsInfoAction())
    },[dispatch])

    //分类展示
    function productCardShow() {
        const obj = {}
        for (let i of productTags) {
            obj[i] = []
        }
        for (let i of productsArr) {
            obj[i.tags[0]].push(i)
        }
        return productTags.map((item, index) => (
            <ProductCard 
                key = {index}
                title = {item}
                infoArr = {obj[item]}
                token = {token}
                history = {history}
            />
        ))
    }

    return (
        <ShowWrapper>
            <div className='carousels'>
                <div className='left-carousel'>
                    <h2> recommendation </h2>
                    <Carousel 
                        autoplay
                    >
                        <div >
                            1
                        </div>
                        <div>
                            2
                        </div>
                        <div>
                            3
                        </div>
                    </Carousel>
                </div>
                <div className='right-carousel'>
                    <h2> group-buying </h2>
                    <Carousel autoplay >
                        <div>
                            3
                        </div>
                        <div>
                            4
                        </div>
                        <div>
                            5
                        </div>

                    </Carousel>
                </div>
            </div>
            {productCardShow()}
        </ShowWrapper>
    )
}

export default memo(Show)
