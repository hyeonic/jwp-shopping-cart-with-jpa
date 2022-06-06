package woowacourse.shoppingcart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.cartitem.CartItemResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemSaveRequest;
import woowacourse.shoppingcart.exception.NotInCustomerCartItemException;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Sql("/truncate.sql")
class CartItemServiceTest {

    private final ProductDao productDao;
    private final CartItemService cartItemService;
    private final CustomerService customerService;

    public CartItemServiceTest(ProductDao productDao, CartItemService cartItemService,
                               CustomerService customerService) {
        this.productDao = productDao;
        this.cartItemService = cartItemService;
        this.customerService = customerService;
    }

    @DisplayName("customer의 username을 기반으로 cartItems를 반환한다.")
    @Test
    void findByCustomerUsername() {
        String username = customerService.save(MAT_SAVE_REQUEST).getUsername();
        Product savedProduct = productDao.save(ONE_PRODUCT);
        cartItemService.save(username, new CartItemSaveRequest(savedProduct.getId(), 2));

        List<CartItemResponse> cartItems = cartItemService.findByCustomerUsername(username);

        assertThat(cartItems.size()).isEqualTo(1);
    }

    @DisplayName("cartItem을 저장한다.")
    @Test
    void save() {
        String username = customerService.save(MAT_SAVE_REQUEST).getUsername();
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItemResponse savedCartItem = cartItemService.save(
                username, new CartItemSaveRequest(savedProduct.getId(), 2));

        assertAll(() -> {
            assertThat(savedCartItem.getProduct().getId()).isEqualTo(savedProduct.getId());
            assertThat(savedCartItem.getQuantity()).isEqualTo(2);
        });
    }

    @DisplayName("특정 customer의 cartItem을 삭제한다.")
    @Test
    void delete() {
        String username = customerService.save(MAT_SAVE_REQUEST).getUsername();
        Product savedProduct = productDao.save(ONE_PRODUCT);
        CartItemResponse savedCartItem = cartItemService.save(
                username, new CartItemSaveRequest(savedProduct.getId(), 2));

        cartItemService.delete(username, savedCartItem.getId());

        List<CartItemResponse> cartItems = cartItemService.findByCustomerUsername(username);
        assertThat(cartItems.size()).isEqualTo(0);
    }

    @DisplayName("특정 customer에 존재하지 않는 cartItem을 삭제할 경우 예외를 던진다.")
    @Test
    void delete_error_notExistsCartItem() {
        String username = customerService.save(MAT_SAVE_REQUEST).getUsername();
        customerService.findByUsername(username);

        assertThatThrownBy(() -> cartItemService.delete(username, 0L))
                .isInstanceOf(NotInCustomerCartItemException.class);
    }
}
