package woowacourse.shoppingcart.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_EMAIL;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_EMAIL;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_USERNAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.test.context.jdbc.Sql;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.customer.LoginCustomer;
import woowacourse.shoppingcart.exception.InvalidCustomerException;
import woowacourse.shoppingcart.exception.NoSuchCustomerException;

@SpringBootTest
@TestConstructor(autowireMode = AutowireMode.ALL)
@Sql("/truncate.sql")
class CustomerServiceTest {

    private final CustomerService customerService;

    public CustomerServiceTest(CustomerService customerService) {
        this.customerService = customerService;
    }

    @DisplayName("customer??? ????????????.")
    @Test
    void save() {
        CustomerSaveRequest request = MAT_SAVE_REQUEST;

        CustomerResponse response = customerService.save(request);

        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getUsername()).isEqualTo(MAT_USERNAME);
            assertThat(response.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(response.getAddress()).isEqualTo(MAT_ADDRESS);
            assertThat(response.getPhoneNumber()).isEqualTo(MAT_PHONE_NUMBER);
        });
    }

    @DisplayName("customer??? username??? ???????????? ????????????.")
    @Test
    void find() {
        CustomerSaveRequest request = YAHO_SAVE_REQUEST;
        customerService.save(YAHO_SAVE_REQUEST);

        CustomerResponse response = customerService.find(new LoginCustomer(request.getUsername()));

        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getUsername()).isEqualTo(YAHO_USERNAME);
            assertThat(response.getEmail()).isEqualTo(YAHO_EMAIL);
            assertThat(response.getAddress()).isEqualTo(YAHO_ADDRESS);
            assertThat(response.getPhoneNumber()).isEqualTo(YAHO_PHONE_NUMBER);
        });
    }

    @DisplayName("???????????? ?????? username??? ?????? ????????? ?????????.")
    @Test
    void findByUsername_error_notExist_username() {
        assertThatThrownBy(() -> customerService.find(new LoginCustomer("merong")))
                .isInstanceOf(NoSuchCustomerException.class);
    }

    @DisplayName("customer??? username??? ???????????? ????????????.")
    @Test
    void findByUsername() {
        customerService.save(YAHO_SAVE_REQUEST);

        Customer foundCustomer = customerService.findByUsername(YAHO_USERNAME);

        assertAll(() -> {
            assertThat(foundCustomer.getId()).isNotNull();
            assertThat(foundCustomer.getUsername()).isEqualTo(YAHO_USERNAME);
            assertThat(foundCustomer.getEmail()).isEqualTo(YAHO_EMAIL);
            assertThat(foundCustomer.getAddress()).isEqualTo(YAHO_ADDRESS);
            assertThat(foundCustomer.getPhoneNumber()).isEqualTo(YAHO_PHONE_NUMBER);
        });
    }

    @DisplayName("???????????? ?????? username??? ?????? ????????? ?????????.")
    @Test
    void find_error_notExist_username() {
        assertThatThrownBy(() -> customerService.findByUsername("merong"))
                .isInstanceOf(NoSuchCustomerException.class);
    }

    @DisplayName("???????????? ?????? uesrname??? password??? ?????? ????????? ?????????.")
    @Test
    void validateUsernameAndPassword() {
        assertThatThrownBy(() -> customerService.validateUsernameAndPassword(MAT_USERNAME, MAT_PASSWORD))
                .isInstanceOf(InvalidCustomerException.class);
    }

    @DisplayName("customer??? ????????????.")
    @Test
    void update() {
        CustomerSaveRequest request = MAT_SAVE_REQUEST;
        customerService.save(request);

        customerService.update(new LoginCustomer(MAT_USERNAME), UPDATE_REQUEST);

        CustomerResponse response = customerService.find(new LoginCustomer(MAT_USERNAME));
        assertAll(() -> {
            assertThat(response.getId()).isNotNull();
            assertThat(response.getUsername()).isEqualTo(MAT_USERNAME);
            assertThat(response.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(response.getAddress()).isEqualTo(UPDATE_ADDRESS);
            assertThat(response.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
        });
    }

    @DisplayName("???????????? ?????? username??? ???????????? ?????? ????????? ?????????.")
    @Test
    void update_error_notExist_username() {
        assertThatThrownBy(() -> customerService.update(new LoginCustomer("merong"), UPDATE_REQUEST))
                .isInstanceOf(NoSuchCustomerException.class);
    }

    @DisplayName("customer??? ????????????.")
    @Test
    void delete() {
        customerService.save(YAHO_SAVE_REQUEST);

        customerService.delete(new LoginCustomer(YAHO_USERNAME));

        assertThatThrownBy(() -> customerService.find(new LoginCustomer(YAHO_USERNAME)))
                .isInstanceOf(NoSuchCustomerException.class);
    }

    @DisplayName("???????????? ?????? username??? ???????????? ?????? ????????? ?????????.")
    @Test
    void delete_error_notExist_username() {
        assertThatThrownBy(() -> customerService.delete(new LoginCustomer(YAHO_USERNAME)))
                .isInstanceOf(NoSuchCustomerException.class);
    }
}
