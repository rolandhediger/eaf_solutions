package ch.fhnw.edu.rental.daos;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public interface DAO<T> {
	T getById(Long id);
	List<T> getAll();
	@Transactional(propagation=Propagation.MANDATORY)
	void saveOrUpdate(T t);
	@Transactional(propagation=Propagation.MANDATORY)
	void delete(T t);
}
