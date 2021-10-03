import { combineReducers } from "redux"

import { reducer as userReducer } from "@/components/signIn/store"

const cReducer = combineReducers({
    User: userReducer
})

export default cReducer
