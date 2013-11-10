package ch.fhnw.edu.rental.services;

import java.util.Date;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import ch.fhnw.edu.rental.dto.MovieDTO;
import ch.fhnw.edu.rental.dto.RentalDTO;
import ch.fhnw.edu.rental.dto.UserDTO;
import ch.fhnw.edu.rental.service.RmiMovieService;
import ch.fhnw.edu.rental.service.RmiRentalService;
import ch.fhnw.edu.rental.service.RmiUserService;

public class BusinessLogicRemote implements BusinessLogic {
	
	private RmiMovieService movieService;
	private RmiUserService userService;
	private RmiRentalService rentalService;

	public BusinessLogicRemote() throws Exception {
		movieService = getMovieService();
		userService = getUserService();
		rentalService = getRentalService();
	}
	
	
	public String getUserLastName(Long id){
		return getUser(id).getLastName();
	}
	public String getUserFirstName(Long id){
		return getUser(id).getFirstName();
	}
	public int getUserRentalsSize(Long id){
		return getUser(id).getRentalIds().length;
	}
	
	public String getMovieTitle(Long id){
		return getMovie(id).getTitle();
	}
	public String getMoviePriceCategory(Long id){
		return getMovie(id).getPriceCategory().toString();
	}
	public Date getMovieReleaseDate(Long id){
		return getMovie(id).getReleaseDate();
	}
	public boolean getMovieIsRented(Long id){
		return getMovie(id).isRented();
	}
	
	
	private UserDTO user;
	private UserDTO getUser(Long id){
		if(user == null || !user.getId().equals(id)){
			user = userService.getUserById(id);
		}
		return user;
	}
	
	private MovieDTO movie;
	private MovieDTO getMovie(Long id){
		if(movie == null || !movie.getId().equals(id)){
			movie = movieService.getMovieById(id);
		}
		return movie;
	}
	
//	private RentalDTO rental;
//	private RentalDTO getRental(Long id){
//		if(rental == null || !rental.getId().equals(id)){
//			rental = rentalService.getRentalById(id);
//		}
//		return rental;
//	}
	
	void invalidateCache(){
		movie = null;
		user = null;
	}
	
	public void removeRental(Long rentalId){
		rentalService.deleteRental(rentalId);
		invalidateCache();
	}
	
	public void deleteUser(Long userId) {
		userService.deleteUser(userId);
		invalidateCache();
	}

	public void updateUser(Long userId, String lastName, String firstName) {
		UserDTO currUser = getUser(userId);
		currUser = new UserDTO(userId, lastName, firstName, currUser.getEmail(), currUser.getRentalIds());
		userService.saveOrUpdateUser(currUser);
		invalidateCache();
	}

	public Long createUser(String lastName, String firstName) {
		UserDTO currUser = new UserDTO(null, lastName, firstName, null, null);
		Long id = userService.saveOrUpdateUser(currUser);
		invalidateCache();
		return id;
	}
	
	public void deleteMovie(Long movieId) {
		movieService.deleteMovie(movieId);
		invalidateCache();
	}

	public Long createMovie(String movieTitle, Date date, String category) {
		MovieDTO currMovie = new MovieDTO(null, movieTitle, false, date, category);
		Long id = movieService.saveOrUpdateMovie(currMovie);
		invalidateCache();
		return id;
	}

	public void updateMovie(Long movieId, String movieTitle, Date date,	String category) {
		MovieDTO movie = getMovie(movieId);
		movie = new MovieDTO(movieId, movieTitle, movie.isRented(), date, category);
		movieService.saveOrUpdateMovie(movie);
		invalidateCache();
	}

	public void createRental(Long movieId, Long userId, Integer rentalDays) {
		rentalService.rentMovie(userId, movieId, rentalDays);
// old implementeation based on the old remote interface
//		MovieDTO movie = getMovie(movieId);
//		UserDTO user = getUser(userId);
//
//		Date now = Calendar.getInstance().getTime();
//		RentalDTO rental = new RentalDTO(null, now, rentalDays, 0, user.getId(), movie.getId(), 0);
//		Long id = rentalService.saveOrUpdateRental(rental);
		
		invalidateCache();
	}

	public void visitUsers(UserVisitor visitor) {
		for(UserDTO u : userService.getAllUsers()){
			visitor.visit(u.getId(), u.getLastName(), u.getFirstName());
		}
	}

	public void visitMovies(MovieVisitor visitor) {
		for(MovieDTO m : movieService.getAllMovies()){
			visitor.visit(m.getId(), m.getTitle(), m.getReleaseDate(), m.isRented(), m.getPriceCategory());
		}
	}

	public void visitRentals(RentalVisitor visitor) {
		for(RentalDTO r : rentalService.getAllRentals()){
			UserDTO user = userService.getUserById(r.getUserId());
			MovieDTO movie = movieService.getMovieById(r.getMovieId());
			visitor.visit(r.getId(), r.getRentalDays(), r.getRentalDate(), user.getLastName(), user.getFirstName(), movie.getTitle(), r.getRemainingDays(), r.getRentalFee());
		}
	}

	public void visitRentalsOfUser(Long userId, RentalVisitor visitor) {
		UserDTO user = userService.getUserById(userId);
		for(Long id : user.getRentalIds()){
			RentalDTO r = rentalService.getRentalById(id);
			MovieDTO movie = movieService.getMovieById(r.getMovieId());
			visitor.visit(r.getId(), r.getRentalDays(), r.getRentalDate(), user.getLastName(), user.getFirstName(), movie.getTitle(), r.getRemainingDays(), r.getRentalFee());
		}
	}

	private static ApplicationContext context;
	
	private static RmiMovieService getMovieService() {
		return (RmiMovieService) context.getBean("movieService");
	}
	
	private static RmiUserService getUserService() {
		return (RmiUserService) context.getBean("userService");
	}	
	
	private static RmiRentalService getRentalService() {
		return (RmiRentalService) context.getBean("rentalService");
	}	

	static {
    	context = new ClassPathXmlApplicationContext("applicationRemote.xml");
	}
	
}
