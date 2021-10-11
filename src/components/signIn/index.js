import React, { memo, useState } from "react";
import { useDispatch } from "react-redux";
import { Form, Input, Button, message } from "antd";

import { setUserIDInfoAction } from "./store/actionCreators"
import ResetPassword from "@/components/reset-password";
import { userLogin } from "@/services/user"

function SignIn({ signInCancel }) {
    // 组件自己的状态
    const [resetVisiable, setResetVisible] = useState(false)
    const [loading, setLoading] = useState(false)

    // redux相关
    const dispatch = useDispatch()

    // 其他hooks

    //业务逻辑

    const onFinish = (values) => {
        setLoading(true)
        userLogin(values).then(res => {
            message.success('login success', 1, () => {
                dispatch(setUserIDInfoAction({...values, token: res.data.id_token}))
                setLoading(false)
                signInCancel()
            })
        }).catch(err => {
            // console.log('?', err.response)
            message.error(err.response?.data.message, 1, () => {
                setLoading(false)
            })
        })

    };

    function resetCancel() {
        setResetVisible(false)
    }

    return (
        <div>
            <Form
                name='signIn'
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
                            message: 'Please input your username!',
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
                            message: 'Please input your password!',
                        },
                    ]}
                >
                    <Input.Password />
                </Form.Item>

                <Form.Item>
                    <Button type="primary" loading={loading} htmlType="submit" style={{
                        margin: '0 100px',
                    }}>
                        Submit
                    </Button>

                    <Button
                        disabled
                        type="primary"
                        style={{
                            margin: '0 50px',
                        }}
                        onClick={() => { setResetVisible(!resetVisiable) }}
                    >
                        Froget
                    </Button>
                </Form.Item>
            </Form>
            <ResetPassword
                visible={resetVisiable}
                resetCancel={resetCancel}
            />
        </div>
    )

}

export default memo(SignIn)
