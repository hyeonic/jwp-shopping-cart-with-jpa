package woowacourse.shoppingcart.exception;

public class NoSuchProductException extends RuntimeException {

    public NoSuchProductException() {
        this("존재하지 않는 상품입니다.");
    }

    public NoSuchProductException(final String msg) {
        super(msg);
    }
}
