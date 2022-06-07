package woowacourse.shoppingcart.domain;

import java.util.Objects;

public class Product {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private String description;
    private boolean deleted;

    private Product() {
    }

    public Product(Long id, Product product) {
        this(id, product.name, product.price, product.imageUrl, product.description, product.deleted);
    }

    public Product(String name, int price, String imageUrl, String description) {
        this(null, name, price, imageUrl, description, false);
    }

    public Product(Long id, String name, int price, String imageUrl, String description, boolean deleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.description = description;
        this.deleted = deleted;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDeleted() {
        return deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
