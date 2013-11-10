package ch.fhnw.edu.rental.daos.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import ch.fhnw.edu.rental.daos.UserDAO;
import ch.fhnw.edu.rental.model.User;

public class JpaUserDAO extends AbstractManagedDAO<User> implements UserDAO {

	public User getById(Long id) {
		return em.find(User.class, id);
	}

	public void delete(User user) {
		em.remove(user);
	}

	@SuppressWarnings("unchecked")
	public List<User> getAll() {
//		List<User> users = em.createQuery("from User").getResultList();
//		return (users.isEmpty()) ? Collections.EMPTY_LIST : users;
		Query q = em.createNamedQuery("user.all");
		List<User> users = q.getResultList();
		return (users.isEmpty()) ? Collections.EMPTY_LIST : users;
	}

	public void saveOrUpdate(User user) {
		if (user.getId() != null) {
			em.merge(user);
		} else {
			em.persist(user);
		}
	}

	@SuppressWarnings("unchecked")
	public List<User> getByName(String name) {
		Query q = em.createNamedQuery("user.byName2"); // FIXME LAZY 2
		q.setParameter("name", name);
		List<User> users = q.getResultList();
		return (users.isEmpty()) ? Collections.EMPTY_LIST : users;
	}

}
