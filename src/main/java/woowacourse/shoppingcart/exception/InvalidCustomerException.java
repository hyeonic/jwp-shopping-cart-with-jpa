package woowacourse.shoppingcart.exception;

public class InvalidCustomerException extends RuntimeException {

    public InvalidCustomerException() {
        this("유효하지 않은 유저입니다.");
    }

    public InvalidCustomerException(String msg) {
        super(msg);
    }
}
