import React, { memo, useEffect, useRef, useState } from "react";
import { Form, Input, Button, Select, message, Image, Upload } from "antd";
import { UploadOutlined } from '@ant-design/icons';

import { deleteProductItem, editProductItem } from "@/services/product";
import { ProductFormWrapper } from "./style";
import { imgBaseURL } from "@/constants"

const { Option } = Select

function ProductForm({ productInfo, token, update }) {
    const [deleteLoading, setDeleteLoading] = useState(false)
    const [editLoading, setEditLoading] = useState(false)

    const form = useRef()
    useEffect(() => {
        const formRef = form.current
        for (let [key, value] of Object.entries(productInfo)) {
            formRef.setFieldsValue({
                [key]: value
            })
        }
    })

    function onFinish(values) {
        setEditLoading(true)
        const formData = new FormData()
        for (let [key, value] of Object.entries(values)) {
            formData.append(key, value)
        }
        editProductItem(productInfo.id, formData, token)
            .then(() => {
                message.success('edit success', 0.5, () => {
                    setEditLoading(false)
                    update()
                })
            }).catch(() => {
                message.error('edit failed', 0.5, () => {
                    setDeleteLoading(false)
                })
            })
    }

    function deleteItem() {
        setDeleteLoading(true)
        deleteProductItem(productInfo.id, token).then(() => {
            message.success('delete success', 0.5, () => {
                setDeleteLoading(false)
                update()
            })
        }).catch(() => {
            message.error('delete failed', 0.5, () => {
                setDeleteLoading(false)
            })
        })
    }

    return (
        <ProductFormWrapper>
            <Form
                name='product-form'
                ref={form}
                onFinish={onFinish}
                labelCol={{ span: 8 }}
                wrapperCol={{ span: 10 }}
            >
                <Form.Item
                    label='id'
                    name='id'
                    initialValue={productInfo.id}
                >
                    <Input disabled />
                </Form.Item>
                <Form.Item
                    label='name'
                    name='name'
                    wrapperCol={{ span: 24 }}
                    initialValue={productInfo.name}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='descript'
                    name='description'
                    wrapperCol={{ span: 32 }}
                    initialValue={productInfo.description}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='price'
                    name='price'
                    initialValue={productInfo.price}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='discountPrice'
                    name='discountPrice'
                    initialValue={productInfo.discountPrice}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='amount'
                    name='amount'
                    initialValue={productInfo.amount}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='tag'
                    name='tags'
                    initialValue={productInfo.tags[0]}
                >
                    <Select>
                        <Option value='foods'> foods </Option>
                        <Option value='electornics'> electornics </Option>
                        <Option value='daily necessities'> daily necessities </Option>
                        <Option value='clothing'> clothing </Option>
                    </Select>
                </Form.Item>
                {/* <Form.Item
                    label='figure'
                    name='files'
                >
                    <Upload
                        beforeUpload={() => false}
                    >
                        <Button icon={<UploadOutlined />}>reselect </Button>
                    </Upload>
                </Form.Item> */}
                <Image
                    src={imgBaseURL + productInfo.imagePaths[0]}
                    width={150}
                    style={{
                        display: 'block',
                        margin: '5px auto',
                    }}
                    alt='img...'
                    placeholder={
                        <div
                            style={{
                                width: '150px',
                                height: '150px',
                                backgroundColor: 'black',
                            }}
                        >
                            loading...
                        </div>}
                />
                <Form.Item
                    wrapperCol={{ span: 25 }}
                >
                    <Button
                        disabled
                        htmlType="submit"
                        style={{
                            marginRight: '10px'
                        }}
                        loading={editLoading}
                    >
                        edit
                    </Button>
                    {/* <Button
                        style={{
                            marginRight: '10px'
                        }}
                    >
                        select
                    </Button> */}
                    <Button
                        danger
                        loading={deleteLoading}
                        onClick={() => { deleteItem() }}
                    >
                        delete
                    </Button>
                </Form.Item>
            </Form>
        </ProductFormWrapper>
    )

}

export default memo(ProductForm)
