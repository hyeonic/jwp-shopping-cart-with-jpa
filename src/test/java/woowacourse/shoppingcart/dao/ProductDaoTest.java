package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_IMAGE_URL;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_NAME;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_PRICE;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.Product;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Sql("/truncate.sql")
public class ProductDaoTest {

    private final ProductDao productDao;

    public ProductDaoTest(JdbcTemplate jdbcTemplate) {
        this.productDao = new ProductDao(jdbcTemplate);
    }

    @DisplayName("Product를 저장하면 product를 반환한다.")
    @Test
    void save() {
        Product product = productDao.save(ONE_PRODUCT);

        assertAll(() -> {
                    assertThat(product.getName()).isEqualTo(ONE_PRODUCT_NAME);
                    assertThat(product.getPrice()).isEqualTo(ONE_PRODUCT_PRICE);
                    assertThat(product.getImageUrl()).isEqualTo(ONE_PRODUCT_IMAGE_URL);
                    assertThat(product.isDeleted()).isFalse();
                }
        );
    }

    @DisplayName("productId의 상품을 찾으면 product를 반환한다.")
    @Test
    void findProductById() {
        Product product = productDao.save(ONE_PRODUCT);

        assertAll(() -> {
                    assertThat(product.getName()).isEqualTo(ONE_PRODUCT_NAME);
                    assertThat(product.getPrice()).isEqualTo(ONE_PRODUCT_PRICE);
                    assertThat(product.getImageUrl()).isEqualTo(ONE_PRODUCT_IMAGE_URL);
                    assertThat(product.isDeleted()).isFalse();
                }
        );
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void getProducts() {
        List<Product> products = productDao.findAll();

        assertThat(products.size()).isEqualTo(0);
    }

    @DisplayName("싱품을 삭제한다.")
    @Test
    void deleteProduct() {
        Product product = productDao.save(ONE_PRODUCT);
        int beforeSize = productDao.findAll().size();

        productDao.delete(product);

        int afterSize = productDao.findAll().size();
        assertThat(beforeSize - 1).isEqualTo(afterSize);
    }
}
