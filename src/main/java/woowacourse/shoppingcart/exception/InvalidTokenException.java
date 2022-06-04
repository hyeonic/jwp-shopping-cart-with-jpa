package woowacourse.shoppingcart.exception;

public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException() {
        this("유효하지 않은 토큰입니다.");
    }

    public InvalidTokenException(final String msg) {
        super(msg);
    }
}
