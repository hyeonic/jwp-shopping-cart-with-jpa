package woowacourse.shoppingcart.dto.orders;

import woowacourse.shoppingcart.domain.OrdersDetail;
import woowacourse.shoppingcart.dto.product.ProductResponse;

public class OrdersDetailResponse {

    private final Long id;
    private final ProductResponse product;
    private final int quantity;

    public OrdersDetailResponse(OrdersDetail ordersDetail) {
        this.id = ordersDetail.getId();
        this.product = new ProductResponse(ordersDetail.getProduct());
        this.quantity = ordersDetail.getQuantity();
    }

    public Long getId() {
        return id;
    }

    public ProductResponse getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
