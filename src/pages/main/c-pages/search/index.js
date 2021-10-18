import React, { memo, useEffect, useMemo } from "react";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import { Table, Button, message } from 'antd';

import { getProductsInfoAction } from "@/pages/management/store/actionCreators";
import { imgBaseURL } from "@/constants";

function Search({ location, history }) {
    const keyWord = location.state.keyWord

    const dispatch = useDispatch()
    const { productsArr, token } = useSelector(state => ({
        productsArr: state.Product.ProductsInfo,
        token: state.User.UserIDInfo.token
    }), shallowEqual)

    useEffect(() => {
        dispatch(getProductsInfoAction())
    }, [dispatch])

    const searchRes = useMemo(() => {
        const reg = new RegExp(keyWord, 'i')
        return productsArr.filter(item => (
            reg.test(item.description)
        ))
    }, [keyWord, productsArr])

    function detailClick(id) {
        if (token === undefined) {
            message.error('please login')
        } else {
            history.push('/detail/' + id)
        }
    }

    const columns = [
        {
            title: 'Name',
            dataIndex: 'name',
            key: 'name',
            width: 1,
        },
        {
            title: 'Price',
            dataIndex: 'price',
            key: 'price',
            width: 1,
            sorter: (a,b) => a.price-b.price
        },
        {
            title: 'Discount',
            dataIndex: 'discountPrice',
            key: 'discountPrice',
            width: 1,
            render: discountPrice => !!discountPrice && discountPrice
        },
        {
            title: 'Tag',
            dataIndex: 'tags',
            key: 'tag',
            width: 1,
            render: tags => tags[0]
        },
        {
            title: 'Img',
            dataIndex: 'imagePaths',
            key: 'img',
            width: 1,
            render: img => <img src={imgBaseURL + img[0]} width={100} alt={'img...'} />
        },
        {
            title: 'Option',
            dataIndex: 'id',
            key: 'id',
            width: 1,
            render: id => 
                <Button
                    size='large'
                    type='primary'
                    onClick={()=>{ detailClick(id) }}
                >   
                    detail
                </Button>
        }
    ]

    return (
        <div>
            <Table
                columns={columns}
                dataSource={searchRes}
                pagination={false}
            />
        </div>
    )
}

export default memo(Search)
