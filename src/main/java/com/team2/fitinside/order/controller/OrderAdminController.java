package com.team2.fitinside.order.controller;

import com.team2.fitinside.order.dto.OrderResponseDto;
import com.team2.fitinside.order.dto.OrderStatusUpdateRequestDto;
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

    @GetMapping
    public ResponseEntity<?> findAllOrdersByAdmin() {
        List<OrderResponseDto> response = orderAdminService.findAllOrdersByAdmin();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // 굳이 dto로 안 보여줘도 될 것 같은데.. 나중에 확인
    @PatchMapping("/{order_id}/status")
    public ResponseEntity<?> updateStatusOrder(
            @PathVariable("order_id") Long orderId,
            @RequestBody OrderStatusUpdateRequestDto request) throws Exception {

        OrderResponseDto response = orderAdminService.updateOrderStatus(orderId, request);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<?> deleteOrder(@PathVariable("order_id") Long orderId) throws Exception {
        orderAdminService.deleteOrder(orderId);
        return ResponseEntity.status(HttpStatus.OK).body("주문 삭제 완료. orderId: " + orderId);
    }

}
