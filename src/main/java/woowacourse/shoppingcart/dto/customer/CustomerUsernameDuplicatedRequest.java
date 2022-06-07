package woowacourse.shoppingcart.dto.customer;

import javax.validation.constraints.Pattern;

public class CustomerUsernameDuplicatedRequest {

    @Pattern(regexp = "^[a-z0-9_-]{5,20}$",
            message = "username 형식이 올바르지 않습니다. (영문 소문자, 숫자와 특수기호(_), (-)만 사용 가능)")
    private String username;

    private CustomerUsernameDuplicatedRequest() {
    }

    public CustomerUsernameDuplicatedRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}
