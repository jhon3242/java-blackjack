package blackjack.domain.participants;

import blackjack.domain.card.Card;
import blackjack.domain.card.Rank;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Hands {

    private final List<Card> cards;

    public Hands(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public int size() {
        return cards.size();
    }

    public int calculateScore() {
        int totalScore = 0;
        for (Card card : cards) {
            Rank rank = card.getRank();
            totalScore += rank.getScore(totalScore);
        }
        return totalScore;
    }

    public void receiveHands(Hands newHands) {
        cards.addAll(newHands.cards);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }
}
