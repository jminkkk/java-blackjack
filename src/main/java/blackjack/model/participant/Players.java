package blackjack.model.participant;

import blackjack.dto.NameCardsScore;
import blackjack.model.deck.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Players {
    private static final int MINIMUM_PLAYER_SIZE = 1;
    private static final int MAXIMUM_PLAYER_SIZE = 10;

    private final List<Player> players;

    private Players(final List<Player> players) {
        validatePlayerSize(players);
        this.players = players;
    }

    public static Players of(final List<String> rawNames, final List<Hand> cards) {
        validateNotDuplicateName(rawNames);
        validateNamesInitialCardsSize(rawNames, cards);
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < rawNames.size(); i++) {
            players.add(Player.of(rawNames.get(i), cards.get(i)));
        }
        return new Players(players);
    }

    private static void validateNamesInitialCardsSize(final List<String> rawNames, final List<Hand> cards) {
        if (rawNames.size() != cards.size()) {
            throw new IllegalArgumentException("플레이어 인원과 초기 카드 목록의 사이즈가 다릅니다.");
        }
    }

    private static void validateNotDuplicateName(final List<String> names) {
        long uniqueNameSize = names.stream()
                .distinct()
                .count();
        if (names.size() != uniqueNameSize) {
            throw new IllegalArgumentException("중복되는 이름을 입력할 수 없습니다.");
        }
    }

    private void validatePlayerSize(final List<Player> players) {
        if (players.size() < MINIMUM_PLAYER_SIZE || players.size() > MAXIMUM_PLAYER_SIZE) {
            throw new IllegalArgumentException("참여할 인원의 수는 최소 1명 최대 10명이어야 합니다.");
        }
    }

    public Map<String, List<Card>> collectCardsOfEachPlayer() {
        Map<String, List<Card>> result = new LinkedHashMap<>();
        for (Player player : players) {
            result.put(player.getName(), player.openCards());
        }
        return result;
    }

    public List<NameCardsScore> collectFinalResults() {
        return players.stream()
                .map(player -> new NameCardsScore(player.getName(), player.openCards(), player.getScore()))
                .toList();
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public List<String> getNames() {
        return players.stream()
                .map(Player::getName)
                .toList();
    }
}
