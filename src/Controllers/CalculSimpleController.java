package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CalculSimpleController {

	@FXML private TextField secCS;
    @FXML private TextField mouCS;
    @FXML private TextField humCS;
    
public void goRH(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/RH.fxml"));
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
	
	public void goPrint(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/Print.fxml"));
		Stage  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public double calculRH() {
		double dry;
		double wet;
		double rh = 0;
		double p = 101.3;
		double a;
		double eswb;
		double ed;
		double esdb;
		
		try {
		    Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
			String query = "SELECT * FROM relativehumidity WHERE Verify = 'True'" ;
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				if(resultSet.getDouble("sec") == Double.parseDouble(secCS.getText()) 
				&& resultSet.getDouble("mou") == Double.parseDouble(mouCS.getText()) ){
					rh = resultSet.getDouble("hum");
				}
			}
	if(rh == 0) {		
		dry = Double.parseDouble(secCS.getText());
		wet = Double.parseDouble(mouCS.getText());
				
		a = 0.00066*(1.0 + 0.00115*wet);
		eswb = Math.exp((16.78*wet-116.9)/(wet+237.3));
		ed = eswb - a*p*(dry-wet);
		esdb = Math.exp((16.78*dry-116.9)/(dry+237.3));
		rh = Math.round(100.0*(ed/esdb));
	}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rh;
	}
	
	 public void clearFieldsRH() {
	    	secCS.clear();
	    	mouCS.clear();
	    	humCS.clear();
	    	}
	
	public void calculerRH(ActionEvent event) {
		
		calculRH();
        humCS.setText(Double.toString(calculRH()));
		
		Connection connection;
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
		 
        String query1 = "INSERT INTO RH(secCS,mouCS,humCS) "
				+ "VALUES ('"+ secCS.getText() + "','" + mouCS.getText()+ "','" + humCS.getText()+ "')";
		Statement statement1 = connection.createStatement();
		statement1.executeUpdate(query1);

		connection.close();
		
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("ERROR!");
		alert.setHeaderText("Informations ajoutées avec succès!");
		alert.setContentText("Cliquer OK");
		alert.show();
		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
	///	clearFieldsRH();
		stage.getIcons().add(myIcone);
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
    }
	
}
