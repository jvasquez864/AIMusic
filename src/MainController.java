import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MainController {

	private AIMusicMain aiMain;
	
	@FXML
	private Button newB;
	
	@FXML
	private Button load;
	
	@FXML
	private Button save;
	
	@FXML
	private Button submit;
	
	@FXML
	private Button crossOVer;
	
	@FXML
	private Button muLambda;
	
	@FXML
	private Button play;
	
	@FXML
	private ChoiceBox<Integer> rate1;
	
	@FXML
	private ChoiceBox<Integer> rate2;
	
	@FXML
	private ChoiceBox<Integer> rate3;
	
	@FXML
	private ListView<Pattern> table;
	
	public List<PatternAndRating> listyyy = new ArrayList<>();
	
	
	public MainController(){}
	
	@FXML
	private void initialize(){
		
	
		 ObservableList<Pattern> listy = FXCollections.observableArrayList();
		 
		
		 
		 listy.addListener(new ListChangeListener<Pattern>() {
	            @Override
	            public void onChanged(@SuppressWarnings("rawtypes") ListChangeListener.Change change) {
	               
	            }
	        });
		 
		 table.setItems(listy);
		 
	        
		
		rate1.setItems(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10));
		rate2.setItems(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10));
		rate3.setItems(FXCollections.observableArrayList(0,1,2,3,4,5,6,7,8,9,10));
		
		rate1.setValue(0);
		rate2.setValue(0);
		rate3.setValue(0);
		
		
		  FileChooser fileChooser = new FileChooser();
		  Stage stage = new Stage();
		  
	        load.setOnAction(new EventHandler<ActionEvent>(){
				@Override
				public void handle(final ActionEvent e){
					
	                File file = fileChooser.showOpenDialog(stage);
	                if (file != null) {
	                	List<PatternAndRating> x = loadEvolvedMusic(file);
	                	listyyy = x;
	                	for(int i = 0; i < listyyy.size();i++){
	                   listy.add(listyyy.get(i).pattern);
	                	}
	                }
	                
	                else System.out.println("NOp");
					
					
				}
			});
		
		submit.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(final ActionEvent e){
				
				int rating = rate1.getValue() + rate2.getValue() + rate3.getValue();
				int index = table.getSelectionModel().getSelectedIndex();
				listyyy.get(index).rating = rating;
				
				
			}
		});
		
		play.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(final ActionEvent e){
				
				int index = table.getSelectionModel().getSelectedIndex();
				Pattern playing = listyyy.get(index).pattern;
				Player musicPlayer = new Player();
				musicPlayer.play(playing);
				
				
			}
		});
		
		
	}
	
	 //Returns an empty list if there was an error reading the file
    public static List<PatternAndRating> loadEvolvedMusic(File file){
        List<PatternAndRating> loadedMusic = new ArrayList<>();
        
        System.out.println(file.getName());

        try(BufferedReader fileReader = new BufferedReader( new FileReader(file))){

            String musicPiece = null;

            while( (musicPiece = fileReader.readLine()) != null){
            	String [] line = musicPiece.split(",");
            	if(line.length > 1){
                loadedMusic.add(new PatternAndRating(line[0],Integer.parseInt(line[1])));
            	}
            	else if(line.length > 0){
            	loadedMusic.add(new PatternAndRating(line[0],-1));
            	}
            }

            return loadedMusic;
        }
        catch (IOException e){
            //If error reading file...
        }
        return loadedMusic;
    }

	
	public void setMain(AIMusicMain aiMain) {
        this.aiMain = aiMain;

        // Add observable list data to the table
        
    }

	public Button getLoad() {
		return load;
	}

	public void setLoad(Button load) {
		this.load = load;
	}
}
