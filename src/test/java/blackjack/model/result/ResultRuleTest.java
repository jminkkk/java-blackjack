package blackjack.model.result;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.model.deck.Card;
import blackjack.model.deck.Score;
import blackjack.model.deck.Shape;
import blackjack.model.participant.Dealer;
import blackjack.model.participant.Hand;
import blackjack.model.participant.Player;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ResultRuleTest {

    @Test
    @DisplayName("딜러와 비교하여 결과를 계산한다.")
    void calculateResult() {
        ResultRule resultRule = new ResultRule(
                new Dealer(new Hand(List.of(new Card(Shape.DIA, Score.TEN), new Card(Shape.SPADE, Score.TEN)))));
        Player player = Player.of("리브",
                new Hand(List.of(new Card(Shape.DIA, Score.ACE), new Card(Shape.SPADE, Score.TEN))));
        assertThat(resultRule.calculateResult(player)).isEqualTo(ResultCommand.WIN);
    }

}
