package eduards.volkovs.codetask;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by eduards.volkovs on 09/05/2016.
 */
public class ViewHolder {
    ImageView photo;

    public ViewHolder(View view) {
        photo = (ImageView) view.findViewById(R.id.photoInList);
    }

    public ImageView getPhoto() {
        return photo;
    }
}
