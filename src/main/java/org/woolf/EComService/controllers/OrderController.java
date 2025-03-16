package org.woolf.EComService.controllers;

import org.woolf.EComService.dtos.OrderDto;
import org.woolf.EComService.exceptions.InsufficientStockException;
import org.woolf.EComService.exceptions.NotFoundException;
import org.woolf.EComService.models.User;
import org.woolf.EComService.models.order.Order;
import org.woolf.EComService.models.order.OrderStatus;
import org.woolf.EComService.repositories.UserRepository;
import org.woolf.EComService.services.OrderService;
import org.woolf.EComService.utils.UserUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserRepository userRepository;
    public OrderController(OrderService orderService, UserRepository userRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
    }

    // Place order (initiate payment)
    @PostMapping("/place")
    public ResponseEntity<OrderDto> placeOrder(Authentication authentication) throws InsufficientStockException, NotFoundException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        User user = UserUtils.createUserIfNotExist(jwt, userRepository);

        Order order = orderService.placeOrder(user.getId());
        return new ResponseEntity<>(OrderDto.fromOrder(order), HttpStatus.CREATED);
    }

    // Confirm payment (called by frontend after payment completion)
    @PostMapping("/confirm-payment/{transactionId}")
    public ResponseEntity<OrderDto> confirmPayment(@PathVariable String transactionId) throws NotFoundException {
        Order order = orderService.confirmPayment(transactionId);
        return new ResponseEntity<>(OrderDto.fromOrder(order), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) throws NotFoundException {
        Order order = orderService.getOrder(orderId);
        return new ResponseEntity<>(OrderDto.fromOrder(order), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders(Authentication authentication,
                                                                 @RequestParam(required = false) OrderStatus status) throws NotFoundException {
        Jwt jwt = ((JwtAuthenticationToken) authentication).getToken();
        User user = UserUtils.createUserIfNotExist(jwt, userRepository);
        List<Order> orders = orderService.getAllOrders(user.getId(), status);

        List<OrderDto> orderDtoList =new ArrayList<>();
        for (Order order : orders) {
            orderDtoList.add(OrderDto.fromOrder(order));
        }
        return new ResponseEntity<>(orderDtoList, HttpStatus.OK);
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity<OrderDto> cancelOrder(@PathVariable Long orderId) throws NotFoundException {
        Order order = orderService.cancelOrder(orderId);
        return new ResponseEntity<>(OrderDto.fromOrder(order), HttpStatus.OK);
    }
}
