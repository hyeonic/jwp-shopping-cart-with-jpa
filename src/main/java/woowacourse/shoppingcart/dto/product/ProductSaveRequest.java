package woowacourse.shoppingcart.dto.product;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import woowacourse.shoppingcart.domain.Product;

public class ProductSaveRequest {

    @Size(min = 1, max = 100)
    private String name;

    @Min(0)
    private int price;

    @Size(max = 1024)
    private String imageUrl;

    @Size(max = 255)
    private String description;

    public ProductSaveRequest(String name, int price, String imageUrl, String description) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public Product toProduct() {
        return new Product(name, price, imageUrl, description);
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }
}
