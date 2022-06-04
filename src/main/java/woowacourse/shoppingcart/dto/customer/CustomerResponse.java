package woowacourse.shoppingcart.dto.customer;

import woowacourse.shoppingcart.domain.customer.Customer;

public class CustomerResponse {

    private final Long id;
    private final String username;
    private final String email;
    private final String address;
    private final String phoneNumber;

    public CustomerResponse(Customer customer) {
        this(customer.getId(), customer.getUsername(), customer.getEmail(), customer.getAddress(),
                customer.getPhoneNumber());
    }

    public CustomerResponse(Long id, String username, String email, String address, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
