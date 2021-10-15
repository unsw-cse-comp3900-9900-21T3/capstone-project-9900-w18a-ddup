export const displayPersentage = (discount, origin) => {
    if(discount <= origin){
        return `-${Math.round((origin-discount)/origin*100)}%`  
    } else {
        return discount
    }
}