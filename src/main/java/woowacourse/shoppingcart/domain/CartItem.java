package woowacourse.shoppingcart.domain;

import java.util.Objects;
import woowacourse.shoppingcart.domain.customer.Customer;

public class CartItem {

    private final Long id;
    private final Customer customer;
    private final Product product;
    private final int quantity;

    public CartItem(Long id, CartItem cartItem) {
        this(id, cartItem.customer, cartItem.product, cartItem.quantity);
    }

    public CartItem(Customer customer, Product product, int quantity) {
        this(null, customer, product, quantity);
    }

    public CartItem(Long id, Customer customer, Product product, int quantity) {
        this.id = id;
        this.customer = customer;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
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
        CartItem cartItem = (CartItem) o;
        return Objects.equals(id, cartItem.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
