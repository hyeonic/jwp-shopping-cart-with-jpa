package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.*;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.OrderDetail;
import woowacourse.shoppingcart.dto.OrderRequest;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.exception.InvalidOrderException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import woowacourse.shoppingcart.exception.NoSuchCartItemException;
import woowacourse.shoppingcart.exception.NoSuchProductException;

@Service
@Transactional(rollbackFor = Exception.class)
public class OrderService {

    private final OrderDao orderDao;
    private final OrdersDetailDao ordersDetailDao;
    private final CartItemDao cartItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;

    public OrderService(final OrderDao orderDao, final OrdersDetailDao ordersDetailDao,
                        final CartItemDao cartItemDao, final CustomerDao customerDao, final ProductDao productDao) {
        this.orderDao = orderDao;
        this.ordersDetailDao = ordersDetailDao;
        this.cartItemDao = cartItemDao;
        this.customerDao = customerDao;
        this.productDao = productDao;
    }

    public Long addOrder(final List<OrderRequest> orderDetailRequests, final String customerName) {
        final Long customerId = customerDao.findIdByUsername(customerName);
        final Long ordersId = orderDao.addOrders(customerId);

        for (final OrderRequest orderDetail : orderDetailRequests) {
            final Long cartId = orderDetail.getCartId();
            final CartItem cartItem = getCartItem(cartId);
            final int quantity = orderDetail.getQuantity();

            ordersDetailDao.addOrdersDetail(ordersId, cartItem.getProduct().getId(), quantity);
            cartItemDao.deleteById(cartId);
        }

        return ordersId;
    }

    private CartItem getCartItem(Long cartId) {
        return cartItemDao.findById(cartId)
                .orElseThrow(NoSuchCartItemException::new);
    }

    public Orders findOrderById(final String customerName, final Long orderId) {
        validateOrderIdByCustomerName(customerName, orderId);
        return findOrderResponseDtoByOrderId(orderId);
    }

    private void validateOrderIdByCustomerName(final String customerName, final Long orderId) {
        final Long customerId = customerDao.findIdByUsername(customerName);

        if (!orderDao.isValidOrderId(customerId, orderId)) {
            throw new InvalidOrderException("유저에게는 해당 order_id가 없습니다.");
        }
    }

    public List<Orders> findOrdersByCustomerName(final String customerName) {
        final Long customerId = customerDao.findIdByUsername(customerName);
        final List<Long> orderIds = orderDao.findOrderIdsByCustomerId(customerId);

        return orderIds.stream()
                .map(orderId -> findOrderResponseDtoByOrderId(orderId))
                .collect(Collectors.toList());
    }

    private Orders findOrderResponseDtoByOrderId(final Long orderId) {
        final List<OrderDetail> ordersDetails = new ArrayList<>();
        for (final OrderDetail productQuantity : ordersDetailDao.findOrdersDetailsByOrderId(orderId)) {
            final Product product = productDao.findById(productQuantity.getProductId())
                    .orElseThrow(NoSuchProductException::new);
            final int quantity = productQuantity.getQuantity();
            ordersDetails.add(new OrderDetail(product, quantity));
        }

        return new Orders(orderId, ordersDetails);
    }
}
