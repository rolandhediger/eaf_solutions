package ch.fhnw.edu.rental.services;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.PriceCategory;
import ch.fhnw.edu.rental.service.RentalServiceException;
@Transactional
public interface MovieService {
	@Transactional(propagation=Propagation.SUPPORTS)
	public Movie getMovieById(Long id) throws RentalServiceException;
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<Movie> getAllMovies() throws RentalServiceException;
	@Transactional(propagation=Propagation.SUPPORTS)
	public List<Movie> getMoviesByTitle(String title) throws RentalServiceException;
	
	public void saveOrUpdateMovie(Movie movie) throws RentalServiceException;
	
	public void deleteMovie(Movie movie) throws RentalServiceException;
	
	public List<PriceCategory> getAllPriceCategories() throws RentalServiceException;
}
