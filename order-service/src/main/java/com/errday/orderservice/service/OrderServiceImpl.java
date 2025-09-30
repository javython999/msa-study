package com.errday.orderservice.service;

import com.errday.orderservice.dto.OrderDto;
import com.errday.orderservice.jpa.OrderEntity;
import com.errday.orderservice.jpa.OrderRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    @Override
    public OrderDto findByOrderId(String orderId) {
        OrderEntity orderEntity = orderRepository.findByOrderId(orderId)
                .orElseThrow(() -> new NotFoundException("Order not found by order id: " + orderId));
        return modelMapper.map(orderEntity, OrderDto.class);
    }

    @Override
    public List<OrderDto> findByUserId(String userId) {
        return orderRepository.findAll()
                .stream()
                .filter(order -> order.getUserId().equals(userId))
                .map(orderEntity -> modelMapper.map(orderEntity, OrderDto.class))
                .toList();
    }

    @Override
    public OrderDto createOrder(OrderDto orderDto) {
        orderDto.setOrderId(UUID.randomUUID().toString());
        orderDto.setTotalPrice(orderDto.getQuantity() * orderDto.getUnitPrice());

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        OrderEntity saved = orderRepository.save(modelMapper.map(orderDto, OrderEntity.class));

        return modelMapper.map(saved, OrderDto.class);
    }
}
