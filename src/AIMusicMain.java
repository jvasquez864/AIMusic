import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



public class AIMusicMain extends Application{
    public static void main(String[] args){
        launch(args);

    }
    public void start(Stage primaryStage) throws IOException{
        //create new Player

        Parent root;
        root = FXMLLoader.load(getClass().getResource("mainFX.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Test AIMUSIC");
        primaryStage.show();
        
        
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(AIMusicMain.class.getResource("mainFX.fxml"));
        loader.load();
        MainController controller = loader.getController();
        controller.setMain(this);
        
      
        
    }

    

}
