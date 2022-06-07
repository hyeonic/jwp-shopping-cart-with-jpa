package woowacourse.shoppingcart.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static woowacourse.shoppingcart.CustomerFixtures.MAT;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_EMAIL;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_USERNAME;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import woowacourse.shoppingcart.domain.customer.Customer;

@JdbcTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
public class CustomerDaoTest {

    private final CustomerDao customerDao;

    public CustomerDaoTest(JdbcTemplate jdbcTemplate) {
        customerDao = new CustomerDao(jdbcTemplate);
    }

    @DisplayName("customer를 생성한다.")
    @Test
    void save() {
        Customer customer = MAT;

        Customer savedCustomer = customerDao.save(customer);

        assertAll(() -> {
            assertThat(savedCustomer.getId()).isNotNull();
            assertThat(savedCustomer.getUsername()).isEqualTo(MAT_USERNAME);
            assertThat(savedCustomer.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(savedCustomer.getPassword()).isEqualTo(MAT_PASSWORD);
            assertThat(savedCustomer.getAddress()).isEqualTo(MAT_ADDRESS);
            assertThat(savedCustomer.getPhoneNumber()).isEqualTo(MAT_PHONE_NUMBER);
        });
    }

    @DisplayName("id를 활용하여 customer를 조회한다.")
    @Test
    void findById() {
        Customer savedCustomer = customerDao.save(MAT);

        Customer foundCustomer = customerDao.findById(savedCustomer.getId()).get();

        assertAll(() -> {
            assertThat(foundCustomer.getId()).isEqualTo(savedCustomer.getId());
            assertThat(foundCustomer.getUsername()).isEqualTo(savedCustomer.getUsername());
            assertThat(foundCustomer.getEmail()).isEqualTo(savedCustomer.getEmail());
            assertThat(foundCustomer.getPassword()).isEqualTo(savedCustomer.getPassword());
            assertThat(foundCustomer.getAddress()).isEqualTo(savedCustomer.getAddress());
            assertThat(foundCustomer.getPhoneNumber()).isEqualTo(savedCustomer.getPhoneNumber());
        });
    }

    @DisplayName("username을 이용해 customer를 조회한다.")
    @Test
    void findByUsername() {
        Customer customer = YAHO;
        customerDao.save(customer);

        Optional<Customer> foundCustomer = customerDao.findByUsername(YAHO_USERNAME);

        assertThat(foundCustomer).isNotEmpty();
    }

    @DisplayName("username과 password를 기반으로 customer의 존재 여부를 반환한다.")
    @Test
    void existsCustomer() {
        customerDao.save(MAT);

        boolean result = customerDao.existsByUsernameAndPassword(MAT_USERNAME, MAT_PASSWORD);

        assertThat(result).isTrue();
    }

    @DisplayName("존재하지 않는 username이거나 password인 경우 false를 반환한다.")
    @Test
    void existsCustomer_notExists() {
        boolean result = customerDao.existsByUsernameAndPassword(MAT_USERNAME, MAT_PASSWORD);

        assertThat(result).isFalse();
    }

    @DisplayName("존재하는 username인 경우 true를 반환한다.")
    @Test
    void existsByUsername() {
        customerDao.save(MAT);

        boolean result = customerDao.existsByUsername(MAT_USERNAME);

        assertThat(result).isTrue();
    }

    @DisplayName("존재하지 않는 username인 경우 false를 반환한다.")
    @Test
    void existsByUsername_notExists() {
        boolean result = customerDao.existsByUsername(MAT_USERNAME);

        assertThat(result).isFalse();
    }

    @DisplayName("존재하는 email인 경우 true를 반환한다.")
    @Test
    void existsByEmail() {
        customerDao.save(MAT);

        boolean result = customerDao.existsByEmail(MAT_EMAIL);

        assertThat(result).isTrue();
    }

    @DisplayName("존재하지 않는 email인 경우 false를 반환한다.")
    @Test
    void existsByEmail_notExists() {
        boolean result = customerDao.existsByEmail(MAT_EMAIL);

        assertThat(result).isFalse();
    }

    @DisplayName("customer 정보를 수정한다.")
    @Test
    void update() {
        Customer savedCustomer = customerDao.save(MAT);

        savedCustomer.modify(UPDATE_ADDRESS, UPDATE_PHONE_NUMBER);

        assertDoesNotThrow(() -> customerDao.update(savedCustomer));
    }

    @DisplayName("customer 정보를 삭제한다.")
    @Test
    void delete() {
        Customer savedCustomer = customerDao.save(YAHO);

        assertDoesNotThrow(() -> customerDao.delete(savedCustomer));
    }
}
