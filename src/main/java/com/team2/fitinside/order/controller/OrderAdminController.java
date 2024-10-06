package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.OrderResponseDto;
import com.team2.fitinside.order.dto.OrderStatusUpdateRequestDto;
import com.team2.fitinside.order.exception.OrderNotFoundException;
import com.team2.fitinside.order.service.OrderAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/orders")
public class OrderAdminController {

    private final OrderAdminService orderAdminService;

    // 주문 전체 조회
    @GetMapping
    public ResponseEntity<?> findAllOrdersByAdmin() {
        List<OrderResponseDto> response = orderAdminService.findAllOrdersByAdmin();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 주문 상태 수정 - 굳이 dto로 안 보여줘도 될 것 같은데.. 어차피 주문 목록 바로 띄우지않나?
    @PatchMapping("/{order_id}/status")
    public ResponseEntity<?> updateStatusOrder(
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderStatusUpdateRequestDto request) {

        try {
            OrderResponseDto response = orderAdminService.updateOrderStatus(orderId, request);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 주문 삭제
    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order_id") Long orderId) {
        try {
            orderAdminService.deleteOrder(orderId);
            return new ResponseEntity<>("주문 삭제 완료. orderId: " + orderId, HttpStatus.OK);
        } catch (OrderNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND); // 404
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
