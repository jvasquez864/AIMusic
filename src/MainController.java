import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import Constants.Chords;
import Constants.NoteDurations;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
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
	private RadioButton muLambda;
	
	@FXML
	private RadioButton genetic;

	@FXML
	private Button evolve;

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
	                	
	                	if(!listy.isEmpty())listy.clear();
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
		
		
		final ToggleGroup group = new ToggleGroup();
		muLambda.setToggleGroup(group);
		muLambda.setSelected(true);
		
		genetic.setToggleGroup(group);
		
		muLambda.setUserData("muLambda");
		genetic.setUserData("genetic");
		
		newB.setOnAction(new EventHandler<ActionEvent>(){
			@SuppressWarnings("unchecked")
			@Override
			public void handle(final ActionEvent e){
				
				listyyy = new ArrayList<>(NoteUtils.initializeRandomPatterns(5, 10));
				
				if(!listy.isEmpty())listy.clear();
				for(int i = 0; i < listyyy.size();i++){
	                   listy.add(listyyy.get(i).pattern);
	                	}
			}
		});
		
		
		save.setOnAction(new EventHandler<ActionEvent>(){
			
			@Override
			public void handle(final ActionEvent e){
				
				saveEvolvedMusic(listyyy);
			}
		});
		
		evolve.setOnAction(new EventHandler<ActionEvent>(){
			
			@SuppressWarnings("unchecked")
			@Override
			public void handle(final ActionEvent e){
				if(group.getSelectedToggle().getUserData().toString() == "muLambda"){
					int musize = listyyy.size() / 3;
					int lsize = listyyy.size() - musize;
					MuLambdaEvolution mul = new MuLambdaEvolution(musize,lsize);
					PriorityQueue<PatternAndRating> qu = new PriorityQueue<>(listyyy);
					listyyy = new ArrayList<>(mul.evolvePatterns(qu));
					if(!listy.isEmpty())listy.clear();
					for(int i = 0; i < listyyy.size();i++){
		                   listy.add(listyyy.get(i).pattern);
		                	}
				}
				else if(group.getSelectedToggle().getUserData().toString() == "genetic"){
					GeneticEvolution ge = new GeneticEvolution();
					listyyy = ge.evolvePatterns(listyyy);
					if(!listy.isEmpty())listy.clear();
					for(int i = 0; i < listyyy.size();i++){
		                   listy.add(listyyy.get(i).pattern);
		                	}
				}
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



	//Saves the list of music as a .txt file
    public static void saveEvolvedMusic(List<PatternAndRating> musicList)
    {
        try {
            //NOTE: Overwrites the previously saved evolvedMusicPieces
            PrintWriter file = new PrintWriter("evolvedMusicPieces.txt", "UTF-8");

            //Add each piece of music as a new line
            for(PatternAndRating musicPiece : musicList){
                file.println(musicPiece.pattern.toString() + "," + musicPiece.rating);
               
            }
            file.close();
        }
        catch(IOException e)
        {
            //Show error message that save was not successful
        }
    }

}

