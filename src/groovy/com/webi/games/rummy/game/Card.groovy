package com.webi.games.rummy.game

/**
 * Created by yuyutsu on 2/23/14.
 */
enum Type {
    Clubs,
    Diamonds,
    Hearts,
    Spades
}

class Card {
    int index //index shown in UI
    Type type
    int value //1-13. 1 is Ace, 11-Jack, 12-Queen, 13-King
    private static final Map valueToWord = [1: 'Ace', 2: 'Two', 3: 'Three', 4: 'Four', 5: 'Five', 6: 'Six', 7: 'Seven',
            8: 'Eight', 9: 'Nine', 10: 'Ten', 11: 'Jack', 12: 'Queen', 13: 'King']

    String imageFileName() {
        "$type/${valueToWord[value]}.jpg"
    }

    @Override
    public String toString() {
        return "Card{" +
                "index=" + index +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
