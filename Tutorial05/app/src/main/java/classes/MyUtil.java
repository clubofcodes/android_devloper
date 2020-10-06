package classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.json.JSONArray;

import java.net.InetAddress;

public class MyUtil {
    //*******************"Tutorial 10 (AsyncTask JSONArray & global URL)"*******************
    public static JSONArray jsonArray = null;
    public static final String URL_USERS = "https://jsonplaceholder.typicode.com/users";
    //*******************"Tutorial 10"*******************

    //*******************"Tutorial 09 (Global File variables)"*******************
    public static final String FILE_ASSETS = "data.json";
    public static final String FILE_INTERNAL = "testFile.txt";
    //*******************"Tutorial 09"*******************

    //*******************"For checking network connection"*******************
    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) return true;
        else return false;
    }
}
