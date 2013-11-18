package ch.fhnw.edu.rental.aspects;

import org.springframework.stereotype.Component;

import ch.fhnw.edu.rental.model.Movie;

@Component
public class MovieValidator {
	public boolean isValid(Movie movie) {
		boolean isValid = true;
		if ((movie.getTitle()==null) || (movie.getTitle().length()<=2)) {
			isValid = false;
		}
		return isValid;
	}
}
