package in.mrasif.app.qispeechtotext.apis;

import in.mrasif.app.qispeechtotext.utils.AppConfig;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Apis {
    private static Retrofit retrofit_main = null;

    /**
     * It will make single instance all over the app.
     * @return object of ApisMainInterface
     */
    public static synchronized ApisMainInterface getMainApis() {
        if (null == Apis.retrofit_main) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

            Retrofit.Builder builder=new Retrofit.Builder();
            builder.baseUrl(AppConfig.URLs.BASE_URL);
            builder.client(clientBuilder.build());
            builder.addConverterFactory(GsonConverterFactory.create());
            Apis.retrofit_main= builder.build();
        }
        return retrofit_main.create(ApisMainInterface.class);
    }

}
