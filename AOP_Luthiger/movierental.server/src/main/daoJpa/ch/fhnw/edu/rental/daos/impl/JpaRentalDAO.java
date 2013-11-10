package ch.fhnw.edu.rental.daos.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import ch.fhnw.edu.rental.daos.RentalDAO;
import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.Rental;
import ch.fhnw.edu.rental.model.User;

public class JpaRentalDAO extends AbstractManagedDAO<Rental> implements RentalDAO {

	@Override
	public Rental getById(Long id) {
		return em.find(Rental.class, id);
	}

	@Override
	public void delete(Rental rental) {
		em.remove(rental);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rental> getAll() {
		Query q = em.createNamedQuery("rental.all");
		List<Movie> rentals = q.getResultList();
		return (rentals.isEmpty()) ? Collections.EMPTY_LIST : rentals;
	}

	public void saveOrUpdate(Rental rental) {
		if (rental.getId() != null) {
			em.merge(rental);
		} else {
			em.persist(rental);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Rental> getRentalsByUser(User user) {
		Query q = em.createNamedQuery("rental.byUser");
		q.setParameter("user", user);
		List<Movie> rentals = q.getResultList();
		return (rentals.isEmpty()) ? Collections.EMPTY_LIST : rentals;
	}
}
