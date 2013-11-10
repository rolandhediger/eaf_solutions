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

import ch.fhnw.edu.rental.daos.RentalDAO;
import ch.fhnw.edu.rental.daos.UserDAO;
import ch.fhnw.edu.rental.model.Rental;
import ch.fhnw.edu.rental.model.User;

public class JdbcUserDAO extends JdbcDaoSupport implements UserDAO {

	private RentalDAO rentalDAO;

	public void setRentalDAO(RentalDAO rentalDAO) {
		this.rentalDAO = rentalDAO;
	}

	@Override
	public User getById(Long id) {
		JdbcTemplate template = getJdbcTemplate();
		return template.queryForObject("select * from USERS where USER_ID = ?",
				new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int row)
							throws SQLException {
						return createUser(rs);
					}
				}, id);
	}

	private User createUser(ResultSet rs) throws SQLException {
		User u = new User(rs.getString("USER_NAME"),
				rs.getString("USER_FIRSTNAME"));
		u.setEmail(rs.getString("USER_EMAIL"));
		u.setId(rs.getLong("USER_ID"));
		List<Rental> rentals = rentalDAO.getRentalsByUser(u);
		u.setRentals(rentals);
		return u;

	}

	@Override
	public List<User> getAll() {
		JdbcTemplate template = getJdbcTemplate();
		List<User> users = template.query("select * from USERS",
				new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int row)
							throws SQLException {
						return createUser(rs);
					}
				});
		return users;
	}

	@Override
	public List<User> getByName(String name) {
		JdbcTemplate template = getJdbcTemplate();
		List<User> users = template.query(
				"select * from USERS where USER_NAME = ?",
				new RowMapper<User>() {
					@Override
					public User mapRow(ResultSet rs, int row)
							throws SQLException {
						return createUser(rs);
					}
				}, name);
		return users;
	}

	@Override
	public void saveOrUpdate(User user) {
		JdbcTemplate template = getJdbcTemplate();
		if (user.getId() == null) {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
			.withTableName("USERS")
			.usingGeneratedKeyColumns("USER_ID");
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("USER_NAME", user.getLastName());
			parameters.put("USER_FIRSTNAME", user.getFirstName());
			parameters.put("USER_EMAIL",user.getEmail());
			Number id = insert.executeAndReturnKey(parameters);
			user.setId((Long)id);
		}
		else {
		// update in DB
			template.update(
					"UPDATE USERS SET USER_NAME = ?, USER_FIRSTNAME = ?,USER_EMAIL = ? WHERE USER_ID = ?",
					user.getLastName(),
					user.getFirstName(),
					user.getEmail(),
					user.getId());
		}
		}

	@Override
	public void delete(User user) {
	JdbcTemplate template = getJdbcTemplate();
	for(Rental r : user.getRentals()){
	rentalDAO.delete(r);
	}
	template.update("delete from USERS where USER_ID = ?",
	user.getId());
	user.setId(null);
	}

}
