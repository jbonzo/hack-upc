package hackupc.gatech.hackaccessibility.net;

import android.content.Context;
import android.util.Log;

/**
 * Created by michael on 7/22/16.
 */
public class DataTroveInstance {

    private static DataTrove Instance = null;

    public static DataTrove GetInstance() {
        if (!IsInitialized()) {
            //cry
            Log.e("DataTrove.GetTnstance", "MOTHERFUCKER DIDNT INITIALIZE THE ISNTANCE HOMIE!!");
        }

        return Instance;
    }

    public static void InitInstance(Context context) {
        Instance = new LocalStorageDataTrove(context);
    }

    public static boolean IsInitialized() {
        return Instance != null;
    }

}
