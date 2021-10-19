import React, { memo, useMemo, useState } from "react";
import { Table, Form, message, Input, Button, InputNumber, Modal } from "antd";
import { useDispatch, useSelector, shallowEqual } from "react-redux";

import { CartWrapper } from "./style";
import { imgBaseURL } from "@/constants";
import { subProductAction } from "./store/actionCreators";
import { createOrder, confirmOrder } from "@/services/order";
import { getProductsInfoAction } from "@/pages/management/store/actionCreators"

function Cart() {
    const [selectedRowKeys, setSelectedRowKeys] = useState([])
    const [numberObj, setNumberObj] = useState({})
    const [orderNumber, setOrderNumber] = useState('')
    const [visible, setVisible] = useState(false)
    const [loading, setLoading] = useState(false)

    const dispatch = useDispatch()
    const { productsArr, token, cartArr, userName } = useSelector(state => ({
        token: state.User.UserIDInfo.token,
        userName: state.User.UserIDInfo.username,
        productsArr: state.Product.ProductsInfo,
        cartArr: state.Cart.productsArrInCart,
    }), shallowEqual)

    const cartRes = useMemo(() => {
        return productsArr.filter(item => (
            cartArr.includes(item.id)
        )).map(item => {
            item.key = item.id
            return item
        })
    }, [productsArr, cartArr])

    const totalPrice = useMemo(() => {
        let res = 0
        cartRes.forEach(item => {
            if (selectedRowKeys.includes(item.id)) {
                res += item.price * numberObj[item.id]
            }
        })
        return res
    }, [selectedRowKeys, numberObj, cartRes])


    function deleteClick(id) {
        setSelectedRowKeys(selectedRowKeys.filter(item => +item !== +id))
        dispatch(subProductAction(id))
    }

    function inputNumber(id, val) {
        const obj = { ...numberObj }
        obj[id] = val
        setNumberObj(obj)
    }

    function onFinish(values) {
        setLoading(true)
        const products = []
        for (let i of selectedRowKeys) {
            products.push({
                productId: i,
                amount: numberObj[i]
            })
        }
        const order = {
            products,
            payerUsername: userName,
            shippingAddress: values.address,
        }
        createOrder(order, token).then(res => {
            setOrderNumber(res.data.orderNumber)
            message.success('create order success', 0.5, () => {
                setLoading(false)
                setVisible(true)
            })
        }).catch(() => {
            message.error('create order failed', 0.5, () => {
                setLoading(false)
            })
        })
    }

    function confirmPay() {
        confirmOrder({ orderNumber: orderNumber }, token)
            .then(() => {
                message.success('pay success', 0.5, () => {
                    dispatch(getProductsInfoAction())
                    setVisible(false)
                })
            }).catch(() => {
                message.error('pay failed')
            })
    }

    const rowSelection = {
        selectedRowKeys,
        onChange(selectedRowKeys) {
            setSelectedRowKeys(selectedRowKeys)
            const obj = { ...numberObj }
            for (let i of selectedRowKeys) {
                if (!numberObj[i]) {
                    obj[i] = 1
                }
            }
            setNumberObj(obj)
        },
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
        },
        {
            title: 'Discount',
            dataIndex: 'discountPrice',
            key: 'discountPrice',
            width: 1,
            render: discountPrice => !!discountPrice && discountPrice
        },
        //所需商品数量
        {
            title: 'Number',
            key: 'number',
            dataIndex: 'id',
            width: 1,
            render: id => {
                return (
                    <InputNumber
                        defaultValue={1}
                        min={1}
                        max={5}
                        onChange={val => inputNumber(id, val)}
                    />
                )
            }
        },
        //库存
        {
            title: 'Amount',
            dataIndex: 'amount',
            key: 'amount',
            width: 1,
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
                    size='middle'
                    type='dashed'
                    onClick={() => { deleteClick(id) }}
                >
                    delete
                </Button>
        }
    ]

    return (
        <CartWrapper>
            <div className='main'>
                <Table
                    rowSelection={rowSelection}
                    columns={columns}
                    dataSource={cartRes}
                    pagination={false}
                />
                <pre> Selected: {selectedRowKeys.length}    Total price: {totalPrice} </pre>
                <Form
                    labelCol={{ span: 8 }}
                    wrapperCol={{ span: 8 }}
                    onFinish={onFinish}
                >
                    <Form.Item
                        label='phone number'
                        name='phone number'
                        rules={[
                            {
                                required: true,
                            },
                        ]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        label='address'
                        name='address'
                        rules={[
                            {
                                required: true,
                            },
                        ]}
                    >
                        <Input />
                    </Form.Item>
                    <Form.Item
                        wrapperCol={{ span: 24 }}
                    >
                        <Button
                            type="primary"
                            loading={loading}
                            htmlType="submit"
                            style={{
                                marginLeft: '300px',
                            }}>
                            Create order
                        </Button>

                        <Button
                            disabled
                            type="primary"
                            style={{
                                marginLeft: '150px',
                            }}
                        >
                            Pay by others
                        </Button>
                    </Form.Item>
                </Form>
                <Modal
                    title='order confirm'
                    centered
                    visible={visible}
                    onCancel={() => { setVisible(false) }}
                    footer={null}
                >
                    <div style={{textAlign: 'center'}}>
                        <h3 style={{color: 'red', fontSize: '24px'}}> {totalPrice} $ </h3>
                         
                        <Button onClick={() => { confirmPay() }}> pay now </Button>
                    </div>
                </Modal>
            </div>
        </CartWrapper>
    )
}

export default memo(Cart)
