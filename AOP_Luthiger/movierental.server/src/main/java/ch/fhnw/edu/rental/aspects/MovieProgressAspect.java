package ch.fhnw.edu.rental.aspects;

import java.util.List;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.edu.rental.model.Movie;

@Aspect
@Component
public class MovieProgressAspect {
	private static final Logger LOG = LoggerFactory
			.getLogger(MovieProgressAspect.class);

	@Autowired
	private MovieProgress movieProgress;
	@AfterReturning(pointcut="execution( * ch.fhnw.edu.rental.services.MovieService.getAllMovies())",returning="movielist")
	public void checkMovieList(Object movielist) {
		if (movielist instanceof List<?>){
			List<Movie> movies = (List<Movie>)movielist;
			boolean isUpdated = movieProgress.checkForUpdateProgress(movies);
			if (isUpdated){
				LOG.debug(movieProgress.toString());
			}
		}
	}
}

