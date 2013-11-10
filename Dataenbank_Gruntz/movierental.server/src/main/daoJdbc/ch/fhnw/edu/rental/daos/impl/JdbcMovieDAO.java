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
import ch.fhnw.edu.rental.daos.PriceCategoryDAO;
import ch.fhnw.edu.rental.model.Movie;

public class JdbcMovieDAO extends JdbcDaoSupport implements MovieDAO {

	private PriceCategoryDAO priceCategoryDAO;

	public void setPriceCategoryDAO(PriceCategoryDAO priceCategoryDAO) {
		this.priceCategoryDAO = priceCategoryDAO;
	}

	@Override
	public Movie getById(Long id) {
		JdbcTemplate template = getJdbcTemplate();
		return template.queryForObject(
				"select * from MOVIES where MOVIE_ID = ?",
				new RowMapper<Movie>() {
					@Override
					public Movie mapRow(ResultSet rs, int row)
							throws SQLException {
						return createMovie(rs);
					}
				}, id);
	}

	@Override
	public List<Movie> getByTitle(String name) {
		JdbcTemplate template = getJdbcTemplate();
		List<Movie> movies = template.query("select * from MOVIES where MOVIE_TITLE = ?", new RowMapper<Movie>() {
			@Override
			public Movie mapRow(ResultSet rs, int row)
					throws SQLException {
				return createMovie(rs);
			}
		},name);
		return movies;
		}

	@Override
	public List<Movie> getAll() {
		JdbcTemplate template = getJdbcTemplate();
		List<Movie> movies = template.query("select * from MOVIES", new RowMapper<Movie>() {
			@Override
			public Movie mapRow(ResultSet rs, int row)
					throws SQLException {
				return createMovie(rs);
			}
		});
		return movies;
	}

	@Override
	public void saveOrUpdate(Movie movie) {
	JdbcTemplate template = getJdbcTemplate();
	if (movie.getId() == null) {
		SimpleJdbcInsert insert = new SimpleJdbcInsert(getDataSource())
		.withTableName("MOVIES")
		.usingGeneratedKeyColumns("MOVIE_ID");
		Map<String,Object> parameters = new HashMap<String, Object>();
		parameters.put("MOVIE_TITLE", movie.getTitle());
		parameters.put("MOVIE_RELEASEDATE", movie.getReleaseDate());
		parameters.put("MOVIE_RENTED", movie.isRented());
		parameters.put("PRICECATEGORY_FK",
		movie.getPriceCategory().getId());
		Number id = insert.executeAndReturnKey(parameters);
		movie.setId((Long)id);
	}
	else {
	// update in DB
		template.update(
				"UPDATE MOVIES SET MOVIE_TITLE = ?, MOVIE_RENTED = ?,MOVIE_RELEASEDATE = ?,PRICECATEGORY_FK = ? WHERE MOVIE_ID = ?",
				movie.getTitle(),
				movie.isRented(),
				movie.getReleaseDate(),
				movie.getPriceCategory().getId(),
				movie.getId());
	}
	}

	@Override
		public void delete(Movie movie) {
		JdbcTemplate template = getJdbcTemplate();
		template.update(
		"delete from MOVIES where MOVIE_ID = ?", movie.getId());
		movie.setId(null);
		}

	private Movie createMovie(ResultSet rs) throws SQLException {
		long priceCategory = rs.getLong("PRICECATEGORY_FK");
		Movie m = new Movie(rs.getString("MOVIE_TITLE"),
				rs.getDate("MOVIE_RELEASEDATE"),
				priceCategoryDAO.getById(priceCategory));
		m.setId(rs.getLong("MOVIE_ID"));
		m.setRented(rs.getBoolean("MOVIE_RENTED"));
		return m;
	}

}
