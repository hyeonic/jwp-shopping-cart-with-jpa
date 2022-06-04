package woowacourse.shoppingcart.domain.customer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Username {

    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9_-]{5,20}$");

    private final String value;

    public Username(String value) {
        validateUsername(value);
        this.value = value;
    }

    private void validateUsername(String username) {
        Matcher matcher = USERNAME_PATTERN.matcher(username);
        if (!matcher.matches()) {
            throw new IllegalArgumentException(
                    "username 형식이 올바르지 않습니다. (영문 소문자, 숫자와 특수기호(_), (-)만 사용 가능");
        }
    }

    public String getValue() {
        return value;
    }
}
