import React, { memo } from "react";

import { ShowWrapper } from './style';
import { Carousel } from 'antd';

function Show() {





    return (
        <ShowWrapper>
            <div className='carousels'>
                <div className='left-carousel'>
                    <h2> recommendation </h2>
                    <Carousel 
                        autoplay
                    >
                        <div >
                            1
                        </div>
                        <div>
                            2
                        </div>
                        <div>
                            3
                        </div>
                    </Carousel>
                </div>
                <div className='right-carousel'>
                    <h2> group-buying </h2>
                    <Carousel autoplay >
                        <div>
                            3
                        </div>
                        <div>
                            4
                        </div>
                        <div>
                            5
                        </div>

                    </Carousel>
                </div>
            </div>
            
        </ShowWrapper>
    )
}

export default memo(Show)
