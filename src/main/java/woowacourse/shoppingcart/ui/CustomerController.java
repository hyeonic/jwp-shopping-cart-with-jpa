package woowacourse.shoppingcart.ui;

import java.net.URI;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.application.CustomerService;
import woowacourse.shoppingcart.dto.customer.CustomerEmailDuplicatedRequest;
import woowacourse.shoppingcart.dto.customer.CustomerEmailDuplicatedResponse;
import woowacourse.shoppingcart.dto.customer.CustomerResponse;
import woowacourse.shoppingcart.dto.customer.CustomerSaveRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUpdateRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUsernameDuplicatedRequest;
import woowacourse.shoppingcart.dto.customer.CustomerUsernameDuplicatedResponse;
import woowacourse.shoppingcart.dto.customer.LoginCustomer;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<Void> save(@Valid @RequestBody CustomerSaveRequest request) {
        CustomerResponse response = customerService.save(request);
        return ResponseEntity.created(URI.create("/api/customers/" + response.getId())).build();
    }

    @GetMapping("/me")
    public ResponseEntity<CustomerResponse> findCustomer(@AuthenticationPrincipal LoginCustomer loginCustomer) {
        CustomerResponse customerResponse = customerService.find(loginCustomer);
        return ResponseEntity.ok(customerResponse);
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateCustomer(@AuthenticationPrincipal LoginCustomer loginCustomer,
                                               @Valid @RequestBody CustomerUpdateRequest updateRequest) {
        customerService.update(loginCustomer, updateRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteCustomer(@AuthenticationPrincipal LoginCustomer loginCustomer) {
        customerService.delete(loginCustomer);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/duplication/username")
    public ResponseEntity<CustomerUsernameDuplicatedResponse> duplicatedUsername(
            @RequestBody CustomerUsernameDuplicatedRequest request) {
        boolean duplicatedUsername = customerService.isDuplicatedUsername(request.getUsername());
        return ResponseEntity.ok(new CustomerUsernameDuplicatedResponse(request.getUsername(), duplicatedUsername));
    }

    @PostMapping("/duplication/email")
    public ResponseEntity<CustomerEmailDuplicatedResponse> duplicatedEmail(
            @RequestBody CustomerEmailDuplicatedRequest request) {
        boolean duplicatedEmail = customerService.isDuplicatedEmail(request.getEmail());
        return ResponseEntity.ok(new CustomerEmailDuplicatedResponse(request.getEmail(), duplicatedEmail));
    }
}
