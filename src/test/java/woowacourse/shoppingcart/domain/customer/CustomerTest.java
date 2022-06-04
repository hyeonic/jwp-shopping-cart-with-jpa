package woowacourse.shoppingcart.domain.customer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_EMAIL;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_EMAIL;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_USERNAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class CustomerTest {

    @DisplayName("고객을 생성한다.")
    @Test
    void create() {
        assertDoesNotThrow(() -> new Customer(MAT_USERNAME, MAT_EMAIL, MAT_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER));
    }

    @DisplayName("username 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"test", "testtesttesttesttesttest", "TESTTEST", "test.test"})
    void create_error_usernameFormat(String username) {
        assertThatThrownBy(() -> new Customer(username, MAT_EMAIL, MAT_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("username 형식에 맞으면 customer가 생성된다.")
    @ParameterizedTest
    @ValueSource(strings = {"pup-paw", "pup_paw", "123456", "a1b2c3_-"})
    void create_valid_username(String username) {
        assertDoesNotThrow(() -> new Customer(username, MAT_EMAIL, MAT_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER));
    }

    @DisplayName("이메일 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"@email.com", "test@", "testemail.com", "test@email", "email"})
    void create_error_emailFormat(String email) {
        assertThatThrownBy(() -> new Customer(YAHO_USERNAME, email, YAHO_PASSWORD, YAHO_ADDRESS, YAHO_PHONE_NUMBER))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("전화번호 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"0000-0000-0000", "-0000-0000", "000-0000", "0000"})
    void create_error_phoneNumberFormat(String phoneNumber) {
        assertThatThrownBy(() -> new Customer(MAT_USERNAME, MAT_EMAIL, MAT_PASSWORD, MAT_ADDRESS, phoneNumber))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("customer의 내부 정보를 수정한다.")
    @Test
    void modify() {
        Customer customer = YAHO;

        customer.modify(UPDATE_ADDRESS, UPDATE_PHONE_NUMBER);

        assertAll(() -> {
            assertThat(customer.getUsername()).isEqualTo(YAHO_USERNAME);
            assertThat(customer.getEmail()).isEqualTo(YAHO_EMAIL);
            assertThat(customer.getPassword()).isEqualTo(YAHO_PASSWORD);
            assertThat(customer.getAddress()).isEqualTo(UPDATE_ADDRESS);
            assertThat(customer.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
        });
    }
}
