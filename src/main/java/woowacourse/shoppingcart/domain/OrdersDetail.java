package woowacourse.shoppingcart.domain;

import java.util.Objects;

public class OrdersDetail {

    private final Long id;
    private final Orders orders;
    private final Product product;
    private final int quantity;

    public OrdersDetail(Long id, OrdersDetail ordersDetail) {
        this(id, ordersDetail.orders, ordersDetail.product, ordersDetail.quantity);
    }

    public OrdersDetail(Orders orders, Product product, int quantity) {
        this(null, orders, product, quantity);
    }

    public OrdersDetail(Long id, Orders orders, Product product, int quantity) {
        this.id = id;
        this.orders = orders;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Orders getOrders() {
        return orders;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrdersDetail that = (OrdersDetail) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
