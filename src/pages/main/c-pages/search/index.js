import React, { memo, useEffect, useMemo } from "react";
import { shallowEqual, useDispatch, useSelector } from "react-redux";

import { getProductsInfoAction } from "@/pages/management/store/actionCreators";

function Search({location}) {
    const keyWord = location.state.keyWord
    
    const dispatch = useDispatch()
    const { productsArr, token } = useSelector(state => ({
        productsArr: state.Product.ProductsInfo,
        token: state.User.UserIDInfo.token
    }), shallowEqual)
    
    useEffect(() => {
        dispatch(getProductsInfoAction())
    },[dispatch])

    const searchRes = useMemo(() => {
        const reg = new RegExp(keyWord, 'i')
        return productsArr.filter(item => (
            reg.test(item.description) 
        ))
    }, [keyWord, productsArr])

    return (
        <div>
            Search {keyWord}
        </div>
    )
}

export default memo(Search)
