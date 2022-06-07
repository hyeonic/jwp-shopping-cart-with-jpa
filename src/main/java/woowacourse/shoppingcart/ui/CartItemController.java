package woowacourse.shoppingcart.ui;

import java.net.URI;
import java.util.List;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import woowacourse.auth.support.AuthenticationPrincipal;
import woowacourse.shoppingcart.application.CartItemService;
import woowacourse.shoppingcart.dto.cartitem.CartItemResponse;
import woowacourse.shoppingcart.dto.cartitem.CartItemSaveRequest;
import woowacourse.shoppingcart.dto.cartitem.CartItemUpdateRequest;
import woowacourse.shoppingcart.dto.customer.LoginCustomer;

@RestController
@RequestMapping("/api/customers/me/cart-items")
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(@AuthenticationPrincipal LoginCustomer loginCustomer) {
        return ResponseEntity.ok().body(cartItemService.findByCustomerUsername(loginCustomer.getUsername()));
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartItemResponse> getCartItem(@AuthenticationPrincipal LoginCustomer loginCustomer,
                                                              @PathVariable Long cartId) {
        CartItemResponse cartItemResponse = cartItemService.findById(cartId);
        return ResponseEntity.ok().body(cartItemResponse);
    }

    @PostMapping
    public ResponseEntity<Void> save(@AuthenticationPrincipal LoginCustomer loginCustomer,
                                     @Valid @RequestBody CartItemSaveRequest request) {
        Long cartId = cartItemService.save(loginCustomer.getUsername(), request).getId();
        URI responseLocation = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{cartId}")
                .buildAndExpand(cartId)
                .toUri();
        return ResponseEntity.created(responseLocation).build();
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCartItem(@AuthenticationPrincipal LoginCustomer loginCustomer,
                                               @PathVariable Long cartId) {
        cartItemService.delete(loginCustomer.getUsername(), cartId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{cartId}")
    public ResponseEntity<Void> updateQuantity(@AuthenticationPrincipal LoginCustomer loginCustomer,
                                               @PathVariable Long cartId,
                                               @Valid @RequestBody CartItemUpdateRequest request) {
        cartItemService.update(cartId, request);
        return ResponseEntity.noContent().build();
    }
}
