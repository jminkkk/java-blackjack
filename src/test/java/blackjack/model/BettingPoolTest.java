package blackjack.model;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.model.deck.Card;
import blackjack.model.deck.Score;
import blackjack.model.deck.Shape;
import blackjack.model.participant.BettingAmount;
import blackjack.model.participant.Hand;
import blackjack.model.participant.Player;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BettingPoolTest {

    @Test
    @DisplayName("플레이어가 만약 블랙잭으로 승리한 경우, 베팅 금액의 1.5배를 딜러에게 받는다.")
    void giveWinnerMoneyByBlackJack() {
        Player player = Player.of("몰리", new Hand(List.of(new Card(Shape.DIA, Score.TEN), new Card(Shape.DIA, Score.ACE))));
        BettingAmount bettingAmount = new BettingAmount(1000);
        Map<Player, BettingAmount> moneyByPlayer = Map.of(player, bettingAmount);

        BettingPool bettingPool = new BettingPool(moneyByPlayer);
        assertThat(bettingPool.giveWinnerMoneyByBlackJack(player)).isEqualTo(bettingAmount.multiple(1.5));
    }

    @Test
    @DisplayName("플레이어가 만약 블랙잭으로 무승부인 경우, 베팅 금액을 돌려받는다.")
    void giveTieMoneyByBlackJack() {
        Player player = Player.of("몰리", new Hand(List.of(new Card(Shape.DIA, Score.TEN), new Card(Shape.DIA, Score.ACE))));
        BettingAmount bettingAmount = new BettingAmount(1000);
        Map<Player, BettingAmount> moneyByPlayer = Map.of(player, bettingAmount);

        BettingPool bettingPool = new BettingPool(moneyByPlayer);
        assertThat(bettingPool.giveTieMoneyByBlackJack(player)).isEqualTo(bettingAmount);
    }

    @Test
    @DisplayName("딜러가 21을 초과하면 그 시점까지 남아 있던 플레이어들은 가지고 있는 패에 상관 없이 승리해 베팅 금액을 받는다.")
    void giveWinnerMoneyWhenDealerBust() {
        Player player = Player.of("몰리", new Hand(List.of(new Card(Shape.DIA, Score.TEN), new Card(Shape.DIA, Score.ACE))));
        BettingAmount bettingAmount = new BettingAmount(1000);
        Map<Player, BettingAmount> moneyByPlayer = Map.of(player, bettingAmount);

        BettingPool bettingPool = new BettingPool(moneyByPlayer);
        assertThat(bettingPool.giveWinnerMoneyWhenDealerBust(player)).isEqualTo(bettingAmount);
    }
}
