package woowacourse.shoppingcart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_IMAGE_URL;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_NAME;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_PRICE;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_SAVE_REQUEST;
import static woowacourse.shoppingcart.ProductFixtures.TWO_PRODUCT_SAVE_REQUEST;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.dto.product.ProductResponse;
import woowacourse.shoppingcart.exception.NoSuchProductException;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Sql("/truncate.sql")
class ProductServiceTest {

    private final ProductService productService;

    public ProductServiceTest(ProductService productService) {
        this.productService = productService;
    }

    @DisplayName("Product를 전체 조회한다.")
    @Test
    void findAll() {
        productService.save(ONE_PRODUCT_SAVE_REQUEST);
        productService.save(TWO_PRODUCT_SAVE_REQUEST);

        List<ProductResponse> products = productService.findAll();

        assertThat(products.size()).isEqualTo(2);
    }

    @DisplayName("ProductRequest를 저장하면 ProductResponse를 반환한다.")
    @Test
    void save() {
        ProductResponse response = productService.save(ONE_PRODUCT_SAVE_REQUEST);

        assertAll(() -> {
                    assertThat(response.getName()).isEqualTo(ONE_PRODUCT_NAME);
                    assertThat(response.getPrice()).isEqualTo(ONE_PRODUCT_PRICE);
                    assertThat(response.getImageUrl()).isEqualTo(ONE_PRODUCT_IMAGE_URL);
                }
        );
    }

    @DisplayName("productId의 상품을 찾으면 productResponse를 반환한다.")
    @Test
    void findById() {
        ProductResponse savedResponse = productService.save(ONE_PRODUCT_SAVE_REQUEST);

        ProductResponse foundResponse = productService.findById(savedResponse.getId());

        assertAll(() -> {
                    assertThat(foundResponse.getName()).isEqualTo(savedResponse.getName());
                    assertThat(foundResponse.getPrice()).isEqualTo(savedResponse.getPrice());
                    assertThat(foundResponse.getImageUrl()).isEqualTo(savedResponse.getImageUrl());
                }
        );
    }

    @DisplayName("존재하지 않는 product id로 조회할 경우 예외를 던진다.")
    @Test
    void findById_error_noSuchId() {
        assertThatThrownBy(() -> productService.findById(0L))
                .isInstanceOf(NoSuchProductException.class);
    }

    @DisplayName("product id를 기반으로 product를 삭제한다.")
    @Test
    void deleteById() {
        ProductResponse savedResponse = productService.save(ONE_PRODUCT_SAVE_REQUEST);

        productService.deleteById(savedResponse.getId());

        productService.findAll();
    }
}
