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
				
				listyyy = new ArrayList<>(initializeRandomPatterns(5, 10));
				
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

	public static PriorityQueue<PatternAndRating> initializeRandomPatterns(int numOfPatterns, int patternLength) {
		PriorityQueue<PatternAndRating> patterns = new PriorityQueue<PatternAndRating>(numOfPatterns);
		while (patterns.size() < numOfPatterns) {
			patterns.add(createRandomPatternAndRating(patternLength));
		}
		return patterns;
	}

	//Creates a random pattern of length 'patternLength'.  Pattern rating is initialized to -1
	public static PatternAndRating createRandomPatternAndRating(int patternLength) {
		String thePattern = "";

		for (int currentPatternLength = 0; currentPatternLength < patternLength; ++currentPatternLength) {
			thePattern += createRandomPattern() + " ";
		}
		Pattern randomPattern = new Pattern(thePattern);
		setPatternRandomInstrument(randomPattern);

		return new PatternAndRating(thePattern, -1);
	}

	private static void setPatternRandomInstrument(Pattern pattern){
		Random rnd = new Random();
		//Give voice 0 a random instrument that's not a percussion instrument
		pattern.setVoice(0).setInstrument(rnd.nextInt(112));
	}

	//Takes in two patterns, and produces a new string that can be used to make a new pattern.  The new pattern
	//is a crossover of both parents.  Both patterns must be of the same length
	public static String crossoverMutation(PatternAndRating parent1, PatternAndRating parent2){
		Random rnd = new Random();

		//Get each parents genes as a string[], each index being a single note/chord
		String[] parent1PatternNotes = parent1.pattern.toString().split(" ");
		String[] parent2PatternNotes = parent2.pattern.toString().split(" ");
		int indexToCrossoverAt = rnd.nextInt( parent1PatternNotes.length );

		//Make the child's genes a combination of the parents genes
		//Arrays.copyOfRange bounds are [x,y) (x is inclusive, y is exclusive)
		String[] inheritedParent1Genes=  Arrays.copyOfRange(parent1PatternNotes,0,indexToCrossoverAt);
		String[] inheritedParent2Genes = Arrays.copyOfRange(parent2PatternNotes,indexToCrossoverAt,parent1PatternNotes.length);
		ArrayList<String> childGenes = new ArrayList<String>();
		childGenes.addAll(Arrays.asList(inheritedParent1Genes));
		childGenes.addAll(Arrays.asList(inheritedParent2Genes));

		String crossOverString = "";
		for(int i = 0; i < childGenes.size(); ++i){
			crossOverString += childGenes.get(i) + " ";
		}
		return crossOverString;
	}
	//mutates the 1/4 randomly selected notes in the pattern
	public static String mutatePattern(PatternAndRating pattern) {
		String thePattern = pattern.pattern.toString();
		//Split by space to get each individual note
		String[] thePatternAsArray = thePattern.split(" ");

		Random rnd = new Random();

		//Mutate 1/4 randomly selected notes
		for (int amountOfNotes = 0; amountOfNotes < thePatternAsArray.length / 4; ++amountOfNotes) {
			thePatternAsArray[rnd.nextInt(thePatternAsArray.length)] = MainController.createRandomPattern();
		}

		String mutatedPattern = "";
		for(int i = 0; i < thePatternAsArray.length; ++i){
			mutatedPattern += thePatternAsArray[i] + " ";
		}
		return mutatedPattern;
	}

	//Creates a random single pattern, I.E "Cq"
	public static String createRandomPattern() {
		String noteChordOrRest = getRandomTune();
		String noteDuration = getRandomNoteDuration();
		return (noteChordOrRest + noteDuration);
	}

	//65% chance to be a note, 25% to be a chord, 10% to be a rest
	private static String getRandomTune() {
		Random rnd = new Random();

		//random number between 0-99;
		int noteVal = rnd.nextInt(100);
		String patternString = "";

		//pattern will be a note
		if (noteVal < 65) {
			patternString = getRandomNote();
		}
		//pattern will be chord
		else if (noteVal > 65 && noteVal < 90) {
			patternString = getRandomChord();
		}
		//pattern is a rest
		else {
			patternString = "R";
		}
		return patternString;
	}

	//Return a random note
	private static String getRandomNote() {
		Random rnd = new Random();

		//Notes in jFugue can be represented as a random number between 0-127
		return Integer.toString(rnd.nextInt(128));

	}

	//Return a random chord
	private static String getRandomChord() {
		Random rnd = new Random();
		return Chords.Chords[rnd.nextInt(Chords.Chords.length)];
	}

	//Return a random note duration
	private static String getRandomNoteDuration() {
		Random rnd = new Random();
		return NoteDurations.durations[rnd.nextInt(NoteDurations.durations.length)];
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

