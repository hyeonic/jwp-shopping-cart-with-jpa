package woowacourse.shoppingcart.domain;

import java.util.Objects;
import woowacourse.shoppingcart.domain.customer.Customer;

public class Orders {

    private final Long id;
    private final Customer customer;

    public Orders(Customer customer) {
        this(null, customer);
    }

    public Orders(Long id, Orders orders) {
        this(id, orders.getCustomer());
    }

    public Orders(Long id, Customer customer) {
        this.id = id;
        this.customer = customer;
    }

    public Long getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orders orders = (Orders) o;
        return Objects.equals(id, orders.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
