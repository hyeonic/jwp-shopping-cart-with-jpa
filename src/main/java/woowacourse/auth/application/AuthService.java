package woowacourse.auth.application;

import org.springframework.stereotype.Service;

import woowacourse.auth.dto.TokenRequest;
import woowacourse.auth.dto.TokenResponse;
import woowacourse.auth.support.JwtTokenProvider;
import woowacourse.shoppingcart.application.CustomerService;

@Service
public class AuthService {

    private final CustomerService customerService;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(CustomerService customerService, JwtTokenProvider jwtTokenProvider) {
        this.customerService = customerService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public TokenResponse createToken(TokenRequest tokenRequest) {
        customerService.validateUsernameAndPassword(tokenRequest.getUsername(), tokenRequest.getPassword());
        String token = jwtTokenProvider.createToken(tokenRequest.getUsername());
        return new TokenResponse(token);
    }
}
