package types;

import java.util.ArrayList;

import database.Connect;
import logic.Race;

public class Athlete implements Comparable<Athlete> {

	private int id;
	private String firstName;
	private String lastName;
	private int age;
	private Nation nation;
	private Club club;
	private double form;
	private double experience;
	private double agility;
	private double balance;
	private double flightTechnique;
	private double landingTechnique;
	private double timing;
	private String training;
	private int intensity;
	private int participated;
	private int worldCupPoints;
	private boolean active;
	private double personalBest;
	private static ArrayList<Athlete> athletes = new ArrayList<Athlete>();
	
	public Athlete(int id, String firstName, String lastName, int age, Nation nation,
		Club club, double form, double experience, double agility, double balance,
		double flightTechnique, double landingTechnique, double timing, String training,
		int intensity, int participated, int worldCupPoints, boolean active, 
		double personalBest) { //lacking some new fields (I'm lazy)
			this.id = id;
			this.firstName = firstName;
			this.lastName = lastName;
			this.age = age;
			this.nation = nation;
			this.club = club;
			this.form = form;
			this.experience = experience;
			this.agility = agility;
			this.balance = balance;
			this.flightTechnique = flightTechnique;
			this.landingTechnique = landingTechnique;
			this.timing = timing;
			this.training = training;
			this.intensity = intensity;
			this.participated = participated;
			this.worldCupPoints = worldCupPoints;
			this.active = active;
			this.personalBest = personalBest;
			athletes.add(this);
		}
	
	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Nation getNation() {
		return nation;
	}

	public void setNation(Nation nation) {
		this.nation = nation;
	}

	public Club getClub() {
		return club;
	}

	public void setClub(Club club) {
		this.club = club;
	}

	public double getForm() {
		return form;
	}

	public void setForm(double form) {
		this.form = form;
		if (form < 0)
			this.form = 0;
		else if (form > 100)
			this.form = 100;
	}

	public double getExperience() {
		return experience;
	}

	public void setExperience(double experience) {
		this.experience = experience;
	}

	public double getAgility() {
		return agility;
	}

	public void setAgility(double agility) {
		this.agility = agility;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getFlightTechnique() {
		return flightTechnique;
	}

	public void setFlightTechnique(double flightTechnique) {
		this.flightTechnique = flightTechnique;
	}

	public double getLandingTechnique() {
		return landingTechnique;
	}

	public void setLandingTechnique(double landingTechnique) {
		this.landingTechnique = landingTechnique;
	}

	public double getTiming() {
		return timing;
	}

	public void setTiming(double timing) {
		this.timing = timing;
	}

	public String getTraining() {
		return training;
	}

	public void setTraining(String training) {
		this.training = training;
	}

	public int getIntensity() {
		return intensity;
	}

	public void setIntensity(int intensity) {
		this.intensity = intensity;
	}

	public int getParticipated() {
		return participated;
	}

	public void setParticipated(int participated) {
		this.participated = participated;
	}

	public int getWorldCupPoints() {
		return worldCupPoints;
	}

	public void setWorldCupPoints(int worldCupPoints) {
		this.worldCupPoints = worldCupPoints;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public double getPersonalBest() {
		return personalBest;
	}

	public void setPersonalBest(double personalBest) {
		this.personalBest = personalBest;
	}

	public String getName() {
		return firstName + " " + lastName;
	}
	
	public void increaseWorldCupPoints(int points) {
		worldCupPoints += points;
	}
	
	public void increaseExperience(double increase) {
		experience += increase;
		if (experience > 100)
			experience = 100;
	}
	
	public void increaseAgility(double increase) {
		agility += increase;
		if (agility > 100)
			agility = 100;
	}
	
	public void increaseBalance(double increase) {
		balance += increase;
		if (balance > 100)
			balance = 100;
	}
	
	public void increaseFlightTechnique(double increase) {
		flightTechnique += increase;
		if (flightTechnique > 100)
			flightTechnique = 100;
	}
	
	public void increaseLandingTechnique(double increase) {
		landingTechnique += increase;
		if (landingTechnique > 100)
			landingTechnique = 100;
	}
	
	public void increaseTiming(double increase) {
		timing += increase;
		if (timing > 100)
			timing = 100;
	}
	
	public void decreaseAgility(double decrease) {
		agility -= decrease;
		if (agility < 0)
			agility = 0;
	}
	
	public void decreaseBalance(double decrease) {
		balance -= decrease;
		if (balance < 0)
			balance = 0;
	}
	
	public void decreaseFlightTechnique(double decrease) {
		flightTechnique -= decrease;
		if (flightTechnique < 0)
			flightTechnique = 0;
	}
	
	public void decreaseLandingTechnique(double decrease) {
		landingTechnique -= decrease;
		if (landingTechnique < 0)
			landingTechnique = 0;
	}
	
	public void decreaseTiming(double decrease) {
		timing -= decrease;
		if (timing < 0)
			timing = 0;
	}
	
	public static Athlete getAthlete(int athleteID) {
		for (Athlete athlete : athletes) {
			if (athlete.getID() == athleteID)
				return athlete;
		}
		return Connect.getAthlete(athleteID);
	}

	@Override
	public int compareTo(Athlete other) {
		if (Race.getCompareMode().equals("worldCupPoints")) {
			return other.getWorldCupPoints() - worldCupPoints;
		}
		if (Race.getCompareMode().equals("name")) {
			int res = lastName.compareToIgnoreCase(other.getLastName());
			if (res != 0)
				return res;
			return firstName.compareTo(other.getFirstName());
		}
		if (Race.getCompareMode().equals("id")) {
			return id - other.getID();
		}
		if (Race.getCompareMode().equals("club")) {
			return club.getID() - other.getClub().getID();
		}
		return 0;
	}
}