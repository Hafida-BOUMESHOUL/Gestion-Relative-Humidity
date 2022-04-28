package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class RHController implements Initializable {
	
	@FXML private Label idS;
	@FXML private Label idO;
	@FXML private Label id;
	@FXML private Label heureL;
	@FXML private Label dateL;
	
	@FXML private TextField sec;
    @FXML private TextField mou;
    @FXML private TextField hum;
    @FXML private TextField ma;
    @FXML  private TextField mi;
    @FXML private TextField max;
    @FXML private TextField min;
    @FXML  private TextField moy;
    
    @FXML private ComboBox<String> heureMesure;
    @FXML private ComboBox<String> station;
    @FXML private ComboBox<String> observateur;
    
    @FXML private DatePicker dateMesure;
    
    @FXML private TableView<RH> tableRH;
    @FXML private TableColumn<RH,Integer> idRH;
    @FXML private TableColumn<RH,Double> secT;
    @FXML private TableColumn<RH,Double> mouT;
    @FXML private TableColumn<RH,Double> humT;
    @FXML private TableColumn<RH,Double> maT;
    @FXML private TableColumn<RH,Double> miT;
    @FXML private TableColumn<RH,Double> maxT;
    @FXML private TableColumn<RH,Double> minT;
    @FXML private TableColumn<RH,Double> moyT;
    @FXML private TableColumn<RH,String> heureT;
    @FXML private TableColumn<RH,String> dateT;
    @FXML private TableColumn<RH,String> verifyT;
    @FXML private TableColumn<RH,String> stati;
    @FXML private TableColumn<RH,String> obser;
    
    private String[] heure = {"7h" , "14h" , "18h" , "21h"};
    private ObservableList<String> stat = FXCollections.observableArrayList();
    private ObservableList<String> obs = FXCollections.observableArrayList();
	
    public void goHome(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/Home.fxml"));
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
	
	public void goPrint(ActionEvent event) throws IOException {
		
		Parent root = FXMLLoader.load(getClass().getResource("/View/Print.fxml"));
		Stage  stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
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

    		station.getItems().addAll(stat);
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
    		observateur.getItems().addAll(obs);
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public int getStationId(){
        int idst = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            String query = "SELECT * FROM station";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString("nomStation").equals(station.getValue())){
                    idst = resultSet.getInt("idStation");
                    break;
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return idst;
    }
	 
    public void setStationId(ActionEvent event) {
        idS.setText(Integer.toString(getStationId()));

    }

    public void setObservateurId(ActionEvent event) {
    	int idob = 0;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            String query = "SELECT * FROM observateur";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString("nomObservateur").equals(observateur.getValue())){
                    idob = resultSet.getInt("idObservateur");
                    break;
                }
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        idO.setText(Integer.toString(idob));
    }
	
	public double calculRH() {
		double dry;
		double wet;
		long rh;
		double p = 101.3;
		double a;
		double eswb;
		double ed;
		double esdb;
			
		dry = Double.parseDouble(sec.getText());
		wet = Double.parseDouble(mou.getText());
				
		a = 0.00066*(1.0 + 0.00115*wet);
		eswb = Math.exp((16.78*wet-116.9)/(wet+237.3));
		ed = eswb - a*p*(dry-wet);
		esdb = Math.exp((16.78*dry-116.9)/(dry+237.3));
		rh = Math.round(100.0*(ed/esdb));
		
		return rh;
	}
	
	public void setValHum(MouseEvent event) {
		calculRH();
        hum.setText(Double.toString(calculRH()));
    }
	
	public double calculMoy() {
		double maxi;
		double mini;
		
		maxi = Double.parseDouble(max.getText());
		mini = Double.parseDouble(min.getText());
		
		return (maxi+mini)/2;
	}
	
	public void setValMoy(MouseEvent event) {
		calculMoy();
        moy.setText(Double.toString(calculMoy()));
    }
	
	public void settingHour(ActionEvent e) {
		if(heureMesure.getValue().equals("21h")) {
			ma.setEditable(true);
			mi.setEditable(true);
			max.setEditable(true);
			min.setEditable(true);
			moy.setEditable(true);
			
			}
			else {
				ma.setText("0");
				ma.setEditable(false);
				mi.setText("0");
				mi.setEditable(false);
				max.setText("0");
				max.setEditable(false);
				min.setText("0");
				min.setEditable(false);
				moy.setText("0");
				moy.setEditable(false);
				
			}	
		}
	public boolean checkSecMouExist() {
    	boolean check = false;
    	try {
    		    Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    			String query = "SELECT * FROM relativehumidity";
    			Statement statement = connection.createStatement();
    			ResultSet resultSet = statement.executeQuery(query);
    			while (resultSet.next()) {
    				if(resultSet.getDouble("sec") == Double.parseDouble(sec.getText()) 
    				&& resultSet.getDouble("mou") == Double.parseDouble(mou.getText()) ){
    					check = true;
    					break;
    				}
    			}
    			connection.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return check;
    }
	
	 public void clearFieldsRH() {
	    	idO.setText("idO");
	    	idS.setText("idS");
	    	id.setText("id");
	    	heureL.setText("Heure");
	    	dateL.setText("Date");
	    	sec.clear();
	    	mou.clear();
	    	hum.clear();
	    	hum.setEditable(true);
	    	ma.clear();
	    	ma.setEditable(true);
	    	mi.clear();
	    	mi.setEditable(true);
	    	max.clear();
	    	max.setEditable(true);
	    	min.clear();
	    	min.setEditable(true);
	    	moy.clear();
	    	moy.setEditable(true);
	    	heureMesure.setPromptText("Heure");
	    	heureMesure.setValue("Heure");
	    	dateMesure.setPromptText("Date De Mesure");
	    	station.setPromptText("Station");
	    	station.setValue("Station");
	    	observateur.setPromptText("Observateur");
	    	observateur.setValue("Observateur");
	    }
    
	public void ajouterRH(ActionEvent e) {
		try {
    		Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    		String query1 = "SELECT * FROM relativehumidity";
    		Statement statement1 = connection.createStatement();
    		ResultSet resultSet = statement1.executeQuery(query1);
    		
    		if (sec.getText().equals("") || mou.getText().equals("") || hum.getText().equals("") || ma.getText().equals("")
    			|| mi.getText().equals("") || max.getText().equals("") || min.getText().equals("") || moy.getText().equals("")
    			|| heureMesure.getSelectionModel().isEmpty() || station.getSelectionModel().isEmpty() || observateur.getSelectionModel().isEmpty()
    			|| dateMesure.getEditor().getText().equals("")) {
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
    			if(checkSecMouExist() || Double.parseDouble(hum.getText()) != calculRH()) {
    				
					String query = "INSERT INTO relativehumidity(sec,mou,hum,thMin,thMax,thMoy,thMA,thMI,dateMesure,dateChar,heureMesure,Verify,idObservateur,idStation) "
    						+ "VALUES ('"+ sec.getText() + "','" + mou.getText()+ "','" + hum.getText() + "','" + min.getText() + "','"+ max.getText() + "','" + moy.getText()
    						+ "','" + ma.getText()+ "','" + mi.getText() + "','" + dateMesure.getValue() + "','" + dateMesure.getEditor().getText() 
    						+ "','" + heureMesure.getValue() + "','" + "True" + "','" + idO.getText()+ "','" + idS.getText() + "')";
    				hum.setEditable(true);
    				Statement statement = connection.createStatement();
    				statement.executeUpdate(query);
  
    				connection.close();
    				
    				Alert alert = new Alert(Alert.AlertType.INFORMATION);
    				alert.setTitle("ERROR!");
    				alert.setHeaderText("Informations ajoutées avec succès (Valeur d'humidité vérifiée)!");
    				alert.setContentText("Cliquer OK");
    				alert.show();
    				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    				Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
    				clearFieldsRH();
    				heureMesure.setPromptText("Heure");
    		    	heureMesure.setValue("Heure");
    		    	dateMesure.setPromptText("Date De Mesure");
    		    	station.setPromptText("Station");
    		    	station.setValue("Station");
    		    	observateur.setPromptText("Observateur");
    		    	observateur.setValue("Observateur");
    				stage.getIcons().add(myIcone);
    			}
				
				  else if(checkSecMouExist() && resultSet.getDouble("hum") == Double.parseDouble(hum.getText()) 
						  && resultSet.getDouble("thMax") == Double.parseDouble(max.getText()) 
						  && resultSet.getDouble("thMin") == Double.parseDouble(min.getText()) 
						  && resultSet.getDouble("thMoy") == Double.parseDouble(moy.getText()) 
						  && resultSet.getDouble("thMA") == Double.parseDouble(ma.getText()) 
						  && resultSet.getDouble("thMI") == Double.parseDouble(mi.getText()) 
						  && resultSet.getInt("idObservateur") == Integer.parseInt(idO.getText()) 
						  && resultSet.getInt("idStation") == Integer.parseInt(idS.getText()) 
						  && resultSet.getString("heureMesure").equals(heureMesure.getValue()) 
						  && resultSet.getString("dateChar").equals(dateMesure.getEditor().getText())) {
				  
				  Alert alert = new Alert(Alert.AlertType.ERROR);
				  alert.setTitle("ERROR!"); 
				  alert.setHeaderText("Ces informations existent déjà"); 
				  alert.setContentText("Cliquer OK"); 
				  alert.show(); 
				  Stage stage = (Stage) alert.getDialogPane().getScene().getWindow(); 
				  Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
				  clearFieldsRH();
				  heureMesure.setPromptText("Heure");
				  heureMesure.setValue("Heure");
				  dateMesure.setPromptText("Date De Mesure");
				  station.setPromptText("Station");
				  station.setValue("Station");
				  observateur.setPromptText("Observateur");
				  observateur.setValue("Observateur");
				  stage.getIcons().add(myIcone); 
				  } else {
					  
    				String query = "INSERT INTO relativehumidity(sec,mou,hum,thMin,thMax,thMoy,thMA,thMI,dateMesure,dateChar,heureMesure,Verify,idObservateur,idStation) "
    						+ "VALUES ('"+ sec.getText() + "','" + mou.getText()+ "','" + hum.getText() + "','" + min.getText() + "','"+ max.getText() + "','" + moy.getText()
    						+ "','" + ma.getText()+ "','" + mi.getText() + "','" + dateMesure.getValue() + "','" + dateMesure.getEditor().getText() + "','"
    						+ heureMesure.getValue() + "','" + "False" + "','" + idO.getText()+ "','" + idS.getText() + "')";
    				
    				Statement statement = connection.createStatement();
    				statement.executeUpdate(query);
  
    				connection.close();
    				
    				Alert alert = new Alert(Alert.AlertType.INFORMATION);
    				alert.setTitle("ERROR!");
    				alert.setHeaderText("Informations ajoutées avec succès (Valeur d'humidité non vérifiée)!");
    				alert.setContentText("Cliquer OK");
    				alert.show();
    				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    				Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
    				clearFieldsRH();
    				heureMesure.setPromptText("Heure");
    		    	heureMesure.setValue("Heure");
    		    	dateMesure.setPromptText("Date De Mesure");
    		    	station.setPromptText("Station");
    		    	station.setValue("Station");
    		    	observateur.setPromptText("Observateur");
    		    	observateur.setValue("Observateur");
    				stage.getIcons().add(myIcone);
    			}
    		} 
    	}catch (SQLException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
		
	}
	
	public ObservableList<RH> getRHList() {

    	ObservableList<RH> RHList = FXCollections.observableArrayList();
    	Connection connection = getConnection();
    	String query = "SELECT * FROM relativehumidity";
    	Statement statement;
    	ResultSet resultSet;

    	try {
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(query);
    		RH rh;
    		while (resultSet.next()) {
    			rh = new RH(resultSet.getInt("idRH"), resultSet.getDouble("sec"), resultSet.getDouble("mou"), resultSet.getDouble("hum"), 
    					 resultSet.getDouble("thMax"), resultSet.getDouble("thMin"), resultSet.getDouble("thMoy"), resultSet.getDouble("thMA"),
    					 resultSet.getDouble("thMI"), resultSet.getString("dateChar"), resultSet.getString("heureMesure"), resultSet.getString("Verify"),
    					 resultSet.getInt("idStation"),resultSet.getInt("idObservateur") );
    			RHList.add(rh);
    		}
    		connection.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return RHList;
    }

    public void showRH(ActionEvent e) {

    	ObservableList<RH> list = getRHList();

    	idRH.setCellValueFactory(new PropertyValueFactory<RH, Integer>("idRH"));
    	secT.setCellValueFactory(new PropertyValueFactory<RH, Double>("sec"));
    	mouT.setCellValueFactory(new PropertyValueFactory<RH, Double>("mou"));
    	humT.setCellValueFactory(new PropertyValueFactory<RH, Double>("hum"));
    	minT.setCellValueFactory(new PropertyValueFactory<RH, Double>("thMin"));
    	maxT.setCellValueFactory(new PropertyValueFactory<RH, Double>("thMax"));
    	moyT.setCellValueFactory(new PropertyValueFactory<RH, Double>("thMoy"));
    	maT.setCellValueFactory(new PropertyValueFactory<RH, Double>("thMA"));
    	miT.setCellValueFactory(new PropertyValueFactory<RH, Double>("thMI"));
    	dateT.setCellValueFactory(new PropertyValueFactory<RH, String>("dateChar"));
    	heureT.setCellValueFactory(new PropertyValueFactory<RH, String>("heureMesure"));
    	verifyT.setCellValueFactory(new PropertyValueFactory<RH, String>("Verify"));
    	stati.setCellValueFactory(new PropertyValueFactory<RH, String>("nomStation"));
    	obser.setCellValueFactory(new PropertyValueFactory<RH, String>("nomObservateur"));
    	
    	tableRH.setItems(list);
    }
    
    @FXML private void RHMouseAction(MouseEvent event) throws SQLException {

    	if(!tableRH.getSelectionModel().isEmpty()){
    		RH rh = tableRH.getSelectionModel().getSelectedItem();
    		id.setText("" + rh.getId());
    		idO.setText("" + rh.getIdO());
    		idS.setText("" + rh.getIdS());
    		
    		Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    		String query1 = "SELECT * FROM station";
    		Statement statement1 = connection.createStatement();
    		ResultSet resultSet = statement1.executeQuery(query1);
    		String nomSt = null;
    		while(resultSet.next()) {
    			 if(resultSet.getInt("idStation")==Integer.parseInt(idS.getText())){
                     nomSt = resultSet.getString("nomStation");
                     break;
                 }
    		}
    		station.setValue(nomSt);
    		
    		String query2 = "SELECT * FROM observateur";
    		Statement statement2 = connection.createStatement();
    		ResultSet resultSet2 = statement2.executeQuery(query2);
    		String nomOb = null;
    		while(resultSet2.next()) {
    			 if(resultSet2.getInt("idObservateur")==Integer.parseInt(idO.getText())){
                     nomOb = resultSet2.getString("nomObservateur");
                     break;
                 }
    		}
    		observateur.setValue(nomOb);
    		
    		dateL.setText("" + rh.getDate());
    		heureL.setText("" + rh.getHeure());
    		sec.setText("" + rh.getSec());
    		mou.setText("" + rh.getMou());
    		hum.setText("" + rh.getHum());
    		ma.setText("" + rh.getMA());
    		mi.setText("" + rh.getMI());
    		max.setText("" + rh.getMax());
    		min.setText("" + rh.getMin());
    		moy.setText("" + (rh.getMax()+rh.getMin())/2);
    		heureMesure.setValue("" + rh.getHeure());
    		dateMesure.setPromptText("" + rh.getDate());
    		
    		connection.close();
    	}
    }
    
    public void modifierRH(ActionEvent e) throws SQLException {
    	try {
    		if(tableRH.getSelectionModel().isEmpty()){
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("ERROR!");
    			alert.setHeaderText("selectionnez la ligne a modifier!");
    			alert.setContentText("Cliquer OK pour reessayer");
    			alert.show();
    			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    			Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    			stage.getIcons().add(myIcone);
    		}else{
    			Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    			String query = "UPDATE relativehumidity SET sec = '" + sec.getText() + "', mou = '" + mou.getText() + "', hum = '" + hum.getText()
    			+ "', thMin = '" + min.getText() + "', thMax = '" + max.getText() + "', thMoy = '" + moy.getText() + "', thMA = '" + ma.getText()
    			+ "', thMI = '" + mi.getText()+ "', dateMesure = '" + dateMesure.getEditor().getText() + "', idObservateur = '" + idO.getText() 
    			+ "', idStation = '" + idS.getText() + "' WHERE idRH = " + id.getText() + "";
    			Statement statement = connection.createStatement();
    			statement.executeUpdate(query);
    			connection.close();
  
    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
    			alert.setTitle("Confirmation!");
    			alert.setHeaderText("Informations modifiées avec succès!");
    			alert.setContentText("Cliquer Ok");
    			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    			Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
    			stage.getIcons().add(myIcone);
    			showRH(e);
    			clearFieldsRH();
    			heureMesure.setPromptText("Heure");
    	    	heureMesure.setValue("Heure");
    	    	dateMesure.setPromptText("Date De Mesure");
    	    	station.setPromptText("Station");
    	    	station.setValue("Station");
    	    	observateur.setPromptText("Observateur");
    	    	observateur.setValue("Observateur");
    			alert.showAndWait();
    		} 
    	}catch (Exception exception) {
    		exception.printStackTrace();
    	}
    }
    
    public void handleDeleteRH(ActionEvent event){

    	if(tableRH.getSelectionModel().isEmpty()){
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("ERROR!");
    		alert.setHeaderText("selectionnez la ligne a supprimer!");
    		alert.setContentText("Cliquer OK pour reessayer");
    		alert.show();
    		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    		Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    		stage.getIcons().add(myIcone);
    	}else{
    		Connection connection;
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    		
    		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    		alert.setTitle("Confirmation!");
    		alert.setHeaderText(null);
    		alert.setContentText("étes vous sur de vouloir supprimer ces valeurs!");
    		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    		Image myIcone = new Image("/View/ressources/warning.png");
    		stage.getIcons().add(myIcone);
    		Optional <ButtonType> action = alert.showAndWait();
    		
    		if(action.get() == ButtonType.OK) {
    			
    			String query = "DELETE FROM relativehumidity WHERE idRH = " + id.getText() + "";
        		Statement statement = connection.createStatement();
        		statement.executeUpdate(query);
        		connection.close();
      
        		Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        		alert1.setTitle("Confirmation!");
        		alert1.setHeaderText("Valeurs supprimées avec succès!");
        		alert1.setContentText("Cliquer Ok");
        		Stage stage1 = (Stage) alert1.getDialogPane().getScene().getWindow();
        		Image myIcone1 = new Image("/View/ressources/iconfinder_Info_728979.png");
        		stage1.getIcons().add(myIcone1);
        		clearFieldsRH();
        		showRH(event);
      			alert1.showAndWait();
        	}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	
    }
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		heureMesure.getItems().addAll(heure);
		fillObsBox();
		fillStationBox();
		moy.setEditable(false);
		station.setOnAction(this::setStationId);
		observateur.setOnAction(this::setObservateurId);
		heureMesure.setOnAction(this::settingHour);
		
		dateMesure.setConverter(new StringConverter<LocalDate>() {
			 String pattern = "dd-MM-yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			 {
				 dateMesure.setPromptText(pattern.toLowerCase());
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
