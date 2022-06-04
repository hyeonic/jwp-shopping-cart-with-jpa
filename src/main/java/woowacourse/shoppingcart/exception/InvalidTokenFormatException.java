package woowacourse.shoppingcart.exception;

public class InvalidTokenFormatException extends RuntimeException {

    public InvalidTokenFormatException() {
        this("token 형식이 잘못 되었습니다. (형식: Bearer aaaaaaaa.bbbbbbbb.cccccccc)");
    }

    public InvalidTokenFormatException(String msg) {
        super(msg);
    }
}
