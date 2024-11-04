package org.score.board;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class LiveFootballScoreboardAcceptanceTest {
    private LiveFootballScoreboard scoreboard;

    @BeforeEach
    void setUp() {
        scoreboard = new LiveFootballScoreboard();
    }

    @Test
    void testStartMatch() {
        scoreboard.startMatch("Mexico", "Canada");
        List<Match> summary = scoreboard.getSummary();
        assertEquals(1, summary.size());
        assertEquals("Mexico 0 - Canada 0", summary.get(0).toString());
    }

    @Test
    void testUpdateScore() {
        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);
        List<Match> summary = scoreboard.getSummary();
        assertEquals("Spain 10 - Brazil 2", summary.get(0).toString());
    }

    @Test
    void testFinishMatch() {
        scoreboard.startMatch("Germany", "France");
        scoreboard.finishMatch("Germany", "France");
        List<Match> summary = scoreboard.getSummary();
        assertTrue(summary.isEmpty());
    }

    @Test
    void testGetSummaryOrderByTotalScoreAndTime() {
        scoreboard.startMatch("Mexico", "Canada");
        scoreboard.updateScore("Mexico", "Canada", 0, 5);

        scoreboard.startMatch("Spain", "Brazil");
        scoreboard.updateScore("Spain", "Brazil", 10, 2);

        scoreboard.startMatch("Germany", "France");
        scoreboard.updateScore("Germany", "France", 2, 2);

        scoreboard.startMatch("Uruguay", "Italy");
        scoreboard.updateScore("Uruguay", "Italy", 6, 6);

        scoreboard.startMatch("Argentina", "Australia");
        scoreboard.updateScore("Argentina", "Australia", 3, 1);

        List<Match> summary = scoreboard.getSummary();
        String[] expectedSummary = {
                "Uruguay 6 - Italy 6",
                "Spain 10 - Brazil 2",
                "Mexico 0 - Canada 5",
                "Argentina 3 - Australia 1",
                "Germany 2 - France 2"
        };

        for (int i = 0; i < expectedSummary.length; i++) {
            assertEquals(expectedSummary[i], summary.get(i).toString());
        }
    }

    @Test
    void testStartMatchWithDuplicateTeamsThrowsException() {
        scoreboard.startMatch("Uruguay", "Italy");
        assertThrows(IllegalArgumentException.class, () -> scoreboard.startMatch("Uruguay", "Italy"));
    }

    @Test
    void testUpdateScoreWithNegativeValuesThrowsException() {
        scoreboard.startMatch("Argentina", "Australia");
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore("Argentina", "Australia", -1, 0));
    }

    @Test
    void testUpdateScoreForNonExistentMatchThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> scoreboard.updateScore("England", "Italy", 1, 1));
    }
}
