package woowacourse.shoppingcart.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.shoppingcart.application.OrdersService;
import woowacourse.shoppingcart.dto.orders.OrdersDetailRequest;
import woowacourse.shoppingcart.dto.orders.OrdersResponse;

@RestController
@RequestMapping("/api/customers/{customerName}/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrders(@PathVariable String customerName,
                                          @RequestBody @Valid List<OrdersDetailRequest> orderDetails) {
        OrdersResponse ordersResponse = ordersService.save(orderDetails, customerName);
        return ResponseEntity.created(
                URI.create("/api/" + customerName + "/orders/" + ordersResponse.getId())).build();
    }
}
