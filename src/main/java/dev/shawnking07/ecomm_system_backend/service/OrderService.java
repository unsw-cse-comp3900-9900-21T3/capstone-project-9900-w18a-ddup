package dev.shawnking07.ecomm_system_backend.service;

import dev.shawnking07.ecomm_system_backend.dto.DiscountVM;
import dev.shawnking07.ecomm_system_backend.dto.OrderDTO;
import dev.shawnking07.ecomm_system_backend.dto.OrderVM;
import dev.shawnking07.ecomm_system_backend.entity.Order;

public interface OrderService {
    /**
     * create and cached OrderVM, but not save <br> load and check amount info in redis
     *
     * @param orderDTO
     * @return
     */
    OrderVM createOrder(OrderDTO orderDTO);

    /**
     * Discount will only be activated by access this interface {@code n} times
     *
     * @param orderNumber order uuid
     * @return discount info
     */
    DiscountVM operateDiscount(String orderNumber);

    OrderVM queryOrder(String orderNumber);

    /**
     * save cached Order
     *
     * @param orderNumber generated orderNumber not id
     */
    void confirmOrder(String orderNumber);

    OrderVM order2orderVM(Order order);
}
