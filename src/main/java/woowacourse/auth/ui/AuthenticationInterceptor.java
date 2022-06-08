package woowacourse.auth.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import woowacourse.auth.support.AuthorizationExtractor;
import woowacourse.auth.support.JwtTokenProvider;

public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String ATTRIBUTE_PAYLOAD_KEY = "payload";

    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }

        String accessToken = AuthorizationExtractor.extract(request);
        jwtTokenProvider.validateToken(accessToken);

        String payload = jwtTokenProvider.getPayload(accessToken);
        request.setAttribute(ATTRIBUTE_PAYLOAD_KEY, payload);
        return true;
    }
}
