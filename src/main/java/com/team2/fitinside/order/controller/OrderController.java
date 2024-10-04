package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.*;
import com.team2.fitinside.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping("/users/{user_id}/orders")
    public ResponseEntity<?> createOrder(
            @PathVariable("user_id") Long userId,
            @RequestBody OrderRequestDto request) {

        try{
            OrderDetailResponseDto response = orderService.createOrder(userId, request);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch(Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    // 주문 수정(회원)
    @PatchMapping("/users/{user_id}/orders/{order_id}")
    public ResponseEntity<?> updateOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderRequestDto request){

        try {
            OrderDetailResponseDto response = orderService.updateOrder(userId, orderId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    // 주문 상태 수정(관리자) - 굳이 dto로 안 보여줘도 될 것 같은데.. 어차피 주문 목록 바로 띄우지않나?
    @PatchMapping("/admin/orders/{order_id}/status")
    public ResponseEntity<?> updateStatusOrder(
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderStatusUpdateRequestDto request){

        try {
            OrderResponseDto response = orderService.updateOrderStatus(orderId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    // 주문 취소(회원)
    @DeleteMapping("/users/{user_id}/orders/{order_id}")
    public ResponseEntity<?> cancelOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId){

        try {
            orderService.cancelOrder(userId, orderId);
            return new ResponseEntity<>("주문 취소 완료", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    // 주문 삭제 (관리자)
    @DeleteMapping("/admin/orders/{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order_id") Long orderId){
        try {
            orderService.deleteOrder(orderId);
            return new ResponseEntity<>("주문 삭제 완료", HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 주문 조회 (회원)
    @GetMapping("/users/{user_id}/orders/{order_id}")
    public ResponseEntity<?> findOrder(
            @PathVariable("user_id") Long userId,
            @PathVariable("order_id") Long orderId){

        try {
            OrderDetailResponseDto response = orderService.findOrder(userId, orderId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }

    }

    // 주문 전체 조회 (회원)
    @GetMapping("/users/{user_id}/orders")
    public ResponseEntity<?> findAllOrders(@PathVariable("user_id") Long userId) {
        try{
            List<OrderUserResponseDto> response = orderService.findAllOrders(userId);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    // 주문 전체 조회 (관리자)
    @GetMapping("/admin/orders")
    public ResponseEntity<?> findAllOrdersByAdmin(){
        List<OrderResponseDto> response = orderService.findAllOrdersByAdmin();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
