import React, { memo, useMemo, useState } from "react";
import { Table, Form, message, Input, Button, InputNumber, Modal } from "antd";
import { CreditCardOutlined } from "@ant-design/icons"
import { useDispatch, useSelector, shallowEqual } from "react-redux";
import copy from "copy-to-clipboard";

import { CartWrapper } from "./style";
import { imgBaseURL, copyBase } from "@/constants";
import { subProductAction } from "./store/actionCreators";
import { createOrder, confirmOrder, getOrderNumberInfo } from "@/services/order";
import { getProductsInfoAction } from "@/pages/management/store/actionCreators"

function Cart() {
    const [form] = Form.useForm();
    const [selectedRowKeys, setSelectedRowKeys] = useState([])
    const [numberObj, setNumberObj] = useState({})
    const [orderNumber, setOrderNumber] = useState('')
    const [discountOrderNumber, setDiscountOrderNumber] = useState('')
    const [visible, setVisible] = useState(false)
    const [discountVisible, setDiscountVisible] = useState(false)
    const [checkVisible, setCheckVisible] = useState(false)
    const [loading, setLoading] = useState(false)
    const [discountLoading, setDiscountLoading] = useState(false)
    const [discountOrderInfo, setDiscountOrderInfo] = useState({})

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

    const totalDiscountPrice = useMemo(() => {
        let res = 0
        cartRes.forEach(item => {
            if (selectedRowKeys.includes(item.id)) {
                res += item.discountPrice * numberObj[item.id]
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
            // payerUsername: userName,
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

    function createDiscount() {
        if (form.getFieldValue('phone number') && form.getFieldValue('address')) {
            setDiscountLoading(true)
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
                shippingAddress: form.getFieldValue('address'),
            }
            createOrder(order, token).then(res => {
                setDiscountOrderNumber(res.data.orderNumber)
                message.success('create discount order success', 0.5, () => {
                    setDiscountLoading(false)
                    setDiscountVisible(true)
                })
            }).catch(() => {
                message.error('create discount order failed', 0.5, () => {
                    setDiscountLoading(false)
                })
            })
        } else {
            message.error('pelase finish form')
        }
    }

    function checkDiscount() {
        if (discountOrderNumber) {
            getOrderNumberInfo(discountOrderNumber).then(res => {
                setDiscountOrderInfo(res.data)
                setCheckVisible(true)
            }).catch(() => {
                message.error('check dicount order failed')
            })
        } else {
            message.error('please create dicount order first')
        }
    }

    function confirmPay() {
        confirmOrder({ orderNumber: orderNumber }, token)
            .then(() => {
                message.success('pay success', 1, () => {
                    dispatch(getProductsInfoAction())
                    setVisible(false)
                })
            }).catch(() => {
                message.error('pay failed')
            })
    }

    function confirmDicountPay() {
        confirmOrder({ orderNumber: discountOrderNumber }, token)
            .then(() => {
                message.success('pay success', 1, () => {
                    setDiscountOrderNumber('')
                    dispatch(getProductsInfoAction())
                    setCheckVisible(false)
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
                    form={form}
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
                        wrapperCol={{ span: 30 }}
                    >
                        <Button
                            type="primary"
                            loading={loading}
                            htmlType="submit"
                            style={{
                                marginLeft: '150px',
                            }}>
                            Create order
                        </Button>

                        <Button
                            type="primary"
                            loading={discountLoading}
                            style={{
                                marginLeft: '100px',
                            }}
                            onClick={() => { createDiscount() }}
                        >
                            Create discount order
                        </Button>

                        <Button
                            type="link"
                            // loading={discountLoading}
                            style={{
                                marginLeft: '100px',
                            }}
                            onClick={() => { checkDiscount() }}
                        >
                            Check discount order
                        </Button>

                    </Form.Item>
                </Form>
                <Modal
                    title='create order'
                    centered
                    visible={visible}
                    onCancel={() => { setVisible(false) }}
                    footer={null}
                >
                    <div style={{ textAlign: 'center' }}>
                        <h3 style={{ color: 'red', fontSize: '24px' }}> ${totalPrice}  </h3>

                        <Button onClick={() => { confirmPay() }} icon={<CreditCardOutlined />} style={{ margin: '20px' }}> Pay </Button>
                        <Button
                            onClick={() => {
                                copy(`${copyBase}/pay/${orderNumber}`)
                                message.success('copy success, please share the link')
                            }}
                            style={{ margin: '20px' }}
                        >
                            Pay by others
                        </Button>
                    </div>
                </Modal>
                <Modal
                    title='create discount order'
                    centered
                    visible={discountVisible}
                    onCancel={() => { setDiscountVisible(false) }}
                    footer={null}
                >
                    <div style={{ textAlign: 'center' }}>
                        <h3 style={{ color: 'red', fontSize: '24px' }}> ${totalDiscountPrice} </h3>
                        <pre> please copy this link to your friends </pre>
                        <pre> {`${copyBase}/share/${discountOrderNumber}`} </pre>
                        <Button
                            onClick={() => {
                                copy(`${copyBase}/share/${discountOrderNumber}`)
                                message.success('copy success')
                            }}>
                            copy
                        </Button>
                    </div>
                </Modal>
                <Modal
                    title='check discount order'
                    centered
                    visible={checkVisible}
                    onCancel={() => { setCheckVisible(false) }}
                    footer={null}
                >
                    <pre> price: {discountOrderInfo.totalPrice} discount: {discountOrderInfo.discount ? 'YES' : 'NO'} </pre>
                    <Button onClick={() => { confirmDicountPay() }}> Pay </Button>
                </Modal>
            </div>
        </CartWrapper>
    )
}

export default memo(Cart)
