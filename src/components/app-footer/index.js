import React, { memo } from "react";
import { CopyrightOutlined, WechatOutlined } from "@ant-design/icons";

import {
    AppFooterWrapper
} from "./style"

function AppFooter() {
    return (
        <AppFooterWrapper>
            <div> <CopyrightOutlined /> UNSW 9900 </div>
            <div> <WechatOutlined /> AnyScript123</div>
        </AppFooterWrapper>
    )
}

export default memo(AppFooter)
