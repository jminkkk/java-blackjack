package blackjack.model.participant;

import static blackjack.model.deck.Score.ACE;
import static blackjack.model.deck.Score.EIGHT;
import static blackjack.model.deck.Score.FIVE;
import static blackjack.model.deck.Score.FOUR;
import static blackjack.model.deck.Shape.CLOVER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import blackjack.model.deck.Card;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class ParticipantsTest {

    @Test
    @DisplayName("플레이어의 이름과 초기 카드들을 받아서 플레이어 그룹을 생성한다.")
    void createPlayers() {
        List<String> names = List.of("리브", "몰리");
        List<Hand> cards = List.of(
                new Hand(List.of(new Card(CLOVER, ACE), new Card(CLOVER, FIVE))),
                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT)))
        );
        assertThatCode(() -> Participants.of(names, cards));
    }

    @Test
    @DisplayName("플레이어의 이름이 중복되는 경우 예외를 던진다.")
    void createPlayersByDuplicatedName() {
        List<String> names = List.of("몰리", "몰리");
        List<Hand> cards = List.of(
                new Hand(List.of(new Card(CLOVER, ACE), new Card(CLOVER, FIVE))),
                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT)))
        );
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Participants.of(names, cards))
                .withMessage("중복되는 이름을 입력할 수 없습니다.");
    }

    @Test
    @DisplayName("플레이어 인원과 초기 카드 목록의 사이즈가 다른 경우 예외를 던진다.")
    void createPlayersByDifferCardsSize() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Participants.of(List.of("몰리", "파랑"), List.of()))
                .withMessage("플레이어 인원과 초기 카드 목록의 사이즈가 다릅니다.");
    }

    @ParameterizedTest
    @MethodSource("InvalidNames")
    @DisplayName("플레이어의 이름이 1개 이상 10개 이하가 아니면 예외를 던진다.")
    void createPlayersByOutBound(List<String> names, List<Hand> cards) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> Participants.of(names, cards))
                .withMessage("참여할 인원의 수는 최소 1명 최대 10명이어야 합니다.");
    }

    private static Stream<Arguments> InvalidNames() {
        return Stream.of(
                Arguments.arguments(List.of(), List.of()),
                Arguments.arguments(List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"),
                        List.of(
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))),
                                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT)))
                        )
                ));
    }

    @Test
    @DisplayName("플레이어들의 이름 목록을 반환한다.")
    void getNames() {
        List<String> names = List.of("리브", "몰리");
        List<Hand> cards = List.of(
                new Hand(List.of(new Card(CLOVER, ACE), new Card(CLOVER, FIVE))),
                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT)))
        );
        assertThat(Participants.of(names, cards).getNames()).containsExactly(new Name("리브"), new Name("몰리"));
    }

    @Test
    @DisplayName("각 플레이어의 이름과 카드들을 모아서 반환한다.")
    void collectCardsOfEachPlayer() {
        List<String> names = List.of("리브", "몰리");
        List<Hand> cards = List.of(
                new Hand(List.of(new Card(CLOVER, ACE), new Card(CLOVER, FIVE))),
                new Hand(List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT)))
        );

        Map<Name, List<Card>> expected = new LinkedHashMap<>();
        expected.put(new Name("리브"), List.of(new Card(CLOVER, ACE), new Card(CLOVER, FIVE)));
        expected.put(new Name("몰리"), (List.of(new Card(CLOVER, FOUR), new Card(CLOVER, EIGHT))));
        assertThat(Participants.of(names, cards).collectCardsOfEachPlayer())
                .containsExactlyEntriesOf(expected);
    }
}
