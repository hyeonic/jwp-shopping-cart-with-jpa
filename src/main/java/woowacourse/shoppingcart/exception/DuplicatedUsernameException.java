package woowacourse.shoppingcart.exception;

public class DuplicatedUsernameException extends RuntimeException {

    public DuplicatedUsernameException() {
        this("중복된 username입니다.");
    }

    public DuplicatedUsernameException(final String msg) {
        super(msg);
    }
}
