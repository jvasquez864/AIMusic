import org.jfugue.pattern.Pattern;

//Helper class to pair patterns with our respective ratings for them
public class PatternAndRating {
    private Pattern pattern;
    public int rating;

    public PatternAndRating(String pattern, int rating){
        this.pattern = new Pattern(pattern);
        this.rating = rating;
    }

    public Pattern getPattern(){
        return pattern;
    }
}
