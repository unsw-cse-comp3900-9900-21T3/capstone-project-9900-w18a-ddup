import React, { useCallback } from "react";
import { useSelector, useDispatch, shallowEqual } from "react-redux";
import { withRouter } from "react-router-dom";

import { customerAvatarMenu, adminAvatarMenu } from "@/constants";
import { AvatarWrapper } from "./style";
import { clearUserAuthorityAction } from "../signIn/store/actionCreators";
import {
    Menu,
    Dropdown
} from "antd";
import {
    UserOutlined,
    TeamOutlined
} from "@ant-design/icons";

function Avatar({ history }) {

    const { userName } = useSelector(state => ({
        userName: state.User.userAuthority.username
    }), shallowEqual)
    const dispatch = useDispatch()

    function handleMenuClick(e) {
        if (e.key === 'logout') {
            dispatch(clearUserAuthorityAction())
            history.push('/')
        } else {
            history.push(`/${e.key}`)
        }
    }

    const customerMenu = useCallback(() => (
        <Menu onClick={handleMenuClick}>
            {customerAvatarMenu.map((item, _index) => (
                <Menu.Item key={item.key}>
                    {item.title}
                </Menu.Item>
            ))}
        </Menu>
    ), [customerAvatarMenu])

    const adminMenu = useCallback(() => (
        <Menu onClick={handleMenuClick}>
            {adminAvatarMenu.map((item, _index) => (
                <Menu.Item key={item.key}>
                    {item.title}
                </Menu.Item>
            ))}
        </Menu>
    ), [adminAvatarMenu])

    return (
        <AvatarWrapper>
            <Dropdown.Button
                overlay={userName === 'admin' ? adminMenu : customerMenu}
                placement="bottomCenter"
                icon={userName === 'admin' ? <TeamOutlined /> : <UserOutlined />}
            />
        </AvatarWrapper>
    )
}

export default withRouter(Avatar)
