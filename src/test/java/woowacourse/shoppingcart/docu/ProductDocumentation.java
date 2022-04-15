package woowacourse.shoppingcart.docu;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import woowacourse.shoppingcart.application.ProductService;
import woowacourse.shoppingcart.dto.ProductDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductDocumentation extends Documentation {

    @MockBean
    private ProductService productService;

    @Test
    void createProduct() throws Exception {
        ProductDto productDto = new ProductDto(
                "상품이름",
                10_000,
                "http://example.com/chicken.jpg"
        );

        when(productService.addProduct(any())).thenReturn(1L);

        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .characterEncoding("UTF-8")
                .content(objectMapper.writeValueAsString(productDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andDo(document("products/create"));
    }
}
