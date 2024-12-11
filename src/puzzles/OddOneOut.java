package puzzles;

import java.util.Arrays;

// To represent an OddOneOut type of Puzzle
public class OddOneOut extends Puzzle<String[]> {

    public OddOneOut(String difficulty, String hint, String answer, String type, String question, String[] visual) {
        super(difficulty, hint, answer, type, question, visual);

    }

    public void displayVisual(String[] visual) {
        /*
        Intent: Displays the visual for an OddOneOut type Puzzle.
        Postcondition: the visual for the OddOneOut type is displayed to the console
         */
        System.out.println(Arrays.toString(visual));
    }
}
