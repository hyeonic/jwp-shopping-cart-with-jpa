package woowacourse.shoppingcart.dto.customer;

public class CustomerUsernameDuplicatedResponse {

    private final String username;
    private final boolean duplicated;

    public CustomerUsernameDuplicatedResponse(String username, boolean duplicated) {
        this.username = username;
        this.duplicated = duplicated;
    }

    public String getUsername() {
        return username;
    }

    public boolean isDuplicated() {
        return duplicated;
    }
}
