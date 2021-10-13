import style from "styled-components";

export const DetailWrapper = style.div`
    background-color: #f5f5f5;

    .main {
        width: 1000px;
        margin: 0 auto;
        background-color: #fff;
        height: 500px;
        display: flex;
    
        .main-right {
            width: 400px;
            text-align: left;
            margin-left: 50px;

            p {
                width: 200px;
                word-wrap: break-word;
            }

            .option {
                margin: 30px;

                button {
                    margin: 10px;
                }
            }
        }
    }
`
