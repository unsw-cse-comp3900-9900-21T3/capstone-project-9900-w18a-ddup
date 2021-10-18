import {
    ADD_PRODUCT,
    SUB_PRODUCT,
} from "./constants";

export const addProductAction = id => ({
    type: ADD_PRODUCT,
    id
})

export const subProductAction = id => ({
    type: SUB_PRODUCT,
    id
})
