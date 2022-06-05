package woowacourse.shoppingcart;

import static woowacourse.shoppingcart.CustomerFixtures.*;
import static woowacourse.shoppingcart.ProductFixtures.*;

import woowacourse.shoppingcart.domain.CartItem;

public class CartItemFixtures {

    public static final CartItem CART_ITEM_1 = new CartItem(MAT, ONE_PRODUCT, 10);
    public static final CartItem CART_ITEM_2 = new CartItem(MAT, TWO_PRODUCT, 15);
}
