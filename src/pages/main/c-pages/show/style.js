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
            text-align: center;
        
            .click:hover {
                cursor:pointer
            }

            h3 {
                color: red;
            }
            .slick-dots-top {       
                li {
                    background-color: gray;
                }

                .slick-active {
                    button{
                        background-color: red;
                    }
                }                
            }
        } 

    }
`
