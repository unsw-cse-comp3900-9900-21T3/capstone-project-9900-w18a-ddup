import React, { useState } from "react";
import { withRouter } from "react-router-dom";
import { useSelector, shallowEqual } from "react-redux";

import {
    Button,
    Modal
} from "antd";
import {
    AppHeaderWrapper
} from "./style";
import SignIn from "@/components/signIn";
import SignUp from "@/components/signUp";
import Avatar from "@/components/avatar";


function AppHeader(props) {
    const history = props.history
    const [signInVisible, setSignInVisible] = useState(false)
    const [signUpVisible, setSignUpVisible] = useState(false)

    const { userIDInfo={} } = useSelector(state => ({
        userIDInfo: state.User.UserIDInfo
    }), shallowEqual)

    return (
        <AppHeaderWrapper>
            <div />
            <div
                className='header-middle'
                onClick={() => { history.push('/') }}
            >
                <h1> DDUP shopping </h1>
            </div>

            {userIDInfo.username ?
                <Avatar/>
                :
                <div className='header-right'>
                    <Button
                        type='primary'
                        onClick={() => { setSignInVisible(true) }}
                    >
                        SignIn
                    </Button>
                    <Button
                        type='primary'
                        onClick={() => { setSignUpVisible(true) }}
                    >
                        SignUp
                    </Button>
                </div>}
            <Modal
                title='Sign In'
                centered
                visible={signInVisible}
                onOk={() => { setSignInVisible(false) }}
                onCancel={() => { setSignInVisible(false) }}
                footer={null}
            >
                <SignIn signInCancel={() => {setSignInVisible(false)}} />
            </Modal>
            <Modal
                title='Sign Up'
                centered
                visible={signUpVisible}
                onCancel={() => { setSignUpVisible(false) }}
                footer={null}
            >
                <SignUp signUpCancel={() => {setSignUpVisible(false)}}/>
            </Modal>
        </AppHeaderWrapper>
    )
}

export default withRouter(AppHeader)
