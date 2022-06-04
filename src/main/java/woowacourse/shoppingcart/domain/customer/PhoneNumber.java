package woowacourse.shoppingcart.domain.customer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber {

    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");

    private final String value;

    public PhoneNumber(String value) {
        validatePhoneNumber(value);
        this.value = value;
    }

    private void validatePhoneNumber(String phoneNumber) {
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("전화번호 형식이 올바르지 않습니다. (형식: 000-0000-0000");
        }
    }

    public String getValue() {
        return value;
    }
}
