package in.mrasif.app.qispeechtotext.models;

import java.util.ArrayList;
import java.util.List;

public class Dictionary {
    List<Item> dictionary;

    public Dictionary() {
        this.dictionary=new ArrayList<>();
    }

    public List<Item> getDictionary() {
        return dictionary;
    }

    public void setDictionary(List<Item> dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public String toString() {
        return "Dictionary{" +
                "dictionary=" + dictionary +
                '}';
    }
}
