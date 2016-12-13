import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import Constants.Chords;
import Constants.NoteDurations;
import com.sun.deploy.util.ArrayUtil;
import org.jfugue.pattern.Pattern;

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


	public MainController() {
	}

	@FXML
	private void initialize() {


		ObservableList<Pattern> listy = FXCollections.observableArrayList();

		listy.addListener(new ListChangeListener<Pattern>() {
			@Override
			public void onChanged(@SuppressWarnings("rawtypes") ListChangeListener.Change change) {

			}
		});

		table.setItems(listy);


		rate1.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		rate2.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));
		rate3.setItems(FXCollections.observableArrayList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

		rate1.setValue(0);
		rate2.setValue(0);
		rate3.setValue(0);


		FileChooser fileChooser = new FileChooser();
		Stage stage = new Stage();

		load.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent e) {

				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					listy.addAll(loadEvolvedMusic(file));
				} else System.out.println("NOp");


			}
		});


	}

	//Returns an empty list if there was an error reading the file
	public static List<Pattern> loadEvolvedMusic(File file) {
		List<Pattern> loadedMusic = new ArrayList<Pattern>();

		System.out.println(file.getName());

		try (BufferedReader fileReader = new BufferedReader(new FileReader(file))) {

			String musicPiece = null;

			while ((musicPiece = fileReader.readLine()) != null) {
				loadedMusic.add(new Pattern(musicPiece));
			}

			return loadedMusic;
		} catch (IOException e) {
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

		return new PatternAndRating(thePattern, -1);
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
		int x = childGenes.size();
		return childGenes.toString();
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

		return thePatternAsArray.toString();
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


}

