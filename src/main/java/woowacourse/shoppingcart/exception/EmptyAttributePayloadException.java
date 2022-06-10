package woowacourse.shoppingcart.exception;

public class EmptyAttributePayloadException extends RuntimeException {

    public EmptyAttributePayloadException() {
        this("attribute에 payload가 존재하지 않습니다.");
    }

    public EmptyAttributePayloadException(String msg) {
        super(msg);
    }
}
