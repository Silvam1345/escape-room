package tests;
import org.junit.jupiter.api.Test;
import puzzles.*;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.ExecutorService;
import leaderboards.*;

import static org.junit.jupiter.api.Assertions.*;
public class PuzzleConcurrencyTests {
    public static ExecutorService executorService;

    @Test
    public void testThreadConcurrency() throws ExecutionException, InterruptedException {
        executorService = Executors.newCachedThreadPool();
        Leaderboard leaderboard = new Leaderboard();
        Future<ArrayList<Puzzle>> puzzleFuture = executorService.submit(() ->
                PuzzleDataHandler.loadPuzzlesFromFile("allPuzzles.txt", 5));
        Future<ArrayList<Player>> playerFuture = executorService.submit(() ->
                leaderboard.getLeaderboardFromBinary("testExpBinary.ser"));
        ArrayList<Puzzle> puzzles = puzzleFuture.get();
        assertEquals(5, puzzles.size());
        ArrayList<Player> players = playerFuture.get();
        assertEquals(10, players.size());
        executorService.shutdown();


    }
}
