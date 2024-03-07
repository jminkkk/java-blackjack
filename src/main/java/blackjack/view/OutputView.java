package blackjack.view;

import blackjack.model.Card;
import blackjack.view.score.Converter;
import blackjack.view.score.ShapeConverter;
import java.util.List;
import java.util.stream.Collectors;

public class OutputView {
    public static void printDistributionSubject(final List<String> names) {
        String formattedName = String.join(", ", names);
        System.out.println(String.format("딜러와 %s에게 2장을 나누었습니다.", formattedName));
    }

    public static void printInitialCards(final String name, final List<Card> cards) {
        System.out.println(name + ": " + convert(cards));
    }

    private static String convert(final List<Card> cards) {
        return cards.stream()
                .map(card -> Converter.getValue(card.getScore()) + ShapeConverter.getValue(card.getShape()))
                .collect(Collectors.joining(", "));
    }
}