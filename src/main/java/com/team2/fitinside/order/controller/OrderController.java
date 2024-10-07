package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.OrderDetailResponseDto;
import com.team2.fitinside.order.dto.OrderRequestDto;
import com.team2.fitinside.order.dto.OrderUserResponseDto;
import com.team2.fitinside.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{order_id}")
    public ResponseEntity<?> findOrder(@PathVariable("order_id") Long orderId) throws AccessDeniedException {
        OrderDetailResponseDto response = orderService.findOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping
    public ResponseEntity<?> findAllOrders() {
        List<OrderUserResponseDto> response = orderService.findAllOrders();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDto request) {
        OrderDetailResponseDto response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{order_id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderRequestDto request) throws AccessDeniedException {

        OrderDetailResponseDto response = orderService.updateOrder(orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("order_id") Long orderId) throws AccessDeniedException {
        orderService.cancelOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문 취소 완료. orderId: " + orderId);
    }

}
