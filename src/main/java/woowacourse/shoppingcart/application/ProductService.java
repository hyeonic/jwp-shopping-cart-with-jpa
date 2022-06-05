package woowacourse.shoppingcart.application;

import static java.util.stream.Collectors.toList;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.dto.product.ProductResponse;
import woowacourse.shoppingcart.dto.product.ProductSaveRequest;
import woowacourse.shoppingcart.exception.NoSuchProductException;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<ProductResponse> findAll() {
        List<Product> products = productDao.findAll();
        return products.stream()
                .map(ProductResponse::new)
                .collect(toList());
    }

    @Transactional
    public ProductResponse save(ProductSaveRequest request) {
        Product product = productDao.save(request.toProduct());
        return new ProductResponse(product);
    }

    public ProductResponse findById(Long id) {
        Product product = getProduct(id);
        return new ProductResponse(product);
    }

    @Transactional
    public void deleteById(Long id) {
        Product product = getProduct(id);
        productDao.delete(product);
    }

    private Product getProduct(Long id) {
        return productDao.findById(id)
                .orElseThrow(NoSuchProductException::new);
    }
}
