package logic;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import database.Connect;
import types.Athlete;
import types.Hill;
import types.Nation;
import util.Util;

public class Jump {

	private double length;
	private double marks;
	private double points;
	private String suffix;
	
	public Jump(Athlete athlete, Hill hill) {
		Util util = new Util();
		double formCoefficientI = 0.85 + athlete.getForm() * 0.003;
		double formCoefficientII = 0.8 + athlete.getForm() * 0.004;
		double formCoefficientIII = 0.95 + athlete.getForm() * 0.001;
		double Atemp = Math.pow((athlete.getExperience() * 0.001 + athlete.getBalance()
				* 0.009) * formCoefficientI, 1/3);
		double Btemp = Math.pow((athlete.getExperience() * 0.001 + athlete.getAgility()
				* 0.009) * formCoefficientII, 1/3);
		double Ctemp = Math.pow((athlete.getExperience() * 0.0005 + athlete.getBalance()
				* 0.0015 + athlete.getFlightTechnique() * 0.008) * formCoefficientII, 1/3);
		double Dtemp = Math.pow((athlete.getExperience() * 0.001 + athlete.getBalance()
				* 0.0025 + athlete.getLandingTechnique() * 0.0065) * formCoefficientIII,
				1/3);
		double Etemp = Math.pow((athlete.getExperience() * 0.004 + athlete.getBalance()
				* 0.006) * formCoefficientIII, 1/3);
		double timingCoefficient = 1 - (athlete.getTiming() * 0.01);
		double random = Math.random();
		double A = Atemp - Atemp * random * 0.3;
		double B = Btemp - Btemp * random * 0.2 - Btemp * timingCoefficient * 0.6;
		double C = Ctemp - Ctemp * random * 0.2 - Ctemp * timingCoefficient * 0.15;
		double D = Dtemp - Dtemp * random * 0.35 - Dtemp * timingCoefficient * 0.4;
		double E = Etemp - Etemp * random * 0.2;
		double x = 1 / ((hill.getHS() / 140) + 1.5);
		double y = (hill.getHS() / 140) / ((hill.getHS() / 140) + 1.5);
		double jump = A * 0.5 * x + B * x + C * y;
		double length = jump * (hill.getHS() / 2) + (hill.getHS() / 2);
		double percentOverHS = 0;
		if (length > hill.getHS())
			percentOverHS = ((length - hill.getHS()) / hill.getHS()) * 100;
		D -= percentOverHS * 0.1;
		E -= percentOverHS * 0.05;
		double style = C * 0.25 + D * 0.4 + E * 0.25;
		double marks = 36 + style * 24;
		if (marks > 60)
			marks = 60;
		else if (marks < 9)
			marks = 9;
		this.length = util.official(length);
		suffix = "";
		if (this.length > athlete.getPersonalBest()) {
			suffix = "PB";
			athlete.setPersonalBest(this.length);
			Connect.editPersonalBest(athlete.getID(), this.length);
		}
		if (this.length > athlete.getNation().getNationalRecord()) {
			suffix = "NR";
			athlete.getNation().setNationalRecord(this.length);
			Connect.editNationalRecord(athlete.getNation().getID(), this.length);
		}
		if (this.length > hill.getHillRecord()) {
			suffix = "HR";
			hill.setHillRecord(this.length);
			hill.setHillRecordAthlete(athlete);
			hill.setHillRecordDate(Main.getDate());
			Connect.editHillRecord(hill.getID(), this.length, athlete.getID(), Main.getDate());
		}
		if (this.length > Nation.getNation(0).getNationalRecord()) {
			suffix = "WR";
			Nation.getNation(0).setNationalRecord(this.length);
			Connect.editNationalRecord(0, this.length);
		}
		this.marks = util.official(marks);
		int basis = 60;
		double meterValue = 1.8;
		if (hill.getK_point() >= 170) {
			basis = 120;
			meterValue = 1.2;
		}
		else if (hill.getK_point() < 100)
			meterValue = 2;
		double lengthPoints = basis + (this.length - hill.getK_point()) * meterValue;
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		DecimalFormat df = new DecimalFormat("#.##", otherSymbols);
		this.points = Double.valueOf(df.format(lengthPoints + this.marks));
		//System.out.println(toText(athlete));
	}
	
	public double getLength() {
		return length;
	}
	
	public double getMarks() {
		return marks;
	}
	
	public double getPoints() {
		return points;
	}
	
	public String getSuffix() {
		return suffix;
	}
	
	public String toText(Athlete athlete) {
		String text = "~";
		text += athlete.getID();
		text += "|";
		text += athlete.getName();
		text += "|";
		text += athlete.getNation().getAbbrevation();
		text += "|";
		text += athlete.getClub().getName(); // Possibly not working outside World Cup
		text += "|";
		text += athlete.getClub().getManager(); // Same concerns as above
		text += "|";
		text += length;
		text += "|";
		text += marks;
		text += "|";
		text += points;
		return text;
	}
}