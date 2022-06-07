package woowacourse.shoppingcart;

import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.product.ProductSaveRequest;

public class ProductFixtures {

    public static final String ONE_PRODUCT_NAME = "초콜렛";
    public static final int ONE_PRODUCT_PRICE = 1_000;
    public static final String ONE_PRODUCT_IMAGE_URL = "www.test.com";
    private static final String ONE_DESCRIPTION = "첫 번째 상품";

    public static final Product ONE_PRODUCT = new Product(
            ONE_PRODUCT_NAME, ONE_PRODUCT_PRICE, ONE_PRODUCT_IMAGE_URL, ONE_DESCRIPTION);
    public static final ProductSaveRequest ONE_PRODUCT_SAVE_REQUEST = new ProductSaveRequest(
            ONE_PRODUCT_NAME, ONE_PRODUCT_PRICE, ONE_PRODUCT_IMAGE_URL, ONE_DESCRIPTION);

    public static final String TWO_PRODUCT_NAME = "커피";
    public static final int TWO_PRODUCT_PRICE = 1_500;
    public static final String TWO_PRODUCT_IMAGE_URL = "https://composecoffee.com/";
    private static final String TWO_DESCRIPTION = "두 번째 상품";

    public static final Product TWO_PRODUCT = new Product(
            TWO_PRODUCT_NAME, TWO_PRODUCT_PRICE, TWO_PRODUCT_IMAGE_URL, TWO_DESCRIPTION);
    public static final ProductSaveRequest TWO_PRODUCT_SAVE_REQUEST = new ProductSaveRequest(
            TWO_PRODUCT_NAME, TWO_PRODUCT_PRICE, TWO_PRODUCT_IMAGE_URL, TWO_DESCRIPTION);
}
