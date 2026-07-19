package com.github.naratipudx.order.service;

import com.github.naratipudx.order.dto.Order;

public interface OrderService {

    Order findById(Long orderId);

    Order create(Order order);
}
