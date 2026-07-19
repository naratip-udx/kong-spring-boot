package com.github.naratipudx.order.service;

import com.github.naratipudx.order.dto.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Override
    public Order findById(Long orderId) {
        var order = new Order();
        order.setId(orderId);
        order.setUserId("user_999");
        order.setProduct("Sample Product");
        order.setQuantity(1);
        return order;
    }

    @Override
    public Order create(Order order) {
        order.setId(123L); // สมมติว่ามีการสร้าง Order ใหม่และกำหนด ID ให้
        order.setUserId("user_999");
        return order;
    }
}
