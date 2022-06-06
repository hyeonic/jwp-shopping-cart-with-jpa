package woowacourse.shoppingcart.dto.orders;

import java.util.List;

public class OrdersResponse {

    private final Long id;
    private final List<OrdersDetailResponse> ordersDetail;

    public OrdersResponse(Long id, List<OrdersDetailResponse> ordersDetail) {
        this.id = id;
        this.ordersDetail = ordersDetail;
    }

    public Long getId() {
        return id;
    }

    public List<OrdersDetailResponse> getOrdersDetail() {
        return ordersDetail;
    }
}
