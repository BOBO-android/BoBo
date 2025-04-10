package android.example.bobo.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "http://192.168.0.110:8080/api/v1/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            // Tạo Gson builder với các cấu hình
            Gson gson = new GsonBuilder()
                    .setLenient()  // Để xử lý JSON không nghiêm ngặt
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (retrofit == null) {
            getInstance();
        }
        return retrofit.create(ApiService.class);
    }
}