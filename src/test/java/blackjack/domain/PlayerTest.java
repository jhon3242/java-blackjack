package blackjack.domain;

import static org.assertj.core.api.Assertions.assertThat;

import blackjack.domain.card.Card;
import blackjack.domain.card.Rank;
import blackjack.domain.card.Shape;
import blackjack.domain.participants.Hands;
import blackjack.domain.participants.Name;
import blackjack.domain.participants.Player;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
    @DisplayName("플레이어가 잘 생성된다.")
    void playerConstructSuccessTest() {
        Assertions.assertThatNoException()
                .isThrownBy(() -> new Player(new Name("이름")));
    }

    @Test
    @DisplayName("플레이어가 패를 받는다.")
    void receiveDeckTest() {
        Player player = new Player(new Name("이름"));
        Hands hands = new Hands(List.of(
                new Card(Shape.HEART, Rank.ACE),
                new Card(Shape.HEART, Rank.TWO))
        );
        player.receiveHands(hands);

        assertThat(player.getHands().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("플레이어가 카드를 더 받을 수 있다.")
    void isPlayerNotOverTest() {
        Player player = new Player(new Name("이름"));
        Hands hands = new Hands(List.of(
                new Card(Shape.HEART, Rank.ACE),
                new Card(Shape.HEART, Rank.TWO))
        );
        player.receiveHands(hands);
        assertThat(player.canHit()).isTrue();
    }

    @Test
    @DisplayName("플레이어가 카드를 잘 받는다.")
    void playerReceiveCardTest() {
        Player player = new Player(new Name("이름"));
        Assertions.assertThatNoException()
                .isThrownBy(() -> player.hit(new Card(Shape.HEART, Rank.ACE)));
    }

    @Test
    @DisplayName("플레이어 점수를 계산한다.")
    void calculateScoreTest() {
        Player player = new Player(new Name("이름"));
        Hands hands = new Hands(List.of(
                new Card(Shape.HEART, Rank.ACE),
                new Card(Shape.HEART, Rank.TWO))
        );

        player.receiveHands(hands);
        int result = player.calculateScore();

        assertThat(result).isEqualTo(13);
    }

    @Test
    @DisplayName("딜러가 터지고 플레이어가 안 터진 경우 플레이어가 이긴다.")
    void isWinWithDealerBurstTest() {
        Player player = new Player(new Name("이름"));
        player.hit(new Card(Shape.HEART, Rank.NINE));
        player.hit(new Card(Shape.SPADE, Rank.NINE));

        assertThat(player.isWin(25)).isTrue();
    }

    @Test
    @DisplayName("딜러와 플레이어가 터진 경우 딜러가 이긴다.")
    void isWinWithBothBurstTest() {
        Player player = new Player(new Name("이름"));
        player.hit(new Card(Shape.HEART, Rank.NINE));
        player.hit(new Card(Shape.SPADE, Rank.NINE));
        player.hit(new Card(Shape.SPADE, Rank.NINE));

        assertThat(player.isWin(25)).isFalse();
    }

    @Test
    @DisplayName("둘 다 터지지 않고 딜러 점수가 더 높은 경우 딜러가 이긴다.")
    void isWinWithDealerWinTest() {
        Player player = new Player(new Name("이름"));
        player.hit(new Card(Shape.HEART, Rank.NINE));
        player.hit(new Card(Shape.SPADE, Rank.NINE));

        assertThat(player.isWin(21)).isFalse();
    }

    @Test
    @DisplayName("딜러와 플레이어 점수가 같은 경우 딜러가 이긴다.")
    void isWinWithSameScoreTest() {
        Player player = new Player(new Name("이름"));
        player.hit(new Card(Shape.HEART, Rank.TEN));
        player.hit(new Card(Shape.SPADE, Rank.ACE));

        assertThat(player.isWin(21)).isFalse();
    }

    @Test
    @DisplayName("둘 다 터지지 않고 플레이어 점수가 더 높은 경우 플레이어가 이긴다.")
    void isWinWithPlayerWinTest() {
        Player player = new Player(new Name("이름"));
        player.hit(new Card(Shape.HEART, Rank.KING));
        player.hit(new Card(Shape.SPADE, Rank.ACE));

        assertThat(player.isWin(20)).isTrue();
    }
}
