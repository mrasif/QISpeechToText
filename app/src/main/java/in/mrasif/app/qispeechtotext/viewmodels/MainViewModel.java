package in.mrasif.app.qispeechtotext.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.annimon.stream.Stream;

import java.util.List;

import in.mrasif.app.qispeechtotext.apis.Apis;
import in.mrasif.app.qispeechtotext.models.Dictionary;
import in.mrasif.app.qispeechtotext.models.Item;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private static final String TAG = "MainViewModel";
    private MutableLiveData<List<Item>> items;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<Item> item;
    private boolean isMatched;

    public LiveData<Item> getItem(){
        if (item==null){
            item=new MutableLiveData<>();
            item.setValue(new Item());
        }
        return item;
    }

    public LiveData<List<Item>> getItems(){
        if (items==null){
            items=new MutableLiveData<>();
            loadData();
        }
        return items;
    }

    public void changeVoiceOutput(String voice){
        Item item1=item.getValue();
        item1.setWord(voice);
        item.postValue(item1);
    }

    public LiveData<Boolean> isLoading(){
        if (isLoading==null){
            isLoading=new MutableLiveData<>();
            isLoading.setValue(false);
        }
        return isLoading;
    }

    public boolean updateUI(String text){
        isMatched=false;
        List<Item> dictionary=items.getValue();
        Item item=new Item(text);
        List<Item> newDictionary= Stream.of(dictionary).map(item1 -> {
            if (item.equals(item1)){
                item1.setHighlight(true);
                item1.setFrequency(item1.getFrequency()+1);
                isMatched=true;
                return item1;
            }
            else {
                item1.setHighlight(false);
                return item1;
            }
        }).toList();

        String[] textPart=text.split("\\s");
        if (textPart.length>1){
            for (String part:textPart){
                Item partItem=new Item(part);
                for (Item item1:newDictionary){
                    if (item1.equals(partItem)){
                        item1.setFrequency(item1.getFrequency()+1);
                    }
                }
            }
        }
        items.postValue(newDictionary);
        return isMatched;
    }

    private void loadData() {
        isLoading.postValue(true);
        Apis.getMainApis().getDictionary().enqueue(new Callback<Dictionary>() {
            @Override
            public void onResponse(Call<Dictionary> call, Response<Dictionary> response) {
                Log.d(TAG, "onResponse: "+response.message());
                Dictionary dictionary=response.body();
                if (null!=dictionary) {
                    List<Item> newItems = dictionary.getDictionary();
                    if (null != newItems) {
                        items.postValue(newItems);
                    }
                }
                isLoading.postValue(false);
            }

            @Override
            public void onFailure(Call<Dictionary> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
                isLoading.postValue(false);
            }
        });
    }
}
