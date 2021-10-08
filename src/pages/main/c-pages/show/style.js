import style from 'styled-components';

export const ShowWrapper = style.div`
    .carousels {
        height: 500px;
        display: flex;
        justify-content: space-around;
        align-items: center;
        border-bottom: 1px solid #000;

        h2 {
            text-align: center;
        }

        .left-carousel, .right-carousel {
            width: 400px;
            height: 400px;
            background-color:  blue;

            div {
                height: 350px;
            }
        } 

    }
`
