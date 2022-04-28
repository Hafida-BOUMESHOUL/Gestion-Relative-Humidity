package Controllers;

public class RH {
	private int idRH;
	private double sec;
	private double mou;
	private double hum;
	private double thMax;
	private double thMin;
	private double thMoy;
	private double thMA;
	private double thMI;
	private String dateChar;
	private String heureMesure;
	private String Verify;
	private int idS;
	private int idO;
	
	public RH(int idRH,double sec, double mou, double hum, double thMax, double thMin, double thMoy, double thMA, double thMI, String dateChar, String heureMesure, String Verify, int idS, int idO) {
	        this.idRH = idRH;
	        this.sec = sec;
	        this.mou = mou;
	        this.hum = hum;
	        this.thMax = thMax;
	        this.thMin = thMin;
	        this.thMA = thMA;
	        this.thMI = thMI;
	        this.dateChar = dateChar;
	        this.heureMesure = heureMesure;
	        this.Verify = Verify;
	        this.idO = idO;
	        this.idS = idS;
	}
	
	public int getId() {
		return idRH;
	}
	
	public double getSec() {
		return sec;
	}
	
	public double getMou() {
		return mou;
	}
	
	public double getHum() {
		return hum;
	}
	
	public double getMax() {
		return thMax;
	}
	
	public double getMin() {
		return thMin;
	}
	
	public double getMoy() {
		return thMoy;
	}
	
	public double getMA() {
		return thMA;
	}
	
	public double getMI() {
		return thMI;
	}
	
	 public String getDate() {
		 return dateChar;
	 }
	 
	 public String getHeure() {
		 return heureMesure;
	 }
	 
	 public String getVerify() {
		 return Verify;
	 }
	 
	 public int getIdS() {
			return idS;
	 }
	 
	public int getIdO() {
			return idO;
	}
}
