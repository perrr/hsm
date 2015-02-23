package logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import types.Athlete;
import types.Club;
import types.Hill;
import types.Result;
import types.TeamResult;

public class Race {
	
	private ArrayList<Athlete> athletes;
	private Hill hill;
	private String date;
	private boolean isTeamCompetition;
	private boolean hasClubs;
	private ArrayList<Result> results;
	private ArrayList<TeamResult> teamResults;
	private List<Result> qualifyingAthletes;
	private int numberOfPrequalified;
	private String extraInfo;
	private static String compareMode;

	public Race(ArrayList<Athlete> athletes, Hill hill, String date, boolean isTeamCompetition, boolean hasClubs, String extraInfo) {
		this.athletes = athletes;
		this.hill = hill;
		this.date = date;
		this.isTeamCompetition = isTeamCompetition;
		this.hasClubs = hasClubs;
		this.extraInfo = extraInfo;
		System.out.println(printInfo());
		ArrayList<Athlete> firstRoundAthletes = new ArrayList<Athlete>();
		ArrayList<Athlete> finalRoundAthletes = new ArrayList<Athlete>();
		if (!isTeamCompetition) {
			int numberOfParticipants = 50;
			if (hill.getK_point() >= 170)
				numberOfParticipants = 40;
			if (athletes.size() > numberOfParticipants && extraInfo.length() == 0) {
				compareMode = "worldCupPoints";
				Collections.sort(athletes);
				Collections.reverse(athletes);
				ArrayList<Result> qualificationResults = performRound(athletes);
				int firstPrequalified = athletes.size() - 10;
				int pointsToPrequalify = athletes.get(firstPrequalified).getWorldCupPoints();
				while (true) {
					if (athletes.get(firstPrequalified - 1).getWorldCupPoints() == pointsToPrequalify)
						firstPrequalified--;
					else
						break;
				}
				qualifyingAthletes = qualificationResults.subList(0, firstPrequalified);
				List<Result> prequalifiedAthletes = qualificationResults.subList(firstPrequalified, qualificationResults.size());
				numberOfPrequalified = prequalifiedAthletes.size();
				Collections.sort(qualifyingAthletes);
				double lastPointsToQualify = 0;
				for (int i = 0; i < numberOfParticipants - prequalifiedAthletes.size(); i++) {
					firstRoundAthletes.add(qualifyingAthletes.get(i).getAthlete());
					lastPointsToQualify = qualifyingAthletes.get(i).getJump1().getPoints();
				}
				int j = numberOfParticipants - prequalifiedAthletes.size();
				while (true) {
					try {
						if (qualifyingAthletes.get(j).getJump1().getPoints() == lastPointsToQualify) {
							firstRoundAthletes.add(qualifyingAthletes.get(j).getAthlete());
							j++;
						}
						else
							break;
					}
					catch (Exception e) {
						break;
					}
				}
				for (int i = 0; i < prequalifiedAthletes.size(); i++) {
					firstRoundAthletes.add(prequalifiedAthletes.get(i).getAthlete());
				}
				System.out.println(printQualificationResults(qualifyingAthletes, prequalifiedAthletes, lastPointsToQualify));
			}
			else {
				qualifyingAthletes = new ArrayList<Result>();
				firstRoundAthletes = athletes;
			}
			compareMode = "worldCupPoints";
			Collections.sort(firstRoundAthletes); // To make the ordering right for the textual results
			Collections.reverse(firstRoundAthletes);
			ArrayList<Result> firstRoundResults = performRound(firstRoundAthletes);
			Collections.sort(firstRoundResults);
			double lastPointsToQualifyForFinalRound = 0;
			for (int i = 0; i < 30 && i < firstRoundAthletes.size(); i++) {
				finalRoundAthletes.add(firstRoundResults.get(i).getAthlete());
				lastPointsToQualifyForFinalRound = firstRoundResults.get(i).getJump1().getPoints();
			}
			for (int i = 30; i < firstRoundResults.size(); i++) {
				if (firstRoundResults.get(i).getJump1().getPoints() == lastPointsToQualifyForFinalRound)
					finalRoundAthletes.add(firstRoundResults.get(i).getAthlete());
				else
					break;
			}
			System.out.println(printFirstRoundResults(firstRoundResults));
			Collections.reverse(finalRoundAthletes); // To make the ordering right for the textual results
			ArrayList<Result> finalRoundResults = performRound(finalRoundAthletes);
			Collections.reverse(finalRoundResults); // To be able to unify results correctly
			results = new ArrayList<Result>();
			for (int i = 0; i < firstRoundResults.size(); i++) {
				results.add(firstRoundResults.get(i));
			}
			for (int i = 0; i < finalRoundResults.size(); i++) {
				results.get(i).addJump(finalRoundResults.get(i).getJump1());
			}
			Collections.sort(results);
			System.out.println(printFinalRoundResults());
		}
		else {
			ArrayList<Club> teams = new ArrayList<Club>();
			ArrayList<TeamResult> firstRoundTeamResults = new ArrayList<TeamResult>();
			for (Athlete athlete : athletes) {
				if (!teams.contains(athlete.getClub())) {
					teams.add(athlete.getClub());
					firstRoundTeamResults.add(new TeamResult(athlete.getClub()));
				}
			}
			ArrayList<Result> firstRoundResults = performRound(athletes);
			for (Result result : firstRoundResults) {
				int index = teams.indexOf(result.getAthlete().getClub());
				firstRoundTeamResults.get(index).addResult(result);
			}
			Collections.sort(firstRoundTeamResults);
			System.out.println(printFirstRoundTeamResults(firstRoundTeamResults));
			double lastPointsToQualifyForFinalRound = 0;
			for (int i = 0; i < firstRoundTeamResults.size(); i++) {
				if (i == 8)
					break;
				lastPointsToQualifyForFinalRound = firstRoundTeamResults.get(i).getTotalPoints();
				for (Result result : firstRoundTeamResults.get(i).getResults()) {
					finalRoundAthletes.add(result.getAthlete());
				}
			}
			for (int i = 8; i < firstRoundTeamResults.size(); i++) {
				if (firstRoundTeamResults.get(i).getTotalPoints() == lastPointsToQualifyForFinalRound) {
					for (Result result : firstRoundTeamResults.get(i).getResults()) {
						finalRoundAthletes.add(result.getAthlete());
					}
				}
				else
					break;
			}
			ArrayList<Result> finalRoundResults = performRound(finalRoundAthletes);
			results = new ArrayList<Result>();
			teamResults = new ArrayList<TeamResult>();
			for (int i = 0; i < firstRoundResults.size(); i++) {
				results.add(firstRoundResults.get(i));
			}
			int k;
			for (k = 0; k < finalRoundResults.size(); k++) {
				if (k % 4 == 0)
					teamResults.add(new TeamResult(finalRoundResults.get(k).getAthlete().getClub()));
				for (int j = 0; j < results.size(); j++) {
					if (results.get(j).getAthlete().getID() == finalRoundResults.get(k).getAthlete().getID()) {
						results.get(j).addJump(finalRoundResults.get(k).getJump1());
						teamResults.get(teamResults.size() - 1).addResult(results.get(j));
						break;
					}
				}
			}
			for (k /= 4; k < firstRoundTeamResults.size(); k++) {
				teamResults.add(firstRoundTeamResults.get(k));
			}
			Collections.sort(teamResults);
			System.out.println(printFinalRoundTeamResults());
		}
	}
	
