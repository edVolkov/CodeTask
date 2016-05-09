package eduards.volkovs.codetask;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by eduards.volkovs on 09/05/2016.
 */
public class PhotoManager {
    public void displayPhoto(String id, ImageView imageView) {
        int stubResourceId = R.drawable.ic_sort;
        imageView.setImageResource(stubResourceId);

        FlickrPhotoUrlDownloadTask urlDownloadTask = new FlickrPhotoUrlDownloadTask(imageView);
        urlDownloadTask.execute("https://api.flickr.com/services/rest/?method=flickr.photos.getSizes&api_key=a9eb4b87975f17d54c75eb7f2ed41a4e&photo_id="+ id +"&format=json&nojsoncallback=1",id);

    }

    public class FlickrPhotoUrlDownloadTask extends AsyncTask<String,Void,String> {
        ImageView imageView;
        String id;

        public FlickrPhotoUrlDownloadTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected String doInBackground(String... params) {
            String photoUrl;
            String downloadUrl = params[0];
            id = params[1];

            String result = JSONDownloader.downloadJSON(downloadUrl);
            photoUrl = getPhotoUrl(result);
            return photoUrl;
        }

        @Override
        protected void onPostExecute(String url) {
            super.onPostExecute(url);
            Log.i("PhotoManager",url);
        }

        public String getPhotoUrl(String result) {
            String photoUrl = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject sizes = jsonObject.getJSONObject("sizes");
                JSONArray size = sizes.getJSONArray("size");
                photoUrl = size.getJSONObject(5).getString("source");
                return photoUrl;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return photoUrl;
        }
    }
}
