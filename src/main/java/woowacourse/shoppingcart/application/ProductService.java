package woowacourse.shoppingcart.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import woowacourse.shoppingcart.dao.ProductDao;
import woowacourse.shoppingcart.domain.Product;

import java.util.List;
import woowacourse.shoppingcart.dto.product.ProductSaveRequest;

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
        return productDao.save(request.toProduct());
    }

    public Product findProductById(Long productId) {
        return productDao.findProductById(productId);
    }

    @Transactional
    public void deleteProductById(Long productId) {
        productDao.delete(productId);
    }
}
