package ch.fhnw.edu.rental.aspects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.edu.rental.services.MovieService;

@Component
public class MovieStatistic {
	private int nrOfMovieInstance = 0;
	private boolean isInitialized = false;
	@Autowired
	private MovieService movieService;
	
	public void movieAdded() {
		if (! isInitialized) {
			init();
		} else {
			nrOfMovieInstance++;
		}
	}
	
	public void movieDeleted() {
		if (! isInitialized) {
			init();
		} else {
			nrOfMovieInstance--;
		}
	}
	
	public int getNrOfMovieInstance() {
		return nrOfMovieInstance;
	}
	
	public void init() {
		nrOfMovieInstance = movieService.getAllMovies().size();
		isInitialized = true;
	}
}
