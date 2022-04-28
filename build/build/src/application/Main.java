	package application;
	
	import javafx.application.Application;
	import javafx.fxml.FXMLLoader;
	import javafx.stage.Stage;
	import javafx.scene.Parent;
	import javafx.scene.Scene;
	import javafx.scene.image.Image;
	
	
	public class Main extends Application {
		
		@Override
		public void start(Stage primaryStage) {
			try {
				
				Parent root =FXMLLoader.load(getClass().getResource("/View/welcome.fxml"));
				primaryStage.setTitle("Agence du Bassin Hydraulique du Guir-Ziz-Rhéris");
				Image myIcon = new Image("/View/ressources/logoApp.png");
		        primaryStage.getIcons().add(myIcon);
		        primaryStage.setScene(new Scene(root));
		        primaryStage.show();
				
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
	
		public static void main(String[] args) {
			launch(args);
			
		}
	
	}