import Constants.NoteDurations;
import org.jfugue.pattern.Pattern;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class NoteUtils {

    public static PriorityQueue<PatternAndRating> initializeRandomPatterns(int numOfPatterns, int patternLength) {
        PriorityQueue<PatternAndRating> patterns = new PriorityQueue<PatternAndRating>(numOfPatterns);
        while (patterns.size() < numOfPatterns) {
            patterns.add(createRandomPatternAndRating(patternLength));
        }
        return patterns;
    }


    //Takes in two patterns, and produces a new string that can be used to make a new pattern.  The new pattern
    //is a crossover of both parents.  Both patterns must be of the same length
    public static String crossoverMutationEntirePattern(PatternAndRating parent1, PatternAndRating parent2) {
        String melodyCrossover = crossoverMutation(parent1.pattern, parent2.pattern);
        String percussionCrossover = crossoverMutation(parent1.percussionPattern, parent2.percussionPattern);
        return melodyCrossover + percussionCrossover;
    }

    private static String crossoverMutation(Pattern pattern1, Pattern pattern2) {
        Random rnd = new Random();

        //Get each parents genes as a string[], each index being a single note/chord
        String[] parent1PatternNotes = pattern1.toString().split(" ");
        String[] parent2PatternNotes = pattern2.toString().split(" ");
        int indexToCrossoverAt = rnd.nextInt(parent1PatternNotes.length);

        //Make the child's genes a combination of the parents genes
        //Arrays.copyOfRange bounds are [x,y) (x is inclusive, y is exclusive)
        String[] inheritedParent1Genes = Arrays.copyOfRange(parent1PatternNotes, 0, indexToCrossoverAt);
        String[] inheritedParent2Genes = Arrays.copyOfRange(parent2PatternNotes, indexToCrossoverAt, parent1PatternNotes.length);
        ArrayList<String> childGenes = new ArrayList<String>();
        childGenes.addAll(Arrays.asList(inheritedParent1Genes));
        childGenes.addAll(Arrays.asList(inheritedParent2Genes));

        String crossOverString = "";
        for (int i = 0; i < childGenes.size(); ++i) {
            crossOverString += childGenes.get(i) + " ";
        }
        return crossOverString;
    }

    //mutates the 1/4 randomly selected notes in the pattern
    public static String mutateEntirePattern(PatternAndRating pattern) {
        String melody = mutatePattern(pattern.pattern);
        String percussion = mutatePattern(pattern.percussionPattern);
        return melody + percussion;
    }

    private static String mutatePattern(Pattern melodyPattern) {
        String thePattern = melodyPattern.toString();
        //Split by space to get each individual note
        String[] thePatternAsArray = thePattern.split(" ");

        Random rnd = new Random();

        boolean shouldChangeInstrument = false;
        //Mutate 1/4 randomly selected notes
        // 1 of the indices in the pattern correspond to a voice number  (index 0)
        //so don't take this into account when mutating a quarter of the notes
        for (int amountOfNotes = 0; amountOfNotes < (thePatternAsArray.length - 1) / 4; ++amountOfNotes) {
            //index should be a random number from [1,lengthOfPattern) to avoid mutating the voice
            int indexToMutate = rnd.nextInt(thePatternAsArray.length - 1) + 1;
            if (indexToMutate == 1) {
                shouldChangeInstrument = true;
            } else {
                thePatternAsArray[indexToMutate] = createRandomPattern();
            }
        }
        
        //Possible to end up with two percussion, or two voices
        if (shouldChangeInstrument) {
            Pattern randomizedPattern = new Pattern();
            randomizedPattern.setVoice(0).setInstrument(rnd.nextInt(128));
            String randomInstrument = randomizedPattern.toString().split(" ")[1];
            thePatternAsArray[1] = randomInstrument;
        }

        String mutatedPattern = "";
        for (int i = 0; i < thePatternAsArray.length; ++i) {
            mutatedPattern += thePatternAsArray[i] + " ";
        }

        return mutatedPattern;
    }

    //Creates a random pattern of length 'patternLength'.  Pattern rating is initialized to -1
    private static PatternAndRating createRandomPatternAndRating(int patternLength) {
        String thePatternMelody = "";
        String thePatternPercussion = "";
        for (int currentPatternLength = 0; currentPatternLength < patternLength; ++currentPatternLength) {
            thePatternMelody += createRandomPattern() + " ";
            thePatternPercussion += createRandomPattern() + " ";
        }
        Pattern randomPatternMelody = new Pattern(thePatternMelody);
        Pattern randomPatternPercussion = new Pattern(thePatternPercussion);


        setPatternRandomInstrument(randomPatternMelody);
        setPatternRandomPercussionInstrument(randomPatternPercussion);
        Pattern randomPattern = new Pattern();

        randomPattern.add(randomPatternMelody);
        randomPattern.add(randomPatternPercussion);

        return new PatternAndRating(randomPattern, -1);
    }

    private static void setPatternRandomInstrument(Pattern pattern) {
        Random rnd = new Random();
        //Give voice 0 a random instrument that's not a percussion instrument
        pattern.setVoice(0).setInstrument(rnd.nextInt(112));
    }

    private static void setPatternRandomPercussionInstrument(Pattern pattern) {
        Random rnd = new Random();
        //Get random number from [112-128) (these are the percussion instruments)
        int randomInstrument = rnd.nextInt(16) + 112;
        //Give voice 1 a random instrument that's a percussion instrument
        pattern.setVoice(1).setInstrument(randomInstrument);
    }


    //Creates a random single pattern, I.E "Cq"
    private static String createRandomPattern() {
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
        String[] possibleNoteBases = {"A", "B", "C", "D", "E", "F", "G"};
        String baseOfChord = possibleNoteBases[rnd.nextInt(possibleNoteBases.length)];
        String[] chords = org.jfugue.theory.Chord.getChordNames();
        return baseOfChord + chords[rnd.nextInt(chords.length)];
    }

    //Return a random note duration
    private static String getRandomNoteDuration() {
        Random rnd = new Random();
        return NoteDurations.durations[rnd.nextInt(NoteDurations.durations.length)];
    }

}
