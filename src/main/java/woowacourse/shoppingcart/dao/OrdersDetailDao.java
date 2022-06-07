package woowacourse.shoppingcart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.OrdersDetail;

import java.util.List;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;

@Repository
public class OrdersDetailDao {

    private static final String TABLE_NAME = "orders_detail";
    private static final String KEY_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrdersDetailDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
    }

    public OrdersDetail save(OrdersDetail ordersDetail) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("orders_id", ordersDetail.getOrders().getId())
                .addValue("product_id", ordersDetail.getProduct().getId())
                .addValue("quantity", ordersDetail.getQuantity());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new OrdersDetail(id, ordersDetail);
    }

    public List<OrdersDetail> findByOrdersId(Long ordersId) {
        String sql = "SELECT od.id as id, od.quantity as quantity, "
                + "o.id as orderId, "
                + "c.id as customerId, c.username as customerUsername, c.email as customerEmail, "
                + "c.password as customerPassword, c.address as customerAddress, "
                + "c.phone_number as customerPhoneNumber, "
                + "p.id as productId, p.name as productName, p.price as productPrice, "
                + "p.image_url as productImageUrl, p.description as productDescription, p.deleted as productDeleted "
                + "FROM orders_detail od "
                + "JOIN orders o ON od.orders_id = o.id "
                + "JOIN product p ON od.product_id = p.id "
                + "JOIN customer c ON o.customer_id = c.id "
                + "WHERE orders_id = :ordersId";

        SqlParameterSource parameterSource = new MapSqlParameterSource("ordersId", ordersId);
        return jdbcTemplate.query(sql, parameterSource, generateOrderDetailMapper());
    }

    private RowMapper<OrdersDetail> generateOrderDetailMapper() {
        return (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            int quantity = resultSet.getInt("quantity");

            Long orderId = resultSet.getLong("orderId");
            Long customerId = resultSet.getLong("customerId");
            String customerUsername = resultSet.getString("customerUsername");
            String customerEmail = resultSet.getString("customerEmail");
            String customerPassword = resultSet.getString("customerPassword");
            String customerAddress = resultSet.getString("customerAddress");
            String customerPhoneNumber = resultSet.getString("customerPhoneNumber");
            Customer customer = new Customer(customerId, customerUsername, customerEmail, customerPassword,
                    customerAddress, customerPhoneNumber);

            Orders orders = new Orders(orderId, customer);

            Long productId = resultSet.getLong("productId");
            String productName = resultSet.getString("productName");
            int productPrice = resultSet.getInt("productPrice");
            String productImageUrl = resultSet.getString("productImageUrl");
            String productDescription = resultSet.getString("productDescription");
            boolean deleted = resultSet.getBoolean("productDeleted");
            Product product = new Product(
                    productId, productName, productPrice, productImageUrl, productDescription, deleted);

            return new OrdersDetail(id, orders, product, quantity);
        };
    }
}
