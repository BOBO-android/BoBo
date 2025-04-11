package android.example.bobo.network;

import android.example.bobo.data.model.UploadResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageService {
    @Multipart
    @POST("api/v1/media/images/upload")
    Call<UploadResponse> uploadImage(
            @Header("Authorization") String token,
            @Part MultipartBody.Part file
    );
}
