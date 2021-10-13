import React, { memo, useState } from "react";
import { Pagination, Card, message } from "antd";

import { ProductCardWrapper } from "./style";
import { itemsPerPageForCustomer, imgBaseURL } from "@/constants";

const gridStyle = {
    width: `${200 / itemsPerPageForCustomer}%`,
    height: '350px',
    textAlign: 'center',
};


function ProductCard({ title, infoArr, token, history }) {
    const [current, setCurrent] = useState(1)

    function cardClick(item) {
        if (token === undefined) {
            message.error('please login')
        } else {
            history.push('/detail/' + item.id)
        }
    }
    function productsShow() {
        return infoArr.slice((current - 1) * itemsPerPageForCustomer, current * itemsPerPageForCustomer)
            .map((item, index) => {
                return (
                    <Card.Grid
                        key={index}
                        style={gridStyle}
                        className='card'
                    >
                        <div onClick={() => { cardClick(item) }}>
                            <img
                                alt='ing...'
                                width={300}
                                src={imgBaseURL + item.imagePaths[0]}
                            />
                            <h3> {item.name} </h3>
                            <h4> {item.price} </h4>
                            {item.discountPrice !== 0 &&
                                <h4
                                    style={{ color: 'red' }}
                                >
                                    {item.discountPrice}
                                </h4>}
                        </div>
                    </Card.Grid>
                )
            })
    }

    return (
        <ProductCardWrapper>
            <Card title={title}>
                {productsShow()}
            </Card >
            <Pagination
                className='pagination'
                current={current}
                onChange={(page) => { setCurrent(page) }}
                total={infoArr.length}
                defaultPageSize={itemsPerPageForCustomer} />
        </ProductCardWrapper>
    )
}

export default memo(ProductCard)
