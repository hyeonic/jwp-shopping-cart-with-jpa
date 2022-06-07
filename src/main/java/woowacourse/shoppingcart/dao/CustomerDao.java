package woowacourse.shoppingcart.dao;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import woowacourse.shoppingcart.domain.customer.Customer;

@Repository
public class CustomerDao {

    private static final String TABLE_NAME = "customer";
    private static final String KEY_NAME = "id";

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public CustomerDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
    }

    public Customer save(Customer customer) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(customer);
        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Customer(id, customer);
    }

    public Optional<Customer> findById(Long id) {
        try {
            String sql = "SELECT id, username, email, password, address, phone_number "
                    + "FROM customer "
                    + "WHERE id = :id";

            SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, parameterSource, generateCustomerMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Customer> findByUsername(String username) {
        try {
            String sql = "SELECT id, username, email, password, address, phone_number "
                    + "FROM customer "
                    + "WHERE username = :username";
            SqlParameterSource parameterSource = new MapSqlParameterSource("username", username);
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, parameterSource, generateCustomerMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    private RowMapper<Customer> generateCustomerMapper() {
        return (resultSet, rowNum) ->
                new Customer(
                        resultSet.getLong("id"),
                        resultSet.getString("username"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getString("address"),
                        resultSet.getString("phone_number")
                );
    }

    public boolean existsByUsernameAndPassword(String username, String password) {
        String sql = "SELECT EXISTS (SELECT 1 FROM customer WHERE username = :username AND password = :password)";
        SqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("username", username)
                .addValue("password", password);

        return jdbcTemplate.queryForObject(sql, parameterSource, Boolean.class);
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT EXISTS (SELECT 1 FROM customer WHERE username = :username)";
        SqlParameterSource parameterSource = new MapSqlParameterSource("username", username);
        return jdbcTemplate.queryForObject(sql, parameterSource, Boolean.class);
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT EXISTS (SELECT 1 FROM customer WHERE email = :email)";
        SqlParameterSource parameterSource = new MapSqlParameterSource("email", email);
        return jdbcTemplate.queryForObject(sql, parameterSource, Boolean.class);
    }

    public void update(Customer customer) {
        String sql = "UPDATE customer SET address = :address, phone_number = :phoneNumber WHERE username = :username";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(customer);
        jdbcTemplate.update(sql, parameterSource);
    }

    public void delete(Customer customer) {
        String sql = "DELETE FROM customer WHERE username = :username";
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(customer);
        jdbcTemplate.update(sql, parameterSource);
    }
}
