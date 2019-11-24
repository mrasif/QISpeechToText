package in.mrasif.app.qispeechtotext;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import in.mrasif.app.qispeechtotext.models.Item;

public class AppTest {
    @Test
    public void modelSortingTestCase(){
        List<Item> items=new ArrayList<>();
        Item item1=new Item("Hello");
        item1.setFrequency(5);
        items.add(item1);
        Item item2=new Item("world");
        item2.setFrequency(10);
        items.add(item2);
        /**
         * Sort by frequency as descending
         */
        Collections.sort(items);
        assert items.get(0).equals(item2);
    }

}
