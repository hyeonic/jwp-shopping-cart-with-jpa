package woowacourse.shoppingcart.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.cartitem.CartItemResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemSaveRequest;
import woowacourse.shoppingcart.exception.NoSuchProductException;
import woowacourse.shoppingcart.exception.NotInCustomerCartItemException;

@Service
@Transactional(readOnly = true)
public class CartItemService {

    private final CartItemDao cartItemDao;
    private final ProductDao productDao;
    private final CustomerService customerService;

    public CartItemService(CartItemDao cartItemDao, ProductDao productDao, CustomerService customerService) {
        this.cartItemDao = cartItemDao;
        this.productDao = productDao;
        this.customerService = customerService;
    }

    public List<CartItemResponse> findByCustomerId(String username) {
        Customer customer = customerService.findByUsername(username);
        return cartItemDao.findByCustomerId(customer.getId())
                .stream()
                .map(CartItemResponse::new)
                .collect(toList());
    }

    @Transactional
    public Long save(String username, CartItemSaveRequest request) {
        Customer customer = customerService.findByUsername(username);
        Product product = getProduct(request);
        return cartItemDao.save(new CartItem(customer, product, request.getQuantity())).getId();
    }

    @Transactional
    public void delete(String username, Long cartId) {
        Customer customer = customerService.findByUsername(username);

        List<Long> cartItemIds = cartItemDao.findByCustomerId(customer.getId())
                .stream()
                .map(CartItem::getId)
                .collect(toList());

        if (!cartItemIds.contains(cartId)) {
            throw new NotInCustomerCartItemException();
        }

        cartItemDao.deleteById(cartId);
    }

    private Product getProduct(CartItemSaveRequest request) {
        return productDao.findById(request.getProductId())
                .orElseThrow(NoSuchProductException::new);
    }
}
