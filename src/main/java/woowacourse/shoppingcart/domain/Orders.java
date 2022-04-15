package woowacourse.shoppingcart.domain;

import java.util.List;

public class Orders {

    private final Long id;
    private final List<OrderDetail> orderDetails;

    public Orders(final Long id, final List<OrderDetail> orderDetails) {
        this.id = id;
        this.orderDetails = orderDetails;
    }

    public Long getId() {
        return id;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }
}
