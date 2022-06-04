package woowacourse.shoppingcart.dto.customer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerUpdateRequest {

    @NotEmpty(message = "address는 필수 입력 사항압니다.")
    @Size(max = 255, message = "address 형식이 올바르지 않습니다. (길이: 255 이하)")
    private String address;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식이 올바르지 않습니다. (형식: 000-0000-0000)")
    private String phoneNumber;

    private CustomerUpdateRequest() {
    }

    public CustomerUpdateRequest(String address, String phoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
