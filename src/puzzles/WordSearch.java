package puzzles;

public class WordSearch extends Puzzle<String[][]> {

    public WordSearch(String difficulty, String hint, String answer, String type, String question, String[][] visual) {
        super(difficulty, hint, answer, type, question, visual);
    }

    public void displayVisual(String[][] visual) {
        /*
        Intent: Displays the visual for a Crossword type Puzzle.
        Postcondition: the 2d grid visual for the Crossword puzzle is displayed to the console
         */
        for (int i = 0; i < visual.length; i++) {
            for (int j = 0; j < visual[i].length; j++) {
                System.out.print(visual[i][j] + " ");
            }
            System.out.println();
        }
    }
}
