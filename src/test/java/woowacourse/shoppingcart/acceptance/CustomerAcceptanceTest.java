package woowacourse.shoppingcart.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_EMAIL;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.UPDATE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_ADDRESS;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_EMAIL;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_PHONE_NUMBER;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.YAHO_USERNAME;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.customer.CustomerEmailDuplicatedRequest;
import woowacourse.shoppingcart.dto.customer.CustomerEmailDuplicatedResponse;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUsernameDuplicatedRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUsernameDuplicatedResponse;

@DisplayName("회원 관련 기능")
public class CustomerAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원가입을 한다.")
    @Test
    void addCustomer() {
        CustomerSaveRequest request = MAT_SAVE_REQUEST;
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/customers")
                .then().log().all()
                .extract();

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).contains("customers");
        });
    }

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMe() {
        generateCustomer(YAHO_SAVE_REQUEST);

        String accessToken = generateToken(new TokenRequest(YAHO_USERNAME, YAHO_PASSWORD));

        CustomerResponse customerResponse = findCustomer(accessToken);
        assertAll(() -> {
            assertThat(customerResponse.getId()).isNotNull();
            assertThat(customerResponse.getUsername()).isEqualTo(YAHO_USERNAME);
            assertThat(customerResponse.getEmail()).isEqualTo(YAHO_EMAIL);
            assertThat(customerResponse.getAddress()).isEqualTo(YAHO_ADDRESS);
            assertThat(customerResponse.getPhoneNumber()).isEqualTo(YAHO_PHONE_NUMBER);
        });
    }

    @DisplayName("유효하지 않는 토큰으로 내 정보 조회 시 401 상태코드를 반환한다.")
    @Test
    void getMe_error_invalidToken() {
        String accessToken = "aaaaaaa.bbbbbbb.ccccccc";

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    @DisplayName("토큰 헤더 없이 내 정보 조회 시 401 상태코드를 반환한다.")
    @Test
    void getMe_error_emptyAuthorizationHeader() {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
                .extract();
    }

    @DisplayName("내 정보를 수정한다.")
    @Test
    void updateMe() {
        generateCustomer(MAT_SAVE_REQUEST);

        String accessToken = generateToken(new TokenRequest(MAT_USERNAME, MAT_PASSWORD));

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(UPDATE_REQUEST)
                .when().put("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        CustomerResponse customerResponse = findCustomer(accessToken);

        assertAll(() -> {
            assertThat(customerResponse.getId()).isNotNull();
            assertThat(customerResponse.getUsername()).isEqualTo(MAT_USERNAME);
            assertThat(customerResponse.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(customerResponse.getAddress()).isEqualTo(UPDATE_ADDRESS);
            assertThat(customerResponse.getPhoneNumber()).isEqualTo(UPDATE_PHONE_NUMBER);
        });
    }

    @DisplayName("회원을 탈퇴한다.")
    @Test
    void deleteMe() {
        generateCustomer(YAHO_SAVE_REQUEST);

        String accessToken = generateToken(new TokenRequest(YAHO_USERNAME, YAHO_PASSWORD));

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        RestAssured.given().log().all()
                .auth().oauth2(accessToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/customers/me")
                .then().log().all()
                .statusCode(HttpStatus.NOT_FOUND.value())
                .extract();
    }

    @DisplayName("username의 중복 여부를 확인한다.")
    @Test
    void duplicatedUsername() {
        CustomerUsernameDuplicatedResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerUsernameDuplicatedRequest("mat"))
                .when().post("/api/customers/duplication/username")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerUsernameDuplicatedResponse.class);

        assertAll(() -> {
            assertThat(response.getUsername()).isEqualTo("mat");
            assertThat(response.isDuplicated()).isFalse();
        });
    }

    @DisplayName("username이 중복된 경우 true를 반환한다.")
    @Test
    void duplicatedUsername_duplicated() {
        generateCustomer(MAT_SAVE_REQUEST);

        CustomerUsernameDuplicatedResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerUsernameDuplicatedRequest(MAT_USERNAME))
                .when().post("/api/customers/duplication/username")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerUsernameDuplicatedResponse.class);

        assertAll(() -> {
            assertThat(response.getUsername()).isEqualTo(MAT_USERNAME);
            assertThat(response.isDuplicated()).isTrue();
        });
    }

    @DisplayName("email의 중복 여부를 확인한다.")
    @Test
    void duplicatedEmail() {
        CustomerEmailDuplicatedResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerEmailDuplicatedRequest(MAT_EMAIL))
                .when().post("/api/customers/duplication/email")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerEmailDuplicatedResponse.class);

        assertAll(() -> {
            assertThat(response.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(response.isDuplicated()).isFalse();
        });
    }

    @DisplayName("email이 중복된 경우 true를 반환한다.")
    @Test
    void duplicatedEmail_duplicated() {
        generateCustomer(MAT_SAVE_REQUEST);

        CustomerEmailDuplicatedResponse response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new CustomerEmailDuplicatedRequest(MAT_EMAIL))
                .when().post("/api/customers/duplication/email")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerEmailDuplicatedResponse.class);

        assertAll(() -> {
            assertThat(response.getEmail()).isEqualTo(MAT_EMAIL);
            assertThat(response.isDuplicated()).isTrue();
        });
    }

    private void generateCustomer(CustomerSaveRequest request) {
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/customers")
                .then().log().all()
                .extract();
    }

    private String generateToken(TokenRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/auth/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(TokenResponse.class)
                .getAccessToken();
    }

    private CustomerResponse findCustomer(String accessToken) {
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
