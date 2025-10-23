package com.errday.orderservice.controller;

import com.errday.orderservice.dto.OrderDto;
import com.errday.orderservice.messagequeue.KafkaProducer;
import com.errday.orderservice.messagequeue.OrderProducer;
import com.errday.orderservice.service.OrderService;
import com.errday.orderservice.vo.RequestOrder;
import com.errday.orderservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/order-service")
public class OrderController {

    private final Environment env;
    private final OrderService orderService;
    private final ModelMapper modelMapper = new  ModelMapper();
    private final KafkaProducer kafkaProducer;
    private final OrderProducer orderProducer;

    @GetMapping("/health-check")
    public String status() {
        return String.format("It's Working in Order Service on LOCAL PORT %s (SERVER PORT %s)",
                env.getProperty("local.server.port"),
                env.getProperty("server.port"));
    }

    @PostMapping("/{userId}/orders")
    public ResponseEntity<ResponseOrder> createOrder(@PathVariable String userId, @RequestBody RequestOrder request) {
        log.info("Before add orders data");
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        OrderDto orderDto = modelMapper.map(request, OrderDto.class);
        orderDto.setUserId(userId);

        ResponseOrder responseOrder = modelMapper.map(orderService.createOrder(orderDto), ResponseOrder.class);

        //orderDto.setOrderId(UUID.randomUUID().toString());
        //orderDto.setTotalPrice(request.getQuantity() * request.getUnitPrice());
        //ResponseOrder responseOrder = modelMapper.map(orderDto, ResponseOrder.class);

        //kafkaProducer.send("example-catalog-topic", orderDto);
        //orderProducer.send("orders", orderDto);

        log.info("After added orders data");
        return ResponseEntity.status(HttpStatus.CREATED).body(responseOrder);
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<List<ResponseOrder>> getOrders(@PathVariable String userId) {
        log.info("Before retrieve orders data");
        List<ResponseOrder> findByUserId = orderService.findByUserId(userId)
                .stream()
                .map(orderDto -> modelMapper.map(orderDto, ResponseOrder.class))
                .toList();
        log.info("After retrieve orders data");
        return ResponseEntity.status(HttpStatus.OK).body(findByUserId);
    }



}

