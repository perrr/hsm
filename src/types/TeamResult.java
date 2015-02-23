package types;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class TeamResult implements Comparable<TeamResult> {

	private ArrayList<Result> results;
	private Club club;
	
	public TeamResult(Club club) {
		this.club = club;
		results = new ArrayList<Result>();
	}
	
	public void addResult(Result result) {
		results.add(result);
	}
	
	public ArrayList<Result> getResults() {
		return results;
	}
	
	public Club getClub() {
		return club;
	}
	
	public double getTotalPoints() {
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		DecimalFormat df = new DecimalFormat("#.##", otherSymbols);
		double totalPoints = 0;
		for (int i = 0; i < results.size(); i++) {
			totalPoints += (int) results.get(i).getTotalPoints();
		}
		return(Double.valueOf(df.format(totalPoints)));
	}

	@Override
	public int compareTo(TeamResult other) {
		double res = other.getTotalPoints() - getTotalPoints();
		if (res != 0)
			return (int) res * 10;
		return getClub().getName().compareToIgnoreCase(other.getClub().getName());
	}
}