package woowacourse.shoppingcart.dao;

import java.util.List;
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
import woowacourse.shoppingcart.domain.Product;

@Repository
public class ProductDao {

    public static final String TABLE_NAME = "product";
    public static final String KEY_NAME = "id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    public ProductDao(JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
        this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(TABLE_NAME)
                .usingGeneratedKeyColumns(KEY_NAME);
    }

    public Product save(Product product) {
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(product);
        Long id = simpleJdbcInsert.executeAndReturnKey(parameterSource).longValue();
        return new Product(id, product);
    }

    public Optional<Product> findById(Long id) {
        try {
            String sql = "SELECT id, name, price, image_url, description, deleted FROM product WHERE id = :id";
            SqlParameterSource parameterSource = new MapSqlParameterSource("id", id);
            return Optional.ofNullable(
                    namedParameterJdbcTemplate.queryForObject(sql, parameterSource, generateProductMapper()));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Product> findAll() {
        String sql = "SELECT id, name, price, image_url, description, deleted FROM product WHERE deleted = false";
        return namedParameterJdbcTemplate.query(sql, generateProductMapper());
    }

    private RowMapper<Product> generateProductMapper() {
        return (resultSet, rowNum) -> new Product(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getInt("price"),
                resultSet.getString("image_url"),
                resultSet.getString("description"),
                resultSet.getBoolean("deleted")
        );
    }

    public void delete(Product product) {
        String sql = "UPDATE product SET deleted = true WHERE id = :id";
        SqlParameterSource parameterSource = new MapSqlParameterSource("id", product.getId());
        namedParameterJdbcTemplate.update(sql, parameterSource);
    }
}
