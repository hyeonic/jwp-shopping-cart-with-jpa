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
import woowacourse.shoppingcart.dto.cartitem.CartItemUpdateRequest;
import woowacourse.shoppingcart.exception.NoSuchCartItemException;
import woowacourse.shoppingcart.exception.NoSuchProductException;
import woowacourse.shoppingcart.exception.NotInCustomerCartItemException;

@Service
@Transactional(readOnly = true)
public class CartItemService {

    private final ProductDao productDao;
    private final CartItemDao cartItemDao;
    private final CustomerService customerService;

    public CartItemService(ProductDao productDao, CartItemDao cartItemDao, CustomerService customerService) {
        this.productDao = productDao;
        this.cartItemDao = cartItemDao;
        this.customerService = customerService;
    }

    public List<CartItemResponse> findByCustomerUsername(String username) {
        Customer customer = customerService.findByUsername(username);
        return cartItemDao.findByCustomerId(customer.getId())
                .stream()
                .map(CartItemResponse::new)
                .collect(toList());
    }

    @Transactional
    public CartItemResponse save(String username, CartItemSaveRequest request) {
        Customer customer = customerService.findByUsername(username);
        Product product = getProduct(request.getProductId());
        CartItem cartItem = cartItemDao.save(new CartItem(customer, product, request.getQuantity()));
        return new CartItemResponse(cartItem);
    }

    public CartItemResponse findById(Long id) {
        CartItem cartItem = cartItemDao.findById(id)
                .orElseThrow(NoSuchCartItemException::new);
        return new CartItemResponse(cartItem);
    }

    @Transactional
    public void update(Long cartItemId, CartItemUpdateRequest request) {
        cartItemDao.update(cartItemId, request.getQuantity());
    }

    @Transactional
    public void delete(String username, Long cartId) {
        Customer customer = customerService.findByUsername(username);
        if (!cartItemDao.existsByIdAndCustomerId(cartId, customer.getId())) {
            throw new NotInCustomerCartItemException();
        }

        cartItemDao.deleteById(cartId);
    }

    private Product getProduct(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(NoSuchProductException::new);
    }
}
