package types;

import java.util.ArrayList;

import database.Connect;

public class Nation {

	private int id;
	private String abbrevation;
	private String name;
	private double nationalRecord;
	private static ArrayList<Nation> nations = new ArrayList<>();
	
	public Nation(int id, String abbrevation, String name, double nationalRecord) {
		this.id = id;
		this.abbrevation = abbrevation;
		this.name = name;
		this.nationalRecord = nationalRecord;
		nations.add(this);
	}
	
	public int getID() {
		return id;
	}
	
	public String getAbbrevation() {
		return abbrevation;
	}
	
	public String getName() {
		return name;
	}
	
	public double getNationalRecord() {
		return nationalRecord;
	}
	
	public void setNationalRecord(double nationalRecord) {
		this.nationalRecord = nationalRecord;
	}
	
	public static Nation getNation(int id) {
		for (Nation nation : nations) {
			if (nation.getID() == id)
				return nation;
		}
		return Connect.getNation(id);
	}
}