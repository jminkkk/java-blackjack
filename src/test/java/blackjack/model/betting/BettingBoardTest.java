package blackjack.model.betting;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.model.deck.Card;
import blackjack.model.deck.Score;
import blackjack.model.deck.Shape;
import blackjack.model.participant.Hand;
import blackjack.model.participant.Player;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class BettingBoardTest {

    @Nested
    class PlayerMoney {
        private BettingBoard bettingBoard;
        private Player player;

        @BeforeEach
        void init() {
            player = Player.of("몰리",
                    new Hand(List.of(new Card(Shape.DIA, Score.TEN), new Card(Shape.DIA, Score.ACE))));
            BettingAmount bettingAmount = new BettingAmount(1000);
            Map<Player, BettingAmount> bettingAmountsPerPlayer = Map.of(player, bettingAmount);

            bettingBoard = new BettingBoard(bettingAmountsPerPlayer);
        }

        @Test
        @DisplayName("플레이어가 만약 블랙잭으로 승리한 경우, 베팅 금액의 1.5배를 딜러에게 받는다.")
        void giveWinnerMoneyByBlackJack() {
            assertThat(bettingBoard.giveWinnerMoneyByBlackJack(player)).isEqualTo(new BettingAmount(1500));
        }

        @Test
        @DisplayName("플레이어가 만약 블랙잭으로 무승부인 경우, 베팅 금액을 돌려받는다.")
        void giveTieMoneyByBlackJack() {
            assertThat(bettingBoard.giveTieMoneyByBlackJack(player)).isEqualTo(new BettingAmount(1000));
        }

        @Test
        @DisplayName("딜러가 21을 초과하면 그 시점까지 남아 있던 플레이어들은 가지고 있는 패에 상관 없이 승리해 베팅 금액을 받는다.")
        void giveWinnerMoneyWhenDealerBust() {
            assertThat(bettingBoard.giveWinnerMoneyWhenDealerBust(player)).isEqualTo(new BettingAmount(1000));
        }

        @Test
        @DisplayName("21을 초과한 경우 플레이어의 배팅 금액은 모두 잃는다.")
        void payMoneyWhenPlayerBust() {
            assertThat(bettingBoard.payMoneyWhenPlayerBust(player)).isEqualTo(new BettingAmount(0));
        }
    }
}
