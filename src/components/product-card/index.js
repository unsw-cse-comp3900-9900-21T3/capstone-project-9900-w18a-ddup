import React, { memo, useState } from "react";
import { Pagination, Card } from "antd";

import { ProductCardWrapper } from "./style";

const gridStyle = {
    width: '33.3%',
    height: '400px',
    textAlign: 'center',
  };


function ProductCard() {
    const [current, setCurrent] = useState(1)


    function onChange(page) {
        console.log(page)
        setCurrent(page)
    }

    return (
        <ProductCardWrapper>
            <Card title='lalala'>
                <Card.Grid style={gridStyle}>Content</Card.Grid>
                <Card.Grid style={gridStyle}>Content</Card.Grid>
                <Card.Grid style={gridStyle}>Content</Card.Grid>
                <Card.Grid style={gridStyle}>Content</Card.Grid>
                <Card.Grid style={gridStyle}>Content</Card.Grid>
                <Card.Grid style={gridStyle}>Content</Card.Grid>
            </Card >
            <Pagination className='card' current={current} onChange={(page) => { onChange(page) }} total={15} defaultPageSize={6}/>
        </ProductCardWrapper>
    )
}

export default memo(ProductCard)
