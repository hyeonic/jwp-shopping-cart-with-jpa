package woowacourse.shoppingcart.domain.customer;

import java.util.Objects;

public class Customer {

    private final Long id;
    private final Username username;
    private final Email email;
    private final String password;
    private Address address;
    private PhoneNumber phoneNumber;

    public Customer(Long id, Customer customer) {
        this(id, customer.getUsername(), customer.getEmail(), customer.getPassword(), customer.getAddress(),
                customer.getPhoneNumber());
    }

    public Customer(String username, String email, String password, String address, String phoneNumber) {
        this(null, username, email, password, address, phoneNumber);
    }

    public Customer(Long id, String username, String email, String password, String address, String phoneNumber) {
        this.id = id;
        this.username = new Username(username);
        this.email = new Email(email);
        this.password = password;
        this.address = new Address(address);
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public void modify(String address, String phoneNumber) {
        this.address = new Address(address);
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username.getValue();
    }

    public String getEmail() {
        return email.getValue();
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address.getValue();
    }

    public String getPhoneNumber() {
        return phoneNumber.getValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
