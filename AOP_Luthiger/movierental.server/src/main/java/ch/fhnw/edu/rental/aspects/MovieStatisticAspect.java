package ch.fhnw.edu.rental.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.fhnw.edu.rental.model.Movie;

@Aspect
@Component
public class MovieStatisticAspect {
    private static final Logger LOG = LoggerFactory.getLogger(MovieStatisticAspect.class);
    @Autowired
    private MovieStatistic statistic;

    @Around("execution(* *..*.MovieService.saveOrUpdateMovie(..)) && args(movie)")
    public void checkSave(ProceedingJoinPoint pjp, Movie movie) throws Throwable  {
	    if (movie.getId() == null) {
	    	pjp.proceed();
	    	statistic.movieAdded();
	    	LOG.debug("Actual # of movies are {}", statistic.getNrOfMovieInstance());
    	} else {
    		pjp.proceed();
    	}
    }
    
    
    
    @After("execution(* *..*.MovieService.deleteMovie(..))")
    public void checkDelete()  {
    	statistic.movieDeleted();
    	LOG.debug("Actual # of movies are {}", statistic.getNrOfMovieInstance());
    }    
}
