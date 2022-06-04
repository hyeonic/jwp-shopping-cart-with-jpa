package woowacourse.shoppingcart.domain.customer;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class AddressTest {

    @DisplayName("Address를 생성한다.")
    @ParameterizedTest
    @ValueSource(strings = {
            "서울 강남구 테헤란로 411, 성담빌딩 13층 (선릉 캠퍼스)",
            "서울 송파구 올림픽로 35다길 42, 루터회관 14층 (잠실 캠퍼스)"
    })
    void createAddress(String address) {
        assertDoesNotThrow(() -> new Address(address));
    }

    @DisplayName("Address의 길이가 255를 초과하는 경우 예외를 던진다.")
    @Test
    void createAddress_error_overSize() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i <= 255; i++) {
            stringBuilder.append(" ");
        }

        String address = stringBuilder.toString();

        assertThatThrownBy(() -> new Address(address))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
