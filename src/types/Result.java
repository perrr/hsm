package types;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import logic.Jump;

public class Result implements Comparable<Result> {

	private Athlete athlete;
	private Jump jump1;
	private Jump jump2;
	
	public Result(Athlete athlete, Jump jump) {
		this.athlete = athlete;
		this.jump1 = jump;
	}
	
	public void addJump(Jump jump) {
		this.jump2 = jump;
	}
	
	public Athlete getAthlete() {
		return athlete;
	}
	
	public Jump getJump1() {
		return jump1;
	}
	
	public Jump getJump2() {
		return jump2;
	}
	
	public double getTotalPoints() {
		if (jump2 != null) {
			DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
			DecimalFormat df = new DecimalFormat("#.##", otherSymbols);
			return(Double.valueOf(df.format(jump1.getPoints() + jump2.getPoints())));
		}
		return jump1.getPoints();
	}

	@Override
	public int compareTo(Result other) {
		double res = other.getTotalPoints() - getTotalPoints();
		if (res != 0) {
			return (int) (Math.round(res * 10));
		}
		int resName = athlete.getLastName().compareToIgnoreCase(other.getAthlete().getLastName());
		if (resName != 0)
			return resName;
		return athlete.getFirstName().compareTo(other.getAthlete().getFirstName());
	}
}