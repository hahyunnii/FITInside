package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.OrderDetailResponseDto;
import com.team2.fitinside.order.dto.OrderRequestDto;
import com.team2.fitinside.order.dto.OrderUserResponseDto;
import com.team2.fitinside.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{user_id}/orders/{order_id}")
    public ResponseEntity<?> findOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId) throws Exception {

        OrderDetailResponseDto response = orderService.findOrder(userId, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{user_id}/orders")
    public ResponseEntity<?> findAllOrders(@PathVariable("user_id") Long userId) throws Exception {
        List<OrderUserResponseDto> response = orderService.findAllOrders(userId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/{user_id}/orders")
    public ResponseEntity<?> createOrder(
            @PathVariable("user_id") Long userId,
            @RequestBody OrderRequestDto request) throws Exception {

        OrderDetailResponseDto response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{user_id}/orders/{order_id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderRequestDto request) throws Exception {

        OrderDetailResponseDto response = orderService.updateOrder(userId, orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{user_id}/orders/{order_id}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId) throws Exception {

        orderService.cancelOrder(userId, orderId);
        return new ResponseEntity<>("주문 취소 완료. orderId: " + orderId, HttpStatus.OK);
    }

}
