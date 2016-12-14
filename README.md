# AIMusic - Intelligently Developed Compositions (IDC)

**Contributors:**

*First Name, Last Name, N#*

* Mahmoud Abugharbieh, MKA299

* John Vasquez, JV1065

* Andreina Vivas Thomas, AV1351

#About IDC

IDC uses the [jFugue](http://www.jfugue.org/doc/index.html) API for playing music.

  *IDC* is a Java written application that lets you choose between two evolutionary algorithms to develop and evolve your own music -
  [Genetic Evolution](https://en.wikipedia.org/wiki/Genetic_algorithm) , and [Mu Lambda Evolution](http://seat.massey.ac.nz/personal/s.r.marsland/PUBS/AAAI11a.pdf).
  
  The current fitness function to determine how each generation is evolved is user rating.  There are three dropdown
  boxes to choose user rating because our team consists of 3 people, and we each wanted to have our own vote.
  It is not necessary to rate three times though.  A major downside to this is that it removes the possibility of having 
  multiple generations worth of evolution in one go since evaluation of our fitness function requires user input every
  generation.
  
 
  
#How to use IDC
  
**1. Create a population**
  *  **Instantiating a random population -**
  
  Enter in the size of your random population.  That is, how many random pieces of music you'd like to generate.  Then, enter in
  the length of each piece of music in terms of how many notes per piece.
  
  *  **Loading from a file -**
  
  Loading from a file follows the jFugue music pattern style, and our "pattern and rating" approach.  The pattern and rating 
  approach is simply a file where each line is a jFugue pattern sequence and a rating separated by a comma.
  
  Ex. E D C R E D C R E E E E D D D D E D C, 10
  
  (For more complex patterns, refer to the jFugue documentation)

**2. Choose your evolutionary strategy**
  * Select one of the two radio buttons located at the bottom right of the application - either "muLambda" or "Genetic"
  
**3. Play and rate music**
  * Playing a piece of music -
  
  To play a song, select the desired piece from the listbox on the lefthand side, then click on the "Play" button on the buttom.
  
  * Rating a piece of music -
  
  To rate a song, select the desired piece from the listbox on the lefthand side, then select the desired rating from the dropdowns
  on the right hand side.  After all the ratings are in, press the "Submit Rating" button.
  
**4. Evolving the generation**
  * Your desired evolutionary strategy should have been selected from Step 2.  However, if not, do so now, then press the "Evolve"
  button.  Repeat steps 3-4 until desired.
  
**5.  Save your progress**
  *  To save your progress, give your generation a name in the "Save File Name Box" box, then click on "Save To File".
  When you want to load your saved progress, the file should be located in the same working directory as IDC
  
  
  
