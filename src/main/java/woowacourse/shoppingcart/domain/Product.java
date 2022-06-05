package woowacourse.shoppingcart.domain;

import java.util.Objects;

public class Product {

    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private boolean deleted;

    private Product() {
    }

    public Product(Long id, Product product) {
        this(id, product.getName(), product.getPrice(), product.getImageUrl(), product.isDeleted());
    }

    public Product(String name, int price, String imageUrl) {
        this(null, name, price, imageUrl, false);
    }

    public Product(Long id, String name, int price, String imageUrl, boolean deleted) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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
