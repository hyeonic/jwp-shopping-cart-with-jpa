package woowacourse.shoppingcart.exception;

public class NoSuchOrdersException extends RuntimeException {

    public NoSuchOrdersException() {
        this("존재하지 않는 주문입니다.");
    }

    public NoSuchOrdersException(final String msg) {
        super(msg);
    }
}
