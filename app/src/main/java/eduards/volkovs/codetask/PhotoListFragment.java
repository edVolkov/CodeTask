package eduards.volkovs.codetask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

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

        initializePhotoList();
    }

    public  void initializePhotoList() {
        photoList = (ListView) getActivity().findViewById(R.id.photoList);
        LazyAdapter lazyAdapter = new LazyAdapter(getActivity());
        photoList.setAdapter(lazyAdapter);
        photoList.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showPhotoDetailsFragment();
    }

    public void showPhotoDetailsFragment() {
        PhotoDetailsFragment photoDetailsFragment = new PhotoDetailsFragment();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fragmentContainer,photoDetailsFragment).addToBackStack("addedPhotoDetailsFragment");
        transaction.hide(this);
        transaction.commit();
    }
}
