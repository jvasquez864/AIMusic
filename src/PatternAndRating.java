import org.jfugue.pattern.Pattern;

//Helper class to pair patterns with our respective ratings for them
public class PatternAndRating implements Comparable<PatternAndRating>{
    public Pattern pattern;
    public int rating;

    public PatternAndRating(String pattern, int rating){
        this.pattern = new Pattern(pattern);
        this.rating = rating;
    }

    //copy constructor
    public PatternAndRating(PatternAndRating copy){
        this.pattern = new Pattern(copy.pattern);
        this.rating = copy.rating;
    }

    public int compareTo(PatternAndRating comparisonPattern){
        //ascending order
        return this.rating - comparisonPattern.rating;
    }
}