	private ArrayList<Result> performRound(ArrayList<Athlete> roundAthletes) {
		ArrayList<Result> results = new ArrayList<Result>(); 
		for (int i = 0; i < roundAthletes.size(); i++) {
			Jump jump = new Jump(roundAthletes.get(i), hill);
			Result result = new Result(roundAthletes.get(i), jump);
			results.add(result);
		}
		return results;
	}
	
	public String printInfo() {
		String string = "[table][tr][th align=center colspan=2]";
		string += extraInfo + hill.getName();
		string += " (";
		string += hill.getNation().getAbbrevation();
		string += ")[/th][/tr][tr][th]HS: ";
		string += hill.getHS();
		string += " m[/th][th]K-point: ";
		string += hill.getK_point();
		string += " m[/th][/tr][tr][th colspan=2]Hill record: ";
		string += hill.getHillRecord();
		string += " m";
		if (hill.getHillRecordAthlete() != null) {
			string += " (";
			string += hill.getHillRecordAthlete().getName();
			string += ", ";
			string += hill.getHillRecordDate();
			string += ")";
		}
		string += "[/th][/tr][tr][th]";
		if (isTeamCompetition)
			string += "Team competition";
		else
			string += "Individual competition";
		string += "[/th][th]";
		string += date;
		string += "[/th][/tr]";
		compareMode = "id";
		Collections.sort(athletes);
		if (extraInfo.length() == 0) {
			compareMode = "club";
			Collections.sort(athletes);
			int lastID = 0;
			for (Athlete athlete : athletes) {
				if (athlete.getClub().getID() != lastID) {
					if (lastID != 0)
						string += "[/td][/tr]";
					lastID = athlete.getID();
					string += "[tr][td colspan=2][b]";
					string += athlete.getClub().getName();
					string += "[/b]";
					lastID = athlete.getClub().getID();
				}
				string += "[br]";
				string += athlete.getName();
				if (hill.getK_point() >= 170) {
					string += " (";
					string += athlete.getPersonalBest();
					string += " m)";
				}
			}
		}
		else {
			string += "[tr][td colspan=2]";
			boolean first = true;
			for (Athlete athlete : athletes) {
				if (first)
					first = false;
				else
					string += "[br]";
				string += athlete.getName();
			}
		}
		string += "[/td][/tr][/table]";
		return string;
	}
	
