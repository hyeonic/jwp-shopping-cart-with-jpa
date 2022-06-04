package woowacourse.shoppingcart.exception;

public class EmptyAuthorizationHeaderException extends RuntimeException {

    public EmptyAuthorizationHeaderException() {
        this("header에 Authorization이 존재하지 않습니다.");
    }

    public EmptyAuthorizationHeaderException(String msg) {
        super(msg);
    }
}
