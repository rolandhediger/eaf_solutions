package ch.fhnw.edu.rental.test;

import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import ch.fhnw.edu.rental.model.Movie;
import ch.fhnw.edu.rental.model.Rental;
import ch.fhnw.edu.rental.model.User;
import ch.fhnw.edu.rental.services.RentalService;
import ch.fhnw.edu.rental.services.UserService;
import ch.fhnw.edu.rental.test.util.TestUtil;

public class UserServiceTest {
	private ApplicationContext context;
	private UserService userService;
	private RentalService rentalService;

	@Before
	public void setUp() throws Exception {
		context = TestUtil.getSpringContext();	
		TestUtil.setSupportForLazyLoading();
		userService = (UserService) context.getBean("userService");
		rentalService = (RentalService) context.getBean("rentalService");
	}

	@After
	public void tearDown() throws Exception {
		TestUtil.closeSupportForLazyLoading();
	}

	@Test
	public void testGetAllUsers() {
		List<User> users = userService.getAllUsers();
		Assert.assertEquals(4, users.size());
	}

	@Test
	public void testSaveUser() {
		User user = new User("Tester", "Hugo");
		Assert.assertNull("id must be null as long as object is not yet saved", user.getId());
		userService.saveOrUpdateUser(user);
		Assert.assertNotNull("id must be assigned after insertion into db", user.getId());
		List<User> users = userService.getAllUsers();
		Assert.assertEquals(5, users.size());
	}

	@Test
	public void testCreateUser() {
		User user = new User("Tester", "Hugo");
		userService.saveOrUpdateUser(user);
		List<User> users = userService.getAllUsers();
		Assert.assertTrue("users list must contain new user", users.contains(user));
	}

	// Tests whether all rentals are deleted if a user is deleted
	@Test
	public void testDeleteUser() {
		List<User> users = userService.getUsersByName("Keller");
		Assert.assertEquals(1, users.size());
		User user = users.get(0);
		Assert.assertEquals(2, user.getRentals().size());
		userService.deleteUser(user);
		List<Rental> rentals = rentalService.getAllRentals();
		Assert.assertEquals(1, rentals.size());
	}

	@Test
	public void testGetUsersByName() {
		List<User> users = userService.getUsersByName("Keller");
		Assert.assertEquals(1, users.size());
		User user = users.get(0);
		Assert.assertEquals("Keller", user.getLastName());
	}
	
	@Test
	public void testDeleteRental() {
		List<User> users = userService.getUsersByName("Keller");
		Assert.assertEquals(1, users.size());
		List<Rental> rentals = rentalService.getAllRentals();
		Assert.assertEquals(3, rentals.size());
		User user = users.get(0);
		Assert.assertEquals("Keller", user.getLastName());
		Assert.assertEquals(2, user.getRentals().size());
		Rental rental = user.getRentals().get(1);
		userService.returnMovie(user, rental.getMovie());
		Assert.assertEquals(1, user.getRentals().size());
		rentals = rentalService.getAllRentals();
		Assert.assertEquals(2, rentals.size());
	}

	@Test
	public void testGetRentalsOfUserUsingLazyLoading() {
		List<User> users = userService.getUsersByName("Keller");
		Assert.assertEquals(1, users.size());
		User user = users.get(0);
		Assert.assertEquals("Keller", user.getLastName());
		List<Rental> rentals = user.getRentals();
		Assert.assertEquals(2, rentals.size());
		Rental rental = rentals.get(0);
		Movie movie = rental.getMovie();
		Assert.assertEquals("Lord of the Rings", movie.getTitle());
	}
	
	@Test
	public void testGetEmailAddress() {
		List<User> users = userService.getUsersByName("Keller");
		Assert.assertEquals(1, users.size());
		User user = users.get(0);
		Assert.assertEquals("Keller", user.getLastName());
		Assert.assertEquals("marc.keller@gmail.com", user.getEmail());

		users = userService.getUsersByName("Knecht");
		Assert.assertEquals(1, users.size());
		user = users.get(0);
		Assert.assertEquals("Knecht", user.getLastName());
	}
}
