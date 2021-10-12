import style from "styled-components";

export const ProductCardWrapper = style.div`
    border-bottom: 1px solid #000;

    .pagination {
        margin: 20px auto;
        text-align: center;
    }

    .card {
        :hover {
            cursor:pointer
        }
    }
`
