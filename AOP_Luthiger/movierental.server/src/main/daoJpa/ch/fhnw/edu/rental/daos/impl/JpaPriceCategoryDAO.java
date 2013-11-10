package ch.fhnw.edu.rental.daos.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.Query;

import ch.fhnw.edu.rental.daos.PriceCategoryDAO;
import ch.fhnw.edu.rental.model.PriceCategory;

public class JpaPriceCategoryDAO extends AbstractManagedDAO<PriceCategory> implements PriceCategoryDAO {

	@Override
	public PriceCategory getById(Long id) {
		return em.find(PriceCategory.class, id);
	}

	@Override
	public void delete(PriceCategory priceCategory) {
		em.remove(priceCategory);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<PriceCategory> getAll() {
		Query q = em.createNamedQuery("pricecategory.all");
		List<PriceCategory> prList = q.getResultList();
		return (prList.isEmpty()) ? Collections.EMPTY_LIST : prList;
	}

	@Override
	public void saveOrUpdate(PriceCategory priceCategory) {
		if (priceCategory.getId() != null) {
			em.merge(priceCategory);
		} else {
			em.persist(priceCategory);
		}
	}
}
