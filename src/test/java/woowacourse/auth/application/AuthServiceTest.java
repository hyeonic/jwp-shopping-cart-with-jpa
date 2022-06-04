package woowacourse.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.application.CustomerService;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class AuthServiceTest {

    private final AuthService authService;
    private final CustomerService customerService;

    AuthServiceTest(AuthService authService, CustomerService customerService) {
        this.authService = authService;
        this.customerService = customerService;
    }

    @DisplayName("로그인 정보를 기반으로 토큰을 발급 받는다.")
    @Test
    void createToken() {
        customerService.save(MAT_SAVE_REQUEST);
        TokenRequest tokenRequest = new TokenRequest(MAT_USERNAME, MAT_PASSWORD);

        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }

    @DisplayName("존재하지 않는 고객의 로그인 정보인 경우 예외를 던진다.")
    @Test
    void createToken_error_notExistsCustomer() {
        TokenRequest tokenRequest = new TokenRequest(MAT_USERNAME, MAT_PASSWORD);

        TokenResponse tokenResponse = authService.createToken(tokenRequest);

        assertThat(tokenResponse.getAccessToken()).isNotEmpty();
    }
}
