package logic;

import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import types.Athlete;
import types.Club;
import types.Hill;
import types.Nation;
import types.Result;
import types.TeamResult;
import database.Connect;

public class Main {
	
	private static String date;

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Hillsize Manager Administrator running.\n---------");
		System.out.println("Menu: Race [r] - Training [t] - Administration [a]");
		String mode = in.nextLine();
		if (mode.equals("r")) {
			System.out.println("Select World Cup [c], National Championships [n] or World Championship [w]");
			String type = in.nextLine();
			if (type.equals("c")) {
				System.out.println("ID of hill:");
				int id = in.nextInt();
				in.nextLine();
				System.out.println("Team competition? [y/n]");
				String choice = in.nextLine();
				boolean teamCompetition = false;
				if (choice.equals("y"))
					teamCompetition = true;
				System.out.println("Date:");
				date = in.nextLine();
				System.out.println("Calculating...\n----------");
				ArrayList<Club> clubs = Connect.getAllActiveClubs();
				ArrayList<Athlete> athletes = Connect.getAllCompetingAthletes(teamCompetition);
				Hill hill = Connect.getHill(id);
				Race race = new Race(athletes, hill, date, teamCompetition, true, "");
				if (!teamCompetition) {
					int[] worldCupPoints = {100, 80, 60, 50, 45, 40, 36, 32, 29, 26, 24, 22, 20, 18, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1};
					ArrayList<Result> results = race.getResults();
					int placement = 0;
					for (int i = 0; i < results.size(); i++) {
						/*double fourHillsPoints = Connect.getFourHillsPoints(results.get(i).getAthlete().getID());
						if (fourHillsPoints == 0)
							Connect.addFourHillsAthlete(results.get(i).getAthlete().getID(), results.get(i).getTotalPoints());
						else
							Connect.editFourHillsAthlete(results.get(i).getAthlete().getID(), fourHillsPoints + results.get(i).getTotalPoints());*/
						if (i != 0 && results.get(i).getTotalPoints() != results.get(i-1).getTotalPoints())
							placement = i;
						if (placement < 30) {
							results.get(i).getAthlete().increaseWorldCupPoints(worldCupPoints[placement]);
							results.get(i).getAthlete().getClub().increaseWorldCupPoints(worldCupPoints[placement]);
							Connect.editClubResult(results.get(i).getAthlete().getClub().getID(), results.get(i).getAthlete().getClub().getWorldCupPoints());
						}
						Connect.editResult(results.get(i).getAthlete().getID(), results.get(i).getAthlete().getParticipated() + 1, results.get(i).getAthlete().getWorldCupPoints());
						Training.changeExperience(results.get(i).getAthlete(), placement + 1, results.size());
						Connect.editExperience(results.get(i).getAthlete().getID(), results.get(i).getAthlete().getExperience());
					}
					//System.out.println(printFourHills(hill));
					List<Result> qualificationResults = race.getQualifyingAthletes();
					placement = results.size();
					int numberOfPrequalified = race.getNumberOfPrequalified();
					for (int i = results.size() - numberOfPrequalified; i < qualificationResults.size(); i++) {
						if (i != results.size() && qualificationResults.get(i).getTotalPoints() != qualificationResults.get(i-1).getTotalPoints())
							placement = i + numberOfPrequalified;
						Connect.editResult(qualificationResults.get(i).getAthlete().getID(), qualificationResults.get(i).getAthlete().getParticipated() + 1, qualificationResults.get(i).getAthlete().getWorldCupPoints());
						Training.changeExperience(qualificationResults.get(i).getAthlete(), placement + 1, results.size());
						Connect.editExperience(qualificationResults.get(i).getAthlete().getID(), qualificationResults.get(i).getAthlete().getExperience());
					}
					athletes = Connect.getAllActiveAthletes();
					Race.setCompareMode("worldCupPoints");
					Collections.sort(athletes);
					System.out.println(printWorldCupPoints(athletes, hill));
				}
				else {
					int[] worldCupPoints = {400, 350, 300, 250, 200, 150, 100, 50};
					ArrayList<TeamResult> teamResults = race.getTeamResults();
					int placement = 0;
					for (int i = 0; i < teamResults.size(); i++) {
						if (i != 0 && teamResults.get(i).getTotalPoints() != teamResults.get(i-1).getTotalPoints())
							placement = i;
						if (placement < 8) {
							teamResults.get(i).getClub().increaseWorldCupPoints(worldCupPoints[placement]);
							Connect.editClubResult(teamResults.get(i).getClub().getID(), teamResults.get(i).getClub().getWorldCupPoints());
						}
						for (Result result : teamResults.get(i).getResults()) {
							Athlete athlete = result.getAthlete();
							Connect.editResult(athlete.getID(), athlete.getParticipated() + 1, athlete.getWorldCupPoints());
							Training.changeExperience(athlete, placement + 1, teamResults.size());
							Connect.editExperience(athlete.getID(), athlete.getExperience());
						}
					}
				}
				Collections.sort(clubs);
				System.out.println(printClubWorldCupPoints(clubs, hill));
			}
			else if (type.equals("n")) {
				ArrayList<Athlete> athletes = Connect.getAllActiveAthletes();
				ArrayList<Nation> nations = Connect.getActiveAthletesNations();
				for (Nation nation : nations) {
					System.out.println("ID of hill for " + nation.getAbbrevation() + ":");
					int id = in.nextInt();
					in.nextLine();
					System.out.println("Date:");
					date = in.nextLine();
					System.out.println("Calculating...\n----------");
					ArrayList<Athlete> nationAthletes = new ArrayList<>();
					for (Athlete athlete : athletes) {
						if (athlete.getNation() == nation)
							nationAthletes.add(athlete);
					}
					Hill hill = Connect.getHill(id);
					Race race = new Race(nationAthletes, hill, date, false, false, "National Championship " + nation.getName() + " - ");
					ArrayList<Result> results = race.getResults();
					int placement = 0;
					for (int i = 0; i < results.size(); i++) {
						if (i != 0 && results.get(i).getTotalPoints() != results.get(i-1).getTotalPoints())
							placement = i;
						Connect.editResult(results.get(i).getAthlete().getID(), results.get(i).getAthlete().getParticipated() + 1, results.get(i).getAthlete().getWorldCupPoints());
						Training.changeExperience(results.get(i).getAthlete(), placement + 1, results.size());
						Connect.editExperience(results.get(i).getAthlete().getID(), results.get(i).getAthlete().getExperience());
					}
				}
			}
			else if (type.equals("w")) {
				System.out.println("ID of hill:");
				int id = in.nextInt();
				in.nextLine();
				System.out.println("Date:");
				date = in.nextLine();
				System.out.println("Calculating...\n----------");
				ArrayList<Athlete> athletes = Connect.getAllActiveAthletes(); // all active jumpers participate (should be tied up to NCs in the future)
				Hill hill = Connect.getHill(id);
				Race race = new Race(athletes, hill, date, false, false, "World Championship - ");
				ArrayList<Result> results = race.getResults();
				int placement = 0;
				for (int i = 0; i < results.size(); i++) {
					if (i != 0 && results.get(i).getTotalPoints() != results.get(i-1).getTotalPoints())
						placement = i;
					Connect.editResult(results.get(i).getAthlete().getID(), results.get(i).getAthlete().getParticipated() + 1, results.get(i).getAthlete().getWorldCupPoints());
					Training.changeExperience(results.get(i).getAthlete(), placement + 1, results.size());
					Connect.editExperience(results.get(i).getAthlete().getID(), results.get(i).getAthlete().getExperience());
				}
			}
		}
		else if (mode.equals("t")) {
			System.out.println("End of season? [y/n]");
			String end = in.nextLine();
			if (end.equals("y")) {
				ArrayList<Athlete> athletes = Connect.getAllActiveAthletes(); //also semi-active, ideally...
				for (Athlete athlete : athletes) {
					athlete.setAge(athlete.getAge() + 1);
					Connect.editAge(athlete.getID(), athlete.getAge());
				}
			}
			ArrayList<Club> clubs = Connect.getAllActiveClubs();
			ArrayList<Athlete> athletes = Connect.getAllActiveAthletes();
			new Training(athletes);
			for (int i = 0; i < athletes.size(); i++) {
				Connect.editSkills(athletes.get(i).getID(), athletes.get(i).getForm(),
						athletes.get(i).getAgility(),
						athletes.get(i).getBalance(),
						athletes.get(i).getFlightTechnique(),
						athletes.get(i).getLandingTechnique(),
						athletes.get(i).getTiming());
			}
			System.out.println(printTrainingReports(clubs, athletes));
		}
		else if (mode.equals("a")) {
			System.out.println("Select Team selection [s] or Training instructions [i]");
			String action = in.next();
			if (action.equals("s")) {
				System.out.println("Select Team competition [t] or Individual competition [i]");
				String type = in.next();
				boolean team = false;
				int number = 6;
				if (type.equals("t")) {
					team = true;
					number = 4;
				}
				System.out.println("Please input club ID followed by athlete IDs.");
				int clubID = in.nextInt();
				ArrayList<Integer> clubAthleteIDs = Connect.getClubAthleteIDs(clubID);
				HashSet<Integer> selection = new HashSet<>();
				for (int i = 0; i < number; i++) {
					selection.add(in.nextInt());
				}
				for (Integer ID : clubAthleteIDs) {
					if (selection.contains(ID))
						Connect.editSelection(ID, team, 1);
					else
						Connect.editSelection(ID, team, 0);
				}
				System.out.println("Update completed.");
			}
			else if (action.equals("i")) {
				System.out.println("Please input club ID followed by athlete IDs and training abbrevations and/or intensities.");
				int clubID = in.nextInt();
				ArrayList<Integer> clubAthleteIDs = Connect.getClubAthleteIDs(clubID);
				try {
					for (int i = 0; i < 8; i++) {
						int id = in.nextInt();
						String training = in.next();
						int intensity = in.nextInt();
						String trainingType;
						switch (training) {
						case "a":
							trainingType = "agility";
							break;
						case "b":
							trainingType = "balance";
							break;
						case "f":
							trainingType = "flight_technique";
							break;
						case "l":
							trainingType = "landing_technique";
							break;
						case "t":
							trainingType = "timing";
							break;
						default:
							throw new IllegalArgumentException("Illegal training type " + training);
						}
						if (!clubAthleteIDs.contains(id))
							throw new IllegalArgumentException("Illegal athlete ID " + id);
						if (intensity < 0 || intensity > 100)
							throw new IllegalArgumentException("Illegal intensity " + intensity);
						Connect.editTraining(id, trainingType, intensity);
					}
				}
				finally {
					in.close();
				}
				System.out.println("Update completed.");
			}
		}
		in.close();
	}
	
	private static String printWorldCupPoints(ArrayList<Athlete> athletes, Hill hill) {
		String string = "[table][tr][th align=center colspan=4]World Cup standings after ";
		string += hill.getName();
		string += " (HS ";
		string += hill.getHS();
		if (athletes.get(30).getWorldCupPoints() != 0)
			string += ") (part 1";
		string += ")[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]Points[/td][/tr]";
		int placement = 1;
		for (int i = 0; i < athletes.size(); i++) {
			if (athletes.get(i).getWorldCupPoints() == 0)
				break;
			if (i % 30 == 0 && i != 0) {
				string += "[/table]\n\n[table][tr][th align=center colspan=4]World Cup standings after ";
				string += hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += ") (part ";
				string += (int) (i / 30) + 1;
				string += ")[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]Points[/td][/tr]";
			}
			if (i != 0 && athletes.get(i).getWorldCupPoints() != athletes.get(i-1).getWorldCupPoints())
				placement = i + 1;
			string += "[tr][td]";
			string += placement;
			string += "[/td][td]";
			string += athletes.get(i).getName();
			string += "[/td][td]";
			string += athletes.get(i).getClub().getName();
			string += "[/td][td]";
			string += athletes.get(i).getWorldCupPoints();
			string += "[/td][/tr]";
		}
		string += "[/table]";
		return string;
	}
	
	private static String printClubWorldCupPoints(ArrayList<Club> clubs, Hill hill) {
		String string = "[table][tr][th align=center colspan=3]Club World Cup standings after ";
		string += hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += ")[/th][/tr][tr][td]#[/td][td]Name[/td][td]Points[/td][/tr]";
		int placement = 1;
		for (int i = 0; i < clubs.size(); i++) {
			if (clubs.get(i).getWorldCupPoints() == 0)
				break;
			if (i != 0 && clubs.get(i).getWorldCupPoints() != clubs.get(i-1).getWorldCupPoints())
				placement = i + 1;
			string += "[tr][td]";
			string += placement;
			string += "[/td][td]";
			string += clubs.get(i).getName();
			string += "[/td][td]";
			string += clubs.get(i).getWorldCupPoints();
			string += "[/td][/tr]";
		}
		string += "[/table]";
		return string;
	}
	
	private static String printTrainingReports(ArrayList<Club> clubs, ArrayList<Athlete> athletes) {
		String string = "";
		for (Club club : clubs) {
			ArrayList<Athlete> clubAthletes = new ArrayList<Athlete>();
			for (int i = 0; clubAthletes.size() < 8; i++) {
				if (athletes.get(i).getClub() == club)
					clubAthletes.add(athletes.get(i));
			}
			string += club.getManager() + "\n";
			string += club.getID() + "[br]";
			for (int i = 0; i < clubAthletes.size(); i++) {
				string += clubAthletes.get(i).getID() + " " + clubAthletes.get(i).getTraining().charAt(0) + " " + clubAthletes.get(i).getIntensity() + "[br]";
			}
			string += "[table][tr][th]Name[/th][th]ID[/th][th]Age[/th][th]Nation[/th][th]Training[/th][th]Intensity[/th][th]Experience[/th][/tr]";
			for (int i = 0; i < clubAthletes.size(); i++) {
				string += "[tr][td]";
				string += clubAthletes.get(i).getName();
				string += "[/td][td]";
				string += clubAthletes.get(i).getID();
				string += "[/td][td]";
				string += clubAthletes.get(i).getAge();
				string += "[/td][td]";
				string += clubAthletes.get(i).getNation().getAbbrevation();
				string += "[/td][td]";
				String training = Character.toUpperCase(clubAthletes.get(i).getTraining().charAt(0)) + clubAthletes.get(i).getTraining().substring(1);
				training = training.replace("_", " ");
				string += training;
				string += "[/td][td]";
				string += clubAthletes.get(i).getIntensity();
				string += "[/td][td]";
				string += Math.round(clubAthletes.get(i).getExperience());
				string += "[/td][/tr]";
			}
			string += "[tr][th]Name[/th][th]Form[/th][th]Agility[/th][th]Balance[/th][th]Flight technique[/th][th]Landing technique[/th][th]Timing[/th][/tr]";
			for (int i = 0; i < clubAthletes.size(); i++) {
				string += "[tr][td]";
				string += clubAthletes.get(i).getName();
				string += "[/td][td]";
				string += Math.round(clubAthletes.get(i).getForm());
				string += "[/td][td]";
				string += Math.round(clubAthletes.get(i).getAgility());
				string += "[/td][td]";
				string += Math.round(clubAthletes.get(i).getBalance());
				string += "[/td][td]";
				string += Math.round(clubAthletes.get(i).getFlightTechnique());
				string += "[/td][td]";
				string += Math.round(clubAthletes.get(i).getLandingTechnique());
				string += "[/td][td]";
				string += Math.round(clubAthletes.get(i).getTiming());
				string += "[/td][/tr]";
			}
			string += "[/table]\n\n";
		}
		return string;
	}
	
	public static String printFourHills(Hill hill) {
		String string = "[table][tr][th align=center colspan=4]Four Hills Tournament standings after ";
		string += hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += ") (part 1";
		string += ")[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]Points[/td][/tr]";
		int placement = 1;
		ArrayList<Athlete> athletes = Connect.getFourHillsStandings();
		double lastPoints = 0;
		for (int i = 0; i < athletes.size(); i++) {
			if (i % 30 == 0 && i != 0) {
				string += "[/table]\n\n[table][tr][th align=center colspan=4]Four Hills Tournament standings after ";
				string += hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += ") (part ";
				string += (int) (i / 30) + 1;
				string += ")[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]Points[/td][/tr]";
			}
			double points = Connect.getFourHillsPoints(athletes.get(i).getID());
			if (i != 0 && points != lastPoints)
				placement = i + 1;
			string += "[tr][td]";
			string += placement;
			string += "[/td][td]";
			string += athletes.get(i).getName();
			string += "[/td][td]";
			string += athletes.get(i).getClub().getName();
			string += "[/td][td]";
			string += points;
			string += "[/td][/tr]";
			lastPoints = points;
		}
		string += "[/table]";
		return string;
	}
	
	public static String getDate() {
		return date;
	}
}