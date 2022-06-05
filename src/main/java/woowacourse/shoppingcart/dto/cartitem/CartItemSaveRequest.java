package woowacourse.shoppingcart.dto.cartitem;

public class CartItemSaveRequest {

    private Long productId;
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
