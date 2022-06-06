package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.CustomerFixtures.MAT;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.OrdersDetail;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrdersDetailDaoTest {

    private final OrdersDao ordersDao;
    private final ProductDao productDao;
    private final CustomerDao customerDao;
    private final OrdersDetailDao ordersDetailDao;

    public OrdersDetailDaoTest(JdbcTemplate jdbcTemplate) {
        this.customerDao = new CustomerDao(jdbcTemplate);
        this.productDao = new ProductDao(jdbcTemplate);
        this.ordersDao = new OrdersDao(jdbcTemplate);
        this.ordersDetailDao = new OrdersDetailDao(jdbcTemplate);
    }

    @DisplayName("OrderDatail을 추가한다.")
    @Test
    void save() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct = productDao.save(ONE_PRODUCT);
        Orders savedOrders = ordersDao.save(new Orders(savedCustomer));

        OrdersDetail savedOrdersDetail = ordersDetailDao.save(new OrdersDetail(savedOrders, savedProduct, 2));

        assertAll(() -> {
            assertThat(savedOrdersDetail.getOrders()).isEqualTo(savedOrders);
            assertThat(savedOrdersDetail.getProduct()).isEqualTo(savedProduct);
        });
    }

    @DisplayName("OrderId로 OrderDetails 조회하는 기능")
    @Test
    void findOrdersDetailsByOrderId() {
        Customer savedCustomer = customerDao.save(MAT);
        Product savedProduct1 = productDao.save(ONE_PRODUCT);
        Product savedProduct2 = productDao.save(ONE_PRODUCT);
        Orders savedOrders = ordersDao.save(new Orders(savedCustomer));
        ordersDetailDao.save(new OrdersDetail(savedOrders, savedProduct1, 2));
        ordersDetailDao.save(new OrdersDetail(savedOrders, savedProduct2, 1));

        List<OrdersDetail> ordersDetails = ordersDetailDao.findByOrdersId(savedOrders.getId());

        assertThat(ordersDetails.size()).isEqualTo(2);
    }
}
