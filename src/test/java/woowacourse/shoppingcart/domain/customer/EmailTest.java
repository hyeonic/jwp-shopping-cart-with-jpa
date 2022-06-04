package woowacourse.shoppingcart.domain.customer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class EmailTest {

    @DisplayName("이메일 형식이 맞지 않으면 예외를 던진다.")
    @ParameterizedTest
    @ValueSource(strings = {"@email.com", "test@", "testemail.com", "test@email", "email"})
    void createEmail_error_emailFormat(String email) {
        assertThatThrownBy(() -> new Email(email))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
