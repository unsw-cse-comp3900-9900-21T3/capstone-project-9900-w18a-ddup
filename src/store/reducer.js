import { combineReducers } from "redux"

import { reducer as userReducer } from "@/components/signIn/store"
import { reducer as productReducer } from "@/pages/management/store"

const cReducer = combineReducers({
    User: userReducer,
    Product: productReducer,
})

export default cReducer
