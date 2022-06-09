package woowacourse.shoppingcart.dto.orders;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class OrdersDetailRequest {

    @NotNull
    private final Long cartItemId;

    @Min(0)
    private final int quantity;

    public OrdersDetailRequest(final Long cartItemId, final int quantity) {
        this.cartItemId = cartItemId;
        this.quantity = quantity;
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public int getQuantity() {
        return quantity;
    }
}
