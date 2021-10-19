import { combineReducers } from "redux";

import { reducer as userReducer } from "@/components/signIn/store";
import { reducer as productReducer } from "@/pages/management/store";
import { reducer as cartReducer } from "@/pages/cart/store";
import { reducer as orderReducer } from "@/pages/order/store" 

const cReducer = combineReducers({
    User: userReducer,
    Product: productReducer,
    Cart: cartReducer,
    Order: orderReducer,
})

export default cReducer
