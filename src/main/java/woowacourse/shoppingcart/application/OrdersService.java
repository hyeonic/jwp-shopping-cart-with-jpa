package woowacourse.shoppingcart.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CartItemDao;
import woowacourse.shoppingcart.dao.OrdersDao;
import woowacourse.shoppingcart.dao.OrdersDetailDao;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.OrdersDetail;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.orders.OrdersDetailRequest;
import woowacourse.shoppingcart.dto.orders.OrdersDetailResponse;
import woowacourse.shoppingcart.dto.orders.OrdersResponse;
import woowacourse.shoppingcart.exception.NoSuchCartItemException;
import woowacourse.shoppingcart.exception.NoSuchOrdersException;

@Service
@Transactional(readOnly = true)
public class OrdersService {

    private final OrdersDao ordersDao;
    private final CartItemDao cartItemDao;
    private final OrdersDetailDao ordersDetailDao;
    private final CustomerService customerService;

    public OrdersService(OrdersDao ordersDao, CartItemDao cartItemDao, OrdersDetailDao ordersDetailDao,
                         CustomerService customerService) {
        this.ordersDao = ordersDao;
        this.cartItemDao = cartItemDao;
        this.ordersDetailDao = ordersDetailDao;
        this.customerService = customerService;
    }

    @Transactional
    public OrdersResponse save(List<OrdersDetailRequest> orderDetailRequests, String username) {
        Customer customer = customerService.findByUsername(username);
        Orders orders = ordersDao.save(new Orders(customer));

        for (OrdersDetailRequest orderDetail : orderDetailRequests) {
            CartItem cartItem = getCartItem(orderDetail.getCartItemId());

            ordersDetailDao.save(new OrdersDetail(orders, cartItem.getProduct(), orderDetail.getQuantity()));
            cartItemDao.deleteById(cartItem.getId());
        }

        List<OrdersDetailResponse> ordersDetails = ordersDetailDao.findByOrdersId(orders.getId())
                .stream()
                .map(OrdersDetailResponse::new)
                .collect(toList());

        return new OrdersResponse(orders.getId(), ordersDetails);
    }

    private CartItem getCartItem(Long cartId) {
        return cartItemDao.findById(cartId)
                .orElseThrow(NoSuchCartItemException::new);
    }

    public OrdersResponse findById(Long id) {
        Orders orders = getOrders(id);
        List<OrdersDetailResponse> ordersDetails = ordersDetailDao.findByOrdersId(orders.getId())
                .stream()
                .map(OrdersDetailResponse::new)
                .collect(toList());

        return new OrdersResponse(orders.getId(), ordersDetails);
    }

    public List<OrdersResponse> findByCustomerUsername(String username) {
        Customer customer = customerService.findByUsername(username);
        return ordersDao.findByCustomerId(customer.getId())
                .stream()
                .map(this::getOrdersResponse)
                .collect(toList());
    }

    private Orders getOrders(Long id) {
        return ordersDao.findById(id)
                .orElseThrow(NoSuchOrdersException::new);
    }

    private OrdersResponse getOrdersResponse(Orders orders) {
        List<OrdersDetailResponse> ordersDetails = ordersDetailDao.findByOrdersId(orders.getId())
                .stream()
                .map(OrdersDetailResponse::new)
                .collect(toList());

        return new OrdersResponse(orders.getId(), ordersDetails);
    }
}
