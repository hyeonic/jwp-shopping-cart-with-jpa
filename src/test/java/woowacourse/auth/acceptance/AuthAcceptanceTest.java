package woowacourse.auth.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_USERNAME;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.acceptance.AcceptanceTest;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;

@DisplayName("인증 관련 기능")
public class AuthAcceptanceTest extends AcceptanceTest {

    @DisplayName("Bearer Auth 로그인 성공")
    @Test
    void myInfoWithBearerAuth() {
        generateCustomer(MAT_SAVE_REQUEST);
        String accessToken = generateToken(new TokenRequest(MAT_USERNAME, MAT_PASSWORD));

        CustomerResponse customerResponse = getMe(accessToken);

        assertThat(customerResponse.getUsername()).isEqualTo(MAT_USERNAME);
    }

    @DisplayName("Bearer Auth 로그인 실패")
    @Test
    void myInfoWithBadBearerAuth() {
        generateCustomer(MAT_SAVE_REQUEST);

        TokenRequest tokenRequest = new TokenRequest(YAHO_USERNAME, YAHO_PASSWORD);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/api/auth/token")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    @DisplayName("Bearer Auth 유효하지 않은 토큰")
    @Test
    void myInfoWithWrongBearerAuth() {
        String accessToken = "aaaaaaa.bbbbbbb.ccccccc";

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    private void generateCustomer(CustomerSaveRequest request) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/customers")
                .then().log().all()
                .extract();
    }

    private String generateToken(TokenRequest tokenRequest) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/api/auth/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class)
                .getAccessToken();
    }

    private CustomerResponse getMe(String accessToken) {
        return RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerResponse.class);
    }
}