	private String printQualificationResults(List<Result> qualifyingAthletes, List<Result> prequalifiedAthletes, double lastPointsToQualify) {
		String string = "[table][tr][th align=center colspan=7]";
		string += extraInfo + hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += "), qualified athletes (part 1)[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
		int placement = 1;
		int i = 0;
		for (i = 0; qualifyingAthletes.get(i).getJump1().getPoints() >= lastPointsToQualify; i++) {
			if (i % 30 == 0 && i != 0) {
				string += "[/table]\n\n[table][tr][th align=center colspan=7]";
				string += hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += "), qualified athletes (part ";
				string += (i / 30) + 1;
				string += ")[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
			}
			string += "[tr][td]";
			if (i != 0 && qualifyingAthletes.get(i).getJump1().getPoints() != qualifyingAthletes.get(i-1).getJump1().getPoints())
				placement = i + 1;
			string += placement;
			string += "[/td][td]";
			string += qualifyingAthletes.get(i).getAthlete().getName();
			string += "[/td][td]";
			string += qualifyingAthletes.get(i).getAthlete().getClub().getName();
			string += "[/td][td]";
			string += qualifyingAthletes.get(i).getJump1().getLength();
			string += "[/td][td]";
			string += qualifyingAthletes.get(i).getJump1().getMarks();
			string += "[/td][td]";
			string += qualifyingAthletes.get(i).getJump1().getPoints();
			string += "[/td][td]";
			string += qualifyingAthletes.get(i).getJump1().getSuffix();
			string += "[/td][/tr]";
		}
		boolean twoTables = qualifyingAthletes.size() - i > 30;
		string += "[/table]\n\n[table][tr][th align=center colspan=7]";
		string += extraInfo + hill.getName();
		string += " (HS ";
		string += Integer.toString(hill.getHS());
		string += "), not qualified athletes";
		if (twoTables)
			string += " (part 1)";
		string += "[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
		for (int j = i; j < qualifyingAthletes.size(); j++) {
			if (j == i + 30) {
				string += "[/table]\n\n[table][tr][th align=center colspan=6]";
				string += hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += "), not qualified athletes (part 2)[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
			}
			if (j != 0 && qualifyingAthletes.get(j).getJump1().getPoints() != qualifyingAthletes.get(j-1).getJump1().getPoints())
				placement = j + 1;
			string += "[tr][td]";
			string += placement;
			string += "[/td][td]";
			string += qualifyingAthletes.get(j).getAthlete().getName();
			string += "[/td][td]";
			string += qualifyingAthletes.get(j).getAthlete().getClub().getName();
			string += "[/td][td]";
			string += qualifyingAthletes.get(j).getJump1().getLength();
			string += "[/td][td]";
			string += qualifyingAthletes.get(j).getJump1().getMarks();
			string += "[/td][td]";
			string += qualifyingAthletes.get(j).getJump1().getPoints();
			string += "[/td][td]";
			string += qualifyingAthletes.get(j).getJump1().getSuffix();
			string += "[/td][/tr]";
		}
		string += "[/table]\n\n[table][tr][th align=center colspan=4]";
		string += extraInfo + hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += "), prequalified athletes[/th][/tr][tr][td]Name[/td][td]Club[/td][td]L[/td][td][/td][/tr]";
		for (int j = 0; j < prequalifiedAthletes.size(); j++) {
			string += "[tr][td]";
			string += prequalifiedAthletes.get(j).getAthlete().getName();
			string += "[/td][td]";
			string += prequalifiedAthletes.get(j).getAthlete().getClub().getName();
			string += "[/td][td]";
			string += prequalifiedAthletes.get(j).getJump1().getLength();
			string += "[/td][td]";
			string += prequalifiedAthletes.get(j).getJump1().getSuffix();
			string += "[/td][/tr]";
		}
		string += "[/table]";
		return string;
	}
	
