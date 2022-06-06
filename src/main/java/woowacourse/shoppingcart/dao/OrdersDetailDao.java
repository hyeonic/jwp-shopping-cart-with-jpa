package woowacourse.shoppingcart.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.OrderDetail;

import java.sql.PreparedStatement;
import java.util.List;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.Product;
import woowacourse.shoppingcart.domain.customer.Customer;

@Repository
public class OrdersDetailDao {

    private final JdbcTemplate jdbcTemplate;

    public OrdersDetailDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Long addOrdersDetail(final Long ordersId, final Long productId, final int quantity) {
        final String sql = "INSERT INTO orders_detail (orders_id, product_id, quantity) VALUES (?, ?, ?)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement preparedStatement = con.prepareStatement(sql, new String[]{"id"});
            preparedStatement.setLong(1, ordersId);
            preparedStatement.setLong(2, productId);
            preparedStatement.setLong(3, quantity);
            return preparedStatement;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<OrderDetail> findOrdersDetailsByOrderId(final Long orderId) {
        String sql = "SELECT od.id as id, od.quantity as quantity, "
                + "o.id as orderId, "
                + "c.id as customerId, c.username as customerUsername, c.email as customerEmail, "
                + "c.password as customerPassword, c.address as customerAddress, "
                + "c.phone_number as customerPhoneNumber, "
                + "p.id as productId, p.name as productName, p.price as productPrice, "
                + "p.image_url as productImageUrl, p.deleted as productDeleted "
                + "FROM orders_detail od "
                + "JOIN orders o ON od.orders_id = o.id "
                + "JOIN product p ON od.product_id = p.id "
                + "JOIN customer c ON o.customer_id = c.id "
                + "WHERE orders_id = ?";

        return jdbcTemplate.query(sql, generateOrderDetailMapper(), orderId);
    }

    public RowMapper<OrderDetail> generateOrderDetailMapper() {
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
            boolean deleted = resultSet.getBoolean("productDeleted");
            Product product = new Product(productId, productName, productPrice, productImageUrl, deleted);

            return new OrderDetail(id, orders, product, quantity);
        };
    }
}
