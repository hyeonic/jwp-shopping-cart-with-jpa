package woowacourse.shoppingcart.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.dao.OrderDao;
import woowacourse.shoppingcart.dao.OrdersDetailDao;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.OrderRequest;
import woowacourse.shoppingcart.exception.InvalidOrderException;
import woowacourse.shoppingcart.exception.NoSuchCartItemException;
import woowacourse.shoppingcart.exception.NoSuchCustomerException;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

    private final OrderDao orderDao;
    private final OrdersDetailDao ordersDetailDao;
    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public OrderService(final OrderDao orderDao, final OrdersDetailDao ordersDetailDao, final CartItemDao cartItemDao,
                        final CustomerDao customerDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.ordersDetailDao = ordersDetailDao;
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    public Long addOrder(final List<OrderRequest> orderDetailRequests, final String customerName) {
        Orders orders = orderDao.save(new Orders(getCustomer(customerName)));

        for (final OrderRequest orderDetail : orderDetailRequests) {
            final Long cartId = orderDetail.getCartId();
            final CartItem cartItem = getCartItem(cartId);
            final int quantity = orderDetail.getQuantity();

            ordersDetailDao.addOrdersDetail(orders.getId(), cartItem.getProduct().getId(), quantity);
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

        if (!orderDao.existsByIdAndCustomerId(customerId, orderId)) {
            throw new InvalidOrderException("유저에게는 해당 order_id가 없습니다.");
        }
    }

    public List<Orders> findOrdersByCustomerName(final String customerName) {
        final Long customerId = getCustomer(customerName).getId();
        final List<Long> orderIds = orderDao.findByCustomerId(customerId)
                .stream()
                .map(Orders::getId)
                .collect(toList());

        return null;
    }

    private Customer getCustomer(String customerName) {
        return customerDao.findByUsername(customerName).orElseThrow(NoSuchCustomerException::new);
    }
}
