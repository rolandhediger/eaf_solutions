package ch.fhnw.edu.rental.aspects;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import ch.fhnw.edu.rental.model.Movie;

/**
 * Class to track the number of movie instances over the time.
 * 
 * @author juerg.luthiger
 *
 */
@Component
public class MovieProgress {
	private List<ProgressEntry> progressList = new ArrayList<ProgressEntry>();
	
	public boolean checkForUpdateProgress(List<Movie> movies) {
		if (progressList.isEmpty() &&  !movies.isEmpty()) {
			// on start
			progressList.add(new ProgressEntry(movies.size()));
			return true;
		} else if (progressList.get(progressList.size()-1).nrEntries != movies.size()) {
			// add new entry only if the # of movie instances has changed
			progressList.add(new ProgressEntry(movies.size()));
			return true;
		}
		return false;
	}
	
	class ProgressEntry {
		private int nrEntries;
		private Date date;
		
		public ProgressEntry(int nrEntries) {
			this.nrEntries = nrEntries;
			this.date = new Date();
		}
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer().append("\n");
		for (ProgressEntry entry : progressList) {
			sb.append(String.format("%tT      %s\n", entry.date, entry.nrEntries));
		}
		return sb.toString();
	}
}
