package woowacourse.shoppingcart.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.dao.OrdersDao;
import woowacourse.shoppingcart.dao.OrdersDetailDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.OrdersDetail;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.OrderRequest;
import woowacourse.shoppingcart.exception.InvalidOrderException;
import woowacourse.shoppingcart.exception.NoSuchCartItemException;
import woowacourse.shoppingcart.exception.NoSuchCustomerException;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

    private final OrdersDao ordersDao;
    private final OrdersDetailDao ordersDetailDao;
    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public OrderService(final OrdersDao ordersDao, final OrdersDetailDao ordersDetailDao, final CartItemDao cartItemDao,
                        final CustomerDao customerDao, final ProductDao productDao) {
        this.ordersDao = ordersDao;
        this.ordersDetailDao = ordersDetailDao;
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    public Long addOrder(final List<OrderRequest> orderDetailRequests, final String customerName) {
        Orders orders = ordersDao.save(new Orders(getCustomer(customerName)));

        for (final OrderRequest orderDetail : orderDetailRequests) {
            final Long cartId = orderDetail.getCartId();
            final CartItem cartItem = getCartItem(cartId);
            final int quantity = orderDetail.getQuantity();

            ordersDetailDao.save(new OrdersDetail(orders, cartItem.getProduct(), quantity));
            cartItemDao.deleteById(cartId);
        }

        return orders.getId();
    }

    private CartItem getCartItem(Long cartId) {
        return cartItemDao.findById(cartId).orElseThrow(NoSuchCartItemException::new);
    }

    public Orders findOrderById(final String customerName, final Long orderId) {
        validateOrderIdByCustomerName(customerName, orderId);
        return null;
    }

    private void validateOrderIdByCustomerName(final String customerName, final Long orderId) {
        final Long customerId = getCustomer(customerName).getId();

        if (!ordersDao.existsByIdAndCustomerId(customerId, orderId)) {
            throw new InvalidOrderException("유저에게는 해당 order_id가 없습니다.");
        }
    }

    public List<Orders> findOrdersByCustomerName(final String customerName) {
        final Long customerId = getCustomer(customerName).getId();
        final List<Long> orderIds = ordersDao.findByCustomerId(customerId)
                .stream()
                .map(Orders::getId)
                .collect(toList());

        return null;
    }

    private Customer getCustomer(String customerName) {
        return customerDao.findByUsername(customerName).orElseThrow(NoSuchCustomerException::new);
    }
}
