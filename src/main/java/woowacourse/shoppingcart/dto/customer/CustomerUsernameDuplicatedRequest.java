package woowacourse.shoppingcart.dto.customer;

public class CustomerUsernameDuplicatedRequest {

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
