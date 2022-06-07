package woowacourse.shoppingcart.exception;

public class DuplicatedEmailException extends RuntimeException {

    public DuplicatedEmailException() {
        this("중복된 email입니다.");
    }

    public DuplicatedEmailException(final String msg) {
        super(msg);
    }
}
