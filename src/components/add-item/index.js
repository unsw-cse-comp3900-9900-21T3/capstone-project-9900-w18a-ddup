import React, { memo, useState } from "react";
import { Form, Button, message, Select, Input, Upload } from "antd";
import { UploadOutlined } from '@ant-design/icons';

import { addProductItem } from "@/services/product"
import { productTags } from "@/constants"

const { Option } = Select

function AddItem({ token, quit }) {
    const [loading, setLoading] = useState(false)

    function onFinish(values) {
        setLoading(true)
        const formData = new FormData()
        for (let [key, value] of Object.entries(values)) {
            if (key !== 'files') {
                formData.append(key, value)
            } else {
                formData.append(key, value.file)
            }
        }
        addProductItem(formData, token).then(
        ).then(() => {
            message.success('add success', 0.5, () => {
                setLoading(false)
                quit()
            })
        }).catch(err => {
            message.error('add fail', 0.5, () => {
                setLoading(false)
            })
        })
    }

    return (
        <div>
            <Form
                labelCol={{ span: 8 }}
                wrapperCol={{ span: 10 }}
                onFinish={onFinish}
            >
                <Form.Item
                    label='name'
                    name='name'
                    rules={[
                        {
                            required: true,
                            // message: 'Please input your username!',
                        },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='descript'
                    name='description'
                    wrapperCol={{ span: 32 }}
                    initialValue={null}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='price'
                    name='price'
                    initialValue={null}
                    rules={[
                        {
                            required: true,
                            // message: 'Please input your username!',
                        },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='discountPrice'
                    name='discountPrice'
                    initialValue={null}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='amount'
                    name='amount'
                    initialValue={null}
                    rules={[
                        {
                            required: true,
                            // message: 'Please input your username!',
                        },
                    ]}
                >
                    <Input />
                </Form.Item>
                <Form.Item
                    label='tag'
                    name='tags'
                    initialValue={null}
                >
                    <Select>
                        {productTags.map((item, index) => (
                            <Option key={index} value={item}> 
                                {item}
                            </Option>
                        ))}
                    </Select>
                </Form.Item>
                <Form.Item
                    label='figure'
                    name='files'
                >
                    <Upload
                        beforeUpload={() => false}
                    >
                        <Button icon={<UploadOutlined />}>Select Figure</Button>
                    </Upload>
                </Form.Item>
                <Form.Item
                    wrapperCol={{ push: 10 }}
                >
                    <Button
                        type="primary"
                        htmlType="submit"
                        loading={loading}
                    >
                        Submit
                    </Button>
                </Form.Item>
            </Form>
        </div>
    )
}

export default memo(AddItem)
