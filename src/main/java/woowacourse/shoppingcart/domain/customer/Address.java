package woowacourse.shoppingcart.domain.customer;

public class Address {

    private final String value;

    public Address(String value) {
        validateAddress(value);
        this.value = value;
    }

    private void validateAddress(String address) {
        if (address.length() > 255) {
            throw new IllegalArgumentException("address 형식이 올바르지 않습니다. (길이: 255 이하)");
        }
    }

    public String getValue() {
        return value;
    }
}
