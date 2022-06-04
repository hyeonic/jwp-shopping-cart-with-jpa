package woowacourse.shoppingcart;

import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdateRequest;

public class CustomerFixtures {

    public static final String MAT_USERNAME = "hyeonic";
    public static final String MAT_EMAIL = "dev.hyeonic@gmail.com";
    public static final String MAT_PASSWORD = "1q2w3e4r!";
    public static final String MAT_ADDRESS = "서울 강남구 테헤란로 411, 성담빌딩 13층 (선릉 캠퍼스)";
    public static final String MAT_PHONE_NUMBER = "010-0000-0000";

    public static final Customer MAT = new Customer(
            MAT_USERNAME, MAT_EMAIL, MAT_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER);

    public static final CustomerSaveRequest MAT_SAVE_REQUEST = new CustomerSaveRequest(
            MAT_USERNAME, MAT_EMAIL, MAT_PASSWORD, MAT_ADDRESS, MAT_PHONE_NUMBER);

    public static final String YAHO_USERNAME = "pup-paw";
    public static final String YAHO_EMAIL = "pup-paw@gmail.com";
    public static final String YAHO_PASSWORD = "1q2w3e4r!";
    public static final String YAHO_ADDRESS = "서울 강남구 테헤란로 411, 성담빌딩 13층 (선릉 캠퍼스)";
    public static final String YAHO_PHONE_NUMBER = "010-0000-0000";

    public static final Customer YAHO = new Customer(
            YAHO_USERNAME, YAHO_EMAIL, YAHO_PASSWORD, YAHO_ADDRESS, YAHO_PHONE_NUMBER);

    public static final CustomerSaveRequest YAHO_SAVE_REQUEST = new CustomerSaveRequest(
            YAHO_USERNAME, YAHO_EMAIL, YAHO_PASSWORD, YAHO_ADDRESS, YAHO_PHONE_NUMBER);

    public static final String UPDATE_ADDRESS = "서울 송파구 올림픽로 35다길 42, 루터회관 14층 (잠실 캠퍼스)";
    public static final String UPDATE_PHONE_NUMBER = "010-1111-1111";

    public static final CustomerUpdateRequest UPDATE_REQUEST = new CustomerUpdateRequest(
            UPDATE_ADDRESS, UPDATE_PHONE_NUMBER);
}
