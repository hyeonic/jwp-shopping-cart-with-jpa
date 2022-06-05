package woowacourse.shoppingcart.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.cartitem.CartItemResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemSaveRequest;
import woowacourse.shoppingcart.exception.NoSuchCustomerException;
import woowacourse.shoppingcart.exception.NoSuchProductException;
import woowacourse.shoppingcart.exception.NotInCustomerCartItemException;

@Service
@Transactional(readOnly = true)
public class CartItemService {

    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public CartItemService(CartItemDao cartItemDao, CustomerDao customerDao, ProductDao productDao) {
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    public List<CartItemResponse> findByCustomerId(final String customerName) {
        final Long customerId = customerDao.findIdByUsername(customerName);
        return cartItemDao.findByCustomerId(customerId)
                .stream()
                .map(CartItemResponse::new)
                .collect(toList());
    }

    @Transactional
    public Long save(String username, CartItemSaveRequest request) {
        Customer customer = customerDao.findByUsername(username)
                .orElseThrow(NoSuchCustomerException::new);

        Product product = productDao.findById(request.getProductId())
                .orElseThrow(NoSuchProductException::new);

        return cartItemDao.save(new CartItem(customer, product, request.getQuantity())).getId();
    }

    @Transactional
    public void delete(final String username, final Long cartId) {
        Customer customer = customerDao.findByUsername(username)
                .orElseThrow(NoSuchCustomerException::new);

        List<Long> cartItemIds = cartItemDao.findByCustomerId(customer.getId())
                .stream()
                .map(CartItem::getId)
                .collect(toList());

        if (!cartItemIds.contains(cartId)) {
            throw new NotInCustomerCartItemException();
        }

        cartItemDao.deleteById(cartId);
    }
}
