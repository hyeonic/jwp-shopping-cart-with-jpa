package woowacourse.shoppingcart.exception;

public class NoSuchCustomerException extends RuntimeException {
    public NoSuchCustomerException() {
        this("존재하지 않는 유저입니다.");
    }

    public NoSuchCustomerException(final String msg) {
        super(msg);
    }
}
