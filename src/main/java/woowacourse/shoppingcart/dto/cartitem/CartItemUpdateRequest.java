package woowacourse.shoppingcart.dto.cartitem;

import javax.validation.constraints.Min;

public class CartItemUpdateRequest {

    @Min(value = 0, message = "수량은 0미만이 될 수 없습니다.")
    private int quantity;

    private CartItemUpdateRequest() {
    }

    public CartItemUpdateRequest(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }
}
