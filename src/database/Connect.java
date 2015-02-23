package database;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;

import types.Athlete;
import types.Club;
import types.Hill;
import types.Nation;

import com.mysql.jdbc.PreparedStatement;

public class Connect {

	private static final String CONNECTION_STRING = "jdbc:mysql://mysql.stud.ntnu.no/perod_hsm";
	private static final String USERNAME = "perod_hillsize";
	private static final String PASSWORD = "7xFsMPvM";
	
	public static Connection connect() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection connection = (Connection) DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD);
			return connection;
		}
		catch (Exception e) {
			System.out.println("Could not establish connection");
			return null;
		}
	}
	
	public static void close(Connection connection) {
		try {
			connection.close();
		}
		catch (Exception e) {
			System.out.println("Could not close connection");
		}
	}
	
	public static ArrayList<Club> getAllActiveClubs() {
		Connection connection = connect();
		String sql = "SELECT * FROM club WHERE active = 1";
		ArrayList<Club> activeClubs = new ArrayList<Club>();
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				String manager = rs.getString("manager");
				Nation nation = Nation.getNation(rs.getInt("nation"));
				int worldCupPoints = rs.getInt("world_cup_points");
				Club club = new Club(id, name, manager, nation, worldCupPoints, true);
				activeClubs.add(club);
			} 
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return activeClubs;
	}
	
	public static ArrayList<Athlete> getAllActiveAthletes() {
		Connection connection = connect();
		String sql = "SELECT * FROM athlete WHERE active = 1";
		ArrayList<Athlete> activeAthletes = new ArrayList<Athlete>();
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				int age = rs.getInt("age");
				Nation nation = Nation.getNation(rs.getInt("nation"));
				int clubID = rs.getInt("club");
				Club club = Club.getClub(clubID);
				double form = rs.getDouble("form");
				double experience = rs.getDouble("experience");
				double agility = rs.getDouble("agility");
				double balance = rs.getDouble("balance");
				double flightTechnique = rs.getDouble("flight_technique");
				double landingTechnique = rs.getDouble("landing_technique");
				double timing = rs.getDouble("timing");
				String training = rs.getString("training");
				int intensity = rs.getInt("intensity");
				int participated = rs.getInt("participated");
				int worldCupPoints = rs.getInt("world_cup_points");
				double personalBest = rs.getDouble("personal_best");
				Athlete athlete = new Athlete(id, firstName, lastName, age, nation,
						club, form, experience, agility, balance, flightTechnique,
						landingTechnique, timing, training, intensity, participated, worldCupPoints, true, personalBest);
				activeAthletes.add(athlete);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return activeAthletes;
	}
	
	public static ArrayList<Athlete> getAllCompetingAthletes(boolean teamCompetition) {
		Connection connection = connect();
		String sql = "SELECT * FROM athlete WHERE selected_";
		sql += teamCompetition ? "team" : "individual";
		sql += " = 1 AND active = 1";
		ArrayList<Athlete> competingAthletes = new ArrayList<Athlete>();
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				int age = rs.getInt("age");
				Nation nation = Nation.getNation(rs.getInt("nation"));
				int clubID = rs.getInt("club");
				Club club = Club.getClub(clubID);
				double form = rs.getDouble("form");
				double experience = rs.getDouble("experience");
				double agility = rs.getDouble("agility");
				double balance = rs.getDouble("balance");
				double flightTechnique = rs.getDouble("flight_technique");
				double landingTechnique = rs.getDouble("landing_technique");
				double timing = rs.getDouble("timing");
				int participated = rs.getInt("participated");
				String training = rs.getString("training");
				int intensity = rs.getInt("intensity");
				int worldCupPoints = rs.getInt("world_cup_points");
				double personalBest = rs.getDouble("personal_best");
				Athlete athlete = new Athlete(id, firstName, lastName, age, nation,
						club, form, experience, agility, balance, flightTechnique,
						landingTechnique, timing, training, intensity, participated,
						worldCupPoints, true, personalBest);
				competingAthletes.add(athlete);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return competingAthletes;
	}
	
	public static Hill getHill(int hillID) {
		Connection connection = connect();
		String sql = "SELECT * FROM hill WHERE id = " + hillID;
		Hill hill = null;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String name = rs.getString("name");
				Nation nation = Nation.getNation(rs.getInt("nation"));
				int hs = rs.getInt("hs");
				int k_point = rs.getInt("k-point");
				double hillRecord = rs.getDouble("hill_record");
				int athleteID = rs.getInt("hill_record_athlete");
				Athlete hillRecordAthlete = Athlete.getAthlete(athleteID);
				String hillRecordDate = rs.getString("hill_record_date");
				hill = new Hill(id, name, nation, hs, k_point, hillRecord,
						hillRecordAthlete, hillRecordDate);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return hill;
	}
	
	public static ArrayList<Nation> getActiveAthletesNations() {
		Connection connection = connect();
		String sql = "SELECT DISTINCT nation FROM athlete WHERE active = 1";
		ArrayList<Nation> nations = new ArrayList<>();
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Nation nation = Nation.getNation(rs.getInt("nation"));
				nations.add(nation);
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return nations;
	}
	
	public static void editResult(int id, int participated, int worldCupPoints) {
		Connection connection = connect();
		String sql = "UPDATE athlete SET participated = " + participated + ", world_cup_points = " + worldCupPoints + " WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editClubResult(int id, int worldCupPoints) {
		Connection connection = connect();
		String sql = "UPDATE club SET world_cup_points = ? WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setInt(1, worldCupPoints);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editSkills(int id, double form, double agility, double balance, double flightTechnique, double landingTechnique, double timing) {
		Connection connection = connect();
		String sql = "UPDATE athlete SET form = ?, agility = ?, balance = ?, flight_technique = ?, landing_technique = ?, timing = ?, participated = 0 WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setDouble(1, form);
			ps.setDouble(2, agility);
			ps.setDouble(3, balance);
			ps.setDouble(4, flightTechnique);
			ps.setDouble(5, landingTechnique);
			ps.setDouble(6, timing);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editExperience(int id, double experience) {
		Connection connection = connect();
		String sql = "UPDATE athlete SET experience = " + experience + " WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editAge(int id, int age) {
		Connection connection = connect();
		String sql = "UPDATE athlete SET age = " + age + " WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editHillRecord(int id, double length, int athleteID, String date) {
		Connection connection = connect();
		String sql = "UPDATE hill SET hill_record = ?, hill_record_athlete = ?, hill_record_date = ? WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.setDouble(1, length);
			ps.setInt(2, athleteID);
			ps.setString(3, date);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void resetAllParticipated() {
		Connection connection = connect();
		String sql = "UPDATE athlete SET participated = 0";
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static ArrayList<Integer> getClubAthleteIDs(int clubID) {
		Connection connection = connect();
		String sql = "SELECT id FROM athlete WHERE active = 1 AND club = " + clubID;
		ArrayList<Integer> clubAthleteIDs = new ArrayList<>();
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				clubAthleteIDs.add(rs.getInt("id"));
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return clubAthleteIDs;
	}
	
	public static Athlete getAthlete(int athleteID) {
		Connection connection = connect();
		String sql = "SELECT * FROM athlete WHERE id = " + athleteID;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String firstName = rs.getString("first_name");
				String lastName = rs.getString("last_name");
				int age = rs.getInt("age");
				Nation nation = Nation.getNation(rs.getInt("nation"));
				int clubID = rs.getInt("club");
				Club club = Club.getClub(clubID);
				double form = rs.getDouble("form");
				double experience = rs.getDouble("experience");
				double agility = rs.getDouble("agility");
				double balance = rs.getDouble("balance");
				double flightTechnique = rs.getDouble("flight_technique");
				double landingTechnique = rs.getDouble("landing_technique");
				double timing = rs.getDouble("timing");
				String training = rs.getString("training");
				int intensity = rs.getInt("intensity");
				int participated = rs.getInt("participated");
				int worldCupPoints = rs.getInt("world_cup_points");
				double personalBest = rs.getDouble("personal_best");
				Athlete athlete = new Athlete(id, firstName, lastName, age, nation,
						club, form, experience, agility, balance, flightTechnique,
						landingTechnique, timing, training, intensity, participated,
						worldCupPoints, true, personalBest);
				return athlete;
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return null;
	}
	
	public static Nation getNation(int nationID) {
		Connection connection = connect();
		String sql = "SELECT * FROM nation WHERE id = " + nationID;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				int id = rs.getInt("id");
				String abbrevation = rs.getString("abbrevation");
				String name = rs.getString("name");
				double nationalRecord = rs.getDouble("national_record");
				Nation nation = new Nation(id, abbrevation, name, nationalRecord);
				return nation;
			}
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
		return null;
	}
	
	public static void editSelection(int id, boolean team, int selection) {
		Connection connection = connect();
		String sql = "UPDATE athlete SET " + (team ? "selected_team" : "selected_individual") + " = " + selection + " WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editTraining(int id, String trainingType, int intensity) {
		Connection connection = connect();
		String sql = "UPDATE athlete SET training = \"" + trainingType + "\", intensity = " + intensity + " WHERE id = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editPersonalBest(int athleteID, double length) {
		Connection connection = connect();
		String sql = "UPDATE athlete SET personal_best = " + length + " WHERE id = " + athleteID;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void editNationalRecord(int nationID, double length) {
		Connection connection = connect();
		String sql = "UPDATE nation SET national_record = " + length + " WHERE id = " + nationID;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	
	public static double getFourHillsPoints(int id) { // Ugly method for some quick implementation
		Connection connection = connect();
		String sql = "SELECT points FROM competition_result WHERE competition = 999 AND athlete = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				return rs.getDouble("points");
			}
			return 0;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return 0;
		}
		finally {
			close(connection);
		}
	}
	
	public static void editFourHillsAthlete(int id, double points) { // Ugly method for some quick implementation
		Connection connection = connect();
		String sql = "UPDATE competition_result SET points = " + points + " WHERE competition = 999 AND athlete = " + id;
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static void addFourHillsAthlete(int id, double points) { // Ugly method for some quick implementation
		Connection connection = connect();
		String sql = "INSERT INTO competition_result (athlete, competition, points) VALUES (" + id + ", 999, " + points + ")";
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}
	
	public static ArrayList<Athlete> getFourHillsStandings() {
		Connection connection = connect();
		String sql = "SELECT athlete FROM competition_result WHERE competition = 999 ORDER BY points DESC";
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			ArrayList<Athlete> athletes = new ArrayList<>();
			while (rs.next()) {
				athletes.add(getAthlete(rs.getInt("athlete")));
			}
			return athletes;
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		finally {
			close(connection);
		}
	}
	
	/*public static void workAround() {
		Connection connection = connect();
		String sql = "INSERT INTO hill (id, name, nation, hs, `k-point`) VALUES (64, 'Brotterode', 41, 117, 105)";
		try {
			PreparedStatement ps = (PreparedStatement) connection.prepareStatement(sql);
			ps.executeUpdate();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
		finally {
			close(connection);
		}
	}*/
}