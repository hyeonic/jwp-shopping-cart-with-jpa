package woowacourse.shoppingcart.dto.cartitem;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CartItemSaveRequest {

    @NotNull
    private Long productId;

    @Min(value = 0, message = "수량은 0미만이 될 수 없습니다.")
    private int quantity;

    private CartItemSaveRequest() {
    }

    public CartItemSaveRequest(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
