package ch.fhnw.edu.rental.service;

import java.util.List;

import ch.fhnw.edu.rental.dto.MovieDTO;

public interface RmiMovieService {
	List<MovieDTO> getAllMovies() throws RentalServiceException;
	MovieDTO getMovieById(Long id) throws RentalServiceException;
	Long saveOrUpdateMovie(MovieDTO movie) throws RentalServiceException;
	void deleteMovie(Long id) throws RentalServiceException;
}
