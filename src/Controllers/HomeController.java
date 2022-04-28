package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	@FXML private Label idB;
	@FXML private Label idS;
	@FXML private Label idO;
	
	@FXML private TextField nomBassin;
	@FXML private TextField nomStation;
	@FXML private TextField nomObservateur;
	
	@FXML private TableView<Bassin> bassinTable;
    @FXML private TableColumn<Bassin,Integer> idBassin;
    @FXML private TableColumn<Bassin,String> nomBTable;
    
	@FXML private TableView<Station> stationTable;
    @FXML private TableColumn<Station,Integer> idStation;
    @FXML private TableColumn<Station,String> nomSTable;

	@FXML private TableView<Observateur> observateurTable;
    @FXML private TableColumn<Observateur,Integer> idObservateur;
    @FXML private TableColumn<Observateur,String> nomOTable;
    
    @FXML private Button ajouterBas;
    @FXML private Button modifierBas;
    @FXML private Button supprimerBas;
    @FXML private Button rechercherBas;
    
    @FXML private Button ajouterStat;
    @FXML private Button modifierStat;
    @FXML private Button supprimerStat;
    @FXML private Button rechercherStat;
    
    @FXML private Button ajouterObs;
    @FXML private Button modifierObs;
    @FXML private Button supprimerObs;
    @FXML private Button rechercherObs;

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

	public boolean checkBassinExist() {
        boolean check = false;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            String query = "SELECT * FROM bassin";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString("nomBassin").equals(nomBassin.getText())){
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
	
	public void ajouterBassin(ActionEvent e) throws IOException {
        try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
			
			if(nomBassin.getText().equals("")) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("ERROR!");
	            alert.setHeaderText("Veuillez saisir le nom du bassin!");
	            alert.setContentText("Cliquer OK pour réessayer");
	            alert.show();
	            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	            Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
	            clearFieldsBassin();
	            stage.getIcons().add(myIcone);
			}
			else {
				if(checkBassinExist()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
		            alert.setTitle("ERROR!");
		            alert.setHeaderText("Bassin existe deja!");
		            alert.setContentText("Cliquer OK pour entrer un autre");
		            alert.show();
		            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		            Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
		            clearFieldsBassin();
		            stage.getIcons().add(myIcone);
				}
				else {
			String query = "INSERT INTO bassin(nomBassin) VALUES ('"+ nomBassin.getText() + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            
            connection.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Le bassin a été ajouté avec succès!");
            alert.setContentText("Cliquer Ok");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
            stage.getIcons().add(myIcone);
            showBassin();
            clearFieldsBassin();
            alert.showAndWait();
				}
		} 
        }catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
	}
	
	public ObservableList<Bassin> getBassinList() {

        ObservableList<Bassin> bassinList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM Bassin";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Bassin bassin;
            while (resultSet.next()) {
                bassin = new Bassin(resultSet.getInt("idBassin"), resultSet.getString("nomBassin"));
                bassinList.add(bassin);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return bassinList;
    }

    public void showBassin() {

        ObservableList<Bassin> list = getBassinList();

        idBassin.setCellValueFactory(new PropertyValueFactory<Bassin, Integer>("idBassin"));
        nomBTable.setCellValueFactory(new PropertyValueFactory<Bassin, String>("nomBassin"));
        bassinTable.setItems(list);
    }
    
    @FXML private void BassinMouseAction(MouseEvent event) throws SQLException {

        if(!bassinTable.getSelectionModel().isEmpty()){
            Bassin bassin = bassinTable.getSelectionModel().getSelectedItem();
            idB.setText("" + bassin.getIdBassin());
            nomBassin.setText("" + bassin.getNomBassin());
            modifierBas.setDisable(false);
            rechercherBas.setDisable(false);
        }
    }
    
    public void clearFieldsBassin() {
    	idB.setText("");
    	nomBassin.clear();
    }
    
    
    public void handleDeleteBassin(ActionEvent event){

    	if(bassinTable.getSelectionModel().isEmpty()){
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("ERROR!");
    		alert.setHeaderText("selectionnez le bassin a supprimer!");
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
    		alert.setContentText("étes vous sur de vouloir supprimer ce bassin!");
    		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    		Image myIcone = new Image("/View/ressources/warning.png");
    		stage.getIcons().add(myIcone);
    		Optional <ButtonType> action = alert.showAndWait();
    		
    		if(action.get() == ButtonType.OK) {
    			
				/*
				 * String query =
				 * "DELETE FROM bassin JOIN station ON bassin.idBassin = station.idBassin" +
				 * " JOIN observateur ON idStation = observateur.idStation" +
				 * " WHERE idBassin = " + idB.getText() + "";
				 */
    			
    			String query = "DELETE FROM bassin WHERE idBassin = " + idB.getText();
        		Statement statement = connection.createStatement();
        		statement.executeUpdate(query);
        		
        		String query1 = "DELETE FROM station WHERE idBassin = " + idB.getText();
        		Statement statement1 = connection.createStatement();
        		statement1.executeUpdate(query1);
        		
        		connection.close();
      
        		Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        		alert1.setTitle("Confirmation!");
        		alert1.setHeaderText("Le bassin a été supprimé avec succès!");
        		alert1.setContentText("Cliquer Ok");
        		Stage stage1 = (Stage) alert1.getDialogPane().getScene().getWindow();
        		Image myIcone1 = new Image("/View/ressources/iconfinder_Info_728979.png");
        		stage1.getIcons().add(myIcone1);
        		showObservateur();
        		showStation();
        		showBassin();
        		clearFieldsBassin();
      			alert1.showAndWait();
        	}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	
    }
    
    public void modifierBassin(ActionEvent e) throws SQLException {
        try {
        	if(bassinTable.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("selectionnez le bassin a modifier!");
                alert.setContentText("Cliquer OK pour reessayer");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
            }else{
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            String query = "UPDATE bassin SET nomBassin = '" + nomBassin.getText() + "' WHERE idBassin = " + idB.getText() + "";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("Le bassin a été modifié avec succès!");
            alert.setContentText("Cliquer Ok");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
            stage.getIcons().add(myIcone);
            showBassin();
            clearFieldsBassin();
            alert.showAndWait();
        } 
        	}catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void handleSearchBassin(ActionEvent event) {

        try {
            int index = -1, totalRows = 0;
            if (nomBassin.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("Veuillez entrer le nom du bassin a rechercher !");
                alert.setContentText("Cliquer OK pour reessayer");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                nomBassin.clear();
            } else {
                String query = "SELECT * FROM bassin";
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    index++;
                    if (resultSet.getString("nomBassin").contains(nomBassin.getText())) {
                        break;
                    }
                }
                Connection connection2 = getConnection();
                Statement statement2 = connection2.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(query);
                while (resultSet2.next()) {
                    totalRows++;
                }
                index++;
                if (index == totalRows) {
                	bassinTable.getSelectionModel().select(index - 1);
                } else if (index < totalRows) {
                	bassinTable.getSelectionModel().select(index - 1);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText("Bassin non touve !");
                    alert.setContentText("Cliquer Ok pour Continuer");
                    alert.show();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
                    stage.getIcons().add(myIcone);
                    nomBassin.clear();
                }
                connection.close();
                connection2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	///////////////STATION/////////////
	
    
    public boolean checkStationExist() {
        boolean check = false;
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            String query = "SELECT * FROM station";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                if(resultSet.getString("nomStation").equals(nomStation.getText()) 
                		&& resultSet.getInt("idBassin") == Integer.parseInt(idB.getText())){
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
	
	public void ajouterStation(ActionEvent e) throws IOException {
        try {
			Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
			
			if(bassinTable.getSelectionModel().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("ERROR!");
	            alert.setHeaderText("Veuillez selectionner le bassin pour lui ajouter une station!");
	            alert.setContentText("Cliquer OK pour réessayer");
	            alert.show();
	            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	            Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
	            clearFieldsStation();
	            stage.getIcons().add(myIcone);
			}
			else if (nomStation.getText().equals("")) {
				Alert alert = new Alert(Alert.AlertType.ERROR);
	            alert.setTitle("ERROR!");
	            alert.setHeaderText("Veuillez le nom de la station!");
	            alert.setContentText("Cliquer OK pour réessayer");
	            alert.show();
	            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
	            Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
	            stage.getIcons().add(myIcone);
			}
			else {
				if(checkStationExist()) {
					Alert alert = new Alert(Alert.AlertType.ERROR);
		            alert.setTitle("ERROR!");
		            alert.setHeaderText("Station existe deja dans ce bassin!");
		            alert.setContentText("Cliquer OK pour entrer un autre");
		            alert.show();
		            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		            Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
		            clearFieldsBassin();
		            clearFieldsStation();
		            stage.getIcons().add(myIcone);
				}
				else {
			String query = "INSERT INTO station(nomStation,idBassin) VALUES ('"+ nomStation.getText() + "','" + idB.getText() + "')";
			
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            
            connection.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("La station a été ajoutée avec succès!");
            alert.setContentText("Cliquer Ok");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
            stage.getIcons().add(myIcone);
            showStation();
            clearFieldsStation();
            clearFieldsBassin();
            alert.showAndWait();
				}
		} 
        }catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}    
	}
	
	public ObservableList<Station> getStationList() {

        ObservableList<Station> stationList = FXCollections.observableArrayList();
        Connection connection = getConnection();
        String query = "SELECT * FROM station";
        Statement statement;
        ResultSet resultSet;

        try {
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            Station station;
            while (resultSet.next()) {
                station = new Station(resultSet.getInt("idStation"), resultSet.getString("nomStation"));
                stationList.add(station);
            }
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stationList;
    }

    public void showStation() {

        ObservableList<Station> list = getStationList();

        idStation.setCellValueFactory(new PropertyValueFactory<Station, Integer>("idStation"));
        nomSTable.setCellValueFactory(new PropertyValueFactory<Station, String>("nomStation"));
        stationTable.setItems(list);
    }
    
    @FXML private void StationMouseAction(MouseEvent event) throws SQLException {

        if(!stationTable.getSelectionModel().isEmpty()){
            Station station = stationTable.getSelectionModel().getSelectedItem();
            idS.setText("" + station.getIdStation());
            nomStation.setText("" + station.getNomStation());
            modifierStat.setDisable(false);
            rechercherStat.setDisable(false);
        }
    }
    
    public void clearFieldsStation() {
    	idS.setText("");
    	nomStation.clear();
    }
    
    
    public void handleDeleteStation(ActionEvent event){

    	if(stationTable.getSelectionModel().isEmpty()){
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("ERROR!");
    		alert.setHeaderText("selectionnez la station a supprimer!");
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
    		alert.setContentText("étes vous sur de vouloir supprimer cette station!");
    		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    		Image myIcone = new Image("/View/ressources/warning.png");
    		stage.getIcons().add(myIcone);
    		Optional <ButtonType> action = alert.showAndWait();
    		
    		if(action.get() == ButtonType.OK) {
    			
    			String query = "DELETE FROM station WHERE idStation = " + idS.getText() + "";
        		Statement statement = connection.createStatement();
        		statement.executeUpdate(query);
        		
        		String query1 = "DELETE FROM observateur WHERE idStation = " + idS.getText() + "";
        		Statement statement1 = connection.createStatement();
        		statement1.executeUpdate(query1);
        		
        		String query2 = "DELETE FROM relativehumidity WHERE idStation = " + idS.getText() + "";
        		Statement statement2 = connection.createStatement();
        		statement2.executeUpdate(query2);
        		
        		connection.close();
      
        		Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        		alert1.setTitle("Confirmation!");
        		alert1.setHeaderText("La station a été supprimée avec succès!");
        		alert1.setContentText("Cliquer Ok");
        		Stage stage1 = (Stage) alert1.getDialogPane().getScene().getWindow();
        		Image myIcone1 = new Image("/View/ressources/iconfinder_Info_728979.png");
        		stage1.getIcons().add(myIcone1);
        		showStation();
        		showObservateur();
        		clearFieldsStation();
      			alert1.showAndWait();
        	}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	
    }
    
    public void modifierStation(ActionEvent e) throws SQLException {
        try {
        	if(stationTable.getSelectionModel().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("selectionnez la station a modifier!");
                alert.setContentText("Cliquer OK pour reessayer");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
            }else{
            Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
            String query = "UPDATE station SET nomStation = '" + nomStation.getText() + "' WHERE idStation = " + idS.getText() + "";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            connection.close();
            
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation!");
            alert.setHeaderText("La station a été modifiée avec succès!");
            alert.setContentText("Cliquer Ok");
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
            stage.getIcons().add(myIcone);
            showStation();
            clearFieldsStation();
            alert.showAndWait();
        } 
        	}catch (Exception exception) {
            exception.printStackTrace();
        }
    }
    
    public void handleSearchStation(ActionEvent event) {

        try {
            int index = -1, totalRows = 0;
            if (nomStation.getText().equals("")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText("Veuillez entrer le nom de la station a rechercher !");
                alert.setContentText("Cliquer OK pour reessayer");
                alert.show();
                Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
                stage.getIcons().add(myIcone);
                nomStation.clear();
            } else {
                String query = "SELECT * FROM station";
                Connection connection = getConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    index++;
                    if (resultSet.getString("nomStation").contains(nomStation.getText())) {
                        break;
                    }
                }
                Connection connection2 = getConnection();
                Statement statement2 = connection2.createStatement();
                ResultSet resultSet2 = statement2.executeQuery(query);
                while (resultSet2.next()) {
                    totalRows++;
                }
                index++;
                if (index == totalRows) {
                    stationTable.getSelectionModel().select(index - 1);
                } else if (index < totalRows) {
                    stationTable.getSelectionModel().select(index - 1);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("ERROR!");
                    alert.setHeaderText("station non touvee !");
                    alert.setContentText("Cliquer Ok pour Continuer");
                    alert.show();
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
                    stage.getIcons().add(myIcone);
                    nomStation.clear();
                }
                connection.close();
                connection2.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

///////////////OBSERVATEUR/////////////
	
    
    public boolean checkObservateurExist() {
    	boolean check = false;
    	try {
    		    Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    			String query = "SELECT * FROM observateur";
    			Statement statement = connection.createStatement();
    			ResultSet resultSet = statement.executeQuery(query);
    			while (resultSet.next()) {
    				if(resultSet.getString("nomObservateur").equals(nomObservateur.getText()) 
    						&& resultSet.getInt("idStation") == Integer.parseInt(idS.getText())){
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

    public void ajouterObservateur(ActionEvent e) throws IOException {
    	try {
    		Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
	
    		if(stationTable.getSelectionModel().isEmpty()) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("ERROR!");
    			alert.setHeaderText("Veuillez selectionner la station pour lui ajouter un observateur!");
    			alert.setContentText("Cliquer OK pour réessayer");
    			alert.show();
    			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    			Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    			stage.getIcons().add(myIcone);
    			clearFieldsObservateur();
    			
    		}
    		else if (nomObservateur.getText().equals("")) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("ERROR!");
    			alert.setHeaderText("Veuillez le nom de l'observateur!");
    			alert.setContentText("Cliquer OK pour réessayer");
    			alert.show();
    			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    			Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    			stage.getIcons().add(myIcone);
    		}
    		else {
    			if(checkObservateurExist()) {
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setTitle("ERROR!");
    				alert.setHeaderText("Observateur existe deja dans cette Station!");
    				alert.setContentText("Cliquer OK pour entrer un autre");
    				alert.show();
    				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    				Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    				clearFieldsObservateur();
    				clearFieldsStation();
    				stage.getIcons().add(myIcone);
    			}
    			else {
    				String query = "INSERT INTO observateur(nomObservateur,idStation) VALUES ('"+ nomObservateur.getText() + "','" + idS.getText() + "')";
	
    				Statement statement = connection.createStatement();
    				statement.executeUpdate(query);
  
    				connection.close();
  
    				Alert alert = new Alert(Alert.AlertType.INFORMATION);
    				alert.setTitle("Confirmation!");
    				alert.setHeaderText("L'observateur a été ajouté avec succès!");
    				alert.setContentText("Cliquer Ok");
    				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    				Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
    				stage.getIcons().add(myIcone);
    				showObservateur();
    				clearFieldsStation();
    				clearFieldsObservateur();
    				alert.showAndWait();
    			}
    		} 
    	}catch (SQLException e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}    
    }

    public ObservableList<Observateur> getObservateurList() {

    	ObservableList<Observateur> observateurList = FXCollections.observableArrayList();
    	Connection connection = getConnection();
    	String query = "SELECT * FROM observateur";
    	Statement statement;
    	ResultSet resultSet;

    	try {
    		statement = connection.createStatement();
    		resultSet = statement.executeQuery(query);
    		Observateur observateur;
    		while (resultSet.next()) {
    			observateur = new Observateur(resultSet.getInt("idObservateur"), resultSet.getString("nomObservateur"));
    			observateurList.add(observateur);
    		}
    		connection.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return observateurList;
    }

    public void showObservateur() {

    	ObservableList<Observateur> list = getObservateurList();

    	idObservateur.setCellValueFactory(new PropertyValueFactory<Observateur, Integer>("idObservateur"));
    	nomOTable.setCellValueFactory(new PropertyValueFactory<Observateur, String>("nomObservateur"));
    	observateurTable.setItems(list);
    }

    @FXML private void ObservateurMouseAction(MouseEvent event) throws SQLException {

    	if(!observateurTable.getSelectionModel().isEmpty()){
    		Observateur observateur = observateurTable.getSelectionModel().getSelectedItem();
    		idO.setText("" + observateur.getIdObservateur());
    		nomObservateur.setText("" + observateur.getNomObservateur());
    		modifierObs.setDisable(false);
    		rechercherObs.setDisable(false);
    	}
    }

    public void clearFieldsObservateur() {
    	idO.setText("");
    	nomObservateur.clear();
    }


    public void handleDeleteObservateur(ActionEvent event){

    	if(observateurTable.getSelectionModel().isEmpty()){
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setTitle("ERROR!");
    		alert.setHeaderText("selectionnez l'observateur a supprimer!");
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
    		alert.setContentText("étes vous sur de vouloir supprimer cet observateur!");
    		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    		Image myIcone = new Image("/View/ressources/warning.png");
    		stage.getIcons().add(myIcone);
    		Optional <ButtonType> action = alert.showAndWait();
    		
    		if(action.get() == ButtonType.OK) {
    			
    			String query = "DELETE FROM observateur WHERE idObservateur = " + idO.getText() + "";
        		Statement statement = connection.createStatement();
        		statement.executeUpdate(query);
        		
        		String query1 = "DELETE FROM relativehumidity WHERE idObservateur = " + idO.getText() + "";
        		Statement statement1 = connection.createStatement();
        		statement1.executeUpdate(query1);
        		
        		connection.close();
      
        		Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
        		alert1.setTitle("Confirmation!");
        		alert1.setHeaderText("L'oservateur a été supprimé avec succès!");
        		alert1.setContentText("Cliquer Ok");
        		Stage stage1 = (Stage) alert1.getDialogPane().getScene().getWindow();
        		Image myIcone1 = new Image("/View/ressources/iconfinder_Info_728979.png");
        		stage1.getIcons().add(myIcone1);
        		showObservateur();
        		clearFieldsObservateur();
      			alert1.showAndWait();
        	}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}	
    }

    public void modifierObservateur(ActionEvent e) throws SQLException {
    	try {
    		if(observateurTable.getSelectionModel().isEmpty()){
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("ERROR!");
    			alert.setHeaderText("selectionnez l'observateur a modifier!");
    			alert.setContentText("Cliquer OK pour reessayer");
    			alert.show();
    			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    			Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    			stage.getIcons().add(myIcone);
    		}else{
    			Connection connection = DriverManager.getConnection("jdbc:sqlite:D:\\stageObservation\\GRH\\src\\DataBase\\sqlite.db");
    			String query = "UPDATE observateur SET nomObservateur = '" + nomObservateur.getText() + "' WHERE idObservateur = " + idO.getText() + "";
    			Statement statement = connection.createStatement();
    			statement.executeUpdate(query);
    			connection.close();
  
    			Alert alert = new Alert(Alert.AlertType.INFORMATION);
    			alert.setTitle("Confirmation!");
    			alert.setHeaderText("L'observateur a été modifié avec succès!");
    			alert.setContentText("Cliquer Ok");
    			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    			Image myIcone = new Image("/View/ressources/iconfinder_Info_728979.png");
    			stage.getIcons().add(myIcone);
    			showObservateur();
    			clearFieldsObservateur();
    			alert.showAndWait();
    		} 
    	}catch (Exception exception) {
    		exception.printStackTrace();
    	}
    }

    public void handleSearchObservateur(ActionEvent event) {

    	try {
    		int index = -1, totalRows = 0;
    		if (nomObservateur.getText().equals("")) {
    			Alert alert = new Alert(Alert.AlertType.ERROR);
    			alert.setTitle("ERROR!");
    			alert.setHeaderText("Veuillez entrer le nom de l'observateur a rechercher !");
    			alert.setContentText("Cliquer OK pour reessayer");
    			alert.show();
    			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    			Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    			stage.getIcons().add(myIcone);
    			nomObservateur.clear();
    		} else {
    			String query = "SELECT * FROM observateur";
    			Connection connection = getConnection();
    			Statement statement = connection.createStatement();
    			ResultSet resultSet = statement.executeQuery(query);
    			while (resultSet.next()) {
    				index++;
    				if (resultSet.getString("nomObservateur").contains(nomObservateur.getText())) {
    					break;
    				}
    			}
    			Connection connection2 = getConnection();
    			Statement statement2 = connection2.createStatement();
    			ResultSet resultSet2 = statement2.executeQuery(query);
    			while (resultSet2.next()) {
    				totalRows++;
    			}
    			index++;
    			if (index == totalRows) {
    				observateurTable.getSelectionModel().select(index - 1);
    			} else if (index < totalRows) {
    				observateurTable.getSelectionModel().select(index - 1);
    			} else {
    				Alert alert = new Alert(Alert.AlertType.ERROR);
    				alert.setTitle("ERROR!");
    				alert.setHeaderText("observateur non touvee !");
    				alert.setContentText("Cliquer Ok pour Continuer");
    				alert.show();
    				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
    				Image myIcone = new Image("/View/ressources/iconfinder_sign-error_299045.png");
    				stage.getIcons().add(myIcone);
    				nomObservateur.clear();
    			}
    			connection.close();
    			connection2.close();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		showBassin();
		showStation();
		showObservateur();
	}
	
	
}
