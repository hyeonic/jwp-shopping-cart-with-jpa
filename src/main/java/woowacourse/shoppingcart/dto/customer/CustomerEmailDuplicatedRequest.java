package woowacourse.shoppingcart.dto.customer;

public class CustomerEmailDuplicatedRequest {

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
