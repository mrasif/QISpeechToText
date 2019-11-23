package in.mrasif.app.qispeechtotext.models;

import java.util.Objects;

public class Item implements Comparable {
    private String word;
    private int frequency;
    private boolean highlight;

    public Item() {
    }

    public Item(String word) {
        this.word = word;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(boolean highlight) {
        this.highlight = highlight;
    }

    @Override
    public String toString() {
        return "Item{" +
                "word='" + word + '\'' +
                ", frequency=" + frequency +
                ", highlight=" + highlight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return word.equalsIgnoreCase(item.word);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public int compareTo(Object o) {
        Item item=(Item)o;
        return frequency==item.frequency?0:(frequency<item.frequency?1:-1);
    }
}
