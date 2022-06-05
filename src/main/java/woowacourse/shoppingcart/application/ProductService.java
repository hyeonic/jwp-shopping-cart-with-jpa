package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Product;

import java.util.List;
import woowacourse.shoppingcart.dto.product.ProductSaveRequest;
import woowacourse.shoppingcart.exception.NoSuchProductException;

@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductDao productDao;

    public ProductService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public List<Product> findProducts() {
        return productDao.findProducts();
    }

    @Transactional
    public Long save(ProductSaveRequest request) {
        Product product = productDao.save(request.toProduct());
        return product.getId();
    }

    public Product findProductById(Long productId) {
        return productDao.findById(productId)
                .orElseThrow(NoSuchProductException::new);
    }

    @Transactional
    public void deleteProductById(Long productId) {
        productDao.delete(productId);
    }
}
