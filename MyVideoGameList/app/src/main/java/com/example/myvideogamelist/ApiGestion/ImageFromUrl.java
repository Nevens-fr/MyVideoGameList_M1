package com.example.myvideogamelist.ApiGestion;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.URL;

public class ImageFromUrl extends AsyncTask<String, Void, Bitmap> {

    private String url = "https://media.rawg.io/media/screenshots/453/453ae178a4b0413b8c4687215266dbc9.jpg";
    ImageView bmImage;

    public ImageFromUrl(ImageView bmImage) {
        this.bmImage = bmImage;
    }

    protected Bitmap doInBackground(String... urls) {
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}
