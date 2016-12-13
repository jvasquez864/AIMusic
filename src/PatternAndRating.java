import org.jfugue.pattern.Pattern;

//Helper class to pair patterns with our respective ratings for them
public class PatternAndRating implements Comparable<PatternAndRating>{
    public Pattern pattern;
    public Pattern percussionPattern;
    public int rating;

    public PatternAndRating(String pattern, int rating){
        initializePatterns(pattern);

        this.rating = rating;
    }

    public PatternAndRating(Pattern pattern, int rating){
        initializePatterns(pattern);
        this.rating = rating;
    }

    //copy constructor
    public PatternAndRating(PatternAndRating copy){
        this.pattern = copy.pattern;
        this.percussionPattern = copy.percussionPattern;
        this.rating = copy.rating;
    }

    public Pattern getEntirePattern(){
        Pattern entirePattern = new Pattern();
        entirePattern.add(pattern);
        entirePattern.add(percussionPattern);
        return entirePattern;
    }
    public int compareTo(PatternAndRating comparisonPattern){
        //ascending order
        return this.rating - comparisonPattern.rating;
    }

    private void initializePatterns(String pattern){
        String[] splitPatterns = pattern.split("V1");
        this.pattern =  new Pattern(splitPatterns[0]);
        if(splitPatterns.length <2 )
            this.percussionPattern = new Pattern("");
        else {
            this.percussionPattern = new Pattern("V1 " + splitPatterns[1]);
        }
    }

    private void initializePatterns(Pattern pattern){
        String[] splitPatterns = pattern.toString().split("V1");
        this.pattern =  new Pattern(splitPatterns[0]);
        if(splitPatterns.length <2 )
            this.percussionPattern = new Pattern("");
        else {
            this.percussionPattern = new Pattern("V1 " + splitPatterns[1]);
        }
    }
}
