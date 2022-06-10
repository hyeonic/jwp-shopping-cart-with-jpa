package woowacourse.shoppingcart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_SAVE_REQUEST;
import static woowacourse.shoppingcart.ProductFixtures.TWO_PRODUCT_SAVE_REQUEST;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.dto.cartitem.CartItemResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemSaveRequest;
import woowacourse.shoppingcart.dto.orders.OrdersDetailRequest;
import woowacourse.shoppingcart.dto.orders.OrdersResponse;
import woowacourse.shoppingcart.dto.product.ProductResponse;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Sql("/truncate.sql")
class OrdersServiceTest {

    private final OrdersService ordersService;
    private final ProductService productService;
    private final CartItemService cartItemService;
    private final CustomerService customerService;

    public OrdersServiceTest(OrdersService ordersService, ProductService productService,
                             CartItemService cartItemService, CustomerService customerService) {
        this.ordersService = ordersService;
        this.productService = productService;
        this.cartItemService = cartItemService;
        this.customerService = customerService;
    }

    @DisplayName("orders를 저장한다.")
    @Test
    void save() {
        String username = customerService.save(MAT_SAVE_REQUEST).getUsername();
        ProductResponse savedProduct1 = productService.save(ONE_PRODUCT_SAVE_REQUEST);
        ProductResponse savedProduct2 = productService.save(TWO_PRODUCT_SAVE_REQUEST);
        CartItemResponse savedCartItem1 = cartItemService.save(
                username, new CartItemSaveRequest(savedProduct1.getId(), 2));
        CartItemResponse savedCartItem2 = cartItemService.save(
                username, new CartItemSaveRequest(savedProduct2.getId(), 3));
        OrdersDetailRequest ordersDetailRequest1 = new OrdersDetailRequest(savedCartItem1.getId(), 2);
        OrdersDetailRequest ordersDetailRequest2 = new OrdersDetailRequest(savedCartItem2.getId(), 3);

        OrdersResponse ordersResponse = ordersService.save(
                List.of(ordersDetailRequest1, ordersDetailRequest2), username);

        assertThat(ordersResponse.getOrdersDetail()).hasSize(2);
    }
}
