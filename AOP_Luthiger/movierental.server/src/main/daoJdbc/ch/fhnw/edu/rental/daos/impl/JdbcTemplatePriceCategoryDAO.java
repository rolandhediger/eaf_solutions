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

public class JdbcTemplatePriceCategoryDAO extends JdbcDaoSupport implements PriceCategoryDAO {

	@Override
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
	
	@Override
	public PriceCategory getById(Long id){
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

	@Override
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
	}

	@Override
	public void delete(PriceCategory category) {
		JdbcTemplate template = getJdbcTemplate();
		template.update("delete from PRICECATEGORIES where PRICECATEGORY_ID = ?",
				category.getId()
		);
		category.setId(null);
	}

	private PriceCategory createPriceCategory(ResultSet rs) throws SQLException {
		String type = rs.getString("PRICECATEGORY_TYPE");
		PriceCategory c = null;
		switch(type) {
		case "Regular" : c = new PriceCategoryRegular();
		case "Children" :  c = new PriceCategoryChildren();
		case "NewRelease" : c = new PriceCategoryNewRelease();
		}
		c.setId(rs.getLong("PRICECATEGORY_ID"));
		return c;
	}

}
