import java.util.*;

import Constants.Chords;
import Constants.NoteDurations;
import org.jfugue.pattern.Pattern;

public class MuLambdaEvolution {

    private int mu;
    private int lambda;


    public MuLambdaEvolution(int mu, int lambda) {
        this.mu = mu;
        this.lambda = lambda;
    }


    //Updates the evolvedMuLambdaPatterns
    public PriorityQueue<PatternAndRating> evolvePatterns(PriorityQueue<PatternAndRating> patternsToEvolve) {
        //PriorityQueue<PatternAndRating> patternsToEvolve = new PriorityQueue<PatternAndRating>(evolvedMuLambdaPatterns);
        List<PatternAndRating> lambdaWorstPatterns = new ArrayList<PatternAndRating>(lambda);
        List<PatternAndRating> muBestPatterns = new ArrayList<PatternAndRating>(mu);

        for (int i = 0; i < lambda; ++i) {
            //Removes the worst lambda elements from patterns to evolve and puts them into lambdaWorstPatterns
            lambdaWorstPatterns.add(patternsToEvolve.poll());
        }
        for (int i = 0; i < mu; ++i) {
            //Removes the best mu elements from patterns to evolve and puts them into lambdaWorstPatterns
            muBestPatterns.add(patternsToEvolve.poll());
        }

        return mutateLambdaWorst(muBestPatterns, lambdaWorstPatterns);
    }

    //Returns a new priority queue consisting of mutated copies of mu best as the new lambda worst
    private PriorityQueue<PatternAndRating> mutateLambdaWorst(List<PatternAndRating> muBestPatterns,
                                                              List<PatternAndRating> lambdaWorstPatterns) {
        //Update lambda worst to mutated copies of muBest
        for (int i = 0; i < lambdaWorstPatterns.size(); ++i) {
            int muIndex = i % mu;
            PatternAndRating patternToChange = new PatternAndRating(muBestPatterns.get(muIndex));
            patternToChange.pattern = new Pattern(mutatePattern(patternToChange));
            lambdaWorstPatterns.set(i, patternToChange);
        }

        //Populate new priority queue with updated muBest/lambdaWorst patterns
        PriorityQueue<PatternAndRating> mutatedPatterns = new PriorityQueue<>(mu + lambda);
        for (int i = 0; i < lambdaWorstPatterns.size(); ++i) {
            mutatedPatterns.add(lambdaWorstPatterns.get(i));
        }
        for (int i = 0; i < muBestPatterns.size(); ++i) {
            mutatedPatterns.add(lambdaWorstPatterns.get(i));
        }
        return mutatedPatterns;
    }

    private String mutatePattern(PatternAndRating pattern) {
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
}
