package com.errday.userservice.client;

import com.errday.userservice.error.FeignErrorDecoder;
import com.errday.userservice.vo.ResponseOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "order-service", configuration = FeignErrorDecoder.class)
public interface OrderServiceClient {

    @GetMapping("/order-service/{userId}/orders")
    //@GetMapping("/order-service/{userId}/orders_error")
    List<ResponseOrder> getOrders(@PathVariable("userId") String userId);


}
