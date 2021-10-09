import style from "styled-components";

export const MainWrapper = style.div`
    background-color: #f5f5f5;
`

export const MainHeaderWrapper = style.div`
    height: 80px;
    border-bottom: 1px solid #000;

    .main-header {
        width: 1000px;
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        align-items: center;

        .ant-input-affix-wrapper {
            width: 500px;
            height: 50px;
            font-size: 18px; 
            margin-right: 200px;
        }
    }

    .main-logo {
        width: 150px;
        height: 80px;
        background-color: red;
    }
`
export const MainFooterWrapper = style.div`
    // height: 1000px;
    width: 1000px;
    background-color: #fff;
    margin: 0 auto;
`

