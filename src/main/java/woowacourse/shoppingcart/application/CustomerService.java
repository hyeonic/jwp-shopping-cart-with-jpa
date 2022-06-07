package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.CustomerDao;
import woowacourse.shoppingcart.domain.customer.Customer;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdateRequest;
import woowacourse.shoppingcart.dto.customer.LoginCustomer;
import woowacourse.shoppingcart.exception.InvalidCustomerException;
import woowacourse.shoppingcart.exception.NoSuchCustomerException;

@Service
@Transactional(readOnly = true)
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    @Transactional
    public CustomerResponse save(CustomerSaveRequest customerSaveRequest) {
        Customer customer = customerDao.save(customerSaveRequest.toCustomer());
        return new CustomerResponse(customer);
    }

    public CustomerResponse find(LoginCustomer loginCustomer) {
        Customer customer = getCustomer(loginCustomer.getUsername());
        return new CustomerResponse(customer);
    }

    public Customer findByUsername(String username) {
        return getCustomer(username);
    }

    public void validateUsernameAndPassword(String username, String password) {
        if (!customerDao.existsByUsernameAndPassword(username, password)) {
            throw new InvalidCustomerException();
        }
    }

    public boolean isDuplicatedUsername(String username) {
        return customerDao.existsByUsername(username);
    }

    public boolean isDuplicatedEmail(String email) {
        return customerDao.existsByEmail(email);
    }

    @Transactional
    public void update(LoginCustomer loginCustomer, CustomerUpdateRequest customerUpdateRequest) {
        Customer customer = getCustomer(loginCustomer.getUsername());
        customer.modify(customerUpdateRequest.getAddress(), customerUpdateRequest.getPhoneNumber());
        customerDao.update(customer);
    }

    @Transactional
    public void delete(LoginCustomer loginCustomer) {
        Customer customer = getCustomer(loginCustomer.getUsername());
        customerDao.delete(customer);
    }

    private Customer getCustomer(String username) {
        return customerDao.findByUsername(username)
                .orElseThrow(NoSuchCustomerException::new);
    }
}
