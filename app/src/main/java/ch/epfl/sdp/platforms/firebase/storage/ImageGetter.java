package ch.epfl.sdp.platforms.firebase.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.File;
import java.io.FileOutputStream;

public class ImageGetter {


    /**
     * Downloads an image and puts it into the imageView to display. Also saves it in cache
     * and loads it from there if possible
     * @param context
     * @param imageId
     * @param imageView
     */
    public static void getImage(@NonNull Context context, @NonNull String imageId, @NonNull ImageView imageView) {
        int imageStringHash = imageId.hashCode();
        String filename = Integer.toString(imageStringHash).replace('-', '1');
        File imageFile = new File(context.getCacheDir(), filename);

        final Bitmap[] bitmap = new Bitmap[1];

        if(1 == 0 && !imageFile.isDirectory()){
            bitmap[0] = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        } else {
            Glide.with(context).asBitmap().load(imageId).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    bitmap[0] = resource;
                }
            });

            saveInCache(imageFile, bitmap[0]);
        }

        imageView.setImageBitmap(bitmap[0]);
    }

    private static void saveInCache(File file, Bitmap bitmap){

        try(FileOutputStream fileOutputStream = new FileOutputStream(file)) {

            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
