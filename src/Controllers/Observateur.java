package Controllers;

public class Observateur {
	private int idObservateur;
	private String nomObservateur;
	
	public Observateur(int idObservateur, String nomObservateur) {
	        this.nomObservateur = nomObservateur;
	        this.idObservateur = idObservateur;
	 }
	 
	 public String getNomObservateur() {
	        return nomObservateur;
	    }
	 public int getIdObservateur() {
	        return idObservateur;
	    }

}