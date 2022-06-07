package woowacourse.shoppingcart.dto.customer;

import javax.validation.constraints.Pattern;

public class CustomerEmailDuplicatedRequest {

    @Pattern(regexp = "^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$",
            message = "email 형식이 올바르지 않습니다. (형식: example@email.com)")
    private String email;

    private CustomerEmailDuplicatedRequest() {
    }

    public CustomerEmailDuplicatedRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
