package tests;
import leaderboards.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PlayerReadWriteTests {

    @Test
    public void testSerializePlayers() {
        List<Player> deserializedPlayers = PlayerDataHandler.deserializePlayers("testExpBinary.ser");
        assertEquals(10, deserializedPlayers.size());
        assertEquals("""
                Name: Terry
                Puzzles Solved: 5
                Time: 171 secs
                Final Score: 4500""", deserializedPlayers.getFirst().toString());
        assertEquals("""
                Name: Linda
                Puzzles Solved: 2
                Time: 89 secs
                Final Score: 1000""", deserializedPlayers.getLast().toString());
    }


}
