package ch.fhnw.edu.rental.daos.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import ch.fhnw.edu.rental.daos.MovieDAO;
import ch.fhnw.edu.rental.model.Movie;

public class JpaMovieDAO extends AbstractManagedDAO<Movie> implements MovieDAO {

	@Override
	public Movie getById(Long id) {
		return em.find(Movie.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Movie> getByTitle(String title) {
// without named query
//		List<Movie> movies = em.createQuery("from Movie m where m.title = :title")
//			.setParameter("title", title)
//			.getResultList();
		Query q = em.createNamedQuery("movie.byTitle");
		q.setParameter("title", title);
		List<Movie> movies = q.getResultList();
		return (movies.isEmpty()) ? Collections.EMPTY_LIST : movies;
	}

	@Override
	public void delete(Movie movie) {
		em.remove(movie);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Movie> getAll() {
		Query q = em.createNamedQuery("movie.all");
		List<Movie> movies = q.getResultList();
		return (movies.isEmpty()) ? Collections.EMPTY_LIST : movies;
	}

	@Override
	public void saveOrUpdate(Movie movie) {
		if (movie.getId() != null) {
			em.merge(movie);
		} else {
			em.persist(movie);
		}
	}

}
