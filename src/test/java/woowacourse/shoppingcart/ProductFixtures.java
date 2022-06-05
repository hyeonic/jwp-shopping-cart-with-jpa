package woowacourse.shoppingcart;

import woowacourse.shoppingcart.domain.Product;

public class ProductFixtures {

    public static final String ONE_PRODUCT_NAME = "초콜렛";
    public static final int ONE_PRODUCT_PRICE = 1_000;
    public static final String ONE_PRODUCT_IMAGE_URL = "www.test.com";

    public static final Product ONE_PRODUCT = new Product(ONE_PRODUCT_NAME, ONE_PRODUCT_PRICE, ONE_PRODUCT_IMAGE_URL);

    public static final String TWO_PRODUCT_NAME = "커피";
    public static final int TWO_PRODUCT_PRICE = 1_500;
    public static final String TWO_PRODUCT_IMAGE_URL = "https://composecoffee.com/";

    public static final Product TWO_PRODUCT = new Product(TWO_PRODUCT_NAME, TWO_PRODUCT_PRICE, TWO_PRODUCT_IMAGE_URL);
}
