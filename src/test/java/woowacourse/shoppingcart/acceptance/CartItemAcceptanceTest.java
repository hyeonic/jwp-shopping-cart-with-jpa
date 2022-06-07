package woowacourse.shoppingcart.acceptance;

import static woowacourse.shoppingcart.CustomerFixtures.MAT_PASSWORD;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_SAVE_REQUEST;
import static woowacourse.shoppingcart.CustomerFixtures.MAT_USERNAME;
import static woowacourse.shoppingcart.ProductFixtures.ONE_PRODUCT_SAVE_REQUEST;
import static woowacourse.shoppingcart.ProductFixtures.TWO_PRODUCT_SAVE_REQUEST;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemSaveRequest;
import woowacourse.shoppingcart.dto.cartitem.CartItemUpdateRequest;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.product.ProductSaveRequest;

@DisplayName("장바구니 관련 기능")
public class CartItemAcceptanceTest extends AcceptanceTest {

    @DisplayName("장바구니를 추가한다.")
    @Test
    void addCartItem() {
        generateCustomer(MAT_SAVE_REQUEST);
        String accessToken = generateToken(new TokenRequest(MAT_USERNAME, MAT_PASSWORD));

        Long productId = generateProduct(ONE_PRODUCT_SAVE_REQUEST);

        CartItemSaveRequest cartItemSaveRequest = new CartItemSaveRequest(productId, 2);
        generateCartItem(accessToken, cartItemSaveRequest);
    }

    @DisplayName("장바구니를 조회한다.")
    @Test
    void getCartItems() {
        generateCustomer(MAT_SAVE_REQUEST);
        String accessToken = generateToken(new TokenRequest(MAT_USERNAME, MAT_PASSWORD));

        Long productId1 = generateProduct(ONE_PRODUCT_SAVE_REQUEST);
        Long productId2 = generateProduct(TWO_PRODUCT_SAVE_REQUEST);
        generateCartItem(accessToken, new CartItemSaveRequest(productId1, 2));
        generateCartItem(accessToken, new CartItemSaveRequest(productId2, 3));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/api/customers/me/cart-items")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }

    @DisplayName("장바구니를 삭제한다.")
    @Test
    void deleteCartItem() {
        generateCustomer(MAT_SAVE_REQUEST);
        String accessToken = generateToken(new TokenRequest(MAT_USERNAME, MAT_PASSWORD));

        Long productId = generateProduct(ONE_PRODUCT_SAVE_REQUEST);
        Long cartItemId = generateCartItem(accessToken, new CartItemSaveRequest(productId, 2));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().log().all()
                .delete("/api/customers/me/cart-items/{cartItemId}", cartItemId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();
    }

    @DisplayName("장바구니의 수량을 증가 시킨다.")
    @Test
    void updateQuantity() {
        generateCustomer(MAT_SAVE_REQUEST);
        String accessToken = generateToken(new TokenRequest(MAT_USERNAME, MAT_PASSWORD));

        Long productId = generateProduct(ONE_PRODUCT_SAVE_REQUEST);
        Long cartItemId = generateCartItem(accessToken, new CartItemSaveRequest(productId, 2));

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(new CartItemUpdateRequest(10))
                .when().log().all()
                .patch("/api/customers/me/cart-items/{cartItemId}", cartItemId)
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .extract();

        CartItemResponse response = findCartItem(accessToken, cartItemId);
        Assertions.assertThat(response.getQuantity()).isEqualTo(10);
    }

    private CartItemResponse findCartItem(String accessToken, Long cartItemId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().log().all()
                .get("/api/customers/me/cart-items/{cartItemId}", cartItemId)
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CartItemResponse.class);
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

    private Long generateProduct(ProductSaveRequest request) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/products")
                .then().log().all()
                .extract();

        return Long.parseLong(response.header("Location").split("/products/")[1]);
    }

    private Long generateCartItem(String accessToken, CartItemSaveRequest cartItemSaveRequest) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(cartItemSaveRequest)
                .when().post("/api/customers/me/cart-items")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();

        return Long.parseLong(response.header("Location").split("/cart-items/")[1]);
    }
}
