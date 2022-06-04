package woowacourse.auth.support;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtTokenProviderTest(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @DisplayName("토큰을 생성한다.")
    @Test
    void createToken() {
        String payload = MAT_USERNAME;

        String token = jwtTokenProvider.createToken(payload);

        assertThat(token).isNotEmpty();
    }

    @DisplayName("토큰 정보를 기반으로 Payload를 조회한다.")
    @Test
    void getPayload() {
        String token = jwtTokenProvider.createToken(MAT_USERNAME);

        String payload = jwtTokenProvider.getPayload(token);

        assertThat(payload).isEqualTo(MAT_USERNAME);
    }

    @DisplayName("유효한 토큰인 경우 예외를 던지지 않는다.")
    @Test
    void validateToken() {
        String token = jwtTokenProvider.createToken(MAT_USERNAME);

        assertDoesNotThrow(() -> jwtTokenProvider.validateToken(token));
    }
}
