package woowacourse.shoppingcart.domain;

public class Cart {

    private Long id;
    private Long productId;
    private String name;
    private int price;
    private String imageUrl;

    public Cart() {
    }

    public Cart(final Long id, final Product product) {
        this(id, product.getId(), product.getName(), product.getPrice(), product.getImageUrl());
    }

    public Cart(final Long id, final Long productId, final String name, final int price, final String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
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
}
