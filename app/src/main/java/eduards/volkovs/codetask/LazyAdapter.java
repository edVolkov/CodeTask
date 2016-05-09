package eduards.volkovs.codetask;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by eduards.volkovs on 09/05/2016.
 */
public class LazyAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<String> photoIds;
    private PhotoManager photoManager;

    public LazyAdapter(Context context, ArrayList<String> photoIds) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.photoIds = photoIds;
        photoManager = new PhotoManager();
    }
    @Override
    public int getCount() {
        return photoIds.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if(convertView == null) {
            view = inflater.inflate(R.layout.photo_list_row,parent,false);
            viewHolder = initializeViewHolder(view);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        photoManager.displayPhoto(photoIds.get(position),viewHolder.getPhoto());

        return view;
    }

    public ViewHolder initializeViewHolder(View view){

        ViewHolder viewHolder = new ViewHolder(view);

        view.setTag(viewHolder);

        return viewHolder;
    }


}

