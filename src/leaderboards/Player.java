package leaderboards;

import java.io.Serial;
import java.io.Serializable;

public class Player implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String name;
    private int puzzlesSolved;
    private long timePlayed;

    private int finalScore;

    public Player(String name, int puzzlesSolved, long timePlayed, int finalScore) {
        this.name = name;
        this.puzzlesSolved = puzzlesSolved;
        this.timePlayed = timePlayed;
        this.finalScore = finalScore;
    }

    public String getName() {
        return name;
    }
    public int getPuzzlesSolved() {
        return puzzlesSolved;
    }

    public long getTime() {
        return timePlayed;
    }

    public int getFinalScore() {
        return finalScore;
    }

    @Override
    public String toString() {
        return "Name: " + name + "\nPuzzles Solved: " + puzzlesSolved + "\nTime: " + timePlayed + " secs" + "\nFinal Score: " + finalScore;
    }

}
