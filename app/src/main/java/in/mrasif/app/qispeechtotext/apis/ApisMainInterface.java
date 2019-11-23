package in.mrasif.app.qispeechtotext.apis;

import java.util.List;

import in.mrasif.app.qispeechtotext.models.Dictionary;
import in.mrasif.app.qispeechtotext.models.Item;
import in.mrasif.app.qispeechtotext.utils.AppConfig;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApisMainInterface {

    @GET(AppConfig.URLs.DICTIONARY)
    Call<Dictionary> getDictionary();

}
