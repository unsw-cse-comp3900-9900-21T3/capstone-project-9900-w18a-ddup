import React, { memo, useEffect, useRef, useState } from "react";
import { useSelector, useDispatch, shallowEqual } from "react-redux";
import { UserOutlined } from "@ant-design/icons";
import { Form, Input, Button, message } from "antd"

import { getUserInfoAction } from "@/components/signIn/store/actionCreators"
import { ProfileWrapper } from "./style";
import { editUserInfo } from "@/services/user";

function Profile() {
    const [loading, setLoading] = useState(false)

    const dispatch = useDispatch()
    const { token, username, address, password } = useSelector(state => ({
        token: state.User.UserIDInfo.token,
        username: state.User.UserIDInfo.username,
        address: state.User.UserIDInfo.address,
        password: state.User.UserIDInfo.password,
    }), shallowEqual)

    const form = useRef()
    useEffect(() => {
        dispatch(getUserInfoAction(token))
    }, [dispatch, token, address, password])
    useEffect(() => {
        form.current.setFieldsValue({
            address: address,
        })
    }, [address])

    const onFinish = values => {
        setLoading(true)
        editUserInfo(values, token).then(() => {
            dispatch(getUserInfoAction(token))
            message.success('edit success!', 0.5, () => {
                setLoading(false)
            })
        }
        )
    }

    return (
        <ProfileWrapper>
            <div className='profile-header'>
                <div className='avatar'>
                    <UserOutlined style={{ fontSize: '300px' }} />
                </div>
                <div className='form'>
                    <Form
                        name='signUp'
                        labelCol={{ span: 8 }}
                        wrapperCol={{ span: 24 }}
                        onFinish={onFinish}
                        ref={form}
                    >
                        <Form.Item
                            label="Username"
                            name="username"
                            initialValue={username}
                            rules={[
                                {
                                    required: true,
                                    message: 'Please input your username!',
                                },
                            ]}
                        >
                            <Input disabled />
                        </Form.Item>

                        <Form.Item
                            label="E-mail"
                            name="address"
                            initialValue={address}
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
                            label="Phone"
                            name="phone"
                        // rules={[
                        //     {
                        //         required: true,
                        //         message: 'Please input your phone number!',
                        //     },
                        // ]}
                        >
                            <Input />
                        </Form.Item>

                        <Form.Item
                            label="Password"
                            name="password"
                            initialValue={password}
                            rules={[
                                {
                                    required: true,
                                    message: 'Please input your password!',
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
                                Edit
                            </Button>
                        </Form.Item>
                    </Form>
                </div>
                <div />
            </div>
        </ProfileWrapper>
    )
}

export default memo(Profile)
