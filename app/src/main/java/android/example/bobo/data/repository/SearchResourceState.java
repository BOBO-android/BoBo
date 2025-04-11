package android.example.bobo.data.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SearchResourceState<T> {
    @NonNull
    private final SearchDataStatus status;
    @Nullable
    private final T data;
    @Nullable
    private final String message;

    private SearchResourceState(@NonNull SearchDataStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> SearchResourceState<T> success(@Nullable T data) {
        return new SearchResourceState<>(SearchDataStatus.SUCCESS, data, null);
    }

    public static <T> SearchResourceState<T> error(String msg, @Nullable T data) {
        return new SearchResourceState<>(SearchDataStatus.ERROR, data, msg);
    }

    public static <T> SearchResourceState<T> loading(@Nullable T data) {
        return new SearchResourceState<>(SearchDataStatus.LOADING, data, null);
    }

    @NonNull
    public SearchDataStatus getStatus() {
        return status;
    }

    @Nullable
    public T getData() {
        return data;
    }

    @Nullable
    public String getMessage() {
        return message;
    }

    public enum SearchDataStatus {
        SUCCESS,
        ERROR,
        LOADING
    }
}