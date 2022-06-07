package woowacourse.shoppingcart.dao;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.Orders;
import woowacourse.shoppingcart.domain.customer.Customer;

@Repository
public class OrdersDao {

    private static final String TABLE_NAME = "orders";
    private static final String KEY_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public OrdersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
    }

    public Orders save(Orders orders) {
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("customer_id", orders.getCustomer().getId());

        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Orders(id, orders);
    }

    public Optional<Orders> findById(Long id) {
        try {
            String sql = "SELECT o.id as id, "
                    + "c.id as customerId, c.username as customerUsername, c.email as customerEmail, "
                    + "c.password as customerPassword, c.address as customerAddress, "
                    + "c.phone_number as customerPhoneNumber "
                    + "FROM orders o "
                    + "JOIN customer c ON o.customer_id = c.id "
                    + "WHERE o.id = :id";

            SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, parameterSource, generateOrdersMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Orders> findByCustomerId(Long customerId) {
        String sql = "SELECT o.id as id, "
                + "c.id as customerId, c.username as customerUsername, c.email as customerEmail, "
                + "c.password as customerPassword, c.address as customerAddress, "
                + "c.phone_number as customerPhoneNumber "
                + "FROM orders o "
                + "JOIN customer c ON o.customer_id = c.id "
                + "WHERE o.customer_id = :customerId";

        SqlParameterSource parameterSource = new MapSqlParameterSource("customerId", customerId);
        return jdbcTemplate.query(sql, parameterSource, generateOrdersMapper());
    }

    public RowMapper<Orders> generateOrdersMapper() {
        return (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");

            Long customerId = resultSet.getLong("customerId");
            String customerUsername = resultSet.getString("customerUsername");
            String customerEmail = resultSet.getString("customerEmail");
            String customerPassword = resultSet.getString("customerPassword");
            String customerAddress = resultSet.getString("customerAddress");
            String customerPhoneNumber = resultSet.getString("customerPhoneNumber");
            Customer customer = new Customer(customerId, customerUsername, customerEmail, customerPassword,
                    customerAddress, customerPhoneNumber);

            return new Orders(id, customer);
        };
    }

    public boolean existsByIdAndCustomerId(Long id, Long customerId) {
        String sql = "SELECT EXISTS (SELECT 1 FROM orders WHERE id = :id AND customer_id = :customerId)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", id)
                .addValue("customerId", customerId);
        return jdbcTemplate.queryForObject(sql, parameterSource, Boolean.class);
    }
}
