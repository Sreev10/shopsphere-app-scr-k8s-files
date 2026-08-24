package com.shopsphere.order;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderRepository orders; private final OrderItemRepository items;
    public OrderController(OrderRepository orders, OrderItemRepository items){this.orders=orders;this.items=items;}

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public OrderResponse create(@Valid @RequestBody CreateOrderRequest request){
        BigDecimal total=request.items().stream().map(i -> i.unitPrice().multiply(BigDecimal.valueOf(i.quantity()))).reduce(BigDecimal.ZERO,BigDecimal::add);
        CustomerOrder o=new CustomerOrder(); o.setUserId(request.userId()); o.setTotal(total); o.setStatus(OrderStatus.CREATED); o.setCreatedAt(OffsetDateTime.now());
        o=orders.save(o);
        for(ItemRequest i:request.items()) { OrderItem x=new OrderItem(); x.setOrderId(o.getId()); x.setProductId(i.productId()); x.setQuantity(i.quantity()); x.setUnitPrice(i.unitPrice()); items.save(x); }
        return new OrderResponse(o.getId(),o.getUserId(),o.getTotal(),o.getStatus(),o.getCreatedAt(),items.findByOrderId(o.getId()));
    }

    @GetMapping("/user/{userId}") public List<CustomerOrder> byUser(@PathVariable Long userId){return orders.findByUserIdOrderByCreatedAtDesc(userId);}

    public record CreateOrderRequest(@NotNull Long userId,@NotEmpty List<@Valid ItemRequest> items){}
    public record ItemRequest(@NotNull Long productId,@Min(1) Integer quantity,@NotNull BigDecimal unitPrice){}
    public record OrderResponse(Long id,Long userId,BigDecimal total,OrderStatus status,OffsetDateTime createdAt,List<OrderItem> items){}
}
