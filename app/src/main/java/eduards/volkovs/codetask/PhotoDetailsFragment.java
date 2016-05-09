package eduards.volkovs.codetask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by eduards.volkovs on 09/05/2016.
 */
public class PhotoDetailsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.photo_details,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ImageView imageView = (ImageView) getActivity().findViewById(R.id.photoInDetailsFragment);
        byte[] byteArray = getArguments().getByteArray("bitmap");
        Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray,0, byteArray.length);
        imageView.setImageBitmap(bitmap);
    }
}
