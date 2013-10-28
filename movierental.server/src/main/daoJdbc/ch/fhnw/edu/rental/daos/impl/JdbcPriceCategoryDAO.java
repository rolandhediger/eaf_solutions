package ch.fhnw.edu.rental.daos.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ch.fhnw.edu.rental.daos.PriceCategoryDAO;
import ch.fhnw.edu.rental.model.PriceCategory;
import ch.fhnw.edu.rental.model.PriceCategoryChildren;
import ch.fhnw.edu.rental.model.PriceCategoryNewRelease;
import ch.fhnw.edu.rental.model.PriceCategoryRegular;

public class JdbcPriceCategoryDAO extends JdbcDaoSupport implements PriceCategoryDAO {

	@Override
	public PriceCategory getById(Long id) {
		JdbcTemplate template = getJdbcTemplate();
		return template.queryForObject(
				"select * from PRICECATEGORIES where PRICECATEGORY_ID = ?",
				new RowMapper<PriceCategory>() {
					@Override
					public PriceCategory mapRow(ResultSet rs, int row)
							throws SQLException {
						return createPriceCategory(rs);
					}
				}, id);
	}

		private PriceCategory createPriceCategory(ResultSet rs)
				throws SQLException {
			String type = rs.getString("PRICECATEGORY_TYPE");
			PriceCategory c = null;
			if("Regular".equals(type)) {
			c = new PriceCategoryRegular();
			}
			else if("Children".equals(type)) {
			c = new PriceCategoryChildren();
			}
			else if("NewRelease".equals(type)) {
			c = new PriceCategoryNewRelease();
			}
			//c.setId(rs.getLong("PRICECATEGORY_ID"));
			return c;
			}
	

	@Override
	public List<PriceCategory> getAll() {
		JdbcTemplate template = getJdbcTemplate();
		List<PriceCategory> movies = template.query("select * from PRICECATEGORIES", new RowMapper<PriceCategory>() {
			@Override
			public PriceCategory mapRow(ResultSet rs, int row)
					throws SQLException {
				return createPriceCategory(rs);
			}
		});
		return movies;
	}

	@Override
	public void saveOrUpdate(PriceCategory priceCategory) {
		JdbcTemplate template = getJdbcTemplate();
		if (priceCategory.getId() == null) {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
			.withTableName("PRICECATEGORIES");
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("PRICECATEGORY_ID", priceCategory.getId());
			parameters.put("PRICECATEGORY_TYPE",priceCategory.toString());
			Number id = insert.executeAndReturnKey(parameters);
			priceCategory.setId((Long)id);
		}
		else {
		// update in DB
			template.update(
					"UPDATE MOVIES SET PRICECATEGORY_TYPE = ?, PRICECATEGORY.ID = ?",
					priceCategory.toString(),
					priceCategory.getId());
		}
		}

	@Override
	public void delete(PriceCategory priceCategory) {
		// TODO Auto-generated method stub
	}
}
