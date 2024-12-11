package puzzles;

// To represent a Riddle type of Puzzle
public class Riddle extends Puzzle<String> {

    public Riddle(String difficulty, String hint, String answer, String type, String question, String visual) {
        super(difficulty, hint, answer, type, question, visual);

    }

    public void displayVisual(String visual) {
        /*
        Intent: Displays the visual for a Riddle type Puzzle.
        Postcondition: the visual for the Riddle type is displayed to the console
         */
        System.out.println(visual);
    }
}
