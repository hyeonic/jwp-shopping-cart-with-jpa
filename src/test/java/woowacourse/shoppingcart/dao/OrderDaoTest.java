package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.CustomerFixtures.MAT;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class OrderDaoTest {

    private final OrderDao orderDao;
    private final CustomerDao customerDao;

    public OrderDaoTest(JdbcTemplate jdbcTemplate) {
        this.orderDao = new OrderDao(jdbcTemplate);
        this.customerDao = new CustomerDao(jdbcTemplate);
    }

    @DisplayName("Order를 추가하는 기능")
    @Test
    void save() {
        Customer savedCustomer = customerDao.save(MAT);

        Orders savedOrders = orderDao.save(new Orders(savedCustomer));

        assertThat(savedOrders.getCustomer()).isEqualTo(savedCustomer);
    }

    @DisplayName("id를 활용하여 order를 조회한다.")
    @Test
    void findById() {
        Customer savedCustomer = customerDao.save(MAT);
        Orders savedOrders = orderDao.save(new Orders(savedCustomer));

        Orders foundOrders = orderDao.findById(savedOrders.getId()).get();

        assertAll(() -> {
            assertThat(foundOrders).isEqualTo(savedOrders);
            assertThat(foundOrders.getCustomer()).isEqualTo(savedCustomer);
        });
    }

    @DisplayName("존재하지 않는 id의 order인 경우 비어있다.")
    @Test
    void findById_error_notExistsOrders() {
        Optional<Orders> orders = orderDao.findById(0L);

        assertThat(orders).isEmpty();
    }

    @DisplayName("CustomerId 집합을 이용하여 OrderId 집합을 얻는 기능")
    @Test
    void findByCustomerId() {
        Customer savedCustomer = customerDao.save(MAT);
        orderDao.save(new Orders(savedCustomer));
        orderDao.save(new Orders(savedCustomer));

        List<Orders> orders = orderDao.findByCustomerId(savedCustomer.getId());

        assertThat(orders).hasSize(2);
    }

    @DisplayName("특정 customer가 특정 order를 가지고 있는지 확인한다.")
    @Test
    void existsByIdAndCustomerId() {
        Customer savedCustomer = customerDao.save(MAT);
        Orders savedOrders = orderDao.save(new Orders(savedCustomer));

        boolean result = orderDao.existsByIdAndCustomerId(savedOrders.getId(), savedCustomer.getId());

        assertThat(result).isTrue();
    }
}
