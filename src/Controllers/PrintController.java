package Controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.util.StringConverter;

	public class PrintController implements Initializable{

		@FXML private DatePicker datePrint;
    	@FXML private ComboBox<String> stationPrint;
    	@FXML private ComboBox<String> obsPrint;
    	@FXML private Label idS;
    	@FXML private Label idO;

    	private ObservableList<String> stat = FXCollections.observableArrayList();
    	private ObservableList<String> obs = FXCollections.observableArrayList();
	
    public void goRH(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/RH.fxml"));
		Stage  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void goCS(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/CalculSimple.fxml"));
		Stage  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void goHome(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
		Stage  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//SELECT datemesure, datechar, strftime('%Y', datemesure)as Year, strftime('%d', datemesure)as Day, strftime('%m', datemesure)as Month FROM relativehumidity;
	
	public Connection getConnection() {

        Connection connection;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            return connection;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }

    }
	
	public void fillStationBox() {

        String query = "SELECT * FROM station";
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
            	stat.add(resultSet.getString("nomStation"));
            }

    		stationPrint.getItems().addAll(stat);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
	public void fillObsBox() {
		String query = "SELECT * FROM observateur"; //WHERE idStation = " + getStationId();
        try {

            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {

                obs.add(resultSet.getString("nomObservateur"));
            }
    		obsPrint.getItems().addAll(obs);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public int getStationId(){
        int id = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            
            String query = "SELECT * FROM station";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            
            while (resultSet.next()) {
                if(resultSet.getString("nomStation").equals(stationPrint.getValue())){
                    id = resultSet.getInt("idStation");
                    break;
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
	 
    public void setStationId(ActionEvent event) {
        idS.setText(Integer.toString(getStationId()));
    }
	
	public void getObservateurId(ActionEvent event){
        int id = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            String query = "SELECT * FROM observateur";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString("nomObservateur").equals(obsPrint.getValue())){
                    id = resultSet.getInt("idObservateur");
                    break;
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        idO.setText(Integer.toString(id));

        Connection connection;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
		
        String query = "DELETE FROM rhprint" ;
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        String m = "" + datePrint.getValue().getMonthValue();
        if(datePrint.getValue().getMonthValue()<10) {
        	m = "0"+datePrint.getValue().getMonthValue();
        }
        String query1 = "SELECT sec,mou,hum,thMAx,thMin,thMoy,thMA,thMI,heureMesure,dateMesure,"
        		+ " strftime('%m', dateMesure),strftime('%Y', dateMesure),strftime('%d', dateMesure),"
        		+ " idStation, idObservateur FROM relativehumidity"
        		+ " WHERE idStation = " + idS.getText() 
        		+ " AND idObservateur = " + idO.getText() 
        		+ " AND  strftime('%m', dateMesure) = '" + m
        		+ "' AND  strftime('%Y', dateMesure) = '" + datePrint.getValue().getYear() + "'";
        
        System.out.println(datePrint.getValue().getMonthValue());
        System.out.println(datePrint.getValue().getYear());
        
        Statement statement1 = connection.createStatement();
        ResultSet resultSet1 = statement1.executeQuery(query1);
        
        while(resultSet1.next()) {

        	double sec7 = 0;
        	double mou7 = 0;
        	double hum7 = 0;
        	double sec14 = 0;
        	double mou14 = 0;
        	double hum14 = 0;
        	double sec18 = 0;
        	double mou18 = 0;
        	double hum18 = 0;
        	double sec21 = 0;
        	double mou21 = 0;
        	double hum21 = 0;
        	double max = 0;
        	double min = 0;
        	double moy = 0;
        	double ma = 0;
        	double mi = 0;
        	int jour;
        	String mois;
        	int annee;

        	jour = Integer.parseInt(resultSet1.getString("strftime('%d', datemesure)"));
        	mois = resultSet1.getString("strftime('%m', datemesure)");
        	annee = Integer.parseInt(resultSet1.getString("strftime('%Y', datemesure)"));

        	if(resultSet1.getString("heureMesure").equals("7h")) {
        		sec7 = resultSet1.getDouble("sec");
        		mou7 = resultSet1.getDouble("mou");
        		hum7 = resultSet1.getDouble("hum");
        	}

        	resultSet1.next();
        	if(resultSet1.getString("heureMesure").equals("14h")) {
        		sec14 = resultSet1.getDouble("sec");
        		mou14 = resultSet1.getDouble("mou");
        		hum14 = resultSet1.getDouble("hum");
        	}
        	resultSet1.next();
        	if(resultSet1.getString("heureMesure").equals("18h")) {
        		sec18 = resultSet1.getDouble("sec");
        		mou18 = resultSet1.getDouble("mou");
        		hum18 = resultSet1.getDouble("hum");
        	}
        	resultSet1.next();
        	if(resultSet1.getString("heureMesure").equals("21h")) {
        		max = resultSet1.getDouble("thMax");
        		min = resultSet1.getDouble("thMin");
        		moy = resultSet1.getDouble("thMoy");
        		ma = resultSet1.getDouble("thMA");
        		mi = resultSet1.getDouble("thMI");
        		sec21 = resultSet1.getDouble("sec");
        		mou21 = resultSet1.getDouble("mou");
        		hum21 = resultSet1.getDouble("hum");
        		
        	}
        	
        	String query2 = "INSERT INTO rhprint (sec7, mou7, hum7, sec14, mou14, hum14,"
        			+ " sec18, mou18, hum18, sec21, mou21, hum21, max21, min21, moy21,"
        			+ " ma21, mi21, minSol, jour, mois, annee, idObservateur, idStation) "
        	+ "VALUES ('" + sec7 + "','"+ mou7 + "','"+ hum7 + "','" + sec14 + "','"+ mou14 + "','"+ hum14
        	+ "','" + sec18 + "','"+ mou18 + "','"+ hum18 + "','" + sec21 + "','"+ mou21 + "','"+ hum21 
        	+ "','"+ max + "','"+ min + "','" + moy + "','"+ ma + "','"+ mi + "','"+ "--" + "','" 
        	+ jour + "','"+ mois +"','" + annee + "','" +idO.getText()+ "','" +idS.getText()+ "')" ;
            Statement statement2 = connection.createStatement();
            statement2.executeUpdate(query2);
            
			/*
			 * Alert alert = new Alert(Alert.AlertType.INFORMATION);
			 * alert.setTitle("ERROR!");
			 * alert.setHeaderText("Informations ajoutées avec succès !!");
			 * alert.setContentText("Cliquer OK"); alert.show(); Stage stage = (Stage)
			 * alert.getDialogPane().getScene().getWindow(); Image myIcone = new
			 * Image("/View/ressources/iconfinder_Info_728979.png");
			 * stage.getIcons().add(myIcone);
			 */
        }
        
        connection.close();
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void print(ActionEvent e){
    	Document doc = new Document();
    	String query = "SELECT * FROM rhprint";
    	
    	if(datePrint.getEditor().getText().equals("") || stationPrint.getSelectionModel().isEmpty() 
    			|| obsPrint.getSelectionModel().isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("ERROR!");
			alert.setHeaderText("Veuillez remplir tous les champs");
			alert.setContentText("Cliquer OK pour réessayer");
			alert.show();
			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
			stage.getIcons().add(myIcone);
    	}
    	else {
    	
    		try {
    			Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    			Statement statement = connection.createStatement();
    	        ResultSet resultSet = statement.executeQuery(query);
    			
    			PdfWriter.getInstance(doc, new FileOutputStream("D:\\RH.pdf"));
			
    			doc.open();
    			PdfPTable table = new PdfPTable(19);
			
    			table.setWidthPercentage(100);
			
    			PdfPCell cell;
			
    			cell = new PdfPCell (new Phrase("BULLETIN MENSUEL THERMOMETRIQUE", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(18);
    			table.addCell(cell);
			
    			cell = new PdfPCell (new Phrase("1", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    			table.addCell(cell);
			
    			cell = new PdfPCell (new Phrase("STATION : "+ stationPrint.getValue().toUpperCase() +"\nObservateur : "
    			+obsPrint.getValue().toUpperCase()+ "\nANNEE : " + datePrint.getValue().getYear(), FontFactory.getFont("Comic Sans MS",11)));
    			cell.setColspan(5);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("DIRECTION DE LA RECHERCHE ET DE LA PLANIFICATION DE L'EAU\nMOIS : " + datePrint.getValue().getMonth(), 
    					FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(9);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("STATION : "+ stationPrint.getValue().toUpperCase() +"\nANNEE : "+datePrint.getValue().getYear()+
    					"\nMOIS : " +datePrint.getValue().getMonthValue(), 
    					FontFactory.getFont("Comic Sans MS",11)));
    			cell.setColspan(5);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("JOUR", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setRowspan(4);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("THERMOMETRE SEC ET MOUILLE", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(12);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("THERMOMETRE MAX ET MIN", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(6);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("7 heures", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(3);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("14 heures", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(3);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("18 heures", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(3);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("21 heures", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(3);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("21 heures", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(5);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("7 h", FontFactory.getFont("Comic Sans MS",11)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			table.addCell(cell);
    			
    			for (int i=0;i<4;i++) {
    				cell = new PdfPCell (new Phrase("SEC", FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("MOU", FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("HUM%", FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			table.addCell(cell);
    			}
    			
    			cell = new PdfPCell (new Phrase("MAX", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setRowspan(2);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("MIN", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setRowspan(2);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("MOY", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setRowspan(2);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("Amorçage", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setColspan(2);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("MIN SOL", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			cell.setRowspan(2);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("MA", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			table.addCell(cell);
    			
    			cell = new PdfPCell (new Phrase("MI", FontFactory.getFont("Comic Sans MS",8)));
    			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
    			table.addCell(cell);
    			
    			for (int i=1;i<=19;i++) {
    				cell = new PdfPCell (new Phrase("" + i, FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
    			}
    			
    			///////////////////////////////////////////////////////////////////////////////////////////
    			
    			while(resultSet.next()) {
    				cell = new PdfPCell (new Phrase(Integer.toString(resultSet.getInt("jour")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("sec7")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
    				
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("mou7")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("hum7")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("sec14")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("mou14")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("hum14")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("sec18")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("mou18")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("hum18")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("sec21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("mou21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("hum21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("max21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("min21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("moy21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("ma21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet.getDouble("mi21")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(resultSet.getString("minSol"), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
    			}
    			

    	    	String query1 = "SELECT SUM(hum7),SUM(hum14),SUM(hum18),SUM(hum21),SUM(moy21)"
    	    			+ " ,AVG(hum7),AVG(hum14),AVG(hum18),AVG(hum21),AVG(moy21)"
    	    			+ " ,MAX(max21), MIN(min21) FROM rhprint";
    	    	Statement statement1 = connection.createStatement();
    	        ResultSet resultSet1 = statement1.executeQuery(query1);
    	        
    	        if(resultSet1.next()) {
    	        	
    	        	cell = new PdfPCell (new Phrase("TOTAL", FontFactory.getFont("Comic Sans MS",7)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("X", FontFactory.getFont("Comic Sans MS",20)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			cell.setColspan(2);        			
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("SUM(hum7)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("X", FontFactory.getFont("Comic Sans MS",20)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			cell.setColspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("SUM(hum14)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("X", FontFactory.getFont("Comic Sans MS",20)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			cell.setColspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("SUM(hum18)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("X", FontFactory.getFont("Comic Sans MS",20)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			cell.setColspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("SUM(hum21)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("Max\n"+Double.toString(resultSet1.getDouble("MAX(max21)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("Min\n"+Double.toString(resultSet1.getDouble("MIN(min21)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("SUM(moy21)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("X", FontFactory.getFont("Comic Sans MS",20)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			cell.setRowspan(2);
        			cell.setColspan(2);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("Min", FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("MOY", FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("AVG(hum7)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("AVG(hum14)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("AVG(hum18)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("AVG(hum21)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase(Double.toString(resultSet1.getDouble("AVG(moy21)")), FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
        			
        			cell = new PdfPCell (new Phrase("--", FontFactory.getFont("Comic Sans MS",8)));
        			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        			table.addCell(cell);
    	        }
			
    			doc.add(table);
			
    			doc.close();
    			Desktop.getDesktop().open(new File("D:\\RH.pdf"));
    			connection.close();
			
    		} catch (FileNotFoundException | DocumentException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
    		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
    		} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		fillStationBox();
		fillObsBox();
		stationPrint.setOnAction(this::setStationId);
		obsPrint.setOnAction(this::getObservateurId);
		
		 datePrint.setConverter(new StringConverter<LocalDate>() {
			 String pattern = "dd-MM-yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			 {
			     datePrint.setPromptText(pattern.toLowerCase());
			 }

			 @Override public String toString(LocalDate date) {
			     if (date != null) {
			         return dateFormatter.format(date);
			     } else {
			         return "";
			     }
			 }

			 @Override public LocalDate fromString(String string) {
			     if (string != null && !string.isEmpty()) {
			         return LocalDate.parse(string, dateFormatter);
			     } else {
			         return null;
			     }
			 }
			});
		
	}
}
