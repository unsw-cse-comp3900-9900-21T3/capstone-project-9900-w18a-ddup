import React, { memo } from "react";

import { Form, Input, Button, Modal } from "antd"

function ResetPassword({visible, resetCancel}) {

    function onFinish(values) {
        console.log('reset password!', values)
    }

    return (
        <Modal
            title='Reset Password'
            centered
            footer={null}
            visible={visible}
            onCancel={()=>{resetCancel()}}
        >   
            <Form
                name='signIn'
                labelCol={{ span: 8 }}
                wrapperCol={{ span: 12 }}
                onFinish={onFinish}
            >
                <Form.Item
                    label="Verification:"
                    name="Verification:"
                    rules={[
                        {
                            required: true,
                            message: 'Please input your Verification:!',
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

                <Form.Item
                    wrapperCol={{
                        offset: 8,
                        span: 16,
                    }}
                >
                    <Button type="primary" htmlType="submit">
                        Submit
                    </Button>
                </Form.Item>

            </Form>
        </Modal>
    )
}

export default memo(ResetPassword)
