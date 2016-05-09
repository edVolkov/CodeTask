package eduards.volkovs.codetask;

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
public class JSONDownloader {
    public static String downloadJSON(String downloadUrl) {
        String result = "";
        StringBuilder buffer = new StringBuilder();
        InputStream inputStream = null;
        URL url;
        HttpURLConnection urlConnection;

        try {
            url = new URL(downloadUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            int data = reader.read();

            while (data != -1) {
                char charRead = (char) data;
                buffer.append(charRead);
                data = reader.read();
            }

            result = buffer.toString();

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if(inputStream != null)
                    inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}
