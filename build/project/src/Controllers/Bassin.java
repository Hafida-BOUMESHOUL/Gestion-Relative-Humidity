package Controllers;

public class Bassin {
	private String nomBassin;
	private int idBassin;
	public Bassin(int idBassin,String nomBassin) {
	        this.nomBassin = nomBassin;
	        this.idBassin = idBassin;
	}
	 
	 public String getNomBassin() {
	        return nomBassin;
	 }
	 
	 public int getIdBassin() {
	        return idBassin;
	 }
}
