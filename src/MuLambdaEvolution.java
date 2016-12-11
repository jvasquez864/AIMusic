import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Constants.Chords;
import Constants.NoteDurations;
import org.jfugue.pattern.Pattern;
import org.jfugue.theory.Note;

/**
 * Created by John on 12/11/2016.
 */
public class MuLambdaEvolution {
    List<Pattern> evolvedMuLambdaPatterns;
    int mu;
    int lambda;

    //Make sure that mu+lambda is the same as the amount of patterns from the file being read, otherwise there could
    //be  length issues. I.E trying to choose the mu best of a file, when mu is 10 but the file size is only 5.
    public MuLambdaEvolution(List<Pattern> evolvedMuLambdaPatterns, int mu, int lambda) {
        if (evolvedMuLambdaPatterns.size() > 0) {
            this.evolvedMuLambdaPatterns = new ArrayList<Pattern>(evolvedMuLambdaPatterns);
        } else {
            this.evolvedMuLambdaPatterns = new ArrayList<Pattern>(mu + lambda);
        }
        this.mu = mu;
        this.lambda = lambda;
    }

    //Use this constructor when you're not loading in a list of patterns
    public MuLambdaEvolution(int mu, int lambda){
        this.evolvedMuLambdaPatterns = new ArrayList<Pattern>(mu + lambda);
        this.mu = mu;
        this.lambda = lambda;
    }

    private Pattern createRandomPattern() {
        String noteChordOrRest = getRandomTune();
        String noteDuration = getRandomNoteDuration();
        String thePattern = noteChordOrRest + noteDuration;


        return new Pattern(thePattern);
    }

    //65% chance to be a note, 25% to be a chord, 10% to be a rest
    private String getRandomTune() {
        Random rnd = new Random();

        //random number between 0-99;
        int noteVal = rnd.nextInt(99);
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
    private String getRandomNote() {
        Random rnd = new Random();

        //Notes in jFugue can be represented as a random number between 0-127
        return Integer.toString(rnd.nextInt(127));

    }

    //Return a random chord
    private String getRandomChord() {
        Random rnd = new Random();
        return Chords.Chords[rnd.nextInt(Chords.Chords.length)];
    }

    //Return a random note duration
    private String getRandomNoteDuration() {
        Random rnd = new Random();
        return NoteDurations.durations[rnd.nextInt(NoteDurations.durations.length)];
    }
}