	private String printFirstRoundResults(ArrayList<Result> firstRoundResults) {
		String string = "[table][tr][th align=center colspan=";
		string += hasClubs ? "7]" : "6]";
		string += extraInfo + hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += "), first round ";
		if (firstRoundResults.size() > 30)
			string += "(part 1)";
		string += "[/th][/tr][tr][td]#[/td][td]Name[/td]";
		if (hasClubs)
			string += "[td]Club[/td]";
		string += "[td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
		int placement = 1;
		int i = 0;
		for (i = 0; i < firstRoundResults.size(); i++) {
			if (i == 30) {
				string += "[/table]\n\n[table][tr][th align=center colspan=7]";
				string += extraInfo + hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += "), first round (part 2)[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
			}
			string += "[tr][td]";
			if (i != 0 && firstRoundResults.get(i).getJump1().getPoints() != firstRoundResults.get(i-1).getJump1().getPoints())
				placement = i + 1;
			string += placement;
			string += "[/td][td]";
			string += firstRoundResults.get(i).getAthlete().getName();
			string += "[/td][td]";
			if (hasClubs) {
				string += firstRoundResults.get(i).getAthlete().getClub().getName();
				string += "[/td][td]";
			}
			string += firstRoundResults.get(i).getJump1().getLength();
			string += "[/td][td]";
			string += firstRoundResults.get(i).getJump1().getMarks();
			string += "[/td][td]";
			string += firstRoundResults.get(i).getJump1().getPoints();
			string += "[/td][td]";
			string += firstRoundResults.get(i).getJump1().getSuffix();
			string += "[/td][/tr]";
		}
		string += "[/table]";
		return string;
	}
	
	private String printFinalRoundResults() {
		String string = "[table][tr][th align=center colspan=";
		string += hasClubs ? "7]" : "6]";
		string += extraInfo + hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += "), final round and final results ";
		if (results.size() > 30)
			string += "(part 1)";
		string += "[/th][/tr][tr][td]#[/td][td]Name[/td]";
		if (hasClubs)
			string += "[td]Club[/td]";
		string += "[td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
		int placement = 1;
		int i = 0;
		for (i = 0; i < results.size(); i++) {
			if (i == 30) {
				string += "[/table]\n\n[table][tr][th align=center colspan=7]";
				string += extraInfo + hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += "), final round and final results (part 2)[/th][/tr][tr][td]#[/td][td]Name[/td][td]Club[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
			}
			string += "[tr][td]";
			if (i != 0 && results.get(i).getTotalPoints() != results.get(i-1).getTotalPoints())
				placement = i + 1;
			string += placement;
			string += "[/td][td]";
			string += results.get(i).getAthlete().getName();
			string += "[/td][td]";
			if (hasClubs) {
				string += results.get(i).getAthlete().getClub().getName();
				string += "[/td][td]";
			}
			if (results.get(i).getJump2() != null) {
				string += results.get(i).getJump2().getLength();
				string += "[/td][td]";
				string += results.get(i).getJump2().getMarks();
			}
			else
				string += "[/td][td]";
			string += "[/td][td]";
			string += results.get(i).getTotalPoints();
			string += "[/td][td]";
			if (results.get(i).getJump2() != null)
				string += results.get(i).getJump2().getSuffix();
			string += "[/td][/tr]";
		}
		string += "[/table]";
		return string;
	}
	
