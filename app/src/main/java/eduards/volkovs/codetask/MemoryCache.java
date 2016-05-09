package eduards.volkovs.codetask;

import android.graphics.Bitmap;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by eduards.volkovs on 09/05/2016.
 */
public class MemoryCache {
    private Map<String, Bitmap> cache = Collections
            .synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    private long size = 0;

    private long limit = 1000000;

    public MemoryCache() {
        limit = Runtime.getRuntime().maxMemory() / 4;
    }

    public Bitmap get(String id) {
        try {
            if(!cache.containsKey(id))
                return null;

            return cache.get(id);

        } catch(NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        try{
            if(cache.containsKey(id))
                size -= getSizeInBytes(cache.get(id));

            cache.put(id, bitmap);
            size += getSizeInBytes(bitmap);
            checkSize();

        } catch(Throwable e) {
            e.printStackTrace();
        }
    }

    private void checkSize() {
        if(size > limit) {
            Iterator<Map.Entry<String, Bitmap>> iterator = cache.entrySet().iterator();

            while(iterator.hasNext()) {
                Map.Entry<String, Bitmap> entry = iterator.next();
                size -= getSizeInBytes(entry.getValue());
                iterator.remove();
                if(size <= limit)
                    break;
            }
        }
    }

    public void clear() {
        try {
            cache.clear();
            size = 0;
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if(bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
