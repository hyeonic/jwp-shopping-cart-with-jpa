package woowacourse.shoppingcart.exception;

public class NoSuchCartItemException extends RuntimeException {

    public NoSuchCartItemException() {
        this("존재하지 않는 장바구니입니다.");
    }

    public NoSuchCartItemException(final String msg) {
        super(msg);
    }
}