	private String printFirstRoundTeamResults(ArrayList<TeamResult> firstRoundTeamResults) {
		String string = "[table][tr][th align=center colspan=6]";
		string += extraInfo + hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += "), first round ";
		if (firstRoundTeamResults.size() > 6)
			string += "(part 1)";
		string += "[/th][/tr][tr][td]#[/td][td]Name[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
		int placement = 1;
		int i = 0;
		for (i = 0; i < firstRoundTeamResults.size(); i++) {
			if (i == 6) {
				string += "[/table]\n\n[table][tr][th align=center colspan=6]";
				string += extraInfo + hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += "), first round (part 2)[/th][/tr][tr][td]#[/td][td]Name[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
			}
			string += "[tr][td][b]";
			if (i != 0 && firstRoundTeamResults.get(i).getTotalPoints() != firstRoundTeamResults.get(i-1).getTotalPoints())
				placement = i + 1;
			string += placement;
			string += "[/b][/td][td][b]";
			string += firstRoundTeamResults.get(i).getClub().getName();
			string += "[/b][/td][td][/td][td][/td][td][b]";
			string += firstRoundTeamResults.get(i).getTotalPoints();
			string += "[/b][/td][td][/td][/tr]";
			for (int j = 0; j < firstRoundTeamResults.get(i).getResults().size(); j++) {
				string += "[tr][td][/td][td]";
				string += firstRoundTeamResults.get(i).getResults().get(j).getAthlete().getName();
				string += "[/td][td]";
				string += firstRoundTeamResults.get(i).getResults().get(j).getJump1().getLength();
				string += "[/td][td]";
				string += firstRoundTeamResults.get(i).getResults().get(j).getJump1().getMarks();
				string += "[/td][td]";
				string += firstRoundTeamResults.get(i).getResults().get(j).getJump1().getPoints();
				string += "[/td][td]";
				string += firstRoundTeamResults.get(i).getResults().get(j).getJump1().getSuffix();
				string += "[/td][/tr]";
			}
		}
		string += "[/table]";
		return string;
	}
	
	private String printFinalRoundTeamResults() {
		String string = "[table][tr][th align=center colspan=6]";
		string += extraInfo + hill.getName();
		string += " (HS ";
		string += hill.getHS();
		string += "), final round and final results ";
		if (teamResults.size() > 6)
			string += "(part 1)";
		string += "[/th][/tr][tr][td]#[/td][td]Name[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
		int placement = 1;
		int i = 0;
		for (i = 0; i < teamResults.size(); i++) {
			if (i == 6) {
				string += "[/table]\n\n[table][tr][th align=center colspan=6]";
				string += extraInfo + hill.getName();
				string += " (HS ";
				string += hill.getHS();
				string += "), final round and final results (part 2)[/th][/tr][tr][td]#[/td][td]Name[/td][td]L[/td][td]J[/td][td]P[/td][td][/td][/tr]";
			}
			string += "[tr][td][b]";
			if (i != 0 && teamResults.get(i).getTotalPoints() != teamResults.get(i-1).getTotalPoints())
				placement = i + 1;
			string += placement;
			string += "[/b][/td][td][b]";
			string += teamResults.get(i).getClub().getName();
			string += "[/b][/td][td][/td][td][/td][td][b]";
			string += teamResults.get(i).getTotalPoints();
			string += "[/b][/td][td][/td][/tr]";
			for (int j = 0; j < teamResults.get(i).getResults().size(); j++) {
				string += "[tr][td][/td][td]";
				string += teamResults.get(i).getResults().get(j).getAthlete().getName();
				string += "[/td][td]";
				if (teamResults.get(i).getResults().get(j).getJump2() != null) {
					string += teamResults.get(i).getResults().get(j).getJump2().getLength();
					string += "[/td][td]";
					string += teamResults.get(i).getResults().get(j).getJump2().getMarks();
				}
				else
					string += "[/td][td]";
				string += "[/td][td]";
				string += teamResults.get(i).getResults().get(j).getTotalPoints();
				string += "[/td][td]";
				if (teamResults.get(i).getResults().get(j).getJump2() != null)
					string += teamResults.get(i).getResults().get(j).getJump2().getSuffix();
				string += "[/td][/tr]";
			}
		}
		string += "[/table]";
		return string;
	}
	
	public ArrayList<Result> getResults() {
		return results;
	}
	
	public ArrayList<TeamResult> getTeamResults() {
		return teamResults;
	}
	
	public List<Result> getQualifyingAthletes() {
		return qualifyingAthletes;
	}
	
	public int getNumberOfPrequalified() {
		return numberOfPrequalified;
	}
	
	public static String getCompareMode() {
		return compareMode;
	}
	
	public static void setCompareMode(String compare) {
		compareMode = compare;
	}
}