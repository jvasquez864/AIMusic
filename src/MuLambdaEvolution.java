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
    private List<PatternAndRating> evolvedMuLambdaPatterns;
    private int mu;
    private int lambda;
    private int patternLengths = 20;

    //Make sure that mu+lambda is the same as the amount of patterns from the file being read, otherwise there could
    //be  length issues. I.E trying to choose the mu best of a file, when mu is 10 but the file size is only 5.
    public MuLambdaEvolution(List<PatternAndRating> evolvedMuLambdaPatterns, int mu, int lambda) {
        this.mu = mu;
        this.lambda = lambda;

        if (evolvedMuLambdaPatterns != null && evolvedMuLambdaPatterns.size() > 0) {
            this.evolvedMuLambdaPatterns = new ArrayList<PatternAndRating>(evolvedMuLambdaPatterns);
        } else {
            this.evolvedMuLambdaPatterns = initializeMuLambdaRandomPatterns();
        }
    }

    //Use this constructor when you're not loading in a list of patterns
    public MuLambdaEvolution(int mu, int lambda) {
        this.mu = mu;
        this.lambda = lambda;
        this.evolvedMuLambdaPatterns = initializeMuLambdaRandomPatterns();
    }

    private List<PatternAndRating> initializeMuLambdaRandomPatterns() {
        List<PatternAndRating> muLambdaPatterns = new ArrayList<PatternAndRating>(mu + lambda);
        while (muLambdaPatterns.size() < mu + lambda) {
            muLambdaPatterns.add(createRandomPatternAndRating());
        }
        return muLambdaPatterns;
    }

    //Creates a random pattern of length 'patternLengths'.  Pattern rating is initialized to -1
    private PatternAndRating createRandomPatternAndRating() {
        String noteChordOrRest;
        String noteDuration;
        String thePattern = "";
        for (int currentPatternLength = 0; currentPatternLength < patternLengths; ++currentPatternLength) {
            noteChordOrRest = getRandomTune();
            noteDuration = getRandomNoteDuration();
            thePattern += noteChordOrRest + noteDuration + " ";
        }

        return new PatternAndRating(thePattern,-1);
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
