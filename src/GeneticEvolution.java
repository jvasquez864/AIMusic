import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeneticEvolution {

    public List<PatternAndRating> evolvePatterns(List<PatternAndRating> patternsToEvolve) {
        List<PatternAndRating> patterns = new ArrayList<PatternAndRating>(patternsToEvolve);

        Random rnd = new Random();
        for (int numOfEvolutions = 0; numOfEvolutions < patternsToEvolve.size(); ++numOfEvolutions) {

            int parent1Index = rnd.nextInt(patterns.size());
            int parent2Index = rnd.nextInt(patterns.size());
            String childPattern = MainController.crossoverMutation(patterns.get(parent1Index), patterns.get(parent2Index));

            //Get random int from 0-99. If it's less than 5, mutate the child's genes (5% chance of mutation)
            childPattern = rnd.nextInt(100) < 5 ?
                    MainController.mutatePattern( new PatternAndRating(childPattern, -1) ) :
                    childPattern;
            patterns.add(new PatternAndRating(childPattern, -1));
        }
        return patterns;
    }
}

