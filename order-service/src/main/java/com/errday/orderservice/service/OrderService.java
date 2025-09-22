package com.errday.orderservice.service;

import com.errday.orderservice.dto.OrderDto;

import java.util.List;

public interface OrderService {
    OrderDto findByOrderId(String orderId);
    List<OrderDto> findByUserId(String userId);

    OrderDto createOrder(OrderDto orderDto);
}
