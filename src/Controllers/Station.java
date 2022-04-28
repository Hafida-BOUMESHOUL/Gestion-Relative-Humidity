package Controllers;

public class Station {
	private String nomStation;
	private int idStation;
	
	public Station(int idStation,String nomStation) {
		this.nomStation = nomStation;
		this.idStation = idStation;
	}

	public String getNomStation() {
		return nomStation;
	}
	
	public int getIdStation() {
		return idStation;
	}

}