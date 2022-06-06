package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.CustomerFixtures.MAT;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CartItemDaoTest {

    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final CartItemDao cartItemDao;

    public CartItemDaoTest(JdbcTemplate jdbcTemplate) {
        productDao = new ProductDao(jdbcTemplate);
        customerDao = new CustomerDao(jdbcTemplate);
        cartItemDao = new CartItemDao(jdbcTemplate);
    }

    @DisplayName("카트에 아이템을 담으면, 담긴 카트 아이템을 반환한다.")
    @Test
    void save() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItem cartItem = new CartItem(savedCustomer, savedProduct, 10);

        CartItem savedCartItem = cartItemDao.save(cartItem);

        assertAll(() -> {
            assertThat(savedCartItem.getCustomer()).isEqualTo(savedCustomer);
            assertThat(savedCartItem.getProduct()).isEqualTo(savedProduct);
        });
    }

    @DisplayName("중복된 물품을 담으면, 기존 수량에 더해서 담긴 카트 아이템을 반환한다.")
    @Test
    void save_duplicated() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItem cartItem = new CartItem(savedCustomer, savedProduct, 10);

        cartItemDao.save(cartItem);
        CartItem savedCartItem = cartItemDao.save(cartItem);

        assertAll(() -> {
            assertThat(savedCartItem.getCustomer()).isEqualTo(savedCustomer);
            assertThat(savedCartItem.getProduct()).isEqualTo(savedProduct);
            assertThat(savedCartItem.getQuantity()).isEqualTo(20);
        });
    }

    @DisplayName("customer의 id를 가진 cartItem 리스트를 반환한다.")
    @Test
    void findByCustomerId() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct1 = productDao.save(ONE_PRODUCT);
        Product savedProduct2 = productDao.save(ONE_PRODUCT);
        cartItemDao.save(new CartItem(savedCustomer, savedProduct1, 10));
        cartItemDao.save(new CartItem(savedCustomer, savedProduct2, 12));

        List<CartItem> cartItems = cartItemDao.findByCustomerId(savedCustomer.getId());

        assertThat(cartItems.size()).isEqualTo(2);
    }

    @DisplayName("cartItem의 id로 조회하면 해당하는 cartItem을 반환한다.")
    @Test
    void findById() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItem cartItem = new CartItem(savedCustomer, savedProduct, 15);
        CartItem savedCartItem = cartItemDao.save(cartItem);

        CartItem foundCartItem = cartItemDao.findById(savedCartItem.getId()).get();

        assertAll(() -> {
            assertThat(foundCartItem.getCustomer()).isEqualTo(savedCustomer);
            assertThat(foundCartItem.getProduct()).isEqualTo(savedProduct);
        });
    }
    
    @DisplayName("customer id와 cart id를 기반으로 cart item의 존재 유무를 확인한다.")
    @Test
    void existsByIdAndCustomerId() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItem cartItem = new CartItem(savedCustomer, savedProduct, 15);
        CartItem savedCartItem = cartItemDao.save(cartItem);

        boolean result = cartItemDao.existsByIdAndCustomerId(savedCartItem.getId(), savedCustomer.getId());

        assertThat(result).isTrue();
    }

    @DisplayName("customer id와 cart id를 기반으로 cart item의 존재 유무를 확인한다.")
    @Test
    void existsByIdAndCustomerId_error_notExists() {
        Customer savedCustomer1 = customerDao.save(MAT);
        Customer savedCustomer2 = customerDao.save(YAHO);
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItem cartItem = new CartItem(savedCustomer1, savedProduct, 15);
        CartItem savedCartItem = cartItemDao.save(cartItem);

        boolean result = cartItemDao.existsByIdAndCustomerId(savedCartItem.getId(), savedCustomer2.getId());

        assertThat(result).isFalse();
    }

    @DisplayName("cart item을 삭제한다.")
    @Test
    void deleteById() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItem cartItem = new CartItem(savedCustomer, savedProduct, 15);
        CartItem savedCartItem = cartItemDao.save(cartItem);

        cartItemDao.deleteById(savedCartItem.getId());

        List<CartItem> cartItems = cartItemDao.findByCustomerId(savedCustomer.getId());
        assertThat(cartItems.size()).isEqualTo(0);
    }
}
