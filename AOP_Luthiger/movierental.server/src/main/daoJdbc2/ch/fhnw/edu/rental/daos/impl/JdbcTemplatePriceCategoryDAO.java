package ch.fhnw.edu.rental.daos.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import ch.fhnw.edu.rental.daos.PriceCategoryDAO;
import ch.fhnw.edu.rental.model.PriceCategory;
import ch.fhnw.edu.rental.model.PriceCategoryChildren;
import ch.fhnw.edu.rental.model.PriceCategoryNewRelease;
import ch.fhnw.edu.rental.model.PriceCategoryRegular;
import ch.fhnw.edu.rental.service.util.ObjectUnifier;

public class JdbcTemplatePriceCategoryDAO extends JdbcDaoSupport implements PriceCategoryDAO {

	private ObjectUnifier<PriceCategory> cache = new ObjectUnifier<PriceCategory>();

	public void clearCache(){
		cache.clear();
	}

	public List<PriceCategory> getAll() {
		JdbcTemplate template = getJdbcTemplate();
		return template.query("select * from PRICECATEGORIES", 
			new RowMapper<PriceCategory>(){
				public PriceCategory mapRow(ResultSet rs, int row)
						throws SQLException {
					return createPriceCategory(rs);
				}
			}
		);
	}
	
	public PriceCategory getById(Long id){
		PriceCategory pc = cache.getObject(id);
		if(pc != null) return pc;
		JdbcTemplate template = getJdbcTemplate();
		return template.queryForObject(
			"select * from PRICECATEGORIES where PRICECATEGORY_ID = ?", 
			new RowMapper<PriceCategory>(){
				public PriceCategory mapRow(ResultSet rs, int row)
						throws SQLException {
					return createPriceCategory(rs);
				}
			},
			id
		);
	}

	public void saveOrUpdate(PriceCategory category) {
		JdbcTemplate template = getJdbcTemplate();
		String type = category.getClass().getSimpleName();
		if(category.getId() == null){	// insert
			long id = template.queryForLong("select max(PRICECATEGORY_ID) from PRICECATEGORIES") + 1;
			category.setId(id);
			template.update("INSERT INTO PRICECATEGORIES (PRICECATEGORY_ID, PRICECATEGORY_TYPE) VALUES (?,?)", id, type);
		}
		else { // update
			template.update("UPDATE PRICECATEGORIES SET PRICECATEGORY_TYPE=? where PRICECATEGORY_ID=?", type, category.getId());
		}
		// TODO saveOrUpdate is a problem is the object which is updated is already in the cache
		if(cache.getObject(category.getId()) != null && cache.getObject(category.getId()) != category){
			throw new RuntimeException("objects need to be unified");
		}
		cache.putObject(category.getId(), category);
	}

	public void delete(PriceCategory category) {
		JdbcTemplate template = getJdbcTemplate();
		template.update("delete from PRICECATEGORIES where PRICECATEGORY_ID = ?",
				category.getId()
		);
		cache.remove(category.getId());
		category.setId(null);
	}

	private PriceCategory createPriceCategory(ResultSet rs) throws SQLException {
		Long id = rs.getLong("PRICECATEGORY_ID");
		PriceCategory c = cache.getObject(id);
		if (c == null) {
			String type = rs.getString("PRICECATEGORY_TYPE");
			if (type.equals("Regular")) {
				c = new PriceCategoryRegular();
			} else if (type.equals("Children")) {
				c = new PriceCategoryChildren();
			} else if (type.equals("NewRelease")) {
				c = new PriceCategoryNewRelease();
			}
			c.setId(id);
			cache.putObject(id, c);
		}
		return c;
	}

}
