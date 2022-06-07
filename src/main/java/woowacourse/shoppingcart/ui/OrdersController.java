package woowacourse.shoppingcart.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.application.OrdersService;
import woowacourse.shoppingcart.dto.customer.LoginCustomer;
import woowacourse.shoppingcart.dto.orders.OrdersDetailRequest;
import woowacourse.shoppingcart.dto.orders.OrdersResponse;

@RestController
@RequestMapping("/api/customers/me/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping
    public ResponseEntity<Void> addOrders(@AuthenticationPrincipal LoginCustomer loginCustomer,
                                          @RequestBody @Valid List<OrdersDetailRequest> orderDetails) {
        OrdersResponse ordersResponse = ordersService.save(orderDetails, loginCustomer.getUsername());
        return ResponseEntity.created(
                URI.create("/api/me/orders/" + ordersResponse.getId())).build();
    }
}
