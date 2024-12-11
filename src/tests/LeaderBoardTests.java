package tests;
import leaderboards.Leaderboard;
import leaderboards.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class LeaderBoardTests {

    private Leaderboard leaderboard = new Leaderboard();
    @Test
    public void testNormLeaderboard() {
        ArrayList<Player> testLeaderboard = leaderboard.getLeaderboardFromBinary("testExpBinary.ser");
        leaderboard.displayLeaderboard(testLeaderboard);
        assertEquals(10, testLeaderboard.size());
    }

    @Test
    public void testCreateLeaderboard() {
        ArrayList<Player> testLeaderboard = leaderboard.getLeaderboardFromBinary("noFileExist.ser");
        assertTrue(testLeaderboard.isEmpty());
        testLeaderboard = leaderboard.createLeaderboard("testExpLeaderboard.txt");
        assertEquals(10, testLeaderboard.size());
        leaderboard.updateLeaderboard(testLeaderboard, null, "testExpBinary.ser");
    }


    @Test
    public void testAddPlayerToLB() {
        ArrayList<Player> testLeaderboard2 = leaderboard.getLeaderboardFromBinary("testExpBinary.ser");
        Player testPlayer = new Player("test", 0, 100, 1000);
        leaderboard.updateLeaderboard(testLeaderboard2, testPlayer, "testBOutput.ser");
        assertEquals(11, testLeaderboard2.size());
        ArrayList<Player> testDeserializedLB = leaderboard.getLeaderboardFromBinary("testBOutput.ser");
        assertEquals(testDeserializedLB.getLast().toString(),testPlayer.toString());
    }

    @Test
    public void testEmptyLeaderboard() {
        ArrayList<Player> testLeaderboard3 = leaderboard.getLeaderboardFromBinary("Test.txt");
        assertTrue(testLeaderboard3.isEmpty());
    }

    @Test
    public void testLBFilters1() {
        ArrayList<Player> testLeaderboard4 = leaderboard.getLeaderboardFromBinary("testExpBinary.ser");
        ArrayList<Player> filteredPlayers = new ArrayList<>();
        testLeaderboard4.stream()
                .filter(player -> player.getPuzzlesSolved() > 3)
                .forEach(player -> filteredPlayers.add(player));

        assertEquals(4, filteredPlayers.size());
        leaderboard.displayLeaderboard(filteredPlayers);
    }

    @Test
    public void testLBFilter2() {
        ArrayList<Player> testLeaderboard5 = leaderboard.getLeaderboardFromBinary("testExpBinary.ser");
        double avgTime = testLeaderboard5.stream()
                .mapToLong(Player::getTime)
                .average()
                .orElse(0.0);
        assertEquals(109.6, avgTime);
    }
}
