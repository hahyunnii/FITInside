package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.OrderDetailResponseDto;
import com.team2.fitinside.order.dto.OrderRequestDto;
import com.team2.fitinside.order.dto.OrderUserResponseDto;
import com.team2.fitinside.order.exception.CartEmptyException;
import com.team2.fitinside.order.exception.OrderModificationNotAllowedException;
import com.team2.fitinside.order.exception.OrderNotFoundException;
import com.team2.fitinside.order.exception.OutOfStockException;
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

    // 주문 조회
    @GetMapping("/{user_id}/orders/{order_id}")
    public ResponseEntity<?> findOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId) {

        try {
            OrderDetailResponseDto response = orderService.findOrder(userId, orderId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 주문 전체 조회
    @GetMapping("/{user_id}/orders")
    public ResponseEntity<?> findAllOrders(@PathVariable("user_id") Long userId) {
        try {
            List<OrderUserResponseDto> response = orderService.findAllOrders(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 주문 생성
    @PostMapping("/{user_id}/orders")
    public ResponseEntity<?> createOrder(
            @PathVariable("user_id") Long userId,
            @RequestBody OrderRequestDto request) {

        try {
            OrderDetailResponseDto response = orderService.createOrder(userId, request);
            return new ResponseEntity<>(response, HttpStatus.CREATED); // 201
        } catch (CartEmptyException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        } catch (OutOfStockException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR); // 500
        }

    }

    // 주문 수정
    @PatchMapping("/{user_id}/orders/{order_id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderRequestDto request) {

        try {
            OrderDetailResponseDto response = orderService.updateOrder(userId, orderId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        } catch (OrderModificationNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 주문 취소
    @DeleteMapping("/{user_id}/orders/{order_id}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId) {

        try {
            orderService.cancelOrder(userId, orderId);
            return new ResponseEntity<>("주문 취소 완료. orderId: " + orderId, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        } catch (OrderModificationNotAllowedException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // 400
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
