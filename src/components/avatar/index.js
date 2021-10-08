import React from "react";
import { useSelector, useDispatch, shallowEqual } from "react-redux";
import { withRouter } from "react-router-dom";
import {
    Menu,
    Dropdown
} from "antd";
import {
    UserOutlined,
    TeamOutlined
} from "@ant-design/icons";

import { customerAvatarMenu, adminAvatarMenu, adminArr } from "@/constants";
import { AvatarWrapper } from "./style";
import { clearUserIDInfoAction } from "../signIn/store/actionCreators";

function Avatar({ history }) {

    const { userName } = useSelector(state => ({
        userName: state.User.UserIDInfo.username
    }), shallowEqual)
    const dispatch = useDispatch()

    function handleMenuClick(e) {
        if (e.key === 'logout') {
            dispatch(clearUserIDInfoAction())
            history.push('/')
        } else {
            history.push(`/${e.key}`)
        }
    }

    const customerMenu = (
        <Menu onClick={handleMenuClick}>
            {customerAvatarMenu.map((item, _index) => (
                <Menu.Item key={item.key}>
                    {item.title}
                </Menu.Item>
            ))}
        </Menu>
    )

    const adminMenu = (
        <Menu onClick={handleMenuClick}>
            {adminAvatarMenu.map((item, _index) => (
                <Menu.Item key={item.key}>
                    {item.title}
                </Menu.Item>
            ))}
        </Menu>
    )

    return (
        <AvatarWrapper>
            <Dropdown.Button
                overlay={adminArr.includes(userName) ? adminMenu : customerMenu}
                placement="bottomCenter"
                icon={adminArr.includes(userName) ? <TeamOutlined /> : <UserOutlined />}
            />
        </AvatarWrapper>
    )
}

export default withRouter(Avatar)
