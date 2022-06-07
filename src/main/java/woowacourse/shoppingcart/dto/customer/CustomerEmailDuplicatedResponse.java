package woowacourse.shoppingcart.dto.customer;

public class CustomerEmailDuplicatedResponse {

    private final String email;
    private final boolean duplicated;

    public CustomerEmailDuplicatedResponse(String email, boolean duplicated) {
        this.email = email;
        this.duplicated = duplicated;
    }

    public String getEmail() {
        return email;
    }

    public boolean isDuplicated() {
        return duplicated;
    }
}
