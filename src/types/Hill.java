package types;

public class Hill {

	private int id;
	private String name;
	private Nation nation;
	private int hs;
	private int k_point;
	private double hillRecord;
	private Athlete hillRecordAthlete;
	private String hillRecordDate;
	
	public Hill(int id, String name, Nation nation, int hs, int k_point,
		double hillRecord, Athlete hillRecordAthlete, String hillRecordDate) {
			this.id = id;
			this.name = name;
			this.nation = nation;
			this.hs = hs;
			this.k_point = k_point;
			this.hillRecord = hillRecord;
			this.hillRecordAthlete = hillRecordAthlete;
			this.hillRecordDate = hillRecordDate;
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

	public Nation getNation() {
		return nation;
	}

	public void setNation(Nation nation) {
		this.nation = nation;
	}

	public int getHS() {
		return hs;
	}

	public void setHS(int hs) {
		this.hs = hs;
	}

	public int getK_point() {
		return k_point;
	}

	public void setK_point(int k_point) {
		this.k_point = k_point;
	}

	public double getHillRecord() {
		return hillRecord;
	}

	public void setHillRecord(double hillRecord) {
		this.hillRecord = hillRecord;
	}

	public Athlete getHillRecordAthlete() {
		return hillRecordAthlete;
	}

	public void setHillRecordAthlete(Athlete hillRecordAthlete) {
		this.hillRecordAthlete = hillRecordAthlete;
	}

	public String getHillRecordDate() {
		return hillRecordDate;
	}

	public void setHillRecordDate(String hillRecordDate) {
		this.hillRecordDate = hillRecordDate;
	}
}