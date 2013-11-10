package ch.fhnw.edu.rental.test;

import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.Rental;
import ch.fhnw.edu.rental.model.User;
import ch.fhnw.edu.rental.services.MovieService;
import ch.fhnw.edu.rental.services.RentalService;
import ch.fhnw.edu.rental.services.UserService;
import ch.fhnw.edu.rental.test.util.TestUtil;

public class RentalServiceTest {
	private ApplicationContext context;

	@Before
	public void setUp() throws Exception {
		// get spring context
		context = TestUtil.getSpringContext();		
		TestUtil.setSupportForLazyLoading();
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.closeSupportForLazyLoading();		
	}

	@Test
	public void testRentMovie() {
		MovieService  movieService  = (MovieService) context.getBean("movieService");
		UserService   userService   = (UserService)  context.getBean("userService");

		User u  = userService.getAllUsers().get(0);
		int size = u.getRentals().size();
		
		// search a movie which is not yet rented
		Movie m = null;
		List<Movie> movies = movieService.getAllMovies();
		for(Movie mm : movies) 
			if(!mm.isRented()) m = mm;
			
		userService.rentMovie(u, m, 10);
		Assert.assertEquals(size+1, u.getRentals().size());
		
		// check whether this movie is also assigned to u in the DB
		for(User uu : userService.getAllUsers())
			if(u.getId().equals(uu.getId())) u = uu;
		Assert.assertEquals(size+1, u.getRentals().size());
	}

	@Test
	public void testGetAllRentals() {
		RentalService rentalService = (RentalService) context.getBean("rentalService");
		List<Rental> rentals = rentalService.getAllRentals();
		Assert.assertEquals(2, rentals.size());
	}
	
	@Test
	public void testGetAllRentalInfos() {
		RentalService rentalService = (RentalService) context.getBean("rentalService");
		List<Rental> rentals = rentalService.getAllRentals();
		Assert.assertEquals(2, rentals.size());
		Rental rental = rentals.get(0);
		Assert.assertNotNull(rental.getUser());
		Assert.assertNotNull(rental.getMovie());
		Assert.assertNotNull(rental.getUser().getEmail());
		Assert.assertNotNull(rental.getMovie().getTitle());
	}	

	@Test
	public void testGetAllRentalsByUser() {
		UserService userService = (UserService) context.getBean("userService");
		List<User> users = userService.getUsersByName("Keller");
		Assert.assertEquals(1, users.size());
		User user = users.get(0);
		List<Rental> rentals = user.getRentals();
		Assert.assertEquals(1, rentals.size());
	}

	@Test
	public void testCalcRemainingDaysOfRental() {
		RentalService rentalService = (RentalService) context.getBean("rentalService");
		Rental rental = rentalService.getRentalById(new Long(1));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(rental.getRentalDate());
		calendar.add(Calendar.DAY_OF_YEAR, 5);	
		int days = rentalService.calcRemainingDaysOfRental(rental, calendar.getTime());
		Assert.assertEquals(2, days);
	}
	
	@Test
	public void testReadRental() {
		RentalService rentalService = (RentalService) context.getBean("rentalService");
		Rental rental = rentalService.getRentalById(1L);
		User user = rental.getUser();
		Assert.assertEquals("Keller", user.getLastName());
		Assert.assertEquals(1, user.getRentals().size());
	}
	
	@Test
	public void testDeleteRental() {
		RentalService rentalService = (RentalService) context.getBean("rentalService");
		Rental rental = rentalService.getRentalById(1L);
		User user = rental.getUser();
		Assert.assertEquals("Keller", user.getLastName());
		Assert.assertEquals(1, user.getRentals().size());
		rentalService.deleteRental(rental);
		List<Rental> rentals = rentalService.getAllRentals();
		Assert.assertEquals(1, rentals.size());
		UserService userService= (UserService) context.getBean("userService");
		userService.saveOrUpdateUser(user);
		user = userService.getUserById(user.getId());
		Assert.assertEquals(0, user.getRentals().size());
	}
}
