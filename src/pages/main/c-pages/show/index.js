import React, { memo, useEffect } from "react";
import { Carousel, message } from 'antd';
import { shallowEqual, useDispatch, useSelector } from "react-redux";

import { ShowWrapper } from './style';
import ProductCard from "@/components/product-card";
import { getProductsInfoAction } from "@/pages/management/store/actionCreators";
import {
    productTags,
    imgBaseURL,
    numberOfDiscountShow,
    numberOfRecommendationShow
} from "@/constants"
import { displayPersentage } from "@/utils"

function Show({ history }) {

    const dispatch = useDispatch()
    const { productsArr, token } = useSelector(state => ({
        productsArr: state.Product.ProductsInfo,
        token: state.User.UserIDInfo.token
    }), shallowEqual)

    useEffect(() => {
        dispatch(getProductsInfoAction())
    }, [dispatch])

    const discountPriceArr = productsArr.filter(item => item.discountPrice !== 0).slice(0, numberOfDiscountShow)
    const recommendationArr = productsArr.sort((a, b) => b.price - a.price).slice(0, numberOfRecommendationShow)

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
                key={index}
                title={item}
                infoArr={obj[item]}
                token={token}
                history={history}
            />
        ))
    }

    function carouselClick(id) {
        if (token) {
            history.push('/detail/' + id)
        } else {
            message.error('please login')
        }
    }

    return (
        <ShowWrapper>
            <div className='carousels'>
                <div className='left-carousel'>
                    <h2> recommendation </h2>
                    <Carousel
                        autoplay
                        dotPosition='top'
                    >
                        {recommendationArr.map((item, index) =>
                        (<div
                            onClick={() => { carouselClick(item.id) }}
                            className='click'
                            key={index}
                        >
                            <img
                                src={imgBaseURL + item.imagePaths[0]}
                                width={400}
                                alt='img...'
                            />
                        </div>)
                        )}
                    </Carousel>
                </div>
                <div className='right-carousel'>
                    <h2> discount </h2>
                    <Carousel
                        autoplay
                        dotPosition='top'
                    >
                        {discountPriceArr.map((item, index) =>
                        (<div
                            onClick={() => { carouselClick(item.id) }}
                            className='click'
                            key={index}
                        >
                            <img
                                src={imgBaseURL + item.imagePaths[0]}
                                width={400}
                                alt='img...'
                            />
                            <h3> {displayPersentage(item.discountPrice, item.price)} </h3>
                        </div>)
                        )}
                    </Carousel>
                </div>
            </div>
            {productCardShow()}
        </ShowWrapper>
    )
}

export default memo(Show)
