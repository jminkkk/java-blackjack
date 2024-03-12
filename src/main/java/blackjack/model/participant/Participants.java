package blackjack.model.participant;

import blackjack.model.deck.Card;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Participants {
    private static final int MINIMUM_PLAYER_SIZE = 1;
    private static final int MAXIMUM_PLAYER_SIZE = 10;

    private final List<Player> players;

    private Participants(final List<Player> players) {
        validatePlayerSize(players);
        this.players = players;
    }

    public static Participants of(final List<String> rawNames, final List<Hand> cards) {
        validateNotDuplicateName(rawNames);
        validateNamesInitialCardsSize(rawNames, cards);
        return IntStream.range(0, rawNames.size())
                .mapToObj(index -> new Player(new Name(rawNames.get(index)), cards.get(index)))
                .collect(Collectors.collectingAndThen(Collectors.toList(), Participants::new));
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

    public Map<Name, List<Card>> collectCardsOfEachPlayer() {
        Map<Name, List<Card>> result = new LinkedHashMap<>();
        for (Player player : players) {
            result.put(player.getName(), player.openCards());
        }
        return result;
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public List<Name> getNames() {
        return players.stream()
                .map(Player::getName)
                .toList();
    }
}