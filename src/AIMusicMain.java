import org.jfugue.pattern.Pattern;
import org.jfugue.player.Player;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;



public class AIMusicMain {
    public static void main(String[] args)
    {
        Player musicPlayer = new Player();

    }

    //Saves the list of music as a .csv file
    public static void saveEvolvedMusic(List<Pattern> musicList)
    {
        try {
            //NOTE: Overwrites the previously saved evolvedMusicPieces
            PrintWriter file = new PrintWriter("evolvedMusicPieces.txt", "UTF-8");

            //Add each piece of music as a new line
            for(Pattern musicPiece : musicList){
                file.println(musicPiece);
            }
        }
        catch(IOException e)
        {
            //Show error message that save was not successful
        }
    }

    //Returns an empty list if there was an error reading the file
    public static List<Pattern> loadEvolvedMusic(){
        List<Pattern> loadedMusic = new ArrayList<Pattern>();

        try(BufferedReader fileReader = new BufferedReader( new FileReader("evolvedMusicPieces.txt"))){

            String musicPiece = null;

            while( (musicPiece = fileReader.readLine()) != null){
                loadedMusic.add(new Pattern(musicPiece));
            }

            return loadedMusic;
        }
        catch (IOException e){
            //If error reading file...
        }
        return loadedMusic;
    }
}
