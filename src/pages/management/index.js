import React, { memo, useEffect, useState } from "react";
import { shallowEqual, useDispatch, useSelector } from "react-redux";
import { Pagination, Card, Button, Modal } from "antd";

import { getProductsInfoAction } from "./store/actionCreators";
import { ManagementWrapper } from "./style";
import AddItem from "@/components/add-item";
import ProductForm from "@/components/product-form";
import { itemsPerPageForAdmin } from "@/constants";

const gridStyle = {
    width: '25%',
    height: '500px',
    textAlign: 'center',
};

function Management() {
    const [current, setCurrent] = useState(1)
    const [update, setUpdate] = useState(0)
    const [addVisible, setAddVisible] = useState(false)

    const dispatch = useDispatch()
    const { productsArr, token } = useSelector(state => ({
        productsArr: state.Product.ProductsInfo,
        token: state.User.UserIDInfo.token
    }), shallowEqual)

    useEffect(() => {
        dispatch(getProductsInfoAction())
    }, [dispatch, addVisible, update])

    function onChange(page) {
        setCurrent(page)
    }

    function showProductCards() {
        return productsArr.sort((a, b) => a.id-b.id).slice((current-1) * itemsPerPageForAdmin, current  * itemsPerPageForAdmin)
            .map((item, index) => {
                return (
                    <Card.Grid
                        style={gridStyle}
                        key={index}
                    >
                        <ProductForm productInfo={item} token={token} update={() => { setUpdate(update + 1) }} />
                    </Card.Grid>
                )
            })
    }


    return (
        <ManagementWrapper>
            <div className='main'>
                <Card >
                    {showProductCards()}
                </Card >
                <Button
                    className='add-product'
                    type='primary'
                    size='large'
                    onClick={() => { setAddVisible(true) }}
                >
                    add product
                </Button>
                <Pagination
                    className='pagination'
                    current={current}
                    onChange={(page) => { onChange(page) }}
                    total={productsArr.length}
                    defaultPageSize={itemsPerPageForAdmin}
                />
            </div>
            <Modal
                title='add new item'
                centered
                visible={addVisible}
                onCancel={() => { setAddVisible(false) }}
                footer={null}
            >
                <AddItem token={token} quit={() => { setAddVisible(false) }} />
            </Modal>
        </ManagementWrapper>
    )
}

export default memo(Management)
