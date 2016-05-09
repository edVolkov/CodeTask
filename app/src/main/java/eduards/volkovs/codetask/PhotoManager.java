package eduards.volkovs.codetask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
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
            if(cancelPotentialDownload(id,imageView)){
                PhotoDownloadTask photoDownloadTask = new PhotoDownloadTask(imageView, id);
                DrawableWithTaskReference downloadedDrawable = new DrawableWithTaskReference(photoDownloadTask);
                imageView.setImageDrawable(downloadedDrawable);
                photoDownloadTask.execute(url, id);
            }
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

    public class PhotoDownloadTask extends AsyncTask<String,Void,Bitmap> {

        private WeakReference<ImageView> imageViewReference;
        private String photoId;
        public PhotoDownloadTask(ImageView imageView, String photoId) {
            this.imageViewReference = new WeakReference<ImageView>(imageView);
            this.photoId = photoId;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = getBitmap(params[0]);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            displayPhoto(bitmap);
        }

        private Bitmap getBitmap(String url) {
            try {
                Bitmap bitmap = null;
                URL imageUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                InputStream is = conn.getInputStream();

                bitmap = BitmapFactory.decodeStream(is);

                conn.disconnect();

                return bitmap;

            } catch(Throwable e) {
                e.printStackTrace();
                return null;
            }
        }

        public void displayPhoto(Bitmap bitmap) {
            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();

                // Get PhotoDownloadTask reference associated with this ImageView
                PhotoDownloadTask photoDownloadTask = getPhotoDownloadTask(imageView);

                // Set a new bitmap only if this task is still associated with it
                if (this == photoDownloadTask) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }

        public String getPhotoId() {
            return photoId;
        }
    }

    // A drawable which has a reference to a PhotoDownloadTask
    public class DrawableWithTaskReference extends ColorDrawable {
        private final WeakReference<PhotoDownloadTask> photoDownloadTaskReference;

        public DrawableWithTaskReference(PhotoDownloadTask photoDownloadTask) {
            super(Color.WHITE);
            photoDownloadTaskReference =
                    new WeakReference<PhotoDownloadTask>(photoDownloadTask);
        }

        public PhotoDownloadTask getPhotoDownloadTask() {
            return photoDownloadTaskReference.get();
        }
    }

    private boolean cancelPotentialDownload(String id, ImageView imageView) {
        PhotoDownloadTask photoDownloadTask = getPhotoDownloadTask(imageView);

        if (photoDownloadTask != null) {
            String bitmapId = photoDownloadTask.getPhotoId();
            // If id does not exist or ids do not match the photoDownloadTask can be cancelled
            if ((bitmapId == null) || (!bitmapId.equals(id))) {
                Log.i("PhotoManager","Download cancelled");
                photoDownloadTask.cancel(true);
            } else {
                // A photo with the same id is already being downloaded
                return false;
            }
        }
        return true;
    }

    private PhotoDownloadTask getPhotoDownloadTask(ImageView imageView) {
        if (imageView != null) {
            Drawable drawable = imageView.getDrawable();
            if (drawable instanceof DrawableWithTaskReference) {
                DrawableWithTaskReference downloadedDrawable = (DrawableWithTaskReference)drawable;
                return downloadedDrawable.getPhotoDownloadTask();
            }
        }
        return null;
    }
}
