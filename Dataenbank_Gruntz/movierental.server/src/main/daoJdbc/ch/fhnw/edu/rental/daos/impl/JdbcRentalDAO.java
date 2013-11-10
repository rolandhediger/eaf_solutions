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

import ch.fhnw.edu.rental.daos.MovieDAO;
import ch.fhnw.edu.rental.daos.RentalDAO;
import ch.fhnw.edu.rental.daos.UserDAO;
import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.Rental;
import ch.fhnw.edu.rental.model.User;

public class JdbcRentalDAO extends JdbcDaoSupport implements RentalDAO {

	private MovieDAO movieDAO;
	private UserDAO userDAO;

	public void setMovieDAO(MovieDAO movieDAO) {
		this.movieDAO = movieDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public Rental getById(Long id) {
		JdbcTemplate template = getJdbcTemplate();
		return template.queryForObject(
				"select * from RENTALS where RENTAL_ID = ?",
				new RowMapper<Rental>() {
					@Override
					public Rental mapRow(ResultSet rs, int row)
							throws SQLException {
						return createRental(rs);
					}
				}, id);
	}

	@Override
	public List<Rental> getAll() {
		JdbcTemplate template = getJdbcTemplate();
		List<Rental> movies = template.query("select * from RENTALS", new RowMapper<Rental>() {
			@Override
			public Rental mapRow(ResultSet rs, int row)
					throws SQLException {
				return createRental(rs);
			}
		});
		return movies;
	}

	@Override
	public List<Rental> getRentalsByUser(User user) {
		JdbcTemplate template = getJdbcTemplate();
		List<Rental> rentals = template.query("select * from RENTALS where USER_ID = ? ", new RowMapper<Rental>() {
			@Override
			public Rental mapRow(ResultSet rs, int row)
					throws SQLException {
				return createRental(rs);
			}
		},user.getId());
		return rentals;
	}

	@Override
	public void saveOrUpdate(Rental rental) {
		JdbcTemplate template = getJdbcTemplate();
		if (rental.getId() == null) {
			SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
			.withTableName("RENTALS")
			.usingGeneratedKeyColumns("RENTAL_ID");
			Map<String,Object> parameters = new HashMap<String, Object>();
			parameters.put("RENTAL_RENTALDATE",rental.getRentalDate());
			parameters.put("RENTAL_RENTALDAYS", rental.getRentalDays());
			parameters.put("USER_ID",rental.getUser().getId());
			parameters.put("MOVIE_ID", rental.getMovie().getId());
			Number id = insert.executeAndReturnKey(parameters);
			rental.setId((Long)id);
		}
		else {
		// update in DB
			template.update(
					"UPDATE MOVIES SET RENTAL_RENTALDATE = ?, RENTAL_RENTALDAYS = ?,USER_ID = ?,MOVIE_ID = ? WHERE RENTAL_ID = ?",
					rental.getRentalDate(),
					rental.getRentalDays(),
					rental.getUser().getId(),
					rental.getMovie().getId(),
					rental.getId());
		}
		}

	@Override
	public void delete(Rental rental) {
		JdbcTemplate template = getJdbcTemplate();
		template.update(
		"delete from RENTALS where RENTAL_ID = ?", rental.getId());
		rental.setId(null);
	}

	private Rental createRental(ResultSet rs) throws SQLException {
		User user = userDAO.getById(rs.getLong("USER_ID"));
		return createRental(rs, user);
	}

	private Rental createRental(ResultSet rs, User u) throws SQLException {
		Movie m = movieDAO.getById(rs.getLong("MOVIE_ID"));
		if (!m.isRented())
			throw new RuntimeException("movie must be rented if read from DB");
		m.setRented(false);
		Rental r = new Rental(u, m, rs.getInt("RENTAL_RENTALDAYS"));
		m.setRented(true);
		r.setId(rs.getLong("RENTAL_ID"));
		return r;
	}

}
