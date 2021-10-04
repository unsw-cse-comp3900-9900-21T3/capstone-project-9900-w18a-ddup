package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.OrderDTO;
import dev.shawnking07.ecomm_system_backend.dto.OrderVM;

public interface OrderService {
    /**
     * create and cached OrderVM, but not save <br> load and check amount info in redis
     *
     * @param orderDTO
     * @return
     */
    OrderVM createOrder(OrderDTO orderDTO);

    /**
     * save cached Order
     *
     * @param orderNumber generated orderNumber not id
     */
    void confirmOrder(String orderNumber);
}
