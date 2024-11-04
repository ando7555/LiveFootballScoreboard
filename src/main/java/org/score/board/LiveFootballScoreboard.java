package org.score.board;

import java.time.LocalDateTime;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LiveFootballScoreboard {
    private final Map<String, Match> matches;

    public LiveFootballScoreboard() {
        this.matches = new HashMap<>();
    }

    public void startMatch(String homeTeam, String awayTeam) {
        String key = homeTeam + " vs " + awayTeam;
        if (matches.containsKey(key)) {
            throw new IllegalArgumentException("Match already exists.");
        }
        matches.put(key, new Match(homeTeam, awayTeam, LocalDateTime.now()));
    }

    public void updateScore(String homeTeam, String awayTeam, int homeScore, int awayScore) {
        Match match = getMatch(homeTeam, awayTeam);
        match.updateScore(homeScore, awayScore);
    }

    public void finishMatch(String homeTeam, String awayTeam) {
        matches.remove(homeTeam + " vs " + awayTeam);
    }

    public List<Match> getSummary() {
        return matches.values().stream()
                .sorted(Comparator.comparingInt(Match::getTotalScore).reversed()
                        .thenComparing(Match::getStartTime, Comparator.reverseOrder()))
                .toList();
    }

    private Match getMatch(String homeTeam, String awayTeam) {
        Match match = matches.get(homeTeam + " vs " + awayTeam);
        if (match == null) {
            throw new IllegalArgumentException("Match not found.");
        }
        return match;
    }
}