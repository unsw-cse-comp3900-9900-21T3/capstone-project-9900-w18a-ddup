import style from "styled-components";

export const AppHeaderWrapper = style.div`
    height: 80px;
    background-color: #242424;
    display: flex;
    justify-content: space-between;

    .header-middle {
        font-size: 20px;
        line-height: 80px;
        :hover {
            cursor:pointer
        }

        h1 {
            color: #fff;
        }
    }

    .header-right {
        width: 150px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        margin-right: 5px;
    }
    
`
