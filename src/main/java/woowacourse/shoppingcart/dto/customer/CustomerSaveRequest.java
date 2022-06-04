package woowacourse.shoppingcart.dto.customer;

import woowacourse.shoppingcart.domain.customer.Customer;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CustomerSaveRequest {

    @Pattern(regexp = "^[a-z0-9_-]{5,20}$",
            message = "username 형식이 올바르지 않습니다. (영문 소문자, 숫자와 특수기호(_), (-)만 사용 가능)")
    private String username;

    @Pattern(regexp = "^[a-z0-9._-]+@[a-z]+[.]+[a-z]{2,3}$",
            message = "email 형식이 올바르지 않습니다. (형식: example@email.com)")
    private String email;

    @NotEmpty(message = "password는 필수 입력 사항압니다.")
    private String password;

    @NotEmpty(message = "address는 필수 입력 사항압니다.")
    @Size(max = 255, message = "address 형식이 올바르지 않습니다. (길이: 255 이하)")
    private String address;

    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "전화번호 형식이 올바르지 않습니다. (형식: 000-0000-0000)")
    private String phoneNumber;

    private CustomerSaveRequest() {
    }

    public CustomerSaveRequest(String username, String email, String password, String address,
                               String phoneNumber) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Customer toCustomer() {
        return new Customer(username, email, password, address, phoneNumber);
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
