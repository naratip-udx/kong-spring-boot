package com.github.naratipudx.order.controller;

import com.github.naratipudx.order.dto.Order;
import com.github.naratipudx.order.service.OrderService;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderCtrl {

    private final OrderService orderService;

    public OrderCtrl(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')") // ต้องมี Role USER เท่านั้นถึงจะสร้าง Order ได้
    public ResponseEntity<Order> createOrder(@RequestBody Order order) {
        return ResponseEntity.ok(orderService.create(order));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<Order> getOrder(@PathVariable Long orderId, Authentication authentication) {
        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String loggedInUserId = authentication.getName();
        var order = orderService.findById(orderId);
        // ตรวจสอบว่าผู้ใช้ที่เรียกดู เป็นเจ้าของ Order นั้นหรือไม่ หรือผู้ใช้เป็น Admin
        if (!order.getUserId().equals(loggedInUserId) && !isUserAdmin()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(order);
    }

    private boolean isUserAdmin() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return auth.getAuthorities().stream()
            .anyMatch(a -> Objects.equals(a.getAuthority(), "ROLE_ADMIN"));
    }
}
