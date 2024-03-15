package blackjack.model.result;

import static blackjack.model.result.ResultCommand.DRAW;
import static blackjack.model.result.ResultCommand.LOSE;
import static blackjack.model.result.ResultCommand.WIN;
import static org.assertj.core.api.Assertions.assertThat;

import blackjack.model.deck.Card;
import blackjack.model.deck.Score;
import blackjack.model.deck.Shape;
import blackjack.model.participant.Dealer;
import blackjack.model.participant.Hand;
import blackjack.model.participant.Players;
import blackjack.model.participant.Player;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class RefereeTest {
    @Nested
    @DisplayName("딜러의 합이 21일 미만이면")
    class under21 {

        @Test
        @DisplayName("플레이어의 합이 딜러보다 크면 플레이어가 승리한다.")
        void playerWinWhenBiggerThanDealer() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN)))));
            Dealer dealer = new Dealer(
                    new Hand(List.of(new Card(Shape.SPADE, Score.SEVEN), new Card(Shape.DIA, Score.TEN))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), WIN));
        }

        @Test
        @DisplayName("플레이어 결과가 딜러의 결과와 동일하지만 카드 수는 적은 경우 플레이어가 승리한다.")
        void playerWinWhenHasLittleCardsThanDealer() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN)))));
            Dealer dealer = new Dealer(new Hand(
                    List.of(new Card(Shape.SPADE, Score.SEVEN), new Card(Shape.DIA, Score.TEN),
                            new Card(Shape.DIA, Score.THREE))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), WIN));
        }
    }

    @Nested
    @DisplayName("딜러가 21인 경우")
    class DealerIs21 {

        @Test
        @DisplayName("플레이어 카드만 블랙잭인 경우 플레이어가 승리한다.")
        void playerWinWhenOnlyPlayerBlackJack() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.ACE)))));
            Dealer dealer = new Dealer(new Hand(
                    List.of(new Card(Shape.SPADE, Score.SEVEN), new Card(Shape.DIA, Score.TEN),
                            new Card(Shape.DIA, Score.FOUR))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), WIN));
        }

        @Test
        @DisplayName("플레이어의 카드 수가 딜러의 카드 수보다 적은 경우 플레이어가 승리한다.")
        void playerWinWhenHasLittleCardsThanDealer() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.CLOVER, Score.NINE),
                            new Card(Shape.SPADE, Score.TWO)))));
            Dealer dealer = new Dealer(new Hand(
                    List.of(new Card(Shape.SPADE, Score.SEVEN), new Card(Shape.DIA, Score.FIVE),
                            new Card(Shape.CLOVER, Score.FIVE), new Card(Shape.DIA, Score.FOUR))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), WIN));
        }
    }

    @Nested
    @DisplayName("딜러가 21 초과했을 경우")
    class DealerOver21 {

        @Test
        @DisplayName("플레이어 결과가 21이하인 경우 플레이어가 승리한다.")
        void playerWinWhenOnlyPlayerNotBust() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.FOUR),
                            new Card(Shape.DIA, Score.SIX)))));
            Dealer dealer = new Dealer(new Hand(
                    List.of(new Card(Shape.SPADE, Score.FOUR), new Card(Shape.DIA, Score.TEN),
                            new Card(Shape.DIA, Score.TEN))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), WIN));
        }
    }

    @Nested
    @DisplayName("딜러와 플레이어가 무승부인 경우")
    class draw {

        @Test
        @DisplayName("플레이어와 딜러 모두 블랙잭인 경우")
        void bothBlackJack() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.ACE)))));
            Dealer dealer = new Dealer(
                    new Hand(List.of(new Card(Shape.HEART, Score.ACE), new Card(Shape.DIA, Score.TEN))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), DRAW));
        }

        @Test
        @DisplayName("플레이어와 딜러의 결과, 카드 수가 모두 동일한 경우")
        void sameScoreAndCount() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.FIVE), new Card(Shape.SPADE, Score.FIVE)))));
            Dealer dealer = new Dealer(
                    new Hand(List.of(new Card(Shape.HEART, Score.FIVE), new Card(Shape.DIA, Score.FIVE))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), DRAW));
        }

        @Test
        @DisplayName("플레이어와 딜러 모두 21 초과인 경우")
        void bothBust() {
            Players players = Players.of(
                    List.of("몰리"),
                    List.of(new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN),
                            new Card(Shape.SPADE, Score.NINE)))));
            Dealer dealer = new Dealer(
                    new Hand(List.of(new Card(Shape.HEART, Score.TEN), new Card(Shape.DIA, Score.TEN),
                            new Card(Shape.DIA, Score.NINE))));

            Referee referee = new Referee(new ResultRule(dealer), players);
            assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(Map.of(players.getPlayers().get(0), DRAW));
        }
    }

    @Test
    @DisplayName("플레이어들의 결과를 반환한다.")
    void getPlayerResult() {
        List<Hand> cards = List.of(
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN),
                        new Card(Shape.DIA, Score.FOUR))),
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.ACE))),
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN)))
        );

        Players players = Players.of(List.of("몰리", "리브", "포비"), cards);
        Dealer dealer = new Dealer(
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN))));

        Referee referee = new Referee(new ResultRule(dealer), players);
        Map<Player, ResultCommand> expected = new LinkedHashMap<>();
        expected.put(players.getPlayers().get(0), LOSE);
        expected.put(players.getPlayers().get(1), WIN);
        expected.put(players.getPlayers().get(2), DRAW);
        assertThat(referee.judgePlayerResult()).containsExactlyEntriesOf(expected);
    }

    @Test
    @DisplayName("딜러의 결과를 반환한다.")
    void getDealerResult() {
        List<Hand> cards = List.of(
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN),
                        new Card(Shape.DIA, Score.FOUR))),
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.ACE))),
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN)))
        );

        Players players = Players.of(List.of("몰리", "리브", "포비"), cards);
        Dealer dealer = new Dealer(
                new Hand(List.of(new Card(Shape.CLOVER, Score.TEN), new Card(Shape.SPADE, Score.TEN))));

        Referee referee = new Referee(new ResultRule(dealer), players);
        assertThat(referee.judgeDealerResult()).containsAllEntriesOf(Map.of(WIN, 1, LOSE, 1, DRAW, 1));
    }
}
