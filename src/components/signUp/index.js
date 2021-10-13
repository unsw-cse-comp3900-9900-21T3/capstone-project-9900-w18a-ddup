import React, { memo, useState } from "react";
import { Form, Input, Button, message } from "antd";

import { userRegister } from "@/services/user"


function SignUp({ signUpCancel }) {
    const [loading, setLoading] = useState(false)

    const onFinish = (values) => {
        setLoading(true)
        userRegister({ ...values, firstname: values.username }).then(() => {
            message.success('register success!', 1, () => {
                setLoading(false)
                signUpCancel()
            })
        }).catch(err => {
            message.error(err.response.data.detail, 1, () => {
                setLoading(false)
            })
        })
    };

    return (
        <div>
            <Form
                name='signUp'
                labelCol={{ span: 8 }}
                wrapperCol={{ span: 24 }}
                onFinish={onFinish}
            >
                <Form.Item
                    label="Username"
                    name="username"
                    rules={[
                        {
                            required: true,
                            message: 'Please input your username! At least 4 chats',
                            min: 4,
                        },
                    ]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="E-mail"
                    name="address"
                    rules={[
                        {
                            type: 'email',
                            message: 'The input is not valid E-mail!',
                        },
                        {
                            required: true,
                            message: 'Please input your E-mail!',
                        },
                    ]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="Phone Number"
                    name="phone"
                    rules={[
                        {
                            // required: true,
                            message: 'Please input your phone number!',
                        },
                    ]}
                >
                    <Input />
                </Form.Item>

                <Form.Item
                    label="Password"
                    name="password"
                    rules={[
                        {
                            required: true,
                            message: 'Please input your password! At least 4 chats',
                            min: 4,
                        },
                    ]}
                >
                    <Input.Password />
                </Form.Item>

                <Form.Item
                    wrapperCol={{
                        offset: 8,
                        span: 16,
                    }}
                >
                    <Button type="primary" htmlType="submit" loading={loading}>
                        Submit
                    </Button>
                </Form.Item>
            </Form>
        </div >
    )
}

export default memo(SignUp)
