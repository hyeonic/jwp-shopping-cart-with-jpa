package woowacourse.shoppingcart.dto.product;

import woowacourse.shoppingcart.domain.Product;

public class ProductSaveRequest {

    private String name;
    private int price;
    private String imageUrl;
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
