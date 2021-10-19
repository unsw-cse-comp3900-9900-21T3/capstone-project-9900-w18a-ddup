package dev.shawnking07.ecomm_system_backend.api;

import dev.shawnking07.ecomm_system_backend.dto.*;
import dev.shawnking07.ecomm_system_backend.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Create an order, but not save")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public OrderVM createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

    @Operation(summary = "Shared link operate discount for all items in order")
    @GetMapping("/discount")
    public DiscountVM operateDiscount(@Valid @RequestParam String orderNumber) {
        return orderService.operateDiscount(orderNumber);
    }

    @Operation(summary = "Query order", description = "query order which has not been confirmed")
    @GetMapping("/{orderNumber}")
    public OrderVM queryOrder(@PathVariable String orderNumber) {
        return orderService.queryOrder(orderNumber);
    }

    @Operation(summary = "Save the order")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/confirm")
    public void confirmOrder(@Valid @RequestBody OrderNumberDTO orderNumberDTO) {
        orderService.confirmOrder(orderNumberDTO.getOrderNumber());
    }

    @Operation(summary = "My orders")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping
    public MyOrderVM findAllMyOrders() {
        return orderService.findMyOrders();
    }
}
