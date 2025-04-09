package android.example.bobo.utils;

import android.content.Context;
import android.example.bobo.R;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class ImageLoader {

    /**
     * Load an image from a URL into an ImageView
     *
     * @param context Context
     * @param imageUrl URL of the image to load
     * @param imageView Target ImageView
     */
    public static void loadImage(Context context, String imageUrl, ImageView imageView) {
        if (context == null || imageView == null) {
            return;
        }

        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder);

        Glide.with(context)
                .load(imageUrl)
                .apply(options)
                .into(imageView);
    }

    /**
     * Load an image from a resource ID into an ImageView
     *
     * @param context Context
     * @param resourceId Resource ID of the image to load
     * @param imageView Target ImageView
     */
    public static void loadImageResource(Context context, int resourceId, ImageView imageView) {
        if (context == null || imageView == null) {
            return;
        }

        Glide.with(context)
                .load(resourceId)
                .into(imageView);
    }
}
