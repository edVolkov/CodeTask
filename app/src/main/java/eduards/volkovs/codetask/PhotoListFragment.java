package eduards.volkovs.codetask;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by eduards.volkovs on 09/05/2016.
 */
public class PhotoListFragment extends Fragment implements AdapterView.OnItemClickListener{
    private ListView photoList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_list,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Download photo ids once the Activity is created
        FlickrPhotoIdDownloadTask idDownloadTask = new FlickrPhotoIdDownloadTask();
        idDownloadTask.execute("https://api.flickr.com/services/rest/?&method=flickr.people.getPublicPhotos&api_key=a9eb4b87975f17d54c75eb7f2ed41a4e&user_id=8935325@N08&format=json&nojsoncallback=1';");
    }

    public class FlickrPhotoIdDownloadTask extends AsyncTask<String,Void,ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> photoIds = new ArrayList<String>();
            try {
                String downloadedJSON = JSONDownloader.downloadJSON(params[0]);
                photoIds = getPhotoIds(downloadedJSON);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return photoIds;
        }

        public ArrayList<String> getPhotoIds(String jsonData) throws JSONException {
            ArrayList<String> photoIds = new ArrayList<String>();
            JSONObject jsonFlickrApi = new JSONObject(jsonData);
            JSONObject photos = jsonFlickrApi.getJSONObject("photos");
            JSONArray photo = photos.getJSONArray("photo");
            for (int i = 0; i < photo.length(); i++) {
                String id = photo.getJSONObject(i).getString("id");
                photoIds.add(id);
            }
            return photoIds;
        }

        @Override
        protected void onPostExecute(ArrayList<String> photoIds) {
            super.onPostExecute(photoIds);
            Log.i("PhotoListFragment","Downloaded " + photoIds.size() + "ids");
            initializePhotoList(photoIds);
        }
    }



    public  void initializePhotoList(ArrayList<String> photoIds) {
        photoList = (ListView) getActivity().findViewById(R.id.photoList);
        LazyAdapter lazyAdapter = new LazyAdapter(getActivity(), photoIds);
        photoList.setAdapter(lazyAdapter);
        photoList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = getBundleWithBitmap(view);
        showPhotoDetailsFragment(bundle);
    }

    public Bundle getBundleWithBitmap(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.photoInList);

        Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] b = outputStream.toByteArray();

        Bundle bundle = new Bundle();
        bundle.putByteArray("bitmap",b);
        return bundle;
    }

    public void showPhotoDetailsFragment(Bundle bundle) {
        PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment();
        photoDetailsFragment.setArguments(bundle);
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragmentContainer,photoDetailsFragment).addToBackStack("addedPhotoDetailsFragment");
        transaction.hide(this);
        transaction.commit();
    }
}
