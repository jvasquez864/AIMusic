import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import Constants.Chords;
import Constants.NoteDurations;
import org.jfugue.pattern.Pattern;

public class MuLambdaEvolution {
    private PriorityQueue<PatternAndRating> evolvedMuLambdaPatterns;
    //private <PatternAndRating> muBestPatterns;
    //private List<PatternAndRating> lambdaWorstPatterns;

    private int mu;
    private int lambda;
    private int patternLengths = 20;

    //Make sure that mu+lambda is the same as the amount of patterns from the file being read, otherwise there could
    //be  length issues. I.E trying to choose the mu best of a file, when mu is 10 but the file size is only 5.
    public MuLambdaEvolution(PriorityQueue<PatternAndRating> evolvedMuLambdaPatterns, int mu, int lambda) {
        this.mu = mu;
        this.lambda = lambda;

        if (evolvedMuLambdaPatterns != null && evolvedMuLambdaPatterns.size() > 0) {
            this.evolvedMuLambdaPatterns = new PriorityQueue<PatternAndRating>(evolvedMuLambdaPatterns);
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

    private PriorityQueue<PatternAndRating> initializeMuLambdaRandomPatterns() {
        PriorityQueue<PatternAndRating> muLambdaPatterns = new PriorityQueue<PatternAndRating>(mu + lambda);
        while (muLambdaPatterns.size() < mu + lambda) {
            muLambdaPatterns.add(createRandomPatternAndRating());
        }
        return muLambdaPatterns;
    }

    //Updates the evolvedMuLambdaPatterns
    private void evolvePatterns(){
        PriorityQueue<PatternAndRating> patternsToEvolve = new PriorityQueue<PatternAndRating>(evolvedMuLambdaPatterns);
        List<PatternAndRating> lambdaWorstPatterns = new ArrayList<PatternAndRating>(lambda);
        List<PatternAndRating> muBestPatterns = new ArrayList<PatternAndRating>(mu);

        for(int i = 0; i < lambda; ++i){
            //Removes the worst lambda elements from patterns to evolve and puts them into lambdaWorstPatterns
            lambdaWorstPatterns.add(patternsToEvolve.poll());
        }
        for(int i = 0; i < mu; ++i){
            //Removes the best mu elements from patterns to evolve and puts them into lambdaWorstPatterns
            muBestPatterns.add(patternsToEvolve.poll());
        }

        evolvedMuLambdaPatterns = mutateLambdaWorst(muBestPatterns,lambdaWorstPatterns);
    }

    //Returns a new priority queue consisting of mutated copies of mu best as the new lambda worst
    private PriorityQueue<PatternAndRating> mutateLambdaWorst(List<PatternAndRating> muBestPatterns,
                                                              List<PatternAndRating> lambdaWorstPatterns) {
        //Update lambda worst to mutated copies of muBest
            for(int i = 0; i < lambdaWorstPatterns.size(); ++i){
                int muIndex = i % mu;
                PatternAndRating patternToChange = new PatternAndRating( muBestPatterns.get(muIndex) );
                patternToChange.pattern = new Pattern(mutatePattern(patternToChange));
                lambdaWorstPatterns.set(i, patternToChange);
            }

            //Populate new priority queue with updated muBest/lambdaWorst patterns
            PriorityQueue<PatternAndRating> mutatedPatterns = new PriorityQueue<>(mu + lambda);
            for(int i = 0; i < lambdaWorstPatterns.size(); ++i){
                mutatedPatterns.add(lambdaWorstPatterns.get(i));
            }
            for(int i = 0; i < muBestPatterns.size(); ++i){
                mutatedPatterns.add(lambdaWorstPatterns.get(i));
            }
            return mutatedPatterns;
    }

    private String mutatePattern(PatternAndRating pattern){
        String thePattern = pattern.pattern.toString();
        //Split by space to get each individual note
        String[] thePatternAsArray = thePattern.split(" ");

        Random rnd = new Random();

        //Mutate 1/4 randomly selected notes
        for(int amountOfNotes = 0; amountOfNotes < thePatternAsArray.length / 4; ++amountOfNotes){
            thePatternAsArray[rnd.nextInt(thePatternAsArray.length)] = createRandomPattern();
        }

        return thePatternAsArray.toString();
    }

    //Creates a random pattern of length 'patternLengths'.  Pattern rating is initialized to -1
    private PatternAndRating createRandomPatternAndRating() {
        String thePattern = "";

        for (int currentPatternLength = 0; currentPatternLength < patternLengths; ++currentPatternLength) {
            thePattern += createRandomPattern() + " ";
        }

        return new PatternAndRating(thePattern,-1);
    }

    //Creates a random single pattern, I.E "Cq"
    private String createRandomPattern(){
        String noteChordOrRest = getRandomTune();
        String noteDuration = getRandomNoteDuration();
        return (noteChordOrRest + noteDuration);
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
