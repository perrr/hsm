package types;

import java.util.ArrayList;

public class Club implements Comparable<Club> {

	private int id;
	private String name;
	private String manager;
	private Nation nation;
	private int worldCupPoints;
	private boolean active;
	private static ArrayList<Club> clubs = new ArrayList<Club>();
	
	public Club(int id, String name, String manager, Nation nation, int worldCupPoints,
		boolean active) {
			this.id = id;
			this.name = name;
			this.manager = manager;
			this.nation = nation;
			this.worldCupPoints = worldCupPoints;
			this.active = active;
			clubs.add(this);
	}

	public int getID() {
		return id;
	}

	public void setID(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public Nation getNation() {
		return nation;
	}

	public void setNation(Nation nation) {
		this.nation = nation;
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
	
	public void increaseWorldCupPoints(int points) {
		this.worldCupPoints += points;
	}
	
	public static Club getClub(int clubID) {
		for (Club club : clubs) {
			if (club.getID() == clubID)
				return club;
		}
		return null;
	}

	@Override
	public int compareTo(Club other) {
		int res = other.getWorldCupPoints() - worldCupPoints;
		if (res != 0)
			return res;
		return name.compareTo(other.getName());
	}
}