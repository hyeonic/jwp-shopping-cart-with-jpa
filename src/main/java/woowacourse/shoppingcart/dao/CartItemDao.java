package woowacourse.shoppingcart.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.CartItem;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;

@Repository
public class CartItemDao {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public CartItemDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public CartItem save(CartItem cartItem) {
        String sql = "INSERT INTO cart_item(customer_id, product_id, quantity) "
                + "VALUES(:customerId, :productId, :quantity) "
                + "ON DUPLICATE KEY UPDATE quantity = quantity + :quantity";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customerId", cartItem.getCustomer().getId())
                .addValue("productId", cartItem.getProduct().getId())
                .addValue("quantity", cartItem.getQuantity());

        jdbcTemplate.update(sql, parameterSource);
        return findByCustomerIdAndProductId(cartItem);
    }

    private CartItem findByCustomerIdAndProductId(CartItem cartItem) {
        String sql = "SELECT ci.id as id, ci.quantity as quantity, "
                + "c.id as customerId, c.username as customerUsername, c.email as customerEmail, "
                + "c.password as customerPassword, c.address as customerAddress, "
                + "c.phone_number as customerPhoneNumber, "
                + "p.id as productId, p.name as productName, p.price as productPrice, "
                + "p.image_url as productImageUrl, p.description as productDescription, p.deleted as productDeleted "
                + "FROM cart_item ci "
                + "JOIN customer c ON ci.customer_id = c.id "
                + "JOIN product p ON ci.product_id = p.id "
                + "WHERE ci.customer_id = :customerId AND ci.product_id = :productId ";

        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customerId", cartItem.getCustomer().getId())
                .addValue("productId", cartItem.getProduct().getId());

        return jdbcTemplate.queryForObject(sql, parameterSource, generateCartItemMapper());
    }

    public List<CartItem> findByCustomerId(Long customerId) {
        String sql = "SELECT ci.id as id, ci.quantity as quantity, "
                + "c.id as customerId, c.username as customerUsername, c.email as customerEmail, "
                + "c.password as customerPassword, c.address as customerAddress, "
                + "c.phone_number as customerPhoneNumber, "
                + "p.id as productId, p.name as productName, p.price as productPrice, "
                + "p.image_url as productImageUrl, p.description as productDescription, p.deleted as productDeleted "
                + "FROM cart_item ci "
                + "JOIN customer c ON ci.customer_id = c.id "
                + "JOIN product p ON ci.product_id = p.id "
                + "WHERE ci.customer_id = :customerId";

        SqlParameterSource parameterSource = new MapSqlParameterSource("customerId", customerId);
        return jdbcTemplate.query(sql, parameterSource, generateCartItemMapper());
    }

    public Optional<CartItem> findById(Long id) {
        try {
            String sql = "SELECT ci.id as id, ci.quantity as quantity, "
                    + "c.id as customerId, c.username as customerUsername, c.email as customerEmail, "
                    + "c.password as customerPassword, c.address as customerAddress, "
                    + "c.phone_number as customerPhoneNumber, "
                    + "p.id as productId, p.name as productName, p.price as productPrice, "
                    + "p.image_url as productImageUrl, p.description as productDescription, "
                    + "p.deleted as productDeleted "
                    + "FROM cart_item ci "
                    + "JOIN customer c ON ci.customer_id = c.id "
                    + "JOIN product p ON ci.product_id = p.id "
                    + "WHERE ci.id = :id";

            SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, parameterSource, generateCartItemMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public RowMapper<CartItem> generateCartItemMapper() {
        return (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            int quantity = resultSet.getInt("quantity");

            Long customerId = resultSet.getLong("customerId");
            String customerUsername = resultSet.getString("customerUsername");
            String customerEmail = resultSet.getString("customerEmail");
            String customerPassword = resultSet.getString("customerPassword");
            String customerAddress = resultSet.getString("customerAddress");
            String customerPhoneNumber = resultSet.getString("customerPhoneNumber");
            Customer customer = new Customer(customerId, customerUsername, customerEmail, customerPassword,
                    customerAddress, customerPhoneNumber);

            Long productId = resultSet.getLong("productId");
            String productName = resultSet.getString("productName");
            int productPrice = resultSet.getInt("productPrice");
            String productImageUrl = resultSet.getString("productImageUrl");
            String productDescription = resultSet.getString("productDescription");

            boolean productDeleted = resultSet.getBoolean("productDeleted");
            Product product = new Product(
                    productId, productName, productPrice, productImageUrl, productDescription, productDeleted);

            return new CartItem(id, customer, product, quantity);
        };
    }

    public boolean existsByIdAndCustomerId(Long id, Long customerId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM cart_item WHERE id = :id AND customer_id = :customerId)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("customerId", customerId);

        return jdbcTemplate.queryForObject(sql, parameterSource, Boolean.class);
    }

    public void update(Long id, int quantity) {
        String sql = "UPDATE cart_item SET quantity = :quantity WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("quantity", quantity)
                .addValue("id", id);

        jdbcTemplate.update(sql, parameterSource);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM cart_item WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
        jdbcTemplate.update(sql, parameterSource);
    }
}
